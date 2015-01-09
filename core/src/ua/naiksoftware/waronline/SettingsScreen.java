package ua.naiksoftware.waronline;

import ua.naiksoftware.libgdx.ui.HardInputProcessor;
import com.badlogic.gdx.Application;
import com.badlogic.gdx.scenes.scene2d.ui.*;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import ua.naiksoftware.waronline.map.GameMap;
import ua.naiksoftware.waronline.map.MapEntry;
import ua.naiksoftware.waronline.map.MapUtils;
import ua.naiksoftware.waronline.map.editor.EditorScreen;
import ua.naiksoftware.waronline.res.Lng;
import ua.naiksoftware.waronline.res.ResKeeper;
import ua.naiksoftware.waronline.res.Words;
import ua.naiksoftware.waronline.res.id.TextureId;
import ua.naiksoftware.waronline.screenmanager.Manager;

/**
 * Экран настроек (в Android-версии есть свой (нативный, xml) экран настроек)
 *
 * @author Naik
 *
 */
public class SettingsScreen implements Screen {

    private Manager manager;
    private Stage stage;
    private Texture logo;
    private TextButton btnCreate;
    private TextButton btnEdit;
    private TextButton btnDelete;
    private Dialog dialogCreate, dialogEdit;
    private MapEntry level;

