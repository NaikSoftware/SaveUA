package ua.naiksoftware.waronline.game.editor;

import ua.naiksoftware.waronline.MapUtils;
import ua.naiksoftware.waronline.ScrollMap;
import ua.naiksoftware.waronline.game.TileCode;
import ua.naiksoftware.waronline.res.ResKeeper;
import ua.naiksoftware.waronline.res.Words;
import ua.naiksoftware.waronline.res.id.AtlasId;
import ua.naiksoftware.waronline.screenmanager.Manager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.ArrayMap;

public class EditorHandler extends ScrollMap {

	private Manager manager;
	private TiledMap map;
	private SpriteBatch batch;
	private BitmapFont font;
	private Label headLabel;
	private TextButton btnBack, btnNext;
	private Tile currTile;
	private Vector2 tapCoords = new Vector2();
	private TiledMapTileLayer tileLayer;

	private enum State {
		TILEMAP, OBJECTS, FREE_UNITS, GAMERS_BASES
	}

	private State state;

	public EditorHandler(Manager manager, TiledMap map) {
		super(map);
		this.manager = manager;
		this.map = map;
		this.tileLayer = (TiledMapTileLayer) map.getLayers().get(0);
		font = new BitmapFont();
		batch = new SpriteBatch();

		btnBack = new TextButton(null, manager.getSkin());
		btnNext = new TextButton(null, manager.getSkin());
		headLabel = new Label(null, manager.getSkin());

		setUI(State.TILEMAP);

		Table head = new Table();
		head.add(btnBack).left();
		head.add(headLabel).expandX().align(Align.center);
		head.add(btnNext).right();
		head.setBackground(manager.getSkin().getDrawable("default-pane"));
		head.pack();
		setWidget(head, Side.TOP, Align.center);
	}

	@Override
	public void render(float deltaTime) {
		super.render(deltaTime);

		batch.begin();
		font.draw(batch, "Tap: " + tapCoords.x + ", " + tapCoords.y, 10, 40);
		font.draw(batch, "FPS: " + Gdx.graphics.getFramesPerSecond(), 10, 20);
		batch.end();
	}

	private ChangeListener btnListener = new ChangeListener() {

		@Override
		public void changed(ChangeListener.ChangeEvent ev, Actor a) {
		}
	};

	@Override
	protected void tapMap(Vector2 tapCoords) {
		this.tapCoords = tapCoords;
		tileLayer.setCell((int) tapCoords.x, (int) tapCoords.y,
				currTile.getInsertCell());
	}

	@Override
	protected void hardKeyUp(int key) {
		if (key == Keys.BACKSPACE) {
			MapUtils.saveMap(map);
			dispose();
			manager.showMenu();
		}
	}

	private void setUI(State state) {

		if (state == State.TILEMAP) {
			btnBack.setText(manager.lng.get(Words.BACK));
			btnNext.setText(manager.lng.get(Words.NEXT));
			headLabel.setText(manager.lng.get(Words.BUILD_MAP));

			Table tilesTable = new Table();
			tilesTable.setBackground(manager.getSkin().getDrawable(
					"default-pane"));

			ArrayMap<TileCode, Tile> tiles = initTiles();
			for (int i = 0, n = tiles.size; i < n; i++) {
				tilesTable.add(tiles.getValueAt(i));
			}
			ScrollPane scrollPane = new ScrollPane(tilesTable);
			scrollPane.pack();
			setWidget(scrollPane, Side.BOTTOM, Align.left);
		}

		this.state = state;
	}

