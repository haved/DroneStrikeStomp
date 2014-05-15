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
	
	private int maxHealth = 5;
	private int health = 1;
	
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
	
	@Override
	public void move(GameDroneStrikeStomp game)
	{
		float newX = x + xSpeed * Time.delta();
		float newX2 = newX + width;
		float newY = y + ySpeed * Time.delta();
		float newY2 = newY + height;
		float x2 = x + width;
		float y2 = y + height;
		
		for(Collider c:game.clouds)
			checkCollider(c, x2, y2, newX, newY, newX2, newY2);
		
		super.move(game);
	}
	
	private void checkCollider(Collider c, float x2, float y2, float newX, float newY, float newX2, float newY2)
	{
		if(xSpeed < 0)
		{
			if(isBlockedLeft(c, newX, newX2, y2))
			{
				x = c.getX2();
				xSpeed = 0;
			}
		}
		else if(xSpeed > 0)
		{
			if(isBlockedRight(c, newX, newX2, y2))
			{
				x = c.getX() - width;
				xSpeed = 0;
			}
		}
		
		if(ySpeed < 0)
		{
			if(isBlockedUp(c, newY, newY2, x2))
			{
				y = c.getY2();
				ySpeed = 0;
			}
		}
		else if(ySpeed > 0)
		{
			if(isBlockedDown(c, newY, newY2, x2))
			{
				y = c.getY() - height;
				ySpeed = 0;
			}
		}
	}
	
	private boolean isBlockedLeft(Collider c, float newX, float newX2, float y2)
	{
		return newX < c.getX2() & newX > c.getX() & y < c.getY2() & y2 > c.getY();
	}
	
	private boolean isBlockedRight(Collider c, float newX, float newX2, float y2)
	{
		return newX2 > c.getX() & newX2 < c.getX2() & y < c.getY2() & y2 > c.getY();
	}
	
	private boolean isBlockedUp(Collider c, float newY, float newY2, float x2)
	{
		return newY < c.getY2() & newY > c.getY() & x < c.getX2() & x2 > c.getX();
	}
	
	private boolean isBlockedDown(Collider c, float newY, float newY2, float x2)
	{
		return newY2 > c.getY() & newY2 < c.getY2() & x < c.getX2() & x2 > c.getX();
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

	public float getCameraScroll(GameDroneStrikeStomp game)
	{
		return Math.max(0, Math.min(game.worldWidth-RenderEngine.getCanvasWidth(), x - RenderEngine.getCanvasWidth() / 2));
	}
	
	public void damage(int amount)
	{
		health -= amount;
		if(health <= 0)
		{
			health = maxHealth;
		}
	}
	
	public void heal(int amount)
	{
		health += amount;
		health = Math.min(maxHealth, health);
	}
	
	public int getHealth()
	{
		return health;
	}
}
