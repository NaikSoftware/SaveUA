package ua.naiksoftware.waronline.map.editor;

import ua.naiksoftware.waronline.map.MapObject;
import ua.naiksoftware.waronline.map.TileCode;
import ua.naiksoftware.waronline.map.MapObjCode;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import ua.naiksoftware.waronline.game.*;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import ua.naiksoftware.waronline.map.MapCell;
import ua.naiksoftware.waronline.map.MapUtils;
import ua.naiksoftware.waronline.ScrollMap;
import ua.naiksoftware.waronline.game.unit.UnitCode;
import ua.naiksoftware.waronline.res.MapMetaData;
import ua.naiksoftware.waronline.res.ResKeeper;
import ua.naiksoftware.waronline.res.Words;
import ua.naiksoftware.waronline.res.id.AtlasId;
import ua.naiksoftware.waronline.screenmanager.Manager;

public class EditorScreen extends ScrollMap {

    private Manager manager;
    private final TiledMap map;
    private final Batch batch;
    private final BitmapFont font;
    private final Label headLabel;
    private TextButton btnBack, btnNext;
    private final TiledMapTileLayer layerBg;
    private Dialog dialog;

    /**
     * Tiles on panel
     */
    Array<SelectPanelCell> panelTiles;
    private SelectPanelCell currTile;
    /**
     * Objects (buildings, trees) on panel
     */
    Array<SelectPanelCell> panelObjects;
    private SelectPanelCell currObj;
    /**
     * Units on panel
     */
    private Array<SelectPanelCell> panelUnits;
    private SelectPanelCell currUnit;

    private final Array<Sprite> sprites;
    private Gamer currentGamer, savedCurrentGamer;

    private final int cellSize;

    private enum State {

        TILEMAP, OBJECTS, FREE_UNITS, GAMERS_UNITS
    }

    private State state;

    // TODO: send map objects if edit existing map
    public EditorScreen(Manager manager, TiledMap map) {
        super(map);
        this.manager = manager;
        this.map = map;
        this.layerBg = (TiledMapTileLayer) map.getLayers().get(0);
        cellSize = mapCellSize();
        font = new BitmapFont();
        batch = new SpriteBatch();
        sprites = new Array<Sprite>();

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
        setSprites(sprites);
        ImpassableCells.clear();
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
    }

    private final ChangeListener btnListener = new ChangeListener() {

        @Override
        public void changed(ChangeListener.ChangeEvent ev, Actor a) {
            if (state == State.TILEMAP) {
                if (a == btnBack) {
                    dialog = new Dialog("", manager.getSkin()) {
                        @Override
                        protected void result(Object o) {
                            if (o != null) {
                                dispose();
                                manager.showMenu();
                            }
                        }
                    };
                    dialog.text(manager.lng.get(Words.EXIT) + "?");
                    dialog.button(manager.lng.get(Words.CANCEL));
                    dialog.button(manager.lng.get(Words.OK), this);
                    dialog.key(Keys.ENTER, this);
                    dialog.key(Keys.ESCAPE, null);
                    dialog.show(getStage());
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
                } else if (a == btnNext) {
                    if (Gamer.count < 2) {
                        dialog = new Dialog("", manager.getSkin());
                        dialog.text(manager.lng.get(Words.MIN_TWO_GAMERS_REQUIRED));
                        dialog.button(manager.lng.get(Words.OK));
                        dialog.key(Keys.ENTER, null);
                        dialog.key(Keys.ESCAPE, null);
                        dialog.show(getStage());
                    } else {
                        setUI(State.FREE_UNITS);
                    }
                }
            } else if (state == State.FREE_UNITS) {
                if (a == btnBack) {
                    setUI(State.GAMERS_UNITS);
                } else if (a == btnNext) {
                    dialog = new Dialog("", manager.getSkin()) {
                        @Override
                        protected void result(Object o) {
                            if (o != null) {
                                MapUtils.saveMap(new EditGameMap(map, sprites));
                                dispose();
                                manager.showMenu();
                            }
                        }
                    };
                    dialog.text(manager.lng.get(Words.SAVE_MAP_AND_EXIT));
                    dialog.button(manager.lng.get(Words.OK), this);
                    dialog.button(manager.lng.get(Words.CANCEL));
                    dialog.key(Keys.ENTER, this);
                    dialog.key(Keys.ESCAPE, null);
                    dialog.show(getStage());
                }
            }
        }
    };

