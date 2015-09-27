package com.prototype3.gameobjects;

import org.newdawn.slick.*;

import com.prototype3.main.Game;

public class Blob extends PhysicsObject {
	private static final float BLOB_JUMP_THRUST = 17f;
	
	private SpriteSheet blobSprites;
	private Animation jumpingAnimation;
	private Image blobImage;

	public Blob(int x, int y, int width, int height) throws SlickException {
		super(x, y, width, height);

		this.blobSprites = new SpriteSheet(new Image("assets/img/Blob.png"), 128, 128);
		this.blobImage = this.blobSprites.getSubImage(0, 0);

		// Init jumping animation
		Image[] jumpAnimation = new Image[] { this.blobSprites.getSubImage(0, 0), this.blobSprites.getSubImage(1, 0),
				this.blobSprites.getSubImage(0, 1), this.blobSprites.getSubImage(1, 1) };

		int[] jumpAnimationDurations = new int[] { 50, 100, 100, 100 };

		this.jumpingAnimation = new Animation(jumpAnimation, jumpAnimationDurations, false);
		this.jumpingAnimation.setLooping(false);
	}

	@Override
	public void prePhysicsUpdate(int delta) throws SlickException {
		// Gravity TODO: speed limit!
		this.speedY += Game.GRAVITY;

		super.prePhysicsUpdate(delta);
	}

	@Override
	public void afterPhysicsUpdate(int delta) throws SlickException {
		super.afterPhysicsUpdate(delta);

		// Jump again if we are onGround and our animation has finished
		if (this.onGround && this.jumpingAnimation.isStopped())
			this.jumpingAnimation.restart();
		
		// As soon as the vertical thrust part of the animation plays jump
		if (this.jumpingAnimation.getFrame() == 2) {
			this.speedY = -BLOB_JUMP_THRUST;
		}
		
		// Constantly update our jump animation
		this.jumpingAnimation.update(delta);
	}

	@Override
	public void render(Graphics g, int viewPortX, int viewPortY, int viewPortWidth, int viewPortHeight)
			throws SlickException {
		if (this.jumpingAnimation.isStopped())
			// We are not jumping -> draw normal blob
			this.blobImage.draw(this.x, this.y, this.width, this.height);
		else
			// Jump animation
			this.jumpingAnimation.draw(this.x, this.y, this.width, this.height);
	}

}
