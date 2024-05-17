import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.JOptionPane;

public class Frame extends JFrame {

    private JPanel buttonPanel;
    private Panel sudokuPanel;
    private JLabel timeDisplay;
    private JLabel scoreDisplay;
    private Timer gameTimer;
    private Timer colorChangeTimer;
    private int elapsedTime; // in seconds
    private int score;
    private JPanel mainPanel;
    private JPanel infoPanel;

    public Frame() {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("Sudoku");
        this.setMinimumSize(new Dimension(800, 600));

        JMenuBar menuBar = new JMenuBar();
        JMenu gameMenu = new JMenu("Game");
        JMenu newGameMenu = new JMenu("New Game");
        JMenuItem sixBySixItem = new JMenuItem("6 By 6 Game");
        sixBySixItem.addActionListener(new NewGameAction(PuzzleType.SIX_BY_SIX, 30));
        JMenuItem nineByNineItem = new JMenuItem("9 By 9 Game");
        nineByNineItem.addActionListener(new NewGameAction(PuzzleType.NINE_BY_NINE, 26));
        JMenuItem twelveByTwelveItem = new JMenuItem("12 By 12 Game");
        twelveByTwelveItem.addActionListener(new NewGameAction(PuzzleType.TWELVE_BY_TWELVE, 20));
        JMenuItem sixteenBySixteenItem = new JMenuItem("16 By 16 Game");
        sixteenBySixteenItem.addActionListener(new NewGameAction(PuzzleType.SIXTEEN_BY_SIXTEEN, 16));

        newGameMenu.add(sixBySixItem);
        newGameMenu.add(nineByNineItem);
        newGameMenu.add(twelveByTwelveItem);
        newGameMenu.add(sixteenBySixteenItem);
        gameMenu.add(newGameMenu);

        JMenu colorMenu = new JMenu("Color");
        JMenuItem changeColorItem = new JMenuItem("Change Color");
        changeColorItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                changeColorScheme();
            }
        });

        JMenuItem autoChangeColorItem = new JMenuItem("Auto Change Color");
        autoChangeColorItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startAutoChangeColor();
            }
        });

        JMenuItem stopAutoChangeItem = new JMenuItem("Stop Auto Change");
        stopAutoChangeItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                stopAutoChangeColor();
            }
        });

        JMenuItem setColorIntervalItem = new JMenuItem("Set Interval");
        setColorIntervalItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setAutoChangeInterval();
            }
        });

        colorMenu.add(changeColorItem);
        colorMenu.add(autoChangeColorItem);
        colorMenu.add(stopAutoChangeItem);
        colorMenu.add(setColorIntervalItem);
        menuBar.add(gameMenu);
        menuBar.add(colorMenu);
        this.setJMenuBar(menuBar);

        mainPanel = new JPanel();
        mainPanel.setLayout(new FlowLayout());
        mainPanel.setPreferredSize(new Dimension(800, 600));

        buttonPanel = new JPanel();
        buttonPanel.setPreferredSize(new Dimension(90, 500));

        sudokuPanel = new Panel();

        timeDisplay = new JLabel("Time: 0:00");
        scoreDisplay = new JLabel("Score: 0");
        infoPanel = new JPanel();
        infoPanel.add(timeDisplay);
        infoPanel.add(scoreDisplay);

        mainPanel.add(sudokuPanel);
        mainPanel.add(buttonPanel);
        mainPanel.add(infoPanel);
        this.add(mainPanel);

        elapsedTime = 0;
        score = 0;

        gameTimer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                elapsedTime++;
                int minutes = elapsedTime / 60;
                int seconds = elapsedTime % 60;
                timeDisplay.setText(String.format("Time: %d:%02d", minutes, seconds));
            }
        });

        colorChangeTimer = new Timer(2000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                changeColorScheme();
            }
        });

        startNewGame(PuzzleType.NINE_BY_NINE, 26);
    }

    public void startNewGame(PuzzleType puzzleType, int fontSize) {
        Puzzle generatedPuzzle = new Generator().generateRandomSudoku(puzzleType);
        sudokuPanel.loadNewSudokuPuzzle(generatedPuzzle);
        sudokuPanel.updateFontSize(fontSize);
        buttonPanel.removeAll();
        for (String value : generatedPuzzle.getValidValues()) {
            JButton button = new JButton(value);
            button.setPreferredSize(new Dimension(40, 40));
            button.addActionListener(sudokuPanel.new NumActionListener());
            buttonPanel.add(button);
        }
        sudokuPanel.repaint();
        buttonPanel.revalidate();
        buttonPanel.repaint();
        startGameTimer();
    }

    public void startGameTimer() {
        elapsedTime = 0;
        gameTimer.start();
    }

    public void stopGameTimer() {
        gameTimer.stop();
    }

    public void resetGameTimer() {
        gameTimer.stop();
        elapsedTime = 0;
        timeDisplay.setText("Time: 0:00");
    }

    public void updateScore(int points) {
        score += points;
        scoreDisplay.setText("Score: " + score);
    }

    public void changeColorScheme() {
        Color newColor = new Color((int) (Math.random() * 0x1000000)); // Random color
        mainPanel.setBackground(newColor);
        buttonPanel.setBackground(newColor);
        sudokuPanel.setBackground(newColor);
        infoPanel.setBackground(newColor);
    }

    public void startAutoChangeColor() {
        colorChangeTimer.start();
    }

    public void stopAutoChangeColor() {
        colorChangeTimer.stop();
    }

    public void setAutoChangeInterval() {
        String intervalStr = JOptionPane.showInputDialog(this, "Enter interval in milliseconds:", "Set Interval", JOptionPane.PLAIN_MESSAGE);
        if (intervalStr != null) {
            try {
                int interval = Integer.parseInt(intervalStr);
                colorChangeTimer.setDelay(interval);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Invalid interval. Please enter a valid number.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private class NewGameAction implements ActionListener {

        private PuzzleType puzzleType;
        private int fontSize;

        public NewGameAction(PuzzleType puzzleType, int fontSize) {
            this.puzzleType = puzzleType;
            this.fontSize = fontSize;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            startNewGame(puzzleType, fontSize);
            resetGameTimer();
            startGameTimer();
        }
    }

    public static void main(String[] args) {
        Frame frame = new Frame();
        frame.pack();
        frame.setVisible(true);
    }
}
