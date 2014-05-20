package me.haved.dss.entitiy;

import me.haved.dss.core.DSSTextureLoader;
import me.haved.dss.core.GameDroneStrikeStomp;
import me.haved.engine.RenderEngine;
import me.haved.engine.Time;
import me.haved.engine.Util;

import org.newdawn.slick.opengl.Texture;

public class Drone extends Entity implements Collider
{
	private static Texture sprite;
	
	private float bulletSpeed = 800;
	
	private float anim;
	public float speed;
	private float maxSpeed;
	public float rotation;
	
	public float playerBulletsLeft = 5;
	public float bulletTimer = 4;
	
	public Drone(float x, float y, float speed, float maxSpeed)
	{
		this.x = x;
		this.y = y;
		width  = 128;
		height = 32;
		this.speed = speed;
		this.maxSpeed = maxSpeed;
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
		xSpeed = (float)Math.cos(Math.toRadians(rotation))*speed+120;
		ySpeed = (float)Math.sin(Math.toRadians(rotation))*speed;
		bulletUpdate(game);
		move(game);
	}
	
	public void bulletUpdate(GameDroneStrikeStomp game)
	{
		if(bulletTimer > 0)
		{
			bulletTimer -= Time.delta();
			if(bulletTimer <= 0)
			{
				float rad = (float) Math.toRadians(rotation);
				float cos = (float) Math.cos(rad);
				float sin = (float) Math.sin(rad);
				game.bullets.add(new Bullet(getCentreX() + cos*width/2 -4, getCentreY() + sin*width/2 -4, cos*bulletSpeed, sin*bulletSpeed));
				bulletTimer = 3 + Util.randomFloat(3);
			}
		}
	}
	
	@Override
	public void render()
	{
		RenderEngine.resetColor();
		sprite.bind();
		float frame = anim < 0.5f ? 0:0.5f;
		RenderEngine.pushMatrix();
		RenderEngine.translate(getCentreX(), getCentreY(), 0);
		RenderEngine.pushMatrix();
		RenderEngine.rotate(rotation, 0, 0, 1);
		RenderEngine.fillRectangleWithTexture(-width/2, -height/2, width, height, 0, frame, 1, frame+0.5f);
		RenderEngine.popMatrix();
		RenderEngine.popMatrix();
	}
	
	public void speedChange(float dir)
	{
		speed += dir;
		
		speed = Math.min(maxSpeed, Math.max(100, speed));
	}
}
