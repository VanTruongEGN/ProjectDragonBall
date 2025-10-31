package controller;

import view.CharacterSelectPanel;
import view.GamePanel;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class CharacterSelectController implements KeyListener {

    private final CharacterSelectPanel view;
    private final JFrame frame;

    public CharacterSelectController(CharacterSelectPanel view, JFrame frame) {
        this.view = view;
        this.frame = frame;
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();

        switch (code) {
            case KeyEvent.VK_W, KeyEvent.VK_UP -> view.moveUp();
            case KeyEvent.VK_S, KeyEvent.VK_DOWN -> view.moveDown();
            case KeyEvent.VK_A, KeyEvent.VK_LEFT -> view.moveLeft();
            case KeyEvent.VK_D, KeyEvent.VK_RIGHT -> view.moveRight();
            case KeyEvent.VK_ENTER -> selectCharacter();
        }

        view.repaint();
    }

    private void selectCharacter() {
        System.out.println("Selected character at row " + view.getSelectedRow() + ", col " + view.getSelectedCol());

        // Giả sử sau khi chọn thì vào GamePanel
        frame.getContentPane().removeAll();
        GamePanel gamePanel = new GamePanel();
        frame.add(gamePanel);
        frame.revalidate();
        frame.repaint();
        gamePanel.setFocusable(true);
        gamePanel.requestFocusInWindow();
        gamePanel.startGameThread();
    }

    @Override
    public void keyReleased(KeyEvent e) {}
}
