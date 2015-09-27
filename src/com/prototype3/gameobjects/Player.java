package com.prototype3.gameobjects;

import org.newdawn.slick.*;
import com.prototype3.main.Game;

public class Player extends PhysicsObject {
	private static final int IDLE_ANIMATION_TIME = 5000;
	private static final float PLAYER_SPEED = 1f;
	private static final float PLAYER_MAX_SPEED = 15f;
	private SpriteSheet playerSprites;
	private Animation idleAnimation;
	private Image playerImage;
	private int idleTime;
	private boolean playIdleAnimation;

	public Player(int x, int y, int width, int height) throws SlickException {
		super(x, y, width, height);

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
	public void prePhysicsUpdate(int delta) throws SlickException {
		// Update speed first
		if (this.onGround && Game.isUpKeyDown)
			this.speedY -= 20f;
		
		if (Game.isLeftKeyDown) {
			if (this.speedX > -PLAYER_MAX_SPEED) {
				this.speedX -= PLAYER_SPEED;
			} else {
				this.speedX = -PLAYER_MAX_SPEED;
			}
		} else if (this.onGround && this.speedX < 0) {
			this.speedX += this.speedX < -PLAYER_SPEED ? PLAYER_SPEED : -this.speedX;
		}

		if (Game.isRightKeyDown) {
			if (this.speedX < PLAYER_MAX_SPEED) {
				this.speedX += PLAYER_SPEED;
			} else {
				this.speedX = PLAYER_MAX_SPEED;
			}
		} else if (this.onGround && this.speedX > 0) {
			this.speedX -= this.speedX > PLAYER_SPEED ? PLAYER_SPEED : this.speedX;
		}

		// Gravity TODO: speed limit!
		this.speedY += Game.GRAVITY;

		// Update newX and newY according to speed
		super.prePhysicsUpdate(delta);
	}

	public void afterPhysicsUpdate(int delta) throws SlickException {
		// Update everything first
		super.afterPhysicsUpdate(delta);

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
	}

	@Override
	public void render(Graphics g, int viewPortX, int viewPortY, int viewPortWidth, int viewPortHeight)
			throws SlickException {
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
}