package me.haved.dss.entitiy;

import org.lwjgl.input.Keyboard;
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
	
	private float friction = 4f;
	
	private float speed = 10;
	private float maxSpeed = 500;
	private float jump = 1000;
	
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
		
		xSpeed -= xSpeed * friction * Time.delta();
		
		input(game);
		move(game);
		
		if(y + height > RenderEngine.getCanvasHeight())
		{
			y = RenderEngine.getCanvasHeight() - height;
			ySpeed = 0;
		}

		updateAnimation();
	}
	
	private void input(GameDroneStrikeStomp game)
	{
		if(Keyboard.isKeyDown(GameDroneStrikeStomp.KEY_CODE_LEFT))
		{
			xSpeed -= speed * 1000 * Time.delta();
		}
		
		if(Keyboard.isKeyDown(GameDroneStrikeStomp.KEY_CODE_RIGHT))
		{
			xSpeed += speed * 1000 * Time.delta();
		}
		
		if(Keyboard.isKeyDown(GameDroneStrikeStomp.KEY_CODE_JUMP))
			ySpeed = -jump;
		
		xSpeed = Math.max(-maxSpeed, Math.min(maxSpeed, xSpeed));
	}
	
	private void updateAnimation()
	{
		animation += Math.abs(xSpeed)*Time.delta();
		animation %= 140;
		
		if(xSpeed > 0.2f)
			facingRight = true;
		if(xSpeed < -0.2f)
			facingRight = false;
	}
	
	public void render()
	{
		RenderEngine.resetColor();
		sprite.bind();
		float i = animation < 70 ? 0 : 0.5f;
		float faceShift = facingRight ? 0.25f : -0.25f;
		RenderEngine.fillRectangleWithTexture(x, y, width, height, i+(0.25f+faceShift), 0, i+(0.25f-faceShift), 1);
	}
}
