package me.haved.dss.entitiy;

import org.newdawn.slick.opengl.Texture;

import me.haved.dss.core.DSSTextureLoader;
import me.haved.dss.core.GameDroneStrikeStomp;
import me.haved.engine.RenderEngine;
import me.haved.engine.Time;

public class Player extends Entity
{
	private static Texture sprite;
	
	private float animation = 0;
	private boolean facingRight = false;
	
	
	public Player()
	{
		width = 32;
		height = 64;
	}
	
	public static void init()
	{
		sprite = DSSTextureLoader.loadTexture("player.png");
	}
	
	public void update(GameDroneStrikeStomp game)
	{
		ySpeed += GRAVITY * Time.delta();
		move();
		if(y + height > RenderEngine.getCanvasHeight())
			y = RenderEngine.getCanvasHeight() - height;
		
		updateAnimation();
	}
	
	private void updateAnimation()
	{
		animation += Time.delta();
		animation %= 0.3f;
		
		if(xSpeed > 0.2f)
			facingRight = true;
		if(xSpeed < -0.2f)
			facingRight = false;
	}
	
	public void render()
	{
		sprite.bind();
		float i = animation < 0.15f ? 0 : 0.5f;
		byte faceShift = facingRight ? (byte)1 : 0;
		RenderEngine.fillRectangleWithTexture(x, y, width, height, i, (0+faceShift)%2, i+0.5f, (1+faceShift)%2);
	}
}
