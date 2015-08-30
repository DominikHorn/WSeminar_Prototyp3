package com.prototype3.gameobjects.tiles;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class TilePlatform extends Tile {
	private Image tileSprite;
	
	public TilePlatform(int x, int y, int width, int height) {
		super(x, y, width, height);
		
		this.tileSprite = Tile.tileSpriteSheet.getSubImage(3, 0);
	}

	@Override
	public void render(Graphics g, int viewPortX, int viewPortY, int viewPortWidth, int viewPortHeight)
			throws SlickException {	
		this.tileSprite.draw(this.x, this.y, this.width, this.height);
	}
	
}
