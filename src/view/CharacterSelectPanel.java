package view;

import controller.CharacterSelectController;

import javax.swing.*;
import java.awt.*;

public class CharacterSelectPanel extends JPanel implements Runnable {

    private final int tileSize = 100; // kích thước mỗi ô nhân vật
    private final int cols = 3;       // 3 cột
    private final int rows = 2;       // 2 hàng
    private final int screenWidth = 800;
    private final int screenHeight = 600;

    private Image background;
    private Image[][] characters; // 6 nhân vật
    private int selectedRow = 0;
    private int selectedCol = 0;

    private Thread gameThread;
    private CharacterSelectController controller;
    private float glowAlpha = 0f; // hiệu ứng sáng viền

    public CharacterSelectPanel(JFrame frame) {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);
        this.setFocusable(true);

        // Controller
        controller = new CharacterSelectController(this, frame);
        this.addKeyListener(controller);

        // Background
        background = new ImageIcon("src/assets/map/img_1.png").getImage();

        // 6 nhân vật (char1 -> char6)
        characters = new Image[rows][cols];
        int count = 1;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                characters[i][j] = new ImageIcon("src/assets/chars/char" + count + ".png").getImage();
                count++;
            }
        }
    }

    public void startThread() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {
        double drawInterval = 1000000000.0 / 60;
        double nextDrawTime = System.nanoTime() + drawInterval;
        boolean increase = true;

        while (gameThread != null) {
            // hiệu ứng sáng nhấp nháy
            glowAlpha += increase ? 0.05f : -0.05f;
            if (glowAlpha >= 1f) increase = false;
            if (glowAlpha <= 0f) increase = true;

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
            g2.drawImage(background, 0, 0, getWidth(), getHeight(), null);

        } else {
            g2.setColor(Color.black);
            g2.fillRect(0, 0, screenWidth, screenHeight);
        }
        String title = "SELECT YOUR FIGHTER";
        g2.setFont(new Font("PressStart2P-Regular", Font.BOLD, 32));
        int titleX = getXforCenteredText(title, g2);
        g2.setColor(new Color(0, 0, 0, 180));
        g2.drawString(title, titleX-80 + 3, 80 + 3);
        g2.setColor(new Color(255, 200, 60));
        g2.drawString(title, titleX -80, 80);
        g2.fillRect(titleX -80, 88, (int) g2.getFontMetrics().getStringBounds(title, g2).getWidth(), 3);


        int gap = 50;
        int totalWidth = cols * tileSize + (cols - 1) * gap;
        int totalHeight = rows * tileSize + (rows - 1) * gap;

        int startX = (screenWidth - totalWidth) /2 -80;
        int startY = (screenHeight - totalHeight) / 2 - 20;

        // Vẽ từng nhân vật
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                int x = startX + j * (tileSize + gap);
                int y = startY + i * (tileSize + gap);
                // Nền ô nhân vật
                g2.setColor(Color.gray);
                g2.fillRoundRect(x, y, tileSize, tileSize, 15, 15);

                // Ảnh nhân vật
                if (characters[i][j] != null) {
                    int imgSize = tileSize - 10;
                    g2.drawImage(characters[i][j], x + 5, y + 5, imgSize, imgSize, null);
                }

                // Viền trắng
                g2.setColor(Color.white);
                g2.setStroke(new BasicStroke(2));
                g2.drawRoundRect(x, y, tileSize, tileSize, 15, 15);

                // Ô đang chọn — hiệu ứng sáng vàng
                if (i == selectedRow && j == selectedCol) {
                    int glow = (int) (150 + 100 * glowAlpha);
                    g2.setColor(new Color(255, 220, 0, glow));
                    g2.setStroke(new BasicStroke(5));
                    g2.drawRoundRect(x - 4, y - 4, tileSize + 8, tileSize + 8, 18, 18);
                }
            }
        }


        g2.setFont(new Font("PressStart2P-Regular", Font.PLAIN, 18));
        g2.setColor(Color.white);

        }

    private int getXforCenteredText(String text, Graphics2D g2) {
        int length = (int) g2.getFontMetrics().getStringBounds(text, g2).getWidth();
        return screenWidth / 2 - length / 2;
    }

    // Điều khiển chọn nhân vật
    public void moveUp() {
        selectedRow--;
        if (selectedRow < 0) selectedRow = rows - 1;
    }

    public void moveDown() {
        selectedRow++;
        if (selectedRow >= rows) selectedRow = 0;
    }

    public void moveLeft() {
        selectedCol--;
        if (selectedCol < 0) selectedCol = cols - 1;
    }

    public void moveRight() {
        selectedCol++;
        if (selectedCol >= cols) selectedCol = 0;
    }

    public int getSelectedRow() {
        return selectedRow;
    }

    public int getSelectedCol() {
        return selectedCol;
    }
}
