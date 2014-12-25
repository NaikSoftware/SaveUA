package ua.naiksoftware.waronline.game.editor;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import ua.naiksoftware.waronline.game.Gamer;
import ua.naiksoftware.waronline.game.unit.UnitCode;
import ua.naiksoftware.waronline.res.ResKeeper;

/**
 *
 * @author Naik
 */
public class EditUnit extends Image {

    private static final ShapeRenderer renderer = ResKeeper.getShapeRenderer();
    private static final int LABEL_RADIUS = 20;

    private final UnitCode code;
    private final Gamer gamer;
    private final Color color;

    public EditUnit(Drawable d, UnitCode code, Gamer gamer) {
        super(d);
        this.code = code;
        this.gamer = gamer;
        this.color = gamer.getColor();
    }

    public UnitCode getCode() {
        return code;
    }

    @Override
    public void draw(Batch batch, float alphaModulation) {
        super.draw(batch, alphaModulation);
        //batch.end();
        //renderer.begin(ShapeRenderer.ShapeType.Filled);
        //renderer.setColor(color);
        //renderer.circle(getX() + LABEL_RADIUS / 2, getY() - LABEL_RADIUS / 2, LABEL_RADIUS);
        //renderer.end();
        //batch.begin();
    }
    
    public Gamer getGamer() {
        return gamer;
    }
}
