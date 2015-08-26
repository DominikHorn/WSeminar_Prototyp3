package com.prototype3.main;

import java.util.logging.*;

import org.newdawn.slick.*;

public class Game extends BasicGame {
	private static final int DISPLAY_WIDTH = 1280;
	private static final int DISPLAY_HEIGHT = 720;

	public Game(String gameName) {
		super(gameName);
	}

	@Override
	public void init(GameContainer container) throws SlickException {
		// Do nothing
	}

	@Override
	public void update(GameContainer container, int delta) throws SlickException {
		// Do nothing
	}

	@Override
	public void render(GameContainer container, Graphics g) throws SlickException {
		g.drawString("Howdy!", DISPLAY_WIDTH / 2, DISPLAY_HEIGHT / 2);
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