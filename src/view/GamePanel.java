package view;

import controller.KeyHandler;
import module.*;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Random;

public class GamePanel extends JPanel implements Runnable {

    public final int tileSize = 59;
    final int maxScreenCol = 16;
    final int maxScreenRow = 12;
    final int screenWidth = tileSize * maxScreenCol; // 768
    final int screenHeight = tileSize * maxScreenRow; // 576
    Image gokuAvatar = new ImageIcon("src/assets/player/goku/hinh.png").getImage();
    Image vegetaAvatar = new ImageIcon("src/assets/player/vegeta/hinh.png").getImage();
    Thread gameThread;
    KeyHandler keyH = new KeyHandler();
    int skillIndex = 0;
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
             skillIndex = keyH.getSkillPressed(); // returns 0..3 and resets
            if (skillIndex != 0) {
                if(skillIndex == 4) {

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
        }
    }
    private void autoAI(){
        int[] skill = new int[3];
        Arrays.fill(skill, -1);
        for(int i = 0; i < skill.length; i++){
            if(vegeta.getManaCost(i+1)<=vegeta.mana){
                skill[i]=i+1;
            }
        }
        int skillUsed = heristic(skill);
        System.out.println("skillUsed:"+skillUsed);
        if (vegeta.canUseSkill(skillUsed)) {
            Projectile p = vegeta.useSkill(skillUsed, goku);
            if (p != null) projectiles.add(p);
        }
        System.out.println(vegeta.getMana());
    }
    public int heristic(int[] skillCanUse){
        double[] heristic =  new double[3];
        for(int i = 0; i < heristic.length; i++){
            if(skillCanUse[i] != -1){
                int dmg = vegeta.getSkillDamage(skillCanUse[i]);
                if(dmg >= goku.hp){
                    heristic[i] = Double.MAX_VALUE;
                } else {
                    heristic[i] = (double)dmg / goku.hp;
                }
            } else {
                heristic[i] = -1;
            }
        }

        double best = -1;
        int bestSkill = -1;

        for(int i = 0; i < heristic.length; i++){
            if(heristic[i] > best){
                best = heristic[i];
                bestSkill = skillCanUse[i];
            }
        }
        if(Arrays.stream(heristic).max().getAsDouble()==0){
            return 4;
        }
        return bestSkill;
    }
    private void switchTurn() {
        gokuTurn = !gokuTurn;
        keyH.getSkillPressed();
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

    //thanh máu
    private void drawStatusBar(Graphics2D g2) {
        int barW = 220, barH = 18;
        int px = 20, py = 20;
        int rx = screenWidth - barW - 20;
        int avatarSize = 50;


// HP gradient
        GradientPaint hpGradient = new GradientPaint(px, py, Color.red, px+barW, py, Color.orange);
        g2.setPaint(hpGradient);
        g2.fillRoundRect(px, py, (int)(barW * goku.getHpRatio()), barH, 10, 10);

// Mana gradient
        GradientPaint manaGradient = new GradientPaint(px, py+22, Color.blue, px+barW, py+22, Color.cyan);
        g2.setPaint(manaGradient);
        g2.fillRoundRect(px, py+22, (int)(barW * goku.getManaRatio()), barH, 10, 10);

// Viền
        g2.setColor(Color.white);
        g2.setStroke(new BasicStroke(2));
        g2.drawRoundRect(px, py, barW, barH, 10, 10);
        g2.drawRoundRect(px, py+22, barW, barH, 10, 10);

// Text
        g2.setFont(new Font("Arial", Font.BOLD, 14));
        g2.setColor(Color.white);
        g2.drawString("HP: " + goku.hp + "/" + goku.maxHp, px, py-3);
        g2.setColor(Color.cyan);
        g2.drawString("Mana: " + (goku.mana>100?goku.maxMana:goku.mana) + "/" + goku.maxMana, px, py+barH+18);



        GradientPaint hpGradientV = new GradientPaint(rx, py, Color.red, rx+barW, py, Color.orange);
        g2.setPaint(hpGradientV);
        g2.fillRoundRect(rx, py, (int)(barW * vegeta.getHpRatio()), barH, 10, 10);

        GradientPaint manaGradientV = new GradientPaint(rx, py+22, Color.blue, rx+barW, py+22, Color.cyan);
        g2.setPaint(manaGradientV);
        g2.fillRoundRect(rx, py+22, (int)(barW * vegeta.getManaRatio()), barH, 10, 10);

        g2.setColor(Color.white);
        g2.setStroke(new BasicStroke(2));
        g2.drawRoundRect(rx, py, barW, barH, 10, 10);
        g2.drawRoundRect(rx, py+22, barW, barH, 10, 10);

        g2.setFont(new Font("Arial", Font.BOLD, 14));
        g2.setColor(Color.white);
        g2.drawString("HP: " + vegeta.hp + "/" + vegeta.maxHp, rx, py-3);
        g2.setColor(Color.cyan);
        g2.drawString("Mana: " + (vegeta.mana>100?vegeta.maxMana:vegeta.mana) + "/" + vegeta.maxMana, rx, py+barH+18);

    }


    public int getTileSize() { return tileSize; }
}
