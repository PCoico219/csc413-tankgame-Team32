package tankgame;

import tankgame.BaseFiles.GameEvents;
import tankgame.BaseFiles.Tank;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.WindowAdapter;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Objects;
import javax.imageio.ImageIO;
import javax.swing.JApplet;
import javax.swing.JFrame;

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
    private Image tankP1, tankP2;
    private BufferedImage background, bimg, bimg2;
    private BufferedImage player_1_window, player_2_window;

    @Override
    public void init() {
        setFocusable(true);
        setLayout(null);
        try {
            this.background = ImageIO.read(new File("Resources/Background.bmp"));
            this.tankP1 = ImageIO.read(new File("Resources/Tank1.gif"));
            this.tankP2 = ImageIO.read(new File("Resources/Tank2.gif"));
            this.life = ImageIO.read(new File("Resources/life.png"));
        } catch (Exception e) {
            System.err.println(e + " NO RESOURCES ARE FOUND!");
        }
        p1 = new Tank(this.tankP1, 736, 64, 5, 65, 68, 87, 83, 16, 2);
        p2 = new Tank(this.tankP2, 736, 1440, 5, 74, 76, 73, 75, 16, 3);
        gameEvents = new GameEvents();

        gameEvents.addObserver(p1);
        gameEvents.addObserver(p2);

        KeyControl key = new KeyControl();
        addKeyListener(key);
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
        this.thread = new Thread(this);
        this.thread.setPriority(1);
        this.thread.start();
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
        this.bimg = ((BufferedImage) createImage(1536, 1536));

        this.g2 = this.bimg.createGraphics();
        if (!this.gameOver) {
            drawDemo();
            g.drawImage(this.bimg2, 0, 0, this);
        } else {
            this.g2.drawImage(this.win, this.windowSize.width / 2 - this.win.getWidth(null) / 2, 0, this);

            g.drawImage(this.bimg, 0, 0, this);
        }
    }

    public void drawDemo() {
        drawBackGround();

        p1.draw(this, this.g2);
        p1.updateMove();
        p2.draw(this, this.g2);
        p2.updateMove();

        this.bimg2 = ((BufferedImage) createImage(this.windowSize.width, this.windowSize.height));

        this.g2 = this.bimg2.createGraphics();

        this.player_1_window = this.bimg.getSubimage(this.p1WindowBoundX, this.p1WindowBoundY, this.windowSize.width / 2, this.windowSize.height);
        this.player_2_window = this.bimg.getSubimage(this.p2WindowBoundX, this.p2WindowBoundY, this.windowSize.width / 2, this.windowSize.height);
        playerViewBoundChecker();

        this.g2.drawImage(this.player_1_window, 0, 0, this);
        
        if (p1.getLife() != 0) {
            for (int i = 0; i < p1.getLife(); i++) {
                this.g2.drawImage(this.life, i * 32, this.windowSize.height - 20, this);
            }
        }
        
        this.g2.drawImage(this.player_2_window, this.windowSize.width / 2, 0, this);
        
        if (p2.getLife() != 0) {
            for (int i = 0; i < p2.getLife(); i++) {
                this.g2.drawImage(this.life, this.windowSize.width - 3 * this.life.getWidth(null) + i * this.life
                        .getWidth(null), this.windowSize.height - 20, this);
            }
        }
        
        Image scaledMap = this.bimg.getScaledInstance(200, 200, 2);

        this.g2.drawImage(scaledMap, this.windowSize.width / 2 - 100, this.windowSize.height - 200, 200, 200, this);
        
        if (p1.isLose()) {
            if (--this.gameOverCounter == 0) {
                this.gameOver = true;
                System.out.println("P1 LOST");
                this.winner = ("Player 2 win!   Score:" + p2.getScore());
                this.loser = ("Player 1 lose!   Score:" + p1.getScore());
            }
        } else if ((p2.isLose())
                && (--this.gameOverCounter == 0)) {
            this.gameOver = true;
            System.out.println("P2 LOST");
            this.winner = ("Player 1 win!   Score:" + p1.getScore());
            this.loser = ("Player 2 lose!   Score:" + p2.getScore());
        }
    }

    public void drawBackGround() {
        int tileWidth = this.background.getWidth();
        int tileHeight = this.background.getHeight();

        int NumberX = 5;
        int NumberY = 7;
        
        for (int i = 0; i < NumberY; i++) {
            for (int j = 0; j < NumberX; j++) {
                this.g2.drawImage(this.background, j * tileWidth, i * tileHeight, tileWidth, tileHeight, this);
            }
        }
    }

    public void playerViewBoundChecker() {
        if ((this.p1WindowBoundX = p1.getTankCenterX() - this.windowSize.width / 4) < 0) {
            this.p1WindowBoundX = 0;
        } else if (this.p1WindowBoundX >= 1536 - this.windowSize.width / 2) {
            this.p1WindowBoundX = (1536 - this.windowSize.width / 2);
        }
        if ((this.p1WindowBoundY = p1.getTankCenterY() - this.windowSize.height / 2) < 0) {
            this.p1WindowBoundY = 0;
        } else if (this.p1WindowBoundY >= 1536 - this.windowSize.height) {
            this.p1WindowBoundY = (1536 - this.windowSize.height);
        }
        if ((this.p2WindowBoundX = p2.getTankCenterX() - this.windowSize.width / 4) < 0) {
            this.p2WindowBoundX = 0;
        } else if (this.p2WindowBoundX >= 1536 - this.windowSize.width / 2) {
            this.p2WindowBoundX = (1536 - this.windowSize.width / 2);
        }
        if ((this.p2WindowBoundY = p2.getTankCenterY() - this.windowSize.height / 2) < 0) {
            this.p2WindowBoundY = 0;
        } else if (this.p2WindowBoundY >= 1536 - this.windowSize.height) {
            this.p2WindowBoundY = (1536 - this.windowSize.height);
        }
    }

    

    public static void main(String[] argv) {
        TANKGAME.init();
        JFrame f = new JFrame("Tank Game");
        f.setDefaultCloseOperation(3);
        f.addWindowListener(new WindowAdapter() {
        });
        f.getContentPane().add("Center", TANKGAME);
        f.pack();
        Objects.requireNonNull(TANKGAME);
        Objects.requireNonNull(TANKGAME);
        f.setSize(new Dimension(900, 600));
        f.setVisible(true);
        f.setResizable(false);

        TANKGAME.windowSize = f.getContentPane().getSize();
        System.out.println(TANKGAME.windowSize.height + " " + TANKGAME.windowSize.width);
        TANKGAME.start();
    }
}
