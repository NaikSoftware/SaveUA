package ua.naiksoftware.waronline;

import android.content.Intent;
import android.os.Bundle;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import ua.naiksoftware.waronline.map.MapEntry;
import ua.naiksoftware.waronline.res.Lng;
import ua.naiksoftware.waronline.res.ResKeeper;
import ua.naiksoftware.waronline.res.Words;
import ua.naiksoftware.waronline.screenmanager.AndroidManager;

public class GdxLauncher extends AndroidApplication {

    public static final String MODE = "m";

    public static final short SPLASH = 0;
    public static final short PLAY = 1;
    public static final short EDIT = 2;
    public static final short GDX_MENU = 3;

    public static final String MAP_NAME = "mapname";
    public static final String MAP_W = "mapw";
    public static final String MAP_H = "maph";
    public static final String MAP_PATH = "mappath";
    public static final String MAP_INTERNAL = "mapinternal";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ResKeeper.disposeAll(); // бывает после некоректного завершения ресурсы остаются неочищенными

        AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
        config.numSamples = 2; // MSAA

        Intent i = getIntent();

        int wMap = i.getIntExtra(MAP_W, 10);
        int hMap = i.getIntExtra(MAP_H, 10);
        String mapName = i.getStringExtra(MAP_NAME);
        String pathToMap = i.getStringExtra(MAP_PATH);
        boolean internalMap = i.getBooleanExtra(MAP_INTERNAL, false);

        switch (i.getShortExtra(MODE, (short) 1)) {
            case SPLASH:
                initialize(new AndroidManager(
                        AndroidManager.LaunchMode.SPLASH_SCREEEN, lng), config);
                break;
            case PLAY:
                initialize(new AndroidManager(AndroidManager.LaunchMode.PLAY, lng,
                        new MapEntry(pathToMap, internalMap)), config);
                break;
            case EDIT:
                if (pathToMap == null) {
                    initialize(new AndroidManager(AndroidManager.LaunchMode.MAP_EDITOR,
                            lng, wMap, hMap, mapName), config);
                } else {
                    initialize(new AndroidManager(AndroidManager.LaunchMode.MAP_EDITOR,
                            lng, new MapEntry(pathToMap, internalMap), mapName), config);
                }
                break;
            case GDX_MENU:
                initialize(new AndroidManager(AndroidManager.LaunchMode.GDX_MENU, lng));
                break;
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
                case BACK:
                    return getString(R.string.back);
                case NEXT:
                    return getString(R.string.next);
                case ADD:
                    return getString(R.string.add);
                case EDIT:
                    return getString(R.string.edit);
                case DELETE:
                    return getString(R.string.delete);
                case INPUT_MAP_NAME:
                    return getString(R.string.input_map_name);
                case INPUT_MAP_SIZE:
                    return getString(R.string.input_map_size);
                case SELECT_MAP:
                    return getString(R.string.select_map);
                case BUILD_MAP:
                    return getString(R.string.build_map);
                case OK:
                    return getString(R.string.ok);
                case CANCEL:
                    return getString(R.string.cancel);
                case EXIT:
                    return getString(R.string.exit);
                case LOCATE_OBJECTS:
                    return getString(R.string.locate_objects);
                case UNITS_GAMER:
                    return getString(R.string.units_gamer);
                case FREE_UNITS:
                    return getString(R.string.free_units);
                case MIN_TWO_GAMERS_REQUIRED:
                    return getString(R.string.min_two_gamers_req);
                case SAVE_MAP_AND_EXIT:
                    return getString(R.string.save_map_and_exit);
                case SIZE:
                    return getString(R.string.size);
                case MAX_GAMERS:
                    return getString(R.string.max_gamers);
            }
            return null;
        }
    };
}
