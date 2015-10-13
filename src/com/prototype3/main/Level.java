package com.prototype3.main;

import java.io.*;
import java.util.ArrayList;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

import com.prototype3.gameobjects.Blob;
import com.prototype3.gameobjects.GameObject;
import com.prototype3.gameobjects.PhysicsObject;
import com.prototype3.gameobjects.tiles.Tile;
import com.prototype3.gameobjects.tiles.TilePlatform;
import com.prototype3.helper.LineSegment;
import com.prototype3.helper.Maths;
import com.prototype3.helper.StringUtility;
import com.prototype3.helper.Vector2f;
import com.prototype3.helper.Vector3f;

public class Level extends GameObject {
	/* Info default values */
	public String levelName;
	public int tileWidth;
	public int tileHeight;
	public int levelWidth;
	public int levelHeight;
	public int playerSpawnX;
	public int playerSpawnY;

	// Loaded entities, [Game] will sort and add them to it's own data
	// structures
	public ArrayList<PhysicsObject> entities;

	/* FileFormat tags */
	private static final String INFO_SEGMENT_TAG = "!Info";
	private static final String LEVEL_SEGMENT_TAG = "!Level";

	/* InfoSegment keys for data values */
	private static final String INFO_SEGMENT_KEY_NAME = "Name:";
	private static final String INFO_SEGMENT_KEY_TILEWIDTH = "TileWidth:";
	private static final String INFO_SEGMENT_KEY_TILEHEIGHT = "TileHeight:";

	private Tile[][] levelData;

	// All tiles currently visible, updated each frame
	public ArrayList<Tile> visibleTiles;

	public static Level currentLevel;

	public Level(String filePath) throws IOException {
		this.entities = new ArrayList<>();

		this.levelName = "NoName";
		this.tileWidth = 75;
		this.tileHeight = 75;
		this.playerSpawnX = 0;
		this.playerSpawnY = 0;

		this.loadLevel(filePath);
	}

	private void loadLevel(String filePath) throws IOException {
		// Read every line from file
		String infoSegment = "";
		String levelSegment = "";
		String everything = "";

		try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
			StringBuilder sb = new StringBuilder();
			String line = br.readLine();
			// While there are more lines to read
			while (line != null) {
				if (line.contains("#")) {
					// Remove everything after "#" in this line, including "#"
					line = line.substring(0, line.indexOf('#'));
				}

				// Only add nonempty lines
				if (!line.equals("")) {
					sb.append(System.lineSeparator());
					sb.append(line);
				}

				// Read next line
				line = br.readLine();
			}

			// Combine the whole file (- comments) into one String
			everything = sb.toString();
		}

		// System.out.println("Parsing step 1 done:\n" + everything);

		// Verify File / Split into info & data segments
		if (StringUtility.getOccurenceCount(everything, INFO_SEGMENT_TAG) != 2) {
			System.err.println("Level (" + filePath + ") Info segment broken!");
			System.exit(1);
		}

		if (StringUtility.getOccurenceCount(everything, LEVEL_SEGMENT_TAG) != 2) {
			System.err.println("Level (" + filePath + ") Level segment broken!");
			System.exit(1);
		}

		// Separate file into the 2 different segments
		int infoSegmentStart = everything.indexOf(INFO_SEGMENT_TAG) + INFO_SEGMENT_TAG.length(); // +1
																									// to
																									// account
																									// for
																									// "\n"
		infoSegment = everything.substring(infoSegmentStart,
				everything.substring(infoSegmentStart).indexOf(INFO_SEGMENT_TAG) + infoSegmentStart);

		int levelSegmentStart = everything.indexOf(LEVEL_SEGMENT_TAG) + INFO_SEGMENT_TAG.length() + 1;
		levelSegment = everything.substring(levelSegmentStart,
				everything.substring(levelSegmentStart).indexOf(LEVEL_SEGMENT_TAG) + levelSegmentStart);

