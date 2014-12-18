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
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.utils.ArrayMap;

public class EditorHandler extends ScrollMap {

	private Manager manager;
	private TiledMap map;
	private SpriteBatch batch;
	private BitmapFont font;
	private Label headLabel;
	private TextButton btnBack, btnNext;

	private enum State {
		TILEMAP, OBJECTS, FREE_UNITS, GAMERS_BASES
	}

	private State state;

	public EditorHandler(Manager manager, TiledMap map) {
		super(map);
		this.manager = manager;
		this.map = map;
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
		head.setHeight(head.getMinHeight());
		head.setBackground(manager.getSkin().getDrawable("default-pane"));
		setWidget(head, Side.TOP, Align.center);
	}

	@Override
	public void render(float deltaTime) {
		super.render(deltaTime);

		batch.begin();
		font.draw(batch, "FPS: " + Gdx.graphics.getFramesPerSecond(), 10, 20);
		batch.end();
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
			scrollPane.setHeight(scrollPane.getMinHeight());
			setWidget(scrollPane, Side.BOTTOM, Align.left);
		}

		this.state = state;
	}

	private ArrayMap<TileCode, Tile> initTiles() {
		ArrayMap<TileCode, Tile> tiles = new ArrayMap<TileCode, Tile>();

		putTile(tiles, TileCode.GRASS);
		putTile(tiles, TileCode.TREES);
		putTile(tiles, TileCode.TREES_EDGE_DOWN);
		putTile(tiles, TileCode.TREES_EDGE_RIGHT);

		return tiles;
	}

	private void putTile(ArrayMap<TileCode, Tile> tiles, TileCode code) {
		tiles.put(code, new Tile(MapUtils.getCell(code, false).getTile()
				.getTextureRegion()));
	}

	@Override
	public void dispose() {
		super.dispose();
		map.dispose();
		ResKeeper.dispose(AtlasId.MAP_TILES);
	}
}
