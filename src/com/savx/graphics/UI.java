package com.savx.graphics;
import java.awt.Color;
import java.awt.Graphics;
import com.savx.entity.Player;
import com.savx.main.Game;

public class UI {
	
	public void render(Graphics g)
	{
		g.setColor(Color.green);
		g.fillRect(4, 4, (int)((Player.life/Game.player.maxLife) * 70), 8);  //Render Front life.
		g.setColor(Color.white);
		g.drawRect(4, 4, 70, 8);   //Render Back Life.
	}
}
