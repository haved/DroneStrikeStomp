package me.haved.dss.core;

import java.io.FileInputStream;

import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

public class DSSTextureLoader
{
	public static Texture loadTexture(String path)
	{
		try
		{
			return TextureLoader.getTexture("PNG", new FileInputStream("bin/textures/" + path));
		}
		catch(Exception e)
		{
			System.err.println("Failed to load texture: " + "bin/textures/" + path);
			e.printStackTrace();
			System.exit(1);
		}
		
		return null;
	}
}
