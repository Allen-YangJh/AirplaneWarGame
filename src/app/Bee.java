package app;

import java.util.Random;

/**
 * Bee
 */
public class Bee extends Flyer {

    // 奖励类型 0: 奖励双倍火力 1: 奖励生命值
    public static final int DOUBLE_FIRE = 0;
    public static final int LIFE = 1;

    // 水平速度
    protected int xSpeed = 1;
    // 下落速度
    protected int ySpeed = 2;
    // 奖励类型
    protected int awardType;

    public Bee(){
        this.image = AirplaneWar.beeBg;
        this.width = image.getWidth();
        this.height = image.getHeight();
        this.y = -this.height;

        Random random = new Random();

        this.x = random.nextInt(AirplaneWar.WIDTH - this.width);
        this.awardType = random.nextInt(2);
    }

    @Override
    public void step() {
        
        this.y += this.ySpeed;
        this.x += this.xSpeed;

        if (this.x < 0 || this.x > (AirplaneWar.WIDTH - this.width)){
            this.xSpeed *= -1;
        }
    }

    @Override
    public boolean outOfBounds() {
        // 判断是否超出边界
        return this.y >= AirplaneWar.HEIGHT;
    }

    public int getAwardType() {

        return awardType;
    }

}