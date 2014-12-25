package ua.naiksoftware.waronline;

import java.io.BufferedReader;
import java.io.IOException;

import ua.naiksoftware.waronline.game.ObjCode;
import ua.naiksoftware.waronline.game.TileCode;
import ua.naiksoftware.waronline.res.ResKeeper;
import ua.naiksoftware.waronline.res.id.AtlasId;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
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
import com.badlogic.gdx.utils.DataInput;
import com.badlogic.gdx.utils.DataOutput;
import ua.naiksoftware.waronline.game.GameMap;
import ua.naiksoftware.waronline.game.editor.EditGameMap;

public class MapUtils {

    public static final String MAP_W_PROP = "cellw";
    public static final String MAP_H_PROP = "cellh";
    public static final String CELL_SIZE_PROP = "cellsize";
    public static final String MAP_NAME_PROP = "mapname";
    public static final String MAX_GAMERS_PROP = "maxgamers";

    public static final String CELL_IMPASSABLE_PROP = "impassable_cell";
    public static final String CELL_CODE_PROP = "code";
    public static final String CELL_OBJ_PROP = "codeobj";

    private static final int CELL_SIZE = 72;
    private static final float ANIM_INTERVAL = 0.1f;

    private static final String INTERNAL_LIST = "maps/list.txt";
    private static final String LOCAL_MAP_DIR = "maps_local";
    private static final int DATA_DIVIDER = -99;

    private static TextureAtlas tileAtlas;
    private static final ArrayMap<TileCode, Cell> cells = new ArrayMap<TileCode, Cell>();

    public static TiledMap genVoidMap(int w, int h, String name) {
        tileAtlas = ResKeeper.get(AtlasId.MAP_TILES);
        TiledMap map = new TiledMap();
        MapProperties mapProp = map.getProperties();
        mapProp.put(MAP_W_PROP, w);
        mapProp.put(MAP_H_PROP, h);
        mapProp.put(MAP_NAME_PROP, name);
        mapProp.put(CELL_SIZE_PROP, CELL_SIZE);
        MapLayers layers = map.getLayers();
        TiledMapTileLayer layerBg = new TiledMapTileLayer(w, h, CELL_SIZE,
                CELL_SIZE);
        Cell cell = getCell(TileCode.GRASS, true);
        for (int i = 0; i < w; i++) {
            for (int j = 0; j < h; j++) {
                layerBg.setCell(i, j, cell);
            }
        }
        layers.add(layerBg);
        cells.clear();

        layers.add(new TiledMapTileLayer(w, h, CELL_SIZE, CELL_SIZE));// 1x1
        layers.add(new TiledMapTileLayer(w / 2, h / 2, CELL_SIZE * 2,
                CELL_SIZE * 2));// 2x2
        layers.add(new TiledMapTileLayer(w / 3, h / 3, CELL_SIZE * 3,
                CELL_SIZE * 3));// 3x3
        layers.add(new TiledMapTileLayer(w / 4, h / 4, CELL_SIZE * 4,
                CELL_SIZE * 4));// 4x4
        layers.add(new TiledMapTileLayer(w / 5, h / 5, CELL_SIZE * 5,
                CELL_SIZE * 5));// 5x5

        return map;
    }

    /**
     * Загружает тайловую карту.
     *
     * @param path - путь к файлу карты
     * @param internal - предустановленная ли карта, или созданная пользователем
     * @return загруженную карту
     */
    public static GameMap loadTileMap(String path, boolean internal) {
        tileAtlas = ResKeeper.get(AtlasId.MAP_TILES);
        TiledMap map = new TiledMap();
        MapProperties mapProp = map.getProperties();
        MapLayers layers = map.getLayers();
        TiledMapTileLayer layerBg;

        int mapW, mapH;

        FileHandle fh;
        if (internal) {
            fh = Gdx.files.internal(path);
        } else {
            fh = Gdx.files.local(path);
        }
        if (!fh.exists()) {
            throw new RuntimeException("Map " + path + " not found");
        }

        DataInput data = new DataInput(fh.read());
        try {
            mapProp.put(MAP_NAME_PROP, data.readUTF());
            data.readInt();//ignore maxGamers, get it in GameMap class
            mapW = data.readInt();
            mapH = data.readInt();
            mapProp.put(MAP_W_PROP, mapW);
            mapProp.put(MAP_H_PROP, mapH);
            mapProp.put(CELL_SIZE_PROP, CELL_SIZE);
            layerBg = new TiledMapTileLayer(mapW, mapH, CELL_SIZE, CELL_SIZE);

            layers.add(layerBg);
            // Read other layers, etc
            data.close();
            cells.clear();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new GameMap(map, null);
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

    public static Cell getObjCell(ObjCode code) {
        TextureAtlas atlas = ResKeeper.get(AtlasId.OVERLAY_IMAGES);
        String name = null;
        switch (code) {
            case HATA_1:
                name = "hata";
                break;
            case FORT:
                name = "fort2x2";
                break;
            case ATB:
                name = "atb3x3";
                break;
            case CHURCH:
                name = "church2x1";
                break;
            case REMAINS1:
                name = "remains1-2x2";
                break;
            case REMAINS2:
                name = "remains2-2x1";
                break;
            case HATA_2:
                name = "hata2x2";
                break;
            case TREE_1:
                name = "tree2";
                break;
            case TREE_2:
                name = "tree7";
                break;
            case HATA_3:
                name = "oldhata2x1";
                break;
            case HATA_4:
                name = "hata1";
                break;
            case TENT:
                name = "tent2x1";
                break;
            case STOLB_1:
                name = "stolb1x1";
                break;
            case STOLB_2:
                name = "stolb2-1x1";
                break;
            case WELL:
                name = "well";
                break;
            case KPP:
                name = "kpp1x2";
                break;
        }

        if (name == null) {
            throw new IllegalArgumentException("Name \"" + name
                    + "\" in atlas objects not exists");
        }
        Cell cell = new Cell();

        Array<StaticTiledMapTile> shots = new Array<StaticTiledMapTile>();
        for (TextureAtlas.AtlasRegion region : atlas.findRegions(name)) {
            shots.add(new StaticTiledMapTile(region));
        }

        if (shots.size > 1) {
            cell.setTile(new AnimatedTiledMapTile(ANIM_INTERVAL, shots));
        } else {
            cell.setTile(shots.first());
        }
        cell.getTile().getProperties().put(CELL_OBJ_PROP, code);

        return cell;
    }

    public static void saveMap(EditGameMap gameMap) {
        FileHandle dir = Gdx.files.local(LOCAL_MAP_DIR);
        dir.mkdirs();
        FileHandle file = dir.child(String.valueOf(System.currentTimeMillis()));
        DataOutput data = new DataOutput(file.write(false));
        TiledMap map = gameMap.getTiledMap();
        MapProperties prop = map.getProperties();
        int mapW = prop.get(MAP_W_PROP, Integer.class);
        int mapH = prop.get(MAP_H_PROP, Integer.class);
        int maxGamers = prop.get(MAX_GAMERS_PROP, Integer.class);
        try {
            data.writeUTF(prop.get(MAP_NAME_PROP, String.class));
            data.writeInt(maxGamers);
            data.writeInt(mapW);
            data.writeInt(mapH);

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
