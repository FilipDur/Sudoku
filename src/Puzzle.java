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
        if (isValidValue(value) && ValidMove(row, col, value) && Changeble(row, col)) {
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

    public boolean isSlotAvailable(int row, int col) {
        return inRange(row, col) && this.board[row][col].equals("") && Changeble(row, col);
    }

    public boolean Changeble(int row, int col) {
        return this.mutable[row][col];
    }

    public String getValue(int row, int col) {
        return inRange(row, col) ? this.board[row][col] : "";
    }

    public String[][] getBoard() {
        return this.board;
    }

    private boolean isValidValue(String value) {
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
}
