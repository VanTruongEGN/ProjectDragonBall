package view;

import controller.CharacterSelectController;

import javax.swing.*;
import java.awt.*;

public class CharacterSelectPanel extends JPanel implements Runnable {

    private final int tileSize = 130;
    private final int cols = 3;
    private final int rows = 2;
    private final int screenWidth = 800;
    private final int screenHeight = 600;

    private boolean selectingOpponent = false;
    private String selectedPlayer = null;

    private Image[][] characters;
    private Image background;

    private int selectedRow = 0;
    private int selectedCol = 0;

    private Thread gameThread;
    private CharacterSelectController controller;
    private float glowAlpha = 0f;

    private String[][] characterNames = {
            {"Goku", "Vegeta", "Gohan"},
            {"Piccolo", "Krillin", "Trunks"}
    };

    public CharacterSelectPanel(JFrame frame) {
        setPreferredSize(new Dimension(screenWidth, screenHeight));
        setDoubleBuffered(true);
        setFocusable(true);

        controller = new CharacterSelectController(this, frame);
        addKeyListener(controller);

        background = new ImageIcon("src/assets/map/img_1.png").getImage();

        characters = new Image[][]{
                { new ImageIcon("src/assets/select/img_6.png").getImage(),
                        new ImageIcon("src/assets/select/img_7.png").getImage(),
                        new ImageIcon("src/assets/select/img_4.png").getImage() },

                { new ImageIcon("src/assets/select/img_8.png").getImage(),
                        new ImageIcon("src/assets/select/img_9.png").getImage(),
                        new ImageIcon("src/assets/select/img_5.png").getImage() }
        };

        startThread();
    }

    public void startThread() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    public boolean isSelectingOpponent() {
        return selectingOpponent;
    }

    public void confirmSelection() {
        String chosen = characterNames[selectedRow][selectedCol];

        if (!selectingOpponent) {
            selectedPlayer = chosen;
            selectingOpponent = true;
            selectedRow = selectedCol = 0;
        } else {
            controller.startGame(selectedPlayer, chosen);
        }
    }

    public String getTitleText() {
        return selectingOpponent ? "SELECT OPPONENT" : "SELECT YOUR FIGHTER";
    }

    public void moveUp() { selectedRow = (selectedRow + rows - 1) % rows; }
    public void moveDown() { selectedRow = (selectedRow + 1) % rows; }
    public void moveLeft() { selectedCol = (selectedCol + cols - 1) % cols; }
    public void moveRight() { selectedCol = (selectedCol + 1) % cols; }

    @Override
    public void run() {
        boolean inc = true;
        while (true) {
            glowAlpha += inc ? 0.05f : -0.05f;
            if (glowAlpha >= 1f) inc = false;
            if (glowAlpha <= 0f) inc = true;
            repaint();
            try { Thread.sleep(16); } catch (Exception ignored) {}
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        g2.drawImage(background, 0, 0, getWidth(), getHeight(), null);

        g2.setFont(new Font("PressStart2P-Regular", Font.BOLD, 30));
        g2.setColor(Color.yellow);
        g2.drawString(getTitleText(), 180, 60);

        int gap = 70;
        int startX = 80;
        int startY = 120;

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                int x = startX + j * (tileSize + gap);
                int y = startY + i * (tileSize + gap);

                g2.setColor(Color.darkGray);
                g2.fillRoundRect(x, y, tileSize, tileSize, 20, 20);
                g2.drawImage(characters[i][j], x + 5, y + 5, tileSize - 10, tileSize - 10, null);

                g2.setColor(Color.white);
                g2.drawRoundRect(x, y, tileSize, tileSize, 20, 20);

                if (i == selectedRow && j == selectedCol) {
                    g2.setStroke(new BasicStroke(5));
                    g2.setColor(new Color(255, 215, 0, (int)(200 * glowAlpha)));
                    g2.drawRoundRect(x - 4, y - 4, tileSize + 8, tileSize + 8, 25, 25);
                }
            }
        }
    }
}
