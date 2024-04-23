import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Menu implements ActionListener {
    JPanel topPanel = new JPanel();
    JPanel midPanel = new JPanel();
    JPanel bottomPanel = new JPanel();
    JButton playButton = new JButton();
    JLabel label = new JLabel();
    JFrame menuFrame = new JFrame();
    ImageIcon image = new ImageIcon("sudokuicon.jpg");


    Menu() {


        playButton.setBounds(300, 150, 200, 100);
        playButton.addActionListener(this);
        playButton.setText("Start");
        playButton.setFont(new Font("Arial White", Font.PLAIN, 40));
        playButton.setHorizontalTextPosition(JButton.CENTER);
        playButton.setBackground(new Color(255, 232, 0));



        label.setText("Sudoku");
        label.setHorizontalAlignment(JLabel.CENTER);
        label.setVerticalAlignment(JLabel.CENTER);
        label.setBounds(0, 0, 800, 200);
        label.setFont(new Font("Sneakerhead BTN Shadow", Font.PLAIN, 80));

        topPanel.setBounds(0, 0, 800, 200);
        topPanel.setBackground(new Color(169, 10, 10));
        topPanel.setLayout(null);
        topPanel.add(label);

        midPanel.setBounds(0, 200, 800, 400);
        midPanel.setBackground(new Color(3, 13, 155));
        midPanel.setLayout(null);
        midPanel.add(playButton);

        bottomPanel.setBounds(0, 600, 800, 800);
        bottomPanel.setBackground(new Color(255, 255, 255));
        bottomPanel.setLayout(null);

        menuFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        menuFrame.setTitle("Sudoku - Menu");
        menuFrame.setResizable(false);
        menuFrame.setSize(816, 839);
        menuFrame.setVisible(true);
        menuFrame.setLayout(null);
        menuFrame.setIconImage(image.getImage());
        menuFrame.add(topPanel);
        menuFrame.add(midPanel);
        menuFrame.add(bottomPanel);

    }


    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == playButton) {
            menuFrame.dispose();
            Hra hra = new Hra();
        }
    }
}