package module;

import controller.KeyHandler;
import view.GamePanel;

import javax.swing.*;
import java.awt.*;
import java.util.Random;


public class Trunks extends Player {
    private Random rand = new Random();
    private int cooldown = 0;
    ImageIcon trunks;
    public Trunks(GamePanel gp, KeyHandler keyH) {
        super(gp, "Trunks", 160,100,0);
        this.x = gp.getWidth()+60; // later gp.getWidth may be 0 during init; GamePanel uses fixed width so it's fine
        this.y = 360;
        this.color = Color.cyan;
        this.facingRight = false;
    }
    public Trunks() {
        super( "Trunks", 160, 100,0);
    }

    public Trunks(Player Trunks) {
        super(Trunks);
    }

    @Override
    public void draw(Graphics2D g2) {
        trunks= new ImageIcon("src/assets/player/trunks/trunks.png");
        g2.setColor(color);
        g2.drawImage(trunks.getImage(), x, y,width*4,height*4, null);
        g2.setColor(Color.white);

    }

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
            case 1: return "Punch";
            case 2: return "Ki Blast";
            case 3: return "Kamehameha";
            case 4: return "Recover";
            case 5: return "Quick Jab";
            default: return "";
        }
    }

}
