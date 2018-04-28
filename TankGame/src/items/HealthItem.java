/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package items;

import tankgame.BaseFiles.GameObj;
import tankgame.BaseFiles.Tank;
import tankgame.TankGame;
import java.awt.Graphics2D;
import java.awt.image.ImageObserver;
import java.awt.Image;

/**
 *
 * @author akeelin
 */
public class HealthItem extends SpecialItem {

    private Tank p1 = TankGame.getTank(1);
    private Tank p2 = TankGame.getTank(2);

    private int width;
    private int height;

    public HealthItem(Image img, int x, int y, int speed) {
        super(img, x, y, speed);
        this.width = img.getWidth(null);
        this.height = img.getHeight(null);
    }

    @Override
    public void update() {
        y += speed;
        if (p1.collision(x, y, width, height) && p1.getHealth() != 4) {
            p1.healthIncrease();
            visible = false;
        } else if (p2.collision(x, y, width, height) && p2.getHealth() != 4) {
            p2.healthIncrease();
            visible = false;
        }
    }
}
