package ua.naiksoftware.waronline.game.editor;

import ua.naiksoftware.waronline.MyGame;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.maps.tiled.TiledMap;

/**
 * Класс для запуска редактора карт напрямую из платформы, в обход GdxMenu, т.к.
 * меню проще реализовать нативное.
 * 
 * @author Naik
 */
public class PlatformDirectEditorRunner extends Game implements EditorReceiver {

	private TiledMap map;

	public PlatformDirectEditorRunner(TiledMap map) {
		this.map = map;
	}

	@Override
	public void create() {
		setScreen(new EditorHandler(this, map));
	}

	@Override
	public void onEditMapComplete(TiledMap map, EditorHandler editor) {
		map.dispose();
		editor.dispose();
		MyGame.getInstance().showMenu();
	}
}
