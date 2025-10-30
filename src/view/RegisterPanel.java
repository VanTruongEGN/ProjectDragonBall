package view;

import controller.RegisterController;
import javax.swing.*;
import java.awt.*;

public class RegisterPanel extends JPanel implements Runnable {

    public final int tileSize = 48;
    final int maxScreenCol = 16;
    final int maxScreenRow = 12;
    final int screenWidth = tileSize * maxScreenCol;
    final int screenHeight = tileSize * maxScreenRow;

    Thread gameThread;
    Image character;
    Font titleFont, menuFont;
    String[] menuOptions = {"NEW GAME", "LOAD GAME", "QUIT"};
    int currentChoice = 0;

    private RegisterController controller;

    public RegisterPanel(JFrame parentFrame) {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);
        this.setFocusable(true);

        // Gắn controller
        controller = new RegisterController(this, parentFrame);
        this.addKeyListener(controller);

        // Font pixel
        titleFont = new Font("PressStart2P-Regular", Font.BOLD, 60);
        menuFont = new Font("PressStart2P-Regular", Font.PLAIN, 30);

        // Ảnh nhân vật
        character = new ImageIcon("src/assets/player/hinh.png").getImage();
    }

    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {
        double drawInterval = 1000000000.0 / 60;
        double nextDrawTime = System.nanoTime() + drawInterval;

        while (gameThread != null) {
            repaint();

            try {
                double remainingTime = nextDrawTime - System.nanoTime();
                remainingTime /= 1000000;

                if (remainingTime < 0) remainingTime = 0;
                Thread.sleep((long) remainingTime);

                nextDrawTime += drawInterval;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        // Tiêu đề
        g2.setFont(titleFont);
        g2.setColor(Color.white);
        String title = "DragonBall Promax";
        int titleX = getXforCenteredText(title, g2);
        g2.drawString(title, titleX, 150);

        // Nhân vật
        g2.drawImage(character, screenWidth / 2 - 24, 200, tileSize, tileSize, null);

        // Menu
        g2.setFont(menuFont);
        for (int i = 0; i < menuOptions.length; i++) {
            String text = menuOptions[i];
            int x = getXforCenteredText(text, g2);
            int y = 350 + i * 50;
            if (i == currentChoice) {
                g2.drawString(">", x - 40, y);
            }
            g2.drawString(text, x, y);
        }

        g2.dispose();
    }

    private int getXforCenteredText(String text, Graphics2D g2) {
        int length = (int) g2.getFontMetrics().getStringBounds(text, g2).getWidth();
        return screenWidth / 2 - length / 2;
    }

    public void moveUp() {
        currentChoice--;
        if (currentChoice < 0) currentChoice = menuOptions.length - 1;
    }

    public void moveDown() {
        currentChoice++;
        if (currentChoice >= menuOptions.length) currentChoice = 0;
    }

    public String getSelectedOption() {
        return menuOptions[currentChoice];
    }
}
