import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.io.File;
import java.io.IOException;

public class Ex2SheetTest {

    @Test
    public void testLoadAndSave() throws IOException {
        Ex2Sheet sheet = new Ex2Sheet(5, 5);
        sheet.set(0, 0, "5");
        sheet.set(1, 1, "=A1+2");

        // Save to a temporary file
        String tempFile = "testSheet.csv";
        sheet.save(tempFile);

        // Create a new sheet and load from the file
        Ex2Sheet loadedSheet = new Ex2Sheet(5, 5);
        loadedSheet.load(tempFile);

        // Check if the loaded values are correct
        assertEquals("5", loadedSheet.value(0, 0));
        assertEquals("=A1+2", loadedSheet.value(1, 1));

        // Clean up the temporary file
        new File(tempFile).delete();
    }

    @Test
    public void testInvalidCellName() {
        Ex2Sheet sheet = new Ex2Sheet(5, 5);
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            sheet.set(0, 0, "InvalidCellName");
        });
        assertEquals("Invalid cell coordinates", exception.getMessage());
    }

    @Test
    public void testEval() {
        Ex2Sheet sheet = new Ex2Sheet(5, 5);
        sheet.set(0, 0, "10");
        sheet.set(1, 0, "20");
        sheet.set(2, 0, "=A1+A2");

        assertEquals("10", sheet.eval(0, 0));
        assertEquals("20", sheet.eval(1, 0));
        assertEquals("30.0", sheet.eval(2, 0)); // Evaluating the formula
    }

    @Test
    public void testLoadInvalidData() {
        Ex2Sheet sheet = new Ex2Sheet(5, 5);
        Exception exception = assertThrows(IOException.class, () -> {
            sheet.load("nonexistentfile.csv");
        });
        assertTrue(exception.getMessage().contains("No such file or directory"));
    }
}