package view;

import controller.RegisterController;
import javax.swing.*;
import java.awt.*;

public class RegisterPanel extends JPanel implements Runnable {

    public final int tileSize = 59;
    final int maxScreenCol = 16;
    final int maxScreenRow = 12;
    final int screenWidth = tileSize * maxScreenCol;
    final int screenHeight = tileSize * maxScreenRow;

    Image background;
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

        // Ảnh nhân vật và background
        character = new ImageIcon("src/assets/player/hinh.png").getImage();
        background = new ImageIcon("src/assets/map/img.png").getImage();
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

        if (background != null) {
            g2.drawImage(background, 0, 0, screenWidth, screenHeight, null);
        } else {
            g2.setColor(Color.black);
            g2.fillRect(0, 0, screenWidth, screenHeight);
        }

        String title = "DRAGONBALL PROMAX";
        g2.setFont(titleFont);
        int titleX = getXforCenteredText(title, g2);
        int titleY = 150;

        // Bóng đen
        g2.setColor(new Color(0, 0, 0, 180));
        g2.drawString(title, titleX + 4, titleY + 4);

        // Gradient vàng cam
        GradientPaint gp = new GradientPaint(
                titleX, titleY - 60, new Color(255, 140, 0),
                titleX, titleY + 10, new Color(255, 230, 100)
        );
        g2.setPaint(gp);
        g2.drawString(title, titleX, titleY);

        // Gạch chân ánh sáng
        g2.setColor(new Color(255, 200, 80));
        g2.fillRect(titleX, titleY + 10,
                (int) g2.getFontMetrics().getStringBounds(title, g2).getWidth(), 4);

        // 🥋 Nhân vật
        g2.drawImage(character, screenWidth / 2 - 30, 200, tileSize * 2, tileSize * 2, null);

        // 🎮 Menu lựa chọn
        g2.setFont(menuFont);
        for (int i = 0; i < menuOptions.length; i++) {
            String text = menuOptions[i];
            int x = getXforCenteredText(text, g2);
            int y = 430 + i * 70;

            if (i == currentChoice) {
                // Hiệu ứng nổi bật cho dòng đang chọn
                g2.setColor(Color.black);
                g2.drawString(">", x - 60, y + 4);

                // Viền đen mờ phía sau
                g2.setColor(new Color(0, 0, 0, 180));
                g2.drawString(text, x + 3, y + 3);

                // Chữ vàng sáng
                g2.setColor(new Color(255, 210, 0));
                g2.drawString(text, x, y);

                // Viền sáng nhẹ quanh dòng
                g2.setColor(new Color(255, 255, 150, 100));
                g2.drawRect(x - 70, y - 35,
                        (int) g2.getFontMetrics().getStringBounds(text, g2).getWidth() + 90, 45);
            } else {
                // Dòng chưa chọn -> màu trắng mờ
                g2.setColor(new Color(0, 0, 0, 180));
                g2.drawString(text, x, y);
            }
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
