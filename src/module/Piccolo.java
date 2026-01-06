package module;

import controller.KeyHandler;
import view.GamePanel;

import javax.swing.*;
import java.awt.*;

public class Piccolo extends Player {

    KeyHandler keyH = new KeyHandler();
    private Image piccoloImg;

    public Piccolo(GamePanel gp, KeyHandler keyH) {
        super(gp, "Piccolo", 180, 100, 0);
        this.x = 120;
        this.y = 360;
        this.color = Color.orange;
        this.facingRight = true;

        // 🔥 LOAD ẢNH 1 LẦN
        piccoloImg = new ImageIcon(
                "src/assets/player/piccolo/Thiết kế chưa có tên (36).png"
        ).getImage();
    }

    public Piccolo() {
        super("Piccolo", 180, 100, 0);
        piccoloImg = new ImageIcon(
                "src/assets/player/piccolo/Thiết kế chưa có tên (36).png"
        ).getImage();
    }

    public Piccolo(Player piccolo) {
        super(piccolo);
        piccoloImg = new ImageIcon(
                "src/assets/player/piccolo/Thiết kế chưa có tên (36).png"
        ).getImage();
    }

    // ================= DRAW =================
    @Override
    public void draw(Graphics2D g2) {

        int drawW = width * 3;
        int drawH = height * 4;

        if (facingRight) {
            g2.drawImage(piccoloImg, x, y, drawW, drawH, null);
        } else {
            // 🔥 LẬT ẢNH NGANG
            g2.drawImage(
                    piccoloImg,
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
            case 2: return 18;
            case 3: return 30;
            case 4: return -50;
            default: return 0;
        }
    }

    @Override
    public int getSkillDamage(int skillIndex) {
        switch (skillIndex) {
            case 1: return 5;
            case 2: return 13;
            case 3: return 34;
            case 5: return 37;
            default: return 0;
        }
    }

    @Override
    public String getSkillName(int skillIndex) {
        switch (skillIndex) {
            case 1: return "Punch(J)";
            case 2: return "Ki Blast(K)";
            case 3: return "Laze(L)";
            case 4: return "Recover(M)";
            case 5: return "Quick Jab(H)";
            default: return "";
        }
    }
}
