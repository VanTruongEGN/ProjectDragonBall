package module;

import controller.KeyHandler;
import view.GamePanel;

import javax.swing.*;
import java.awt.*;

public class Gohan extends Player {
    KeyHandler keyH = new KeyHandler();
    ImageIcon gohan;
    public Gohan(GamePanel gp, KeyHandler keyH) {
        super(gp, "Gohan", 120, 100,0);
        this.x = 120;
        this.y = 360;
        this.color = Color.orange;
        this.facingRight = true;
    }
    public Gohan() {
        super( "Gohan", 120, 100,0);
    }

    public Gohan(Player Gohan) {
        super(Gohan);
    }

    @Override
    public void draw(Graphics2D g2) {
        gohan = new ImageIcon("src/assets/player/gohan/Thiết kế chưa có tên (31).png");
        g2.setColor(color);
        g2.drawImage(gohan.getImage(), x, y,width*3,height*4, null);
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
            default: return 0;
        }
    }

    @Override
    public int getSkillDamage(int skillIndex) {
        switch (skillIndex) {
            case 1: return 8;
            case 2: return 27;
            case 3: return 28;
            case 5: return 38;
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
