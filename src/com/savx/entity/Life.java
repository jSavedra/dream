package com.savx.entity;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.savx.main.Game;
import com.savx.world.Camera;

public class Life extends Entity {

	//Animation and WaitAnimation.
	
	private BufferedImage[] animationLight;
			
	private int frames = 0;
	private int maxFrames = 10;
	private int curAnimation = 0;
	private int maxAnimation = 4;
			
	private int waitFrames = 0;
	private int waitMaxFrames = 20;
	private int waitAnimation = 0;
	private int waitMaxAnimation = 4;
			
	private boolean wait = false;
			
	/**/
		
	public Life(int x, int y, int width, int height, BufferedImage sprite) 
	{
		super(x, y, width, height, sprite);
		
		animationLight = new BufferedImage[4];
		
		for (int i = 0; i < 4; i++)
		{
			animationLight[i] = Game.sheet.getSprite(0 + (i * 16), 4 * 16, 16, 16);
		}
	}
		
	public void update()
	{
		if (!wait)
		{
			frames++;
			if (frames == maxFrames)
			{
				frames = 0;
				curAnimation++;
				if (curAnimation >= maxAnimation)
				{
					curAnimation = 0;
					wait = true;
				}
			}
		}
			
		//Wait Animation.
			
		if (wait)
		{
			waitFrames++;
			if (waitFrames == waitMaxFrames)
			{
				waitFrames = 0;
				waitAnimation++;
				if (waitAnimation >= waitMaxAnimation)
				{
					waitAnimation = 0;
					wait = false;
				}
			}
		}
			
		/**/
	}
		
	public void render(Graphics g)
	{
		g.drawImage(animationLight[curAnimation], this.getX() - Camera.x, this.getY() - Camera.y, null);
	}
}
