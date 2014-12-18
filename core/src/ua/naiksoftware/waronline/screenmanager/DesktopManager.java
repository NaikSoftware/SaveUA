package ua.naiksoftware.waronline.screenmanager;

import ua.naiksoftware.waronline.GdxMenu;
import ua.naiksoftware.waronline.SplashScreen;
import ua.naiksoftware.waronline.res.Lng;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class DesktopManager extends Manager {

	private Skin skin;
	private BitmapFont titleFont;

	public DesktopManager(Lng lng) {
		super(lng);
	}

	@Override
	public void create() {
		super.create();
		skin = new Skin(Gdx.files.internal("skins/uiskin.json"));
		BitmapFont font = skin.getFont("default-font");
		float scale = (Math.min(Gdx.graphics.getHeight(),
				Gdx.graphics.getWidth()) / 25)
				/ font.getLineHeight();
		if (scale < 1) {
			font.setScale(scale);
		}
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

	public BitmapFont getTitleFont() {
		if (titleFont == null) {
			titleFont = new BitmapFont(
					Gdx.files.internal("skins/albionic_72.fnt"));
		}
		return titleFont;
	}

	public void freeTitleFont() {
		if (titleFont != null) {
			titleFont.dispose();
			titleFont = null;
		}
	}

	@Override
	public void dispose() {
		freeTitleFont();
		super.dispose();
	}
}
