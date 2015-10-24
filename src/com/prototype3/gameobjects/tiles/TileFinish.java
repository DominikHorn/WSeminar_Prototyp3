package com.prototype3.gameobjects.tiles;

import java.util.ArrayList;

import com.prototype3.helper.LineSegment;

public class TileFinish extends Tile {

	public TileFinish(int x, int y, int width, int height) {
		super(x, y, width, height);
	}

	@Override
	public ArrayList<LineSegment> getOuterLineSegments() {
		return new ArrayList<LineSegment>();
	}

}
