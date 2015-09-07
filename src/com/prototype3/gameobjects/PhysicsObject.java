package com.prototype3.gameobjects;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

public class PhysicsObject extends GameObject {
	public int x;
	public int y;
	public int width;
	public int height;
	public float speedX;
	public float speedY;
	public boolean isStatic;
	public boolean onGround;
	public boolean usesSimpleCollision;

	public PhysicsObject(int x, int y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.isStatic = false; // Objects can by default be moved
		this.onGround = false;
		this.usesSimpleCollision = true;
	}

	@Override
	public void prePhysicsUpdate(int delta) throws SlickException {
		// Subclasses: F.e. update speed
		this.onGround = false;	// Reset OnGround
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