		// Remove white spaces & tabs from info segment
		infoSegment = infoSegment.replaceAll(" ", "");
		infoSegment = infoSegment.replaceAll("\t", "");

		// Parse info segment. Note: We remove "\r" from each info because new
		// Integer() will throw an unchecked exception elsewise
		for (String line : infoSegment.split("\n")) {
			line = line.replaceAll("\n", "");
			line = line.replaceAll("\r", "");

			if (line.startsWith("\r") || line.equals(""))
				continue;

			if (line.startsWith(INFO_SEGMENT_KEY_NAME)) {
				this.levelName = line.substring(INFO_SEGMENT_KEY_NAME.length());
			} else if (line.startsWith(INFO_SEGMENT_KEY_TILEWIDTH)) {
				this.tileWidth = new Integer(line.substring(INFO_SEGMENT_KEY_TILEWIDTH.length()).replaceAll("\r", ""));
			} else if (line.startsWith(INFO_SEGMENT_KEY_TILEHEIGHT)) {
				this.tileHeight = new Integer(
						line.substring(INFO_SEGMENT_KEY_TILEHEIGHT.length()).replaceAll("\r", ""));
			} else {
				System.err.println("Unrecognized InfoSegment line in \"" + filePath + "\": " + line);
			}
		}

		// Remove white spaces & tabs
		levelSegment = levelSegment.replaceAll(" ", "");
		levelSegment = levelSegment.replaceAll("\t", "");

		// Determine basic level parameters TODO: make more robust
		this.levelWidth = levelSegment.split("\n")[1].length();
		this.levelHeight = levelSegment.split("\n").length;

		this.levelData = new Tile[levelWidth][levelHeight];

		System.out.println("Level: (" + levelWidth + ", " + levelHeight + ")");

