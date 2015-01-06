package ua.naiksoftware.waronline.game;

import java.util.HashSet;
import java.util.Set;

/**
 * Непроходимые клетки на карте (под зданиями, деревьями и др. обьектами)
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

        @Override
        public boolean equals(Object obj) {
            Point p2 = (Point) obj;
            return p2.x == x && p2.y == y;
        }

        @Override
        public int hashCode() {
            int hash = 7;
            hash = 11 * hash + this.x;
            hash = 11 * hash + this.y;
            return hash;
        }
    }

    private static final Set<Point> points = new HashSet<Point>();

    public static void add(int x, int y) {
        points.add(new Point(x, y));
    }

    public static boolean have(int x, int y) {
        return points.contains(new Point(x, y));
    }

    public static void remove(int x, int y) {
        points.remove(new Point(x, y));
    }

    public static void clear() {
        points.clear();
    }
}
