package me.haved.dss.entitiy;

import me.haved.dss.core.DSSTextureLoader;
import me.haved.dss.core.GameDroneStrikeStomp;
import me.haved.engine.RenderEngine;
import me.haved.engine.Time;
import me.haved.engine.Util;

import org.lwjgl.util.vector.Vector2f;
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
	
	private Player rider;
	
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
		if(y < -width)
			kill();
		if(y > RenderEngine.getCanvasHeight()+width)
			kill();
		if(x > game.worldWidth+width)
			kill();
		if(x < -width)
			kill();
		checkProjectilesAndPickupsAndGround(game);
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
				game.bullets.add(new Bullet(getCentreX() + cos*width/1.5f, getCentreY() + sin*width/2 -4, cos*bulletSpeed, sin*bulletSpeed));
				bulletTimer = 3 + Util.randomFloat(3);
			}
		}
	}
	
	private void checkProjectilesAndPickupsAndGround(GameDroneStrikeStomp game)
	{
		if(rider != null)
			for(Pickup p:game.pickups)
			{
				if(isEntityInsideDrone(p))
					p.pickedUp(rider);
			}
		
		for(Bullet b:game.bullets)
		{
			if(isEntityInsideDrone(b))
			{
				explode(game, 5);
				b.kill();
			}
		}
		
		for(Drone d:game.drones)
		{
			if(d==this)
				continue;
			if(isDroneInsideDrone(d))
			{
				explode(game, 10);
				d.explode(game, 10);
			}
		}
		
		if(isDroneBellowGrass(RenderEngine.getCanvasHeight()-6))
			explode(game, 10);
	}
	
	private boolean isEntityInsideDrone(Entity e)
	{
		return e.getCentreX() > getX() & e.getCentreX() < getX2() & e.getCentreY() > getY() & e.getCentreY() < getY2(); //TODO
	}
	
	private boolean isDroneInsideDrone(Entity e)
	{
		return e.getCentreX() > getX() & e.getCentreX() < getX2() & e.getCentreY() > getY() & e.getCentreY() < getY2(); //TODO
	}
	
	private boolean isDroneBellowGrass(int i)
	{
		return getCentreY() > i; //TODO
	}
	
	private void explode(GameDroneStrikeStomp game, int riderDamage)
	{
		for(int i = 0; i < 12+Util.randomInt(10); i++)
		{
			Vector2f particlePos = new Vector2f(Util.randomFloat(width)-width/2,Util.randomFloat(height)-height/2);
			float dist = particlePos.length();
			float rad = (float) Math.atan2(-particlePos.y, particlePos.x);
			rad += (float)Math.toRadians(rotation);
			
			game.particles.add(new ExplotionParticle(getCentreX()+(float)Math.cos(rad)*dist, getCentreY()+(float)Math.sin(rad)*dist, 20+Util.randomFloat(12), 10+Util.randomFloat(6), 10+Util.randomFloat(10), Util.randomFloat(0.3f)));
		}
		kill();
		if(rider != null)
			rider.damage(riderDamage);
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
	
	public void setRider(Player p)
	{
		this.rider = p;
	}
}
