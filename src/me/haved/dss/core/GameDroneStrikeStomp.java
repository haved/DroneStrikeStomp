package me.haved.dss.core;

import java.util.ArrayList;

import org.lwjgl.input.Keyboard;
import org.newdawn.slick.Color;
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
	
	private static final float HEART_SIZE = 32;
	private static final float HEART_SPACING = 4;
	private static float RADAR_WIDTH;
	private static final float RADAR_HEIGHT = 20;
	private static final float RADAR_Y_SPACING = 2;
	
	private static Texture background;
	private static Texture heart;
	
	public float worldWidth = 10000;
	
	public Player player;
	public ArrayList<Cloud> clouds;
	
	public void init()
	{
		initAssets();
		
		RADAR_WIDTH = RenderEngine.getCanvasWidth() - 12 - HEART_SPACING*2;
		
		player = new Player();
		clouds = new ArrayList<Cloud>();
		
		clouds.add(new Cloud(20, 400, 100));
		clouds.add(new Cloud(200, 600, 100));
		clouds.add(new Cloud(600, 880, 100));
	}
	
	private void initAssets()
	{
		background = DSSTextureLoader.loadTexture("bg.png");
		heart = DSSTextureLoader.loadTexture("heart.png");
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
		RenderEngine.pushMatrix();
		RenderEngine.translate(-scroll, 0, 0);
		renderWorld();
		RenderEngine.popMatrix();
		renderUI();
	}
	
	private void renderBG(float scroll)
	{
		float xShift = scroll / RenderEngine.getCanvasWidth();
		
		RenderEngine.resetColor();
		background.bind();
		RenderEngine.fillRectangleWithTexture(0, 0, RenderEngine.getCanvasWidth(), RenderEngine.getCanvasHeight(), xShift, 0, xShift + 1, 1);
	}
	
	private void renderWorld()
	{
		player.render();
		for(Cloud c:clouds)
			c.render();
	}
	
	private void renderUI()
	{
		RenderEngine.resetColor();
		heart.bind();
		for(int i = 0; i < player.getHealth(); i++)
		{
			RenderEngine.fillRectangleWithTexture(6 + i * HEART_SIZE + HEART_SPACING , RenderEngine.getCanvasHeight() - RADAR_HEIGHT - RADAR_Y_SPACING*2 - HEART_SIZE, HEART_SIZE, HEART_SIZE, 0, 0, 1, 1);
		}
		
		drawRadar();
	}
	
	private void drawRadar()
	{
		RenderEngine.pushMatrix();
		
		RenderEngine.translate((RenderEngine.getCanvasWidth() - RADAR_WIDTH) / 2, RenderEngine.getCanvasHeight() - RADAR_HEIGHT - RADAR_Y_SPACING*2, 0);
		
		RenderEngine.setColor(1, 1, 1, 0.5f);
		RenderEngine.fillRectangle(RADAR_WIDTH, RADAR_HEIGHT);
		
		RenderEngine.fillRectangle(player.getCameraScroll(this)/worldWidth*RADAR_WIDTH, -RADAR_Y_SPACING, RenderEngine.getCanvasWidth()/worldWidth*RADAR_WIDTH, RADAR_HEIGHT + RADAR_Y_SPACING*2);
		
		for(Cloud c:clouds)
			drawDotOnRadar(Color.blue, c.getCentreX(), c.getCentreY());
		
		drawDotOnRadar(Color.green, player.getCentreX(), player.getCentreY());
		
		RenderEngine.popMatrix();
	}
	
	private void drawDotOnRadar(Color c, float x, float y)
	{
		c.bind();
		RenderEngine.fillRectangle(x/worldWidth*RADAR_WIDTH-2, y/RenderEngine.getCanvasHeight()*RADAR_HEIGHT-2, 4, 4);
	}
}
