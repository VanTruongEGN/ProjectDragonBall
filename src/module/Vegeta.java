package module;

import controller.KeyHandler;
import view.GamePanel;

import javax.swing.*;
import java.awt.*;

public class Vegeta extends Player {

    private Image vegetaImg;

    public Vegeta(GamePanel gp, KeyHandler keyH) {
        super(gp, "Vegeta", 140, 100, 0);

        // spawn bên phải
        this.x = gp.getWidth() + 60;
        this.y = 360;
        this.color = Color.cyan;
        this.facingRight = false;

        // 🔥 LOAD ẢNH 1 LẦN
        vegetaImg = new ImageIcon(
                "src/assets/player/vegeta/hinh1.png"
        ).getImage();
    }

    public Vegeta() {
        super("Vegeta", 120, 100, 0);
        vegetaImg = new ImageIcon(
                "src/assets/player/vegeta/hinh.png"
        ).getImage();
    }

    public Vegeta(Player vegeta) {
        super(vegeta);
        vegetaImg = new ImageIcon(
                "src/assets/player/vegeta/hinh.png"
        ).getImage();
    }

    // ================= DRAW =================
    @Override
    public void draw(Graphics2D g2) {

        int drawW = width * 4;
        int drawH = height * 4;

        if (facingRight) {
            g2.drawImage(vegetaImg, x, y, drawW, drawH, null);
        } else {
            // 🔥 LẬT ẢNH NGANG
            g2.drawImage(
                    vegetaImg,
                    x + drawW,
                    y,
                    -drawW,
                    drawH,
                    null
            );
        }
    }

    // ================= SKILL =================
    @Override
    public int getManaCost(int skillIndex) {
        switch (skillIndex) {
            case 1: return 15;
            case 2: return 23;
            case 3: return 40;
            case 4: return -50;
            default: return 0;
        }
    }

    @Override
    public int getSkillDamage(int skillIndex) {
        switch (skillIndex) {
            case 1: return 5;
            case 2: return 16;
            case 3: return 22;
            case 5: return 36;
            default: return 0;
        }
    }

    @Override
    public String getSkillName(int skillIndex) {
        switch (skillIndex) {
            case 1: return "Punch";
            case 2: return "Ki Blast";
            case 3: return "Kamehameha";
            case 4: return "Recover";
            case 5: return "Quick Jab";
            default: return "";
        }
    }
}
