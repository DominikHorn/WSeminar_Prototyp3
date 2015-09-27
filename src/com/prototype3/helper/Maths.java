package com.prototype3.helper;

import com.prototype3.gameobjects.PhysicsObject;

public class Maths {
	/**
	 * Will cast a ray from rayFoot through rayTip at the Line "segment"
	 * (Starting at segmentFoot and ending at segmentTip)
	 * 
	 * @param rayFoot
	 *            Starting point of the ray
	 * @param rayTip
	 *            point describing the ray direction (rayDirection = rayTip -
	 *            rayFoot)
	 * @param segmentFoot
	 *            start point of the line segment
	 * @param segmentTip
	 *            end point of the line segment
	 * @return Vector3f: x = impact_x; y = impact_y; z = T1 (The smaller the T1
	 *         value, the closer the Impact point to rayFoot)
	 */
	public static Vector3f getRayImpactPoint(Vector2f rayFoot, Vector2f rayTip, Vector2f segmentFoot,
			Vector2f segmentTip) {
		// RAY in parametric: Point + Direction*T1
		float r_px = rayFoot.x;
		float r_py = rayFoot.y;
		float r_dx = rayTip.x - rayFoot.x;
		float r_dy = rayTip.y - rayFoot.y;
		// SEGMENT in parametric: Point + Direction*T2
		float s_px = segmentFoot.x;
		float s_py = segmentFoot.y;
		float s_dx = segmentTip.x - segmentFoot.x;
		float s_dy = segmentTip.y - segmentFoot.y;
		// Are they parallel? If so, no intersect
		float r_mag = (float) Math.sqrt(r_dx * r_dx + r_dy * r_dy);
		float s_mag = (float) Math.sqrt(s_dx * s_dx + s_dy * s_dy);
		if (r_dx / r_mag == s_dx / s_mag && r_dy / r_mag == s_dy / s_mag) {
			// Normalized vectors are equal => Parallel => Won't ever intersect
			// unless completely equal
			return null;
		}
		// SOLVE FOR T1 & T2
		// r_px+r_dx*T1 = s_px+s_dx*T2 && r_py+r_dy*T1 = s_py+s_dy*T2
		// ==> T1 = (s_px+s_dx*T2-r_px)/r_dx = (s_py+s_dy*T2-r_py)/r_dy
		// ==> s_px*r_dy + s_dx*T2*r_dy - r_px*r_dy = s_py*r_dx + s_dy*T2*r_dx -
		// r_py*r_dx
		// ==> T2 = (r_dx*(s_py-r_py) + r_dy*(r_px-s_px))/(s_dx*r_dy -
		// s_dy*r_dx)
		float T2 = (r_dx * (s_py - r_py) + r_dy * (r_px - s_px)) / (s_dx * r_dy - s_dy * r_dx);
		float T1 = (s_px + s_dx * T2 - r_px) / r_dx;
		// Must be within parametic whatevers for RAY/SEGMENT
		if (T1 < 0)
			return null;
		if (T2 < 0 || T2 > 1)
			return null;
		// Return the POINT OF INTERSECTION
		return new Vector3f(r_px + r_dx * T1, r_py + r_dy * T1, T1 * T1);
	}

	public static float distanceX(PhysicsObject object1, PhysicsObject object2) {
		return (float) Math.sqrt((object1.x - object2.x) * (object1.x - object2.x));
	}
}