    public SettingsScreen(Manager manager) {
        this.manager = manager;
        logo = ResKeeper.get(TextureId.LOGO);
        stage = new Stage(new ScreenViewport());
        Table root = new Table();
        root.setFillParent(true);
        Image image = new Image(logo);
        root.add(image).top();
        root.row();

        Skin skin = manager.getSkin();
        Lng lng = manager.lng;
        Table content = new Table();
        content.setBackground(skin.getDrawable("default-pane"));
        content.defaults().pad(20).align(Align.center);

        Label labelSelMap = new Label(lng.get(Words.SELECT_MAP), skin);
        content.add(labelSelMap);
        content.row();

        final Array<MapEntry> levels = MapUtils.readMapList();
        final SelectBox<MapEntry> dropdown = new SelectBox<MapEntry>(skin);
        dropdown.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                level = levels.get(dropdown.getSelectedIndex());
            }
        });
        dropdown.setItems(levels);
        if (levels.size > 0) {
            dropdown.setSelectedIndex(0);
            level = levels.first();
        }
        content.add(dropdown).row();

        Table btnPanel = new Table();
        btnPanel.defaults().align(Align.center);
        btnCreate = new TextButton(lng.get(Words.ADD), skin);
        btnCreate.addListener(btnListener);
        btnPanel.add(btnCreate);
        btnEdit = new TextButton(lng.get(Words.EDIT), skin);
        btnEdit.addListener(btnListener);
        btnPanel.add(btnEdit);
        btnDelete = new TextButton(lng.get(Words.DELETE), skin);
        btnDelete.addListener(btnListener);
        btnPanel.add(btnDelete);
        content.add(btnPanel).center();
        content.row();
        if (Gdx.app.getType() == Application.ApplicationType.Android) {
            Table andSpecTable = new Table();
            Label andMenu = new Label("Gdx menu", skin);
            final CheckBox check = new CheckBox("", skin);
            check.setChecked(Prefs.is(Prefs.ANDROID_GDX_MENU));
            check.addListener(new ChangeListener() {

                @Override
                public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                    Prefs.put(Prefs.ANDROID_GDX_MENU, check.isChecked());
                }
            });
            andSpecTable.add(andMenu).padRight(50);
            andSpecTable.add(check).row();
            andSpecTable.pack();
            content.add(andSpecTable).row();
        }

        ScrollPane scroll = new ScrollPane(content);
        root.add(scroll).expand();
        stage.addActor(root);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act();
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void show() {
        InputMultiplexer im = new InputMultiplexer(stage);
        im.addProcessor(new HardInputProcessor() {
            @Override
            public boolean keyUp(int keycode) {
                if (keycode == Keys.BACKSPACE || keycode == Keys.BACK) {
                    dispose();
                    manager.showMenu();
                }
                return false;
            }
        });
        Gdx.input.setInputProcessor(im);
        Gdx.input.setCatchBackKey(true);
    }

    private ChangeListener btnListener = new ChangeListener() {

        @Override
        public void changed(ChangeEvent event, Actor actor) {
            if (actor == btnCreate) {
                showDialogEditCreate(false);
            } else if (actor == btnEdit) {
                showDialogEditCreate(true);
            }
        }
    };

    private void showDialogEditCreate(boolean edit) {
        if (edit) {
            if (dialogEdit == null) {
                Skin skin = manager.getSkin();

                Label labelName = new Label(
                        manager.lng.get(Words.INPUT_MAP_NAME), skin);

                final TextField fieldName = new TextField("", skin);
                fieldName.setMaxLength(15);
                fieldName.setOnlyFontChars(true);

                dialogEdit = new Dialog("", skin) {
                    @Override
                    protected void result(Object object) {
                        if (object != null) {
                            GameMap gameMap = MapUtils.loadTileMap(level);
                            String newName = fieldName.getText().trim();
                            if (newName.equals("")) newName = "А имя карты где?";
                            dispose();
                            manager.setScreen(new EditorScreen(manager, gameMap, newName));
                        }
                    }
                };

                Table content = dialogEdit.getContentTable();
                content.pad(20);
                content.add(labelName).center();
                content.row();
                content.add(fieldName).center().expandX().fillX();
                content.row();

                dialogEdit.button(manager.lng.get(Words.OK), this);
                dialogEdit.button(manager.lng.get(Words.CANCEL));
            }
            dialogEdit.show(stage);
        } else {
            if (dialogCreate == null) {
                Skin skin = manager.getSkin();

                Label labelName = new Label(
                        manager.lng.get(Words.INPUT_MAP_NAME), skin);

                final TextField fieldName = new TextField("", skin);
                fieldName.setMaxLength(15);
                fieldName.setOnlyFontChars(true);

                Label labelSize = new Label(
                        manager.lng.get(Words.INPUT_MAP_SIZE), skin);

                Table panel = new Table();
                final TextField fieldW = new TextField("30", skin);
                fieldW.setMaxLength(3);
                fieldW.setTextFieldFilter(numberFilter);
                panel.add(fieldW);

                Label labelXX = new Label(" X ", skin);
                panel.add(labelXX);

                final TextField fieldH = new TextField("40", skin);
                fieldH.setMaxLength(3);
                fieldH.setTextFieldFilter(numberFilter);
                panel.add(fieldH);

                dialogCreate = new Dialog("", skin) {
                    @Override
                    protected void result(Object object) {
                        if (object != null) {
                            String name = fieldName.getText().trim();
                            int w = Integer.valueOf("0" + fieldW.getText());
                            int h = Integer.valueOf("0" + fieldH.getText());
                            if (w < 5) w = 5;
                            if (h < 5) h = 5;
                            if (name.equals("")) name = "Где название карты?";
                            TiledMap map = MapUtils.genVoidMap(w, h);
                            dispose();
                            manager.setScreen(new EditorScreen(manager, map, name));
                        }
                    }
                };

                Table content = dialogCreate.getContentTable();
                content.pad(20);
                content.add(labelName).center();
                content.row();
                content.add(fieldName).center().expandX().fillX();
                content.row();
                content.add(labelSize).center();
                content.row();
                content.add(panel).center();

                dialogCreate.button(manager.lng.get(Words.OK), this);
                dialogCreate.button(manager.lng.get(Words.CANCEL));
            }
            dialogCreate.show(stage);
        }
    }

    private final TextField.TextFieldFilter numberFilter = new TextField.TextFieldFilter() {

        public boolean acceptChar(TextField textField, char c) {
            if ("0123456789".indexOf(c) == -1) {
                return false;
            } else {
                return true;
            }
        }
    };

    @Override
    public void hide() {
        // TODO Auto-generated method stub

    }

    @Override
    public void pause() {
        // TODO Auto-generated method stub

    }

    @Override
    public void resume() {
        // TODO Auto-generated method stub

    }

    @Override
    public void dispose() {
        stage.dispose();
        ResKeeper.dispose(TextureId.LOGO);
    }
}
