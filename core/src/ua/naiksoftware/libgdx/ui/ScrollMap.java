package ua.naiksoftware.libgdx.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Input.Peripheral;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

/**
 * Экран, который содержит ScrollPane и предназначен для синхронизации координат
 * TiledMap с координатами ScrollPane, так, как невозможно напрямую поместить
 * карту в панель. Также реализует зум карты. Имеется возможность добавить любой
 * Actor (Button, etc) на выбранный край экрана.
 *
 * @author Naik
 *
 */
public abstract class ScrollMap implements Screen {

    private final InputMultiplexer im;
    private final OrthogonalTiledMapRenderer mapRenderer;
    private OrthographicCamera mapCamera;
    private final Actor mapHolder;
    private final ScreenViewport stageViewport;
    private final Stage stage;
    private boolean disposed;
    private final ScrollPane scrollPane;
    private final Table root;
    private int screenW, screenH, mapW, mapH;
    private float zoom = 1f;
    private float lastZoom;
    private final float minZoom = 1f / 5f;
    private int padTop, padLeft, padBottom, padRight;
    private Actor actTop, actLeft, actBottom, actRight;
    private int alTop, alLeft, alBottom, alRight;
    private int cellSize;
    private final Batch gameBatch;
    private Array<Sprite> sprites;
    private Rectangle scrRect;
    private final GestureDetector gestureDetector;

    public static enum Side {

        TOP, LEFT, BOTTOM, RIGHT
    }

    protected static final boolean HAVE_BOARD = Gdx.input
            .isPeripheralAvailable(Peripheral.HardwareKeyboard);

    /**
     *
     * @param tileMap ширина, высота и размер тайла берется из первого слоя:
     * TiledMapTileLayer bg = (TiledMapTileLayer) tileMap.getLayers().get(0);
     * cellSize = (int) bg.getTileHeight(); mapW = bg.getWidth(); mapH =
     * bg.getHeight();
     */
    public ScrollMap(TiledMap tileMap) {
        mapCamera = new OrthographicCamera();
        mapRenderer = new OrthogonalTiledMapRenderer(tileMap, 1f/* / cellSize */);
        stageViewport = new ScreenViewport(new OrthographicCamera());
        TiledMapTileLayer bg = (TiledMapTileLayer) tileMap.getLayers().get(0);
        cellSize = (int) bg.getTileHeight();
        mapW = bg.getWidth() * cellSize;
        mapH = bg.getHeight() * cellSize;
        mapHolder = new Actor();
        mapHolder.setSize(mapW, mapH);
        scrollPane = new ScrollPane(mapHolder);
        scrollPane.setOverscroll(false, false);
        root = new Table();
        root.setFillParent(true);
        root.add(scrollPane).fill().expand();
        stage = new Stage(stageViewport);
        stage.addActor(root);
        im = new InputMultiplexer();
        gameBatch = mapRenderer.getSpriteBatch();
        sprites = new Array<Sprite>();
        gestureDetector = new GestureDetector(gestureListener);
    }

    @Override
    public void render(float deltaTime) {
        //Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        if (HAVE_BOARD) {
            processInput(deltaTime);
        }
        mapCamera.position.x = (scrollPane.getScrollX() * zoom + screenW / 2)
                + screenW / 2 * (zoom - 1) - padLeft * zoom;
        mapCamera.position.y = (-scrollPane.getScrollY() * zoom + mapH - screenH / 2)
                - screenH / 2 * (zoom - 1) + padTop * zoom;

        mapCamera.update();
        mapRenderer.setView(mapCamera);
        mapRenderer.render();

        scrRect.x = mapCamera.position.x - screenW / 2 * zoom;
        scrRect.y = mapCamera.position.y - screenH / 2 * zoom;

        gameBatch.begin();
        Rectangle spriteRect;
        for (Sprite s : sprites) {
            spriteRect = s.getBoundingRectangle();
            if (scrRect.contains(spriteRect) || scrRect.overlaps(spriteRect)) {
                s.draw(gameBatch);
            }
        }
        drawGameScreen(gameBatch, deltaTime);
        gameBatch.end();

