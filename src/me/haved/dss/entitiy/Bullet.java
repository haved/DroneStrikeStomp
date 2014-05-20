package me.haved.dss.entitiy;

import org.newdawn.slick.opengl.Texture;

import me.haved.dss.core.DSSTextureLoader;
import me.haved.dss.core.GameDroneStrikeStomp;
import me.haved.engine.RenderEngine;

public class Bullet extends Entity
{
	private static Texture sprite;
	
	public Bullet(float x, float y, float xSpeed, float ySpeed)
	{
		this.x = x;
		this.y = y;
		this.width = 8;
		this.height = 8;
		this.xSpeed = xSpeed;
		this.ySpeed = ySpeed;
	}
	
	public static void init()
	{
		sprite = DSSTextureLoader.loadTexture("bullet.png");
	}
	
	@Override
	public void update(GameDroneStrikeStomp game)
	{
		if(y<-height | x<-width | x>game.worldWidth | y>RenderEngine.getCanvasHeight())
			kill();
		
		super.move(game);
	}
	
	@Override
	public void render()
	{
		RenderEngine.resetColor();
		sprite.bind();
		RenderEngine.fillRectangleWithTexture(x, y, width, height, 0, 0, 1, 1);
	}
}
