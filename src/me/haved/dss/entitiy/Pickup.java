package me.haved.dss.entitiy;

import me.haved.dss.core.DSSTextureLoader;
import me.haved.dss.core.GameDroneStrikeStomp;
import me.haved.engine.RenderEngine;

import org.newdawn.slick.opengl.Texture;

public class Pickup extends Entity
{
	public static final int HEALTH_PICKUP = 0;

	private static Texture heart;
	
	private int type;
	
	public Pickup(int type, float x, float y)
	{
		this.type = type;
		
		this.x = x;
		this.y = y;
		width = 32;
		height = 32;
		
		ySpeed = 200;
	}
	
	public static void init()
	{
		heart = DSSTextureLoader.loadTexture("heart.png");
	}
	
	@Override
	public void update(GameDroneStrikeStomp game)
	{
		move(game);
	}
	
	public void render()
	{
		getTexture(this.type).bind();
		RenderEngine.fillRectangleWithTexture(x, y, width, height, 0, 0, 1, 1);
	}
	
	private Texture getTexture(int type)
	{
		switch(type){
		case HEALTH_PICKUP: return heart;
		default: return null;
		}
	}
}
