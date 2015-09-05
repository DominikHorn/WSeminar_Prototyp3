package com.prototype3.helper;

public class Vector {
	public int x;
	public int y;

	public Vector(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public Vector multiply(int f) {
		this.x *= f;
		this.y *= f;
		return this;
	}

	public Vector divide(int i) {
		this.x /= i;
		this.y /= i;
		return this;
	}

	@Override
	public String toString() {
		return "(" + this.x + ", " + this.y + ")";
	}
}
