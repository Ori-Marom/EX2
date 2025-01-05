

public class SimpleIndex2D implements Index2D {
    private char x;
    private int y;

    public SimpleIndex2D(char x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public int getX() {
        try {
            char upperC = Character.toUpperCase(this.x);
            return upperC - 'A';
        } catch (Exception e) {
            return Ex2Utils.ERR;
        }
    }

    @Override
    public int getY() {
        return y;
    }

    @Override
    public String toString() {
        char column = (char) ('A' + x);
        return column + Integer.toString(y);
    }

    @Override
    public boolean isValid() {
        char column = (char) ('A' + x);
        return column >= 'A' && column <= 'Z' && y >= 0 && y < 100;
    }
}
