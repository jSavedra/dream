package com.savx.world;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.savx.main.Game;

public class Tile {

	public static BufferedImage floorTile = Game.sheet.getSprite(0, 0, 16, 16);
	public static BufferedImage floorTileNew = Game.sheet.getSprite(32, 9 * 16, 16, 16);
	public static BufferedImage floorTileLeft = Game.sheet.getSprite(16, 0, 16, 16);
	public static BufferedImage floorTileDown = Game.sheet.getSprite(32, 0, 16, 16);
	public static BufferedImage floorTileUp = Game.sheet.getSprite(48, 0, 16, 16);
	public static BufferedImage floorTileRight = Game.sheet.getSprite(64, 0, 16, 16);
	public static BufferedImage wallTile = Game.sheet.getSprite(0, 16, 16, 16);
	public static BufferedImage tileWater = Game.sheet.getSprite(0, 9 * 16, 16, 16);
	public static BufferedImage wallTileNew = Game.sheet.getSprite(16, 9 * 16, 16, 16);
	
	private BufferedImage sprite;
	private int x;
	private int y;
	
	public Tile(int x, int y, BufferedImage sprite)
	{
		this.x = x;
		this.y = y;
		this.sprite = sprite;
	}
	
	public void update()
	{
		
	}
	
	public void render(Graphics g)
	{
		g.drawImage(sprite, x - Camera.x, y - Camera.y, null);
	}
}
