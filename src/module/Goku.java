package module;

import controller.KeyHandler;
import view.GamePanel;

import java.awt.*;

public class Goku extends Player {
    private KeyHandler keyH;

    public Goku(GamePanel gp, KeyHandler keyH) {
        super(gp, "Goku");
        this.keyH = keyH;
        this.x = 120;
        this.y = 360;
        this.color = Color.orange;
        this.facingRight = true;
    }

    @Override
    public void draw(Graphics2D g2) {
        // simple rectangle as character
        g2.setColor(color);
        g2.fillRect(x, y, 40, 60);
        // name
        g2.setColor(Color.white);
        g2.drawString(name, x, y - 8);
    }

    @Override
    public int getManaCost(int skillIndex) {
        switch (skillIndex) {
            case 1: return 10; // small
            case 2: return 25; // medium
            case 3: return 45; // big
            default: return 0;
        }
    }

    @Override
    public int getSkillDamage(int skillIndex) {
        switch (skillIndex) {
            case 1: return 12;
            case 2: return 25;
            case 3: return 50;
            default: return 0;
        }
    }

    @Override
    public String getSkillName(int skillIndex) {
        switch (skillIndex) {
            case 1: return "Punch";
            case 2: return "Ki Blast";
            case 3: return "Kamehameha";
            default: return "";
        }
    }

    // Goku is player-controlled; no auto skill
    @Override
    public Projectile performAutoSkill(Player target) { return null; }
}
