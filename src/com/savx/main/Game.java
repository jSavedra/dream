package com.savx.main;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.JFrame;

import com.savx.entity.Bullet;
import com.savx.entity.Enemy;
import com.savx.entity.Entity;
import com.savx.entity.Player;
import com.savx.graphics.Spritesheet;
import com.savx.graphics.UI;
import com.savx.world.World;

public class Game extends Canvas implements Runnable, KeyListener {

	//General.
	
	private static final long serialVersionUID = 1L;
	public Thread thread;
	public JFrame frame;
	private BufferedImage img;
	private boolean isRunning;
	public static final int width = 240;
	public static final int height = 160;
	public static final double scale = 4.5;   
	
	//Game State.
	
	public static String gameState = "Menu";
	
	//Game Over.
	
	private boolean animationMessageGameOver = true;
	private int framesGameOver = 0; 
	private boolean restartGame = false; 
	
	//Levels. 
	
	private int currentLevel = 1;
	private int maxLevel = 2; 
	
	//List Entity.
	
	public static List<Entity> entities;
	
	//List Enemy.
	
	public static List<Enemy> enemies;
	
	//List Bullets.
	
	public static List<Bullet> bullets;
	
	//Sprite.
	
	public static Spritesheet sheet;
	
	//World.
	
	public static World world;
	
	//Menu.
	
	public static Menu menu;
	
	//Player.
	
	public static Player player;
	
	//Random.
	
	public static Random rand;
	
	//UI.
	
	public static UI ui;
	
	public static void main(String[] args)
	{
		Game game = new Game();
		game.start();
	}
	
