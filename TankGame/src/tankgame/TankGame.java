package tankgame;

import projectile.TankProjectile;
import tankgame.BaseFiles.*;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.WindowAdapter;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.JApplet;
import javax.swing.JFrame;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

public class TankGame extends JApplet implements Runnable {

    private static GameEvents gameEvents;
    private static final TankGame TANKGAME = new TankGame();
    private final int w = 900, h = 600;
    private final int battleField_X_Size = 1536, battleField_Y_Size = 1536;
    private int p1WindowBoundX, p1WindowBoundY;
    private int p2WindowBoundX, p2WindowBoundY;
    private int gameOverCounter = 50;
    private Graphics2D g2;
    private Image life, win;
    private static Tank p1, p2;
    private boolean gameOver = false;
    private String winner, loser;
    private Thread thread;
    private Dimension windowSize;
    private Image tankP1, tankP2, normalBullet, powerUpBullet;
    private BufferedImage background, bimg, bimg2;
    private BufferedImage player_1_window, player_2_window;
    private InputStream map;
    private Image wall, breakableWall;

    private ArrayList<Wall> walls = new ArrayList<>();
    private ArrayList<TankProjectile> bullets = new ArrayList<>();
    private ArrayList<Wall> wallsBreakable = new ArrayList<>();

    public ArrayList<TankProjectile> getProjectile() {
        return bullets;
    }

    public ArrayList<Wall> getWall() {
        return walls;
    }

    public Image getPowerUpBulletImage() {
        return powerUpBullet;
    }

    public Image getNormalBulletImage() {
        return normalBullet;
    }

    @Override
    public void init() {
        setFocusable(true);
        try {
            //background = ImageIO.read(new File("Resources/Background.bmp"));
            //tankP1 = ImageIO.read(new File("Resources/Tank1.gif"));
            //tankP2 = ImageIO.read(new File("Resources/Tank2.gif"));
            //life = ImageIO.read(new File("Resources/life.png"));
            //map = new FileInputStream("Resources/tankmap.txt");
            //wall = ImageIO.read(new File("Resources/Wall1"));
            //breakableWall = ImageIO.read(new File("Resources/Wall2.gif"));

            //URL mapUrl = this.getClass().getResource("Resources/tankmap.txt");
            URL backgroundUrl = this.getClass().getResource("Resources/Background.bmp");
            URL tankP1Url = this.getClass().getResource("Resources/Tank1.gif");
            URL tankP2Url = this.getClass().getResource("Resources/Tank2.gif");
            URL lifeUrl = this.getClass().getResource("Resources/life.png");
            URL wallUrl = this.getClass().getResource("Resources/Wall1.gif");
            URL breakableWallUrl = this.getClass().getResource("Resources/Wall2.gif");
            URL normalBulletUrl = this.getClass().getResource("Resources/Shell.gif");
            //URL powerUpBulletUrl = this.getClass().getResource("Resources/Rocket.gif");

            background = ImageIO.read(backgroundUrl);
            tankP1 = ImageIO.read(tankP1Url);
            tankP2 = ImageIO.read(tankP2Url);
            life = ImageIO.read(lifeUrl);
            map = getClass().getResourceAsStream("Resources/tankmap.txt");
            wall = ImageIO.read(wallUrl);
            breakableWall = ImageIO.read(breakableWallUrl);
            normalBullet = ImageIO.read(normalBulletUrl);
            //powerUpBullet = ImageIO.read(powerUpBulletUrl);

        } catch (Exception e) {
            System.err.println(e + " NO RESOURCES ARE FOUND!");
        }

        p1 = new Tank(tankP1, 736, 64, 5, KeyEvent.VK_A, KeyEvent.VK_D, KeyEvent.VK_W, KeyEvent.VK_S, KeyEvent.VK_C);
        p2 = new Tank(tankP2, 736, 1440, 5, KeyEvent.VK_J, KeyEvent.VK_L, KeyEvent.VK_I, KeyEvent.VK_K, KeyEvent.VK_N);
        gameEvents = new GameEvents();

        gameEvents.addObserver(p1);
        gameEvents.addObserver(p2);

        KeyControl key = new KeyControl();
        addKeyListener(key);
        mapPrinter();
    }

    public static Tank getTank(int j) {
        if (j == 1) {
            return p1;
        }
        return p2;
    }

    public static TankGame getTankGame() {
        return TANKGAME;
    }

    public static GameEvents getGameEvents() {
        return gameEvents;
    }

    @Override
    public void start() {
        thread = new Thread(this);
        thread.setPriority(1);
        thread.start();
    }

    @Override
    public void run() {
        Thread me = Thread.currentThread();
        while (thread == me) {
            repaint();
            try {
                Thread.sleep(1000 / 144);
            } catch (InterruptedException e) {
                break;
            }
        }
    }

    @Override
    public void paint(Graphics g) {
        bimg = ((BufferedImage) createImage(battleField_X_Size, battleField_Y_Size));

        g2 = bimg.createGraphics();
        if (!gameOver) {
            drawDemo();
            g.drawImage(bimg2, 0, 0, this);
        } else {
            //g2.drawImage(win, windowSize.width / 2 - win.getWidth(null) / 2, 0, this);

            g.drawImage(bimg, 0, 0, this);
        }
    }

