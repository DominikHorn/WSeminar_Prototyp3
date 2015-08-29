package com.prototype3.gameobjects;

import org.newdawn.slick.*;

/**
 * Super class for all objects in the game
 * 
 * @author Dominik
 */
public abstract class GameObject {
	// Update called before physics engine runs
	public abstract void prePhysicsUpdate(int delta) throws SlickException;

	// Update called after physics engine ran
	public abstract void afterPhysicsUpdate(int delta) throws SlickException;

	// Render GameObject
	public abstract void render(Graphics g, int viewPortX, int viewPortY, int viewPortWidth, int viewPortHeight) throws SlickException;
}