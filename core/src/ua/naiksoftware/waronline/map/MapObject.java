package ua.naiksoftware.waronline.map;

import ua.naiksoftware.waronline.map.MapObjCode;
import ua.naiksoftware.waronline.res.ResKeeper;
import ua.naiksoftware.waronline.res.id.AtlasId;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.utils.Array;

/**
 * Builddings, trees, etc.
 *
 * @author Naik
 */
public class MapObject extends Sprite {

    private final MapObjCode code;
    private boolean animated;
    private Animation anim;
    private static final float ANIM_FRAME_DURATION = 0.1f;

    public MapObject(MapObjCode code) {
        this.code = code;
        Array<AtlasRegion> regions = getAtlasRegions(code);
        if (regions.size > 1) {
            animated = true;
            anim = new Animation(ANIM_FRAME_DURATION, regions,
                    Animation.PlayMode.LOOP);
        } else {
            setRegion(regions.first());
        }
        setSize(regions.first().getRegionWidth(), regions.first()
                .getRegionHeight());
    }
    
    /**
     * 
     * @param code
     * @param x
     * @param y
     */
    public MapObject(MapObjCode code, int x, int y) {
        this(code);
        setPosition(x, y);
    }

    @Override
    public void draw(Batch batch, float alphaModulation) {
        if (animated) {
            setRegion(anim.getKeyFrame(Gdx.graphics.getDeltaTime()));
        }
        super.draw(batch, alphaModulation);
    }

    public MapObjCode getMapObjCode() {
        return code;
    }

    public static final Array<AtlasRegion> getAtlasRegions(MapObjCode code) {
        String name = null;
        switch (code) {
            case HATA_1:
                name = "hata";
                break;
            case FORT:
                name = "fort2x2";
                break;
            case ATB_1:
                name = "atb3x1";
                break;
            case ATB_2:
                name = "atb3x2";
                break;
            case CHURCH:
                name = "church2x1";
                break;
            case REMAINS1:
                name = "remains1-2x2";
                break;
            case REMAINS2:
                name = "remains2-2x1";
                break;
            case HATA_2:
                name = "hata2x2";
                break;
            case TREE_1:
                name = "tree2";
                break;
            case TREE_2:
                name = "tree7";
                break;
            case HATA_3:
                name = "oldhata2x1";
                break;
            case HATA_4:
                name = "hata1";
                break;
            case TENT:
                name = "tent2x1";
                break;
            case STOLB_1:
                name = "stolb1x1";
                break;
            case STOLB_2:
                name = "stolb2-1x1";
                break;
            case WELL:
                name = "well";
                break;
            case KPP:
                name = "kpp1x2";
                break;
        }

        if (name == null) {
            throw new IllegalArgumentException("Name \"" + name
                    + "\" in atlas objects not exists");
        }

        TextureAtlas atlas = ResKeeper.get(AtlasId.OVERLAY_IMAGES);
        return atlas.findRegions(name);
    }
}
