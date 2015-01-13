package ua.naiksoftware.waronline.map;

import ua.naiksoftware.waronline.map.editor.*;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.tiles.AnimatedTiledMapTile;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ArrayMap;
import com.badlogic.gdx.utils.DataInput;
import com.badlogic.gdx.utils.DataOutput;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import ua.naiksoftware.waronline.game.Gamer;
import ua.naiksoftware.waronline.game.ImpassableCells;
import ua.naiksoftware.waronline.game.unit.Unit;
import ua.naiksoftware.waronline.game.unit.UnitCode;
import ua.naiksoftware.waronline.res.MapMetaData;
import ua.naiksoftware.waronline.res.ResKeeper;
import ua.naiksoftware.waronline.res.id.AtlasId;
import java.util.zip.ZipInputStream;

public class MapUtils {

    public static final String MAP_NAME_PROP = "mapname";
    public static final String MAX_GAMERS_PROP = "maxgamers";

    private static final int CELL_SIZE = 72;
    private static final float ANIM_INTERVAL = 0.1f;

    private static final String INTERNAL_LIST = "maps/list.txt";
    private static final String LOCAL_MAP_DIR = "maps_local";
    private static final int DATA_DIVIDER = -99;

    private static TextureAtlas tileAtlas;
    private static final ArrayMap<TileCode, MapCell> cells = new ArrayMap<TileCode, MapCell>();

    public static TiledMap genVoidMap(int w, int h) {
        ImpassableCells.clear();
        tileAtlas = ResKeeper.get(AtlasId.MAP_TILES);
        TiledMap map = new TiledMap();
        MapLayers layers = map.getLayers();
        TiledMapTileLayer layerBg = new TiledMapTileLayer(w, h, CELL_SIZE,
                CELL_SIZE);
        MapCell cell = getCell(TileCode.GRASS, true);
        for (int i = 0; i < w; i++) {
            for (int j = 0; j < h; j++) {
                layerBg.setCell(i, j, cell);
            }
        }
        layers.add(layerBg);
        cells.clear();

        return map;
    }

    /**
     * Загружает тайловую карту.
     *
     * @param entry - содержит карту и другую информацию
     * @return загруженную карту
     */
    public static GameMap loadTileMap(MapEntry entry) {
        ImpassableCells.clear();
        String path = entry.getPath();
        boolean local = entry.isLocal();
        tileAtlas = ResKeeper.get(AtlasId.MAP_TILES);
        TiledMap map = new TiledMap();
        MapLayers layers = map.getLayers();
        TiledMapTileLayer layerBg;
        String name = null;
        Array<Unit> units = new Array<Unit>();
        Array<MapObject> mapObjects = new Array<MapObject>();
        Array<Gamer> gamers = new Array<Gamer>();

        FileHandle fh;
        if (local) {
            fh = Gdx.files.local(path);
        } else {
            fh = Gdx.files.internal(path);
        }
        if (!fh.exists()) {
            throw new RuntimeException("Map " + path + " not found");
        }

        try {
            ZipInputStream zis = new ZipInputStream(fh.read());
            zis.getNextEntry();
            DataInput data = new DataInput(zis);
            int r;
            // Header
            name = data.readUTF();
            data.readInt(); // ignore max gamers - get it from units info
            int mapW = data.readInt();
            int mapH = data.readInt();
            layerBg = new TiledMapTileLayer(mapW, mapH, CELL_SIZE, CELL_SIZE);
            layers.add(layerBg);
            // Units
            while ((r = data.readInt()) != DATA_DIVIDER) {
                Gamer g = findGamer(gamers, r);
                if (r > 0 && g == null) {
                    g = new Gamer();
                    gamers.add(g);
                }
                UnitCode code = UnitCodeID.convertUnitID(data.readInt());
                int x = data.readInt();
                int y = data.readInt();
                units.add(new Unit(code, g, x, y));
            }
            // Map objects
            while ((r = data.readInt()) != DATA_DIVIDER) {
                MapObjCode code = MapObjCodeID.convertObjID(r);
                int x = data.readInt() * CELL_SIZE;
                int y = data.readInt() * CELL_SIZE;
                MapObject mapObj = new MapObject(code, x, y);
                mapObjects.add(mapObj);
                setImassableCellsUnderObject(mapObj, true);
            }
            // Tiles
            for (int i = 0; i < mapW; i++) {
                for (int j = 0; j < mapH; j++) {
                    r = data.readInt();
                    MapCell cell = getCell(TileCodeID.convertID(r), true);
                    layerBg.setCell(i, j, cell);
                }
            }
            // TODO: set impassable cells
            data.close();
            zis.close();
            cells.clear();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new GameMap(map, name, units, mapObjects, gamers);
    }

    private static Gamer findGamer(Array<Gamer> gamers, int id) {
        for (Gamer g : gamers) {
            if (g.getId() == id) {
                return g;
            }
        }
        return null;
    }

    public static void setImassableCellsUnderObject(MapObject obj, boolean insert) {
        int x = (int) (obj.getX() / CELL_SIZE);
        int y = (int) (obj.getY() / CELL_SIZE);
        int[][] mask = MapMetaData.objIntersect(obj.getMapObjCode());
        int range = mask.length;
        for (int i = 0; i < range; i++) {
            int arr[] = mask[i];
            for (int j = 0; j < range; j++) {
                if (arr[j] == 1) {
                    if (insert) {
                        ImpassableCells.add(x + i, y + j);
                    } else {
                        ImpassableCells.remove(x + i, y + j);
                    }
                }
            }
        }
    }

    public static MapCell getCell(TileCode code, boolean cache) {
        MapCell cell = cells.get(code);
        if (cell == null) {

            String name = null;
            boolean flipHoriz = false;
            boolean flipVert = false;
            int rotate = MapCell.ROTATE_0;

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
                    rotate = MapCell.ROTATE_90;
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
                    rotate = MapCell.ROTATE_90;
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
                    rotate = MapCell.ROTATE_90;
                    break;
                case ROAD_END_DOWN:
                    name = "road_end_right";
                    rotate = MapCell.ROTATE_270;
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
                    rotate = MapCell.ROTATE_270;
                    break;
                case WATER_LEFT_2:
                    name = "water_down2";
                    rotate = MapCell.ROTATE_270;
                    break;
                case WATER_RIGHT_1:
                    name = "water_down1";
                    rotate = MapCell.ROTATE_90;
                    break;
                case WATER_RIGHT_2:
                    name = "water_down2";
                    rotate = MapCell.ROTATE_90;
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
                    rotate = MapCell.ROTATE_90;
                    break;
                case WATER_INCORNER_LEFT_DOWN:
                    name = "water_incorner_right_up";
                    rotate = MapCell.ROTATE_180;
                    break;
                case WATER_INCORNER_RIGHT_DOWN:
                    name = "water_incorner_right_up";
                    rotate = MapCell.ROTATE_270;
                    break;
                case BRIDGE_HORIZ:
                    name = "bridge";
                    break;
                case BRIDGE_VERT:
                    name = "bridge";
                    rotate = MapCell.ROTATE_90;
                    break;
                case BRIDGE_UP:
                    name = "bridge_up";
                    break;
                case BRIDGE_DOWN:
                    name = "bridge_down";
                    break;
                case BRIDGE_LEFT:
                    name = "bridge_down";
                    rotate = MapCell.ROTATE_270;
                    break;
                case BRIDGE_RIGHT:
                    name = "bridge_down";
                    rotate = MapCell.ROTATE_90;
                    break;
                case REDUIT_1:
                    name = "reduit1";
                    break;
                case REDUIT_2:
                    name = "reduit2";
                    break;
            }

            cell = new MapCell(code);

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

            if (cache) {
                cells.put(code, cell);
            }
        }
        return cell;
    }

