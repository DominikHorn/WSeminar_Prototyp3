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
		super(x, y, width * level.tileWidth, height * level.tileHeight);

		this.level = level;
		this.tiles = new Tile[width][height];
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
		if (viewPortX + viewPortWidth < this.x || this.x + this.width < viewPortX || viewPortY + viewPortHeight < this.y
				|| this.y + this.height < viewPortY) // We're not visible
			return;

		// Figure out which tiles are visible
		int visibleStartColumn = viewPortX <= this.x ? 0 : (viewPortX - this.x) / this.level.tileWidth;
		int visibleEndColumn = viewPortX + viewPortWidth >= this.x + this.width ? this.tiles.length
				: 1 + (viewPortX + viewPortWidth - this.x) / this.level.tileWidth;
		int visibleStartRow = viewPortY <= this.y ? 0 : (viewPortY - this.y) / this.level.tileHeight;
		int visibleEndRow = viewPortY + viewPortHeight >= this.y + this.height ? this.tiles[0].length
				: 1 + (viewPortY + viewPortHeight - this.y) / this.level.tileHeight;

		// Render each visible tile (determined by the viewPort Parameters)
		for (int x = visibleStartColumn; x < visibleEndColumn; x++) {
			for (int y = visibleStartRow; y < visibleEndRow; y++) {
				Tile tile = null;
				if ((tile = this.tiles[x][y]) != null)
					tile.render(g, viewPortX, viewPortY, viewPortWidth, viewPortHeight);
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

	/**
	 * Returns the tiles in the specified index - rectangle
	 * 
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @return
	 */
	public Tile[] getTiles(int x, int y, int width, int height) {
		// Loop through every tile and add to array
		Tile[] tiles = new Tile[(width - x) * (height - y)];
		int tilesIndex = 0;
		for (int j = y; j <= y + height; j++) {
			// Break if we reached the end of our chunk
			if (j >= this.tiles[0].length)
				break;

			for (int i = x; i <= x + width; i++) {
				// Break if we reached the end of our chunk
				if (i >= this.tiles.length)
					break;

				Tile tile = this.tiles[i][j];
				if (tile != null)
					tiles[tilesIndex++] = tile;
			}
		}

		return tiles;
	}
}
