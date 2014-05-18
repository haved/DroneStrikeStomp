package me.haved.dss.core;

import java.util.ArrayList;

import org.lwjgl.input.Keyboard;
import org.newdawn.slick.Color;
import org.newdawn.slick.opengl.Texture;

import me.haved.dss.entitiy.Cloud;
import me.haved.dss.entitiy.Drone;
import me.haved.dss.entitiy.Entity;
import me.haved.dss.entitiy.Pickup;
import me.haved.dss.entitiy.Player;
import me.haved.engine.Game;
import me.haved.engine.RenderEngine;
import me.haved.engine.Time;
import me.haved.engine.Util;

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
	
	private float cloudTimer;
	private float prevCloudLoc;
	private float pickupTimer;
	private float droneTimer = 5;
	private float prevDroneLoc;
	
	private static Texture background;
	private static Texture heart;
	
	public float worldWidth = 5000;
	
	public Player player;
	public ArrayList<Cloud> clouds;
	public ArrayList<Pickup> pickups;
	public ArrayList<Drone> drones;
	
	public void init()
	{
		initAssets();
		
		RADAR_WIDTH = RenderEngine.getCanvasWidth() - 12 - HEART_SPACING*2;
		
		player = new Player(100, -64);
		clouds = new ArrayList<Cloud>();
		pickups = new ArrayList<Pickup>();
		drones = new ArrayList<Drone>();
		
		makeInitialClouds();
	}
	
	private void initAssets()
	{
		background = DSSTextureLoader.loadTexture("bg.png");
		heart = DSSTextureLoader.loadTexture("heart.png");
		Player.init();
		Cloud.init();
		Pickup.init();
		Drone.init();
	}
	
	public void update()
	{
		player.update(this);
		
		makeClouds();
		makePickups();
		makeDrones();
		
		updateEntityList(clouds);
		updateEntityList(pickups);
		updateEntityList(drones);
		
		cleanEntityList(clouds);
		cleanEntityList(pickups);
		cleanEntityList(drones);
	}
	
	private void makeInitialClouds()
	{
		int timer = 100;
		
		for(int i = 0; i < worldWidth; i++)
		{
			timer--;
			
			if(timer <= 0)
			{
				float y = 200+Util.randomFloat(600);
				
				if(y < prevCloudLoc)
					y-=100;
				
				if(y > prevCloudLoc)
					y+=100;
				
				prevCloudLoc = y;
				
				clouds.add(new Cloud(i, y, 90 + Util.randomFloat(20)));
				timer = 80 + Util.randomInt(120);
			}
		}
	}
	
	private void makeClouds()
	{
		cloudTimer -= Time.delta();
		
		if(cloudTimer <= 0)
		{
			float y = 200+Util.randomFloat(600);
			
			if(y < prevCloudLoc)
				y-=100;
			
			if(y > prevCloudLoc)
				y+=100;
			
			prevCloudLoc = y;
			
			clouds.add(new Cloud(-128, y, 90 + Util.randomFloat(20)));
			cloudTimer = .5f + Util.randomFloat(1.5f);
		}
	}
	
	private void makePickups()
	{
		pickupTimer -= Time.delta();
		
		if(pickupTimer <= 0)
		{
			pickups.add(new Pickup(Pickup.HEALTH_PICKUP, 100 + Util.randomFloat(worldWidth - 200), -32));
			//pickupTimer = 2+Util.randomFloat(3);
		}
	}
	
	private void makeDrones()
	{
		droneTimer -= Time.delta();
		
		if(droneTimer <= 0)
		{
			float y = 200+Util.randomFloat(600);
			
			if(y < prevDroneLoc)
				y-=100;
			
			if(y > prevDroneLoc)
				y+=100;
			
			prevDroneLoc = y;
			
			drones.add(new Drone(-128, y, 140 + Util.randomFloat(20)));
			droneTimer = 2 + Util.randomFloat(2.5f);
		}
	}
	
	private void updateEntityList(ArrayList<? extends Entity> eList)
	{
		for(Entity e:eList)
			e.update(this);
	}
	
	private void cleanEntityList(ArrayList<? extends Entity> eList)
	{
		for(int i = 0; i < eList.size(); i++)
		{
			if(eList.get(i).isDead())
			{
				eList.remove(i);
				i--;
			}
		}
	}
	
	private void renderEntityList(ArrayList<? extends Entity> eList)
	{
		for(Entity e:eList)
			e.render();
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
		renderEntityList(clouds);
		renderEntityList(pickups);
		renderEntityList(drones);
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
		
		for(Entity o:clouds)
			drawDotOnRadar(Color.blue, o.getCentreX(), o.getCentreY());
		
		for(Entity o:pickups)
			drawDotOnRadar(Color.yellow, o.getCentreX(), o.getCentreY());
		
		for(Entity o:drones)
			drawDotOnRadar(Color.red, o.getCentreX(), o.getCentreY());
		
		drawDotOnRadar(Color.green, player.getCentreX(), player.getCentreY());
		
		RenderEngine.popMatrix();
	}
	
	private void drawDotOnRadar(Color c, float x, float y)
	{
		c.bind();
		RenderEngine.fillRectangle(x/worldWidth*RADAR_WIDTH-2, y/RenderEngine.getCanvasHeight()*RADAR_HEIGHT-2, 4, 4);
	}
}
