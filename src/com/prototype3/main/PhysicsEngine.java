package com.prototype3.main;

import com.prototype3.gameobjects.PhysicsObject;
import com.prototype3.helper.*;

public class PhysicsEngine {

	/**
	 * Check for collision and if collision is found resolve it automatically
	 * 
	 * @param object1
	 *            object to be stepped and tested for collision
	 * @param object2
	 *            object to be tested against
	 */
	public static void resolveCollision(PhysicsObject object1, PhysicsObject object2) {
		if (areRectsIntersecting(new Rect(object1.x + (int) object1.speedX, object1.y, object1.width, object1.height),
				new Rect(object2.x + (int) object2.speedX, object2.y, object2.width, object2.height))) {
			// X Collision -> Separate Objects.
			int x1 = object1.x + (int) object1.speedX;
			int x2 = object2.x + (int) object2.speedX;
			int separation2 = x1 < x2 ? (x1 + object1.width + 1) - x2 : x1 > x2 ? (x2 + object2.width + 1) - x1 : 0;

			if (object1.isStatic && object2.isStatic)
				System.err.println("Two static objects colliding: (" + object1 + ", " + object2 + ")");

			if (object1.isStatic) {
				object2.x += separation2 + object2.speedX;
			} else if (object2.isStatic) {
				object1.x += -separation2 + object1.speedX;
			} else {
				object1.x += -(separation2 / 2) + object1.speedX;
				object2.x += separation2 / 2 + object2.speedX;
			}

			// Nullify speed for now
			if (separation2 * separation2 > 0) {
				object1.speedX = 0;
				object2.speedX = 0;
			}
		}
		if (areRectsIntersecting(new Rect(object1.x, object1.y + (int) object1.speedY, object1.width, object1.height),
				new Rect(object2.x, object2.y, object2.width, object2.height))) {
			// Y Collision -> Separate Objects.
			int y1 = object1.y + (int) object1.speedY;
			int y2 = object2.y + (int) object2.speedY;
			int separation2 = y1 < y2 ? (y1 + object1.height + 1) - y2 : y1 > y2 ? (y2 + object2.height + 1) - y1 : 0;

			if (object1.isStatic && object2.isStatic)
				System.err.println("Two static objects colliding: (" + object1 + ", " + object2 + ")");

			if (object1.isStatic) {
				object2.y += separation2 + object2.speedY;
			} else if (object2.isStatic) {
				object1.y += -separation2 + object1.speedY;
			} else {
				object1.y += -(separation2 / 2) + object1.speedY;
				object2.y += separation2 / 2 + object2.speedY;
			}

			// Nullify speed for now
			if (separation2 * separation2 > 0) {
				object1.speedY = 0;
				object2.speedY = 0;
			}
		}
	}

	private static boolean areRectsIntersecting(Rect rect1, Rect rect2) {
		if (rect1.x > rect2.x + rect2.width || rect2.x > rect1.x + rect1.width || rect1.y > rect2.y + rect2.height
				|| rect2.y > rect1.y + rect1.height)
			return false;

		return true;
	}
}
