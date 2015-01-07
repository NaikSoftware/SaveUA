package ua.naiksoftware.waronline.screenmanager;

import ua.naiksoftware.waronline.res.Lng;
import ua.naiksoftware.waronline.res.ResKeeper;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

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

    public abstract Skin getSkin();

    public abstract BitmapFont getTitleFont();

    public void freeTitleFont() {
        BitmapFont f = getTitleFont();
        if (f != null) {
            f.dispose();
            f = null;
        }
    }

    /**
     * Если верить документации, то этот метод вызывается всегда при закрытии
     * программы, поэтому чистим здесь видеопамять и др.
     */
    @Override
    public void dispose() {
        getScreen().dispose();
        ResKeeper.disposeAll();
        freeTitleFont();
        getSkin().dispose();
        super.dispose();
    }
}
