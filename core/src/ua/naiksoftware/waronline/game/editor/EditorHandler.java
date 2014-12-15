package ua.naiksoftware.waronline.game.editor;

import ua.naiksoftware.waronline.MapUtils;
import ua.naiksoftware.waronline.ScrollMap;
import ua.naiksoftware.waronline.screenmanager.Manager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;

public class EditorHandler extends ScrollMap {

	private Manager manager;
	private TiledMap map;
	private SpriteBatch batch;
	private BitmapFont font;
	
	public EditorHandler(Manager manager, TiledMap map) {
		super(map);
		this.manager = manager;
		this.map = map;
		font = new BitmapFont();
		batch = new SpriteBatch();
	}

	@Override
	public void render(float deltaTime) {
		super.render(deltaTime);
		
		batch.begin();
		font.draw(batch, "FPS: " + Gdx.graphics.getFramesPerSecond(), 10, 20);
		batch.end();
	}
	
	@Override
	protected void hardKeyUp(int key) {
		if (key == Keys.BACKSPACE) {
			MapUtils.saveMap(map);
			dispose();
			manager.showMenu();
		}
	}

	@Override
	public void show() {
		super.show();
	}
	
	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
	}

	@Override
	public void dispose() {
		super.dispose();
		map.dispose();
	}
}