	public Game()
	{
		setPreferredSize(new Dimension(width * (int)scale, height * (int)scale));
		initFrame();
		addKeyListener(this);
		
		//Instance Random.
		
		rand = new Random();
		
		//Rendering Objects.
		
		img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB); 
		ui = new UI();
		entities = new ArrayList<Entity>();
		enemies = new ArrayList<Enemy>();
		bullets = new ArrayList<Bullet>();
		sheet = new Spritesheet("/spritesheet.png");
		player = new Player(0, 0, 16, 16, sheet.getSprite(0, 32, 16, 16));
		entities.add(player);	 			//Add Player to Entity.
		menu = new Menu();
		world = new World("/level1.png");	//After All Objects.
	}
	
	public void initFrame()
	{
		frame = new JFrame("Game #1");
		frame.add(this);
		frame.setResizable(false);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
	
	public void update()
	{
		//Run Only if Playable.
		
		if (gameState == "Playing")
		{
			restartGame = false;
			
			//For Entity.
			
			for (int i = 0; i < entities.size(); i++)
			{
				entities.get(i).update();
				
				/*
				 * entities.get(i).update(); == Entity e = entities.get(i); e.update;
				 */
			}
			
			for (int i = 0; i < bullets.size(); i++)
			{
				bullets.get(i).update();
			}
			
			//New Level.
			
			if (enemies.size() == 0)
			{
				currentLevel++;
				if (currentLevel > maxLevel)
				{
					currentLevel = 1;
				}
				
				String newLevel = "level"+currentLevel+".png"; 
				World.restartGame(newLevel);
			}
		}
		
		else if (gameState == "gameOver")
		{
			Player.hasWeapon = false;
			Player.ammo = 0;
			Player.life = 202;
			framesGameOver++;
			if (framesGameOver == 50)
			{
				framesGameOver = 0;
				if (animationMessageGameOver)
				animationMessageGameOver = false;
				else 
				{
					animationMessageGameOver = true;
				}
			}
			
			if (restartGame)
			{
				currentLevel = 1;
				gameState = "Playing";
				restartGame = false;
				String newLevel = "level"+currentLevel+".png"; 
				World.restartGame(newLevel);
			}
		}
		
		else if (gameState == "Menu")
		{
			//Menu.
			menu.update();
		}
	}
	
	public void render()
	{
		//Background and Definition of Graphics and BufferStrategy.
		
		BufferStrategy bs = this.getBufferStrategy();
		if (bs == null)                                     
		{
			this.createBufferStrategy(3);
			return;
		} 
		Graphics g = img.getGraphics();               
		g.setColor(new Color(0, 0, 0));                     
		g.fillRect(0, 0, width, height);   
		
		//Rendering World.
		
		world.render(g);
		
		//Rendering Entity.
		
		for (int i = 0; i < entities.size(); i++)
		{
			entities.get(i).render(g);
		}
		
		for (int i = 0; i < bullets.size(); i++)
		{
			bullets.get(i).render(g);
		}
		
		//Render UI.
		
		ui.render(g);
		
		g.dispose();    								//Optimization.
		g = bs.getDrawGraphics();                      
		g.drawImage(img, 0, 0, width * (int)scale, height * (int)scale, null);
		g.setColor(Color.white);
		g.setFont(new Font("Arial", Font.BOLD, 30));
		if (Player.hasWeapon)
		{
			g.drawString(Player.ammo + " / " + Player.maxAmmo, 855, 620);
		}
		if (gameState == "gameOver")
		{
			Graphics2D g2 = (Graphics2D) g;    //Works With Opacity.
			g2.setColor(new Color(0, 0, 0, 220));
			g2.fillRect(0, 0, width * (int)scale, height * (int)scale);
			g.setFont(new Font("Arial", Font.BOLD, 50));
			g.setColor(Color.white);
			g.drawString("Game Over", 360, 200);
			g.setFont(new Font("Arial", Font.BOLD, 40));
			g.setColor(Color.white);
			if (animationMessageGameOver)
			{
				g.drawString(">> Press Enter For Restarting The Game <<", 90, 350);
			}
		}
		else if (gameState == "Menu")
		{
			menu.render(g);
		}
		
		bs.show();                
	}
	
	public synchronized void start()
	{
		isRunning = true;
		thread = new Thread(this);
		thread.start();
	}
	
	public synchronized void stop()
	{
		isRunning = false;
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	//Looping.
	
	public void run() 
	{
		long lastTime = System.nanoTime();
		double amountOfTicks = 60.0;
	    double ns = 1000000000 / amountOfTicks;
	    double delta = 0;
	    int frames = 0;
	    long timer = System.currentTimeMillis();
	    while(isRunning)
	    {
	    	requestFocus();
	    	long now = System.nanoTime();
	    	delta += (now - lastTime) / ns;
	    	lastTime = now;
	    	if (delta >= 1)
	    	{
	    		update();
	    		render();
	    		frames++;
	    		delta--;
	    	}
	    	if (System.currentTimeMillis() - timer >= 1000)
	    	{
	    		System.out.println("FPS: "+frames);
	    		frames = 0;
	    		timer += 1000;
	    	}	
	    }
	  stop();
	}

	public void keyTyped(KeyEvent e) 
	{
		
	}

	//Pressed Key.
	
	public void keyPressed(KeyEvent e) 
	{
		if (e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_D)
		{
			Player.right = true;
		}
		else if (e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_A)
		{
			Player.left = true;
		}
		
		//Separate with if and else if to be able to walk diagonally.
		
		if (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_W)
		{
			Player.up = true;
			
			if (gameState == "Menu")
			{
				Menu.up = true;
			}
		}
		
		else if (e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_S)
		{
			Player.down = true;
			
			if (gameState == "Menu")
			{
				Menu.down = true;
			}
		}
		
		//Shooting
		
		if (e.getKeyCode() == KeyEvent.VK_SPACE)
		{
			Player.shooting = true;
		}
		
		//Restart and Interaction in Menu.
		
		if (e.getKeyCode() == KeyEvent.VK_ENTER)
		{
			restartGame = true;
			if (gameState == "Menu")
			{
				Menu.enter = true;
			}
		}
		
		if (e.getKeyCode() == KeyEvent.VK_ESCAPE)
		{
			gameState = "Menu";
			Menu.paused = true;
		}
	}

	//Released Key.
	
	public void keyReleased(KeyEvent e) 
	{
		if (e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_D)
		{
			Player.right = false;
		}
		else if (e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_A)
		{
			Player.left = false;
		}
		
		//Separate with if and else if to be able to walk diagonally.
		
		if (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_W)
		{
			Player.up = false;
		}
		
		else if (e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_S)
		{
			Player.down = false;
		}
		
		//Shooting.
		
		if (e.getKeyCode() == KeyEvent.VK_SPACE)
		{
			Player.shooting = false;
		}
	}
}
