package ua.naiksoftware.waronline.res;

import ua.naiksoftware.waronline.res.id.AtlasId;
import ua.naiksoftware.waronline.res.id.TextureId;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.ArrayMap;

/**
 * Менеджер ресурсов
 *
 * @author Naik
 */
public class ResKeeper {

    private static ArrayMap<TextureId, Texture> textures = new ArrayMap<TextureId, Texture>();
    private static ArrayMap<AtlasId, TextureAtlas> atlases = new ArrayMap<AtlasId, TextureAtlas>();

    public static Texture get(TextureId id) {
        Texture t = textures.get(id);
        if (t == null) {
            t = loadTexture(id);
            textures.put(id, t);
        }
        return t;
    }

    public static TextureAtlas get(AtlasId id) {
        TextureAtlas ta = atlases.get(id);
        if (ta == null) {
            ta = loadAtlas(id);
            atlases.put(id, ta);
        }
        return ta;
    }

    private static Texture loadTexture(TextureId id) {
        String path = null;
        switch (id) {
            case BG:
                path = "bg.jpg";
                break;
            case LOGO:
                path = "logo.png";
                break;
            case BTN:
                path = "btn_off.png";
                break;
        }
        Pixmap.Format format = TextureFormats.data.get(id);
        if (format == null) {
            format = Pixmap.Format.RGB888;
        }
        return new Texture(Gdx.files.internal(path), format, false);
    }

    private static TextureAtlas loadAtlas(AtlasId id) {
        String path = null;
        switch (id) {
            case MAP_TILES:
                path = "atlas/tiles.atlas";
                break;
            case EDITOR_IMAGES:
                path = "atlas/editor_images.atlas";
                break;
            case OVERLAY_IMAGES:
                path = "atlas/overlay_images.atlas";
                break;
            case UNIT_SPRITES:
                path = "atlas/sprites.atlas";
                break;
        }
        return new TextureAtlas(Gdx.files.internal(path));
    }

    public static void disposeAll() {
        for (int i = 0, len = textures.size; i < len; i++) {
            dispose(textures.getKeyAt(i));
        }
        for (int i = 0, len = atlases.size; i < len; i++) {
            dispose(atlases.getKeyAt(i));
        }
    }

    public static void dispose(TextureId id) {
        Texture t = textures.get(id);
        if (t != null) {
            t.dispose();
            textures.removeKey(id);
        }
    }

    public static void dispose(AtlasId id) {
        TextureAtlas ta = atlases.get(id);
        if (ta != null) {
            ta.dispose();
            atlases.removeKey(id);
        }
    }
}
