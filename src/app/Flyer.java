package app;

import java.awt.image.*;;

/**
 * Flyer
 */
public abstract class Flyer {

    protected int width;
    protected int height;
    protected int x;
    protected int y;
    protected BufferedImage image;

    // 飞行物移动方法
    public abstract void step();
    // 飞行物是否超出边界
    public abstract boolean outOfBounds();

    // 判断飞行物之间是否碰撞
    public static boolean isCrash(Flyer f1, Flyer f2) {
        // 计算飞行物中心点
        int f1x = f1.x + f1.width / 2;
        int f1y = f1.y + f1.height / 2;
        int f2x = f2.x + f2.width / 2;
        int f2y = f2.y + f2.height / 2;
        // 计算是否碰撞 横向/纵向碰撞
        boolean h = Math.abs(f1x - f2x) < (f1.width + f2.width) / 2;
        boolean v = Math.abs(f1y - f2y) < (f1.height + f2.height) / 2;

        return h & v;
    }
}