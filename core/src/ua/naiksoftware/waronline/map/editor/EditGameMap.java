package ua.naiksoftware.waronline.map.editor;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.utils.Array;
import java.util.HashSet;
import java.util.Set;
import ua.naiksoftware.waronline.map.MapObject;

/**
 * Содержит тайловую карту, исходные позиции игроков
 *
 * @author Naik
 */
public class EditGameMap {

    private final TiledMap tiledMap;
    private final Array<MapUnit> units = new Array<MapUnit>();
    private final Array<MapObject> objects = new Array<MapObject>();
    private final int maxGamers;
    private final String name;

    public EditGameMap(TiledMap tiledMap, String name, Array<Sprite> sprites) {
        this.tiledMap = tiledMap;
        this.name = name;
        Set<Integer> gamers = new HashSet<Integer>();
        for (Sprite s : sprites) {
            if (s instanceof MapUnit) {
                MapUnit u = (MapUnit) s;
                units.add(u);
                if (u.getGamer() != null) {// if gamer == null - its a free unit
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
    
    public String getName() {
        return name;
    }

    public Array<MapUnit> getUnits() {
        return units;
    }

    public Array<MapObject> getMapObjects() {
        return objects;
    }

    public int maxGamers() {
        return maxGamers;
    }
}
