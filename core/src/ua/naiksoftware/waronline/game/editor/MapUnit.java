package ua.naiksoftware.waronline.game.editor;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import ua.naiksoftware.waronline.game.Gamer;
import ua.naiksoftware.waronline.game.unit.UnitCode;
import ua.naiksoftware.waronline.res.ResKeeper;
import ua.naiksoftware.waronline.res.id.AtlasId;

/**
 *
 * @author Naik
 */
public class MapUnit extends Sprite {

    private static final int LABEL_RADIUS = 7;

    private final UnitCode code;
    private final Gamer gamer;
    private final Color color;
    private final int size;

    public MapUnit(UnitCode code, Gamer gamer) {
        this.code = code;
        this.gamer = gamer;
        TextureRegion region = getTextureRegion(code);
        setRegion(region);
        if (gamer != null) {
            this.color = gamer.getColor();
        } else {
            this.color = new Color(0, 0, 0, 0);
        }
        size = region.getRegionHeight();
        setSize(size, size);
    }

    public UnitCode getCode() {
        return code;
    }

    @Override
    public void draw(Batch batch) {
        super.draw(batch);
        if (gamer != null) {
        }
    }

    public Gamer getGamer() {
        return gamer;
    }

    public static TextureRegion getTextureRegion(UnitCode code) {
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

        TextureAtlas atlas = ResKeeper.get(AtlasId.UNIT_SPRITES);
        TextureRegion r = atlas.findRegion(name);
        if (r == null) {
            throw new IllegalArgumentException("Name \"" + name
                    + "\" in atlas units sprites not exists");
        }
        return r;
    }
}
