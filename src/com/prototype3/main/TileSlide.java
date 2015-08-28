package com.prototype3.main;

import org.newdawn.slick.*;

public class TileSlide extends Tile {
	private Image tileSprite;

	public TileSlide(int x, int y, TileSlideType tileSlideType) {
		super(x, y);
		
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
	public void render(Graphics g) throws SlickException {
		this.tileSprite.draw(this.x, this.y);
	}

}
