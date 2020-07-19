package com.savx.entity;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.savx.main.Game;
import com.savx.world.Camera;

public class Bullet extends Entity {
	
	//Timer for Destruction.
	
	private int timer = 0;
	private int maxTimer = 25;
	
	//Directions.
	
	private int dx;
	private int dy;
	
	//Speed.
	
	private double spd = 2;
	
	public Bullet(int x, int y, int width, int height, int dx, int dy, BufferedImage sprite) 
	{
		super(x, y, width, height, sprite);
		
		this.dx = dx;
		this.dy = dy;
	}
	
	public void update()
	{
		//Speed Directions.
		
		x += dx * spd;
		y += dy * spd;
		
		//Destroy Bullet;
		
		timer++;
		if (timer > maxTimer)
		{
			Game.bullets.remove(this);
			return;
		}
	}
	
	public void render(Graphics g)
	{
		//Render Bullet.
		
		g.setColor(new Color(128, 0, 128));
		g.fillRect(this.getX() - Camera.x, this.getY() - Camera.y, width, height);
	}
}
