package tankgame.BaseFiles;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.ImageObserver;
import tankgame.TankGame;

public class Wall extends GameObj
{
  private Tank p2 = TankGame.getTank(2);
  private Tank p1 = TankGame.getTank(1);
  private final Rectangle wallRect;
  private int height;
  private int width;
  private int coolDown;
  boolean breakableWall;
  
  public Wall(Image img, int x, int y, boolean weakWall)
  {
    super(img, x, y, 0);
    this.coolDown = 0;
    this.breakableWall = weakWall;
    this.width = img.getWidth(null);
    this.height = img.getHeight(null);
    this.boom = false;
    this.wallRect = new Rectangle(x, y, this.width, this.height);
  }
  
  public boolean isDestroyAble()
  {
    return this.breakableWall;
  }
  
  public boolean isRespawning()
  {
    return this.coolDown == 0;
  }
  
  public void breakWall()
  {
    this.coolDown = 700;
  }
  
  public Rectangle getWallRectangle()
  {
    return this.wallRect;
  }
  
  public void draw(ImageObserver obs, Graphics2D g)
  {
    if (this.coolDown == 0)
    {
      g.drawImage(this.img, this.x, this.y, obs);
      update();
    }
    else
    {
      this.coolDown -= 1;
    }
  }
  
  public void update() {}
}
