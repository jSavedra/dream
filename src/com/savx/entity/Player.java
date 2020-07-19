package com.savx.entity;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import com.savx.main.Game;
import com.savx.world.Camera;
import com.savx.world.World;

public class Player extends Entity {
	
	//Directions.
	
	private int dx = 0;
	
	//Shoot.
	
	public static boolean shooting = false;
	
	//Ammo.

	public static int ammo = 16;
	public static int maxAmmo = 16;
	
	//Life.
	
	public static double life = 202;
	public double maxLife = 202;
	
	//Animation.
	
    private int frames = 0;
	private int maxFrames = 5;
	private int curAnimation = 0;
	private int maxAnimation = 3;

	//Directions.
	
	private int rightDir = 0;
	private int leftDir = 1;
	private int dir = rightDir;
	private double spd = 1; 
	private boolean moved = false;
	public static boolean right,  left;
	public static boolean up,  down;
	
	//Images.
	
	private BufferedImage[] rightPlayer;
	private BufferedImage[] leftPlayer;
	private BufferedImage damagedPlayer;
	private BufferedImage damagedRightPlayer;
	private BufferedImage damagedLeftPlayer;
	
	//Hit.
	
	public static boolean isDamaged = false;
	private int damagedFrames = 0;
	
	//Render for Weapon.
	
	public static boolean hasWeapon = false;
	
	//Entity Player position.
	
	public Player(int x, int y, int width, int height, BufferedImage sprite) 
	{
		super(x, y, width, height, sprite);
		
		//Definition In Animations Player.

		rightPlayer = new BufferedImage[3];
		leftPlayer = new BufferedImage[3];
		damagedPlayer = Game.sheet.getSprite(8 * 16, 0, 16, 16);
		damagedRightPlayer = Game.sheet.getSprite(9 * 16, 0, 16, 16);
		damagedLeftPlayer = Game.sheet.getSprite(9 * 16, 16, 16, 16);
		
		//Loop.
		
		for (int i = 0; i < 3; i++)    //If Index < Quantity In Animation.
		{
			//Right.
			rightPlayer[i] = Game.sheet.getSprite(16 + (i * 16), 32, 16, 16);
		}
		
		for (int i = 0; i < 3; i++)
		{
			//Left
			leftPlayer[i] = Game.sheet.getSprite(16 + (i * 16), 48, 16, 16);	
		}
	}
	
	//Collision Ammo.
	
	public void checkCollisionAmmo()
	{
		if (hasWeapon)
		{
			for (int i = 0; i < Game.entities.size(); i++)
			{
				Entity current = Game.entities.get(i);
				if (current instanceof Ammo)
				{
					if (Entity.isColliding(this, current) && ammo < 16)
					{
						ammo += 12;
						if (ammo >= 16)
						{
							ammo = 16;
						}
			
						Game.entities.remove(current);
					}
				}
			}
		}
	}
	
	//Collision Life.
	
	public void checkCollisionLife()
	{
		for (int i = 0; i < Game.entities.size(); i++)
		{
			Entity current = Game.entities.get(i);
			if (current instanceof Life)
			{
				if (Entity.isColliding(this, current) && life < 202)
				{
					life += 45;
					if (life > 202)
					{
						life = 202;
					}
					Game.entities.remove(current);
				}
			}
		}
	}
	
	//Collision Weapon.
	
	public void checkCollisionWeapon()
	{
		for (int i = 0; i < Game.entities.size(); i++)
		{
			Entity current = Game.entities.get(i);
			if (current instanceof Weapon)
			{
				if (Entity.isColliding(this, current))
				{
					hasWeapon = true;
					ammo += 8;
					if (ammo >= 16)
					{
						ammo = 16;
					}
					Game.entities.remove(current);
				}
			}
		}
	}
	
