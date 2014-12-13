package ua.naiksoftware.waronline;

import ua.naiksoftware.waronline.game.GameHandler;
import ua.naiksoftware.waronline.game.editor.EditorReceiver;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

/**
 * Used instead of native menu on the same platforms, like desktop
 * @author Naik
 *
 */
public class GdxMenu implements Screen, EditorReceiver {

	private static final String tag = GdxMenu.class.getName();
	private Stage stage;
	private Texture bg, logo, btn, wndBg;
	private Table root;
	private TextButton btnPAP, btnOnline, btnSettings, btnAbout;
	private OrthographicCamera cam;
	private Dialog dialogInfo;

	public GdxMenu() {
		MyGame game = MyGame.getInstance();
		cam = new OrthographicCamera();
		stage = new Stage(new ScreenViewport(cam));
		Skin skin = game.skin;
		BitmapFont font = skin.getFont("default-font");
		font.setScale(Math.min(Gdx.graphics.getWidth(),
				Gdx.graphics.getHeight())
				/ 10 / font.getLineHeight());
		btn = new Texture(Gdx.files.internal("btn_off.png"),
				Pixmap.Format.RGBA8888, false);
		NinePatchDrawable npBtnOff = new NinePatchDrawable(new NinePatch(btn,
				25, 25, 15, 25)); // рамка r,l,t,b
		NinePatchDrawable npBtnOn = new NinePatchDrawable(new NinePatch(
				npBtnOff.getPatch(), Color.YELLOW));
		TextButton.TextButtonStyle tbStyle = new TextButton.TextButtonStyle(
				npBtnOff, npBtnOn, null, font);
		Lng lng = game.lng;
		btnPAP = new TextButton(lng.get(Words.PASS_AND_PLAY), tbStyle);
		btnPAP.addListener(btnListener);
		btnOnline = new TextButton(lng.get(Words.PLAY_ONLINE), tbStyle);
		btnOnline.addListener(btnListener);
		btnSettings = new TextButton(lng.get(Words.SETTINGS), tbStyle);
		btnSettings.addListener(btnListener);
		btnAbout = new TextButton(lng.get(Words.ABOUT), tbStyle);
		btnAbout.addListener(btnListener);

		bg = new Texture(Gdx.files.internal("bg.jpg"), Pixmap.Format.RGB888,
				false);
		logo = new Texture(Gdx.files.internal("logo.png"),
				Pixmap.Format.RGBA8888, false);
		logo.setFilter(Texture.TextureFilter.Linear,
				Texture.TextureFilter.Linear);
		wndBg = new Texture(Gdx.files.internal("window_bg.png"),
				Pixmap.Format.RGB888, false);

		Table tableMenu = new Table();
		tableMenu.defaults().pad(Gdx.graphics.getHeight() / 90).expand().fill();
		tableMenu.add(btnPAP);
		tableMenu.row();
		tableMenu.add(btnOnline);
		tableMenu.row();
		tableMenu.add(btnSettings);
		tableMenu.row();
		tableMenu.add(btnAbout);
		ScrollPane scrollPane = new ScrollPane(tableMenu);
		scrollPane.setOverscroll(false, true);
		Image logoImage = new Image(logo);
		logoImage.setScaling(Scaling.fit);
		root = new Table();
		root.setFillParent(true);
		root.add(logoImage).expand().align(Align.center);
		root.row();
		root.add(scrollPane).expand().align(Align.top);
		stage.addActor(root);

		dialogInfo = new Dialog("", skin);
		dialogInfo.setBackground(new NinePatchDrawable(new NinePatch(wndBg, 32,
				31, 32, 31)));
		dialogInfo.button("BtnTest");
		dialogInfo.text("Test text rgsrg rgsdrg srgsrtqwweffffffffffffffffffffffff\n SDRGRG АПЕРАЕ  іерівер вр\n\n  парчрывры ыр ывр ыв \nры  чвр ывр ");
	}

	@Override
	public void render(float deltaTime) {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		stage.act(deltaTime);
		stage.draw();
	}

	@Override
	public void resize(int newX, int newY) {
		stage.getViewport().update(newX, newY, true);
		int dy = bg.getHeight() - newY;
		TextureRegion tr = new TextureRegion(bg,
				(bg.getWidth() - newX - dy) / 2, 0, newX + dy, bg.getHeight());
		root.setBackground(new TextureRegionDrawable(tr));
	}

	@Override
	public void show() {
		InputMultiplexer im = new InputMultiplexer(stage);
		im.addProcessor(new HardInputProcessor() {
			@Override
			public boolean keyUp(int keycode) {
				if (keycode == Keys.BACKSPACE) {
					dispose();
					Gdx.app.exit();
				}
				return false;
			}
		});
		Gdx.input.setInputProcessor(im);
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

	private ChangeListener btnListener = new ChangeListener() {

		@Override
		public void changed(ChangeListener.ChangeEvent ev, Actor a) {
			if (a == btnPAP) {
				dispose();
				MyGame.getInstance().setScreen(new GameHandler("atlas/tile_map.atlas"));
			} else if (a == btnOnline) {
			} else if (a == btnSettings) {
				MyGame.getInstance().setScreen(new Settings());
			} else if (a == btnAbout) {
				dialogInfo.show(stage);
			}
		}
	};

	@Override
	public void dispose() {
		stage.dispose();
		bg.dispose();
		logo.dispose();
		btn.dispose();
		wndBg.dispose();
	}

	@Override
	public void onEditMapComplete(TiledMap map) {
		map.dispose();
		MyGame.getInstance().setScreen(this);
	}
}
