package com.prototype3.gameobjects.tiles;

import java.util.ArrayList;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import com.prototype3.helper.LineSegment;

public class TilePlatform extends Tile {
	private Image tileSprite;

	// Only compute this once => Store as attribute
	private ArrayList<LineSegment> outerLineSegments;

	public TilePlatform(int x, int y, int width, int height) {
		super(x, y, width, height);
		
		this.tileSprite = Tile.tileSpriteSheet.getSubImage(3, 0);
		this.outerLineSegments = new ArrayList<>();
		
		// Compute outer line segments
		this.outerLineSegments.add(new LineSegment(x, y, x + width, y, this));
		this.outerLineSegments.add(new LineSegment(x + width, y, x + width, y + height, this));
		this.outerLineSegments.add(new LineSegment(x + width, y + height, x, y + height, this));
		this.outerLineSegments.add(new LineSegment(x, y + height, x, y, this));
	}

	@Override
	public void render(Graphics g, int viewPortX, int viewPortY, int viewPortWidth, int viewPortHeight)
			throws SlickException {
		this.tileSprite.draw(this.x, this.y, this.width, this.height);
	}

	@Override
	public ArrayList<LineSegment> getOuterLineSegments() {
		return this.outerLineSegments;
	}

}
