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
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_W, KeyEvent.VK_UP -> view.moveUp();
            case KeyEvent.VK_S, KeyEvent.VK_DOWN -> view.moveDown();
            case KeyEvent.VK_A, KeyEvent.VK_LEFT -> view.moveLeft();
            case KeyEvent.VK_D, KeyEvent.VK_RIGHT -> view.moveRight();
            case KeyEvent.VK_ENTER -> view.confirmSelection();
        }
        view.repaint();
    }

    public void startGame(String player, String opponent) {
        frame.getContentPane().removeAll();
        GamePanel gp = new GamePanel(player, opponent, frame);
        frame.add(gp);
        frame.revalidate();
        frame.repaint();
        gp.requestFocusInWindow();
        gp.startGameThread();
    }

    @Override public void keyTyped(KeyEvent e) {}
    @Override public void keyReleased(KeyEvent e) {}
}
