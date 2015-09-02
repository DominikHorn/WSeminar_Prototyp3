package com.prototype3.gameobjects;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

public class PhysicsObject extends GameObject {
	public int x;
	public int y;
	public int width;
	public int height;
	public int speedX;
	public int speedY;
	public boolean isStatic;

	public PhysicsObject(int x, int y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.isStatic = false; // Objects can by default be moved
	}

	@Override
	public void prePhysicsUpdate(int delta) throws SlickException {
		// Do nothing. Subclasses: F.e. update speed
	}

	@Override
	public void afterPhysicsUpdate(int delta) throws SlickException {
		// Move object to newX, newY
		this.x += this.speedX;
		this.y += this.speedY;
	}

	@Override
	public void render(Graphics g, int viewPortX, int viewPortY, int viewPortWidth, int viewPortHeight)
			throws SlickException {
		// Do nothing
	}
}
