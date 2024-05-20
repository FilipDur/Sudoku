import java.util.Arrays;

public class Puzzle {

    protected String[][] board;
    protected boolean[][] mutable;
    private final int numRows;
    private final int numCols;
    private final int boxWidth;
    private final int boxHeight;
    private final String[] validValues;

    public Puzzle(int rows, int cols, int boxWidth, int boxHeight, String[] validValues) {
        this.numRows = rows;
        this.numCols = cols;
        this.boxWidth = boxWidth;
        this.boxHeight = boxHeight;
        this.validValues = validValues;
        this.board = new String[numRows][numCols];
        this.mutable = new boolean[numRows][numCols];
        initializeBoard();
        initializeMutableSlots();
    }

    public Puzzle(Puzzle puzzle) {
        this.numRows = puzzle.numRows;
        this.numCols = puzzle.numCols;
        this.boxWidth = puzzle.boxWidth;
        this.boxHeight = puzzle.boxHeight;
        this.validValues = puzzle.validValues;
        this.board = Arrays.stream(puzzle.board).map(String[]::clone).toArray(String[][]::new);
        this.mutable = Arrays.stream(puzzle.mutable).map(boolean[]::clone).toArray(boolean[][]::new);
    }

    public int getNumRows() {
        return this.numRows;
    }

    public int getNumCols() {
        return this.numCols;
    }

    public int getBoxWidth() {
        return this.boxWidth;
    }

    public int getBoxHeight() {
        return this.boxHeight;
    }

    public String[] getValidValues() {
        return this.validValues;
    }

    public void makeMove(int row, int col, String value, boolean isMutable) {
        if (RightValue(value) && ValidMove(row, col, value) && Mutable(row, col)) {
            this.board[row][col] = value;
            this.mutable[row][col] = isMutable;
        }
    }

    public boolean ValidMove(int row, int col, String value) {
        return inRange(row, col) && !Col(col, value) && !Row(row, value) && !BoxNum(row, col, value);
    }

    public boolean Col(int col, String value) {
        return col < this.numCols && Arrays.stream(this.board).anyMatch(row -> row[col].equals(value));
    }

    public boolean Row(int row, String value) {
        return row < this.numRows && Arrays.stream(this.board[row]).anyMatch(cell -> cell.equals(value));
    }

    public boolean BoxNum(int row, int col, String value) {
        if (inRange(row, col)) {
            int boxRow = row / this.boxHeight;
            int boxCol = col / this.boxWidth;

            int startingRow = boxRow * this.boxHeight;
            int startingCol = boxCol * this.boxWidth;

            return Arrays.stream(board, startingRow, startingRow + this.boxHeight)
                    .flatMap(rowArr -> Arrays.stream(rowArr, startingCol, startingCol + this.boxWidth))
                    .anyMatch(cell -> cell.equals(value));
        }
        return false;
    }

    public boolean EmptySlot(int row, int col) {
        return inRange(row, col) && this.board[row][col].equals("") && Mutable(row, col);
    }

    public boolean Mutable(int row, int col) {
        return this.mutable[row][col];
    }

    public String Value(int row, int col) {
        return inRange(row, col) ? this.board[row][col] : "";
    }

    public String[][] getBoard() {
        return this.board;
    }

    private boolean RightValue(String value) {
        return Arrays.stream(this.validValues).anyMatch(str -> str.equals(value));
    }

    public boolean inRange(int row, int col) {
        return row < this.numRows && col < this.numCols && row >= 0 && col >= 0;
    }

    public boolean BoardFull() {
        return Arrays.stream(this.board).allMatch(row -> Arrays.stream(row).noneMatch(String::isEmpty));
    }

    public void clearSlot(int row, int col) {
        if (inRange(row, col))
            this.board[row][col] = "";
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder("Game Board:\n");
        for (String[] row : this.board) {
            for (String cell : row) {
                str.append(cell).append(" ");
            }
            str.append("\n");
        }
        return str.toString() + "\n";
    }

    private void initializeBoard() {
        for (int row = 0; row < this.numRows; row++) {
            Arrays.fill(this.board[row], "");
        }
    }

    private void initializeMutableSlots() {
        for (int row = 0; row < this.numRows; row++) {
            Arrays.fill(this.mutable[row], true);
        }
    }

    public static Puzzle generateSolvedPuzzle(int numRows, int numCols, int boxWidth, int boxHeight, String[] validValues) {
        Puzzle puzzle = new Puzzle(numRows, numCols, boxWidth, boxHeight, validValues);
        solvePuzzle(puzzle, 0, 0);
        return puzzle;
    }

    private static boolean solvePuzzle(Puzzle puzzle, int row, int col) {
        if (row == puzzle.getNumRows()) {
            row = 0;
            if (++col == puzzle.getNumCols()) {
                return true;
            }
        }
        if (puzzle.EmptySlot(row, col)) {
            for (String value : puzzle.getValidValues()) {
                if (puzzle.ValidMove(row, col, value)) {
                    puzzle.makeMove(row, col, value, true);
                    if (solvePuzzle(puzzle, row + 1, col)) {
                        return true;
                    }
                    puzzle.clearSlot(row, col);
                }
            }
            return false;
        }
        return solvePuzzle(puzzle, row + 1, col);
    }

    public static Puzzle generateRandomPuzzle(int numRows, int numCols, int boxWidth, int boxHeight, String[] validValues, int numEmptyCells) {
        Puzzle solvedPuzzle = generateSolvedPuzzle(numRows, numCols, boxWidth, boxHeight, validValues);
        Puzzle randomPuzzle = new Puzzle(solvedPuzzle);
        int remainingEmptyCells = numEmptyCells;
        while (remainingEmptyCells > 0) {
            int row = (int) (Math.random() * numRows);
            int col = (int) (Math.random() * numCols);
            if (randomPuzzle.Mutable(row, col)) {
                randomPuzzle.clearSlot(row, col);
                remainingEmptyCells--;
            }
        }
        return randomPuzzle;
    }

    public boolean isSolved() {
        for (String[] row : this.board) {
            for (String cell : row) {
                if (cell.isEmpty()) {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean isValid() {
        for (int row = 0; row < numRows; row++) {
            for (int col = 0; col < numCols; col++) {
                if (!EmptySlot(row, col) && !ValidMove(row, col, Value(row, col))) {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean isMutable(int row, int col) {
        return Mutable(row, col);
    }

    public void setMutable(int row, int col, boolean isMutable) {
        mutable[row][col] = isMutable;
    }

    public void setCell(int row, int col, String value) {
        if (inRange(row, col) && RightValue(value)) {
            board[row][col] = value;
        }
    }


}

