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
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import ua.naiksoftware.waronline.game.Gamer;
import ua.naiksoftware.waronline.game.unit.UnitCode;

public class EditorHandler extends ScrollMap {

    private Manager manager;
    private TiledMap map;
    private SpriteBatch batch;
    private BitmapFont font;
    private Label headLabel;
    private TextButton btnBack, btnNext;
    private Vector2 tapCoords = new Vector2();
    private TiledMapTileLayer tileLayer;
    private TiledMapTileLayer[] objLayers;

    Array<EditCell> tiles;
    private EditCell currTile;

    Array<EditCell> objects;
    private EditCell currObj;

    private Array<EditCell> panelUnits;
    private EditCell currUnit;

    private Array<EditUnit> units;
    private Gamer currentGamer;

    private int cellSize;
    private Group gameWidget;

    private enum State {

        TILEMAP, OBJECTS, FREE_UNITS, GAMERS_UNITS
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
        units = new Array<EditUnit>();

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
        gameWidget = getGameWidget();
    }

    //int x, y;
    @Override
    public void render(float deltaTime) {
        super.render(deltaTime);

        batch.begin();
        //font.draw(batch, "Sel: " + x + ", " + y + " rang: " + rang, 10, 60);
        //font.draw(batch, "Tap: " + tapCoords.x + ", " + tapCoords.y, 10, 40);
        font.draw(batch, "FPS: " + Gdx.graphics.getFramesPerSecond(), 10, 20);
        batch.end();
    }

