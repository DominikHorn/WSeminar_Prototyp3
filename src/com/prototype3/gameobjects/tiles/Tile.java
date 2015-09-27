package com.prototype3.gameobjects.tiles;

import java.util.ArrayList;

import org.newdawn.slick.*;
import com.prototype3.gameobjects.PhysicsObject;
import com.prototype3.helper.LineSegment;

public abstract class Tile extends PhysicsObject {
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

	private Image sprite;

	public Tile(int x, int y, int width, int height) {
		super(x, y, width, height);

		this.sprite = tileSpriteSheet.getSubImage(3, 3);
		this.isStatic = true; // Tiles can generally not be moved
	}

	@Override
	public void render(Graphics g, int viewPortX, int viewPortY, int viewPortWidth, int viewPortHeight)
			throws SlickException {
		this.sprite.draw(this.x, this.y, this.width, this.height);
	}
	
	public abstract ArrayList<LineSegment> getOuterLineSegments();

	@Override
	public String toString() {
		return "(" + this.x + ", " + this.y + ", " + this.width + ", " + this.height + ")";
	}
}
