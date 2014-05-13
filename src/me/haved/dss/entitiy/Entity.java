package me.haved.dss.entitiy;

import me.haved.dss.core.GameDroneStrikeStomp;
import me.haved.engine.Time;

public class Entity
{
	public static final float GRAVITY = 890f;
	
	protected float x, y, width, height;
	protected float xSpeed, ySpeed;
	
	public void update(GameDroneStrikeStomp game)
	{
		
	}
	
	public void move()
	{
		x += xSpeed * Time.delta();
		y += ySpeed * Time.delta();
	}
	
	public void render()
	{
		
	}
}