    private final ChangeListener btnListener = new ChangeListener() {

        @Override
        public void changed(ChangeListener.ChangeEvent ev, Actor a) {
            if (state == State.TILEMAP) {
                if (a == btnBack) {
                    Dialog d = new Dialog("", manager.getSkin()) {
                        @Override
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
                    setUI(State.GAMERS_UNITS);
                }
            } else if (state == State.GAMERS_UNITS) {
                if (a == btnBack) {
                    setUI(State.OBJECTS);
                }
            } else {
                if (a == btnNext) {
                    //map.getProperties().put(MapUtils.MAX_GAMERS_PROP, 2);
                    //EditGameMap editGameMap = new EditGameMap(map, units);
                    //MapUtils.saveMap(editGameMap);
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
        } else if (state == State.GAMERS_UNITS) {
            int x = (int) tapCoords.x * cellSize;
            int y = (int) tapCoords.y * cellSize;
            boolean hold = false;
            for (EditUnit u : units) {
                if ((int) u.getX() == x && (int) u.getY() == y) {
                    if (u.getGamer() == currentGamer) {
                        units.removeValue(u, true);
                        u.remove();
                    }
                    hold = true;
                    break;
                }
            }
            if (!hold) {
                EditUnit unit = new EditUnit(currUnit.getDrawable(), currUnit.getUnitCode(), currentGamer);
                unit.setPosition(x, y);
                units.add(unit);
                gameWidget.addActor(unit);
            }
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

            if (tiles == null) {
                tiles = new Array<EditCell>();
                initTiles();
                currTile = tiles.first();
                currTile.setSelected(true);
            }
            for (EditCell tile : tiles) {
                tilesTable.add(tile);
            }

            ScrollPane scrollPane = new ScrollPane(tilesTable);
            scrollPane.pack();
            setWidget(scrollPane, Side.BOTTOM, Align.left);

        } else if (state == State.OBJECTS) {
            headLabel.setText(manager.lng.get(Words.LOCATE_OBJECTS));
            Table objTable = new Table();
            objTable.setBackground(manager.getSkin()
                    .getDrawable("default-pane"));

            if (objects == null) {
                objects = new Array<EditCell>();
                initObjects();
                currObj = objects.first();
                currObj.setSelected(true);
            }

            for (EditCell obj : objects) {
                objTable.add(obj);
            }
            ScrollPane scrollPane = new ScrollPane(objTable);
            scrollPane.pack();
            setWidget(scrollPane, Side.BOTTOM, Align.left);

        } else if (state == State.GAMERS_UNITS) {
            headLabel.setText(manager.lng.get(Words.UNITS_GAMER) + " 1");
            Table unitTable = new Table();

            if (panelUnits == null) {
                panelUnits = new Array<EditCell>();
                initPanelUnits();
                currUnit = panelUnits.first();
                currUnit.setSelected(true);
            }

            for (EditCell unit : panelUnits) {
                unitTable.add(unit);
            }

            TextButton btnMinus = new TextButton(" - ", manager.getSkin());
            btnMinus.addListener(new ChangeListener() {

                @Override
                public void changed(ChangeEvent event, Actor actor) {
                }
            });
            TextButton btnPlus = new TextButton(" + ", manager.getSkin());
            btnPlus.addListener(new ChangeListener() {

                @Override
                public void changed(ChangeEvent event, Actor actor) {
                }
            });

            ScrollPane scrollPane = new ScrollPane(unitTable);
            scrollPane.pack();
            Table panel = new Table();
            panel.setBackground(manager.getSkin().getDrawable("default-pane"));
            panel.add(btnMinus).left();
            panel.add(scrollPane).expandX().center();
            panel.add(btnPlus);
            setWidget(panel, Side.BOTTOM, Align.left);
            units.clear();
            Gamer.count = 0;
            currentGamer = new Gamer();
        }

        this.state = state;
    }

    private void initTiles() {
        putTile(TileCode.GRASS);

        putTile(TileCode.TREES);
        putTile(TileCode.TREES_EDGE_DOWN);
        putTile(TileCode.TREES_EDGE_LEFT);
        putTile(TileCode.TREES_EDGE_UP);
        putTile(TileCode.TREES_EDGE_RIGHT);

        putTile(TileCode.TREES_CORNER_RIGHT_DOWN);
        putTile(TileCode.TREES_CORNER_LEFT_DOWN);
        putTile(TileCode.TREES_CORNER_RIGHT_UP);
        putTile(TileCode.TREES_CORNER_LEFT_UP);

        putTile(TileCode.TREES_INCORNER_RIGHT_DOWN);
        putTile(TileCode.TREES_INCORNER_LEFT_DOWN);

        putTile(TileCode.ROAD_HORIZ);
        putTile(TileCode.ROAD_VERT);
        putTile(TileCode.ROAD_INTERSECT);
        putTile(TileCode.ROAD_CORNER_LEFT_DOWN);
        putTile(TileCode.ROAD_CORNER_RIGHT_DOWN);
        putTile(TileCode.ROAD_CORNER_LEFT_UP);
        putTile(TileCode.ROAD_CORNER_RIGHT_UP);
        putTile(TileCode.ROAD_END_DOWN);
        putTile(TileCode.ROAD_END_LEFT);
        putTile(TileCode.ROAD_END_UP);
        putTile(TileCode.ROAD_END_RIGHT);

        putTile(TileCode.WATER);
        putTile(TileCode.WATER_DOWN_1);
        putTile(TileCode.WATER_DOWN_2);
        putTile(TileCode.WATER_UP_1);
        putTile(TileCode.WATER_UP_2);
        putTile(TileCode.WATER_LEFT_1);
        putTile(TileCode.WATER_LEFT_2);
        putTile(TileCode.WATER_RIGHT_1);
        putTile(TileCode.WATER_RIGHT_2);
        putTile(TileCode.WATER_CORNER_LEFT_DOWN);
        putTile(TileCode.WATER_CORNER_RIGHT_DOWN);
        putTile(TileCode.WATER_CORNER_LEFT_UP);
        putTile(TileCode.WATER_CORNER_RIGHT_UP);
        putTile(TileCode.WATER_INCORNER_RIGHT_DOWN);
        putTile(TileCode.WATER_INCORNER_LEFT_DOWN);
        putTile(TileCode.WATER_INCORNER_RIGHT_UP);
        putTile(TileCode.WATER_INCORNER_LEFT_UP);

        putTile(TileCode.BRIDGE_VERT);
        putTile(TileCode.BRIDGE_HORIZ);
        putTile(TileCode.BRIDGE_UP);
        putTile(TileCode.BRIDGE_DOWN);
        putTile(TileCode.BRIDGE_LEFT);
        putTile(TileCode.BRIDGE_RIGHT);

        putTile(TileCode.REDUIT_1);
        putTile(TileCode.REDUIT_2);
    }

    private void putTile(TileCode code) {
        Cell cell = MapUtils.getCell(code, false);
        EditCell tile = new EditCell(cell, cellSize);
        tile.addListener(tileListener);
        tiles.add(tile);
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
            } else if (state == State.GAMERS_UNITS) {
                currUnit.setSelected(false);
                currUnit = (EditCell) event.getTarget();
                currUnit.setSelected(true);
            }
        }
    };

    private void initObjects() {
        putObject(ObjCode.HATA_1);
        putObject(ObjCode.FORT);
        putObject(ObjCode.ATB);
        putObject(ObjCode.CHURCH);
        putObject(ObjCode.REMAINS1);
        putObject(ObjCode.REMAINS2);
        putObject(ObjCode.HATA_2);
        putObject(ObjCode.TREE_1);
        putObject(ObjCode.TREE_2);
        putObject(ObjCode.HATA_3);
        putObject(ObjCode.HATA_4);
        putObject(ObjCode.TENT);
        putObject(ObjCode.STOLB_1);
        putObject(ObjCode.STOLB_2);
        putObject(ObjCode.WELL);
        putObject(ObjCode.KPP);
    }

    private void putObject(ObjCode code) {
        Cell cell = MapUtils.getObjCell(code);
        EditCell editCell = new EditCell(cell, cellSize);
        editCell.addListener(tileListener);
        objects.add(editCell);
    }

    private void initPanelUnits() {
        putUnitToPanel(UnitCode.BTR_4E);
        putUnitToPanel(UnitCode.ARTILLERY);
        putUnitToPanel(UnitCode.HOTCHKISS);
        putUnitToPanel(UnitCode.ING_AVTO);
        putUnitToPanel(UnitCode.PANZER);
        putUnitToPanel(UnitCode.T34_85);
        putUnitToPanel(UnitCode.SOLDIER);
        putUnitToPanel(UnitCode.TIGER);
    }

    private void putUnitToPanel(UnitCode code) {
        TextureAtlas atlas = ResKeeper.get(AtlasId.UNIT_SPRITES);
        String name = null;
        switch (code) {
            case BTR_4E:
                name = "horse_up";
                break;
            case ARTILLERY:
                name = "artillery_up";
                break;
            case HOTCHKISS:
                name = "hotchkiss_up";
                break;
            case ING_AVTO:
                name = "avto_up";
                break;
            case PANZER:
                name = "panzer_up";
                break;
            case T34_85:
                name = "t34_85_up";
                break;
            case SOLDIER:
                name = "soldier_up";
                break;
            case TIGER:
                name = "tiger_up";
                break;
        }

        if (name == null) {
            throw new IllegalArgumentException("Name for code \"" + code
                    + "\" in atlas units sprites not exists");
        }
        TextureRegion r = atlas.findRegion(name);
        if (r == null) {
            throw new IllegalArgumentException("Name \"" + name
                    + "\" in atlas units sprites not exists");
        }
        panelUnits.add(new EditCell(r, code));
    }

    @Override
    public void dispose() {
        super.dispose();
        map.dispose();
        ResKeeper.dispose(AtlasId.MAP_TILES);
        ResKeeper.dispose(AtlasId.OVERLAY_IMAGES);
        ResKeeper.dispose(AtlasId.EDITOR_IMAGES);
        ResKeeper.dispose(AtlasId.UNIT_SPRITES);
    }
}
