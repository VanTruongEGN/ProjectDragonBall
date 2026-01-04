package controller;

import view.CharacterSelectPanel;
import view.RegisterPanel;

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
        }
        else if (code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN) {
            view.moveDown();
        }
        else if (code == KeyEvent.VK_ENTER) {
            selectOption();
        }

        view.repaint();
    }

    private void selectOption() {
        String option = view.getSelectedOption();

        switch (option) {

            case "NEW GAME" -> {
                System.out.println("Starting new game...");

                frame.getContentPane().removeAll();

                CharacterSelectPanel selectPanel = new CharacterSelectPanel(frame);
                frame.add(selectPanel);

                frame.revalidate();
                frame.repaint();

                selectPanel.setFocusable(true);
                selectPanel.requestFocusInWindow();
            }

            case "LOAD GAME" -> {
                // Nếu CHƯA làm save/load → chặn lại cho đỡ lỗi
                JOptionPane.showMessageDialog(
                        frame,
                        "Load Game chưa được hỗ trợ!",
                        "Thông báo",
                        JOptionPane.INFORMATION_MESSAGE
                );
            }

            case "QUIT" -> System.exit(0);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {}
}
