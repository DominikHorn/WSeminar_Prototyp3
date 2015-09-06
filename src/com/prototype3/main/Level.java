package com.prototype3.main;

import java.io.*;
import java.util.ArrayList;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

import com.prototype3.gameobjects.GameObject;
import com.prototype3.gameobjects.tiles.Tile;
import com.prototype3.gameobjects.tiles.TilePlatform;
import com.prototype3.helper.StringUtility;

public class Level extends GameObject {
	/* Info default values TODO: accessors */
	public String levelName;
	public int tileWidth;
	public int tileHeight;
	public int chunkWidth;
	// public int chunkHeight;// TODO: add support for vertical chunks!
	public int playerSpawnX;
	public int playerSpawnY;
	public int levelHeight;

	/* FileFormat tags */
	private static final String INFO_SEGMENT_TAG = "!Info";
	private static final String LEVEL_SEGMENT_TAG = "!Level";

	/* InfoSegment keys for data values */
	private static final String INFO_SEGMENT_KEY_NAME = "Name:";
	private static final String INFO_SEGMENT_KEY_TILEWIDTH = "TileWidth:";
	private static final String INFO_SEGMENT_KEY_TILEHEIGHT = "TileHeight:";
	private static final String INFO_SEGMENT_KEY_CHUNKWIDTH = "ChunkWidth:";
	// private static final String INFO_SEGMENT_KEY_CHUNKHEIGHT =
	// "ChunkHeight:";

	// Each level consists of n-number chunks (Vertical strips of tiles)
	private ArrayList<LevelChunk> chunks;

	public Level(String filePath) throws IOException {
		this.chunks = new ArrayList<>();

		this.levelName = "NoName";
		this.tileWidth = 75;
		this.tileHeight = 75;
		this.chunkWidth = 32;
		// Chunk height == -1 => Chunk height will equal height of level
		// this.chunkHeight = -1;
		this.playerSpawnX = 0;
		this.playerSpawnY = 0;

		// TODO: load level from file
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

		// System.out.println("Parsing step 2 done:\nInfo:\n" + infoSegment +
		// levelSegment);

		// Remove white spaces & tabs from info segment
		infoSegment = infoSegment.replaceAll(" ", "");
		infoSegment = infoSegment.replaceAll("\t", "");

		// System.out.println("Parsing step 3 done:\nInfo:\n" + infoSegment);

		// Parse info segment. Note: We remove "\r" from each info because new
		// Integer() will throw an unchecked exception elsewise
		for (String line : infoSegment.split("\n")) {
			// TODO: dirty fix
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
			} else if (line.startsWith(INFO_SEGMENT_KEY_CHUNKWIDTH)) {
				this.chunkWidth = new Integer(
						line.substring(INFO_SEGMENT_KEY_CHUNKWIDTH.length()).replaceAll("\r", ""));
			}
			// else if (line.startsWith(INFO_SEGMENT_KEY_CHUNKHEIGHT)) {
			// this.chunkHeight = new Integer(
			// line.substring(INFO_SEGMENT_KEY_CHUNKHEIGHT.length()).replaceAll("\r",
			// ""));
			// }
			else {
				System.err.println("Unrecognized InfoSegment line in \"" + filePath + "\": " + line);
			}
		}

		// System.out.println("Parsing step 4 done:\nInfo:\n" + this.levelName +
		// "\n" + this.tileWidth + "\n" + this.tileHeight + "\n" +
		// this.chunkWidth + "\n" + this.chunkHeight);

		// Remove white spaces & tabs
		levelSegment = levelSegment.replaceAll(" ", "");
		levelSegment = levelSegment.replaceAll("\t", "");

		// Determin basic level parameters
		int levelWidth = 0;
		for (String line : levelSegment.split("\n"))

		{
			// TODO: dirty fix
			line = line.replaceAll("\n", "");
			line = line.replaceAll("\r", "");
			if (line.startsWith("\r") || line.equals(""))
				continue;

			this.levelHeight++;
			levelWidth = line.length() > levelWidth ? line.length() : levelWidth;
		}

		// Determine amount of chunks needed and allocate space
		String[] chunkStrings = new String[levelWidth % this.chunkWidth == 0 ? levelWidth / this.chunkWidth
				: levelWidth / this.chunkWidth + 1];
		// Initialize space
		for (int i = 0; i < chunkStrings.length; i++)

		{
			chunkStrings[i] = "";
		}

		// Split level into chunks
		for (

		String line : levelSegment.split("\n"))

		{
			// TODO: dirty fix
			line = line.replaceAll("\n", "");
			line = line.replaceAll("\r", "");
			if (line.startsWith("\r") || line.equals(""))
				continue;

			for (int i = 0; i < chunkStrings.length; i++) {
				int chunkBeginIndex = i * this.chunkWidth;
				int chunkEndIndex = chunkBeginIndex + this.chunkWidth >= line.length() ? line.length()
						: chunkBeginIndex + this.chunkWidth;
				chunkStrings[i] = chunkStrings[i] + line.substring(chunkBeginIndex, chunkEndIndex) + "\n";
			}
		}

