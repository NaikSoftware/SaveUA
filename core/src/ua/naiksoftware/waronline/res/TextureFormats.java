package ua.naiksoftware.waronline.res;

import com.badlogic.gdx.utils.ArrayMap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import static com.badlogic.gdx.graphics.Pixmap.Format.*;
import static ua.naiksoftware.waronline.res.TextureId.*;

public class TextureFormats {

	/** По умолчанию формат RGB888 */
	public static ArrayMap<TextureId, Format> data = new ArrayMap<TextureId, Format>() {
		{
			put(LOGO, RGBA8888);
			put(BTN, RGBA8888);
		}
	};
}
