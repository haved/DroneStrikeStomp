package me.haved.dss.core;

import java.util.ArrayList;

import org.lwjgl.input.Keyboard;

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
	
	Player player;
	ArrayList<Cloud> clouds;
	
	public void init()
	{
		initEntityAssets();
		
		player = new Player();
		clouds = new ArrayList<Cloud>();
		
		clouds.add(new Cloud(20, 400, 50));
	}
	
	private void initEntityAssets()
	{
		Player.init();
		Cloud.init();
	}
	
	public void update()
	{
		player.update(this);
		for(Cloud c:clouds)
			c.render();
	}
	
	public void render()
	{
		renderBG();
		renderWorld();
		renderUI();
	}
	
	private void renderBG()
	{
		RenderEngine.setColor(0.4f, 0.6f, 1f);
		RenderEngine.fillRectangle(0, 0, RenderEngine.getCanvasWidth(), RenderEngine.getCanvasHeight());
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
