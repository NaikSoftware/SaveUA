package ua.naiksoftware.waronline.map;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.utils.Array;
import ua.naiksoftware.waronline.game.Gamer;
import ua.naiksoftware.waronline.game.unit.Unit;

/**
 * Содержит тайловую карту, юнитов и др.
 *
 * @author Naik
 */
public class GameMap {

    private final TiledMap tiledMap;
    private final Array<MapObject> mapObj;
    private final Array<Unit> units;
    private final Array<Sprite> sprites;
    private final Array<Gamer> gamers;
    private final String name;

    public GameMap(TiledMap tiledMap, String name, Array<Unit> units, Array<MapObject> mapObj, Array<Gamer> gamers) {
        this.tiledMap = tiledMap;
        this.name = name;
        this.units = units;
        this.mapObj = mapObj;
        this.gamers = gamers;
        sprites = new Array<Sprite>();
        sprites.addAll(mapObj);
        sprites.addAll(units);
    }

    public TiledMap getTiledMap() {
        return tiledMap;
    }

    public String getName() {
        return name;
    }

    public Array<Sprite> getSprites() {
        return sprites;
    }

    public Array<MapObject> getMapObjects() {
        return mapObj;
    }

    public Array<Unit> getUnits() {
        return units;
    }

    public Array<Gamer> getGamers() {
        return gamers;
    }
}
