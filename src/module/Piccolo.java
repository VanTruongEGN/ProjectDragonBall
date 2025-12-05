package module;

import controller.KeyHandler;
import view.GamePanel;

import javax.swing.*;
import java.awt.*;

public class Piccolo extends Player {
    KeyHandler keyH = new KeyHandler();
    public Piccolo(GamePanel gp, KeyHandler keyH) {
        super(gp, "Piccolo", 180, 100,100);
        this.x = 120;
        this.y = 360;
        this.color = Color.orange;
        this.facingRight = true;
    }

    @Override
    public void draw(Graphics2D g2) {
        ImageIcon Piccolo = new ImageIcon("src/assets/player/piccolo/Thiết kế chưa có tên (36).png");
        g2.setColor(color);
        g2.drawImage(Piccolo.getImage(), x, y,width*3,height*4, null);
        // name
        g2.setColor(Color.white);

    }


    @Override
    public int getManaCost(int skillIndex) {
        switch (skillIndex) {
            case 1: return 10;
            case 2: return 20;
            case 3: return 40;
            case 4: return -30;
            case 5: return 0;
            default: return 0;
        }
    }

    @Override
    public int getSkillDamage(int skillIndex) {
        switch (skillIndex) {
            case 1: return 12;
            case 2: return 25;
            case 3: return 50;
            case 5: return 8;
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
