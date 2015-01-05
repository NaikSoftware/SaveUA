package ua.naiksoftware.waronline.game.editor;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import ua.naiksoftware.waronline.game.Gamer;
import ua.naiksoftware.waronline.game.UnitHelper;
import ua.naiksoftware.waronline.game.unit.UnitCode;
import ua.naiksoftware.waronline.res.ResKeeper;
import ua.naiksoftware.waronline.res.id.AtlasId;
import ua.naiksoftware.waronline.game.unit.Unit;

/**
 *
 * @author Naik
 */
public class MapUnit extends Sprite {

    private static final int BAR_SHIFT = getTextureRegion(UnitCode.BTR_4E).getRegionHeight();

    private final UnitCode code;
    private final Gamer gamer;
    private final TextureRegion lifeBar;
    private final Texture lifeLabel;

    public MapUnit(UnitCode code, Gamer gamer) {
        this.code = code;
        this.gamer = gamer;
        TextureRegion region = getTextureRegion(code);
        setRegion(region);
        int size = region.getRegionHeight();
        setSize(size, size);
        this.lifeBar = ResKeeper.get(AtlasId.OVERLAY_IMAGES).findRegion("life_bar");
        Color c = gamer == null ? Unit.COLOR_FREE_UNITS : gamer.getColor();
        lifeLabel = UnitHelper.getLifeBar(c);
    }

    public UnitCode getCode() {
        return code;
    }

    @Override
    public void draw(Batch batch) {
        super.draw(batch);
        float x = getX(), y = getY();
        batch.draw(lifeBar, x, y + BAR_SHIFT);
        batch.draw(lifeLabel, x, y + BAR_SHIFT);
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
