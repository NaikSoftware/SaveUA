package ua.naiksoftware.waronline;

import ua.naiksoftware.waronline.game.TileCode;
import ua.naiksoftware.waronline.res.ResKeeper;
import ua.naiksoftware.waronline.res.id.AtlasId;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.tiles.AnimatedTiledMapTile;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ArrayMap;

public class MapUtils {

	public static final String CELL_W_PROP = "cellw";
	public static final String CELL_H_PROP = "cellh";
	public static final String CELL_SIZE_PROP = "cellsize";
	public static final String MAP_NAME_PROP = "mapname";

	public static final String CELL_IMPASSABLE_PROP = "impassable_cell";

	private static final int CELL_SIZE = 72;
	private static final float ANIM_INTERVAL = 0.3f;

	private static TextureAtlas tileAtlas;
	private static ArrayMap<TileCode, Cell> cells = new ArrayMap<TileCode, Cell>();

	public static TiledMap genVoidMap(int w, int h, String name) {
		tileAtlas = ResKeeper.get(AtlasId.MAP_TILES);
		TiledMap map = new TiledMap();
		MapProperties mapProp = map.getProperties();
		mapProp.put(CELL_W_PROP, w);
		mapProp.put(CELL_H_PROP, h);
		mapProp.put(MAP_NAME_PROP, name);
		mapProp.put(CELL_SIZE_PROP, CELL_SIZE);
		TiledMapTileLayer layerBg = new TiledMapTileLayer(w, h, CELL_SIZE,
				CELL_SIZE);
		Cell cell = getCell(TileCode.GRASS);
		for (int i = 0; i < w; i++) {
			for (int j = 0; j < h; j++) {
				layerBg.setCell(i, j, cell);
			}
		}
		map.getLayers().add(layerBg);
		cells.clear();
		return map;
	}

	/**
	 * Загружает тайловую карту.
	 * 
	 * @param path
	 *            - путь к файлу карты
	 * @param internal
	 *            - предустановленная ли карта, или созданная пользователем
	 * @return загруженную карту
	 */
	public static TiledMap loadTileMap(String path, boolean internal) {
		tileAtlas = ResKeeper.get(AtlasId.MAP_TILES);
		TiledMap map = new TiledMap();
		MapLayers layers = map.getLayers();
		// debug
		int cellSize = CELL_SIZE;
		int mapW = 50, mapH = 30;
		MapProperties mapProp = map.getProperties();
		mapProp.put(CELL_W_PROP, mapW);
		mapProp.put(CELL_H_PROP, mapH);
		mapProp.put(CELL_SIZE_PROP, cellSize);
		TiledMapTileLayer layer = new TiledMapTileLayer(mapW, mapH, cellSize,
				cellSize);
		TileCode code;
		for (int x = 0; x < mapW; x++) {
			for (int y = 0; y < mapH; y++) {

				if (x % 4 == 0)
					code = TileCode.GRASS;
				else if (x % 4 == 1)
					code = TileCode.BRIDGE_HORIZ;
				else if (y % 4 == 2)
					code = TileCode.TREES;
				else
					code = TileCode.GRASS;

				layer.setCell(x, y, getCell(code));
			}
		}
		layers.add(layer);
		cells.clear();
		return map;
	}

	public static void saveMap(TiledMap map) {
	}

	private static Cell getCell(TileCode code) {
		Cell cell = cells.get(code);
		if (cell == null) {

			String name = null;
			boolean flipHoriz = false;
			boolean flipVert = false;
			int rotate = Cell.ROTATE_0;

			switch (code) {
			case GRASS:
				name = "grass";
				break;
			case TREES:
				name = "trees";
				break;
			case BRIDGE_HORIZ:
				name = "bridge";
				break;
			}

			cell = new Cell();

			Array<StaticTiledMapTile> shots = new Array<StaticTiledMapTile>();
			for (TextureAtlas.AtlasRegion region : tileAtlas.findRegions(name)) {
				shots.add(new StaticTiledMapTile(region));
			}
			if (shots.size > 1) {
				cell.setTile(new AnimatedTiledMapTile(ANIM_INTERVAL, shots));
			} else {
				cell.setTile(shots.first());
			}

			cell.setFlipHorizontally(flipHoriz);
			cell.setFlipVertically(flipVert);
			cell.setRotation(rotate);

			cells.put(code, cell);
		}
		return cell;
	}
}
