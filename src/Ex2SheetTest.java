import org.junit.jupiter.api.Test;
import java.io.IOException;
import static org.junit.jupiter.api.Assertions.*;

public class Ex2SheetTest {

    @Test
    public void testInitialValues() {
        Ex2Sheet sheet = new Ex2Sheet(3, 3);
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                assertEquals("", sheet.value(i, j), "Initial cell value should be empty");
            }
        }
    }

    @Test
    public void testSetAndGetValues() {
        Ex2Sheet sheet = new Ex2Sheet(2, 2);
        sheet.set(0, 0, "42");


        sheet.set(1, 1, "Hello");
        assertEquals("Hello", sheet.value(1, 1), "Value in cell (1,1) should be Hello");
    }



    @Test
    public void testInvalidFormula() {
        Ex2Sheet sheet = new Ex2Sheet(2, 2);
        sheet.set(0, 0, "=A1+A3");
        assertEquals(Ex2Utils.ERR_FORM, sheet.value(0, 0), "Invalid formula should return error");
    }

    @Test
    public void testLoadAndSave() throws IOException {
        Ex2Sheet sheet = new Ex2Sheet(3, 3);
        sheet.set(0, 0, "123");
        sheet.set(1, 1, "=A1*2");
        sheet.set(2, 2, "Hello World");

        String fileName = "testSheet.csv";
        sheet.save(fileName);

        Ex2Sheet loadedSheet = new Ex2Sheet(3, 3);
        loadedSheet.load(fileName);



        assertEquals("Hello World", loadedSheet.value(2, 2), "Loaded value in (2,2) should match saved value");
    }

    @Test
    public void testIsInBounds() {
        Ex2Sheet sheet = new Ex2Sheet(3, 3);
        assertTrue(sheet.isIn(0, 0), "Cell (0,0) should be in bounds");
        assertFalse(sheet.isIn(-1, 0), "Cell (-1,0) should be out of bounds");
        assertFalse(sheet.isIn(3, 3), "Cell (3,3) should be out of bounds");
    }

    @Test
    public void testDepthArray() {
        Ex2Sheet sheet = new Ex2Sheet(3, 3);
        sheet.set(0, 0, "=B1");
        sheet.set(1, 0, "=C1");

        int[][] depth = sheet.depth();
        assertEquals(0, depth[0][0], "Initial depth of (0,0) should be 0");
        assertEquals(0, depth[1][0], "Initial depth of (1,0) should be 0");
    }

    @Test
    public void testEmptySheet() {
        Ex2Sheet sheet = new Ex2Sheet();
        assertEquals("", sheet.value(0, 0), "Empty sheet cells should be empty by default");
    }
}

