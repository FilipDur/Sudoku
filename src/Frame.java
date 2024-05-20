import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

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
    private JButton startColorChangeButton;
    private JButton stopColorChangeButton;

    public Frame() {
        initializeFrame();
        initializeMenu();
        initializeComponents();
        initializeTimers();
        applyStyles();

        startNewGame(PuzzleType.NINE_BY_NINE, 26);
    }

    private void initializeFrame() {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("Sudoku");
        this.setExtendedState(JFrame.MAXIMIZED_BOTH); // Set to fullscreen
        this.setMinimumSize(new Dimension(800, 600));
    }

    private void initializeMenu() {
        JMenuBar menuBar = new JMenuBar();

        JMenu gameMenu = createGameMenu();
        JMenu colorMenu = createColorMenu();

        menuBar.add(gameMenu);
        menuBar.add(colorMenu);
        this.setJMenuBar(menuBar);
    }

    private JMenu createGameMenu() {
        JMenu gameMenu = new JMenu("Game");
        JMenu newGameMenu = new JMenu("New Game");

        newGameMenu.add(createNewGameMenuItem("6 By 6 Game", PuzzleType.SIX_BY_SIX, 30));
        newGameMenu.add(createNewGameMenuItem("9 By 9 Game", PuzzleType.NINE_BY_NINE, 26));
        newGameMenu.add(createNewGameMenuItem("12 By 12 Game", PuzzleType.TWELVE_BY_TWELVE, 16));
        newGameMenu.add(createNewGameMenuItem("16 By 16 Game", PuzzleType.SIXTEEN_BY_SIXTEEN, 12));

        gameMenu.add(newGameMenu);
        return gameMenu;
    }

    private JMenuItem createNewGameMenuItem(String title, PuzzleType puzzleType, int fontSize) {
        JMenuItem menuItem = new JMenuItem(title);
        menuItem.addActionListener(new NewGameAction(puzzleType, fontSize));
        return menuItem;
    }

    private JMenu createColorMenu() {
        JMenu colorMenu = new JMenu("Color");

        colorMenu.add(createColorMenuItem("Change Color", e -> changeColorScheme()));
        colorMenu.add(createColorMenuItem("Auto Change Color", e -> startAutoChangeColor()));
        colorMenu.add(createColorMenuItem("Stop Auto Change", e -> stopAutoChangeColor()));
        colorMenu.add(createColorMenuItem("Set Interval", e -> setAutoChangeInterval()));

        return colorMenu;
    }

    private JMenuItem createColorMenuItem(String title, ActionListener actionListener) {
        JMenuItem menuItem = new JMenuItem(title);
        menuItem.addActionListener(actionListener);
        return menuItem;
    }

    private void initializeComponents() {
        mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setPreferredSize(new Dimension(800, 600));

        buttonPanel = new JPanel(new GridLayout(0, 2, 10, 10)); // Structured grid layout
        buttonPanel.setPreferredSize(new Dimension(100, 500));

        sudokuPanel = new Panel();
        sudokuPanel.setPreferredSize(new Dimension(600, 600));

        timeDisplay = new JLabel("Time: 0:00");
        scoreDisplay = new JLabel("Score: 0");

        infoPanel = new JPanel();
        infoPanel.add(timeDisplay);
        infoPanel.add(scoreDisplay);


        startColorChangeButton = new JButton("Start");
        startColorChangeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sudokuPanel.startAutoChangeColor(1000);
            }
        });

        stopColorChangeButton = new JButton("Stop");
        stopColorChangeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sudokuPanel.stopAutoChangeBackgroundColor();
            }
        });


        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridheight = 2;
        gbc.fill = GridBagConstraints.BOTH;
        mainPanel.add(sudokuPanel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridheight = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(buttonPanel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(infoPanel, gbc);

        this.add(mainPanel);
    }

    private void initializeTimers() {
        elapsedTime = 0;
        score = 0;

        gameTimer = new Timer(1000, e -> updateGameTime());
        colorChangeTimer = new Timer(2000, e -> changeColorScheme());
    }

    private void updateGameTime() {
        elapsedTime++;
        int minutes = elapsedTime / 60;
        int seconds = elapsedTime % 60;
        timeDisplay.setText(String.format("Time: %d:%02d", minutes, seconds));
    }

    public void startNewGame(PuzzleType puzzleType, int fontSize) {
        Puzzle generatedPuzzle = new Generator().generateRandomSudoku(puzzleType);
        sudokuPanel.loadNewSudokuPuzzle(generatedPuzzle);
        sudokuPanel.updateFontSize(fontSize);

        buttonPanel.removeAll();


        for (String value : generatedPuzzle.getValidValues()) {
            JButton button = new JButton(value);
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    sudokuPanel.handleNumberButtonPress(value);
                }
            });
            button.setFont(new Font("Arial", Font.BOLD, 14));
            button.setBackground(new Color(230, 230, 250));
            buttonPanel.add(button);
        }


        buttonPanel.add(startColorChangeButton);
        buttonPanel.add(stopColorChangeButton);

        buttonPanel.revalidate();
        buttonPanel.repaint();

        resetGameTimer();
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

    private void applyStyles() {
        styleMainPanel();
        styleButtonPanel();
        styleSudokuPanel();
        styleInfoPanel();
        styleLabels();
        styleButtons();
    }

    private void styleMainPanel() {
        mainPanel.setBackground(new Color(240, 248, 255)); // Alice Blue
    }

    private void styleButtonPanel() {
        buttonPanel.setBackground(new Color(245, 245, 245)); // White Smoke
        Border line = new LineBorder(Color.GRAY, 1);
        Border margin = new EmptyBorder(5, 5, 5, 5);
        buttonPanel.setBorder(new CompoundBorder(line, margin));
    }

    private void styleSudokuPanel() {
        sudokuPanel.setBackground(Color.WHITE);
        Border line = new LineBorder(Color.GRAY, 1);
        Border margin = new EmptyBorder(5, 5, 5, 5);
        sudokuPanel.setBorder(new CompoundBorder(line, margin));
    }

    private void styleInfoPanel() {
        infoPanel.setBackground(new Color(240, 248, 255)); // Alice Blue
        Border line = new LineBorder(Color.GRAY, 1);
        Border margin = new EmptyBorder(5, 5, 5, 5);
        infoPanel.setBorder(new CompoundBorder(line, margin));
    }

    private void styleLabels() {
        Font labelFont = new Font("Arial", Font.BOLD, 16);
        timeDisplay.setFont(labelFont);
        scoreDisplay.setFont(labelFont);
    }

    private void styleButtons() {
        JButton[] buttons = {startColorChangeButton, stopColorChangeButton};
        for (JButton button : buttons) {
            button.setFont(new Font("Arial", Font.BOLD, 14));
            button.setBackground(new Color(230, 230, 250)); // Lavender
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
        }
    }
}
