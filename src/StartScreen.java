import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StartScreen implements ActionListener {

    JFrame frame = new JFrame();
    JButton startButton = new JButton("Start Game");

    StartScreen() {
        startButton.setBounds(100,250,200,40);
        startButton.setOpaque(true);
        startButton.setFocusable(false);
        startButton.setBorderPainted(false);
        startButton.setBackground(Color.CYAN);
        startButton.setForeground(Color.BLACK);
        startButton.addActionListener(this);

        frame.add(startButton);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400,600);
        frame.setLayout(null);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.getContentPane().setBackground(Color.black);
        frame.setVisible(true);
    }
    public void actionPerformed(ActionEvent e) {
        if(e.getSource()==startButton) {
            frame.dispose();
            JackGameFrame newGame = new JackGameFrame();
        }
    }
}