		int y = 0;
		for (String line : levelSegment.split("\n")) {
			if (line.startsWith("\r") || line.isEmpty())
				continue;

			try {
				for (int x = 0; x < line.length(); x++) {
					this.levelData[x][y] = this.parseCharData(x, y, line.charAt(x));
				}
			} catch (SlickException e) {
				e.printStackTrace();
			}

			y++;
		}
	}

	private Tile parseCharData(int x, int y, char character) throws SlickException {
		switch (character) {
		case '-':
			return new TilePlatform(x * this.tileWidth, y * this.tileHeight, this.tileWidth, this.tileHeight);
		case 'x':
			this.playerSpawnX = x * this.tileWidth;
			this.playerSpawnY = y * this.tileHeight;
			break;
		case 'b':
			this.entities.add(new Blob(x * this.tileWidth, y * this.tileWidth, 100, 100));
			break;
		default:
			break;
		}

		return null;
	}

	@Override
	public void prePhysicsUpdate(int delta) throws SlickException {
		// Update chunks TODO: implement
	}

	@Override
	public void afterPhysicsUpdate(int delta) throws SlickException {
		// Update chunks TODO: implement
	}

	/**
	 * Renders Level with lighting effects centered around cameraFoot
	 * 
	 * @param g
	 * @param cameraFoot
	 * @param viewPortX
	 * @param viewPortY
	 * @param viewPortWidth
	 * @param viewPortHeight
	 * @throws SlickException
	 */
	public void render(Graphics g, Vector2f cameraFoot, int viewPortX, int viewPortY, int viewPortWidth,
			int viewPortHeight) throws SlickException {
		// Find visible tiles
		visibleTiles = this.getTilesInsideAABB(viewPortX, viewPortY, viewPortWidth + this.tileWidth * 2,
				viewPortHeight + this.tileHeight);

		if (Game.RAY_DEBUG_MODE_ENABLED && cameraFoot != null)
			// Get all Line segments
			visibleTiles = this.getVisibleFromFoot(cameraFoot, visibleTiles);

		if (visibleTiles != null)
			for (Tile tile : visibleTiles) {
				if (tile != null)
					tile.render(g, viewPortX, viewPortY, viewPortWidth, viewPortHeight);
			}

		if (Game.RAY_DEBUG_MODE_ENABLED) {
			// Render rays
			for (Vector2f rayTip : this.rayTips) {
				if (rayTip == null)
					continue;
				g.setLineWidth(1);
				g.setColor(Color.white);
				g.drawLine((float) cameraFoot.x, (float) cameraFoot.y, (float) rayTip.x, (float) rayTip.y);
			}

			// Render intersects
			for (Vector3f rayIntersect : this.intersects) {
				if (rayIntersect == null)
					continue;
				g.setColor(Color.red);
				g.fillOval(rayIntersect.x - 5, rayIntersect.y - 5, 10, 10);
			}

			// Render line segments
			for (LineSegment segment : this.lineSegments) {
				if (segment == null)
					continue;
				g.setColor(Color.green);
				g.setLineWidth(2);
				g.drawLine((float) segment.foot.x, (float) segment.foot.y, (float) segment.tip.x,
						(float) segment.tip.y);
			}
		}
	}

	@Override
	public void render(Graphics g, int viewPortX, int viewPortY, int viewPortWidth, int viewPortHeight)
			throws SlickException {
		this.render(g, null, viewPortX, viewPortY, viewPortWidth, viewPortHeight);
	}

	/**
	 * Returns tiles inside of the specified rect for collision purposes
	 * 
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @return
	 */
	public ArrayList<Tile> getTilesInsideAABB(int x, int y, int width, int height) {
		// Allocate space for the tiles
		ArrayList<Tile> tiles = new ArrayList<>();

		// Convert values to tileData() indexes
		x /= this.tileWidth;
		y /= this.tileHeight;
		width /= this.tileWidth;
		height /= this.tileHeight;

		// Bounds checking
		x = x > 0 ? (x < this.levelWidth ? x : this.levelWidth - 1) : 0;
		y = y > 0 ? (y < this.levelHeight ? y : this.levelHeight - 1) : 0;
		width = width > 0 ? (x + width < this.levelWidth ? width : this.levelWidth - x) : 0;
		height = height > 0 ? (y + height < this.levelHeight ? height : this.levelHeight - y) : 0;

		// Get tiles from those parameters
		for (int i = x; i < x + width; i++) {
			for (int j = y; j < y + height; j++) {
				Tile tileToAdd = this.levelData[i][j];
				if (tileToAdd != null)
					tiles.add(tileToAdd);
			}
		}

		return tiles;
	}

	ArrayList<Vector2f> rayTips = new ArrayList<>();
	ArrayList<Vector3f> intersects = new ArrayList<>();
	ArrayList<LineSegment> lineSegments = new ArrayList<>();

	private ArrayList<Tile> getVisibleFromFoot(Vector2f foot, ArrayList<Tile> visibleTiles) {
		lineSegments.clear();
		intersects.clear();
		rayTips.clear();

		// Get all lineSegments
		for (Tile tile : visibleTiles) {
			// Add all lines that make up a tile
			for (LineSegment line : tile.getOuterLineSegments()) {
				// Add all lineSegments
				lineSegments.add(line);

				// Add rays for tile
				boolean footExistent = false;
				boolean tipExistent = false;
				for (Vector2f rayTip : rayTips) {
					if (rayTip.equals(line.foot))
						footExistent = true;

					if (rayTip.equals(line.tip))
						tipExistent = true;

					if (footExistent && tipExistent)
						break;
				}

				// Calculate rays
				if (!footExistent) {
					// Add ray to center of tile (When rayTip.x == rayFoot.x we
					// get very
					// weird behaviour -> workaround patch
					if (line.foot.x != foot.x)
						rayTips.add(line.foot);
					else
						rayTips.add(new Vector2f(line.foot.x + 1, line.foot.y));
				}

				if (!tipExistent) {
					// Add ray to center of tile (When rayTip.x == rayFoot.x we
					// get very
					// weird behaviour -> workaround patch
					if (line.tip.x != foot.x)
						rayTips.add(line.tip);
					else
						rayTips.add(new Vector2f(line.tip.x + 1, line.tip.y));
				}
			}
		}

		visibleTiles.clear();

		// Test each ray and get closest intersection for each ray
		for (Vector2f rayTip : rayTips) {
			Vector3f closestIntersect = null;
			Tile closestIntersectTile = null;
			for (LineSegment line : lineSegments) {
				Vector3f intersect = Maths.getRayImpactPoint(foot, rayTip, line.foot, line.tip);
				if (intersect == null)
					continue;
				if (closestIntersect == null || intersect.z < closestIntersect.z) {
					closestIntersect = intersect;
					closestIntersectTile = line.tile;
				}
			}

			// Closest intersection found (Intersection Point + tile)
			if (closestIntersect != null) {
				visibleTiles.add(closestIntersectTile);
				intersects.add(closestIntersect);
			}
		}

		return visibleTiles;
	}

	/**
	 * Whether or not the object is visible from RayFoot
	 * 
	 * @param object
	 * @param RayFoot
	 * @return
	 */
	public boolean isVisible(PhysicsObject object, Vector2f rayFoot) {
		// Calculate rays to be sent
		ArrayList<Vector2f> rayTips = new ArrayList<>();
		rayTips.add(new Vector2f(object.x + object.width / 2, object.y));
		rayTips.add(new Vector2f(object.x + object.width / 2, object.y + object.height / 2));
		rayTips.add(new Vector2f(object.x + object.width / 2, object.y + object.height));

		// Get all line segments
		for (Vector2f rayTip : rayTips) {
			Vector3f closestIntersection = null;
			for (Tile tile : this.visibleTiles) {
				for (LineSegment segment : tile.getOuterLineSegments()) {
					// Ray check for each segment
					Vector3f intersection = Maths.getRayImpactPoint(rayFoot, rayTip, segment.foot, segment.tip);
					if (intersection == null)
						continue;
					if (closestIntersection == null || intersection.z < closestIntersection.z) {
						closestIntersection = intersection;
					}
				}
			}

			// No intersection at all -> Object is surely visible
			if (closestIntersection == null)
				return true;

			// Calculate vector of closestIntersection
			closestIntersection.x = closestIntersection.x - rayFoot.x;
			closestIntersection.y = closestIntersection.y - rayFoot.y;

			// Calculate vector of object
			Vector2f rayToObject = new Vector2f((object.x + object.width / 2) - rayFoot.x,
					(object.y + object.height / 2) - rayFoot.y);

			// Calculate magnitudes
			float magnitude_intersection = (float) Math.sqrt(
					closestIntersection.x * closestIntersection.x + closestIntersection.y * closestIntersection.y);
			float magnitude_rayToObject = (float) Math
					.sqrt(rayToObject.x * rayToObject.x + rayToObject.y * rayToObject.y);

			// Object is closer than the nearest intersection -> Visible
			if (magnitude_rayToObject < magnitude_intersection)
				return true;
		}

		return false;
	}

	public Tile[][] getNeighboringTiles(Tile tile) {
		// get Tiles in AABB around tile
		ArrayList<Tile> tiles = this.getTilesInsideAABB(tile.x + tile.width / 2 - this.tileWidth,
				tile.y + tile.height / 2 - this.tileHeight, 3 * this.tileWidth, 3 * this.tileHeight);

		// Sort tiles according to geometric layout
		Tile[][] neighborTiles = new Tile[3][3];

		// Sort tiles based on x and y position
		for (Tile neighbor : tiles) {
			int neighborX = (neighbor.x - tile.x) < 0 ? 0 : (neighbor.x == tile.x ? 1 : 2);
			int neighborY = (neighbor.y - tile.y) < 0 ? 0 : (neighbor.y == tile.y ? 1 : 2);

			neighborTiles[neighborX][neighborY] = neighbor;
		}

		return neighborTiles;
	}
}