    public static void saveMap(EditGameMap gameMap) {
        FileHandle dir = Gdx.files.local(LOCAL_MAP_DIR);
        dir.mkdirs();
        FileHandle file = dir.child(String.valueOf(System.currentTimeMillis()));
        TiledMap map = gameMap.getTiledMap();
        TiledMapTileLayer layerBg = (TiledMapTileLayer) map.getLayers().get(0);
        int mapW = (int) layerBg.getWidth();
        int mapH = (int) layerBg.getHeight();
        int maxGamers = gameMap.maxGamers();
        try {
            ZipOutputStream zos = new ZipOutputStream(file.write(false));
            zos.putNextEntry(new ZipEntry("map"));
            DataOutput data = new DataOutput(zos);
            // Header
            data.writeUTF(gameMap.getName());
            data.writeInt(maxGamers);
            data.writeInt(mapW);
            data.writeInt(mapH);
            // Units
            for (MapUnit u : gameMap.getUnits()) {
                Gamer g = u.getGamer();
                if (g == null) { // Write unit gamer id (0 - free unit)
                    data.writeInt(0);
                } else {
                    data.writeInt(g.getId());
                }
                data.writeInt(UnitCodeID.convertUnitCode(u.getCode()));
                data.writeInt((int) (u.getX() / CELL_SIZE));
                data.writeInt((int) (u.getY() / CELL_SIZE));
            }
            data.writeInt(DATA_DIVIDER);
            // Map objects
            for (MapObject o : gameMap.getMapObjects()) {
                data.writeInt(MapObjCodeID.convertObjCode(o.getMapObjCode()));
                data.writeInt((int) (o.getX() / CELL_SIZE));
                data.writeInt((int) (o.getY() / CELL_SIZE));
            }
            data.writeInt(DATA_DIVIDER);
            // Tiles
            for (int i = 0; i < mapW; i++) {
                for (int j = 0; j < mapH; j++) {
                    MapCell cell = (MapCell) layerBg.getCell(i, j);
                    data.writeInt(TileCodeID.convertTile(cell.getCode()));
                }
            }

            data.flush();
            data.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Array<MapEntry> readMapList() {
        final Array<MapEntry> maps = new Array<MapEntry>();

        // read internal
        BufferedReader reader = new BufferedReader(Gdx.files.internal(
                INTERNAL_LIST).reader("utf-8"));
        String path;
        try {
            while ((path = reader.readLine()) != null) {
                if (path.trim().equals("")) {
                    continue;
                }
                maps.add(new MapEntry(path, false));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // read local
        FileHandle dir = Gdx.files.local(LOCAL_MAP_DIR);
        dir.mkdirs();
        for (FileHandle fh : dir.list()) {
            maps.add(new MapEntry(fh.path(), true));
        }

        return maps;
    }
}
