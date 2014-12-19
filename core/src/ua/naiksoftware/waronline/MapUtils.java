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
	public static final String CELL_CODE_PROP = "code";

	private static final int CELL_SIZE = 72;
	private static final float ANIM_INTERVAL = 0.1f;

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
		Cell cell = getCell(TileCode.GRASS, true);
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
					code = TileCode.WATER;
				else if (x % 4 == 1)
					code = TileCode.BRIDGE_HORIZ;
				else if (y % 4 == 2)
					code = TileCode.WATER;
				else
					code = TileCode.WATER;

				layer.setCell(x, y, getCell(code, true));
			}
		}
		layers.add(layer);
		cells.clear();
		return map;
	}

	public static void saveMap(TiledMap map) {
	}

	public static Cell getCell(TileCode code, boolean cache) {
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
			case TREES_EDGE_DOWN:
				name = "trees_edge_down";
				break;
			case TREES_EDGE_RIGHT:
				name = "trees_edge_right";
				break;
			case TREES_EDGE_UP:
				name = "trees_edge_right";
				rotate = Cell.ROTATE_90;
				break;
			case TREES_EDGE_LEFT:
				name = "trees_edge_right";
				flipHoriz = true;
				break;
			case TREES_CORNER_RIGHT_DOWN:
				name = "trees_corner_right_down";
				break;
			case TREES_CORNER_LEFT_DOWN:
				name = "trees_corner_right_down";
				flipHoriz = true;
				break;
			case TREES_CORNER_RIGHT_UP:
				name = "trees_corner_right_up";
				break;
			case TREES_CORNER_LEFT_UP:
				name = "trees_corner_right_up";
				flipHoriz = true;
				break;
			case TREES_INCORNER_RIGHT_DOWN:
				name = "trees_incorner_right_down";
				break;
			case TREES_INCORNER_LEFT_DOWN:
				name = "trees_incorner_right_down";
				flipHoriz = true;
				break;
			case ROAD_HORIZ:
				name = "road_horiz";
				break;
			case ROAD_VERT:
				name = "road_horiz";
				rotate = Cell.ROTATE_90;
				break;
			case ROAD_INTERSECT:
				name = "road_intersect";
				break;
			case ROAD_CORNER_RIGHT_UP:
				name = "road_corner_right_up";
				break;
			case ROAD_CORNER_RIGHT_DOWN:
				name = "road_corner_right_up";
				flipVert = true;
				break;
			case ROAD_CORNER_LEFT_DOWN:
				name = "road_corner_right_up";
				flipHoriz = flipVert = true;
				break;
			case ROAD_CORNER_LEFT_UP:
				name = "road_corner_right_up";
				flipHoriz = true;
				break;
			case ROAD_END_RIGHT:
				name = "road_end_right";
				break;
			case ROAD_END_LEFT:
				name = "road_end_right";
				flipHoriz = true;
				break;
			case ROAD_END_UP:
				name = "road_end_right";
				rotate = Cell.ROTATE_90;
				break;
			case ROAD_END_DOWN:
				name = "road_end_right";
				rotate = Cell.ROTATE_270;
				break;
			case WATER:
				name = "water";
				break;
			case WATER_DOWN_1:
				name = "water_down1";
				break;
			case WATER_DOWN_2:
				name = "water_down2";
				break;
			case WATER_UP_1:
				name = "water_down1";
				flipVert = true;
				break;
			case WATER_UP_2:
				name = "water_down2";
				flipVert = true;
				break;
			case WATER_LEFT_1:
				name = "water_down1";
				rotate = Cell.ROTATE_270;
				break;
			case WATER_LEFT_2:
				name = "water_down2";
				rotate = Cell.ROTATE_270;
				break;
			case WATER_RIGHT_1:
				name = "water_down1";
				rotate = Cell.ROTATE_90;
				break;
			case WATER_RIGHT_2:
				name = "water_down2";
				rotate = Cell.ROTATE_90;
				break;
			case WATER_CORNER_RIGHT_DOWN:
				name = "water_corner1";
				break;
			case WATER_CORNER_LEFT_DOWN:
				name = "water_corner1";
				flipHoriz = true;
				break;
			case WATER_CORNER_RIGHT_UP:
				name = "water_corner2";
				break;
			case WATER_CORNER_LEFT_UP:
				name = "water_corner2";
				flipHoriz = true;
				break;
			case WATER_INCORNER_RIGHT_UP:
				name = "water_incorner_right_up";
				break;
			case WATER_INCORNER_LEFT_UP:
				name = "water_incorner_right_up";
				rotate = Cell.ROTATE_90;
				break;
			case WATER_INCORNER_LEFT_DOWN:
				name = "water_incorner_right_up";
				rotate = Cell.ROTATE_180;
				break;
			case WATER_INCORNER_RIGHT_DOWN:
				name = "water_incorner_right_up";
				rotate = Cell.ROTATE_270;
				break;
			case BRIDGE_HORIZ:
				name = "bridge";
				break;
			case BRIDGE_VERT:
				name = "bridge";
				rotate = Cell.ROTATE_90;
				break;
			case BRIDGE_UP:
				name = "bridge_up";
				break;
			case BRIDGE_DOWN:
				name = "bridge_down";
				break;
			case BRIDGE_LEFT:
				name = "bridge_down";
				rotate = Cell.ROTATE_270;
				break;
			case BRIDGE_RIGHT:
				name = "bridge_down";
				rotate = Cell.ROTATE_90;
				break;
			case REDUIT_1:
				name = "reduit1";
				break;
			case REDUIT_2:
				name = "reduit2";
				break;
			}

			cell = new Cell();

			Array<StaticTiledMapTile> shots = new Array<StaticTiledMapTile>();
			for (TextureAtlas.AtlasRegion region : tileAtlas.findRegions(name)) {
				region.flip(flipHoriz, flipVert);
				shots.add(new StaticTiledMapTile(region));
			}

			if (shots.size > 1) {
				cell.setTile(new AnimatedTiledMapTile(ANIM_INTERVAL, shots));
			} else {
				cell.setTile(shots.first());
			}
			cell.setRotation(rotate);
			cell.getTile().getProperties().put(CELL_CODE_PROP, code);

			if (cache) {
				cells.put(code, cell);
			}
		}
		return cell;
	}
}
