package com.savx.world;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import com.savx.entity.Ammo;
import com.savx.entity.Enemy;
import com.savx.entity.Entity;
import com.savx.entity.Life;
import com.savx.entity.Player;
import com.savx.entity.Weapon;
import com.savx.graphics.Spritesheet;
import com.savx.graphics.UI;
import com.savx.main.Game;

public class World {

	public static Tile[] tiles;
	public static int width;
	public static int height;
	public static int tileSize = 16;
	
	public World(String path)
	{
		try {
			BufferedImage map = ImageIO.read(getClass().getResource(path));
			width = map.getWidth();
			height = map.getHeight();
			int[] pixels = new int[width * height];
			tiles = new Tile[width * height];
			map.getRGB(0, 0, width, height, pixels, 0, width);
			for (int xx = 0; xx < width; xx++)       //X of Tile.
			{
				for (int yy = 0; yy < height; yy++)  //Y of Tile.
				{
					int curPixels = pixels[xx + (yy * width)];
					tiles[xx + (yy * width)] = new FloorTile(xx * 16, yy * 16, Tile.floorTile);  //(xx * 16) == Size of tile.
					if (curPixels == 0xFF000000)       //0x == First Parameter, FF == Second parameter.
					{
						//Floor.
						tiles[xx + (yy * width)] = new FloorTile(xx * 16, yy * 16, Tile.floorTile);
					}
					else if (curPixels == 0xFFFFFFFF)
					{
						//Wall.
						tiles[xx + (yy * width)] = new WallTile(xx * 16, yy * 16, Tile.wallTile);
					}
					else if (curPixels == 0xFF7FD5D8)
					{
						//FloorTileNew.
						tiles[xx + (yy * width)] = new FloorTile(xx * 16, yy * 16, Tile.floorTileNew);
					}
					else if (curPixels == 0xFFB28F08)
					{
						//WallTileNew.
						tiles[xx + (yy * width)] = new WallTile(xx * 16, yy * 16, Tile.wallTileNew);
					}
					else if (curPixels == 0xFFAD1C9E)
					{
						//TileWater.
						tiles[xx + (yy * width)] = new WallTile(xx * 16, yy * 16, Tile.tileWater);
					}
					else if (curPixels == 0xFF6DFF81)
					{
						//TileFloorLeft.
						tiles[xx + (yy * width)] = new FloorTile(xx * 16, yy * 16, Tile.floorTileLeft);
					}
					else if (curPixels == 0xFF14FF47)
					{
						//TileFloorDown.
						tiles[xx + (yy * width)] = new FloorTile(xx * 16, yy * 16, Tile.floorTileDown);
					}
					else if (curPixels == 0xFF99FFA4)
					{
						//TileFloorUp.
						tiles[xx + (yy * width)] = new FloorTile(xx * 16, yy * 16, Tile.floorTileUp);
					}
					else if (curPixels == 0xFFCCFFD1)
					{
						//TileFloorRight.
						tiles[xx + (yy * width)] = new FloorTile(xx * 16, yy * 16, Tile.floorTileRight);
					}
					else if (curPixels == 0xFFFF6A00)
					{
						//Weapon.
						Game.entities.add(new Weapon(xx * 16, yy * 16, 16, 16, Entity.weaponEntity));
					}
					else if (curPixels == 0xFFFF7F7F)
					{
						//Life.
						Life life = new Life(xx * 16, yy * 16, 16, 16, Entity.lifeEntity);
						life.setMask(0, 0, 16, 16);
						Game.entities.add(life);
					}
					else if (curPixels == 0xFFFFD800)
					{
						//Bullet.
						Game.entities.add(new Ammo(xx * 16, yy * 16, 16, 16, Entity.bulletEntity));
					}
					else if (curPixels == 0xFFFF0000)
					{
						//Enemy.
						BufferedImage[] buf = new BufferedImage[2];
						buf[0] = Game.sheet.getSprite(112, 32, 16, 16);
						buf[1] = Game.sheet.getSprite(128, 32, 16, 16);
						Enemy en = new Enemy(xx * 16, yy * 16, 16, 16, Entity.enemyEntity);
						Game.entities.add(en);
						Game.enemies.add(en);          //Collides only enemies instead of all entities.
					}
					else if (curPixels == 0xFF0026FF)
					{
						//Player.
						Game.player.setX(xx * 16);
						Game.player.setY(yy * 16);
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void restartGame(String level)
	{
		Game.entities.clear();
		Game.enemies.clear();
		Game.ui = new UI();
		Game.entities = new ArrayList<Entity>();
		Game.enemies = new ArrayList<Enemy>();
		Game.sheet = new Spritesheet("/spritesheet.png");
		Game.player = new Player(0, 0, 16, 16, Game.sheet.getSprite(0, 32, 16, 16));
		Game.entities.add(Game.player);	 			
		Game.world = new World("/"+level);
		return;
	}
	
	//Tile Collision.
	
	public static boolean isFree(int nextX, int nextY)
	{
		int x1 = nextX / tileSize;
		int y1 = nextY / tileSize;
		
		int x2 = (nextX + tileSize - 1) / tileSize;
		int y2 = nextY / tileSize;
		
		int x3 = nextX / tileSize;
		int y3 = (nextY + tileSize - 1) / tileSize;
		
		int x4 = (nextX + tileSize - 1) / tileSize;
		int y4 = (nextY + tileSize - 1) / tileSize;
		
		return !((tiles[x1 + (y1 * width)] instanceof WallTile) ||
				(tiles[x2 + (y2 * width)] instanceof WallTile) ||
				(tiles[x3 + (y3 * width)] instanceof WallTile) ||
				(tiles[x4 + (y4 * width)] instanceof WallTile));
	}
	
	public void render(Graphics g)
	{
		//Clamp and Optimization.
		
		int startX = Camera.x >> 4;    //>> 4 = / 16.
		int startY = Camera.y >> 4;
		
		int finalX = startX + (Game.width >> 4);  
		int finalY = startY + (Game.height >> 4);
		
		//Rendering of Tile.
		
		for (int xx = startX; xx <= finalX; xx++)
		{
			for (int yy = startY; yy <= finalY; yy++)
			{
				if (xx < 0 || yy < 0 || xx >= width || yy >= height)  //Verification of Negatives Numbers.
				{
					continue;
				}
				
				Tile tile = tiles[xx + (yy * width)];
				tile.render(g);            
			}
		}
	}
}

