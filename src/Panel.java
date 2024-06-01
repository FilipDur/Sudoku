import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import javax.swing.event.MouseInputAdapter;
import java.awt.font.FontRenderContext;

public class Panel extends JPanel {

    private Puzzle currentPuzzle;
    private int selectedCol;
    private int selectedRow;
    private int panelWidth;
    private int panelHeight;
    private int textSize;
    private Color bgColor;
    private Timer autoColorTimer;
    private JPanel topPanel = new JPanel();
    /**
     * Constructs a new Panel object with default settings.
     * Initializes the panel size, mouse listener, current puzzle.
     */
    public Panel() {
        this.setPreferredSize(new Dimension(540, 450));
        this.addMouseListener(new SudokuPanelMouseAdapter());
        this.currentPuzzle = Puzzle.generateRandomPuzzle(9, 9, 3, 3, new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9"}, 40);
        selectedCol = -1;
        selectedRow = -1;
        panelWidth = 0;
        panelHeight = 0;
        textSize = 26;
        bgColor = Color.WHITE;


        startAutoChangeColor(1000);


        JButton startButton = new JButton("Start Color Change");
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startAutoChangeColor(1000);
            }
        });

        JButton stopButton = new JButton("Stop Color Change");
        stopButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                stopAutoChangeBackgroundColor();
            }
        });

        topPanel.add(startButton);
        topPanel.add(stopButton);
    }

    public void loadNewSudokuPuzzle(Puzzle puzzle) {
        this.currentPuzzle = puzzle;
        repaint();
    }

    public void updateFontSize(int fontSize) {
        this.textSize = fontSize;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawBackground(g);
        drawGrid(g);
        drawNumbers(g);
        highlightSelectedCell(g);
    }

    private void drawBackground(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(bgColor);
        panelWidth = (this.getWidth() / currentPuzzle.getNumCols()) * currentPuzzle.getNumCols();
        panelHeight = (this.getHeight() / currentPuzzle.getNumRows()) * currentPuzzle.getNumRows();
        g2d.fillRect(0, 0, panelWidth, panelHeight);
    }
    /**
     * drawGrid was created by using youtube video that's been given credit in documentation.
     * @param g
     */
    private void drawGrid(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        int slotWidth = this.getWidth() / currentPuzzle.getNumCols();
        int slotHeight = this.getHeight() / currentPuzzle.getNumRows();

        g2d.setColor(Color.BLACK);
        for (int x = 0; x <= panelWidth; x += slotWidth) {
            if ((x / slotWidth) % currentPuzzle.getBoxWidth() == 0) {
                g2d.setStroke(new BasicStroke(2));
                g2d.drawLine(x, 0, x, panelHeight);
            } else {
                g2d.setStroke(new BasicStroke(1));
                g2d.drawLine(x, 0, x, panelHeight);
            }
        }

        for (int y = 0; y <= panelHeight; y += slotHeight) {
            if ((y / slotHeight) % currentPuzzle.getBoxHeight() == 0) {
                g2d.setStroke(new BasicStroke(2));
                g2d.drawLine(0, y, panelWidth, y);
            } else {
                g2d.setStroke(new BasicStroke(1));
                g2d.drawLine(0, y, panelWidth, y);
            }
        }
    }
    /**
     * Draws the numbers of the Sudoku puzzle on the panel.
     * It uses the specified Fond for text size and the current puzzle to render the numbers.
     * The numbers are centered in each cell of the puzzle grid.
     *
     * @param g the graphics context used for drawing.
     */
    private void drawNumbers(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        Font font = new Font("Times New Roman", Font.PLAIN, textSize);
        g2d.setFont(font);
        FontRenderContext fontContext = g2d.getFontRenderContext();
        int slotWidth = this.getWidth() / currentPuzzle.getNumCols();
        int slotHeight = this.getHeight() / currentPuzzle.getNumRows();

        for (int row = 0; row < currentPuzzle.getNumRows(); row++) {
            for (int col = 0; col < currentPuzzle.getNumCols(); col++) {
                if (!currentPuzzle.EmptySlot(row, col)) {
                    String value = currentPuzzle.Value(row, col);
                    int textWidth = (int) font.getStringBounds(value, fontContext).getWidth();
                    int textHeight = (int) font.getStringBounds(value, fontContext).getHeight();
                    g2d.drawString(value, (col * slotWidth) + ((slotWidth / 2) - (textWidth / 2)),
                            (row * slotHeight) + ((slotHeight / 2) + (textHeight / 2)));
                }
            }
        }
    }

    private void highlightSelectedCell(Graphics g) {
        if (selectedCol != -1 && selectedRow != -1) {
            Graphics2D g2d = (Graphics2D) g;
            int slotWidth = this.getWidth() / currentPuzzle.getNumCols();
            int slotHeight = this.getHeight() / currentPuzzle.getNumRows();
            g2d.setColor(new Color(0.0f, 0.0f, 1.0f, 0.3f));
            g2d.fillRect(selectedCol * slotWidth, selectedRow * slotHeight, slotWidth, slotHeight);
        }
    }
    /**
     *  Handles the press of a number button.
     *  * If you have selected a cell, this updates the cell with the number you pressed.
     *  * It refreshes the panel to show the updated puzzle.
     *  * If the puzzle is solved after your move, it shows a message saying you've finished.
     */
    public void handleNumberButtonPress(String buttonValue) {
        if (selectedCol != -1 && selectedRow != -1) {
            currentPuzzle.makeMove(selectedRow, selectedCol, buttonValue, true);
            repaint();
            if (isPuzzleComplete()) {
                showCompletionMessage();
            }
        }
    }

    public class NumActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            handleNumberButtonPress(((JButton) e.getSource()).getText());
        }
    }

    private class SudokuPanelMouseAdapter extends MouseInputAdapter {
        @Override
        public void mouseClicked(MouseEvent e) {
            if (e.getButton() == MouseEvent.BUTTON1) {
                int slotWidth = panelWidth / currentPuzzle.getNumCols();
                int slotHeight = panelHeight / currentPuzzle.getNumRows();
                selectedRow = e.getY() / slotHeight;
                selectedCol = e.getX() / slotWidth;
                e.getComponent().repaint();
            }
        }
    }

    public void changeBackgroundColor(Color newColor) {
        this.bgColor = newColor;
        repaint();
    }

    public void startAutoChangeColor(int interval) {
        if (autoColorTimer != null) {
            autoColorTimer.stop();
        }
        autoColorTimer = new Timer(interval, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                changeBackgroundColor(new Color((int) (Math.random() * 0x1000000)));
            }
        });
        autoColorTimer.start();
    }

    public void stopAutoChangeBackgroundColor() {
        if (autoColorTimer != null) {
            autoColorTimer.stop();
        }
    }


    public void setPanelSize(Dimension size) {
        this.setPreferredSize(size);
    }


    public void setPuzzle(Puzzle puzzle) {
        this.currentPuzzle = puzzle;
        repaint();
    }


    public void resetSelectedCell() {
        selectedCol = -1;
        selectedRow = -1;
        repaint();
    }
    /**
     * Updates the size of the panel and its components.
     * It sets the panels size,
     * then revalidates and repaints the panel.
     *
     * @param width  the new width of the panel.
     * @param height the new height of the panel.
     */
    public void updatePanelSize(int width, int height) {
        this.panelWidth = width;
        this.panelHeight = height;
        this.setPreferredSize(new Dimension(width, height));
        revalidate();
        repaint();
    }


    private boolean isPuzzleComplete() {
        return currentPuzzle.isSolved();
    }


    private void showCompletionMessage() {
        if (isPuzzleComplete() == true) {
            JOptionPane.showMessageDialog(this, "Congratulations! You've completed the puzzle!", "Puzzle Complete", JOptionPane.INFORMATION_MESSAGE);

        }
    }

}
