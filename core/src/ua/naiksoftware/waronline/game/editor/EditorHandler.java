package ua.naiksoftware.waronline.game.editor;

import ua.naiksoftware.waronline.ScrollMap;

import com.badlogic.gdx.maps.tiled.TiledMap;

public class EditorHandler extends ScrollMap {

	private EditorReceiver editorReceiver;
	private TiledMap map;
	
	public EditorHandler(EditorReceiver editorReceiver, TiledMap map) {
		super(map);
		this.editorReceiver = editorReceiver;
		this.map = map;
	}
	
	@Override
	protected void hardKeyUp(int key) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
	}
}
