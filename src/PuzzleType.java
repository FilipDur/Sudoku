public enum PuzzleType {
    SIX_BY_SIX(6, 6, 3, 2, new String[] {"1", "2", "3", "4", "5", "6"}, "6 By 6 Game"),
    NINE_BY_NINE(9, 9, 3, 3, new String[] {"1", "2", "3", "4", "5", "6", "7", "8", "9"}, "9 By 9 Game"),
    TWELVE_BY_TWELVE(12, 12, 4, 3, new String[] {"1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C"}, "12 By 12 Game"),
    SIXTEEN_BY_SIXTEEN(16, 16, 4, 4, new String[] {"1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F", "G"}, "16 By 16 Game");

    private final int numRows;
    private final int numCols;
    private final int boxWidth;
    private final int boxHeight;
    private final String[] validValues;
    private final String description;

    private PuzzleType(int numRows, int numCols, int boxWidth, int boxHeight, String[] validValues, String description) {
        this.numRows = numRows;
        this.numCols = numCols;
        this.boxWidth = boxWidth;
        this.boxHeight = boxHeight;
        this.validValues = validValues;
        this.description = description;
    }

    public int getNumRows() {
        return numRows;
    }

    public int getNumCols() {
        return numCols;
    }

    public int getBoxWidth() {
        return boxWidth;
    }

    public int getBoxHeight() {
        return boxHeight;
    }

    public String[] getValidValues() {
        return validValues;
    }

    @Override
    public String toString() {
        return description;
    }
}
