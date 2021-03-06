package me.haved.dss.entitiy;

import org.lwjgl.input.Keyboard;
import org.newdawn.slick.opengl.Texture;

import me.haved.dss.core.DSSTextureLoader;
import me.haved.dss.core.GameDroneStrikeStomp;
import me.haved.engine.RenderEngine;
import me.haved.engine.Time;

public class Player extends Entity
{
	private static Texture sprite;
	private static Texture indicatorSprite;
	
	private float delay;
	
	private float animation = 0;
	private boolean facingRight = false;
	
	private float airFriction = 4f;
	private float groundFriction = 10f;
	private float speed = 7;
	private float maxSpeed = 500;
	private float jump = 1000;
	
	private boolean onGround;
	private float groundSpeed;
	
	private float droneRotSpeed = 150;
	private float droneSpeedChangeSpeed = 2000;
	private Drone ride;
	
	private int maxHealth = 8;
	private int health = 6;
	
	public Player(float x, float y)
	{
		delay = 5;
		
		this.x = x;
		this.y = y;
		width = 32;
		height = 64;
	}
	
	public static void init()
	{
		sprite = DSSTextureLoader.loadTexture("player.png");
		indicatorSprite = DSSTextureLoader.loadTexture("playerIndicator.png");
	}
	
	public void update(GameDroneStrikeStomp game)
	{
		if(isDead())
			return;
		if(ride != null && ride.isDead())
		{
			jumpOut();
		}
		if(ride == null)
		{
			normalUpdate(game);
		}
		else
		{
			droneRideUpdate(game);
		}
	}
	
	private void normalUpdate(GameDroneStrikeStomp game)
	{
		ySpeed += GRAVITY * Time.delta();
		
		xSpeed -= (xSpeed-groundSpeed) * getFriction() * Time.delta();
		
		if(delay>0)
		{
			delay -= Time.delta();
			ySpeed = 0;
		}
		
		input(game);
		move(game);
		
		checkProjectilesAndPickups(game);
		
		if(getY2() > RenderEngine.getCanvasHeight()-6 & ySpeed > 0)
		{
			onGround = true;
			y = RenderEngine.getCanvasHeight() - height-6;
			ySpeed = 0;
		}
		if(getX2() > game.worldWidth-64 & xSpeed > 0)
		{
			x = game.worldWidth - width-64;
			xSpeed = 0;
		}

		updateAnimation();
	}
	
	private void droneRideUpdate(GameDroneStrikeStomp game)
	{
		this.x = ride.getCentreX()-width/2;
		this.y = ride.getCentreY()-width/2;
		
		droneInput(game);
	}
	
	private float getFriction()
	{
		return onGround ? groundFriction : airFriction;
	}
	
	private void input(GameDroneStrikeStomp game)
	{
		if(Keyboard.isKeyDown(GameDroneStrikeStomp.KEY_CODE_LEFT))
		{
			xSpeed -= speed * 1000 * Time.delta();
		}
		
		if(Keyboard.isKeyDown(GameDroneStrikeStomp.KEY_CODE_RIGHT))
		{
			xSpeed += speed * 1000 * Time.delta();
		}
		
		if(Keyboard.isKeyDown(GameDroneStrikeStomp.KEY_CODE_JUMP) & onGround)
			if(ySpeed > -jump)
				ySpeed = -jump;
		
		if(Keyboard.isKeyDown(GameDroneStrikeStomp.KEY_CODE_SHOOT) & onGround)
			tryBoarding(game);
		
		xSpeed = Math.max(-maxSpeed, Math.min(maxSpeed, xSpeed));
	}
	
	private void droneInput(GameDroneStrikeStomp game)
	{	
		if(Keyboard.isKeyDown(GameDroneStrikeStomp.KEY_CODE_DRONE_LEFT))
			ride.rotation -= Time.delta() * droneRotSpeed;
		
		if(Keyboard.isKeyDown(GameDroneStrikeStomp.KEY_CODE_DRONE_RIGHT))
			ride.rotation += Time.delta() * droneRotSpeed;
		
		if(Keyboard.isKeyDown(GameDroneStrikeStomp.KEY_CODE_DRONE_SPEED_UP))
			ride.speedChange(droneSpeedChangeSpeed*Time.delta());
		
		if(Keyboard.isKeyDown(GameDroneStrikeStomp.KEY_CODE_DRONE_SPEED_DOWN))
			ride.speedChange(-droneSpeedChangeSpeed*Time.delta());
		
		if(Keyboard.isKeyDown(GameDroneStrikeStomp.KEY_CODE_JUMP))
			jumpOut();
	}
	
	@Override
	public void move(GameDroneStrikeStomp game)
	{
		float newX = x + xSpeed * Time.delta();
		float newX2 = newX + width;
		float newY = y + ySpeed * Time.delta();
		float newY2 = newY + height;
		float x2 = x + width;
		float y2 = y + height;
		
		groundSpeed = 0;
		onGround = false;
		
		for(Collider c:game.clouds)
			checkCollider(c, x2, y2, newX, newY, newX2, newY2);
		
		for(Collider c:game.drones)
			checkCollider(c, x2, y2, newX, newY, newX2, newY2);
		
		super.move(game);
	}
	
