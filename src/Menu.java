import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Menu extends JFrame implements ActionListener {
    private JPanel topPanel = new JPanel();
    private JPanel midPanel = new JPanel();
    private JPanel bottomPanel = new JPanel();
    private JButton playButton = new JButton();
    private JButton modeButton = new JButton("Mode");
    private JLabel titleLabel = new JLabel("Sudoku");
    private JLabel modeLabel = new JLabel("Select Mode:");
    private Timer colorTimer;

    public Menu() {
        initializeComponents();
        configureFrame();
        startColorChangeTimer();
    }

    private void initializeComponents() {
        // Configure buttons
        configureButton(playButton, "Start", 40, new Color(255, 232, 0));
        configureButton(modeButton, "Mode", 40, new Color(0, 128, 0));

        // Set panel padding
        setPanelPadding(topPanel);
        setPanelPadding(midPanel);
        setPanelPadding(bottomPanel);

        // Configure labels
        configureLabel(titleLabel, new Font("Arial", Font.PLAIN, 70));
        configureLabel(modeLabel, new Font("Arial", Font.PLAIN, 40));

        // Configure panels
        configurePanel(topPanel, new Dimension(800, 200), titleLabel);
        configurePanel(midPanel, new Dimension(800, 400), playButton);
        configurePanel(bottomPanel, new Dimension(800, 200), modeLabel, modeButton);


        updatePanelColors();
    }

    private void configureFrame() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Sudoku - Menu");
        setResizable(false);
        setSize(816, 839);
        setLayout(new BorderLayout(0, 0));
        setLocationRelativeTo(null); // Center the frame on the screen

        add(topPanel, BorderLayout.NORTH);
        add(midPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void configureButton(JButton button, String text, int fontSize, Color backgroundColor) {
        button.setText(text);
        button.setFont(new Font("Arial", Font.BOLD, fontSize));
        button.setHorizontalTextPosition(JButton.CENTER);
        button.setBackground(backgroundColor);
        button.setForeground(Color.BLACK);
        button.setFocusPainted(false);
        button.setToolTipText(text + " button");


        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(backgroundColor.darker());
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(backgroundColor);
            }
        });

        button.addActionListener(this);
    }

    private void configurePanel(JPanel panel, Dimension dimension, JComponent... components) {
        panel.setPreferredSize(dimension);
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        for (JComponent component : components) {
            panel.add(component, gbc);
        }
    }

    private void configureLabel(JLabel label, Font font) {
        label.setFont(font);
    }

    private void setPanelPadding(JPanel panel) {
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
    }

    // Method to start a timer for changing panel colors
    private void startColorChangeTimer() {
        colorTimer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updatePanelColors();
            }
        });
        colorTimer.start();
    }


    private void updatePanelColors() {
        topPanel.setBackground(generateRandomColor());
        midPanel.setBackground(generateRandomColor());
        bottomPanel.setBackground(generateRandomColor());
    }


    private Color generateRandomColor() {
        return new Color((int) (Math.random() * 256), (int) (Math.random() * 256), (int) (Math.random() * 256));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == playButton) {
            SwingUtilities.invokeLater(() -> {
                Frame frame = new Frame();
                frame.setVisible(true);
                dispose();
            });
        } else if (e.getSource() == modeButton) {
            selectMode();
        }
    }

    private void selectMode() {
        String[] modes = {"6x6", "9x9", "12x12", "16x16"};
        String selectedMode = (String) JOptionPane.showInputDialog(this,
                "Select Mode", "Mode Selection", JOptionPane.PLAIN_MESSAGE, null,
                modes, modes[0]);
        if (selectedMode != null) {
            JOptionPane.showMessageDialog(this, "Selected Mode: " + selectedMode);
        }
    }


}
