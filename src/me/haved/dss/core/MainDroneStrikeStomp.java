package me.haved.dss.core;

import me.haved.engine.Engine2D;
import me.haved.engine.GameSettings;

public class MainDroneStrikeStomp
{
	private static Engine2D engine;
	
	public static void main(String[] args)
	{
		engine = new Engine2D(new GameDroneStrikeStomp(), new GameSettings());
		engine.startEngine();
	}
}