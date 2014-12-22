package ua.naiksoftware.waronline.screenmanager;

import ua.naiksoftware.waronline.MapUtils;
import ua.naiksoftware.waronline.SplashScreen;
import ua.naiksoftware.waronline.game.GameHandler;
import ua.naiksoftware.waronline.game.editor.EditorHandler;
import ua.naiksoftware.waronline.res.Lng;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class AndroidManager extends Manager {

	public static enum LaunchMode {
		PLAY, PLAY_ONLINE, SPLASH_SCREEEN, MAP_EDITOR
	}

	private LaunchMode mode;
	private String pathToMap, mapName;
	private boolean internalMap;
	private int wMap, hMap;
	private Skin skin;

	public AndroidManager(LaunchMode mode, Lng lng) {
		this(mode, lng, null, false);
	}

	public AndroidManager(LaunchMode mode, Lng lng, int wMap, int hMap,
			String mapName) {
		this(mode, lng, null, false);
		this.wMap = wMap;
		this.hMap = hMap;
		this.mapName = mapName;
	}

	public AndroidManager(LaunchMode mode, Lng lng, String pathToMap,
			boolean internalMap) {
		super(lng);
		this.mode = mode;
		this.pathToMap = pathToMap;
		this.internalMap = internalMap;
	}

	@Override
	public void create() {
		super.create();
		skin = new Skin(Gdx.files.internal("skin/uiskin.json"));
		BitmapFont font = skin.getFont("default-font");
		font.setScale((Math.max(Gdx.graphics.getHeight(),
				Gdx.graphics.getWidth()) / 25)
				/ font.getLineHeight());

		TiledMap map;
		switch (mode) {
		case SPLASH_SCREEEN:
			setScreen(new SplashScreen(this));
			break;
		case PLAY:
			map = MapUtils.loadTileMap(pathToMap, internalMap);
			setScreen(new GameHandler(this, map));
			break;
		case PLAY_ONLINE:
			map = MapUtils.loadTileMap(pathToMap, internalMap);
			setScreen(new GameHandler(this, map));
			break;
		case MAP_EDITOR:
			if (pathToMap == null) {
				map = MapUtils.genVoidMap(wMap, hMap, mapName);
				map.getProperties().put(MapUtils.MAP_NAME_PROP, mapName);
			} else {
				map = MapUtils.loadTileMap(pathToMap, internalMap);
			}
			setScreen(new EditorHandler(this, map));
			break;
		default:
			break;
		}
	}

	@Override
	public void showMenu() {
		// Back to native menu
		Gdx.app.exit();
	}

	@Override
	public Skin getSkin() {
		return skin;
	}
}
