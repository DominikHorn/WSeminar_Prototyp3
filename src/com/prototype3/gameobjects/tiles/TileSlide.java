package com.prototype3.gameobjects.tiles;

import java.util.ArrayList;

import org.newdawn.slick.*;

import com.prototype3.helper.LineSegment;

public class TileSlide extends Tile {
	private Image tileSprite;

	public TileSlide(int x, int y, int width, int height, TileSlideType tileSlideType) {
		super(x, y, width, height);
		
		switch (tileSlideType) {
		case TILE_SLIDE_FIRST:
			this.tileSprite = Tile.tileSpriteSheet.getSubImage(0, 0);
			break;
		case TILE_SLIDE_SECOND:
			this.tileSprite = Tile.tileSpriteSheet.getSubImage(0, 1);
			break;
		case TILE_SLIDE_THIRD:
			this.tileSprite = Tile.tileSpriteSheet.getSubImage(0, 2);
			break;
		default:
			System.err.println("Unknown tiletype");
			break;
		}
	}
	
	@Override
	public void render(Graphics g, int viewPortX, int viewPortY, int viewPortWidth, int viewPortHeight)
			throws SlickException {
		this.tileSprite.draw(this.x, this.y, this.width, this.height);
	}

	@Override
	public ArrayList<LineSegment> getOuterLineSegments() {
		System.err.println("UNIMPLEMENTED");
		System.exit(0);
		return null;
	}
}
