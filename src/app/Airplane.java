package app;

import java.util.Random;

/**
 * Airplane
 */
public class Airplane extends Flyer {

    // 下落速度
    private int ySpeed = 2;
    // 分值
    private int score = 5;

    public Airplane(){
        this.image = AirplaneWar.airplaneBg;
        this.width = image.getWidth();
        this.height = image.getHeight();
        this.y = -this.height;

        Random random = new Random();

        this.x = random.nextInt(AirplaneWar.WIDTH - this.width);
    }

    public int getScore() {
        
        return this.score;
    }

    @Override
    public void step() {

        this.y += this.ySpeed;
    }

    @Override
    public boolean outOfBounds() {
        // 判断是否超出边界
        return this.y >= AirplaneWar.HEIGHT;
    }
}