import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class PositionFinderTest {


    @Test
    public void testNegativeCoordinates() {
        // בדיקת יצירת אובייקט עם ערכים שליליים
        PositionFinder position = new PositionFinder(-2, -7);
        assertEquals(-2, position.x, "x should be initialized to -2.");
        assertEquals(-7, position.y, "y should be initialized to -7.");
    }

    @Test
    public void testZeroCoordinates() {
        // בדיקת יצירת אובייקט עם ערכים אפסיים
        PositionFinder position = new PositionFinder(0, 0);
        assertEquals(0, position.x, "x should be initialized to 0.");
        assertEquals(0, position.y, "y should be initialized to 0.");
    }

    @Test
    public void testLargeCoordinates() {
        // בדיקת יצירת אובייקט עם ערכים גדולים
        PositionFinder position = new PositionFinder(100000, 200000);
        assertEquals(100000, position.x, "x should be initialized to 100000.");
        assertEquals(200000, position.y, "y should be initialized to 200000.");
    }

    @Test
    public void testSetCoordinates() {
        // בדיקת עדכון ערכי x ו-y לאחר יצירת האובייקט
        PositionFinder position = new PositionFinder(1, 1);
        position.x = 10;
        position.y = 20;
        assertEquals(10, position.x, "x should be updated to 10.");
        assertEquals(20, position.y, "y should be updated to 20.");
    }


}