		// System.out.println("Parsing step 5 done:\nLevel(" + levelWidth + ", "
		// + levelHeight + "); Chunks("
		// + chunkStrings.length + "):");
		// for (int i = 0; i < chunkStrings.length; i++) {
		// System.out.println(chunkStrings[i] + "\n");
		// }

		// Parse each individual chunk
		for (int i = 0; i < chunkStrings.length; i++) {
			// Allocate new Chunk. ypos is defined to be 0
			LevelChunk chunk = new LevelChunk(this, i * this.chunkWidth * this.tileWidth, 0, this.chunkWidth,
					this.levelHeight);// this.chunkHeight > 0 ? this.chunkHeight
										// :
										// levelHeight);
			String chunkData = chunkStrings[i];
			int y = 0;

			// Loop through chunk data and parse
			for (String line : chunkData.split("\n")) {
				for (int x = 0; x < line.length(); x++) {
					Tile tile = null;

					switch (line.charAt(x)) {
					case '-': // Platform
						tile = new TilePlatform(i * this.chunkWidth * this.tileWidth + x * this.tileWidth,
								y * this.tileHeight, this.tileWidth, this.tileHeight);
						break;
					case 'x': // Player spawn
						this.playerSpawnX = i * this.chunkWidth * this.tileWidth + x * this.tileWidth;
						this.playerSpawnY = y * this.tileHeight;
					case '*': // Empty space
					default:
						tile = null;
					}

					chunk.setTile(tile, x, y);
				}

				// Don't forget to increase y
				y++;
			}

			this.chunks.add(chunk);
		}

	}

	@Override
	public void prePhysicsUpdate(int delta) throws SlickException {
		// Update chunks
		for (LevelChunk chunk : this.chunks) {
			chunk.prePhysicsUpdate(delta);
		}
	}

	@Override
	public void afterPhysicsUpdate(int delta) throws SlickException {
		// Update chunks
		for (LevelChunk chunk : this.chunks) {
			chunk.afterPhysicsUpdate(delta);
		}
	}

	@Override
	public void render(Graphics g, int viewPortX, int viewPortY, int viewPortWidth, int viewPortHeight)
			throws SlickException {
		// Render chunks
		for (LevelChunk chunk : this.chunks) {
			chunk.render(g, viewPortX, viewPortY, viewPortWidth, viewPortHeight);
		}
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
	public Tile[] getTilesInsideAABB(int x, int y, int width, int height) {
		// Allocate space for the tiles
		Tile[] tiles = new Tile[(width / this.tileWidth) * (height / this.tileHeight)];
		int tileIndex = 0;

		// Figure out which chunks we need to ask for tiles: convert x to
		// chunkIndex
		int startChunkIndex = ((x / this.tileWidth) - 1) / this.chunkWidth;
		int endChunkIndex = (((x + width) / this.tileWidth) - 1) / this.chunkWidth;

		if (startChunkIndex < 0)
			startChunkIndex = 0;
		else if (startChunkIndex >= this.chunks.size())
			startChunkIndex = this.chunks.size() - 1;

		if (endChunkIndex < 0)
			endChunkIndex = 0;
		else if (endChunkIndex >= this.chunks.size())
			endChunkIndex = this.chunks.size() - 1;

		// Figure out which tiles we want
		int firstTileX = ((x / this.tileWidth) - 1);
		int firstTileY = ((y / this.tileHeight) - 1);
		int lastTileX = (((x + width) / this.tileWidth) - 1);
		int lastTileY = (((y + height) / this.tileHeight) - 1);

		if (firstTileX < 0)
			firstTileX = 0;
		if (firstTileX >= this.chunkWidth * this.chunks.size())
			return tiles;

		if (lastTileY < 0)
			return tiles;
		if (lastTileY >= this.levelHeight)
			lastTileY = this.levelHeight - 1;

		if (lastTileX < 0)
			return tiles;
		if (lastTileX >= this.chunkWidth * this.chunks.size())
			lastTileX = this.chunkWidth * this.chunks.size() - 1;

		if (firstTileY < 0)
			firstTileY = 0;
		if (firstTileY >= this.levelHeight)
			return tiles;

		// Loop through these chunks and ask for tiles
		for (int i = startChunkIndex; i <= endChunkIndex; i++) {
			// TODO: dirty
			int startX = firstTileX - this.chunkWidth * i;
			if (startX < 0)
				startX = 0;

			int stopX = lastTileX - this.chunkWidth * i;
			if (stopX >= this.chunkWidth)
				stopX = this.chunkWidth - 1;
			if (stopX <= 0)	// TODO: rework method without lazy fixes
				stopX = 1;

			Tile[] chunkTiles = this.chunks.get(i).getTiles(startX, firstTileY, stopX, lastTileY);
			for (Tile tile : chunkTiles) {
				if (tile != null)
					tiles[tileIndex++] = tile;
			}
		}
		System.out.println("\n");

		return tiles;
	}
}