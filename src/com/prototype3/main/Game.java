package com.prototype3.main;

import java.util.logging.*;

import org.newdawn.slick.*;

import com.prototype3.gameobjects.Player;

public class Game extends BasicGame {
	private static final int DISPLAY_WIDTH = 1280;
	private static final int DISPLAY_HEIGHT = 720;
	public static final float GRAVITY = 10f;

	// Save player seperatly since he is a very special entity that must react
	// to key input etc
	private Player player;
	private int cameraOriginX;
	private int cameraOriginY;

	public Game(String gameName) {
		super(gameName);
	}

	@Override
	public void init(GameContainer container) throws SlickException {
		container.setMinimumLogicUpdateInterval(16);
		container.setMaximumLogicUpdateInterval(17);
		container.setClearEachFrame(false);

		this.player = new Player(100, 100, 75, 150);
	}

	@Override
	public void update(GameContainer container, int delta) throws SlickException {
		// pre physics engine
		this.player.prePhysicsUpdate(delta);

		// TODO: run physics engine

		// after physics engine
		this.player.afterPhysicsUpdate(delta);

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
		
		g.drawString("Howdy!", DISPLAY_WIDTH / 2, DISPLAY_HEIGHT / 2);
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
			appgc.start();
		} catch (SlickException ex) {
			Logger.getLogger(Game.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		}
	}
}