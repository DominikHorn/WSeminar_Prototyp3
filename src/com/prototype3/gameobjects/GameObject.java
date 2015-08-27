package com.prototype3.gameobjects;

import org.newdawn.slick.*;

public abstract class GameObject {
	// Position in space
	protected float x;
	protected float y;

	// new x and y after collision routine
	public float newX;
	public float newY;
	public float collisionWidth;
	public float collisionHeight;

	public GameObject() {
		// Initialize all attributes to default values
		this.x = 0;
		this.y = 0;
		this.newX = 0;
		this.newY = 0;
		this.collisionWidth = 1;
		this.collisionHeight = 1;
	}

	// Update newX and newY
	public abstract void update(int delta) throws SlickException;

	// Render GameObject
	public abstract void render(Graphics g) throws SlickException;

	// Method gets automatically called after collision routine (before render).
	// Makes sure to update the gameObjects position
	public void updatePosition() {
		this.x = this.newX;
		this.y = this.newY;
	}
}