package com.savx.main;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

public class Menu {

	//Movements In Menu.
	
	public static boolean up, down, enter;
	
	//Options.
	
	private String[] options = {"New Game", "Quit"};
	
	//Pause.
	
	public static boolean paused = false;
	
	//Verifications.
	
	private int currentOption = 0;
	private int maxOption = options.length - 1;
	
	public void update()
	{
		if (up)
		{
			up = false;
			System.out.println("Up");
			currentOption--;
			if (currentOption < 0)
			{
				currentOption = maxOption;
			}
		}
		
		if (down)
		{
			down = false;
			currentOption++;
			if (currentOption > maxOption)
			{
				currentOption = 0;
			}
		}
		
		if (enter)
		{
			enter = false;
			if (options[currentOption] == "New Game" || options[currentOption] == "Continue")
			{
				Game.gameState = "Playing";
				paused = false;
			}
			
			else if (options[currentOption] == "Quit")
			{
				System.exit(1);
			}
		}
	}
	
	public void render(Graphics g)
	{
		//Title and BackGround.
		
		g.setColor(Color.black);
		g.fillRect(0, 0, Game.width * (int)Game.scale, Game.height* (int)Game.scale);
		
		//Name of The Game.
		
		g.setColor(Color.blue);
		g.setFont(new Font("Arial", Font.BOLD, 60));
		g.drawString("Dream?", 380, 100);
		
		//Options.
		
		g.setColor(Color.white);
		g.setFont(new Font("Arial", Font.BOLD, 45));
		if (!paused)
		{
			g.drawString("New Game", 383, 250);
		}
		else 
		{
			g.drawString("Continue", 383, 250);
		}
		
		g.drawString("Quit", 383, 350);
		
		//Arrows and Checks.
		
		if (options[currentOption] == "New Game")
		{
			g.drawString(">", 320, 250);
		}
		
		else if (options[currentOption] == "Quit")
		{
			g.drawString(">", 320, 350);
		}
	}
}




