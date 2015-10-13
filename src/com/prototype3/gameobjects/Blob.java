package com.prototype3.gameobjects;

import org.newdawn.slick.*;

import com.prototype3.helper.Maths;
import com.prototype3.helper.Vector2f;
import com.prototype3.main.Game;
import com.prototype3.main.Level;

public class Blob extends PhysicsObject {
	private static final float BLOB_JUMP_THRUST = 30f;

	private SpriteSheet blobSprites;
	private Animation jumpingAnimation;
	private Image blobImage;

	/** AI */
	private int aiTimer = 0;
	private boolean playerVisible = false;

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

		if (this.onGround)
			this.speedX = 0;

		// Player visible -> Try jumping towards his position
		if (this.playerVisible)
			if (Maths.distanceX(Game.player, this) > 15f)
				this.speedX = Game.player.x < this.x ? -10f : 10f;
			else {
				this.speedX = 0;
				this.x = Game.player.x;
			}

		super.prePhysicsUpdate(delta);
		
		System.out.println("LoL");
	}

	@Override
	public void afterPhysicsUpdate(int delta) throws SlickException {
		super.afterPhysicsUpdate(delta);

		// TODO: only jump when you see player

		// Jump again if we are onGround and our animation has finished
		if (this.onGround && this.jumpingAnimation.isStopped())
			this.jumpingAnimation.restart();

		// As soon as the vertical thrust part of the animation plays jump
		if (this.jumpingAnimation.getFrame() == 2) {
			this.speedY = -BLOB_JUMP_THRUST;
		}

		// Can we see the player? Ask every 2 ticks
		if (aiTimer++ % 2 == 0) {
			playerVisible = false;
			if (Level.currentLevel.isVisible(Game.player, new Vector2f(this.x + this.width / 2, this.y + this.height / 2)))
				playerVisible = true;
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
