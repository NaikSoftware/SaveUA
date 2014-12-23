package ua.naiksoftware.waronline;

import ua.naiksoftware.waronline.game.editor.EditorHandler;
import ua.naiksoftware.waronline.res.Lng;
import ua.naiksoftware.waronline.res.ResKeeper;
import ua.naiksoftware.waronline.res.Words;
import ua.naiksoftware.waronline.res.id.TextureId;
import ua.naiksoftware.waronline.screenmanager.DesktopManager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

/**
 * Экран настроек
 * 
 * @author Naik
 *
 */
public class SettingsScreen implements Screen {

	private DesktopManager manager;
	private Stage stage;
	private Texture logo;
	private TextButton btnCreate;
	private TextButton btnEdit;
	private TextButton btnDelete;
	private Dialog dialogCreate, dialogEdit;

	public SettingsScreen(DesktopManager manager) {
		this.manager = manager;
		logo = ResKeeper.get(TextureId.LOGO);
		stage = new Stage();
		Table root = new Table();
		root.setFillParent(true);
		Image image = new Image(logo);
		root.add(image).top();
		root.row();

		Skin skin = manager.getSkin();
		Lng lng = manager.lng;
		Table content = new Table();
		content.setBackground(manager.getSkin().getDrawable("default-pane"));
		content.defaults().pad(20);

		Label labelSelMap = new Label(lng.get(Words.SELECT_MAP), skin);
		content.add(labelSelMap).align(Align.center);
		content.row();

		final SelectBox<MapEntry> dropdown = new SelectBox<MapEntry>(skin);
		dropdown.addListener(new ChangeListener() {
			public void changed(ChangeEvent event, Actor actor) {
				System.out.println(dropdown.getSelected());
			}
		});
		dropdown.setItems(MapUtils.readMapList());
		if (dropdown.getItems().size > 0) {
			dropdown.setSelectedIndex(0);
		}
		content.add(dropdown).align(Align.center);
		content.row();

		Table btnPanel = new Table();
		btnCreate = new TextButton(lng.get(Words.ADD), skin);
		btnCreate.addListener(btnListener);
		btnPanel.add(btnCreate);
		btnEdit = new TextButton(lng.get(Words.EDIT), skin);
		btnEdit.addListener(btnListener);
		btnPanel.add(btnEdit);
		btnDelete = new TextButton(lng.get(Words.DELETE), skin);
		btnDelete.addListener(btnListener);
		btnPanel.add(btnDelete);
		content.add(btnPanel).align(Align.center);
		content.row();

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
		// TODO Auto-generated method stub

	}

	@Override
	public void show() {
		InputMultiplexer im = new InputMultiplexer(stage);
		im.addProcessor(new HardInputProcessor() {
			@Override
			public boolean keyUp(int keycode) {
				if (keycode == Keys.BACKSPACE) {
					dispose();
					manager.showMenu();
				}
				return false;
			}
		});
		Gdx.input.setInputProcessor(im);
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
				final TextField fieldW = new TextField("20", skin);
				fieldW.setMaxLength(3);
				panel.add(fieldW);
				
				Label labelXX = new Label(" X ", skin);
				panel.add(labelXX);
				
				final TextField fieldH = new TextField("30", skin);
				fieldH.setMaxLength(3);
				panel.add(fieldH);
				
				dialogCreate = new Dialog("", skin) {
					@Override
					protected void result(Object object) {
						if (object != null) {
							String name = fieldName.getText();
							int w =Integer.valueOf("0" + fieldW.getText());
							int h =Integer.valueOf("0" + fieldH.getText());
							TiledMap map = MapUtils.genVoidMap(w, h, name);
							dispose();
							manager.setScreen(new EditorHandler(manager, map));
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
