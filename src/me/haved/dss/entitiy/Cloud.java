package me.haved.dss.entitiy;

import me.haved.dss.core.DSSTextureLoader;
import me.haved.dss.core.GameDroneStrikeStomp;
import me.haved.engine.RenderEngine;

import org.newdawn.slick.opengl.Texture;

public class Cloud extends Entity implements Collider
{
	private static Texture sprite;
	
	public Cloud(float x, float y, float xSpeed)
	{
		this.x = x;
		this.y = y;
		width = 128;
		height = 32;
		
		this.xSpeed = xSpeed;
	}
	
	public static void init()
	{
		sprite = DSSTextureLoader.loadTexture("cloud.png");
	}
	
	public void update(GameDroneStrikeStomp game)
	{
		move(game);
		
		if(getX2()<0 | getX()>game.worldWidth)
			kill();
	}
	
	public void render()
	{
		RenderEngine.resetColor();
		sprite.bind();
		RenderEngine.fillRectangleWithTexture(x, y, width, height, 0, 0, 1, 1);
	}
}