	private ArrayMap<TileCode, Tile> initTiles() {
		ArrayMap<TileCode, Tile> tiles = new ArrayMap<TileCode, Tile>();

		putTile(tiles, TileCode.GRASS);
		currTile = tiles.firstValue();
		currTile.setSelected(true);

		putTile(tiles, TileCode.TREES);
		putTile(tiles, TileCode.TREES_EDGE_DOWN);
		putTile(tiles, TileCode.TREES_EDGE_LEFT);
		putTile(tiles, TileCode.TREES_EDGE_UP);
		putTile(tiles, TileCode.TREES_EDGE_RIGHT);

		putTile(tiles, TileCode.TREES_CORNER_RIGHT_DOWN);
		putTile(tiles, TileCode.TREES_CORNER_LEFT_DOWN);
		putTile(tiles, TileCode.TREES_CORNER_RIGHT_UP);
		putTile(tiles, TileCode.TREES_CORNER_LEFT_UP);

		putTile(tiles, TileCode.TREES_INCORNER_RIGHT_DOWN);
		putTile(tiles, TileCode.TREES_INCORNER_LEFT_DOWN);

		putTile(tiles, TileCode.ROAD_HORIZ);
		putTile(tiles, TileCode.ROAD_VERT);
		putTile(tiles, TileCode.ROAD_INTERSECT);
		putTile(tiles, TileCode.ROAD_CORNER_LEFT_DOWN);
		putTile(tiles, TileCode.ROAD_CORNER_RIGHT_DOWN);
		putTile(tiles, TileCode.ROAD_CORNER_LEFT_UP);
		putTile(tiles, TileCode.ROAD_CORNER_RIGHT_UP);
		putTile(tiles, TileCode.ROAD_END_DOWN);
		putTile(tiles, TileCode.ROAD_END_LEFT);
		putTile(tiles, TileCode.ROAD_END_UP);
		putTile(tiles, TileCode.ROAD_END_RIGHT);

		putTile(tiles, TileCode.WATER);
		putTile(tiles, TileCode.WATER_DOWN_1);
		putTile(tiles, TileCode.WATER_DOWN_2);
		putTile(tiles, TileCode.WATER_UP_1);
		putTile(tiles, TileCode.WATER_UP_2);
		putTile(tiles, TileCode.WATER_LEFT_1);
		putTile(tiles, TileCode.WATER_LEFT_2);
		putTile(tiles, TileCode.WATER_RIGHT_1);
		putTile(tiles, TileCode.WATER_RIGHT_2);
		putTile(tiles, TileCode.WATER_CORNER_LEFT_DOWN);
		putTile(tiles, TileCode.WATER_CORNER_RIGHT_DOWN);
		putTile(tiles, TileCode.WATER_CORNER_LEFT_UP);
		putTile(tiles, TileCode.WATER_CORNER_RIGHT_UP);
		putTile(tiles, TileCode.WATER_INCORNER_RIGHT_DOWN);
		putTile(tiles, TileCode.WATER_INCORNER_LEFT_DOWN);
		putTile(tiles, TileCode.WATER_INCORNER_RIGHT_UP);
		putTile(tiles, TileCode.WATER_INCORNER_LEFT_UP);

		putTile(tiles, TileCode.BRIDGE_VERT);
		putTile(tiles, TileCode.BRIDGE_HORIZ);
		putTile(tiles, TileCode.BRIDGE_UP);
		putTile(tiles, TileCode.BRIDGE_DOWN);
		putTile(tiles, TileCode.BRIDGE_LEFT);
		putTile(tiles, TileCode.BRIDGE_RIGHT);

		putTile(tiles, TileCode.REDUIT_1);
		putTile(tiles, TileCode.REDUIT_2);

		return tiles;
	}

	private void putTile(ArrayMap<TileCode, Tile> tiles, TileCode code) {
		Cell cell = MapUtils.getCell(code, false);
		Tile tile = new Tile(cell);
		tile.addListener(tileListener);
		tiles.put(code, tile);
	}

	private ClickListener tileListener = new ClickListener() {

		@Override
		public void clicked(InputEvent event, float x, float y) {
			currTile.setSelected(false);
			currTile = (Tile) event.getTarget();
			currTile.setSelected(true);
		}
	};

	@Override
	public void dispose() {
		super.dispose();
		map.dispose();
		ResKeeper.dispose(AtlasId.MAP_TILES);
		ResKeeper.dispose(AtlasId.EDITOR_IMAGES);
	}
}
