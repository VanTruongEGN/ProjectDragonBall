package module;

import controller.KeyHandler;
import view.GamePanel;

import javax.swing.*;
import java.awt.*;

public class Goku extends Player {

    KeyHandler keyH = new KeyHandler();
    private Image gokuImg;

    public Goku(GamePanel gp, KeyHandler keyH) {
        super(gp, "Goku", 120, 100, 0);
        this.x = 120;
        this.y = 360;
        this.color = Color.orange;
        this.facingRight = true;

        // 🔥 LOAD ẢNH 1 LẦN
        gokuImg = new ImageIcon("src/assets/player/goku/hinh.png").getImage();
    }

    public Goku() {
        super("Goku", 120, 100, 0);
        gokuImg = new ImageIcon("src/assets/player/goku/hinh.png").getImage();
    }

    public Goku(Player goku) {
        super(goku);
        gokuImg = new ImageIcon("src/assets/player/goku/hinh.png").getImage();
    }

    // ================= DRAW =================
    @Override
    public void draw(Graphics2D g2) {

        int drawW = width * 3;
        int drawH = height * 4;

        if (facingRight) {
            g2.drawImage(gokuImg, x, y, drawW, drawH, null);
        } else {
            // 🔥 LẬT ẢNH NGANG
            g2.drawImage(
                    gokuImg,
                    x + drawW,   // dịch sang phải
                    y,
                    -drawW,      // width âm => lật ảnh
                    drawH,
                    null
            );
        }
    }

    // ================= SKILL =================
    @Override
    public int getManaCost(int skillIndex) {
        switch (skillIndex) {
            case 1: return 10;
            case 2: return 25;
            case 3: return 45;
            case 4: return -50;
            default: return 0;
        }
    }

    @Override
    public int getSkillDamage(int skillIndex) {
        switch (skillIndex) {
            case 1: return 8;
            case 2: return 19;
            case 3: return 29;
            case 5: return 40;
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
