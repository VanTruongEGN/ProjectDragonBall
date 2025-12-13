package module;

import controller.KeyHandler;
import view.GamePanel;

import javax.swing.*;
import java.awt.*;
import java.util.Random;


public class Vegeta extends Player {
    ImageIcon vegeta;

    public Vegeta(GamePanel gp, KeyHandler keyH) {
        super(gp, "Vegeta", 140,100,0);
        this.x = gp.getWidth()+60; // later gp.getWidth may be 0 during init; GamePanel uses fixed width so it's fine
        this.y = 360;
        this.color = Color.cyan;
        this.facingRight = false;
    }
    public Vegeta() {
        super( "Vegeta", 120, 100,0);
    }

    public Vegeta(Player vegeta) {
        super(vegeta);
    }

    @Override
    public void draw(Graphics2D g2) {
         vegeta = new ImageIcon("src/assets/player/vegeta/hinh.png");
        g2.setColor(color);
        g2.drawImage(vegeta.getImage(), x, y,width*4,height*4, null);
        g2.setColor(Color.white);

    }

    @Override
    public int getManaCost(int skillIndex) {
        switch (skillIndex) {
            case 1: return 15;
            case 2: return 23;
            case 3: return 40;
            case 4: return -50;
            default: return 0;
        }
    }

    @Override
    public int getSkillDamage(int skillIndex) {
        switch (skillIndex) {
            case 1: return 5;
            case 2: return 16;
            case 3: return 22;
            case 5: return 36;
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
