package ua.naiksoftware.waronline.game;

import ua.naiksoftware.waronline.MapUtils;
import ua.naiksoftware.waronline.MyGame;
import ua.naiksoftware.waronline.ScrollMap;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.Align;

public class GameHandler extends ScrollMap {

	private static TiledMap tileMap;
	private BitmapFont font;
	private SpriteBatch batch;
	private int screenW, screenH, mapW, mapH;

	public GameHandler(String pathToMap) {
		super(tileMap = MapUtils.loadTileMap(pathToMap));
		font = new BitmapFont();
		batch = new SpriteBatch();
		Label.LabelStyle style = new Label.LabelStyle();
		style.font = font;
		Label label = new Label("Test label", style);
		addWidget(label, Side.TOP, Align.center);
	}

	@Override
	public void render(float deltaTime) {
		super.render(deltaTime);
		batch.begin();
		font.draw(batch, "FPS: " + Gdx.graphics.getFramesPerSecond(), 10, 20);
		batch.end();
	}

	@Override
	public void resize(int newX, int newY) {
		super.resize(newX, newY);
		screenW = newX;
		screenH = newY;
	}

	@Override
	public void show() {
		super.show();
	}

	private ChangeListener btnListener = new ChangeListener() {

		@Override
		public void changed(ChangeListener.ChangeEvent ev, Actor a) {
		}
	};

	@Override
	protected void hardKeyUp(int key) {
		if (key == Keys.BACKSPACE) {
			dispose();
			MyGame.getInstance().showMenu();
		}
	}

	@Override
	public void dispose() {
		super.dispose();
		tileMap.dispose();
		MapUtils.disposeTileAtlas();
	}
}
