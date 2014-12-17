package ua.naiksoftware.waronline.screenmanager;

import ua.naiksoftware.waronline.GdxMenu;
import ua.naiksoftware.waronline.SplashScreen;
import ua.naiksoftware.waronline.res.Lng;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class DesktopManager extends Manager {

	private Skin skin;

	public DesktopManager(Lng lng) {
		super(lng);
	}

	@Override
	public void create() {
		super.create();
		skin = new Skin(Gdx.files.internal("skins/uiskin.json"));
		setScreen(new SplashScreen(this));
	}

	@Override
	public void showMenu() {
		setScreen(new GdxMenu(this));
	}

	@Override
	public Skin getSkin() {
		return skin;
	}
}
