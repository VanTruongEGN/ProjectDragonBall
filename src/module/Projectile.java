package module;

import view.GamePanel;
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
        // if out of screen -> destroyed
        if (x + width < 0 || x > gp.getWidth()) {
            destroyed = true;
        }

        // simple collision: rectangle intersection with target bounding box
        Rectangle r1 = new Rectangle(x, y, width, height);
        Rectangle r2 = new Rectangle(target.getX(), target.getY(), 40, 60);
        if (r1.intersects(r2)) {
            target.takeDamage(damage);
            destroyed = true;
        }
    }

    public void draw(Graphics2D g2) {
        if (owner.getName().equals("Goku")) g2.setColor(Color.yellow);
        else g2.setColor(Color.magenta);
        g2.fillOval(x, y, width, height);
    }

    public boolean isDestroyed() { return destroyed; }
}

