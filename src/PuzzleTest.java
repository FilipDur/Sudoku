import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class PuzzleTest {

    private Puzzle puzzle;

    @Before
    public void setUp() {

        String [][] board = new String [][] {
                {"0","0","8","3","4","2","9","0","0"},
                {"0","0","9","0","0","0","7","0","0"},
                {"4","0","0","0","0","0","0","0","3"},
                {"0","0","6","4","7","3","2","0","0"},
                {"0","3","0","0","0","0","0","1","0"},
                {"0","0","2","8","5","1","6","0","0"},
                {"7","0","0","0","0","0","0","0","8"},
                {"0","0","4","0","0","0","1","0","0"},
                {"0","0","3","6","9","7","5","0","0"}
        };
        puzzle = new PuzzleTesting(board);
    }

    @Test
    public void testRow() {
        Assert.assertTrue(puzzle.Row(0, "9"));
        Assert.assertTrue(puzzle.Row(1, "7"));
        Assert.assertFalse(puzzle.Row(8, "1"));
    }

    @Test
    public void testCol() {
        Assert.assertTrue(puzzle.Col(0, "4"));
        Assert.assertTrue(puzzle.Col(5, "2"));
        Assert.assertFalse(puzzle.Col(8, "1"));
    }

    @Test
    public void testBoxNum() {
        Assert.assertTrue(puzzle.BoxNum(6, 1, "4"));
        Assert.assertFalse(puzzle.BoxNum(4, 4, "2"));
        Assert.assertTrue(puzzle.BoxNum(4, 4, "8"));
    }
    @Test
    public void testMakeMove_ValidMove() {
        puzzle.makeMove(0, 2, "4", true);
        Assert.assertEquals("4", puzzle.getBoard()[0][2]);
    }

    @Test
    public void testMakeMove_InvalidMove() {
        puzzle.makeMove(0, 2, "5", true);
        Assert.assertNotEquals("5", puzzle.getBoard()[0][2]);
    }

    private class PuzzleTesting extends Puzzle {
        public PuzzleTesting(String [][] board) {
            super(9,9,3,3,new String [] {"1","2","3","4","5","6","7","8","9"});
            this.board = board;
        }
    }
}

