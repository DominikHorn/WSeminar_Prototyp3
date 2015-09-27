package com.prototype3.main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.*;

import org.newdawn.slick.*;

import com.prototype3.gameobjects.Blob;
import com.prototype3.gameobjects.PhysicsObject;
import com.prototype3.gameobjects.Player;
import com.prototype3.gameobjects.tiles.Tile;
import com.prototype3.helper.Rect;

public class Game extends BasicGame {
	// Gravity constant that applies to all entities
	public static final float GRAVITY = 2f;

	// Window width and height (GameWorld size)
	private static final int DISPLAY_WIDTH = 1280;
	private static final int DISPLAY_HEIGHT = 720;

	// The level on which we play
	private Level level;

	// Entities
	public ArrayList<PhysicsObject> entities;
	public ArrayList<Blob> blobs;

	// Optimisation (Save entities that are actually visible in first pass,
	// so that we don't have to go through them during collison or
	// afterPhysicsUpdate()
	ArrayList<PhysicsObject> visibleEntities;

	// Save player seperatly since he is a very special entity that must react
	// to key input etc
	private Player player;

	// Camera stuff
	private int cameraOriginX;
	private int cameraOriginY;

	// Key input table TODO: move to separate class
	public static boolean isLeftKeyDown;
	public static boolean isRightKeyDown;
	public static boolean isUpKeyDown;

	public Game(String gameName) {
		super(gameName);

		// Initialize java attributes
		this.blobs = new ArrayList<>();
		this.entities = new ArrayList<>();
		this.visibleEntities = new ArrayList<>();
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

		// Setup player
		this.player = new Player(this.level.playerSpawnX, this.level.playerSpawnY, 74, 149);

		// Setup entities
		for (PhysicsObject object : this.level.entities) {
			if (object instanceof Blob)
				this.blobs.add((Blob) object);

			this.entities.add(object);
		}
	}

	@Override
	public void update(GameContainer container, int delta) throws SlickException {
		// Input handling
		isLeftKeyDown = container.getInput().isKeyDown(Input.KEY_A) || container.getInput().isKeyDown(Input.KEY_LEFT);
		isRightKeyDown = container.getInput().isKeyDown(Input.KEY_D) || container.getInput().isKeyDown(Input.KEY_RIGHT);
		isUpKeyDown = container.getInput().isKeyDown(Input.KEY_SPACE) || container.getInput().isKeyDown(Input.KEY_W) || container.getInput().isKeyDown(Input.KEY_UP);
		
		// TODO: Put in different class
		if (container.getInput().isKeyDown(Input.KEY_F1)) {
			this.player.speedX = 0;
			this.player.speedY = 0;
			this.player.x = this.level.playerSpawnX;
			this.player.y = this.level.playerSpawnY;
		}

		// pre physics player + level
		this.player.prePhysicsUpdate(delta);
		this.level.prePhysicsUpdate(delta);

		// Handle Player + tile collision!
		Tile[] nearPlayerTiles = this.level.getTilesInsideAABB(this.player.x - 300, this.player.y - 300, 600, 600);
		for (Tile tile : nearPlayerTiles) {
			// No more valid tiles will follow
			if (tile == null)
				break;

			PhysicsEngine.resolveCollision(player, tile);
		}

		// after physics player + level
		this.player.afterPhysicsUpdate(delta);
		this.level.afterPhysicsUpdate(delta);

		// Update camera
		this.cameraOriginX = this.player.x - DISPLAY_WIDTH / 2 + this.player.width / 2;
		this.cameraOriginY = this.player.y - DISPLAY_HEIGHT / 2 + this.player.height / 2;

		// Update visible entities
		this.visibleEntities.clear();

		// pre physics entities
		for (PhysicsObject object : this.entities) {
			if (PhysicsEngine.areRectsIntersecting(new Rect(object.x, object.y, object.width, object.height),
					new Rect(cameraOriginX, cameraOriginY, DISPLAY_WIDTH, DISPLAY_HEIGHT))) {
				// Only update entities that are visible on screen
				object.prePhysicsUpdate(delta);

				// update Visible Entites
				this.visibleEntities.add(object);
			}
		}

		// Get tiles in visible area
		Tile[] visibleTiles = this.level.getTilesInsideAABB(cameraOriginX, cameraOriginY, DISPLAY_WIDTH,
				DISPLAY_HEIGHT);

		// Resolve visible entity + visible tile collision
		for (Tile tile : visibleTiles) {
			// No more valid tiles will follow
			if (tile == null)
				break;

			for (PhysicsObject visibleEntity : this.visibleEntities) {
				PhysicsEngine.resolveCollision(visibleEntity, tile);
			}
		}

		// after physics visible entities
		for (PhysicsObject object : this.visibleEntities) {
			object.afterPhysicsUpdate(delta);
		}
	}

	@Override
	public void render(GameContainer container, Graphics g) throws SlickException {
		// clear manually with custom color
		g.setColor(new Color(0.2f, 0.3f, 0.5f));
		g.fillRect(0, 0, DISPLAY_WIDTH, DISPLAY_HEIGHT);
		g.setColor(Color.white);

		// Do transformations
		g.pushTransform();
		g.translate(-this.cameraOriginX, -this.cameraOriginY);

		// Render Background first
		this.level.render(g, cameraOriginX, cameraOriginY, DISPLAY_WIDTH, DISPLAY_HEIGHT);

		// Render Entities
		for (PhysicsObject object : this.visibleEntities) {
			object.render(g, cameraOriginX, cameraOriginY, DISPLAY_WIDTH, DISPLAY_HEIGHT);
		}

		// Render player last
		this.player.render(g, cameraOriginX, cameraOriginY, DISPLAY_WIDTH, DISPLAY_HEIGHT);

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
			appgc.setAlwaysRender(true);
			appgc.start();
		} catch (SlickException ex) {
			Logger.getLogger(Game.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		}
	}
}