	private void tryBoarding(GameDroneStrikeStomp game)
	{
		for(Drone d:game.drones)
		{
			if(getY2()+4>d.getY() & getY2()-4<d.getY() & getX()+4<d.getX2() & getX2()-4>d.getX())
				 setRide(d);
		}
	}
	
	private void jumpOut()
	{
		x = ride.getCentreX()-width/2;
		y = ride.getCentreY()-height/2;
		ySpeed = -jump*1.5f;
		xSpeed = jump*1.5f*(float)Math.sin(Math.toRadians(ride.rotation));
		x+=xSpeed*0.05f;
		y+=ySpeed*0.05f;
		leaveRide();
	}
	
	private void checkCollider(Collider c, float x2, float y2, float newX, float newY, float newX2, float newY2)
	{
		if(!(c instanceof Cloud))
			if(xSpeed < c.getXSpeed())
			{
				if(isBlockedLeft(c, newX, newX2, y2))
				{
					x = c.getX2();
					xSpeed = 0;
				}
			}
			else if(xSpeed > c.getXSpeed())
			{
				if(isBlockedRight(c, newX, newX2, y2))
				{
					x = c.getX() - width;
					xSpeed = 0;
				}
			}
		
		if(ySpeed < c.getYSpeed() & c instanceof Cloud != true)
		{
			if(isBlockedUp(c, newY, newY2, x2))
			{
				y = c.getY2();
				ySpeed = 0;
			}
		}
		else if(ySpeed > c.getYSpeed())
		{
			if(isBlockedDown(c, newY, newY2, x2))
			{
				groundSpeed = c.getXSpeed();
				onGround = true;
				y = c.getY() - height;
				ySpeed = 0;
			}
		}
	}
	
	private boolean isBlockedLeft(Collider c, float newX, float newX2, float y2)
	{
		return newX < c.getX2() & newX > c.getX() & y < c.getY2() & y2 > c.getY();
	}
	
	private boolean isBlockedRight(Collider c, float newX, float newX2, float y2)
	{
		return newX2 > c.getX() & newX2 < c.getX2() & y < c.getY2() & y2 > c.getY();
	}
	
	private boolean isBlockedUp(Collider c, float newY, float newY2, float x2)
	{
		return newY < c.getY2() & newY > c.getY() & x < c.getX2() & x2 > c.getX();
	}
	
	private boolean isBlockedDown(Collider c, float newY, float newY2, float x2)
	{
		return newY2 > c.getY() & newY2 < c.getY2() & x < c.getX2() & x2 > c.getX();
	}
	
	private void checkProjectilesAndPickups(GameDroneStrikeStomp game)
	{
		for(Pickup p:game.pickups)
		{
			if(isEntityInsidePlayer(p))
				p.pickedUp(this);
		}
		
		for(Bullet b:game.bullets)
		{
			if(isEntityInsidePlayer(b))
			{
				damage(b.getDamage());
				b.kill();
			}
		}
	}
	
	private boolean isEntityInsidePlayer(Entity e)
	{
		return e.getCentreX() > getX() & e.getCentreX() < getX2() & e.getCentreY() > getY() & e.getCentreY() < getY2();
	}
	
	private void updateAnimation()
	{
		animation += Math.abs(xSpeed-groundSpeed)*Time.delta();
		animation %= 140;
		
		if(xSpeed-groundSpeed > 0.2f)
			facingRight = true;
		if(xSpeed-groundSpeed < -0.2f)
			facingRight = false;
	}
	
	public void render()
	{
		if(isDead())
			return;
		RenderEngine.resetColor();
		
		if(ride != null)
		{
			indicatorSprite.bind();
			RenderEngine.fillRectangleWithTexture(x, y, width, width, 0, 0, 1, 1);
		}
		else
		{
			if(getY2()>0)
			{
				sprite.bind();
				float i = animation < 70 ? 0 : 0.5f;
				float faceShift = facingRight ? 0.25f : -0.25f;
				RenderEngine.fillRectangleWithTexture(x, y, width, height, i+(0.25f-faceShift), 0, i+(0.25f+faceShift), 1);
			}
			else
			{
				indicatorSprite.bind();
				float size = (1200+y)/1200*width;
				RenderEngine.fillRectangleWithTexture((x+width/2)-size/2, 0, size, size, 0, 0, 1, 1);
			}
		}
	}

	public float getCameraScroll(GameDroneStrikeStomp game)
	{
		return Math.max(0, Math.min(game.worldWidth-RenderEngine.getCanvasWidth(), x - RenderEngine.getCanvasWidth() / 2));
	}
	
	public void leaveRide()
	{
		ride.setRider(null);
		ride = null;
	}
	
	public void setRide(Drone d)
	{
		ride = d;
		d.setRider(this);
	}
	
	public void damage(int amount)
	{
		health -= amount;
		if(health <= 0)
		{
			kill();
		}
	}
	
	public void heal(int amount)
	{
		health += amount;
		health = Math.min(maxHealth, health);
	}
	
	public int getHealth()
	{
		return health;
	}
}
