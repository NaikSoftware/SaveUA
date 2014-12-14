package ua.naiksoftware.waronline;

import ua.naiksoftware.waronline.res.ResKeeper;
import ua.naiksoftware.waronline.res.id.AtlasId;

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
	public static final String CELL_SIZE_PROP = "cellsize";

	private static final int CELL_SIZE = 72;

	public static TiledMap genVoidMap(int w, int h) {
		TiledMap map = new TiledMap();
		MapProperties mapProp = map.getProperties();
		mapProp.put(CELL_W_PROP, w);
		mapProp.put(CELL_H_PROP, h);
		mapProp.put(CELL_SIZE_PROP, CELL_SIZE);
		TiledMapTileLayer layerBg = new TiledMapTileLayer(w, h, CELL_SIZE,
				CELL_SIZE);
		TextureAtlas tileAtlas = ResKeeper.get(AtlasId.MAP_TILES);
		TextureAtlas.AtlasRegion grass = tileAtlas.findRegion("grass");
		StaticTiledMapTile tileGrass = new StaticTiledMapTile(grass);
		tileGrass.setId(20);
		for (int i = 0; i < w; i++) {
			for (int j = 0; j < h; j++) {
				Cell cell = new Cell();
				cell.setTile(tileGrass);
				layerBg.setCell(i, j, cell);
			}
		}
		map.getLayers().add(layerBg);
		return map;
	}

	public static TiledMap loadTileMap(String path) {
		TextureAtlas tileAtlas = ResKeeper.get(AtlasId.MAP_TILES);
		Array<TextureAtlas.AtlasRegion> tiles = tileAtlas.getRegions();
		TiledMap map = new TiledMap();
		MapLayers layers = map.getLayers();
		// debug
		int cellSize = CELL_SIZE;
		int mapW = 50, mapH = 30;
		MapProperties mapProp = map.getProperties();
		mapProp.put(CELL_W_PROP, mapW);
		mapProp.put(CELL_H_PROP, mapH);
		mapProp.put(CELL_SIZE_PROP, cellSize);
		for (int l = 0; l < 1; l++) {
			TiledMapTileLayer layer = new TiledMapTileLayer(mapW, mapH,
					cellSize, cellSize);
			for (int x = 0; x < mapW; x++) {
				for (int y = 0; y < mapH; y++) {
					int t = (int) (Math.random() * tiles.size);
					if (y == 0)
						t = 7;
					else if (y == mapH - 1)
						t = 6;
					if (x == 0)
						t = 3;
					else if (x == mapW - 1)
						t = 2;
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
