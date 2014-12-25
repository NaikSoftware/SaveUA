package ua.naiksoftware.waronline.game;

import com.badlogic.gdx.graphics.Color;
import java.util.Random;

public class Gamer {

    public static int count;

    private final int id;
    private Color color;

    public Gamer() {
        this(null);
    }

    public Gamer(Color color) {
        id = ++count;
        if (color == null) {
            Random r = new Random();
            this.color = new Color();
            this.color.set(r.nextFloat(), r.nextFloat(), r.nextFloat(), 1);
        } else {
            this.color = color;
        }
    }

    public int getId() {
        return id;
    }

    public Color getColor() {
        return color;
    }
}
