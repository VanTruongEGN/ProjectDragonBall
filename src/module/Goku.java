package module;

import controller.KeyHandler;
import view.GamePanel;

import javax.swing.*;
import java.awt.*;

public class Goku extends Player {
    KeyHandler keyH = new KeyHandler();
    public Goku(GamePanel gp, KeyHandler keyH) {
        super(gp, "Goku", 120, 100,0);
        this.x = 120;
        this.y = 360;
        this.color = Color.orange;
        this.facingRight = true;
    }

    @Override
    public void draw(Graphics2D g2) {
        ImageIcon goku = new ImageIcon("src/assets/player/goku/hinh.png");
        g2.setColor(color);
        g2.drawImage(goku.getImage(), x, y,width*3,height*4, null);
        // name
        g2.setColor(Color.white);

    }


    @Override
    public int getManaCost(int skillIndex) {
        switch (skillIndex) {
            case 1: return 10;
            case 2: return 25;
            case 3: return 45;
            case 4: return -50;
            case 5: return 0;
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
