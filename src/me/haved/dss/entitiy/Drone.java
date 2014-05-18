package me.haved.dss.entitiy;

import me.haved.dss.core.DSSTextureLoader;
import me.haved.dss.core.GameDroneStrikeStomp;
import me.haved.engine.RenderEngine;
import me.haved.engine.Time;

import org.newdawn.slick.opengl.Texture;

public class Drone extends Entity implements Collider
{
	private static Texture sprite;
	
	private float anim;
	
	public Drone(float x, float y, float xSpeed)
	{
		this.x = x;
		this.y = y;
		width  = 128;
		height = 32;
		this.xSpeed = xSpeed;
	}
	
	public static void init()
	{
		sprite = DSSTextureLoader.loadTexture("drone.png");
	}
	
	@Override
	public void update(GameDroneStrikeStomp game)
	{
		anim += Time.delta();
		anim %= 1;
		move(game);
	}
	
	@Override
	public void render()
	{
		RenderEngine.resetColor();
		sprite.bind();
		float frame = anim < 0.5f ? 0:0.5f;
		RenderEngine.fillRectangleWithTexture(x, y, width, height, 0, frame, 1, frame+0.5f);
	}
}
