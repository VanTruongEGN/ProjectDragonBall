package AI;

import module.*;

import java.util.Arrays;

public class MiniMax {
    public static GameState current;
    public MiniMax() {
    }
    public Player createPlayer(Player player) {
        if(player instanceof Vegeta){
            return new Vegeta(player);
        }
        if(player instanceof Goku){
            return new Goku(player);
        }
        if(player instanceof Gohan){
            return  new Gohan(player);
        }
        if (player instanceof Krillin) {
            return new Krillin(player);
        }
        if (player instanceof Piccolo) {
            return new Piccolo(player);
        }
        if (player instanceof Trunks) {
            return new Trunks(player);
        }
        return null;
    }
    public int[] skillCanUseInState(Player player) {
        int[] skill = new int[5];
        Arrays.fill(skill, -1);
        for(int i = 0; i < skill.length; i++){
            if(player.getManaCost(i+1)<=player.mana) {
                skill[i] = i + 1;
            }
        }
        if(skill[4]!=-1 && player.strong<100){
            skill[4]=-1;
        }
        if(skill[3]!=-1 && player.mana>player.getManaCost(1)){
            skill[3]=-1;
        }
        return skill;
    }

    public int heuristic(GameState state) {
        if(state.goku.hp<=0){
            return 1000000;
        }
        if(state.vegeta.hp<=0){
            return -1000000;
        }
        return (state.vegeta.hp - state.goku.hp) * 10
                + (state.vegeta.mana - state.goku.mana) * 2;
    }

    public int minimax(boolean isMax, GameState node, int depth) {
        Player vegeta;
        Player goku;
        if (depth == 0 || node.vegeta.hp <= 0 || node.goku.hp <= 0) {
            current = node;
            return heuristic(node);
        }
        if (isMax) {
            int temp = -99999999;
            for (int skill : skillCanUseInState(node.vegeta)) {
                if (skill != -1) {
                    vegeta = createPlayer(node.vegeta);
                    goku = createPlayer(node.goku);
                    int manaV = vegeta.mana - vegeta.getManaCost(skill);
                    vegeta.setManaUse(manaV);
                    vegeta.setStrong();
                    vegeta.strong = vegeta.strong>=vegeta.maxStrong?vegeta.maxStrong:vegeta.strong;
                    int hpG = goku.hp - vegeta.getSkillDamage(skill);
                    goku.setHP(hpG);
                    node.addNewState(new GameState(goku,vegeta,skill));
                }
            }
            for (GameState state : node.listState) {
                int value = minimax(false, state, depth - 1);
                if(value > temp){
                    temp = value;
                }
            }
            return temp;
        }
        else {
            int temp = 99999999;
            for (int skill : skillCanUseInState(node.goku)) {
                if (skill != -1) {
                    goku = createPlayer(node.goku);
                    vegeta = createPlayer(node.vegeta);
                    int manaG = goku.mana - goku.getManaCost(skill);
                    goku.setManaUse(manaG);
                    goku.setStrong();
                    goku.strong = goku.strong>=goku.maxStrong?goku.maxStrong:goku.strong;
                    int hpV = vegeta.hp - goku.getSkillDamage(skill);
                    vegeta.setHP(hpV);
                    node.addNewState(new GameState(goku,vegeta,skill));
                }
            }
            for (GameState state : node.listState) {
                int value = minimax(true, state, depth - 1);
                if(value < temp){
                    temp = value;
                }
            }
            return temp;
        }
    }

    public int getBestSkill(GameState root, int depth) {
        int bestValue = -99999999;
        int bestSkill = -1;

        for (int skill : skillCanUseInState(root.vegeta)) {
            if (skill != -1) {
                Player vegeta = new Vegeta(root.vegeta);
                Player goku = new Goku(root.goku);

                vegeta.setManaUse(vegeta.mana - vegeta.getManaCost(skill));
                vegeta.setStrong();
                vegeta.strong = Math.min(vegeta.strong, vegeta.maxStrong);

                goku.setHP(goku.hp - vegeta.getSkillDamage(skill));

                GameState child = new GameState(goku, vegeta, skill);

                int value = minimax(false, child, depth - 1);

                if (value > bestValue) {
                    bestValue = value;
                    bestSkill = skill;
                }
            }
        }
        return bestSkill;
    }

    public String currentState(GameState state) {
        if(heuristic(state)==1000000){
            return "Máy thắng";
        }
        if(heuristic(state)==-1000000){
            return "Máy thua";
        }
        return "HP người chơi: "+ state.goku.hp+"\t Mana người chơi: " +state.goku.mana +"\tThanh nộ: "+ state.goku.strong +"\n"+
                "HP Máy: "+ state.vegeta.hp+"\t Mana máy: " +state.vegeta.mana +"\tThanh nộ máy: "+ state.vegeta.strong;
    }




    public static void main(String[] args) {
        MiniMax mm = new MiniMax();
    }
}
