package com.prototype3.main;

import java.util.logging.*;

import org.newdawn.slick.*;

import com.prototype3.gameobjects.Player;

public class Game extends BasicGame {
	private static final int DISPLAY_WIDTH = 1280;
	private static final int DISPLAY_HEIGHT = 720;
	public static final float GRAVITY = 0.1f;

	// TODO: manage in a not so primitive fassion
//	private GameObject[] gameObjects;
//	private GameObject[] tiles;
	
	// Save player seperatly since he is a very special entity that must react to key input etc
	private Player player;

	public Game(String gameName) {
		super(gameName);
	}

	@Override
	public void init(GameContainer container) throws SlickException {
		container.setMinimumLogicUpdateInterval(16);
		container.setMaximumLogicUpdateInterval(17);
		container.setClearEachFrame(false);
		
		this.player = new Player(100, 100, 100, 200);
	}

	@Override
	public void update(GameContainer container, int delta) throws SlickException {
		// Update Gameobjects
		this.player.update(delta);
	
		// TODO: collision detection
		
		// Move objects
		this.player.updatePosition();
	}

	@Override
	public void render(GameContainer container, Graphics g) throws SlickException {
		// clear manually TODO: make camera / scroll proof
		g.setColor(new Color(0.5f, 0.5f, 0.5f));
		g.fillRect(0, 0, DISPLAY_WIDTH, DISPLAY_HEIGHT);
		g.setColor(Color.white);
		
		g.drawString("Howdy!", DISPLAY_WIDTH / 2, DISPLAY_HEIGHT / 2);
		this.player.render(g);
	}

	public static void main(String argv[]) {
		try {
			AppGameContainer appgc;
			appgc = new AppGameContainer(new Game("WSeminar-Protoype3"));
			appgc.setDisplayMode(DISPLAY_WIDTH, DISPLAY_HEIGHT, false);
			appgc.start();
		} catch (SlickException ex) {
			Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
}

/*
 * TODO: - Design game - implement game - implement ai + benchmark - implement
 * neural network + evolutionary algorithm - write essay
 *
 */