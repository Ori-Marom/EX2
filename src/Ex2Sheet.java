import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Ex2Sheet  implements Sheet {
    private Cell[][] grid;

    public Ex2Sheet(int rows, int cols) {
        grid = new SCell[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                grid[i][j] = new SCell("");
            }
        }
        eval();
    }

    public Ex2Sheet() {
        this(Ex2Utils.WIDTH, Ex2Utils.HEIGHT);
    }

    @Override
    public String value(int row, int col) {
        return eval(row, col);
    }

    @Override
    public Cell get(int row, int col) {
        return grid[row][col];
    }

    @Override
    public Cell get(String position) {
        PositionFinder pos = parsePosition(position);
        return grid[pos.x][pos.y];
    }

    @Override
    public int width() {
        return grid.length;
    }

    @Override
    public int height() {
        return grid[0].length;
    }

    @Override
    public void set(int row, int col, String content) {
        grid[row][col] = new SCell(content);
    }

    @Override
    public void eval() {
        // Placeholder for evaluation logic.
    }

    @Override
    public boolean isIn(int row, int col) {
        return row >= 0 && row < width() && col >= 0 && col < height();
    }

    @Override
    public int[][] depth() {
        return new int[width()][height()];
    }

    @Override
    public void load(String fileName) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get(fileName));
        for (int row = 0; row < width(); row++) {
            for (int col = 0; col < height(); col++) {
                grid[row][col].setData("");
            }
        }

        for (int i = 1; i < lines.size(); i++) {
            String[] parts = lines.get(i).split(",");
            int row = Integer.parseInt(parts[0]);
            int col = Integer.parseInt(parts[1]);
            grid[row][col].setData(parts[2]);
        }
    }

    @Override
    public void save(String fileName) throws IOException {
        StringBuilder output = new StringBuilder("I2CS ArielU: SpreadSheet (Ex2) assignment\n");

        for (int col = 0; col < height(); col++) {
            for (int row = 0; row < width(); row++) {
                SCell cell = (SCell) grid[row][col];
                String content = cell.getData();
                if (!content.isEmpty()) {
                    output.append(row).append(",").append(col).append(",").append(content).append("\n");
                }
            }
        }

        try (FileWriter writer = new FileWriter(fileName)) {
            writer.write(output.toString());
        }
    }

    @Override
    public String eval(int row, int col) {
        Cell cell = grid[row][col];
        String data = cell.getData();
        String result = evaluateExpression(data, List.of(new PositionFinder(row, col)));

        if (Objects.equals(result, Ex2Utils.ERR_FORM)) {
            cell.setType(Ex2Utils.ERR_FORM_FORMAT);
        } else if (Objects.equals(result, Ex2Utils.ERR_CYCLE)) {
            cell.setType(Ex2Utils.ERR_CYCLE_FORM);
        } else if (parseDouble(result) != -1 && !data.startsWith("=")) {
            cell.setType(Ex2Utils.NUMBER);
        } else if (parseDouble(result) != -1) {
            cell.setType(Ex2Utils.FORM);
        } else {
            cell.setType(Ex2Utils.TEXT);
        }

        return result;
    }

    private String evaluateExpression(String expr, List<PositionFinder> history) {
        if (expr.startsWith("=")) {
            return evaluateHelper(expr.substring(1).replaceAll(" ", ""), history);
        }

        double value = parseDouble(expr);
        return value != -1 ? String.valueOf(value) : expr;
    }

    private String evaluateHelper(String expr, List<PositionFinder> history) {
        while (expr.startsWith("(") && expr.endsWith(")")) {
            expr = expr.substring(1, expr.length() - 1);
        }

        int operatorIndex = findMainOperator(expr);
        if (operatorIndex == -1) {
            return handleSingleOperand(expr, history);
        }

        String left = expr.substring(0, operatorIndex);
        String right = expr.substring(operatorIndex + 1);
        char operator = expr.charAt(operatorIndex);

        String leftResult = evaluateHelper(left, history);
        String rightResult = evaluateHelper(right, history);

        if (leftResult.equals(Ex2Utils.ERR_FORM) || rightResult.equals(Ex2Utils.ERR_FORM)) {
            return Ex2Utils.ERR_FORM;
        }
        if (leftResult.equals(Ex2Utils.ERR_CYCLE) || rightResult.equals(Ex2Utils.ERR_CYCLE)) {
            return Ex2Utils.ERR_CYCLE;
        }

        double leftValue = Double.parseDouble(leftResult);
        double rightValue = Double.parseDouble(rightResult);

        return String.valueOf(performOperation(leftValue, rightValue, operator));
    }

    private String handleSingleOperand(String expr, List<PositionFinder> history) {
        double value = parseDouble(expr);
        if (value != -1) {
            return String.valueOf(value);
        }

        PositionFinder pos = parsePosition(expr);
        if (pos.x == -1 || !isIn(pos.x, pos.y)) {
            return Ex2Utils.ERR_FORM;
        }

        if (history.contains(pos)) {
            return Ex2Utils.ERR_CYCLE;
        }

        List<PositionFinder> newHistory = new ArrayList<>(history);
        newHistory.add(pos);
        return evaluateExpression(grid[pos.x][pos.y].getData(), newHistory);
    }

    private int findMainOperator(String expr) {
        int depth = 0;
        int mainIndex = -1;

        for (int i = 0; i < expr.length(); i++) {
            char ch = expr.charAt(i);
            if (ch == '(') {
                depth++;
            } else if (ch == ')') {
                depth--;
            } else if (isOperator(ch) && depth == 0) {
                mainIndex = i;
                break;
            }
        }

        return mainIndex;
    }

    private boolean isOperator(char ch) {
        return ch == '+' || ch == '-' || ch == '*' || ch == '/';
    }

    private PositionFinder parsePosition(String position) {
        if (position.isEmpty()) {
            return new PositionFinder(-1, -1);
        }

        char columnChar = position.charAt(0);
        int column = Character.isUpperCase(columnChar) ? columnChar - 'A' : columnChar - 'a';

        int row = parseInt(position.substring(1));
        return (column >= 0 && row != -1) ? new PositionFinder(column, row) : new PositionFinder(-1, -1);
    }

    private double performOperation(double a, double b, char op) {
        switch (op) {
            case '+': return a + b;
            case '-': return a - b;
            case '*': return a * b;
            case '/': return b != 0 ? a / b : Double.NaN;
            default: return Double.NaN;
        }
    }

    private double parseDouble(String str) {
        try {
            return Double.parseDouble(str);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private int parseInt(String str) {
        try {
            return Integer.parseInt(str);
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}
