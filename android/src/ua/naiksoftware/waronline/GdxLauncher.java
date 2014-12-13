package ua.naiksoftware.waronline;

import ua.naiksoftware.utils.bind.ParcelableBinder;
import ua.naiksoftware.waronline.game.editor.PlatformDirectEditorRunner;
import android.content.Intent;
import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.badlogic.gdx.maps.tiled.TiledMap;

public class GdxLauncher extends AndroidApplication {

	public static final String MODE = "m";
	
	public static final short PLAY = 1;
	public static final short EDIT = 2;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		config.numSamples = 2; // MSAA
		MyGame.getInstance().lng = lng;
		
		Intent i = getIntent();
		switch (i.getShortExtra(MODE, (short) 1)) {
		case PLAY:
			initialize(MyGame.getInstance(), config);
			break;
		case EDIT:
			TiledMap map = ((ParcelableBinder<TiledMap>) i.getParcelableExtra(AndroidLauncher.MAP)).getObj();
			initialize(new PlatformDirectEditorRunner(map), config);
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
