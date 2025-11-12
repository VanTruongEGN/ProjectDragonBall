package view;

import controller.RegisterController;

import javax.swing.*;


public class GameFrame extends JFrame {
    public GameFrame() {

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setTitle("Game2D");

        RegisterPanel registerPanel = new RegisterPanel(this);
        this.add(registerPanel);
        this.pack();

        this.setLocationRelativeTo(null);
        this.setVisible(true);

        registerPanel.startGameThread();

    }


    public static void main(String[] args) {

        new GameFrame();
    }
}
