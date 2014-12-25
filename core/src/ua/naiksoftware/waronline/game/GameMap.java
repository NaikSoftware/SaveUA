package ua.naiksoftware.waronline.game;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.utils.Array;
import ua.naiksoftware.waronline.game.unit.Unit;

/**
 * Содержит тайловую карту, юнитов
 * @author Naik
 */
public class GameMap {

    private final TiledMap tiledMap;
    
    public GameMap(TiledMap tiledMap, Array<Unit> units) {
        this.tiledMap = tiledMap;
    }
    
    public TiledMap getTiledMap() {
        return tiledMap;
    }
}