    @Override
    protected void tapMap(Vector2 tapCoords) {
        int tapX = (int) tapCoords.x;
        int tapY = (int) tapCoords.y;
        if (state == State.TILEMAP) {
            layerBg.setCell(tapX, tapY, currTile.getInsertCell());
        } else if (state == State.OBJECTS) {
            MapObject obj = null;
            for (Sprite s : sprites) {
                if (s.getBoundingRectangle().contains(tapX * cellSize + 1, tapY * cellSize + 1)
                        && s instanceof MapObject) {
                    obj = (MapObject) s;
                    setImassableCellsUnderObject(obj, false);
                    sprites.removeValue(obj, true);
                    for (Sprite sp : sprites) {
                        if (sp instanceof MapObject) {
                            Rectangle r1 = s.getBoundingRectangle();
                            Rectangle r2 = sp.getBoundingRectangle();
                            if (r1.contains(r2) || r1.overlaps(r2)) {
                                setImassableCellsUnderObject((MapObject) sp, true);
                            }
                        }
                    }
                    break;
                }
            }
            if (obj == null) {
                obj = new MapObject(currObj.getMapObjCode());
                obj.setX(tapX * cellSize);
                obj.setY(tapY * cellSize);
                setImassableCellsUnderObject(obj, true);
                addSprite(obj);
            }
        } else if (state == State.GAMERS_UNITS
                || state == State.FREE_UNITS) {
            int x = tapX * cellSize;
            int y = tapY * cellSize;
            boolean hold = false;
            for (Sprite s : sprites) {
                if (s instanceof MapUnit) {
                    MapUnit u = (MapUnit) s;
                    if ((int) u.getX() == x && (int) u.getY() == y) {
                        if (u.getGamer() == currentGamer) {
                            sprites.removeValue(u, true);
                        }
                        hold = true;
                        break;
                    }
                }
            }
            if (!hold) {
                MapCell cell = (MapCell) layerBg.getCell(tapX, tapY);
                if (!ImpassableCells.have(tapX, tapY)) {
                    switch (cell.getCode()) {
                        case WATER:
                        case WATER_CORNER_LEFT_DOWN:
                        case WATER_CORNER_LEFT_UP:
                        case WATER_CORNER_RIGHT_DOWN:
                        case WATER_CORNER_RIGHT_UP:
                        case WATER_INCORNER_LEFT_DOWN:
                        case WATER_INCORNER_LEFT_UP:
                        case WATER_INCORNER_RIGHT_DOWN:
                        case WATER_INCORNER_RIGHT_UP:
                        case WATER_DOWN_2:
                        case WATER_LEFT_2:
                        case WATER_RIGHT_2:
                        case WATER_UP_2:
                        case REDUIT_1:
                        case REDUIT_2:
                            break;
                        default:
                            MapUnit unit = new MapUnit(currUnit.getUnitCode(), currentGamer);
                            unit.setPosition(x, y);
                            addSprite(unit);
                    }
                }
            }
        }
    }

    private void setImassableCellsUnderObject(MapObject obj, boolean insert) {
        int x = (int) (obj.getX() / cellSize);
        int y = (int) (obj.getY() / cellSize);
        int[][] mask = MapMetaData.objIntersect(obj.getMapObjCode());
        int range = mask.length;
        for (int i = 0; i < range; i++) {
            int arr[] = mask[i];
            for (int j = 0; j < range; j++) {
                if (arr[j] == 1) {
                    if (insert) {
                        ImpassableCells.add(x + i, y + j);
                    } else {
                        ImpassableCells.remove(x + i, y + j);
                    }
                }
            }
        }
    }

    /**
     * For sort by y-axis
     */
    private void addSprite(Sprite s) {
        float y = s.getY();
        int i, size = sprites.size;
        for (i = 0; i < size; i++) {
            if (y > sprites.get(i).getY()) {
                sprites.insert(i, s);
                return;
            }
        }
        sprites.insert(i, s);
    }

