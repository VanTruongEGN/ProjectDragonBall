package module;

import controller.KeyHandler;
import view.GamePanel;

import javax.swing.*;
import java.awt.*;

public class Krillin extends Player {

    KeyHandler keyH = new KeyHandler();
    private Image krillinImg;

    public Krillin(GamePanel gp, KeyHandler keyH) {
        super(gp, "Krillin", 120, 100, 0);
        this.x = 120;
        this.y = 360;
        this.color = Color.orange;
        this.facingRight = true;

        // 🔥 LOAD ẢNH 1 LẦN
        krillinImg = new ImageIcon(
                "src/assets/player/krillin/Thiết kế chưa có tên (37).png"
        ).getImage();
    }

    public Krillin() {
        super("Krillin", 120, 100, 0);
        krillinImg = new ImageIcon(
                "src/assets/player/krillin/Thiết kế chưa có tên (37).png"
        ).getImage();
    }

    public Krillin(Player krillin) {
        super(krillin);
        krillinImg = new ImageIcon(
                "src/assets/player/krillin/Thiết kế chưa có tên (37).png"
        ).getImage();
    }

    // ================= DRAW =================
    @Override
    public void draw(Graphics2D g2) {

        int drawW = width * 3;
        int drawH = height * 4;

        if (facingRight) {
            g2.drawImage(krillinImg, x, y, drawW, drawH, null);
        } else {
            // 🔥 LẬT ẢNH NGANG
            g2.drawImage(
                    krillinImg,
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
            case 2: return 16;
            case 3: return 33;
            case 4: return -50;
            default: return 0;
        }
    }

    @Override
    public int getSkillDamage(int skillIndex) {
        switch (skillIndex) {
            case 1: return 6;
            case 2: return 14;
            case 3: return 24;
            case 5: return 33;
            default: return 0;
        }
    }

    @Override
    public String getSkillName(int skillIndex) {
        switch (skillIndex) {
            case 1: return "Punch(J)";
            case 2: return "Ki Blast(K)";
            case 3: return "Kamehameha(L)";
            case 4: return "Recover(M)";
            case 5: return "Quick Jab(H)";
            default: return "";
        }
    }
}
