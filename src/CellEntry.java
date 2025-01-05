public class CellEntry implements Index2D {
    private char x;
    private int y;

    public CellEntry(char x, int y) {
        this.x = Character.toUpperCase(x);
        this.y = y;
    }

    @Override
    public String toString() {
        if (!isValid()) {
            return "Invalid";
        }
        return String.valueOf(x) + y;
    }

    @Override
    public boolean isValid() {
        return (x >= 'A' && x <= 'Z') && (y >= 0 && y <= 99);
    }

    @Override
    public int getX() {
        if (!isValid()) {
            return Ex2Utils.ERR;
        }
        return x - 'A';
    }

    @Override
    public int getY() {
        if (!isValid()) {
            return Ex2Utils.ERR;
        }
        return y;
    }
}
