package ua.naiksoftware.waronline;

import ua.naiksoftware.waronline.game.GameHandler;
import ua.naiksoftware.waronline.res.Lng;
import ua.naiksoftware.waronline.res.ResKeeper;
import ua.naiksoftware.waronline.res.Words;
import ua.naiksoftware.waronline.res.id.TextureId;
import ua.naiksoftware.waronline.screenmanager.DesktopManager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
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
import ua.naiksoftware.waronline.game.GameMap;

/**
 * Used instead of native menu on the same platforms, like desktop
 *
 * @author Naik
 *
 */
public class GdxMenu implements Screen {

    private DesktopManager manager;
    private Stage stage;
    private Texture bg, logo, btn;
    private Table root;
    private TextButton btnPAP, btnOnline, btnSettings, btnAbout;
    private OrthographicCamera cam;
    private Dialog dialogInfo;

    public GdxMenu(DesktopManager manager) {
        this.manager = manager;
        cam = new OrthographicCamera();
        stage = new Stage(new ScreenViewport(cam));
        Skin skin = manager.getSkin();
        Lng lng = manager.lng;
        BitmapFont font = manager.getTitleFont();
        btn = ResKeeper.get(TextureId.BTN);
        NinePatchDrawable npBtnOff = new NinePatchDrawable(new NinePatch(btn,
                25, 25, 15, 25)); // рамка r,l,t,b
        NinePatchDrawable npBtnOn = new NinePatchDrawable(new NinePatch(
                npBtnOff.getPatch(), Color.YELLOW));
        TextButton.TextButtonStyle tbStyle = new TextButton.TextButtonStyle(
                npBtnOff, npBtnOn, null, font);
        btnPAP = new TextButton(lng.get(Words.PASS_AND_PLAY), tbStyle);
        btnPAP.addListener(btnListener);
        btnOnline = new TextButton(lng.get(Words.PLAY_ONLINE), tbStyle);
        btnOnline.addListener(btnListener);
        btnSettings = new TextButton(lng.get(Words.SETTINGS), tbStyle);
        btnSettings.addListener(btnListener);
        btnAbout = new TextButton(lng.get(Words.ABOUT), tbStyle);
        btnAbout.addListener(btnListener);

        logo = ResKeeper.get(TextureId.LOGO);
        logo.setFilter(Texture.TextureFilter.Linear,
                Texture.TextureFilter.Linear);

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
        dialogInfo.button("BtnTest");
        dialogInfo
                .text("Test text rgsrg rgsdrg srgsrtqwweffffffffffffffffffffffff\n SDRGRG АПЕРАЕ  іерівер вр\n\n  парчрывры ыр ывр ыв \nры  чвр ывр ");
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
        bg = ResKeeper.get(TextureId.BG);
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
        ResKeeper.dispose(TextureId.BG);
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
                // TODO: show select map dialog in future
                GameMap map = MapUtils.loadTileMap(null, true);
                manager.setScreen(new GameHandler(manager, map));
            } else if (a == btnOnline) {
            } else if (a == btnSettings) {
                manager.setScreen(new SettingsScreen(manager));
            } else if (a == btnAbout) {
                dialogInfo.show(stage);
            }
        }
    };

    @Override
    public void dispose() {
        ResKeeper.dispose(TextureId.BG);
        ResKeeper.dispose(TextureId.LOGO);
        ResKeeper.dispose(TextureId.BTN);
        manager.freeTitleFont();
    }
}
