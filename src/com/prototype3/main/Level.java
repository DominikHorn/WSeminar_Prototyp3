package com.prototype3.main;

import java.util.ArrayList;

public class Level {
	public static final int TILE_WIDTH = 75;
	public static final int TILE_HEIGHT = 75;
	
	// Each level consists of n-number chunks (Vertical strips of tiles)
	private ArrayList<LevelChunk> chunks;
	
	public Level(String filePath) {
		this.chunks = new ArrayList<>();
		
		// TODO: load level from file
	}
}
