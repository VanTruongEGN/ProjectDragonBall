package module;

import view.GamePanel;
import java.awt.*;

public abstract class Player {
    protected GamePanel gp;
    public int x;
    protected int y;
    public int hp;
    public int maxHp;
    public int mana;
    public int maxMana;
    protected Color color;
    protected boolean facingRight;
    protected String name;
    protected int width =18;
    protected int height =18;


    public Player(GamePanel gp, String name, int hp, int mana) {
        this.gp = gp;
        this.name = name;
        this.maxHp = hp;
        this.hp = maxHp;
        this.maxMana = mana;
        this.mana = maxMana;
    }

    // draw player
    public abstract void draw(Graphics2D g2);

    // skill definitions
    public abstract int getManaCost(int skillIndex);
    public abstract int getSkillDamage(int skillIndex);
    public abstract String getSkillName(int skillIndex);

    // perform a skill: reduce mana and create projectile towards target
    public Projectile useSkill(int skillIndex, Player target) {
        if (!canUseSkill(skillIndex)) return null;
        int dmg = getSkillDamage(skillIndex);
        setMana(skillIndex);

        int baseSpeed = 6 + skillIndex * 2;
        if (skillIndex == 5) baseSpeed = 8; // tầm gần nhưng nhanh
        int dir = (this.x < target.x) ? 1 : -1;
        int speed = baseSpeed * dir;

        int spawnX = this.x + (dir == 1 ? 30 : -30); // gần hơn
        int spawnY = this.y + 25;

        return new Projectile(gp, this, target, spawnX, spawnY, speed, dmg);
    }

    public void setMana(int skillIndex){
        mana-=getManaCost(skillIndex);
    }
    public boolean canUseSkill(int skillIndex) {
        int cost = getManaCost(skillIndex);
        if (skillIndex == 4) {
            return mana < maxMana;
        }
        return mana >= cost && skillIndex != 0;
    }


    public void takeDamage(int dmg) {
        hp -= dmg;
        if (hp < 0) hp = 0;
    }

    public double getHpRatio() { return (double) hp / maxHp; }
    public double getManaRatio() { return (double) (mana>maxMana?maxMana:mana) / maxMana; }

    public void regenMana(int amount) {
        mana += amount;
        if (mana > maxMana) mana = maxMana;
    }

    public String getName() { return name; }
    public int getX() { return x; }
    public int getY() { return y; }
}
