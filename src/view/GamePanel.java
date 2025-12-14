package view;

import AI.GameState;
import AI.Greedy;
import AI.MiniMax;
import controller.KeyHandler;
import module.*;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

public class GamePanel extends JPanel implements Runnable {

    public final int tileSize = 59;
    final int maxScreenCol = 16;
    final int maxScreenRow = 12;
    final int screenWidth = tileSize * maxScreenCol; // 768
    final int screenHeight = tileSize * maxScreenRow; // 576
    Thread gameThread;
    KeyHandler keyH = new KeyHandler();
    int skillIndex = 0;
    Player goku;
    Player vegeta;
    Image background;
    Greedy greedy = new Greedy();
    boolean gameOver = false;
    MiniMax miniMax = new MiniMax();

    ArrayList<Projectile> projectiles = new ArrayList<>();

    // turn-based
    private boolean gokuTurn = true;
    private boolean waitingProjectiles = false; // true while projectiles are active

    public GamePanel() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyH);
        this.setFocusable(true);
        // create players
        goku = new Goku(this, keyH);
        vegeta = new Vegeta(this, keyH);
        // place vegeta at right side properly
        vegeta.x = screenWidth - 160;

        background = new ImageIcon("src/assets/map/map1.jpg").getImage();
    }

    public GamePanel(String selectedCharacter) {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyH);
        this.setFocusable(true);

        // Người chơi chính (bên trái)
        switch (selectedCharacter) {
            case "Goku" -> {
                goku = new Goku(this, keyH);
            }
            case "Vegeta" -> goku = new Vegeta(this, keyH);
            case "Trunks" -> goku = new Trunks(this, keyH);
            case "Gohan" -> goku = new Gohan(this, keyH);
            case "Piccolo" -> goku = new Piccolo(this, keyH);
            case "Krillin" -> goku = new Krillin(this, keyH);
            default -> goku = new Goku(this, keyH);
        }

        // Đối thủ mặc định (bên phải)
        vegeta = new Vegeta(this, keyH);
        vegeta.x = screenWidth - 160;

        background = new ImageIcon("src/assets/map/map1.jpg").getImage();
    }


    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
    }
    @Override
    public void run() {
        double drawInterval = 1000000000.0 / 60; // 60 FPS
        double nextDrawTime = System.nanoTime() + drawInterval;
        while (gameThread != null) {
            update();
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
    public void update() {
        // if there are active projectiles, update them
        if (!projectiles.isEmpty()) {
            waitingProjectiles = true;
            Iterator<Projectile> it = projectiles.iterator();
            while (it.hasNext()) {
                Projectile p = it.next();
                p.update();
                if (p.isDestroyed()) it.remove();
                if (!p.isDestroyed()) break;
            }
            // if all projectiles finished -> switch turn and regen mana for next player
            if (projectiles.isEmpty()) {
                waitingProjectiles = false;
                switchTurn();
            }
            return; // while projectiles active, skip accepting new inputs
        }
        if (gokuTurn) {
            // check input from key handler
            skillIndex = keyH.getSkillPressed();// returns 0..3 and resets
            if(skillIndex == 5 && goku.strong <100){
                skillIndex=0;
            }
            if (skillIndex != 0) {
                if(skillIndex == 5) {
                    goku.resetStrongRatio();
                }
                if (goku.canUseSkill(skillIndex) && gokuTurn) {
                    Projectile p = goku.useSkill(skillIndex, vegeta);
                    if (p != null) projectiles.add(p);
                    if(p==null){
                        switchTurn();
                        return;
                    }
                }
            }
        }
        else{
            autoAI();

        }

        // check win condition
        if (goku.hp <= 0 || vegeta.hp <= 0) {
            gameThread = null;
            gameOver = true;
        }
    }
    private void autoAI(){
        if (vegeta.strong >= 100) {
            skillIndex = 5;
        } else {
            GameState root = new GameState(goku, vegeta);


            int[] skills = miniMax.skillCanUseInState(root.vegeta);
            int countSkill = 0;
            for (int s : skills) {
                if (s != -1) countSkill++;
            }

            if (countSkill > 2) {
                // cây tìm kiếm lớn → dùng Alpha-Beta
                skillIndex = miniMax.getBestSkillAlphaBeta(root, 3);
                System.out.println("AI chọn bằng Alpha-Beta, skill: " + skillIndex);
            } else {
                // cây nhỏ → dùng Minimax
                skillIndex = miniMax.getBestSkill(root, 3);
                System.out.println("AI chọn bằng Minimax, skill: " + skillIndex);
            }
        }

        if (skillIndex == 5) {
            vegeta.resetStrongRatio();
        }
        if (vegeta.canUseSkill(skillIndex)) {
            Projectile p = vegeta.useSkill(skillIndex, goku);
            if (p != null) projectiles.add(p);
        }
    }

    private void switchTurn() {
        gokuTurn = !gokuTurn;
        keyH.getSkillPressed();
        // mana regen for the player whose turn just started
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        // draw background (if missing image, it's fine)
        if (background != null) g2.drawImage(background, 0, 0, screenWidth, screenHeight, null);
        else {
            g2.setColor(Color.darkGray);
            g2.fillRect(0, 0, screenWidth, screenHeight);
        }

        // draw players
        goku.draw(g2);
        vegeta.draw(g2);

        // draw projectiles
        for (Projectile p : projectiles) {
            p.draw(g2,skillIndex);
        }

        // draw HUD
        drawStatusBar(g2);

        // draw turn indicator
        g2.setFont(new Font("Arial", Font.BOLD, 16));
        g2.setColor(Color.white);
        String turnText = gokuTurn ? "Turn: GOKU (You)" : "Turn: VEGETA (AI)";
        g2.drawString(turnText, screenWidth / 2 - 70, 30);

        // if game stopped due to win, display result
        if (gameThread == null) {
            g2.setFont(new Font("Arial", Font.BOLD, 100));
            g2.setColor(Color.red);
            String result = (goku.hp <= 0) ? "K.O!" : "WINS!";
            g2.drawString(result, screenWidth / 2-100, screenHeight / 2);
        }
        g2.dispose();
    }

    // Hàm tiện ích vẽ chữ có viền đều quanh
    private void drawTextWithOutline(Graphics2D g2, String text, int x, int y, Color mainColor, int fontSize) {
        g2.setFont(new Font("Arial", Font.BOLD, fontSize));

        // Viền đen quanh chữ
        g2.setColor(Color.black);
        g2.drawString(text, x-1, y);
        g2.drawString(text, x+2, y);
        g2.drawString(text, x, y-1);
        g2.drawString(text, x, y+2);

        // Chữ chính
        g2.setColor(mainColor);
        g2.drawString(text, x, y);
    }

    private void drawStatusBar(Graphics2D g2) {
        int barW = 220, barH = 18;
        int px = 20, py = 20;
        int rx = screenWidth - barW - 20;

        // --- Thanh máu/mana bên trái ---
        GradientPaint hpGradient = new GradientPaint(px, py, Color.red, px + barW, py, Color.orange);
        g2.setPaint(hpGradient);
        g2.fillRoundRect(px, py, (int)(barW * goku.getHpRatio()), barH, 10, 10);

        GradientPaint manaGradient = new GradientPaint(px, py + 22, Color.blue, px + barW, py + 22, Color.cyan);
        g2.setPaint(manaGradient);
        g2.fillRoundRect(px, py + 22, (int)(barW * goku.getManaRatio()), barH, 10, 10);
        //strong
        GradientPaint strongGradient = new GradientPaint(px, py + 22, Color.yellow, px + barW, py + 22, Color.cyan);
        g2.setPaint(strongGradient);
        g2.fillRoundRect(px, py + 44, (int)(barW * goku.getStrongRatio()), barH, 10, 10);

        g2.setColor(Color.white);
        g2.setStroke(new BasicStroke(2));
        g2.drawRoundRect(px, py, barW, barH, 10, 10);
        g2.drawRoundRect(px, py + 22, barW, barH, 10, 10);
        g2.drawRoundRect(px, py + 44, barW, barH, 10, 10);

        drawTextWithOutline(g2, "HP: " + goku.hp + "/" + goku.maxHp, px, py - 3, Color.white, 16);
        drawTextWithOutline(g2, "Mana: " + goku.mana + "/" + goku.maxMana, px, py + barH + 18, Color.cyan, 16);

        // --- Tên nhân vật bên trái ---
        drawTextWithOutline(g2, goku.getName(), px, py + 85, Color.yellow, 20);

        // --- Chiêu thức bên trái ---
        int skillY = py + 110;
        for (int i = 1; i <= 3; i++) {
            String skillName = goku.getSkillName(i);
            String dmg = "DMG: " + goku.getSkillDamage(i);
            String mana = "Mana: " + goku.getManaCost(i);

            drawTextWithOutline(g2, skillName, px, skillY, Color.orange, 18);
            drawTextWithOutline(g2, dmg, px, skillY + 20, Color.GREEN, 16);
            drawTextWithOutline(g2, mana, px + 120, skillY + 20, Color.cyan, 16);

            skillY += 45; // khoảng cách lớn hơn để chữ không chồng lên nhau
        }

        // --- Thanh máu/mana bên phải ---
        GradientPaint hpGradientV = new GradientPaint(rx, py, Color.red, rx + barW, py, Color.orange);
        g2.setPaint(hpGradientV);
        g2.fillRoundRect(rx, py, (int)(barW * vegeta.getHpRatio()), barH, 10, 10);

        GradientPaint manaGradientV = new GradientPaint(rx, py + 22, Color.blue, rx + barW, py + 22, Color.cyan);
        g2.setPaint(manaGradientV);
        g2.fillRoundRect(rx, py + 22, (int)(barW * vegeta.getManaRatio()), barH, 10, 10);
        //strong
        GradientPaint strongGradientV = new GradientPaint(rx, py + 22, Color.yellow, rx + barW, py + 22, Color.cyan);
        g2.setPaint(strongGradientV);
        g2.fillRoundRect(rx, py + 44, (int)(barW * vegeta.getStrongRatio()), barH, 10, 10);

        g2.setColor(Color.white);
        g2.setStroke(new BasicStroke(2));
        g2.drawRoundRect(rx, py, barW, barH, 10, 10);
        g2.drawRoundRect(rx, py + 22, barW, barH, 10, 10);
        g2.drawRoundRect(rx, py + 44, barW, barH, 10, 10);

        drawTextWithOutline(g2, "HP: " + vegeta.hp + "/" + vegeta.maxHp, rx, py - 3, Color.white, 16);
        drawTextWithOutline(g2, "Mana: " + vegeta.mana + "/" + vegeta.maxMana, rx, py + barH + 18, Color.cyan, 16);

        // --- Tên nhân vật bên phải ---
        drawTextWithOutline(g2, vegeta.getName(), rx, py + 85, Color.cyan, 20);

        // --- Chiêu thức bên phải ---
        int skillYv = py + 110;
        for (int i = 1; i <= 3; i++) {
            String skillName = vegeta.getSkillName(i);
            String dmg = "DMG: " + vegeta.getSkillDamage(i);
            String mana = "Mana: " + vegeta.getManaCost(i);

            drawTextWithOutline(g2, skillName, rx, skillYv, Color.orange, 18);
            drawTextWithOutline(g2, dmg, rx, skillYv + 20, Color.GREEN, 16);
            drawTextWithOutline(g2, mana, rx + 120, skillYv + 20, Color.cyan, 16);

            skillYv += 45;
        }
    }

    public int getTileSize() { return tileSize; }
}
