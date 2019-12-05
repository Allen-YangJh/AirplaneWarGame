package app;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.Arrays;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

/**
 * AirplaneWar
 */
public class AirplaneWar extends JPanel {

    // 游戏界面大小 400*700
    public static final int WIDTH = 400;
    public static final int HEIGHT = 700;

    // 静态图片资源
    public static BufferedImage mainBg;
    public static BufferedImage startBg;
    public static BufferedImage airplaneBg;
    public static BufferedImage beeBg;
    public static BufferedImage bulletBg;
    public static BufferedImage hero1Bg;
    public static BufferedImage hero2Bg;
    public static BufferedImage pauseBg;
    public static BufferedImage gameoverBg;

    // 管理飞行物对象
    public Hero hero = new Hero();
    public Flyer[] flyers = {};
    public Bullet[] bullets = {};

    // 游戏状态
    private int state;
    // 状态常量
    private final int START = 0;
    private final int RUNNING = 1;
    private final int PAUSE = 2;
    private final int GAMEOVER = 3;

    static {
        try {
            mainBg = ImageIO.read(AirplaneWar.class.getResource("static/images/main.png"));
            System.out.println("mainBg is loaded!");
            startBg = ImageIO.read(AirplaneWar.class.getResource("static/images/start.png"));
            System.out.println("startBg is loaded!");
            airplaneBg = ImageIO.read(AirplaneWar.class.getResource("static/images/airplane.png"));
            System.out.println("airplaneBg is loaded!");
            beeBg = ImageIO.read(AirplaneWar.class.getResource("static/images/bee.png"));
            System.out.println("beeBg is loaded!");
            bulletBg = ImageIO.read(AirplaneWar.class.getResource("static/images/bullet.png"));
            System.out.println("bulletBg is loaded!");
            hero1Bg = ImageIO.read(AirplaneWar.class.getResource("static/images/hero1.png"));
            System.out.println("hero1Bg is loaded!");
            hero2Bg = ImageIO.read(AirplaneWar.class.getResource("static/images/hero2.png"));
            System.out.println("hero2Bg is loaded!");
            pauseBg = ImageIO.read(AirplaneWar.class.getResource("static/images/pause.png"));
            System.out.println("pauseBg is loaded!");
            gameoverBg = ImageIO.read(AirplaneWar.class.getResource("static/images/gameover.png"));
            System.out.println("gameoverBg is loaded!");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        // Java中绘制窗体
        // 1.JFrame对象——窗口（框）
        JFrame frame = new JFrame("War");
        // 设置窗体大小
        frame.setSize(WIDTH, HEIGHT);
        // 设置窗体置顶
        frame.setAlwaysOnTop(true);
        // 设置窗体关闭时同时退出程序
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // 设置窗体初始位置 null标示居中
        frame.setLocationRelativeTo(null);

        // 创建背景面板对象
        AirplaneWar airplaneWar = new AirplaneWar();

        // 在窗体中嵌入背景面板
        frame.add(airplaneWar);
        // 窗体默认不可见，需手动显示
        frame.setVisible(true);
        // 开始游戏
        airplaneWar.action();
    }

    // 游戏开始
    public void action() {
        // 创建鼠标响应事件
        MouseAdapter adapter = new MouseAdapter() {

            @Override
            public void mouseMoved(MouseEvent e) {
                // 游戏进行中 英雄机移动
                if (state == RUNNING) {
                    hero.move(e.getX(), e.getY());
                }
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                // 在开始状态下单击 才修改状态
                if (state == START){
                    state = RUNNING;
                } 
                // 在游戏结束时 重新开始
                else if (state == GAMEOVER) {
                    // 初始化数据
                    flyers = new Flyer[0];
                    bullets = new Bullet[0];
                    hero = new Hero();

                    state = RUNNING;
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                // 在游戏暂停时
                if (state == PAUSE) {
                    state = RUNNING;
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                // 在游戏进行中时
                if (state == RUNNING){
                    state = PAUSE;
                }
            }
            
        };
        // 添加鼠标监听器
        this.addMouseMotionListener(adapter);   // 支持鼠标移动监听
        this.addMouseListener(adapter);         // 支持鼠标单击监听

        // 通过定时器 创建飞行物对象 移动现有对象
        Timer timer = new Timer();
        timer.schedule(new TimerTask(){

            private int runCount = 0;
            @Override
            public void run() {
                // 游戏进行中时执行
                if (state == RUNNING){
                    runCount++;
                    // 每隔500毫秒创建新敌机
                    if (runCount % 50 == 0){
                        newFlyer();
                    }
                    // 每隔10毫秒 使现有敌机对象移动
                    for (int i = 0; i < flyers.length; i++) {
                        flyers[i].step();
                    }
                    // 每隔300毫秒创建子弹
                    if (runCount % 30 == 0) {
                        newBullet();
                    }
                    // 每隔10毫秒 使现有子弹对象移动
                    for (int j = 0; j < bullets.length; j++) {
                        bullets[j].step();
                    }
                    // 英雄机移动效果 （增大间隔、更形象)
                    if (runCount % 3 == 0){
                        hero.step();
                    }
                    // 子弹击中敌人检测
                    crash();
                    // 英雄机与敌机碰撞检测
                    hit();
                    // 飞行物出界销毁检测
                    outOfBounds();
                }
                
                // 重新绘制界面
                repaint();
            }

        }, 100, 10);
    }

    @Override
    public void paint(Graphics g) {
        /* // 画线
        g.drawLine(10, 10, 100, 100);
        // 画矩形
        g.drawRect(10, 10, 100, 100);
        // 画圆
        g.drawOval(10, 10, 100, 100); */
        // 1.画背景图片
        g.drawImage(mainBg, 0, 0, null);
        // 2.画出英雄机
        g.drawImage(hero.image, hero.x, hero.y, null);
        // 3.画出敌机
        paintFlyers(g);
        // 4.画出子弹
        paintBullets(g);
        // 5.显示得分与生命
        printScore_Life(g);

        /* // 测试出界销毁方法是否生效
        g.drawString("liveFlyers:" + this.flyers.length, 250, 20);
        g.drawString("liveBullets" + this.bullets.length, 250, 40); */

        // 根据游戏状态 画不同界面
        switch (state) {
            case START:
                g.drawImage(startBg, 0, 0, null);
                break;
            case PAUSE:
                g.drawImage(pauseBg, 0, 0, null);
                break;
            case GAMEOVER:
                g.drawImage(gameoverBg, 0, 0, null);
                break;
        }
    }

    // 批量绘制敌机方法
    public void paintFlyers(Graphics g) {

        for (int i = 0; i < flyers.length; i++) {
            g.drawImage(flyers[i].image, flyers[i].x, flyers[i].y, null);
        }
    }

    // 批量绘制子弹方法
    public void paintBullets(Graphics g) {
        
        for (int i = 0; i < bullets.length; i++) {
            g.drawImage(bullets[i].image, bullets[i].x, bullets[i].y, null);
        }
    }

    // 显示得分与生命值
    public void printScore_Life(Graphics g) {
        
        Font font = new Font(Font.SANS_SERIF, Font.BOLD, 14);
        // 设置字体
        g.setFont(font);
        // 显示得分
        g.drawString("SCORE:" + hero.getScore(), 10, 20);
        // 显示生命值
        g.drawString("LIFE:" + hero.getLife(), 10, 40);
    }

    // 创建新敌机（随机算法）
    public void newFlyer() {

        Random r = new Random();
        // 95%创建敌机 5%的概率创建蜜蜂
        Flyer newOne = r.nextInt(20) == 10 ? new Bee() : new Airplane();
        // 扩充敌机数组 将新敌机放入末尾
        this.flyers = Arrays.copyOf(this.flyers, this.flyers.length + 1);
        this.flyers[this.flyers.length - 1] = newOne;
    }

    // 创建子弹对象
    public void newBullet() {
        
        // 获取英雄机创建的子弹
        Bullet[] newBullets = hero.shoot();
        // 扩充子弹数组 将新子弹放入末尾
        this.bullets = Arrays.copyOf(bullets, bullets.length + newBullets.length);
        System.arraycopy(newBullets, 0, bullets, bullets.length - newBullets.length, newBullets.length);
    }

    // 判断子弹击中敌机
    public void crash() {
        // 嵌套循环判断
        for (int i = 0; i < bullets.length; i++) {
            for (int j = 0; j < flyers.length; j++) {
                // 如果有击中
                if (Flyer.isCrash(bullets[i], flyers[j])) {
                    // 击中后英雄机获得奖励
                    hero.getScore_Award(flyers[j]);
                    // 1.将数组最后一位替换到当前位置
                    flyers[j] = flyers[flyers.length - 1];
                    // 2.压缩数组
                    flyers = Arrays.copyOf(flyers, flyers.length - 1);
                    bullets[i] = bullets[bullets.length - 1];
                    bullets = Arrays.copyOf(bullets, bullets.length - 1);
                    // 循环仍从当前位开始
                    i--;
                    // 跳出当前循环
                    break;
                }
            }
        }
    }

    // 判断英雄机与敌机碰撞
    public void hit() {
        // 1.创建相同长度的临时数组
        Flyer[] liveFlyers = new Flyer[this.flyers.length];
        // 2.遍历飞行物、将未碰撞的敌机保留
        int index = 0;
        for (int i = 0; i < flyers.length; i++) {
            if (hero.hit(flyers[i])) {
                if (hero.getLife() == 0) {
                    state = GAMEOVER;
                }
            } else {
                liveFlyers[index] = flyers[i];
                index++;
            }
        }
        // 3.压缩数组
        this.flyers = Arrays.copyOf(liveFlyers, index);
    }

    // 判断飞行物超出边界
    public void outOfBounds() {
        // 1.创建相同长度的临时数组
        Flyer[] liveFlyers = new Flyer[this.flyers.length];
        // 2.遍历飞行物、将未超出边界的保留
        int index = 0;
        for (int i = 0; i < flyers.length; i++) {
            if (!flyers[i].outOfBounds()) {
                liveFlyers[index] = flyers[i];
                index++;
            }
        }
        // 3.压缩数组
        this.flyers = Arrays.copyOf(liveFlyers, index);

        // 检测子弹是否超出边界 方法同上
        Bullet[] liveBullets = new Bullet[this.bullets.length];

        index = 0;
        for (int i = 0; i < bullets.length; i++) {
            if (!bullets[i].outOfBounds()) {
                liveBullets[index] = bullets[i];
                index++;
            }
        }
        
        this.bullets = Arrays.copyOf(liveBullets, index);
    }

}