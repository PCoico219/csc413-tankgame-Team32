package tankgame.BaseFiles;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.ImageObserver;
import java.io.PrintStream;
import java.util.Observable;
import tankgame.*;

public class Tank extends GameObj {

    protected int coolDown = 0;
    protected int score = 0;
    protected int health = 4;
    protected int life = 3;
    private int angle = 0;
    private int shootCoolDown = 0;
    private final int leftShift = 2;
    private final int rightShift = 3;
    private final int windowSizeX = 1473;
    private final int windowSizeY = 1473;
    protected boolean powerUp;
    protected int spawnPointX;
    protected int spawnPointY;
    private int left;
    private int right;
    private int up;
    private int down;
    private int shootKey;
    private int shootRate;
    private int shootKeyPosition;
    private boolean moveLeft;
    private boolean moveRight;
    private boolean moveUp;
    private boolean moveDown;
    private Tank p1;
    private Tank p2;

    public Tank(Image img, int x, int y, int speed, int left, int right, int up, int down, int sKey, int keyPosition) {
        super(img, x, y, speed);
        this.powerUp = false;
        this.width = (img.getWidth(null) / 2);
        this.left = left;
        this.right = right;
        this.up = up;
        this.down = down;
        this.shootKey = sKey;
        this.shootKeyPosition = keyPosition;
        this.moveLeft = false;
        this.moveRight = false;
        this.moveUp = false;
        this.moveDown = false;
        this.boom = false;
        this.shootRate = 50;
        this.spawnPointX = x;
        this.spawnPointY = y;
    }

    public void setScore(int s) {
        this.score += s;
    }

    public boolean isLose() {
        if (this.life <= 0) {
            return true;
        }
        return false;
    }

    public void setHP(int hp) {
        this.health = hp;
    }

    public void enemyBulletDmg(int dmg) {
        this.health -= dmg;
    }

    public int getLife() {
        return this.life;
    }

    public int getScore() {
        return this.score;
    }

    public void setBoom(boolean b) {
        this.boom = b;
    }

    public boolean isRespawning() {
        return this.coolDown != 0;
    }

    public void draw(ImageObserver obs, Graphics2D g) {
        this.p1 = TankGame.getTank(1);
        this.p2 = TankGame.getTank(2);
        this.shootCoolDown -= 1;
        if (this.health <= 0) {
            this.boom = true;
        }
        if ((this.health > 0) && (this.coolDown == 0) && (this.life > 0)) {
            this.boom = false;
            AffineTransform rotation = AffineTransform.getTranslateInstance(this.x, this.y);
            rotation.rotate(Math.toRadians(this.angle), this.img.getWidth(null) / 2, this.img.getHeight(null) / 2);
            g.drawImage(this.img, rotation, null);
            if ((!this.p1.isRespawning()) && (!this.p2.isRespawning())
                    && (this.p1.collision(this.p2.x, this.p2.y, this.p2.width, this.p2.height))) {
                if (this.p1.x > this.x) {
                    this.p1.x += this.speed * 5;
                    this.p2.x -= this.speed * 5;
                } else if (this.p1.x < this.x) {
                    this.p1.x -= this.speed * 5;
                    this.p2.x += this.speed * 5;
                }
                if (this.p1.y > this.y) {
                    this.p1.y += this.speed * 5;
                    this.p2.y -= this.speed * 5;
                } else if (this.p1.y < this.y) {
                    this.p1.y -= this.speed * 5;
                    this.p2.y += this.speed * 5;
                }
            }
        } else if ((this.boom == true) && (this.coolDown == 0) && (this.life > 0)) {
            this.coolDown = 180;
            this.powerUp = false;
            if (--this.life >= 0) {
                this.health = 4;
            }
            this.boom = false;
            this.x = this.spawnPointX;
            this.y = this.spawnPointY;
        } else {
            this.coolDown -= 1;
        }
    }

    public void update(Observable obj, Object arg) {
        GameEvents ge = (GameEvents) arg;
        if ((ge.type == 1) && (!this.boom) && (this.coolDown == 0)) {
            KeyEvent e = (KeyEvent) ge.event;
            if (e.getKeyCode() == this.left) {
                if (e.getID() == 402) {
                    this.moveLeft = false;
                } else if (e.getID() == 401) {
                    this.moveLeft = true;
                }
            }
            if (e.getKeyCode() == this.right) {
                if (e.getID() == 402) {
                    this.moveRight = false;
                } else if (e.getID() == 401) {
                    this.moveRight = true;
                }
            }
            if (e.getKeyCode() == this.up) {
                if (e.getID() == 402) {
                    this.moveUp = false;
                } else if (e.getID() == 401) {
                    this.moveUp = true;
                }
            }
            if (e.getKeyCode() == this.down) {
                if (e.getID() == 402) {
                    this.moveDown = false;
                } else if (e.getID() == 401) {
                    this.moveDown = true;
                }
            }
            if ((e.getKeyCode() == this.shootKey) && (this.shootCoolDown <= 0) && (e.getKeyLocation() == 2)) {
                if (e.getID() == 401) {
                    this.shootCoolDown = this.shootRate;
                    System.out.println("P1 Shoot");
                } else if (e.getID() != 402) {
                }
            }
            if ((e.getKeyCode() == this.shootKey) && (this.shootCoolDown <= 0) && (e.getKeyLocation() == 3)) {
                if (e.getID() == 401) {
                    this.shootCoolDown = this.shootRate;
                    System.out.println("P2 Shoot");
                } else if (e.getID() != 402) {
                }
            }
        }
    }

    public int getHealth() {
        return this.health;
    }

    public int getAngle() {
        return this.angle;
    }

    public int getTankCenterX() {
        return this.x + this.img.getWidth(null) / 2;
    }

    public int getTankCenterY() {
        return this.y + this.img.getHeight(null) / 2;
    }

    private void checkLimit() {
        if (this.x < 0) {
            this.x = 0;
        }
        if (this.x >= 1473) {
            this.x = 1473;
        }
        if (this.y < 0) {
            this.y = 0;
        }
        if (this.y >= 1473) {
            this.y = 1473;
        }
    }

    public void updateMove() {
        if (this.moveLeft == true) {
            this.angle -= 3;
        }
        if (this.moveRight == true) {
            this.angle += 3;
        }
        if (this.moveUp == true) {
            this.x = ((int) (this.x + Math.round(this.speed * Math.cos(Math.toRadians(this.angle)))));
            this.y = ((int) (this.y + Math.round(this.speed * Math.sin(Math.toRadians(this.angle)))));
            checkLimit();
        }
        if (this.moveDown == true) {
            this.x = ((int) (this.x - Math.round(this.speed * Math.cos(Math.toRadians(this.angle)))));
            this.y = ((int) (this.y - Math.round(this.speed * Math.sin(Math.toRadians(this.angle)))));
            checkLimit();
        }
        if (this.angle == -1) {
            this.angle = 359;
        } else if (this.angle == 361) {
            this.angle = 1;
        }
        if (this.coolDown > 0) {
            this.moveLeft = false;
            this.moveRight = false;
            this.moveUp = false;
            this.moveDown = false;
        }
    }
}
