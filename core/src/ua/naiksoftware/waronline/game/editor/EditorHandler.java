package ua.naiksoftware.waronline.game.editor;

import ua.naiksoftware.waronline.MapUtils;
import ua.naiksoftware.waronline.ScrollMap;
import ua.naiksoftware.waronline.game.ObjCode;
import ua.naiksoftware.waronline.game.TileCode;
import ua.naiksoftware.waronline.res.MetaData;
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
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent;
import com.badlogic.gdx.utils.ArrayMap;

public class EditorHandler extends ScrollMap {

	private Manager manager;
	private TiledMap map;
	private SpriteBatch batch;
	private BitmapFont font;
	private Label headLabel;
	private TextButton btnBack, btnNext;
	private EditCell currTile;
	private Vector2 tapCoords = new Vector2();
	private TiledMapTileLayer tileLayer;
	private TiledMapTileLayer[] objLayers;
	private EditCell currObj;
	private int cellSize;

	private enum State {
		TILEMAP, OBJECTS, FREE_UNITS, GAMERS_BASES
	}

	private State state;

	public EditorHandler(Manager manager, TiledMap map) {
		super(map);
		this.manager = manager;
		this.map = map;
		this.tileLayer = (TiledMapTileLayer) map.getLayers().get(0);
		objLayers = new TiledMapTileLayer[5];
		for (int i = 0; i < 5; i++) {
			objLayers[i] = (TiledMapTileLayer) map.getLayers().get(i + 1);
		}
		cellSize = mapCellSize();
		font = new BitmapFont();
		batch = new SpriteBatch();

		btnBack = new TextButton(manager.lng.get(Words.BACK), manager.getSkin());
		btnNext = new TextButton(manager.lng.get(Words.NEXT), manager.getSkin());
		headLabel = new Label(null, manager.getSkin());
		btnBack.addListener(btnListener);
		btnNext.addListener(btnListener);

		setUI(State.TILEMAP);

		Table head = new Table();
		head.add(btnBack).left();
		head.add(headLabel).expandX().align(Align.center);
		head.add(btnNext).right();
		head.setBackground(manager.getSkin().getDrawable("default-pane"));
		head.pack();
		setWidget(head, Side.TOP, Align.center);
	}

	int x, y, rang;

	@Override
	public void render(float deltaTime) {
		super.render(deltaTime);

		batch.begin();
		font.draw(batch, "Sel: " + x + ", " + y + " rang: " + rang, 10, 60);
		font.draw(batch, "Tap: " + tapCoords.x + ", " + tapCoords.y, 10, 40);
		font.draw(batch, "FPS: " + Gdx.graphics.getFramesPerSecond(), 10, 20);
		batch.end();
	}

	private ChangeListener btnListener = new ChangeListener() {

		@Override
		public void changed(ChangeListener.ChangeEvent ev, Actor a) {
			if (state == State.TILEMAP) {
				if (a == btnBack) {
					Dialog d = new Dialog("", manager.getSkin()) {
						protected void result(Object o) {
							if (o != null) {
								dispose();
								manager.showMenu();
							}
						}
					};
					d.text(manager.lng.get(Words.EXIT) + "?");
					d.button(manager.lng.get(Words.CANCEL));
					d.button(manager.lng.get(Words.OK), this);
					d.key(Keys.ENTER, this);
					d.key(Keys.ESCAPE, null);
					d.show(getStage());
				} else if (a == btnNext) {
					setUI(State.OBJECTS);
				}
			} else if (state == State.OBJECTS) {
				if (a == btnBack) {
					setUI(State.TILEMAP);
				} else if (a == btnNext) {
					setUI(State.GAMERS_BASES);
				}
			}
		}
	};

	@Override
	protected void tapMap(Vector2 tapCoords) {
		this.tapCoords = tapCoords;
		if (state == State.TILEMAP) {
			tileLayer.setCell((int) tapCoords.x, (int) tapCoords.y,
					currTile.getInsertCell());
		} else if (state == State.OBJECTS) {
			for (int i = 4; i >= 0; i--) {
				int x = (int) tapCoords.x / (i + 1);
				int y = (int) tapCoords.y / (i + 1);
				Cell sel = objLayers[i].getCell(x, y);
				if (sel != null) {
					objLayers[i].setCell(x, y, null);
					return;
				}
			}
			Cell insCell = currObj.getInsertCell();
			int range = MetaData.objRange(insCell.getTile().getProperties()
					.get(MapUtils.CELL_OBJ_PROP, ObjCode.class));
			objLayers[range - 1].setCell((int) tapCoords.x / range,
					(int) tapCoords.y / range, insCell);
			this.rang = range;
		}
	}

