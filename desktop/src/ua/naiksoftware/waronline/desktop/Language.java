package ua.naiksoftware.waronline.desktop;

import java.util.Locale;

import ua.naiksoftware.waronline.res.Lng;
import ua.naiksoftware.waronline.res.Words;

import com.badlogic.gdx.utils.ObjectMap;

/**
 * Created by Naik on 08.12.14.
 */
public class Language implements Lng {

	private ObjectMap<Words, String> en = new ObjectMap<Words, String>() {
		{
			put(Words.PASS_AND_PLAY, "Pass-and-Play");
			put(Words.PLAY_ONLINE, "Play Online");
			put(Words.SETTINGS, "Settings");
			put(Words.ABOUT, "About");
			put(Words.BACK, "Back");
			put(Words.NEXT, "Next");
			put(Words.BUILD_MAP, "Build map");
		}
	};

	private ObjectMap<Words, String> ua = new ObjectMap<Words, String>() {
		{
			put(Words.PASS_AND_PLAY, "Передай-i-Грай");
			put(Words.PLAY_ONLINE, "Грати Онлайн");
			put(Words.SETTINGS, "Налаштування");
			put(Words.ABOUT, "Про гру");
			put(Words.BACK, "Назад");
			put(Words.NEXT, "Далi");
			put(Words.BUILD_MAP, "Побудуйте мапу");
			
		}
	};

	private ObjectMap<Words, String> ru = new ObjectMap<Words, String>() {
		{
			put(Words.PASS_AND_PLAY, "Передай-и-Играй");
			put(Words.PLAY_ONLINE, "Играть Онлайн");
			put(Words.SETTINGS, "Настройки");
			put(Words.ABOUT, "Об игре");
			put(Words.BACK, "Назад");
			put(Words.NEXT, "Далее");
			put(Words.BUILD_MAP, "Постройте карту");
		}
	};

	@Override
	public String get(Words key) {
		ObjectMap<Words, String> l;
		String tag = Locale.getDefault().toLanguageTag().toUpperCase();
		if (tag.contains("UA"))
			l = ua;
		else if (tag.contains("RU"))
			l = ru;
		else
			l = en;
		return l.get(key);
	}
}
