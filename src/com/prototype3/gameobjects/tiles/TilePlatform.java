package com.prototype3.gameobjects.tiles;

import java.util.ArrayList;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import com.prototype3.helper.LineSegment;
import com.prototype3.main.Level;

public class TilePlatform extends Tile {
	private Image tileSprite;

	// Only compute this once => Store as attribute
	private ArrayList<LineSegment> outerLineSegments;

	public TilePlatform(int x, int y, int width, int height) {
		super(x, y, width, height);

		this.tileSprite = Tile.tileSpriteSheet.getSubImage(3, 0);
	}

	@Override
	public void render(Graphics g, int viewPortX, int viewPortY, int viewPortWidth, int viewPortHeight)
			throws SlickException {
		this.tileSprite.draw(this.x, this.y, this.width, this.height);
	}

	@Override
	public ArrayList<LineSegment> getOuterLineSegments() {
		if (this.outerLineSegments == null) {
			// TODO: optimize: move init code out of this method
			this.outerLineSegments = new ArrayList<>();

			// get neighbor tiles to calculate which face is collidable
			Tile[][] neighborTiles = Level.currentLevel.getNeighboringTiles(this);

			// Compute outer line segments
			if (neighborTiles[1][0] == null)
				// No tile above
				this.outerLineSegments.add(new LineSegment(x, y, x + width, y, this));

			if (neighborTiles[2][1] == null)
				// No tile to the right
				this.outerLineSegments.add(new LineSegment(x + width, y, x + width, y + height, this));

			if (neighborTiles[1][2] == null)
				// No tile bellow
				this.outerLineSegments.add(new LineSegment(x + width, y + height, x, y + height, this));

			if (neighborTiles[0][1] == null)
				// No tile to the left
				this.outerLineSegments.add(new LineSegment(x, y + height, x, y, this));
		}

		return this.outerLineSegments;
	}

}
