package me.haved.dss.entitiy;

import me.haved.dss.core.GameDroneStrikeStomp;
import me.haved.engine.Time;

public class Entity
{
	public static final float GRAVITY = 1500f;
	
	private boolean dead;
	
	protected float x, y, width, height;
	protected float xSpeed, ySpeed;
	
	public void update(GameDroneStrikeStomp game)
	{
		
	}
	
	public void move(GameDroneStrikeStomp game)
	{
		x += xSpeed * Time.delta();
		y += ySpeed * Time.delta();
	}
	
	public void render()
	{
		
	}
	
	public void kill()
	{
		dead = true;
	}
	
	public boolean isDead()
	{
		return dead;
	}
	
	public float getX(){return x;}

	public float getY(){return y;}

	public float getX2(){return x+width;}

	public float getY2(){return y+height;}
	
	public float getXSpeed(){return xSpeed;}
	
	public float getYSpeed(){return ySpeed;}
	
	public float getCentreX(){return x+width/2;}
	
	public float getCentreY(){return y+height/2;}
}
