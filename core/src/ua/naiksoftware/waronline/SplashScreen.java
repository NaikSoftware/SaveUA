package ua.naiksoftware.waronline;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.alpha;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.delay;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.parallel;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.scaleTo;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;
import ua.naiksoftware.waronline.res.ResKeeper;
import ua.naiksoftware.waronline.res.id.TextureId;
import ua.naiksoftware.waronline.screenmanager.Manager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

/**
 * Заставка запуска, на таких платформах, как Android реализовано нативное меню
 * и заставка
 *
 * @author Naik
 *
 */
public class SplashScreen implements Screen {

    private Manager manager;
    private Stage stage;
    private Texture logo;
    private Image logoImage;
    private Label label;

    public SplashScreen(Manager manager) {
        this.manager = manager;
        label = new Label("NaikSoftware © 2014", manager.getSkin());
        label.setColor(Color.YELLOW);
        logo = ResKeeper.get(TextureId.LOGO);
        logoImage = new Image(logo);

        stage = new Stage(new ScreenViewport());
        stage.addActor(logoImage);
        stage.addActor(label);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);

        float scale = Math.min(width, height) / (logoImage.getWidth() * 1.3f);
        if (scale < 1) {
            logoImage.setScale(scale);
        } else {
            scale = 1;
        }
        logoImage.setPosition(width / 2 - logoImage.getWidth() / 2 * scale,
                height / 2 - logoImage.getHeight() / 2 * scale);
        logoImage.clearActions();
        logoImage.getColor().a = 0;
        logoImage.setScale(scale, 0.3f * scale);
        Action act = sequence(parallel(alpha(1, 3), scaleTo(scale, scale, 2)),
                delay(0.5f), parallel(alpha(0, 0.7f)), new RunnableAction() {
                    @Override
                    public void run() {
                        manager.showMenu();
                    }
                });
        logoImage.addAction(act);

        label.getColor().a = 0;
        label.setPosition(width / 2 - label.getWidth() / 2, 10);
        label.addAction(sequence(alpha(0.5f, 2), alpha(0, 2)));
    }

    @Override
    public void show() {
    }

    @Override
    public void hide() {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void dispose() {
        stage.dispose();
        ResKeeper.dispose(TextureId.LOGO);
    }
}
