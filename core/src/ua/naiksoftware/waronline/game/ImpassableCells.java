package ua.naiksoftware.waronline.game;

import com.badlogic.gdx.utils.Array;

/**
 *
 * @author Naik
 */
public class ImpassableCells {

    private static class Point {

        private final int x, y;

        private Point(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    private static final Array<Point> points = new Array<Point>();

	/** Can contains duplicates for one point */
    public static void add(int x, int y) {
        points.add(new Point(x, y));
    }

    public static boolean have(int x, int y) {
        for (Point p : points) {
            if (p.x == x && p.y == y) {
                return true;
            }
        }
        return false;
    }

    public static void remove(int x, int y) {
        for (int size = points.size, i = 0;i < size;i++) {
			Point p = points.get(i);
            if (p.x == x && p.y == y) {
                points.removeValue(p, true);
				size--;
				i--;
            }
        }
    }
	
	public static void clear() {
		points.clear();
	}
}
