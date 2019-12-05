package app;

/**
 * Bullet
 */
public class Bullet extends Flyer {

    // 子弹发射速度
    private int ySpeed = 2;

    // 子弹坐标受到英雄机的坐标影响
    public Bullet(int x, int y){
        this.x = x;
        this.y = y;
        this.image = AirplaneWar.bulletBg;
        this.width = image.getWidth();
        this.height = image.getHeight();

    }

    @Override
    public void step() {
        
        this.y -= this.ySpeed;
    }

    @Override
    public boolean outOfBounds() {
        // 判断是否超出上边界
        return this.y + this.height <= 0;
    }
}