        stage.act(deltaTime);
        stage.draw();
    }

    private void processInput(float deltaTime) {
        float scrX = 0, scrY = 0;
        if (Gdx.input.isKeyPressed(Keys.RIGHT)) {
            scrX = deltaTime * 1000;
        } else if (Gdx.input.isKeyPressed(Keys.LEFT)) {
            scrX = -deltaTime * 1000;
        }
        if (Gdx.input.isKeyPressed(Keys.DOWN)) {
            scrY = -deltaTime * 1000;
        } else if (Gdx.input.isKeyPressed(Keys.UP)) {
            scrY = deltaTime * 1000;
        }
        scrollPane.setScrollX(scrollPane.getScrollX() + scrX);
        scrollPane.setScrollY(scrollPane.getScrollY() - scrY);
    }

    public void setWidget(Actor a, Side side, int align) {
        root.clear();
        switch (side) {
            case TOP:
                padTop = a == null ? 0 : (int) a.getHeight();
                actTop = a;
                alTop = align;
                break;
            case LEFT:
                padLeft = a == null ? 0 : (int) a.getWidth();
                actLeft = a;
                alLeft = align;
                break;
            case BOTTOM:
                padBottom = a == null ? 0 : (int) a.getHeight();
                actBottom = a;
                alBottom = align;
                break;
            default:
                padRight = a == null ? 0 : (int) a.getWidth();
                actRight = a;
                alRight = align;
        }

        if (actTop != null) {
            root.add(actTop).align(alTop).expandX().fillX();
            root.row();
        }
        if (actLeft != null) {
            root.add(actLeft).align(alLeft).expandY().fillY();
        }

        root.add(scrollPane).fill().expand();

        if (actRight != null) {
            root.add(actRight).align(alRight).expandY().fillY();
        }
        if (actBottom != null) {
            root.row();
            root.add(actBottom).align(alBottom).expandX().fillX();
        }

        root.invalidate();
    }

    @Override
    public void resize(int newX, int newY) {
        mapCamera.setToOrtho(false, newX, newY);
        stageViewport.update(newX, newY, true);
        screenW = newX;
        screenH = newY;
        scrRect = new Rectangle(0, 0, newX, newY);
    }

    @Override
    public void show() {
        im.addProcessor(hardProcessor);
        im.addProcessor(gestureDetector);
        im.addProcessor(stage);
        Gdx.input.setInputProcessor(im);
        Gdx.input.setCatchBackKey(true);
    }

    private void zoomChanged(float zoom) {
        if ((mapW / zoom < (screenW - padLeft - padRight) || mapH / zoom < (screenH - padTop))
                && zoom > this.zoom) {
            return;
        } else if (zoom < minZoom) {
            return;
        }
        mapCamera.zoom = zoom;
        mapHolder.setSize(mapW / zoom, mapH / zoom);
        scrollPane.invalidate();
        scrollPane.setScrollX(scrollPane.getScrollX()
                + (screenW / 2 + scrollPane.getScrollX()) * (this.zoom - zoom)
                / zoom);
        scrollPane.setScrollY(scrollPane.getScrollY()
                + (screenH / 2 + scrollPane.getScrollY()) * (this.zoom - zoom)
                / zoom);
        scrRect.width = screenW * zoom;
        scrRect.height = screenH * zoom;

        this.zoom = zoom;
    }

    @Override
    public void hide() {
        // TODO: Implement this method
    }

    @Override
    public void pause() {
        // TODO: Implement this method
    }

    @Override
    public void resume() {
        // TODO: Implement this method
    }

    protected abstract void hardKeyUp(int key);

    protected abstract void tapMap(Vector2 tapCoords);

    protected abstract void drawGameScreen(Batch batch, float deltaTime);

    private GestureDetector.GestureListener gestureListener = new GestureDetector.GestureListener() {

        @Override
        public boolean touchDown(float p1, float p2, int p3, int p4) {
            lastZoom = 1;
            return false;
        }

        @Override
        public boolean zoom(float lastDist, float newDist) {
            if (Math.abs(lastZoom - 1) < 0.001) {
                lastZoom = zoom;
            }
            zoomChanged(lastDist / newDist * lastZoom);
            return false;
        }

        @Override
        public boolean tap(float x, float y, int count, int button) {
            if (x > padLeft && x < screenW - padRight && y > padTop
                    && y < screenH - padBottom) {
                Vector3 vec3 = new Vector3(x, y, 0);
                mapCamera.unproject(vec3);
                // vec3.set(vec3.x, mapH - vec3.y, 0);
                if (vec3.x < mapW && vec3.y > 0) {
                    vec3.scl(1f / cellSize);
                    Vector2 vec2 = new Vector2((int) vec3.x, (int) vec3.y);
                    tapMap(vec2);
                }
            }
            return false;
        }

        @Override
        public boolean longPress(float x, float y) {
            // TODO Auto-generated method stub
            return false;
        }

        @Override
        public boolean fling(float velocityX, float velocityY, int button) {
            return false;
        }

        @Override
        public boolean pan(float x, float y, float deltaX, float deltaY) {
            // TODO Auto-generated method stub
            return false;
        }

        @Override
        public boolean panStop(float x, float y, int pointer, int button) {
            // TODO Auto-generated method stub
            return false;
        }

        @Override
        public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2,
                Vector2 pointer1, Vector2 pointer2) {
            // TODO Auto-generated method stub
            return false;
        }
    };

    private final HardInputProcessor hardProcessor = new HardInputProcessor() {
        @Override
        public boolean keyUp(int key) {
            hardKeyUp(key);
            return false;
        }

        @Override
        public boolean scrolled(int amount) {
            zoomChanged(zoom * (amount < 0 ? 0.99f : 1.01f));
            return true;
        }
    };

    protected Stage getStage() {
        return stage;
    }

    protected int mapCellSize() {
        return cellSize;
    }

    public void setSprites(Array<Sprite> sprites) {
        this.sprites = sprites;
    }

    public void setBlockInput(boolean b) {
        if (b) {
            Gdx.input.setInputProcessor(stage);
        } else {
            Gdx.input.setInputProcessor(im);
        }
    }

    @Override
    public void dispose() {
        if (!disposed) {
            stage.dispose();
            disposed = true;
        }
    }
}
