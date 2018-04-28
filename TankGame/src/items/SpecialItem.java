/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package items;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.ImageObserver;
import tankgame.BaseFiles.GameObj;

/**
 *
 * @author akeelin
 */
public abstract class SpecialItem extends GameObj {

    boolean visible = false;

    public SpecialItem(Image img, int x, int y, int speed) {
        super(img, x, y, speed);
        visible = true;
    }

    public boolean isVisible() {
        return visible;
    }

    public void update() {
    }

    public void draw(ImageObserver obs, Graphics2D a) {
        if (visible == true) {
            a.drawImage(img, x, y, obs);
        }
    }

}
