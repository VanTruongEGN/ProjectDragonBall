package module;

import controller.KeyHandler;
import view.GamePanel;

import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class Trunks extends Player {

    private Random rand = new Random();
    private int cooldown = 0;
    private Image trunksImg;

    public Trunks(GamePanel gp, KeyHandler keyH) {
        super(gp, "Trunks", 160, 100, 0);

        // spawn bên phải
        this.x = gp.getWidth() + 60;
        this.y = 360;
        this.color = Color.cyan;
        this.facingRight = false;

        // 🔥 LOAD ẢNH 1 LẦN
        trunksImg = new ImageIcon(
                "src/assets/player/trunks/trunks.png"
        ).getImage();
    }

    public Trunks() {
        super("Trunks", 160, 100, 0);
        trunksImg = new ImageIcon(
                "src/assets/player/trunks/trunks.png"
        ).getImage();
    }

    public Trunks(Player trunks) {
        super(trunks);
        trunksImg = new ImageIcon(
                "src/assets/player/trunks/trunks.png"
        ).getImage();
    }

    // ================= DRAW =================
    @Override
    public void draw(Graphics2D g2) {

        int drawW = width * 4;
        int drawH = height * 4;

        if (facingRight) {
            g2.drawImage(trunksImg, x, y, drawW, drawH, null);
        } else {
            // 🔥 LẬT ẢNH NGANG
            g2.drawImage(
                    trunksImg,
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
            case 1: return 11;
            case 2: return 25;
            case 3: return 40;
            case 4: return -50;
            default: return 0;
        }
    }

    @Override
    public int getSkillDamage(int skillIndex) {
        switch (skillIndex) {
            case 1: return 6;
            case 2: return 13;
            case 3: return 22;
            case 5: return 36;
            default: return 0;
        }
    }

    @Override
    public String getSkillName(int skillIndex) {
        switch (skillIndex) {
            case 1: return "Punch(J)";
            case 2: return "Ki Blast(K)";
            case 3: return "Automic(L)";
            case 4: return "Recover(M)";
            case 5: return "Quick Jab(H)";
            default: return "";
        }
    }
}
