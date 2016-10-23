package me.haved.dss.entitiy;

import me.haved.dss.core.DSSTextureLoader;
import me.haved.engine.RenderEngine;

import org.newdawn.slick.opengl.Texture;

public class Wall extends Entity
{
	private static Texture wallTexture;
	
	private float health;
	private float maxHelath;
	
	public Wall(float x, float y, float width, float height, float maxHealth)
	{
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.maxHelath = maxHealth;
		this.health = maxHealth;
	}
	
	public float getScaledHelath()
	{
		return health/maxHelath;
	}
	
	
	@Override
	public void render()
	{
		wallTexture.bind();
		float sh = getScaledHelath();
		float texX = sh > 0.75f ? 0 : (sh > 0.4f ? 0.25f : (sh > 0 ? 0.5f : 0.75f));
		RenderEngine.fillRectangleWithTexture(x, y, width, height, texX, 0, texX+0.25f, 1);
	}
	
	public static void init()
	{
		wallTexture = DSSTextureLoader.loadTexture("wall.png");
	}
}
