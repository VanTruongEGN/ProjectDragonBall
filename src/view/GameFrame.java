package view;

import controller.RegisterController;

import javax.swing.*;

public class GameFrame extends JFrame {
    public GameFrame() {
        JFrame window = new JFrame();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        window.setTitle("Game2D");

        RegisterPanel registerPanel = new RegisterPanel(window);
        window.add(registerPanel);
        window.pack();

        window.setLocationRelativeTo(null);
        window.setVisible(true);

        registerPanel.startGameThread();

    }


    public static void main(String[] args) {

        new GameFrame();
    }
}
