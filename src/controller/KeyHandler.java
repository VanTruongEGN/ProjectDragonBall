package controller;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener {
    // skillPressed: 0 = none, 1 = J, 2 = K, 3 = L
    private int skillPressed = 0;


    // Called by GamePanel each frame to fetch and clear the pressed skill
    public int getSkillPressed() {
        int s = skillPressed;
        skillPressed = 0;
        return s;
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();
        if (code == KeyEvent.VK_J) skillPressed = 1;
        else if (code == KeyEvent.VK_K) skillPressed = 2;
        else if (code == KeyEvent.VK_L) skillPressed = 3;
    }
    @Override
    public void keyReleased(KeyEvent e) {}
}