    @Override
    protected void hardKeyUp(int key) {
        if (key == Keys.BACKSPACE || key == Keys.BACK) {
            if (!getStage().getActors().contains(dialog, true)) {
                btnListener.changed(null, btnBack);
            }
        }
    }

    private void setUI(State state) {

        if (state == State.TILEMAP) {
            headLabel.setText(manager.lng.get(Words.BUILD_MAP));

            Table tilesTable = new Table();
            tilesTable.setBackground(manager.getSkin().getDrawable(
                    "default-pane"));

            if (panelTiles == null) {
                panelTiles = new Array<SelectPanelCell>();
                initTiles();
                currTile = panelTiles.first();
                currTile.setSelected(true);
            }
            for (SelectPanelCell tile : panelTiles) {
                tilesTable.add(tile);
            }

            ScrollPane scrollPane = new ScrollPane(tilesTable);
            scrollPane.pack();
            setWidget(scrollPane, Side.BOTTOM, Align.left);

        } else if (state == State.OBJECTS) {
            // оставляем саму карту
            Sprite s;
            for (int i = 0; i < sprites.size; i++) {
                s = sprites.get(i);
                if (s instanceof MapUnit) {
                    sprites.removeValue(s, true);
                    i--;
                }
            }
            UnitHelper.disposeAll(); // Очищаем старые life bars если есть
            headLabel.setText(manager.lng.get(Words.LOCATE_OBJECTS));
            Table objTable = new Table();
            objTable.setBackground(manager.getSkin()
                    .getDrawable("default-pane"));

            if (panelObjects == null) {
                panelObjects = new Array<SelectPanelCell>();
                initObjects();
                currObj = panelObjects.first();
                currObj.setSelected(true);
            }

            for (SelectPanelCell obj : panelObjects) {
                objTable.add(obj);
            }
            ScrollPane scrollPane = new ScrollPane(objTable);
            scrollPane.pack();
            setWidget(scrollPane, Side.BOTTOM, Align.left);

        } else if (state == State.GAMERS_UNITS) {
            Table unitTable = new Table();

            if (panelUnits == null) {
                panelUnits = new Array<SelectPanelCell>();
                initPanelUnits();
                currUnit = panelUnits.first();
                currUnit.setSelected(true);
            }

            for (SelectPanelCell unit : panelUnits) {
                unitTable.add(unit);
            }

            TextButton btnMinus = new TextButton(" - ", manager.getSkin());
            btnMinus.addListener(new ChangeListener() {

                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    currentGamer = selectPrevGamer();
                    headLabel.setText(manager.lng.get(Words.UNITS_GAMER) + " "
                            + currentGamer.getId());
                }
            });
            TextButton btnPlus = new TextButton(" + ", manager.getSkin());
            btnPlus.addListener(new ChangeListener() {

                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    if (getCountUnits(currentGamer) > 0) {
                        currentGamer = new Gamer();
                        headLabel.setText(manager.lng.get(Words.UNITS_GAMER)
                                + " " + currentGamer.getId());
                    }
                }
            });

