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

	/**
	 * Creates new LevelChunk
	 * @param x origin x of the chunk
	 * @param y origin y of the chunk
	 * @param width number of tiles in x direction
	 * @param height number of tiles in y direction
	 */
	public LevelChunk(int x, int y, int width, int height) {
		super(x, y, width, height);

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
		int visibleStartColumn = viewPortX <= this.x ? 0 : (viewPortX - this.x) / Level.TILE_WIDTH;
		int visibleEndColumn = viewPortX + viewPortWidth >= this.x + this.width ? this.width : (viewPortX + viewPortWidth - this.x) / Level.TILE_WIDTH;
		int visibleStartRow = viewPortY <= this.y ? 0 : (viewPortY - this.y) / Level.TILE_HEIGHT;
		int visibleEndRow = viewPortY + viewPortHeight >= this.y + this.height ? this.height : (viewPortY + viewPortHeight - this.y) / Level.TILE_HEIGHT;
		
		System.out.println("Visible tiles from (" + visibleStartColumn + ", " + visibleStartRow + ") to (" + visibleEndColumn + ", " + visibleEndRow + ")");
		
		// Render each visible tile (determined by the viewPort Parameters)
		for (int x = visibleStartColumn; x < visibleEndColumn; x++) {
			for (int y = visibleStartRow; y < visibleEndRow; y++) {
				this.tiles[x][y].render(g, viewPortX, viewPortY, viewPortWidth, viewPortHeight);
			}
		}
	}
}
