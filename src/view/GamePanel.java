package view;

import controller.KeyHandler;
import module.*;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;

public class GamePanel extends JPanel implements Runnable {

    public final int tileSize = 48;
    final int maxScreenCol = 16;
    final int maxScreenRow = 12;
    final int screenWidth = tileSize * maxScreenCol; // 768
    final int screenHeight = tileSize * maxScreenRow; // 576

    Thread gameThread;
    KeyHandler keyH = new KeyHandler();

    Player goku;
    Player vegeta;
    Image background;

    ArrayList<Projectile> projectiles = new ArrayList<>();

    // turn-based
    private boolean gokuTurn = true;
    private boolean waitingProjectiles = false; // true while projectiles are active
    private int manaRegenPerTurn = 8;

    public GamePanel() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyH);
        this.setFocusable(true);

        // create players
        goku = new Goku(this, keyH);
        vegeta = new Vegeta(this);
        // place vegeta at right side properly
        vegeta.x = screenWidth - 160;

        background = new ImageIcon("src/assets/map/map.jpg").getImage();
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
            }
            // if all projectiles finished -> switch turn and regen mana for next player
            if (projectiles.isEmpty()) {
                waitingProjectiles = false;
                switchTurn();
            }
            return; // while projectiles active, skip accepting new inputs
        }

        // no active projectiles -> accept actions depending on whose turn
        if (gokuTurn) {
            // check input from key handler
            int skill = keyH.getSkillPressed(); // returns 0..3 and resets
            if (skill > 0) {
                if (goku.canUseSkill(skill)) {
                    Projectile p = goku.useSkill(skill, vegeta);
                    if (p != null) projectiles.add(p);
                } else {
                    // optionally: play a sound or show text; for now do nothing
                    // you could show "Not enough mana" text here
                }
            }
        } else {
            // Vegeta's turn: auto choose and fire immediately
            Projectile p = vegeta.performAutoSkill(goku);
            if (p != null) projectiles.add(p);
            else {
                // Vegeta can't use any skill this turn -> just switch turn (with a small delay handled by projectiles logic)
                // To avoid instant ping-pong, we directly regen and switch
                switchTurn();
            }
        }

        // check win condition
        if (goku.hp <= 0 || vegeta.hp <= 0) {
            // stop game thread or you can show game over UI - for now we just stop thread
            gameThread = null;
        }
    }

    private void switchTurn() {
        gokuTurn = !gokuTurn;
        // mana regen for the player whose turn just started
        if (gokuTurn) goku.regenMana(manaRegenPerTurn);
        else vegeta.regenMana(manaRegenPerTurn);
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
        for (Projectile p : projectiles) p.draw(g2);

        // draw HUD
        drawStatusBar(g2);

        // draw turn indicator
        g2.setFont(new Font("Arial", Font.BOLD, 16));
        g2.setColor(Color.white);
        String turnText = gokuTurn ? "Turn: GOKU (You)" : "Turn: VEGETA (AI)";
        g2.drawString(turnText, screenWidth / 2 - 70, 30);

        // draw skill hints (J/K/L)
        g2.setFont(new Font("Arial", Font.PLAIN, 12));
        g2.drawString("Press J/K/L to use skill (Goku). Costs shown on bars.", 20, screenHeight - 20);

        // if game stopped due to win, display result
        if (gameThread == null) {
            g2.setFont(new Font("Arial", Font.BOLD, 36));
            g2.setColor(Color.yellow);
            String result = (goku.hp <= 0) ? "VEGETA WINS!" : "GOKU WINS!";
            g2.drawString(result, screenWidth / 2 - 140, screenHeight / 2);
        }

        g2.dispose();
    }

    private void drawStatusBar(Graphics2D g2) {
        int barW = 220, barH = 18;

        // Goku (left)
        int px = 20, py = 20;
        g2.setColor(Color.white);
        g2.drawRect(px - 1, py - 15, barW + 2, 60);
        g2.setColor(Color.red);
        g2.fillRect(px, py, (int)(barW * goku.getHpRatio()), barH);
        g2.setColor(Color.blue);
        g2.fillRect(px, py + 22, (int)(barW * goku.getManaRatio()), barH);
        g2.setColor(Color.white);
        g2.drawString("GOKU HP: " + goku.hp + "/" + goku.maxHp, px, py - 3);
        g2.drawString("Mana: " + goku.mana + "/" + goku.maxMana, px, py + 20 + barH - 2);
        // skill costs
        g2.drawString("J:" + goku.getSkillName(1) + " (" + goku.getManaCost(1) + ")", px, py + 55);
        g2.drawString("K:" + goku.getSkillName(2) + " (" + goku.getManaCost(2) + ")", px + 90, py + 55);
        g2.drawString("L:" + goku.getSkillName(3) + " (" + goku.getManaCost(3) + ")", px + 180, py + 55);

        // Vegeta (right)
        int rx = screenWidth - barW - 20;
        g2.setColor(Color.white);
        g2.drawRect(rx - 1, py - 15, barW + 2, 60);
        g2.setColor(Color.red);
        g2.fillRect(rx, py, (int)(barW * vegeta.getHpRatio()), barH);
        g2.setColor(Color.blue);
        g2.fillRect(rx, py + 22, (int)(barW * vegeta.getManaRatio()), barH);
        g2.setColor(Color.white);
        g2.drawString("VEGETA HP: " + vegeta.hp + "/" + vegeta.maxHp, rx, py - 3);
        g2.drawString("Mana: " + vegeta.mana + "/" + vegeta.maxMana, rx, py + 20 + barH - 2);
        // Vegeta skill names for reference
        g2.drawString("1:" + vegeta.getSkillName(1), rx, py + 55);
        g2.drawString("2:" + vegeta.getSkillName(2), rx + 90, py + 55);
        g2.drawString("3:" + vegeta.getSkillName(3), rx + 180, py + 55);
    }

    public int getTileSize() { return tileSize; }
}
