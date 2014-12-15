package ua.naiksoftware.waronline.screenmanager;

import ua.naiksoftware.waronline.res.Lng;
import ua.naiksoftware.waronline.res.ResKeeper;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Application.ApplicationType;

public abstract class Manager extends Game {

	public Lng lng;
	public boolean desktop;

	public Manager(Lng lng) {
		this.lng = lng;
	}

	@Override
	public void create() {
		desktop = Gdx.app.getType() == ApplicationType.Desktop;
	}

	public abstract void showMenu();

	/**
	 * Если верить документации, то этот метод вызывается всегда при закрытии
	 * программы, поэтому чистим здесь видеопамять и др.
	 */
	@Override
	public void dispose() {
		getScreen().dispose();
		ResKeeper.disposeAll();
		super.dispose();
	}
}
