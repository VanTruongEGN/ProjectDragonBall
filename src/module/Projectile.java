package module;

import view.GamePanel;

import javax.swing.*;
import java.awt.*;

public class Projectile {
    private GamePanel gp;
    private Player owner;
    private Player target;
    private int x, y;
    private int speed; // can be negative
    private int damage;
    private boolean destroyed = false;
    private int width = 18, height = 18;


    public Projectile(GamePanel gp, Player owner, Player target, int x, int y, int speed, int damage) {
        this.gp = gp;
        this.owner = owner;
        this.target = target;
        this.x = x;
        this.y = y;
        this.speed = speed;
        this.damage = damage;
    }
    public void update() {
        x += speed;
        // simple collision: rectangle intersection with target bounding box
        Rectangle r1 = new Rectangle(x, y, width, height);
        Rectangle r2 = new Rectangle(target.getX(), target.getY(), 40, 60);
        if (r1.intersects(r2)) {
            target.takeDamage(damage);
            destroyed = true;
        }
    }
    public void draw(Graphics2D g2) {
        ImageIcon goku = new ImageIcon("src/assets/player/goku/skill1.png");
        g2.drawImage(goku.getImage(), x, y-20, width*4, height*4, null);
    }
    public boolean isDestroyed() { return destroyed; }
}

