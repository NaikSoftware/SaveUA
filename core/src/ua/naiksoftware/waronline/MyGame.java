package ua.naiksoftware.waronline;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import ua.naiksoftware.waronline.game.GameHandler;
import ua.naiksoftware.waronline.res.Lng;
import ua.naiksoftware.waronline.res.ResKeeper;

public class MyGame extends Game {

	private MyGame() {
	}

	private static final MyGame game = new MyGame();

	public static MyGame getInstance() {
		return game;
	}
	
	public static enum LaunchMode {
		PLAY, PLAY_ONLINE
	}

	public LaunchMode mode;
	public Lng lng;
	public Skin skin;
	private boolean desktop;

	@Override
	public void create() {
		desktop = Gdx.app.getType() == ApplicationType.Desktop;
		if (desktop) {
			setScreen(new SplashScreen());
		} else {
			setScreen(new GameHandler("atlas/tile_map.atlas"));
		}
	}

	public void showMenu() {
		if (desktop) {
			if (skin == null) {
				skin = new Skin(Gdx.files.internal("skins/uiskin.json"));
			}
			setScreen(new GdxMenu());
		} else {
			Gdx.app.exit(); // закрыть Gdx, и вернуться к нативному меню, если такое реализовано на данной платформе (напр. на Android)
		}
	}

	/**
	 * Если верить документации, то этот метод вызывается
	 * всегда при закрытии программы, поэтому чистим здесь видеопамять и др.
	 */
	@Override
	public void dispose() {
		super.dispose();
		getScreen().dispose();
		ResKeeper.disposeAll();
	}
}
