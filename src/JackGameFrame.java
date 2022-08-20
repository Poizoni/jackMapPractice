import javax.swing.*;
import java.awt.*;

public class JackGameFrame extends JFrame {

    JackGamePanel panel;

    JackGameFrame() {
        panel = new JackGamePanel();
        this.add(panel);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("Jack Practice");
        this.setBackground(Color.black);
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.pack();
        this.setVisible(true);
    }
}
