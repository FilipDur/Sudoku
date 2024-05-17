import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.font.FontRenderContext;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.event.MouseInputAdapter;

public class Panel extends JPanel {

    private Puzzle currentPuzzle;
    private int selectedCol;
    private int selectedRow;
    private int panelWidth;
    private int panelHeight;
    private int textSize;
    private Color bgColor;
    private Timer autoColorTimer;

    public Panel() {
        this.setPreferredSize(new Dimension(540, 450));
        this.addMouseListener(new SudokuPanelMouseAdapter());
        this.currentPuzzle = new Generator().generateRandomSudoku(PuzzleType.NINE_BY_NINE);
        selectedCol = -1;
        selectedRow = -1;
        panelWidth = 0;
        panelHeight = 0;
        textSize = 26;
        bgColor = Color.WHITE;
    }

    public Panel(Puzzle puzzle) {
        this.setPreferredSize(new Dimension(540, 450));
        this.addMouseListener(new SudokuPanelMouseAdapter());
        this.currentPuzzle = puzzle;
        selectedCol = -1;
        selectedRow = -1;
        panelWidth = 0;
        panelHeight = 0;
        textSize = 26;
        bgColor = Color.WHITE;
    }

    public void loadNewSudokuPuzzle(Puzzle puzzle) {
        this.currentPuzzle = puzzle;
    }

    public void updateFontSize(int fontSize) {
        this.textSize = fontSize;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(bgColor);

        int slotWidth = this.getWidth() / currentPuzzle.getNumCols();
        int slotHeight = this.getHeight() / currentPuzzle.getNumRows();

        panelWidth = (this.getWidth() / currentPuzzle.getNumCols()) * currentPuzzle.getNumCols();
        panelHeight = (this.getHeight() / currentPuzzle.getNumRows()) * currentPuzzle.getNumRows();

        g2d.fillRect(0, 0, panelWidth, panelHeight);

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

        Font font = new Font("Times New Roman", Font.PLAIN, textSize);
        g2d.setFont(font);
        FontRenderContext fontContext = g2d.getFontRenderContext();
        for (int row = 0; row < currentPuzzle.getNumRows(); row++) {
            for (int col = 0; col < currentPuzzle.getNumCols(); col++) {
                if (!currentPuzzle.isSlotAvailable(row, col)) {
                    int textWidth = (int) font.getStringBounds(currentPuzzle.getValue(row, col), fontContext).getWidth();
                    int textHeight = (int) font.getStringBounds(currentPuzzle.getValue(row, col), fontContext).getHeight();
                    g2d.drawString(currentPuzzle.getValue(row, col), (col * slotWidth) + ((slotWidth / 2) - (textWidth / 2)),
                            (row * slotHeight) + ((slotHeight / 2) + (textHeight / 2)));
                }
            }
        }

        if (selectedCol != -1 && selectedRow != -1) {
            g2d.setColor(new Color(0.0f, 0.0f, 1.0f, 0.3f));
            g2d.fillRect(selectedCol * slotWidth, selectedRow * slotHeight, slotWidth, slotHeight);
        }
    }

    public void handleNumberButtonPress(String buttonValue) {
        if (selectedCol != -1 && selectedRow != -1) {
            currentPuzzle.makeMove(selectedRow, selectedCol, buttonValue, true);
            repaint();
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
}
