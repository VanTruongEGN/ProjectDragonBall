package view;

import controller.CharacterSelectController;

import javax.swing.*;
import java.awt.*;

public class CharacterSelectPanel extends JPanel implements Runnable {


    private final int tileSize = 64; // kích thước ô nhân vật
    private final int cols = 7;
    private final int rows = 4;
    private final int screenWidth = 800;
    private final int screenHeight = 500;

    private Image background;
    private Image[][] characters; // ma trận ảnh nhân vật
    private int selectedRow = 0;
    private int selectedCol = 0;

    private Thread gameThread;
    private CharacterSelectController controller;

    public CharacterSelectPanel(JFrame frame) {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);
        this.setFocusable(true);

        // Controller
        controller = new CharacterSelectController(this, frame);
        this.addKeyListener(controller);

        // Background
        background = new ImageIcon("src/assets/bg/character_select_bg.jpg").getImage();

        // Tạo ảnh giả lập (ở đây dùng tạm các ô màu)
        characters = new Image[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                characters[i][j] = new ImageIcon("src/assets/chars/char" + (i * cols + j + 1) + ".png").getImage();
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

        g2.drawImage(background, 0, 0, screenWidth, screenHeight, null);

        int gap = 10;
        int totalWidth = cols * tileSize + (cols - 1) * gap;
        int totalHeight = rows * tileSize + (rows - 1) * gap;

        // 🔹 Căn giữa toàn bộ lưới trên màn hình
        int startX = (screenWidth - totalWidth) / 2;
        int startY = (screenHeight - totalHeight) / 2 - 40;

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                int x = startX + j * (tileSize + gap);
                int y = startY + i * (tileSize + gap);

                // Nền ô
                g2.setColor(Color.gray);
                g2.fillRect(x, y, tileSize, tileSize);

                // Ảnh nhân vật (nếu có)
                if (characters[i][j] != null) {
                    g2.drawImage(characters[i][j], x, y, tileSize, tileSize, null);
                }

                // Ô đang chọn
                if (i == selectedRow && j == selectedCol) {
                    g2.setColor(Color.red);
                    g2.setStroke(new BasicStroke(4));
                    g2.drawRect(x - 2, y - 2, tileSize + 4, tileSize + 4);
                }
            }
        }

        // 🔹 Vẽ text menu bên dưới
        g2.setFont(new Font("PressStart2P-Regular", Font.PLAIN, 16));
        g2.setColor(Color.white);

        String[] menuItems = {"1 PLAYER", "2 PLAYERS", "VS MODE", "START", "HELP"};
        int totalMenuWidth = 0;
        for (String item : menuItems)
            totalMenuWidth += g2.getFontMetrics().stringWidth(item) + 60;

        int menuStartX = (screenWidth - totalMenuWidth) / 2;
        int menuY = startY + totalHeight + 80;
        int x = menuStartX;

        for (String item : menuItems) {
            g2.drawString(item, x, menuY);
            x += g2.getFontMetrics().stringWidth(item) + 60;
        }

        g2.dispose();
    }

    // Getter và hành vi di chuyển con trỏ
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