    public void mapPrinter() {
        BufferedReader line = new BufferedReader(new InputStreamReader(map));
        String number;
        int position = 0;
        try {
            number = line.readLine();
            while (number != null) {
                System.out.println(position);
                for (int i = 0; i < number.length(); i++) {
                    if (number.charAt(i) == '1') {
                        walls.add(new Wall(wall, ((i) % 48) * 32, ((position) % 49) * 32, false));
                    } else if (number.charAt(i) == '2') {
                        walls.add(new Wall(breakableWall, (i % 48) * 32, (position % 49) * 32, true));
                    }
                }
                position++;
                number = line.readLine();
            }
        } catch (Exception e) {
            System.err.println("MapPrinter" + e);
        }
    }

    public void drawDemo() {
        drawBackGround();

        if (!walls.isEmpty()) {
            for (int i = 0; i <= walls.size() - 1; i++) {
                walls.get(i).draw(this, g2);
            }
        }

        if (!bullets.isEmpty()) {
            for (int i = 0; i <= bullets.size() - 1; i++) {
                bullets.get(i).draw(this, g2);
                if (!bullets.get(i).isVisible()) {
                    bullets.remove(i--);
                }
            }
        }

        p1.draw(this, g2);
        p1.updateMove();
        p2.draw(this, g2);
        p2.updateMove();

        bimg2 = ((BufferedImage) createImage(windowSize.width, windowSize.height));

        g2 = bimg2.createGraphics();

        player_1_window = bimg.getSubimage(p1WindowBoundX, p1WindowBoundY, windowSize.width / 2, windowSize.height);
        player_2_window = bimg.getSubimage(p2WindowBoundX, p2WindowBoundY, windowSize.width / 2, windowSize.height);
        playerViewBoundChecker();

        g2.drawImage(player_1_window, 0, 0, this);

        if (p1.getLife() != 0) {
            for (int i = 0; i < p1.getLife(); i++) {
                g2.drawImage(life, i * 32, windowSize.height - 20, this);
            }
        }

        g2.drawImage(player_2_window, windowSize.width / 2, 0, this);

        if (p2.getLife() != 0) {
            for (int i = 0; i < p2.getLife(); i++) {
                g2.drawImage(life, windowSize.width - 3 * life.getWidth(null) + i * life
                        .getWidth(null), windowSize.height - 20, this);
            }
        }

        Image scaledMap = bimg.getScaledInstance(200, 200, 2);

        g2.drawImage(scaledMap, windowSize.width / 2 - 100, windowSize.height - 200, 200, 200, this);

        if (p1.isLose()) {
            if (--gameOverCounter == 0) {
                gameOver = true;
                System.out.println("P1 LOST");
                winner = ("Player 2 win!   Score:" + p2.getScore());
                loser = ("Player 1 lose!   Score:" + p1.getScore());
            }
        } else if ((p2.isLose()) && (--gameOverCounter == 0)) {
            gameOver = true;
            System.out.println("P2 LOST");
            winner = ("Player 1 win!   Score:" + p1.getScore());
            loser = ("Player 2 lose!   Score:" + p2.getScore());
        }
    }

    public void drawBackGround() {
        int tileWidth = background.getWidth();
        int tileHeight = background.getHeight();

        int NumberX = 5;
        int NumberY = 7;

        for (int i = 0; i < NumberY; i++) {
            for (int j = 0; j < NumberX; j++) {
                g2.drawImage(background, j * tileWidth, i * tileHeight, tileWidth, tileHeight, this);
            }
        }
    }

    public void playerViewBoundChecker() {
        if ((p1WindowBoundX = p1.getTankCenterX() - windowSize.width / 4) < 0) {
            p1WindowBoundX = 0;
        } else if (p1WindowBoundX >= battleField_X_Size - windowSize.width / 2) {
            p1WindowBoundX = (battleField_X_Size - windowSize.width / 2);
        }
        if ((p1WindowBoundY = p1.getTankCenterY() - windowSize.height / 2) < 0) {
            p1WindowBoundY = 0;
        } else if (p1WindowBoundY >= battleField_Y_Size - windowSize.height) {
            p1WindowBoundY = (battleField_Y_Size - windowSize.height);
        }
        if ((p2WindowBoundX = p2.getTankCenterX() - windowSize.width / 4) < 0) {
            p2WindowBoundX = 0;
        } else if (p2WindowBoundX >= battleField_X_Size - windowSize.width / 2) {
            p2WindowBoundX = (battleField_X_Size - windowSize.width / 2);
        }
        if ((p2WindowBoundY = p2.getTankCenterY() - windowSize.height / 2) < 0) {
            p2WindowBoundY = 0;
        } else if (p2WindowBoundY >= battleField_Y_Size - windowSize.height) {
            p2WindowBoundY = (battleField_Y_Size - windowSize.height);
        }
    }

    public static void main(String[] argv) {
        TANKGAME.init();
        JFrame f = new JFrame("Tank Game");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.addWindowListener(new WindowAdapter() {
        });
        f.getContentPane().add("Center", TANKGAME);
        f.pack();
        f.setSize(new Dimension(TANKGAME.w, TANKGAME.h));
        f.setVisible(true);
        f.setResizable(false);

        TANKGAME.windowSize = f.getContentPane().getSize();
        System.out.println(TANKGAME.windowSize.height + " " + TANKGAME.windowSize.width);
        TANKGAME.start();
    }
}
