package ua.naiksoftware.waronline.res;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.ArrayMap;

public class ResKeeper {

	private static ArrayMap<TextureId, Texture> textures = new ArrayMap<TextureId, Texture>();

	public static Texture get(TextureId id) {
		Texture t = textures.get(id);
		if (t == null) {
			t = loadTexture(id);
			textures.put(id, t);
		}
		return t;
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
		}
		Pixmap.Format format = TextureFormats.data.get(id);
		if (format == null)
			format = Pixmap.Format.RGB888;
		return new Texture(Gdx.files.internal(path), format, false);
	}

	public static void disposeAll() {
		for (int i = 0, len = textures.size; i < len; i++) {
			dispose(textures.getKeyAt(i));
		}
	}

	public static void dispose(TextureId id) {
		Texture t = textures.get(id);
		if (t != null) {
			t.dispose();
			textures.removeKey(id);
		}
	}

	public static void dispose(Texture t) {
		if (t != null) {
			t.dispose();
			textures.removeValue(t, true);
		}
	}
}
