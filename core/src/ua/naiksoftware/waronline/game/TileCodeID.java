package ua.naiksoftware.waronline.game;

import com.badlogic.gdx.utils.ArrayMap;
import static ua.naiksoftware.waronline.game.TileCode.*;

public class TileCodeID {
	
	private static final ArrayMap<TileCode, Integer> map = new ArrayMap<TileCode, Integer>() {
		{
			int i = 1;
			
			put(GRASS, i++);
			put(TREES, i++);
			put(TREES_EDGE_DOWN, i++);
			put(TREES_EDGE_RIGHT, i++);
			put(TREES_EDGE_UP, i++);
			put(TREES_EDGE_LEFT, i++);
			put(TREES_CORNER_RIGHT_DOWN, i++);
			put(TREES_CORNER_LEFT_DOWN, i++);
			put(TREES_CORNER_RIGHT_UP, i++);
			put(TREES_CORNER_LEFT_UP, i++);
			put(TREES_INCORNER_RIGHT_DOWN, i++);
			put(TREES_INCORNER_LEFT_DOWN, i++);
			put(ROAD_HORIZ, i++);
			put(ROAD_VERT, i++);
			put(ROAD_INTERSECT, i++);
			put(ROAD_CORNER_RIGHT_UP, i++);
			put(ROAD_CORNER_RIGHT_DOWN, i++);
			put(ROAD_CORNER_LEFT_DOWN, i++);
			put(ROAD_CORNER_LEFT_UP, i++);
			put(ROAD_END_RIGHT, i++);
			put(ROAD_END_LEFT, i++);
			put(ROAD_END_UP, i++);
			put(ROAD_END_DOWN, i++);
			put(WATER, i++);
			put(WATER_DOWN_1, i++);
			put(WATER_DOWN_2, i++);
			put(WATER_UP_1, i++);
			put(WATER_UP_2, i++);
			put(WATER_LEFT_1, i++);
			put(WATER_LEFT_2, i++);
			put(WATER_RIGHT_1, i++);
			put(WATER_RIGHT_2, i++);
			put(WATER_CORNER_RIGHT_DOWN, i++);
			put(WATER_CORNER_LEFT_DOWN, i++);
			put(WATER_CORNER_RIGHT_UP, i++);
			put(WATER_CORNER_LEFT_UP, i++);
			put(WATER_INCORNER_RIGHT_UP, i++);
			put(WATER_INCORNER_LEFT_UP, i++);
			put(WATER_INCORNER_LEFT_DOWN, i++);
			put(WATER_INCORNER_RIGHT_DOWN, i++);
			put(BRIDGE_HORIZ, i++);
			put(BRIDGE_VERT, i++);
			put(BRIDGE_UP, i++);
			put(BRIDGE_DOWN, i++);
			put(BRIDGE_LEFT, i++);
			put(BRIDGE_RIGHT, i++);
			put(REDUIT_1, i++);
			put(REDUIT_2, i++);
		}
	};
	
	public static int convertTile(TileCode code) {
		return map.get(code);
	}
	
	public static TileCode convertID(int id) {
		return map.getKey(id, true);
	}
}
