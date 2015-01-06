package ua.naiksoftware.waronline.game.editor;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.utils.Array;
import java.util.HashSet;
import java.util.Set;
import ua.naiksoftware.waronline.game.MapObject;

/**
 * Содержит тайловую карту, исходные позиции игроков
 *
 * @author Naik
 */
public class EditGameMap {

    private final TiledMap tiledMap;
    private final Array<MapUnit> units = new Array<MapUnit>();
    private final Array<MapUnit> freeUnits = new Array<MapUnit>();
    private final Array<MapObject> objects = new Array<MapObject>();
    private final int maxGamers;

    public EditGameMap(TiledMap tiledMap, Array<Sprite> sprites) {
        this.tiledMap = tiledMap;
        Set<Integer> gamers = new HashSet<Integer>();
        for (Sprite s : sprites) {
            if (s instanceof MapUnit) {
                MapUnit u = (MapUnit) s;
                if (u.getGamer() == null) {
                    freeUnits.add(u);
                } else {
                    units.add(u);
                    gamers.add(u.getGamer().getId());
                }
            } else if (s instanceof MapObject) {
                objects.add((MapObject) s);
            }
        }
        maxGamers = gamers.size();
    }

    public TiledMap getTiledMap() {
        return tiledMap;
    }

    public Array<MapUnit> getUnits() {
        return units;
    }

    public Array<MapUnit> getFreeUnits() {
        return freeUnits;
    }

    public Array<MapObject> getMapObjects() {
        return objects;
    }

    public int maxGamers() {
        return maxGamers;
    }
}
