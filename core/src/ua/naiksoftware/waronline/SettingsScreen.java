package ua.naiksoftware.waronline;

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
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
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
		content.align(Align.center);
		content.defaults().pad(20);

		Label labelSelMap = new Label(lng.get(Words.SELECT_MAP), skin);
		content.add(labelSelMap).minWidth(image.getWidth() * image.getScaleX());
		content.row();

		final SelectBox dropdown = new SelectBox(skin);
		dropdown.addListener(new ChangeListener() {
			public void changed(ChangeEvent event, Actor actor) {
				System.out.println(dropdown.getSelected());
			}
		});
		dropdown.setItems(MapUtils.readMapList());
		if (dropdown.getItems().size > 0) {
			dropdown.setSelectedIndex(0);
		}
		content.add(dropdown);
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
