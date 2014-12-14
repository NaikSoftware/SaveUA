package ua.naiksoftware.waronline.game.editor;

import com.badlogic.gdx.maps.tiled.TiledMap;

public interface EditorReceiver {
	void onEditMapComplete(TiledMap map, EditorHandler editor);
}
