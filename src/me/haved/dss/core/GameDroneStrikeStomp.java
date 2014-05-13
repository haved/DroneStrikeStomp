package me.haved.dss.core;

import org.lwjgl.input.Keyboard;

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
	
	Player player;
	
	public void init()
	{
		initEntityAssets();
		
		player = new Player();
	}
	
	private void initEntityAssets()
	{
		Player.init();
	}
	
	public void update()
	{
		player.update(this);
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
	}
	
	private void renderUI()
	{
		
	}
}
