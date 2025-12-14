package module;

import controller.KeyHandler;
import view.GamePanel;

import javax.swing.*;
import java.awt.*;

public class Krillin extends Player {
    KeyHandler keyH = new KeyHandler();
    public Krillin(GamePanel gp, KeyHandler keyH) {
        super(gp, "Krillin", 120, 100,0);
        this.x = 120;
        this.y = 360;
        this.color = Color.orange;
        this.facingRight = true;
    }
    public Krillin() {
        super( "Krillin", 120, 100,0);
    }

    public Krillin(Player Krillin) {
        super(Krillin);
    }

    @Override
    public void draw(Graphics2D g2) {
        ImageIcon Krillin = new ImageIcon("src/assets/player/krillin/Thiết kế chưa có tên (37).png");
        g2.setColor(color);
        g2.drawImage(Krillin.getImage(), x, y,width*3,height*4, null);
        // name
        g2.setColor(Color.white);

    }


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
            case 1: return "Punch";
            case 2: return "Ki Blast";
            case 3: return "Kamehameha";
            case 4: return "Recover";
            case 5: return "Quick Jab";
            default: return "";
        }
    }
}

