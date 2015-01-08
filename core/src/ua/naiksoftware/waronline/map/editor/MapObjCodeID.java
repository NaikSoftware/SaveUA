package ua.naiksoftware.waronline.map.editor;

import com.badlogic.gdx.utils.ArrayMap;
import ua.naiksoftware.waronline.map.MapObjCode;
import static ua.naiksoftware.waronline.map.MapObjCode.*;

/**
 * For save and read map
 *
 * @author Naik
 */
public class MapObjCodeID {

    private static final ArrayMap<MapObjCode, Integer> map = new ArrayMap<MapObjCode, Integer>() {
        {
            int i = 1;

            put(HATA_1, i++);
            put(FORT, i++);
            put(ATB_1, i++);
            put(ATB_2, i++);
            put(CHURCH, i++);
            put(REMAINS1, i++);
            put(REMAINS2, i++);
            put(HATA_2, i++);
            put(TREE_1, i++);
            put(TREE_2, i++);
            put(HATA_3, i++);
            put(HATA_4, i++);
            put(TENT, i++);
            put(STOLB_1, i++);
            put(STOLB_2, i++);
            put(WELL, i++);
            put(KPP, i++);
        }
    };

    public static int convertObjCode(MapObjCode code) {
        return map.get(code);
    }

    public static MapObjCode convertObjID(int id) {
        MapObjCode code = map.getKey(id, true);
		if (code == null) {
			throw new IllegalArgumentException("Can't convert id " + id + " to MapObjCode");
		}
		return code;
    }
}
