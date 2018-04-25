package tankgame.BaseFiles;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.ImageObserver;
import java.io.PrintStream;
import java.util.Observable;
import tankgame.*;
import projectile.*;

public class Tank extends GameObj {

    private int coolDown = 0, score = 0, health = 4, life = 3;
    private int angle = 0, shootCoolDown = 0;
    private final int windowSizeX = 1473, windowSizeY = 1473;
    private boolean powerUp;
    private int spawnPointX, spawnPointY;
    private int left, right, up, down;
    private int shootKey, shootRate;
    private boolean moveLeft, moveRight, moveUp, moveDown;
    private Tank p1, p2;
    private TankGame obj;
    //private int height, width, speed;
    //private int x, y;
    //private Image img;

    public Tank(Image img, int x, int y, int speed, int left, int right, int up, int down, int sKey) {
        super(img, x, y, speed);
        this.img = img;
        powerUp = false;
        this.width = img.getWidth(null);
        this.left = left;
        this.right = right;
        this.up = up;
        this.down = down;
        shootKey = sKey;
        moveLeft = false;
        moveRight = false;
        moveUp = false;
        moveDown = false;
        boom = false;
        shootRate = 50;
        spawnPointX = x;
        spawnPointY = y;
    }

    public void setScore(int s) {
        score += s;
    }

    public boolean isLose() {
        if (life <= 0) {
            return true;
        }
        return false;
    }

    public void setHP(int hp) {
        health = hp;
    }

    public void enemyBulletDmg(int dmg) {
        health -= dmg;
    }

    public int getLife() {
        return life;
    }

    public int getScore() {
        return score;
    }

    public void setBoom(boolean b) {
        boom = b;
    }

    public boolean isRespawning() {
        return coolDown != 0;
    }

    public void ShootBullet(Tank a) {

        obj = TankGame.getTankGame();

        //if(powerUp == true){
        //obj.getProjectile().add(new TankProjectile(obj.getPowerUpBulletImage(), speed*3, this, 0, 2));
        //    obj.getProjectile().add(new TankProjectile(obj.getPowerUpBulletImage(),speed*3,this,0,2));
        //}
        //else{
        // obj.getProjectile().add(new TankProjectile(obj.getNormalBulletImage(), speed*2,this,0,1));
        //when shift is pressed need to add a bullet to the bullet arraylist and then project it on screen
        obj.getProjectile().add(new TankProjectile(obj.getNormalBulletImage(), speed * 2, this, 1));

        // }
    }

    @Override
    public void draw(ImageObserver obs, Graphics2D g) {
        p1 = TankGame.getTank(1);
        p2 = TankGame.getTank(2);

        shootCoolDown -= 1;
        if (health <= 0) {
            boom = true;
        }
        if ((health > 0) && (coolDown == 0) && (life > 0)) {
            boom = false;
            AffineTransform rotation = AffineTransform.getTranslateInstance(x, y);
            rotation.rotate(Math.toRadians(angle), img.getWidth(null) / 2, img.getHeight(null) / 2);
            g.drawImage(img, rotation, null);
            if ((!p1.isRespawning()) && (!p2.isRespawning()) && (p1.collision(p2.x, p2.y, p2.width, p2.height))) {
                if (p1.x > x) {
                    p1.x += speed;
                    p2.x -= speed;
                } else if (p1.x < x) {
                    p1.x -= speed;
                    p2.x += speed;
                }
                if (p1.y > y) {
                    p1.y += speed;
                    p2.y -= speed;
                } else if (p1.y < y) {
                    p1.y -= speed;
                    p2.y += speed;
                }
            }
        } else if ((boom == true) && (coolDown == 0) && (life > 0)) {
            coolDown = 180;
            powerUp = false;
            if (--life >= 0) {
                health = 4;
            }
            boom = false;
            x = spawnPointX;
            y = spawnPointY;
        } else {
            coolDown -= 1;
        }
    }

    @Override
    public void update(Observable obj, Object arg) {
        GameEvents ge = (GameEvents) arg;
        if ((ge.type == 1) && (!boom) && (coolDown == 0)) {
            KeyEvent e = (KeyEvent) ge.event;
            if (e.getKeyCode() == left) {
                if (e.getID() == KeyEvent.KEY_RELEASED) {
                    moveLeft = false;
                } else if (e.getID() == KeyEvent.KEY_PRESSED) {
                    moveLeft = true;
                }
            }
            if (e.getKeyCode() == right) {
                if (e.getID() == KeyEvent.KEY_RELEASED) {
                    moveRight = false;
                } else if (e.getID() == KeyEvent.KEY_PRESSED) {
                    moveRight = true;
                }
            }
            if (e.getKeyCode() == up) {
                if (e.getID() == KeyEvent.KEY_RELEASED) {
                    moveUp = false;
                } else if (e.getID() == KeyEvent.KEY_PRESSED) {
                    moveUp = true;
                }
            }
            if (e.getKeyCode() == down) {
                if (e.getID() == KeyEvent.KEY_RELEASED) {
                    moveDown = false;
                } else if (e.getID() == KeyEvent.KEY_PRESSED) {
                    moveDown = true;
                }
            }
            if ((e.getKeyCode() == shootKey) && (shootCoolDown <= 0)) {
                if (e.getID() == KeyEvent.KEY_PRESSED) {
                    shootCoolDown = shootRate;
                    ShootBullet(this);
                    System.out.println("Shoot");
                } else if (e.getID() != KeyEvent.KEY_RELEASED) {
                }
            }
        }
    }

    public int getHealth() {
        return health;
    }

    public int getAngle() {
        return angle;
    }

    public int getTankCenterX() {
        return x + img.getWidth(null) / 2;
    }

    public int getTankCenterY() {
        return y + img.getHeight(null) / 2;
    }

    private void checkLimit() {
        if (x < 0) {
            x = 0;
        }
        if (x >= windowSizeX) {
            x = windowSizeX;
        }
        if (y < 0) {
            y = 0;
        }
        if (y >= windowSizeY) {
            y = windowSizeY;
        }
    }

    public void updateMove() {
        if (moveLeft == true) {
            angle -= 3;
        }
        if (moveRight == true) {
            angle += 3;
        }
        if (moveUp == true) {
            x = ((int) (x + Math.round(speed * Math.cos(Math.toRadians(angle)))));
            y = ((int) (y + Math.round(speed * Math.sin(Math.toRadians(angle)))));
            checkLimit();
        }
        if (moveDown == true) {
            x = ((int) (x - Math.round(speed * Math.cos(Math.toRadians(angle)))));
            y = ((int) (y - Math.round(speed * Math.sin(Math.toRadians(angle)))));
            checkLimit();
        }

        if (angle == -1) {
            angle = 359;
        } else if (angle == 361) {
            angle = 1;
        }

        if (coolDown > 0) {
            moveLeft = false;
            moveRight = false;
            moveUp = false;
            moveDown = false;
        }
    }
}
