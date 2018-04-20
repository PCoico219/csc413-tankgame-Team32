/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projectile;


import tankgame.BaseFiles.Wall;
import tankgame.TankGame;
import tankgame.BaseFiles.Tank;
import tankgame.BaseFiles.GameObj;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.ImageObserver;

/**
 *
 * @author agunderson
 */
public class TankProjectile extends Projectile {
    private Tank p2 = TankGame.getTank(2), p1 = TankGame.getTank(1);
    private Image bullet;
    private int theta;
    private int damage;
     private TankGame obj;
    
    
    public TankProjectile(Image img, int speed, Tank t, int dmg) {
        super(img, t.getTankCenterX(), t.getTankCenterY(),speed);
        bullet = img;
        damage = dmg;
        xSize = img.getWidth(null);
        ySize = img.getHeight(null);
        currentTank = t;
        theta = currentTank.getAngle();
        visible = true;
    }
    
    
    @Override
    public void update(){
        p2 = TankGame.getTank(2);
        p1 = TankGame.getTank(1);

        y+=Math.round(speed*Math.sin(Math.toRadians(theta)));
        x+=Math.round(speed*Math.cos(Math.toRadians(theta)));
        
        if(p1.collision(x, y, xSize, ySize) && visible && currentTank != p1 && !p1.isRespawning()){
            terminateBullet();
            p1.enemyBulletDmg(damage);
            System.out.println(p1.getHealth());
            p2.setScore(30);
        }
        else if(p2.collision(x, y, xSize, ySize) && visible && currentTank != p2 && !p2.isRespawning()){
            terminateBullet();
            p2.enemyBulletDmg(damage);
            p1.setScore(30);
        }
        else {
            obj = TankGame.getTankGame();
            for(int i = 0; i < obj.getWall().size()-1; i++){
                Wall twall = obj.getWall().get(i);
                if(twall.getWallRectangle().intersects(x,y,width,height) && twall.isRespawning()){
                    if(twall.isDestroyAble()){
                        twall.breakWall();
                        twall.update();
                    }
                    terminateBullet();                
                }
            }
        }
        
        if(!visible){
            //
        }
    }
    
    @Override
    public void draw(ImageObserver Iobs, Graphics2D graf){
       AffineTransform rotation = AffineTransform.getTranslateInstance(x, y);
       rotation.rotate(Math.toRadians(theta), 0, 0);
       graf.drawImage(img, rotation, Iobs);
        
        //graf.drawImage(img, x + this.xSize/2, y - this.ySize, Iobs);
        update();
    }
    
    public void terminateBullet(){
        visible = false;
    }
    
    
}