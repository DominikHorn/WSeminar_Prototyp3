package com.prototype3.main;

import org.newdawn.slick.*;
import com.prototype3.gameobjects.PhysicsObject;

public class Tile extends PhysicsObject {
	public static final SpriteSheet tileSpriteSheet = loadTileSpriteSheet();

	private static final SpriteSheet loadTileSpriteSheet() {
		try {
			return new SpriteSheet(new Image("assets/img/Level_Tiles.png"), 128, 128);
		} catch (SlickException e) {
			System.err.println("Could not load Tile_SpriteSheet");
			e.printStackTrace();
			return null;
		}
	}

	public Tile(int x, int y) {
		super(x, y);
	}
}
