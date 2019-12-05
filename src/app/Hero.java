package app;

import java.util.Random;

/**
 * Hero
 */
public class Hero extends Flyer {

    // 双倍火力剩余
    private int doubleFire = 0;
    // 生命值
    private int life = 3;
    // 获得分数
    private int score = 0;
    // 控制背景图显示
    private int bgControl = 0;

    public Hero() {
        this.image = AirplaneWar.hero1Bg;
        this.width = image.getWidth();
        this.height = image.getHeight();
        this.x = 150;
        this.y = 500;
        this.doubleFire = 0;
        this.life = 3;
        this.score = 0;
    }

    public int getScore() {

        return this.score;
    }

    /**
     * @return the life
     */
    public int getLife() {

        return life;
    }

    @Override
    public void step() {
        // （来回切换、更好看）
        this.bgControl = this.bgControl == 1 ? 2 : 1;
        this.image = this.bgControl == 1 ? AirplaneWar.hero1Bg : AirplaneWar.hero2Bg;
    }

    @Override
    public boolean outOfBounds() {
        // TODO Auto-generated method stub
        return false;
    }

    // 跟随鼠标移动方法
    public void move(int x, int y) {
        // 使英雄机的中心位置与鼠标同步
        this.x = x - this.width / 2;
        this.y = y - this.height / 2;
    }

    // 获得分数或奖励方法
    public void getScore_Award(Flyer obj) {
        // 判断敌机对象的类型
        if (obj instanceof Bee) {
            // 如果是蜜蜂 判断奖励类型
            if (((Bee) obj).getAwardType() == Bee.DOUBLE_FIRE) {
                this.doubleFire += 40;
            } else {
                this.life = life < 3 ? life + 1 : 3;
            }
        } else {
            this.score += ((Airplane) obj).getScore();
        }
    }

    // 发射子弹 可能是1发或2发
    public Bullet[] shoot() {

        Bullet[] bullets = null;
        // 默认创建1发子弹 有双倍火力时创建2发
        if (this.doubleFire > 0) {
            bullets = new Bullet[2];
            bullets[0] = new Bullet(this.x + this.width / 4 - 3, this.y - AirplaneWar.bulletBg.getHeight());
            bullets[1] = new Bullet(this.x + this.width / 4 * 3, this.y - AirplaneWar.bulletBg.getHeight());

            this.doubleFire -= 2;
        } else {
            bullets = new Bullet[1];
            bullets[0] = new Bullet(this.x + this.width / 2, this.y - AirplaneWar.bulletBg.getHeight());
        }

        return bullets;
    }

    // 是否碰撞到敌机
    public boolean hit(Flyer obj) {
        // 先判断是否发生碰撞
        boolean isCrash = Flyer.isCrash(this, obj);
        // 如果碰撞 则惩罚
        if (isCrash) {
            this.life--;
            this.doubleFire = 0;
        }
        return isCrash;
    }
}