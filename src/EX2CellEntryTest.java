import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class EX2CellEntryTest {

    @Test
    public void testIsValid() {
        CellEntry cell = new CellEntry();
        assertFalse(cell.isValid(), "The cell should always return false for isValid.");
    }

    @Test
    public void testGetX() {
        CellEntry cell = new CellEntry();
        assertEquals(Ex2Utils.ERR, cell.getX(), "The cell should always return Ex2Utils.ERR for getX.");
    }

    @Test
    public void testGetY() {
        CellEntry cell = new CellEntry();
        assertEquals(Ex2Utils.ERR, cell.getY(), "The cell should always return Ex2Utils.ERR for getY.");
    }
}
