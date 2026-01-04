package AI;

import module.*;

import java.util.Arrays;

public class AlpheBeta {
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

    public int minimaxAlphaBeta(boolean isMax, GameState node, int depth, int alpha, int beta) {
        if (depth == 0 || node.vegeta.hp <= 0 || node.goku.hp <= 0) {
            return heuristic(node);
        }

        if (isMax) {
            int value = -99999999;

            // sinh nhánh
            for (int skill : skillCanUseInState(node.vegeta)) {
                if (skill != -1) {
                    Player vegeta = createPlayer(node.vegeta);
                    Player goku = createPlayer(node.goku);

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
                    Player goku = createPlayer(node.goku);
                    Player vegeta = createPlayer(node.vegeta);

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
                Player vegeta = createPlayer(root.vegeta);
                Player goku = createPlayer(root.goku);
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

    public static void main(String[] args) {
        AlpheBeta minimax = new AlpheBeta();
        Player goku = new Goku();
        Player vegeta = new Vegeta();

        for (int i = 1; i < 11; i++) {
//            System.out.println("depth: "+i);
            Runtime runtime = Runtime.getRuntime();
            runtime.gc();

            long memoryBefore = runtime.totalMemory() - runtime.freeMemory();
            long startTime = System.nanoTime();

            int result = minimax.minimaxAlphaBeta(true, new GameState(goku, vegeta), i,-99999999, 99999999);

            long endTime = System.nanoTime();
            long memoryAfter = runtime.totalMemory() - runtime.freeMemory();

            System.out.println("Execution time (ms): " + (endTime - startTime) / 1_000_000.0);
            System.out.println("Memory used (KB): " + (memoryAfter - memoryBefore) / 1024.0);
        }
    }
}
