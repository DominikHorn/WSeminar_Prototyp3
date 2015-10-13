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
import com.prototype3.helper.Vector2f;

public class Game extends BasicGame {
	// Gravity constant that applies to all entities
	public static final float GRAVITY = 2f;

	// Window width and height (GameWorld size)
	private static final int DISPLAY_WIDTH = 1920;
	private static final int DISPLAY_HEIGHT = 1080;

	// Entities
	public ArrayList<PhysicsObject> entities;
	public ArrayList<Blob> blobs;

	// Optimisation (Save entities that are actually visible in first pass,
	// so that we don't have to go through them during collison or
	// afterPhysicsUpdate()
	ArrayList<PhysicsObject> visibleEntities;

	// Save player seperatly since he is a very special entity that must react
	// to key input etc
	public static Player player;

	// Camera stuff
	private int cameraOriginX;
	private int cameraOriginY;

	// Key input table TODO: move to separate class
	public static boolean isLeftKeyDown;
	public static boolean isRightKeyDown;
	public static boolean isUpKeyDown;
	public static boolean isDuckKeyDown;
	
	public static boolean RAY_DEBUG_MODE_ENABLED = false;

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
			Level.currentLevel = new Level("assets/level/Level1");
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Setup player
		player = new Player(Level.currentLevel.playerSpawnX, Level.currentLevel.playerSpawnY, 74, 149);

		// Setup entities
		for (PhysicsObject object : Level.currentLevel.entities) {
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
		isUpKeyDown = container.getInput().isKeyDown(Input.KEY_SPACE) || container.getInput().isKeyDown(Input.KEY_W)
				|| container.getInput().isKeyDown(Input.KEY_UP);
		isDuckKeyDown = container.getInput().isKeyDown(Input.KEY_S) || container.getInput().isKeyDown(Input.KEY_DOWN);
		
		if (container.getInput().isKeyPressed(Input.KEY_F2))
			RAY_DEBUG_MODE_ENABLED = !RAY_DEBUG_MODE_ENABLED;
		
		// TODO: Put in different class
		if (container.getInput().isKeyDown(Input.KEY_F1)) {
			player.speedX = 0;
			player.speedY = 0;
			player.x = Level.currentLevel.playerSpawnX;
			player.y = Level.currentLevel.playerSpawnY;
		}

		// pre physics player + level
		player.prePhysicsUpdate(delta);
		Level.currentLevel.prePhysicsUpdate(delta);

		// Handle Player + tile collision!
		ArrayList<Tile> nearPlayerTiles = Level.currentLevel.getTilesInsideAABB(player.x - 300, player.y - 300, 600,
				600);
		for (Tile tile : nearPlayerTiles) {
			// No more valid tiles will follow
			if (tile == null)
				break;

			PhysicsEngine.resolveCollision(player, tile);
		}

		// after physics player + level
		player.afterPhysicsUpdate(delta);
		Level.currentLevel.afterPhysicsUpdate(delta);

		// Update camera
		this.cameraOriginX = player.x - DISPLAY_WIDTH / 2 + player.width / 2;
		this.cameraOriginY = player.y - DISPLAY_HEIGHT / 2 + player.height / 2;

		// Update visible entities
		this.visibleEntities.clear();

		// pre physics entities TODO: rework (Blob can get "stuck" while jumping if he leaves the screen -> hangs in air without ever coming back down
		for (PhysicsObject object : this.entities) {
			if (PhysicsEngine.areRectsIntersecting(new Rect(object.x, object.y, object.width, object.height),
					new Rect(cameraOriginX, 0, DISPLAY_WIDTH, cameraOriginY + DISPLAY_HEIGHT))) {
				// Only update entities that are visible on screen
				object.prePhysicsUpdate(delta);

				// update Visible Entites
				this.visibleEntities.add(object);
			}
		}

		// TODO: dirty quickest way to get working
		player.isStatic = true;
		
		
		// Resolve visible entity, visible entity & player collision
		for (int i = 0; i < this.visibleEntities.size(); i++) {
			PhysicsEngine.resolveCollision(player, this.visibleEntities.get(i));
		
			for (int j = 0; j < this.visibleEntities.size(); j++) {
				if (j != i)
					PhysicsEngine.resolveCollision(this.visibleEntities.get(i), this.visibleEntities.get(j));
			}
		}
		
		// TODO: dirty quickest way to get working
		player.isStatic = false;
		
		// Resolve visible entity + visible tile collision
		for (Tile tile : Level.currentLevel.visibleTiles) {
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
		g.setColor(new Color(0.0f, 0.0f, 0.0f));
		g.fillRect(0, 0, DISPLAY_WIDTH, DISPLAY_HEIGHT);
		g.setColor(Color.white);

		// Do transformations
		g.pushTransform();
		g.translate(-this.cameraOriginX, -this.cameraOriginY);

		// Render Background first
		Level.currentLevel.render(g,
				new Vector2f(player.x + player.width / 2, player.y + player.height / 8),
				cameraOriginX, cameraOriginY, DISPLAY_WIDTH, DISPLAY_HEIGHT);

		// Render Entities
		for (PhysicsObject object : this.visibleEntities) {
			object.render(g, cameraOriginX, cameraOriginY, DISPLAY_WIDTH, DISPLAY_HEIGHT);
		}

		// Render player last
		player.render(g, cameraOriginX, cameraOriginY, DISPLAY_WIDTH, DISPLAY_HEIGHT);

		// Return to previous transform state
		g.popTransform();
	}

	public static void main(String argv[]) {
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