package view;

import javax.swing.*;

public class GameFrame extends JFrame {
    public GameFrame() {
        setTitle("2D Map Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        GamePanel panel = new GamePanel();
        add(panel);
        pack();

        setLocationRelativeTo(null);
        setVisible(true);

        // Focus để nhận phím
        panel.requestFocusInWindow();
        panel.startGameThread();
    }

    public static void main(String[] args) {
        new GameFrame();
    }
}
