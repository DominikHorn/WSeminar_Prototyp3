package com.prototype3.helper;

public class Vector2f {
	public float x;
	public float y;
	
	public Vector2f(float x, float y) {
		this.x = x;
		this.y = y;
	}
	
	public Vector2f multiply(float f) {
		this.x *= f;
		this.y *= f;
		return this;
	}

	public Vector2f divide(float i) {
		this.x /= i;
		this.y /= i;
		return this;
	}
	
	public Vector2f normalize() {
		return this.divide((float)Math.sqrt(this.x * this.x + this.y * this.y));
	}

	@Override
	public String toString() {
		return "(" + this.x + ", " + this.y + ")";
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Vector2f) {
			Vector2f vec = (Vector2f) obj;
			if (vec.x == this.x && vec.y == this.y)
				return true;
		}
		
		return false;
	}
}
