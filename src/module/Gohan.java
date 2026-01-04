package module;

import controller.KeyHandler;
import view.GamePanel;

import javax.swing.*;
import java.awt.*;

public class Gohan extends Player {

    KeyHandler keyH = new KeyHandler();
    private Image gohanImg;

    public Gohan(GamePanel gp, KeyHandler keyH) {
        super(gp, "Gohan", 120, 100, 0);
        this.x = 120;
        this.y = 360;
        this.color = Color.orange;
        this.facingRight = true;

        // 🔥 LOAD ẢNH 1 LẦN
        gohanImg = new ImageIcon(
                "src/assets/player/gohan/Thiết kế chưa có tên (31).png"
        ).getImage();
    }

    public Gohan() {
        super("Gohan", 120, 100, 0);
        gohanImg = new ImageIcon(
                "src/assets/player/gohan/Thiết kế chưa có tên (31).png"
        ).getImage();
    }

    public Gohan(Player gohan) {
        super(gohan);
        gohanImg = new ImageIcon(
                "src/assets/player/gohan/Thiết kế chưa có tên (31).png"
        ).getImage();
    }

    // ================= DRAW =================
    @Override
    public void draw(Graphics2D g2) {

        int drawW = width * 3;
        int drawH = height * 4;

        if (facingRight) {
            g2.drawImage(gohanImg, x, y, drawW, drawH, null);
        } else {
            // 🔥 LẬT ẢNH NGANG
            g2.drawImage(
                    gohanImg,
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
            case 2: return 27;
            case 3: return 28;
            case 5: return 38;
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
