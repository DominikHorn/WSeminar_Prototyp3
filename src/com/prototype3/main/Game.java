package com.prototype3.main;

import java.io.IOException;
import java.util.logging.*;

import org.newdawn.slick.*;

import com.prototype3.gameobjects.Player;
import com.prototype3.gameobjects.tiles.Tile;

public class Game extends BasicGame {
	public static final float GRAVITY = 1f;

	private static final int DISPLAY_WIDTH = 1280;
	private static final int DISPLAY_HEIGHT = 720;

	private Level level;
	// Save player seperatly since he is a very special entity that must react
	// to key input etc
	private Player player;
	private int cameraOriginX;
	private int cameraOriginY;

	// Key input table TODO: move to separate class
	public static boolean isWKeyDown;
	public static boolean isAKeyDown;
	public static boolean isDKeyDown;
	public static boolean isSpaceKeyDown;

	public Game(String gameName) {
		super(gameName);
	}

	@Override
	public void init(GameContainer container) throws SlickException {
		container.setMinimumLogicUpdateInterval(16);
		container.setMaximumLogicUpdateInterval(17);
		container.setClearEachFrame(false);

		// Setup + load level
		try {
			this.level = new Level("assets/level/Level1");
		} catch (IOException e) {
			e.printStackTrace();
		}

		this.player = new Player(this.level.playerSpawnX, this.level.playerSpawnY, 75, 145);
	}

	@Override
	public void update(GameContainer container, int delta) throws SlickException {
		// Input handling
		isWKeyDown = container.getInput().isKeyDown(Input.KEY_W);
		isAKeyDown = container.getInput().isKeyDown(Input.KEY_A);
		isDKeyDown = container.getInput().isKeyDown(Input.KEY_D);
		isSpaceKeyDown = container.getInput().isKeyDown(Input.KEY_SPACE);

		// pre physics engine
		this.player.prePhysicsUpdate(delta);
		this.level.prePhysicsUpdate(delta);

		// Handle Player + tile collision!
		// TODO: fix lazy solution
		Tile[] tiles = this.level.getTilesInsideAABB(this.player.x - 300, this.player.y - 300, 600, 600);
		for (Tile tile : tiles) {
			// No more valid tiles will follow
			if (tile == null)
				break;

			PhysicsEngine.resolveCollision(player, tile);
		}

		// after physics engine
		this.player.afterPhysicsUpdate(delta);
		this.level.afterPhysicsUpdate(delta);

		// Update camera
		this.cameraOriginX = this.player.x - DISPLAY_WIDTH / 2 + this.player.width / 2;
		this.cameraOriginY = this.player.y - DISPLAY_HEIGHT / 2 + this.player.height / 2;
	}

	@Override
	public void render(GameContainer container, Graphics g) throws SlickException {
		// clear manually with custom color
		g.setColor(new Color(0.5f, 0.5f, 0.5f));
		g.fillRect(0, 0, DISPLAY_WIDTH, DISPLAY_HEIGHT);
		g.setColor(Color.white);

		// Do transformations
		g.pushTransform();
		g.translate(-this.cameraOriginX, -this.cameraOriginY);

		this.player.render(g, cameraOriginX, cameraOriginY, DISPLAY_WIDTH, DISPLAY_HEIGHT);
		this.level.render(g, cameraOriginX, cameraOriginY, DISPLAY_WIDTH, DISPLAY_HEIGHT);

		// Return to previous transform state
		g.popTransform();
	}

	public static void main(String argv[]) {
		/*
		 * Playground
		 */

		/*
		 * End Playground
		 */

		try {
			AppGameContainer appgc;
			appgc = new AppGameContainer(new Game("WSeminar-Protoype3"));
			appgc.setDisplayMode(DISPLAY_WIDTH, DISPLAY_HEIGHT, false);
			appgc.start();
		} catch (SlickException ex) {
			Logger.getLogger(Game.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		}
	}
}