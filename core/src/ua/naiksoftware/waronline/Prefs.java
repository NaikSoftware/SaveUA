package ua.naiksoftware.waronline;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

/**
 * @author Naik
 */
public class Prefs {
	
	public static final String PREFS_NAME = "prefs";
	public static final String ANDROID_GDX_MENU = "pagm";
	
	private static Preferences prefs = Gdx.app.getPreferences(PREFS_NAME);
	
	public static boolean is(String key) {
		return prefs.getBoolean(key);
	}
	
	public static void put(String key, boolean b) {
		prefs.putBoolean(key, b);
		prefs.flush();
	}
}
