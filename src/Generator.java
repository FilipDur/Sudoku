import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Generator {
    /**
     * Generates a random Sudoku puzzle of the specified type.
     * It creates a new puzzle, fills it with random values in the first column,
     * solves the puzzle, and retains a portion of the solution to make it playable.
     *
     * @param puzzleType
     * @return
     */
    public Puzzle generateRandomSudoku(PuzzleType puzzleType) {
        Puzzle puzzle = new Puzzle(puzzleType.getNumberofRows(), puzzleType.getNumberofCols(), puzzleType.getWidth(), puzzleType.getHeight(), puzzleType.getValidValues());
        Puzzle puzzleCopy = new Puzzle(puzzle);

        Random randomGenerator = new Random();

        List<String> unusedValidValues = new ArrayList<>(Arrays.asList(puzzleCopy.getValidValues()));
        for (int row = 0; row < puzzleCopy.getNumRows(); row++) {
            int randomValueIndex = randomGenerator.nextInt(unusedValidValues.size());
            puzzleCopy.makeMove(row, 0, unusedValidValues.get(randomValueIndex), true);
            unusedValidValues.remove(randomValueIndex);
        }

        solveSudoku(0, 0, puzzleCopy);

        int numberOfValuesToRetain = (int) (0.22222 * (puzzleCopy.getNumRows() * puzzleCopy.getNumRows()));

        for (int i = 0; i < numberOfValuesToRetain; ) {
            int randomRow = randomGenerator.nextInt(puzzle.getNumRows());
            int randomColumn = randomGenerator.nextInt(puzzle.getNumCols());

            if (puzzle.EmptySlot(randomRow, randomColumn)) {
                puzzle.makeMove(randomRow, randomColumn, puzzleCopy.Value(randomRow, randomColumn), false);
                i++;
            }
        }

        return puzzle;
    }
    /**
     * Recursively solves a Sudoku puzzle.
     * It starts solving from the specified row and column.
     *
     * @param row    T
     * @param col
     * @param puzzle
     * @return
     */
    private boolean solveSudoku(int row, int col, Puzzle puzzle) {

        if (!puzzle.inRange(row, col)) {
            return false;
        }

        if (puzzle.EmptySlot(row, col)) {
            for (String value : puzzle.getValidValues()) {

                if (!puzzle.Row(row, value) && !puzzle.Col(col, value) && !puzzle.BoxNum(row, col, value)) {
                    puzzle.makeMove(row, col, value, true);

                    if (puzzle.BoardFull()) {
                        return true;
                    }

                    if (row == puzzle.getNumRows() - 1) {
                        if (solveSudoku(0, col + 1, puzzle)) {
                            return true;
                        }
                    } else {
                        if (solveSudoku(row + 1, col, puzzle)) {
                            return true;
                        }
                    }
                }
            }
        } else {
            if (row == puzzle.getNumRows() - 1) {
                return solveSudoku(0, col + 1, puzzle);
            } else {
                return solveSudoku(row + 1, col, puzzle);
            }
        }

        puzzle.makeMove(row, col, "", true);

        return false;
    }
}
