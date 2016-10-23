package me.haved.dss.entitiy;

import me.haved.dss.core.DSSTextureLoader;
import me.haved.dss.core.GameDroneStrikeStomp;
import me.haved.engine.RenderEngine;
import me.haved.engine.Time;

import org.newdawn.slick.opengl.Texture;

public class ExplotionParticle extends Particle
{
	private static Texture defaultSprite;
	private Texture sprite = defaultSprite;
	
	private float size=0.5f;
	private float speed;
	private float delay;
	
	private boolean expanding = true;
	
	private float maxXSize, maxYSize;
	private float speedVal;
	public ExplotionParticle(float x, float y, float xSize, float ySize, float speed, float delay) {
		this.x = x;
		this.y = y;
		this.maxXSize = xSize;
		this.maxYSize = ySize;
		this.speedVal = speed;
		this.speed = speed;
		this.delay = delay;
	}
	
	@Override
	public void update(GameDroneStrikeStomp game)
	{
		if(delay > 0)
		{
			delay -= Time.delta();
			return;
		}
		if(expanding)
		{
			if(speed < 0)
				expanding = false;
			else if(size > 1)
				speed -= 10*speedVal*Time.delta();
		}
		else
			speed -= speedVal*Time.delta();
		
		size += speed*Time.delta();
		
		if(size < 0)
			kill();
	}
	
	@Override
	public void render()
	{
		sprite.bind();
		RenderEngine.fillRectangleWithTexture(x-size*maxXSize, y-size*maxYSize, 2*size*maxXSize, 2*size*maxYSize, 0, 0, 1, 1);
	}
	
	public static void init()
	{
		defaultSprite = DSSTextureLoader.loadTexture("explosion.png");
	}
}
