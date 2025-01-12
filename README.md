# EX2 - Foundation of Object-Oriented and Recursion
## Ariel University, School of Computer Science (2025A)

### Project Overview
An implementation of a basic spreadsheet system focusing on object-oriented design principles and recursive operations. The project emphasizes the development of a 2D array of cells with various content types and formula processing capabilities.

### Cell Types and Content
1. **Basic Cell Types:**
   - String (Text)
   - Number (Double)
   - Formula

2. **Formula Structures:**
   - Simple number: `=number`
   - Parenthesized formula: `=(Formula)`
   - Compound formula: `=Formula op Formula` (where op ∈ {+,-,*,/})
   - Cell reference: `=cell` (e.g., A0, B1, C12)

3. **Valid Formula Examples:**
   - `=1`, `=1.2`, `=(0.2)`
   - `=1+2`, `=1+2*3`
   - `=(1+2)*((3))-1`
   - `=A1`, `=A2+3`, `=(2+A3)/A2`

4. **Invalid Forms (ERR_WRONG_FORM):**
   - `a`, `AB`, `@2`
   - `2+)`, `(3+1*2)-`
   - `=()`, `=5**`

### Key Implementation Requirements

1. **Cell Class Methods:**
   ```java
   boolean isNumber(String text)  // "1", "-1.1"
   boolean isText(String)         // "2a", "{2}", "hi"
   boolean isForm(String text)    // "=1", "=1+2*2"
   Double computeForm(String form) // "=1+2*2" → 5
   ```

2. **Spreadsheet Class Methods:**
   ```java
   // Basic Operations
   Spreadsheet(int x, int y)      // Constructor
   Cell get(int x, int y)         // Get cell
   void set(int x, int y, Cell c) // Set cell
   int width(), int height()      // Dimensions
   
   // Cell Reference Processing
   int xCell(String c)  // "F13" → 5 (A→0)
   int yCell(String c)  // "F13" → 13
   
   // Core Functionality
   String eval(int x, int y)    // Cell value
   String[][] evalAll()         // All cell values
   int[][] depth()             // Computational depth
   ```

### Special Considerations
1. **Cycle Detection:**
   - Self-reference cells (e.g., A0:A0) are invalid (ERR_CYCLE)
   - Circular references must be detected and handled

2. **Depth Calculation:**
   - Text/Number: depth = 0
   - Formula: depth = 1 + max(all dependencies)
   - Cyclic reference: depth = -1

3. **Testing Requirements:**
   - Comprehensive JUnit testing required
   - Full coverage of functionality
   - Error handling verification

### Technical Requirements
1. **Development Environment:**
   - IDE: IntelliJ IDEA
   - Version Control: GitHub (private repository)
   - Testing Framework: JUnit

2. **Project Structure:**
   - Must implement Sheet interface
   - Required classes: Ex2Sheet, SCell, CellEntry
   - No modification of provided interfaces

### Submission Requirements
- GitHub repository link
- Student ID
- Documentation
- Complete test suite

This project emphasizes clean code, robust error handling, and thorough testing while implementing fundamental programming concepts in a practical application.
