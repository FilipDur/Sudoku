public enum PuzzleType {
    /**
     * Enum with different types of Sudoku puzzles.
     */
    SIX_BY_SIX(6, 6, 3, 2, new String[] {"1", "2", "3", "4", "5", "6"}, "6 By 6 Game"),
    NINE_BY_NINE(9, 9, 3, 3, new String[] {"1", "2", "3", "4", "5", "6", "7", "8", "9"}, "9 By 9 Game"),
    TWELVE_BY_TWELVE(12, 12, 4, 3, new String[] {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12"}, "12 By 12 Game"),
    SIXTEEN_BY_SIXTEEN(16, 16, 4, 4, new String[] {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16"}, "16 By 16 Game");

    private final int numberofRows;
    private final int numberofCols;
    private final int Width;
    private final int Height;
    private final String[] validValues;
    private final String description;

    /**
     * /**
     *      * Constructor for PuzzleType enum.
     * @param numberofRows
     * @param numberofCols
     * @param Width
     * @param Height
     * @param validValues
     * @param description
     */
    private PuzzleType(int numberofRows, int numberofCols, int Width, int Height, String[] validValues, String description) {
        this.numberofRows = numberofRows;
        this.numberofCols = numberofCols;
        this.Width = Width;
        this.Height = Height;
        this.validValues = validValues;
        this.description = description;
    }

    public int getNumberofRows() {
        return numberofRows;
    }

    public int getNumberofCols() {
        return numberofCols;
    }

    public int getWidth() {
        return Width;
    }

    public int getHeight() {
        return Height;
    }

    public String[] getValidValues() {
        return validValues;
    }

    @Override
    public String toString() {
        return description;
    }
}
