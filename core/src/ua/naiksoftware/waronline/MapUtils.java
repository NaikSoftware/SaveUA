package ua.naiksoftware.waronline;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
import com.badlogic.gdx.utils.Array;

public class MapUtils {
	
	public static final String CELL_W_PROP = "cellw";
	public static final String CELL_H_PROP = "cellh";
	public static final String CELL_SIZE = "cellsize";

	public static TiledMap loadTileMap(String path) {
		TextureAtlas tileAtlas = new TextureAtlas(Gdx.files.internal(path));
		Array<TextureAtlas.AtlasRegion> tiles = tileAtlas.getRegions();
		TiledMap map = new TiledMap();
		MapLayers layers = map.getLayers();
		// debug
		int cellSize = 72;
		int mapW = 50, mapH = 30;
		MapProperties mapProp =  map.getProperties();
		mapProp.put(CELL_W_PROP, mapW);
		mapProp.put(CELL_H_PROP, mapH);
		mapProp.put(CELL_SIZE, cellSize);
		for (int l = 0; l < 1; l++) {
			TiledMapTileLayer layer = new TiledMapTileLayer(mapW, mapH,
					cellSize, cellSize);
			for (int x = 0; x < mapW; x++) {
				for (int y = 0; y < mapH; y++) {
					int t = (int) (Math.random() * tiles.size);
					if (y == 0) t = 7;
					else if (y == mapH - 1) t = 6;
					if (x == 0) t = 3;
					else if (x == mapW - 1) t = 2;
					Cell cell = new Cell();
					cell.setTile(new StaticTiledMapTile(tiles.get(t)));
					layer.setCell(x, y, cell);
				}
			}
			layers.add(layer);
		}
		return map;
	}
}
