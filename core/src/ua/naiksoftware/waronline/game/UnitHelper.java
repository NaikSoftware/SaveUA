package ua.naiksoftware.waronline.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.ArrayMap;

public class UnitHelper {

    public static final int CIRCLE_RADIUS = 6;

    public static final int BAR_W = 72, BAR_H = 12;
    public static final int LINE_SHIFT_X = 12;
    public static final int LINE_SHIFT_Y = 4;
    public static final int LINE_WIDTH = 58;
    private static final int LINE_HEIGHT = 4;
    private static final ArrayMap<Color, Texture> bars = new ArrayMap<Color, Texture>();

    public static Texture getLifeBar(Color c) {
        Texture bar = bars.get(c);
        if (bar == null) {
            Pixmap p = new Pixmap(BAR_W, BAR_H, Pixmap.Format.RGBA8888);
            p.setColor(c);
            p.fillCircle(CIRCLE_RADIUS, CIRCLE_RADIUS, CIRCLE_RADIUS - 2);
            p.fillRectangle(LINE_SHIFT_X, LINE_SHIFT_Y, LINE_WIDTH, LINE_HEIGHT);
            bar = new Texture(p);
            p.dispose();
            bars.put(c, bar);
        }
        return bar;
    }

    public static void disposeAll() {
        for (int size = bars.size, i = 0; i < size; i++) {
            bars.getValueAt(i).dispose();
        }
        bars.clear();
    }
}