	public void update()
	{
		//Movement Player.
		
		moved = false;
		if (right && World.isFree((int)(x + spd), this.getY()))
		{
			moved = true;
			dir = rightDir;
			x += spd;
		}
		else if (left && World.isFree((int)(x - spd), this.getY()))
		{
			moved = true;
			dir = leftDir;
			x -= spd;
		}
		
		if (up && World.isFree(this.getX(), (int)(y - spd)))
		{
			moved = true;
			y -= spd;
		}
		else if (down && World.isFree(this.getX(), (int)(y + spd)))
		{
			moved = true;
			y += spd;
		}
		
		//Shooting.
		
		if (shooting)
		{
			shooting = false;
			
			if (hasWeapon && ammo > 0)
			{
				ammo--;
				
				//Position of Bullet.
				
				int px = 0;
				int py = 5;
				
				if (ammo <= 0)
				{
					ammo = 0;
				}
				
				if (dir == rightDir)
				{
					px = 22;
					dx = 1;
				}
				else
				{
					px = -12;
					dx = -1;
				}
				
				Bullet bullet = new Bullet(this.getX() + px, this.getY() + py, 3, 3, dx, 0, null);
				Game.bullets.add(bullet);
			}
		}
		
		//Restart Game In Life = 0.
		
		if (life <= 2)
		{
			//GameOver.
			life = 0;
			Game.gameState = "gameOver";
		}
		
		checkCollisionLife();
		checkCollisionAmmo();
		checkCollisionWeapon();
		
		//Loop Animation.
		
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
				
		//Loop Damaged Animation.
				
		if (isDamaged)
		{
			damagedFrames++;
			if (damagedFrames >= 5)
			{
				damagedFrames = 0;
				isDamaged = false;
			}		
		}
		
		//Camera and Clamp.

		cameraClamp();
	}
	
	public void cameraClamp()
	{
		Camera.x = Camera.clamp(this.getX() - (Game.width / 2), 0, World.width * 16 - Game.width);
		Camera.y = Camera.clamp(this.getY() - (Game.height / 2), 0, World.height * 16 - Game.height);
	}
	
	public void render(Graphics g)
	{
		//Animation For Keys.
		
		if (!isDamaged)
		{
			if (dir == rightDir)
			{
				//Render Sprite.
				
				g.drawImage(rightPlayer[curAnimation], this.getX() - Camera.x, this.getY() - Camera.y, null);
				
				//Render Weapon.
				
				if (hasWeapon)
				{
					//Render Weapon Right.
					g.drawImage(Entity.weaponRightEntity, this.getX() + 8 - Camera.x, this.getY() - Camera.y, null);
				}
			}
			else if (dir == leftDir)
			{
				//Render Sprite.
				
				g.drawImage(leftPlayer[curAnimation], this.getX() - Camera.x, this.getY() - Camera.y, null);
				
				//Render Weapon. 
				
				if (hasWeapon)
				{
					//Render Weapon Left.
					g.drawImage(Entity.weaponLeftEntity, this.getX() - 10 - Camera.x, this.getY() - Camera.y, null);
				}
			}
		}
		
		//Damaged Sprite.
		
		if (isDamaged)
		{
			if (dir == rightDir)
			{
				g.drawImage(damagedRightPlayer, this.getX() - Camera.x, this.getY() - Camera.y, null);
				
				//Render Weapon in Damage.
				
				if (dir == rightDir && hasWeapon)
				{
					g.drawImage(Entity.weaponRightEntity, this.getX() + 8 - Camera.x, this.getY() - Camera.y, null);
				}
			}
			else if (dir == leftDir)
			{
				g.drawImage(damagedLeftPlayer, this.getX() - Camera.x, this.getY() - Camera.y, null);
				
				//Render Weapon in Damage.
				
				if (dir == leftDir && hasWeapon)
				{
					g.drawImage(Entity.weaponLeftEntity, this.getX() - 8 - Camera.x, this.getY() - Camera.y, null);
				}
			}
			else 
			{
				g.drawImage(damagedPlayer, this.getX() - Camera.x, this.getY() - Camera.y, null);
			}
		}
	}
}







