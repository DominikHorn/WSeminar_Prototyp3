package com.prototype3.main;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

import com.prototype3.gameobjects.*;
import com.prototype3.gameobjects.tiles.Tile;

/**
 * Chunk of tiles from level
 * 
 * @author Dominik
 *
 */
public class LevelChunk extends PhysicsObject {
	private Tile[][] tiles;
	private Level level; // TODO: add interface

	/**
	 * Creates new LevelChunk
	 * 
	 * @param level
	 *            level instance that this chunk belongs to
	 * @param x
	 *            origin x of the chunk
	 * @param y
	 *            origin y of the chunk
	 * @param width
	 *            number of tiles in x direction
	 * @param height
	 *            number of tiles in y direction
	 */
	public LevelChunk(Level level, int x, int y, int width, int height) {
		super(x, y, width, height);

		this.level = level;
		this.tiles = new Tile[this.width][this.height];
	}

	@Override
	public void prePhysicsUpdate(int delta) throws SlickException {
		// Do nothing (for now)
	}

	@Override
	public void afterPhysicsUpdate(int delta) throws SlickException {
		// Do nothing (for now)
	}

	@Override
	public void render(Graphics g, int viewPortX, int viewPortY, int viewPortWidth, int viewPortHeight)
			throws SlickException {
		// Determine visible tiles
		int visibleStartColumn = viewPortX <= this.x ? 0 : (viewPortX - this.x) / this.level.tileWidth;
		int visibleEndColumn = viewPortX + viewPortWidth >= this.x + this.width ? this.width
				: (viewPortX + viewPortWidth - this.x) / this.level.tileWidth;
		int visibleStartRow = viewPortY <= this.y ? 0 : (viewPortY - this.y) / this.level.tileHeight;
		int visibleEndRow = viewPortY + viewPortHeight >= this.y + this.height ? this.height
				: (viewPortY + viewPortHeight - this.y) / this.level.tileHeight;

//		System.out.println("Visible tiles from (" + visibleStartColumn + ", " + visibleStartRow + ") to ("
//				+ visibleEndColumn + ", " + visibleEndRow + ")");

		// Render each visible tile (determined by the viewPort Parameters)
		for (int x = visibleStartColumn; x < visibleEndColumn; x++) {
			for (int y = visibleStartRow; y < visibleEndRow; y++) {
				Tile tile = null;
				if ((tile = this.tiles[x][y]) != null) {
					tile.render(g, viewPortX, viewPortY, viewPortWidth, viewPortHeight);
				}
			}
		}
	}

	/**
	 * Set tile at array position x, y to a specified value
	 * 
	 * @param tile
	 *            tile to add
	 * @param x
	 *            array x
	 * @param y
	 *            array y
	 */
	public void setTile(Tile tile, int x, int y) {
		this.tiles[x][y] = tile;
	}
}
