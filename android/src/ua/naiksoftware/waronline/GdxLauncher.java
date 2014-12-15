package ua.naiksoftware.waronline;

import ua.naiksoftware.waronline.res.Lng;
import ua.naiksoftware.waronline.res.Words;
import ua.naiksoftware.waronline.screenmanager.AndroidManager;
import android.content.Intent;
import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

public class GdxLauncher extends AndroidApplication {

	public static final String MODE = "m";

	public static final short SPLASH = 0;
	public static final short PLAY = 1;
	public static final short EDIT = 2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		config.numSamples = 2; // MSAA

		Intent i = getIntent();

		// TODO: get from intent
		int wMap = 20;
		int hMap = 30;
		String pathToMap = null;
		boolean internalMap = true;

		switch (i.getShortExtra(MODE, (short) 1)) {
		case SPLASH:
			initialize(new AndroidManager(
					AndroidManager.LaunchMode.SPLASH_SCREEEN, lng), config);
			break;
		case PLAY:
			initialize(new AndroidManager(AndroidManager.LaunchMode.PLAY, lng,
					pathToMap, internalMap), config);
			break;
		case EDIT:
			initialize(new AndroidManager(AndroidManager.LaunchMode.MAP_EDITOR,
					lng, wMap, hMap), config);
		}
	}

	private Lng lng = new Lng() {

		@Override
		public String get(Words key) {
			switch (key) {
			case PASS_AND_PLAY:
				return getString(R.string.pass_and_play);
			case PLAY_ONLINE:
				return getString(R.string.play_online);
			case SETTINGS:
				return getString(R.string.settings);
			case ABOUT:
				return getString(R.string.about);
			}
			return null;
		}
	};
}
