package ua.naiksoftware.waronline.screenmanager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import ua.naiksoftware.waronline.GdxMenu;
import ua.naiksoftware.waronline.SplashScreen;
import ua.naiksoftware.waronline.game.GameScreen;
import ua.naiksoftware.waronline.map.GameMap;
import ua.naiksoftware.waronline.map.MapEntry;
import ua.naiksoftware.waronline.map.MapUtils;
import ua.naiksoftware.waronline.map.editor.EditorScreen;
import ua.naiksoftware.waronline.res.Lng;

public class AndroidManager extends Manager {

    public static enum LaunchMode {

        PLAY, PLAY_ONLINE, SPLASH_SCREEEN, MAP_EDITOR, GDX_MENU
		}

    private LaunchMode mode;
    private String mapName;
    private MapEntry entry;
    private int wMap, hMap;
    private Skin skin;
	private boolean gdxMenu;
	private BitmapFont titleFont;

	/** Заставка */
    public AndroidManager(LaunchMode mode, Lng lng) {
        this(mode, lng, null, null);
		gdxMenu = mode == LaunchMode.GDX_MENU;
    }

	/** Старт игры */
	public AndroidManager(LaunchMode mode, Lng lng, MapEntry entry) {
        this(mode, lng, entry, null);
    }

	/** Создание новой карты */
    public AndroidManager(LaunchMode mode, Lng lng, int wMap, int hMap,
						  String mapName) {
        this(mode, lng, null, mapName);
        this.wMap = wMap;
        this.hMap = hMap;
    }

	/** Редактирование карты */
    public AndroidManager(LaunchMode mode, Lng lng, MapEntry entry, String mapName) {
        super(lng);
        this.mode = mode;
        this.entry = entry;
		this.mapName = mapName;
    }

    @Override
    public void create() {
        super.create();
        skin = new Skin(Gdx.files.internal("skin/uiskin.json"));
        BitmapFont font = skin.getFont("default-font");
        font.setScale((Math.max(Gdx.graphics.getHeight(),
								Gdx.graphics.getWidth()) / 25)
					  / font.getLineHeight());

        GameMap gameMap;
        switch (mode) {
            case SPLASH_SCREEEN:
			case GDX_MENU:
                setScreen(new SplashScreen(this));
                break;
            case PLAY:
                gameMap = MapUtils.loadTileMap(entry);
                setScreen(new GameScreen(this, gameMap));
                break;
            case PLAY_ONLINE:
                gameMap = MapUtils.loadTileMap(entry);
                setScreen(new GameScreen(this, gameMap));
                break;
            case MAP_EDITOR:
                if (entry == null) {
                    TiledMap map = MapUtils.genVoidMap(wMap, hMap, mapName);
					setScreen(new EditorScreen(this, map));
                } else {
                    gameMap = MapUtils.loadTileMap(entry);
					gameMap.getTiledMap().getProperties().put(MapUtils.MAP_NAME_PROP, mapName);
                    setScreen(new EditorScreen(this, gameMap));
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void showMenu() {
		if (gdxMenu) {
			setScreen(new GdxMenu(this));
		} else {
			// Back to native menu
			Gdx.app.exit();
		}
    }

    @Override
    public Skin getSkin() {
        return skin;
    }
	
	@Override
	public BitmapFont getTitleFont() {
        if (titleFont == null) {
            titleFont = new BitmapFont(Gdx.files.internal("skin/albionic_72.fnt"));
            float scale = Math.min(Gdx.graphics.getWidth(), Gdx.graphics.getHeight())
				/ 10 / titleFont.getLineHeight();
            if (scale < 1.3) {
                titleFont.setScale(scale);
            }
		}
		return titleFont ;
	}
}
