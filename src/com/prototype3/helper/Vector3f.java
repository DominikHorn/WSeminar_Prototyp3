package com.prototype3.helper;

public class Vector3f {
	public float x;
	public float y;
	public float z;

	public Vector3f(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public Vector3f multiply(float f) {
		this.x *= f;
		this.y *= f;
		this.z *= f;
		return this;
	}

	public Vector3f divide(float i) {
		this.x /= i;
		this.y /= i;
		this.z /= z;
		return this;
	}
	
	public Vector3f normalize() {
		return this.divide((float) Math.sqrt(this.x * this.x + this.y * this.y + this.z * this.z));
	}

	@Override
	public String toString() {
		return "(" + this.x + ", " + this.y + ", " + this.z + ")";
	}
}