	@Override
	protected void hardKeyUp(int key) {
		if (key == Keys.BACKSPACE || key == Keys.BACK) {
			btnListener.changed(null, btnBack);
		}
	}

	private void setUI(State state) {

		if (state == State.TILEMAP) {
			headLabel.setText(manager.lng.get(Words.BUILD_MAP));

			Table tilesTable = new Table();
			tilesTable.setBackground(manager.getSkin().getDrawable(
					"default-pane"));

			ArrayMap<TileCode, EditCell> tiles = initTiles();
			for (int i = 0, n = tiles.size; i < n; i++) {
				tilesTable.add(tiles.getValueAt(i));
			}
			ScrollPane scrollPane = new ScrollPane(tilesTable);
			scrollPane.pack();
			setWidget(scrollPane, Side.BOTTOM, Align.left);
		} else if (state == State.OBJECTS) {
			headLabel.setText(manager.lng.get(Words.LOCATE_OBJECTS));
			Table objTable = new Table();
			objTable.setBackground(manager.getSkin()
					.getDrawable("default-pane"));

			ArrayMap<ObjCode, EditCell> objects = initObjects();
			for (int i = 0, n = objects.size; i < n; i++) {
				objTable.add(objects.getValueAt(i));
			}
			ScrollPane scrollPane = new ScrollPane(objTable);
			scrollPane.pack();
			setWidget(scrollPane, Side.BOTTOM, Align.left);
		} else if (state == State.GAMERS_BASES) {
			// TMP (for test save map)
			TextButton btnSave = new TextButton("Save map", manager.getSkin());
			btnSave.addListener(new ChangeListener() {
				
				@Override
				public void changed(ChangeEvent event, Actor actor) {
					map.getProperties().put(MapUtils.MAX_GAMERS_PROP, 2);
					MapUtils.saveMap(map);
				}
			});
			setWidget(btnSave, Side.BOTTOM, Align.center);
		}

		this.state = state;
	}

	private ArrayMap<TileCode, EditCell> initTiles() {
		ArrayMap<TileCode, EditCell> tiles = new ArrayMap<TileCode, EditCell>();

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

	private void putTile(ArrayMap<TileCode, EditCell> tiles, TileCode code) {
		Cell cell = MapUtils.getCell(code, false);
		EditCell tile = new EditCell(cell, cellSize);
		tile.addListener(tileListener);
		tiles.put(code, tile);
	}

	private ClickListener tileListener = new ClickListener() {

		@Override
		public void clicked(InputEvent event, float x, float y) {
			if (state == State.TILEMAP) {
				currTile.setSelected(false);
				currTile = (EditCell) event.getTarget();
				currTile.setSelected(true);
			} else if (state == State.OBJECTS) {
				currObj.setSelected(false);
				currObj = (EditCell) event.getTarget();
				currObj.setSelected(true);
			}
		}
	};

	private ArrayMap<ObjCode, EditCell> initObjects() {
		ArrayMap<ObjCode, EditCell> objects = new ArrayMap<ObjCode, EditCell>();
		putObject(objects, ObjCode.HATA_1);
		currObj = objects.firstValue();
		putObject(objects, ObjCode.FORT);
		putObject(objects, ObjCode.ATB);
		putObject(objects, ObjCode.CHURCH);
		putObject(objects, ObjCode.REMAINS1);
		putObject(objects, ObjCode.REMAINS2);
		putObject(objects, ObjCode.HATA_2);
		putObject(objects, ObjCode.TREE_1);
		putObject(objects, ObjCode.TREE_2);
		putObject(objects, ObjCode.HATA_3);
		putObject(objects, ObjCode.HATA_4);
		putObject(objects, ObjCode.TENT);
		putObject(objects, ObjCode.STOLB_1);
		putObject(objects, ObjCode.STOLB_2);
		putObject(objects, ObjCode.WELL);
		putObject(objects, ObjCode.KPP);
		return objects;
	}

	private void putObject(ArrayMap<ObjCode, EditCell> objects, ObjCode code) {
		Cell cell = MapUtils.getObjCell(code);
		EditCell editCell = new EditCell(cell, cellSize);
		editCell.addListener(tileListener);
		objects.put(code, editCell);
	}

	@Override
	public void dispose() {
		super.dispose();
		map.dispose();
		ResKeeper.dispose(AtlasId.MAP_TILES);
		ResKeeper.dispose(AtlasId.OVERLAY_IMAGES);
		ResKeeper.dispose(AtlasId.EDITOR_IMAGES);
	}
}