            ScrollPane scrollPane = new ScrollPane(unitTable);
            Table panel = new Table();
            panel.setBackground(manager.getSkin().getDrawable("default-pane"));
            panel.add(btnMinus).left();
            panel.add(scrollPane).expandX().center();
            panel.add(btnPlus);
            panel.pack();
            setWidget(panel, Side.BOTTOM, Align.left);
            if (savedCurrentGamer == null) {
                Gamer.count = 0;
                currentGamer = new Gamer();
            } else {
                // Значит вернулись из расстановки ничейных юнитов,
                // удаляем их
                currentGamer = savedCurrentGamer;
                savedCurrentGamer = null;
                Sprite s;
                for (int size = sprites.size, i = 0; i < size; i++) {
                    s = sprites.get(i);
                    if (s instanceof MapUnit) {
                        if (((MapUnit) s).getGamer() == null) {
                            sprites.removeValue(s, true);
                            i--;
                            size--;
                        }
                    }
                }
            }
            headLabel.setText(manager.lng.get(Words.UNITS_GAMER) + " "
                    + currentGamer.getId());

        } else if (state == State.FREE_UNITS) {
            headLabel.setText(manager.lng.get(Words.FREE_UNITS));
            Table unitTable = new Table();

            // Список юнитов инициализирован на пред. шаге расставления юнитов игроков
            for (SelectPanelCell unit : panelUnits) {
                unitTable.add(unit);
            }

            ScrollPane scrollPane = new ScrollPane(unitTable);
            Table panel = new Table();
            panel.setBackground(manager.getSkin().getDrawable("default-pane"));
            panel.add(scrollPane).expandX().center();
            panel.pack();
            setWidget(panel, Side.BOTTOM, Align.left);
            savedCurrentGamer = currentGamer;
            currentGamer = null; // ничейные юниты же
        }

        this.state = state;
    }

    private int getCountUnits(Gamer g) {
        int count = 0;
        for (Sprite s : sprites) {
            if (s instanceof MapUnit && ((MapUnit) s).getGamer() == g) {
                count++;
            }
        }
        return count;
    }

    private Gamer selectPrevGamer() {
        Gamer prev = currentGamer;
        if (Gamer.count > 1) {
            Gamer.count--;
            Sprite s;
            for (int i = 0; i < sprites.size; i++) {
                s = sprites.get(i);
                if (s instanceof MapUnit) {
                    MapUnit u = (MapUnit) s;
                    if (u.getGamer() == currentGamer) {
                        sprites.removeValue(u, true);
                        i--;
                    } else if (u.getGamer().getId() == Gamer.count) {
                        prev = u.getGamer();
                    }
                }
            }
        }
        return prev;
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
        SelectPanelCell tile = new SelectPanelCell(cell);
        tile.addListener(panelListener);
        panelTiles.add(tile);
    }

    private final ClickListener panelListener = new ClickListener() {

        @Override
        public void clicked(InputEvent event, float x, float y) {
            if (state == State.TILEMAP) {
                currTile.setSelected(false);
                currTile = (SelectPanelCell) event.getTarget();
                currTile.setSelected(true);
            } else if (state == State.OBJECTS) {
                currObj.setSelected(false);
                currObj = (SelectPanelCell) event.getTarget();
                currObj.setSelected(true);
            } else if (state == State.GAMERS_UNITS || state == State.FREE_UNITS) {
                currUnit.setSelected(false);
                currUnit = (SelectPanelCell) event.getTarget();
                currUnit.setSelected(true);
            }
        }
    };

    private void initObjects() {
        putMapObjectToPanel(MapObjCode.HATA_1);
        putMapObjectToPanel(MapObjCode.FORT);
        putMapObjectToPanel(MapObjCode.ATB_1);
        putMapObjectToPanel(MapObjCode.ATB_2);
        putMapObjectToPanel(MapObjCode.CHURCH);
        putMapObjectToPanel(MapObjCode.REMAINS1);
        putMapObjectToPanel(MapObjCode.REMAINS2);
        putMapObjectToPanel(MapObjCode.HATA_2);
        putMapObjectToPanel(MapObjCode.TREE_1);
        putMapObjectToPanel(MapObjCode.TREE_2);
        putMapObjectToPanel(MapObjCode.HATA_3);
        putMapObjectToPanel(MapObjCode.HATA_4);
        putMapObjectToPanel(MapObjCode.TENT);
        putMapObjectToPanel(MapObjCode.STOLB_1);
        putMapObjectToPanel(MapObjCode.STOLB_2);
        putMapObjectToPanel(MapObjCode.WELL);
        putMapObjectToPanel(MapObjCode.KPP);
    }

    private void putMapObjectToPanel(MapObjCode code) {
        SelectPanelCell editCell = new SelectPanelCell(MapObject
                .getAtlasRegions(code).first(), code, cellSize);
        editCell.addListener(panelListener);
        panelObjects.add(editCell);
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
        SelectPanelCell cell = new SelectPanelCell(
                MapUnit.getTextureRegion(code), code);
        cell.addListener(panelListener);
        panelUnits.add(cell);
    }

    @Override
    public void dispose() {
        super.dispose();
        UnitHelper.disposeAll();
        map.dispose();
        ResKeeper.dispose(AtlasId.MAP_TILES);
        ResKeeper.dispose(AtlasId.OVERLAY_IMAGES);
        ResKeeper.dispose(AtlasId.EDITOR_IMAGES);
        ResKeeper.dispose(AtlasId.UNIT_SPRITES);
    }
}
