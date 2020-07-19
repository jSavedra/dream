package com.savx.entity;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import com.savx.main.Game;
import com.savx.world.Camera;
public class Entity {

	//Entities.
	
	public static BufferedImage lifeEntity = Game.sheet.getSprite(0, 4 * 16, 16, 16);
	public static BufferedImage weaponEntity = Game.sheet.getSprite(0, 5 * 16, 16, 16);
	public static BufferedImage bulletEntity = Game.sheet.getSprite(0, 6 * 16, 16, 16);
	public static BufferedImage enemyEntity = Game.sheet.getSprite(0, 7 * 16, 16, 16);
	
	//Weapon Directions.
	
	public static BufferedImage weaponRightEntity = Game.sheet.getSprite(8 * 16, 32, 16, 16);
	public static BufferedImage weaponLeftEntity = Game.sheet.getSprite(9 * 16, 32, 16, 16);
	
	private BufferedImage sprite;
	
	//Position Entity.

	protected double x;
	protected double y;
	protected int width;
	protected int height;
	
	//Mask.
	
	private int maskX;
	private int maskY; 
	private int maskW;
	private int maskH;
	
	public Entity(int x, int y, int width, int height, BufferedImage sprite)
	{
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.sprite = sprite;
		
		this.maskX = 0;
		this.maskY = 0;
		this.maskW = width;
		this.maskH = height;
	}
	
	public void update()
	{
		
	}
	
	//Rendering Sprite.
	
	public void render(Graphics g)
	{
		g.drawImage(this.getSprite(), this.getX() - Camera.x, this.getY() - Camera.y, null);
		//g.setColor(Color.red);
		//g.fillRect(this.getX() + maskX - Camera.x, this.getY() + maskY - Camera.y, maskW, maskH);
	}
	
	//Set Masks.
	
	public void setMask(int maskX, int maskY, int maskW, int maskH)
	{
		this.maskX = maskX;
		this.maskY = maskY;
		this.maskW = maskW;
		this.maskH = maskH;
	}
	
	//IsColliding With Entity And Entity.
	
	public static boolean isColliding(Entity e1, Entity e2)
	{
		Rectangle e1Mask = new Rectangle(e1.getX() + e1.maskX, e1.getY() + e1.maskY, e1.maskW, e1.maskH);
		Rectangle e2Mask = new Rectangle(e2.getX() + e2.maskX, e2.getY() + e2.maskY, e2.maskW, e2.maskH);
		
		return e1Mask.intersects(e2Mask);
	}
	
	//Methods for get(Variable).
	
	public int getX()
	{
		return (int)this.x;
	}
	
	public int getY()
	{
		return (int)this.y;
	}
	
	public int getWidth()
	{
		return this.width;
	}
	
	public int getHeight()
	{
		return this.height;
	}
	
	public BufferedImage getSprite()
	{
		return this.sprite;
	}
	
	//Sets.
	
	public void setX(int newX)
	{
		this.x = newX;
	}
		
	public void setY(int newY)
	{
		this.y = newY;
	}
}






