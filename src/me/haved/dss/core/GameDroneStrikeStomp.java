package me.haved.dss.core;

import java.util.ArrayList;

import org.lwjgl.input.Keyboard;
import org.newdawn.slick.opengl.Texture;

import me.haved.dss.entitiy.Cloud;
import me.haved.dss.entitiy.Player;
import me.haved.engine.Game;
import me.haved.engine.RenderEngine;

public class GameDroneStrikeStomp extends Game
{
	public static final int KEY_CODE_LEFT = Keyboard.KEY_LEFT;
	public static final int KEY_CODE_RIGHT = Keyboard.KEY_RIGHT;
	public static final int KEY_CODE_UP = Keyboard.KEY_UP;
	public static final int KEY_CODE_DOWN = Keyboard.KEY_DOWN;
	public static final int KEY_CODE_JUMP = Keyboard.KEY_Z;
	public static final int KEY_CODE_SHOOT = Keyboard.KEY_X;
	
	private static Texture background;
	
	public float worldWidth = 10000;
	
	public Player player;
	public ArrayList<Cloud> clouds;
	
	public void init()
	{
		initAssets();
		
		player = new Player();
		clouds = new ArrayList<Cloud>();
		
		clouds.add(new Cloud(20, 400, 100));
		clouds.add(new Cloud(200, 600, 100));
		clouds.add(new Cloud(600, 880, 100));
	}
	
	private void initAssets()
	{
		background = DSSTextureLoader.loadTexture("bg.png");
		Player.init();
		Cloud.init();
	}
	
	public void update()
	{
		player.update(this);
		for(Cloud c:clouds)
			c.update(this);
	}
	
	public void render()
	{
		float scroll = player.getCameraScroll(this);
		renderBG(scroll);
		renderWorld();
		renderUI();
	}
	
	private void renderBG(float scroll)
	{
		RenderEngine.resetColor();
		background.bind();
		RenderEngine.fillRectangleWithTexture(0, 0, RenderEngine.getCanvasWidth(), RenderEngine.getCanvasHeight(), 0, 0, 1, 1);
	}
	
	private void renderWorld()
	{
		player.render();
		for(Cloud c:clouds)
			c.render();
	}
	
	private void renderUI()
	{
		
	}
}
