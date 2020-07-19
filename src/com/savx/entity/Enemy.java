package com.savx.entity;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import com.savx.main.Game;
import com.savx.world.Camera;
import com.savx.world.World;

public class Enemy extends Entity{
	
	//Sprite Damaged Enemy.
	
	private BufferedImage damagedEnemy;
	
	//Boolean damaged;
	
	private boolean isDamaged = false;
	
	//Animation Damaged.
	
	private int damagedFrames = 0;
	
	//Life Enemy.
	
	private int life = 2;
	
	//Animation.
	
	private boolean moved = false;
	private int frames = 0;
	private int maxFrames = 20;
	private int curAnimation = 0;
	private int maxAnimation = 3;
		
	//BufferedImage Animation.
		
	private BufferedImage[] animationMove;
	
	//Speed.
	
	private double spd = 0.6;
	
	//Mask Enemy.
	
	private int maskX = 0;
	private int maskY = 2;
	private int maskWidth = 14;
	private int maskHeight = 14;
	
	public Enemy(int x, int y, int width, int height, BufferedImage sprite) 
	{
		super(x, y, width, height, sprite);
		
		//Animation Looping.
		
		animationMove = new BufferedImage[3];
		
		for (int i = 0; i < 3; i++)
		{
			animationMove[i] = Game.sheet.getSprite(0 + (i * 16), 7 * 16, 16, 16);
		}
		
		damagedEnemy = Game.sheet.getSprite(8 * 16, 16, 16, 16);
	}
	
	public void update()
	{
		//Enemy Chase and Collision.
		
		if (isCollidingWithPlayer() == false)
		{
			moved = true;
			if ((int)x < Game.player.getX() && World.isFree((int)(x + spd), this.getY())
					&& !isColliding((int)(x + spd), this.getY()))
			{
				moved = true;
				x += spd;
			}
			else if ((int)x > Game.player.getX() && World.isFree((int)(x - spd), this.getY())
					&& !isColliding((int)(x - spd), this.getY()))
			{
				moved = true;
				x -= spd;
			}
			
			if ((int)y < Game.player.getY() && World.isFree(this.getX(), (int)(y + spd))
					&& !isColliding(this.getX(), (int)(y + spd)))
			{
				moved = true;
				y += spd;
			}
			else if ((int)y > Game.player.getY() && World.isFree(this.getX(), (int)(y - spd))
					&& !isColliding(this.getX(), (int)(y - spd)))
			{
				moved = true; 
				y -= spd;
			}
		}
		
		//System of Player Life--.
		
		else 
		{ 
			moved = false;
			Player.life--;
			Player.isDamaged = true;
		}
		
		//Destroy Enemy.
		
		if (life <= 0)
		{
			destroySelf();
			return;
		}
		
		//Check Collision Bullet;
		
		collisionBullet();
		
		//Animation.
		
		if (moved)
		{
			frames++;
			if (frames > maxFrames)
			{
				frames = 0;
				curAnimation++;
				if (curAnimation >= maxAnimation)
				{
					curAnimation = 0;
				}
			}
		}
		
		//Damaged Animation.
		
		if (isDamaged)
		{
			damagedFrames++;
			if (damagedFrames >= 5)
			{
				damagedFrames = 0;
				isDamaged = false;
			}
		}
		
		/*Random Speed For Enemies.
		*if (Game.rand.nextInt(100) < 95)  //Number number from 0 to 99. If < 30.
		*/
	}
	
	public void render(Graphics g)
	{
		//Image of Animation.
		
		if (!isDamaged)
		{
			g.drawImage(animationMove[curAnimation], this.getX() - Camera.x, this.getY() - Camera.y, null);
		}
		
		//Damaged Sprite.
		
		else
		{
			g.drawImage(damagedEnemy, this.getX() - Camera.x, this.getY() - Camera.y, null);
		}
		
		//View Mask.
		
		/*
		super.render(g);
		g.setColor(Color.blue);
		g.fillRect(this.getX() + maskX - Camera.x, this.getY() + maskY - Camera.y, maskWidth, maskHeight);
		*/
	}
	
	public boolean isCollidingWithPlayer()
	{
		Rectangle enemyCurrent = new Rectangle(this.getX() + maskX, this.getY() + maskY, maskWidth, maskHeight);
		Rectangle player = new Rectangle(Game.player.getX(), Game.player.getY(), 16, 16);
		
		return (enemyCurrent.intersects(player));
	}
	
	//Collision Bullets in Enemy.
	
	public void collisionBullet()
	{
		for (int i = 0; i < Game.bullets.size(); i++)
		{
			Entity e = Game.bullets.get(i);
			if (Entity.isColliding(this, e))
			{
				life--;
				isDamaged = true; 
				Game.bullets.remove(i);
				return;
			}
		}
	}
	
	//Collision Between Enemies.
	
	public boolean isColliding(int nextX, int nextY)
	{
		moved = false;
		Rectangle enemyCurrent = new Rectangle(nextX + maskX, nextY + maskY, maskWidth, maskHeight);  //Set New Mask.
		for (int i = 0; i < Game.enemies.size(); i++)
		{
			Enemy e = Game.enemies.get(i);
			if (e == this)
			{
				continue;
			}
			
			Rectangle targetEnemy = new Rectangle(e.getX() + maskX, e.getY() + maskY, maskWidth, maskHeight);   //Set New Mask.
			if (enemyCurrent.intersects(targetEnemy))
			{
				return true;
			}
		}
		
		return false;
	}
	
	//Void Destroy Entity.
	
	public void destroySelf()
	{
		Game.enemies.remove(this);
		Game.entities.remove(this);
	}
}
