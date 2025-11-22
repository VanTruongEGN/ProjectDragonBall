package module;

import controller.KeyHandler;
import view.GamePanel;

import javax.swing.*;
import java.awt.*;

public class Goku extends Player {

    public Goku(GamePanel gp, KeyHandler keyH) {
        super(gp, "Goku", 120, 100);
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
        g2.drawString(name, x, y - 8);
    }

    @Override
    public int getManaCost(int skillIndex) {
        switch (skillIndex) {
            case 1: return 10;
            case 2: return 25;
            case 3: return 45;
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


}
