package com.prototype3.gameobjects;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

public class PhysicsObject extends GameObject {
	public int x;
	public int y;
	public int width;
	public int height;
	public int newX;
	public int newY;
	public float speedX;
	public float speedY;

	// TODO: find solution for collision shape
	// TODO: squeeze width & height for rendering in there somewhere (is this
	// even necessary????)

	public PhysicsObject(int x, int y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}

	@Override
	public void prePhysicsUpdate(int delta) throws SlickException {
		// Update newX. Account for delta (delta is stored in milliseconds hence
		// the conversion)
		this.newX = this.x + (int) (this.speedX * (float) delta / 1000f);
		this.newY = this.y + (int) (this.speedY * (float) delta / 1000f);
	}

	@Override
	public void afterPhysicsUpdate(int delta) throws SlickException {
		// Update position
		this.x = this.newX;
		this.y = this.newY;
	}

	@Override
	public void render(Graphics g, int viewPortX, int viewPortY, int viewPortWidth, int viewPortHeight)
			throws SlickException {
		// Do nothing
	}
}
