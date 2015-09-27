package com.prototype3.helper;

import com.prototype3.gameobjects.tiles.Tile;

public class LineSegment {
	public Vector2f foot;
	public Vector2f tip;
	public Tile tile;

	public LineSegment(float footX, float footY, float tipX, float tipY, Tile tile) {
		this.foot = new Vector2f(footX, footY);
		this.tip = new Vector2f(tipX, tipY);
		this.tile = tile;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof LineSegment) {
			LineSegment line = (LineSegment) obj;
			if ((line.foot.equals(this.foot) && line.tip.equals(this.tip)) || (line.foot.equals(this.tip) && line.tip.equals(this.foot)))
				return true;
		}

		return false;
	}
	
	@Override
	public String toString() {
		return "[" + this.foot + ", " + this.tip + "]";
	}
}
