package module;

import controller.KeyHandler;
import view.GamePanel;

import javax.swing.*;

public class Goku extends Player{
    private String id = "goku";
    public Goku(GamePanel gp, KeyHandler keyH) {
        super(gp, keyH);
    }

    @Override
    public void setDefaultValues() {
        x = 100;
        y = 100;
        speed = 4;
    }

    @Override
    public void loadPlayerImages() {
        up = new ImageIcon("src/assets/player/hinh.png").getImage();
        down = new ImageIcon("src/assets/player/hinh.png").getImage();
        left = new ImageIcon("src/assets/player/left.png").getImage();
        right = new ImageIcon("src/assets/player/right.png").getImage();
    }

}
