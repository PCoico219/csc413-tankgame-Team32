package tankgame.BaseFiles;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.ImageObserver;
import tankgame.TankGame;

public class Wall extends GameObj {

    private Tank p2 = TankGame.getTank(2), p1 = TankGame.getTank(1);
    private final Rectangle wallRect;
    private int height, width, coolDown;
    boolean breakableWall;

    public Wall(Image img, int x, int y, boolean weakWall) {
        super(img, x, y, 0);
        coolDown = 0;
        breakableWall = weakWall;
        this.width = img.getWidth(null);
        this.height = img.getHeight(null);
        dead = false;
        wallRect = new Rectangle(x, y, width, height);
    }

    public boolean isDestroyAble() {
        return breakableWall;
    }

    public boolean isRespawning() {
        return coolDown == 0;
    }

    public void breakWall() {
        coolDown = 700;
    }

    public Rectangle getWallRectangle() {
        return wallRect;
    }

    @Override
    public void draw(ImageObserver obs, Graphics2D g) {
        if (coolDown == 0) {
            g.drawImage(img, x, y, null);
            update();
        } else {
            coolDown -= 1;
        }
    }

    public void update() {
        if (p1.collision(this.x, this.y, width, height)) {
            if (p1.x > (x)) {
                p1.x += 3;
            } else if (p1.x < (this.x)) {
                p1.x -= 3;
            }
            if (p1.y > (this.y)) {
                p1.y += 3;
            } else if (p1.y < this.y) {
                p1.y -= 3;
            }
        }
        if (p2.collision(this.x, this.y, width, height)) {
            if (p2.x > (x)) {
                p2.x += 3;
            } else if (p2.x < (this.x)) {
                p2.x -= 3;
            }
            if (p2.y > (this.y)) {
                p2.y += 3;
            } else if (p2.y < this.y) {
                p2.y -= 3;
            }
        }

    }
}
