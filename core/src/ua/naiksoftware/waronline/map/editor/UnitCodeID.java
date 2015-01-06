package ua.naiksoftware.waronline.map.editor;

import com.badlogic.gdx.utils.ArrayMap;
import ua.naiksoftware.waronline.game.unit.UnitCode;
import static ua.naiksoftware.waronline.game.unit.UnitCode.*;

/**
 * For save and read map
 *
 * @author Naik
 */
public class UnitCodeID {

    private static final ArrayMap<UnitCode, Integer> map = new ArrayMap<UnitCode, Integer>() {
        {
            int i = 1;
            put(ING_AVTO, i++);
            put(SOLDIER, i++);
            put(BTR_4E, i++);
            put(HOTCHKISS, i++);
            put(T34_85, i++);
            put(PANZER, i++);
            put(TIGER, i++);
            put(ARTILLERY, i++);
        }
    };

    public static int convertUnitCode(UnitCode code) {
        return map.get(code);
    }

    public static UnitCode convertUnitID(int id) {
        return map.getKey(id, true);
    }
}
