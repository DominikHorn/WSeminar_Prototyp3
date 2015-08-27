package com.prototype3.gameobjects;

import org.newdawn.slick.*;
import com.prototype3.main.Game;

public class Player extends GameObject {
	private static final int IDLE_ANIMATION_TIME = 5000;
	private SpriteSheet playerSprites;
	private Animation idleAnimation;
	private Image playerImage;
	private float width;
	private float height;
	private int idleTime;
	private boolean playIdleAnimation;

	public float speedX;
	public float speedY;

	public Player(float x, float y, float width, float height) throws SlickException {
		super();

		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;

		this.speedX = 0;
		this.speedY = 0;
		this.idleTime = 0;
		this.playIdleAnimation = false;
		this.playerSprites = new SpriteSheet(new Image("assets/img/Player.png"), 128, 256);
		this.playerImage = this.playerSprites.getSubImage(0, 1);

		Image[] idleImages = new Image[] { this.playerSprites.getSubImage(0, 0), this.playerSprites.getSubImage(1, 0),
				this.playerSprites.getSubImage(2, 0), this.playerSprites.getSubImage(3, 0), };

		int[] idleImageDurations = new int[] { 750, 1000, 2000, 3000, };

		this.idleAnimation = new Animation(idleImages, idleImageDurations, false);
		this.idleAnimation.setLooping(false);
	}

	@Override
	public void update(int delta) throws SlickException {
		// Handle idle animation
		if (!this.playIdleAnimation && this.speedX == 0 && this.speedY == 0) {
			this.idleTime += delta;
		} else
			this.idleTime = 0;

		if (this.playIdleAnimation) {
			this.idleAnimation.update(delta);
			if (this.idleAnimation.isStopped())
				this.playIdleAnimation = false;
		}

		// Update speed
		this.speedY += Game.GRAVITY;

		// Move to new position
		this.newX += speedX;
		this.newY += speedY;
	}

	@Override
	public void render(Graphics g) throws SlickException {
		// Render idle animation after a certain timeout if we're not moving
		if (this.idleTime > IDLE_ANIMATION_TIME) {
			this.idleTime = 0;
			this.idleAnimation.restart();
			this.playIdleAnimation = true;
		}

		if (this.playIdleAnimation)
			this.idleAnimation.draw(this.x, this.y, this.width, this.height);
		else
			this.playerImage.draw(this.x, this.y, this.width, this.height);
	}

	/**
	 * Called when user presses keys that should move the player
	 * 
	 * @param direction
	 *            the direction in which the player should move: 0 - stop moving
	 *            1 - move left 2 - move right
	 */
	public void moveInput(int direction) {

	}
}