import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Ex2Sheet implements Sheet {
    private Cell[][] table;

    public Ex2Sheet(int x, int y) {
        table = new SCell[x][y];
        for (int i = 0; i < x; i++) {
            for (int j = 0; j < y; j++) {
                table[i][j] = new SCell("");
            }
        }
        eval();
    }

    public Ex2Sheet() {
        this(Ex2Utils.WIDTH, Ex2Utils.HEIGHT);
    }

    @Override
    public String value(int x, int y) {
        String ans = Ex2Utils.EMPTY_CELL;
        Cell c = get(x, y);
        if (c != null) {
            ans = c.toString();
        }
        return ans;
    }

    @Override
    public Cell get(int x, int y) {
        return table[x][y];
    }

    @Override
    public Cell get(String cords) {
        Index2D index = parseIndex(cords);
        if (index == null || !isIn(index.getX(), index.getY())) {
            return null;
        }
        return get(index.getX(), index.getY());
    }

    @Override
    public int width() {
        return table.length;
    }

    @Override
    public int height() {
        return table[0].length;
    }

    @Override
    public void set(int x, int y, String s) {
        Cell c = new SCell(s);
        table[x][y] = c;
    }

    @Override
    public void eval() {
        int[][] dd = depth();
        for (int i = 0; i < width(); i++) {
            for (int j = 0; j < height(); j++) {
                eval(i, j);
            }
        }
    }

    @Override
    public boolean isIn(int xx, int yy) {
        return xx >= 0 && xx < width() && yy >= 0 && yy < height();
    }

    @Override
    public int[][] depth() {
        int[][] ans = new int[width()][height()];
        for (int i = 0; i < width(); i++) {
            for (int j = 0; j < height(); j++) {
                ans[i][j] = computeDepth(i, j);
            }
        }
        return ans;
    }

    @Override
    public void load(String fileName) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            reader.readLine(); // ignore the first line
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",", 3);
                if (parts.length >= 3) {
                    int x = Integer.parseInt(parts[0]);
                    int y = Integer.parseInt(parts[1]);
                    String data = parts[2];
                    if (isIn(x, y)) {
                        set(x, y, data);
                    }
                }
            }
        }
    }

    @Override
    public void save(String fileName) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            writer.write("I2CS ArielU: SpreadSheet (Ex2) assignment - this line should be ignored in the load method\n");
            for (int i = 0; i < width(); i++) {
                for (int j = 0; j < height(); j++) {
                    Cell cell = table[i][j];
                    if (cell != null) {
                        writer.write(i + "," + j + "," + cell.getData() + "\n");
                    }
                }
            }
        }
    }

    @Override
    public String eval(int x, int y) {
        Cell cell = get(x, y);
        if (cell == null) {
            return Ex2Utils.EMPTY_CELL;
        }
        return evalCell(cell);
    }

    private String evalCell(Cell cell) {
        switch (cell.getType()) {
            case Ex2Utils.NUMBER:
                return cell.getData();
            case Ex2Utils.TEXT:
                return cell.getData();
            case Ex2Utils.FORM:
                return String.valueOf(((SCell) cell).computeForm(cell.getData())); // שימוש במתודת computeForm
            default:
                return "ERR";
        }
    }

    private int computeDepth(int x, int y) {
        Cell cell = get(x, y);
        if (cell == null) {
            return 0;
        }
        switch (cell.getType()) {
            case Ex2Utils.NUMBER:
            case Ex2Utils.TEXT:
                return 0;
            case Ex2Utils.FORM:
                // חישוב עומק התלות עבור נוסחה
                return 1; // זהו ערך דוגמה, יש לשנות בהתאם לצורך
            default:
                return -1; // במקרה של שגיאה
        }
    }

    private Index2D parseIndex(String entry) {
        if (entry == null || entry.length() < 2) {
            return null;
        }
        try {
            int row = Integer.parseInt(entry.substring(1));
            int col = Character.toUpperCase(entry.charAt(0)) - 'A';

            if (col < 0 || col >= Ex2Utils.WIDTH || row < 0 || row >= Ex2Utils.HEIGHT) {
                throw new IllegalArgumentException("Invalid cell coordinates: " + entry);

            }

            return new Index2D(col, row);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid cell format: " + entry, e);
        }
    }
}
