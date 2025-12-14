package AI;

import module.Goku;
import module.Player;
import module.Vegeta;

import java.util.Arrays;

public class AlpheBeta {

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

    public int minimaxAlphaBeta(boolean isMax, GameState node, int depth, int alpha, int beta) {
        if (depth == 0 || node.vegeta.hp <= 0 || node.goku.hp <= 0) {
            return heuristic(node);
        }

        if (isMax) {
            int value = -99999999;

            // sinh nhánh
            for (int skill : skillCanUseInState(node.vegeta)) {
                if (skill != -1) {
                    Player vegeta = new Vegeta(node.vegeta);
                    Player goku = new Goku(node.goku);

                    vegeta.setManaUse(vegeta.mana - vegeta.getManaCost(skill));
                    vegeta.setStrong();
                    vegeta.strong = Math.min(vegeta.strong, vegeta.maxStrong);

                    goku.setHP(goku.hp - vegeta.getSkillDamage(skill));

                    GameState child = new GameState(goku, vegeta, skill);

                    value = Math.max(value, minimaxAlphaBeta(false, child, depth - 1, alpha, beta));
                    alpha = Math.max(alpha, value);
                    if (alpha >= beta) break; // cắt tỉa
                }
            }
            return value;
        } else {
            int value = 99999999;

            for (int skill : skillCanUseInState(node.goku)) {
                if (skill != -1) {
                    Player goku = new Goku(node.goku);
                    Player vegeta = new Vegeta(node.vegeta);

                    goku.setManaUse(goku.mana - goku.getManaCost(skill));
                    goku.setStrong();
                    goku.strong = Math.min(goku.strong, goku.maxStrong);

                    vegeta.setHP(vegeta.hp - goku.getSkillDamage(skill));

                    GameState child = new GameState(goku, vegeta, skill);

                    value = Math.min(value, minimaxAlphaBeta(true, child, depth - 1, alpha, beta));
                    beta = Math.min(beta, value);
                    if (alpha >= beta) break; // cắt tỉa
                }
            }
            return value;
        }
    }

    public int getBestSkillAlphaBeta(GameState root, int depth) {
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

                int value = minimaxAlphaBeta(false, child, depth - 1, -99999999, 99999999);

                if (value > bestValue) {
                    bestValue = value;
                    bestSkill = skill;
                }
            }
        }
        return bestSkill;
    }
}
