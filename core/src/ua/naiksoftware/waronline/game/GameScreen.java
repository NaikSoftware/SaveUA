package ua.naiksoftware.waronline.game;

import ua.naiksoftware.waronline.map.GameMap;
import ua.naiksoftware.libgdx.ui.ScrollMap;
import ua.naiksoftware.waronline.res.ResKeeper;
import ua.naiksoftware.waronline.res.id.AtlasId;
import ua.naiksoftware.waronline.screenmanager.Manager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

public class GameScreen extends ScrollMap {

    private final Manager manager;
    private final TiledMap tileMap;
    private final GameMap gameMap;
    private final BitmapFont font;
    private final SpriteBatch batch;
    private int screenW, screenH, mapW, mapH;

    public GameScreen(Manager manager, GameMap gameMap) {
        super(gameMap.getTiledMap());
        this.manager = manager;
        this.gameMap = gameMap;
        this.tileMap = gameMap.getTiledMap();

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
    protected void drawGameScreen(Batch batch, float deltaTime) {
        // TODO Auto-generated method stub

    }

    @Override
    protected void tapMap(Vector2 tapCoords) {
        // TODO Auto-generated method stub

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
        if (key == Keys.BACKSPACE || key == Keys.BACK) {
            dispose();
            manager.showMenu();
        }
    }

    @Override
    public void dispose() {
        super.dispose();
        tileMap.dispose();
        ResKeeper.dispose(AtlasId.MAP_TILES);
    }
}
