package ua.naiksoftware.waronline.game.editor;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.utils.Array;

/**
 * Содержит тайловую карту, исходные позиции игроков
 *
 * @author Naik
 */
public class EditGameMap {

    private final TiledMap tiledMap;
    private final Array<MapUnit> units;

    public EditGameMap(TiledMap tiledMap, Array<MapUnit> units) {
        this.tiledMap = tiledMap;
        this.units = units;
    }

    public TiledMap getTiledMap() {
        return tiledMap;
    }
}
