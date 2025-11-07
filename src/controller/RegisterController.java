package controller;

import view.CharacterSelectPanel;
import view.RegisterPanel;
import view.GamePanel;
import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;


public class RegisterController implements KeyListener {

    private final RegisterPanel view;
    private final JFrame frame;

    public RegisterController(RegisterPanel view, JFrame frame) {
        this.view = view;
        this.frame = frame;
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();

        if (code == KeyEvent.VK_W || code == KeyEvent.VK_UP) {
            view.moveUp();
        } else if (code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN) {
            view.moveDown();
        } else if (code == KeyEvent.VK_ENTER) {
            selectOption();
        }

        view.repaint();
    }

    private void selectOption() {
        String option = view.getSelectedOption();

        switch (option) {
            case "NEW GAME":
                System.out.println("Starting new game...");
                // Chuyển sang GamePanel
                frame.getContentPane().removeAll();
                CharacterSelectPanel CPanel = new CharacterSelectPanel(frame);
                frame.add(CPanel);
                frame.revalidate();
                frame.repaint();
                CPanel.setFocusable(true);
                CPanel.requestFocusInWindow();
//                CPanel.startGameThread();
                break;

            case "LOAD GAME":
                System.out.println("Loading game...");
                frame.getContentPane().removeAll();
                GamePanel gamePanel = new GamePanel();
                frame.add(gamePanel);
                frame.revalidate();
                frame.repaint();
                gamePanel.setFocusable(true);
                gamePanel.requestFocusInWindow();
                gamePanel.startGameThread();
                break;

            case "QUIT":
                System.exit(0);
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {}
}
