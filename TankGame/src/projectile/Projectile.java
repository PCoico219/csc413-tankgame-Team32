/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projectile;
import java.awt.image.ImageObserver;
import java.awt.Rectangle;
import java.awt.Image;
import java.awt.Graphics2D;
import tankgame.BaseFiles.GameObj;
import tankgame.BaseFiles.Tank;

/**
 *
 * @author agunderson
 */
public class Projectile extends GameObj {
    
    int xSize;
    int ySize;
    //Rectangle hitBox;
    Tank currentTank;
    Boolean visible;

    public Projectile(Image img, int x, int y, int speed, int sideSpeed) {
        super(img, x, y, speed);
        visible = true;
    }
    
    public void draw(ImageObserver iObs, Graphics2D graf){
        
    }
    
    public void update(){
    }
    
    public Tank getTank(){
        return currentTank;
    }
    
    public boolean isVisible(){
        return visible;
    }
  
    
}