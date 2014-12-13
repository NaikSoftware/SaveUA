package ua.naiksoftware.waronline;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Input.Peripheral;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
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

	private MapHolder mapHolder;
	private OrthographicCamera mapCamera;
	private ScreenViewport stageViewport;
	private Stage stage;
	private ScrollPane scrollPane;
	private Table root;
	private int screenW, screenH, mapW, mapH;
	private float zoom = 1f;
	private float lastZoom;
	private int padTop, padLeft, padBottom, padRight;

	public static enum Side {
		TOP, LEFT, BOTTOM, RIGHT
	}

	protected static final boolean HAVE_BOARD = Gdx.input
			.isPeripheralAvailable(Peripheral.HardwareKeyboard);

	public ScrollMap(TiledMap tileMap) {
		mapCamera = new OrthographicCamera();
		stageViewport = new ScreenViewport(new OrthographicCamera());
		mapHolder = new MapHolder(mapCamera, tileMap);
		mapW = (int) mapHolder.getWidth();
		mapH = (int) mapHolder.getHeight();
		scrollPane = new ScrollPane(mapHolder);
		scrollPane.setOverscroll(false, false);
		root = new Table();
		root.setFillParent(true);
		root.add(scrollPane).fill().pad(padTop, padLeft, padBottom, padRight)
				.expand();
		stage = new Stage(stageViewport);
		stage.addActor(root);
	}

	@Override
	public void render(float deltaTime) {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		if (HAVE_BOARD) {
			processInput(deltaTime);
		}
		mapCamera.position.x = (scrollPane.getScrollX() * zoom + screenW / 2)
				+ screenW / 2 * (zoom - 1) - padLeft * zoom;
		mapCamera.position.y = (-scrollPane.getScrollY() * zoom + mapH - screenH / 2)
				- screenH / 2 * (zoom - 1) + padTop * zoom;

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

	public void addWidget(Actor a, Side side, int align) {
		root.clear();
		if (side == Side.TOP) {
			padTop = (int) a.getHeight();
			root.add(a).align(align);
			root.row();
			root.add(scrollPane).fill().expand();
		} else if (side == Side.LEFT) {
			padLeft = (int) a.getWidth();
			root.add(a).align(align);
			root.add(scrollPane).fill().expand();
		} else if (side == Side.BOTTOM) {
			padBottom = (int) a.getHeight();
			root.add(scrollPane).fill().expand();
			root.row();
			root.add(a).align(align);
		} else {
			padRight = (int) a.getWidth();
			root.add(scrollPane).fill().expand();
			root.add(a).align(align);
		}
		root.invalidate();
	}

	public void resize(int newX, int newY) {
		mapCamera.setToOrtho(false, newX, newY);
		stageViewport.update(newX, newY, true);
		screenW = newX;
		screenH = newY;
	}

	@Override
	public void show() {
		InputMultiplexer im = new InputMultiplexer();
		if (HAVE_BOARD) {
			im.addProcessor(hardProcessor);
		}
		im.addProcessor(new GestureDetector(gestureListener));
		im.addProcessor(stage);
		Gdx.input.setInputProcessor(im);
	}

	private void zoomChanged(float zoom) {
		if ((mapW / zoom < (screenW - padLeft - padRight) || mapH / zoom < (screenH
				- padTop - padBottom))
				&& zoom > this.zoom) {
			return;
		} else if ((mapHolder.getWidth() > Short.MAX_VALUE || mapHolder
				.getHeight() > Short.MAX_VALUE) && zoom < this.zoom) {
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

	private GestureDetector.GestureListener gestureListener = new GestureDetector.GestureListener() {

		@Override
		public boolean touchDown(float p1, float p2, int p3, int p4) {
			lastZoom = 1;
			return false;
		}

		@Override
		public boolean zoom(float lastDist, float newDist) {
			if (Math.abs(lastZoom - 1) < 0.001)
				lastZoom = zoom;
			zoomChanged(lastDist / newDist * lastZoom);
			return false;
		}

		@Override
		public boolean tap(float x, float y, int count, int button) {
			// TODO Auto-generated method stub
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

	private HardInputProcessor hardProcessor = new HardInputProcessor() {
		@Override
		public boolean keyUp(int key) {
			hardKeyUp(key);
			return false;
		}

		public boolean scrolled(int amount) {
			if (amount != 0) { // Иногда прилетает 0, баг?
				zoomChanged(zoom * (amount < 0 ? 0.99f : 1.01f));
			}
			return false;
		}
	};

	@Override
	public void dispose() {
		stage.dispose();
	}

	private static class MapHolder extends Actor {

		private TiledMapRenderer renderer;
		private OrthographicCamera camera;
		private int mapW, mapH;
		private int cellSize;

		MapHolder(OrthographicCamera camera, TiledMap map) {
			this.camera = camera;
			mapW = map.getProperties().get(MapUtils.CELL_W_PROP, Integer.class);
			mapH = map.getProperties().get(MapUtils.CELL_H_PROP, Integer.class);
			cellSize = map.getProperties().get(MapUtils.CELL_SIZE,
					Integer.class);

			renderer = new OrthogonalTiledMapRenderer(map, 1f/* / cellSize */);
			setSize(mapW * cellSize, mapH * cellSize);
		}

		@Override
		public void draw(Batch batch, float alphaParent) {
			camera.update();
			renderer.setView(camera);
			renderer.render();
		}
	}
}
