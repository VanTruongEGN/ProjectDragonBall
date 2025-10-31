package module;
import controller.KeyHandler;
import view.GamePanel;

import java.awt.*;

public abstract class Player {
    protected int x, y, speed;
    protected Image up, down, left, right;
    protected String direction = "stand";
    KeyHandler keyH;
    GamePanel gp;

    public Player(GamePanel gp, KeyHandler keyH) {
        this.gp = gp;
        this.keyH = keyH;
        setDefaultValues();
        loadPlayerImages();
    }

    public abstract void setDefaultValues() ;

    public abstract void loadPlayerImages() ;

    public void update() {
        if (keyH.upPressed) {
            direction = "up";
            y -= speed;
        } else if (keyH.downPressed) {
            direction = "down";
            y += speed;
        } else if (keyH.leftPressed) {
            direction = "left";
            x -= speed;
        } else if (keyH.rightPressed) {
            direction = "right";
            x += speed;
        }else{
            direction = "stand";
        }
    }

    public void draw(Graphics2D g2) {
        Image img = null;
        switch (direction) {
            case "up" -> img = up;
            case "down" -> img = down;
            case "left" -> img = left;
            case "right" -> img = right;
            case "stand"  -> img = down;
        }
        g2.drawImage(img, x, y, gp.getTileSize(), gp.getTileSize(), null);
    }
}
