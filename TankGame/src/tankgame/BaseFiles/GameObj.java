package tankgame.BaseFiles;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.ImageObserver;
import java.util.Observable;
import java.util.Observer;

public abstract class GameObj implements Observer {

    public int x;
    public int y;
    public int speed;
    public int height;
    public int width;
    public int planeLoop = 0;
    Rectangle bbox;
    protected boolean boom;
    protected Image[] imgs = new Image[3];
    protected Image img;

    public GameObj(Image img, int x, int y, int speed) {
        this.img = img;
        this.x = x;
        this.y = y;
        this.speed = speed;
        this.height = img.getHeight(null);
        this.width = img.getWidth(null);
    }

    public boolean collision(int x, int y, int w, int h) {
        this.bbox = new Rectangle(this.x, this.y, this.width, this.height);
        Rectangle otherBBox = new Rectangle(x, y, w, h);
        if ((this.bbox.intersects(otherBBox)) && (!this.boom)) {
            return true;
        }
        return false;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public void update(Observable obj, Object arg) {
    }

    public void draw(ImageObserver obs, Graphics2D g) {
    }
}
