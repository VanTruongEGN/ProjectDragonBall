package view;

import controller.KeyHandler;
import module.*;
import view.GamePanel;

public class PlayerFactory {

    public static Player create(String name, GamePanel gp, KeyHandler keyH) {
        return switch (name) {
            case "Vegeta" -> new Vegeta(gp, keyH);
            case "Gohan" -> new Gohan(gp, keyH);
            case "Trunks" -> new Trunks(gp, keyH);
            case "Piccolo" -> new Piccolo(gp, keyH);
            case "Krillin" -> new Krillin(gp, keyH);
            default -> new Goku(gp, keyH);
        };
    }
}
