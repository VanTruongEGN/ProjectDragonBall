package AI;

import module.Player;

import java.util.Arrays;

public class Greedy {
    private Player goku;
    private Player vegeta;
    public Greedy() {}

    public int heristicGreedy(Player goku, Player vegeta) {
        int[] skillCanUse = new int[5];
        Arrays.fill(skillCanUse, -1);
        for(int i = 0; i < skillCanUse.length; i++){
            if(vegeta.getManaCost(i+1)<=vegeta.mana){
                skillCanUse[i]=i+1;
            }
            if(skillCanUse[4]!=-1 && vegeta.getStrongRatio()<100){
                skillCanUse[4]=-1;
            }
        }
        if (vegeta.mana < vegeta.getManaCost(1)){
            return 4;
        }
        double[] heristic =  new double[5];
        for(int i = 0; i < heristic.length; i++){
            if(skillCanUse[i] != -1){
                int dmg = vegeta.getSkillDamage(skillCanUse[i]);
                if(dmg >= goku.hp){
                    heristic[i] = Double.MAX_VALUE;
                } else {
                    heristic[i] = (double)dmg / goku.hp;
                }
            } else {
                heristic[i] = -1;
            }
        }
        double best = -1;
        int bestSkill = -1;

        for(int i = 0; i < heristic.length; i++){
            if(heristic[i] > best){
                best = heristic[i];
                bestSkill = skillCanUse[i];
            }
        }

        return bestSkill;
    }
}
