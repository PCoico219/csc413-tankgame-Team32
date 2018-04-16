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
    private int x, y;
    boolean breakableWall, boom = getBoom();
    private Image img = getImg();

    public Wall(Image img, int x, int y, boolean weakWall) {
        super(img, x, y, 0);
        coolDown = 0;
        breakableWall = weakWall;
        width = img.getWidth(null);
        height = img.getHeight(null);
        boom = false;
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
            g.drawImage(img, x, y, obs);
            update();
        } else {
            coolDown -= 1;
        }
    }

    public void update() {
    }
}
