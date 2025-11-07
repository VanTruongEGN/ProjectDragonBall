package module;

import view.GamePanel;

import java.awt.*;
import java.util.Random;

public class Vegeta extends Player {
    private Random rand = new Random();
    private int cooldown = 0;

    public Vegeta(GamePanel gp) {
        super(gp, "Vegeta");
        this.x = gp.getWidth() - 160; // later gp.getWidth may be 0 during init; GamePanel uses fixed width so it's fine
        this.y = 360;
        this.color = Color.cyan;
        this.facingRight = false;
    }

    @Override
    public void draw(Graphics2D g2) {
        g2.setColor(color);
        g2.fillRect(x, y, 40, 60);
        g2.setColor(Color.white);
        g2.drawString(name, x, y - 8);
    }

    @Override
    public int getManaCost(int skillIndex) {
        switch (skillIndex) {
            case 1: return 8;
            case 2: return 20;
            case 3: return 40;
            default: return 0;
        }
    }

    @Override
    public int getSkillDamage(int skillIndex) {
        switch (skillIndex) {
            case 1: return 10;
            case 2: return 22;
            case 3: return 48;
            default: return 0;
        }
    }

    @Override
    public String getSkillName(int skillIndex) {
        switch (skillIndex) {
            case 1: return "Quick Strike";
            case 2: return "Galick Gun";
            case 3: return "Final Flash";
            default: return "";
        }
    }

    // Vegeta AI: choose a skill randomly but prefer skills it can afford
    @Override
    public Projectile performAutoSkill(Player target) {
        if (cooldown > 0) { cooldown--; return null; }

        // try random order but ensure at least one affordable skill
        int[] order = {3, 2, 1}; // prefer big, then medium, then small
        // try to find best affordable
        for (int s : order) {
            if (canUseSkill(s)) {
                cooldown = 40 + rand.nextInt(40); // some cooldown so not too spammy
                return useSkill(s, target);
            }
        }
        // if nothing affordable, maybe skip and regen mana next turn (GamePanel will regen)
        return null;
    }
}
