package ua.naiksoftware.waronline.screenmanager;

import ua.naiksoftware.waronline.MapUtils;
import ua.naiksoftware.waronline.SplashScreen;
import ua.naiksoftware.waronline.game.GameHandler;
import ua.naiksoftware.waronline.game.editor.EditorHandler;
import ua.naiksoftware.waronline.res.Lng;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.tiled.TiledMap;

public class AndroidManager extends Manager {

	public static enum LaunchMode {
		PLAY, PLAY_ONLINE, SPLASH_SCREEEN, MAP_EDITOR
	}

	private LaunchMode mode;
	private String pathToMap, mapName;
	private boolean internalMap;
	private int wMap, hMap;

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

}
