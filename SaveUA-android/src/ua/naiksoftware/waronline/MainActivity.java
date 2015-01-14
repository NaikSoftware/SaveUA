package ua.naiksoftware.waronline;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import ua.naiksoftware.waronline.map.MapEntry;
import ua.naiksoftware.waronline.map.MapUtils;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;
import com.badlogic.gdx.utils.Array;
import java.util.Random;
import ua.naiksoftware.widget.NumberPicker;

public class MainActivity extends Activity {

    private static final Random RND = new Random();

    private boolean gdxInit, blockMediaPause;
    private SharedPreferences prefs;
    private LayoutInflater inflater;
    private MediaPlayer mediaPlayer;

    private AlertDialog dialogStartGame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prefs = getSharedPreferences(Prefs.PREFS_NAME, MODE_PRIVATE);
        if (prefs.getBoolean(Prefs.ANDROID_GDX_MENU, false)) {
            Intent i = new Intent(this, GdxLauncher.class);
            i.putExtra(GdxLauncher.MODE, GdxLauncher.GDX_MENU);
            startActivityForResult(i, 0);
        } else {
            setContentView(R.layout.menu);
            ((ImageView) findViewById(R.id.menu_bg)).setImageResource(R.drawable.bg0 + RND.nextInt(4));
            inflater = LayoutInflater.from(this);

            Intent i = new Intent(this, GdxLauncher.class);
            i.putExtra(GdxLauncher.MODE, GdxLauncher.SPLASH);
            startActivityForResult(i, 0);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (prefs.getBoolean(Prefs.ANDROID_GDX_MENU, false)) {
            finish();
        } else if (gdxInit) {
            dialogStartGame = null; // for update level list in dialog
            if (mediaPlayer == null) {
                mediaPlayer = MediaPlayer.create(this, R.raw.entwine_oblivion);
                mediaPlayer.setVolume(0.1f, 0.1f);
                mediaPlayer.setLooping(true);
            }
            mediaPlayer.start();
        }
        gdxInit = true;
        blockMediaPause = false;
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mediaPlayer != null && !blockMediaPause) {
            mediaPlayer.pause();
        }
    }

    public void onClickStart(View view) {
        final boolean online = view.getId() == R.id.btn_online;
        if (dialogStartGame == null) {
            final View v = inflater.inflate(R.layout.dialog_start_game, null);
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setView(v);
            final Array<MapEntry> maps = MapUtils.readMapList();
            final NumberPicker pickGamers = (NumberPicker) v.findViewById(R.id.gamersNumberPicker);
            pickGamers.setMinValue(2);
            pickGamers.setMaxValue(maps.get(0).getMaxGamers());
            pickGamers.setValue(2);
            final ListView levels = (ListView) v.findViewById(R.id.start_level_list_view);
            LevelListAdapter adapter = new LevelListAdapter(this, maps);
            levels.setAdapter(adapter);
            levels.setSelection(0);
            levels.setItemChecked(0, true);
            levels.setOnItemClickListener(new OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> p1, View p2, int pos, long id) {
                    int max = maps.get(levels.getCheckedItemPosition()).getMaxGamers();
                    pickGamers.setMaxValue(max);
                }
            });
            builder.setNegativeButton(R.string.cancel, null);
            builder.setPositiveButton(R.string.ok,
                    new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface p1, int p2) {
                            mediaPlayer.stop();
                            mediaPlayer.release();
                            mediaPlayer = null;
                            MapEntry entry = maps.get(levels.getCheckedItemPosition());
                            Toast.makeText(MainActivity.this, "Start game on map " + entry.getName(), Toast.LENGTH_SHORT).show();
                            finish();
                            //Intent i = new Intent(this, GdxLauncher.class);
                            //startActivityForResult(i, 0);
                        }
                    });
            dialogStartGame = builder.create();
        }

        dialogStartGame.show();
    }

    public void onClickSettings(View v) {
        blockMediaPause = true;
        Intent i = new Intent(this, SettingsActivity.class);
        startActivityForResult(i, 0);
    }

    @Override
    protected void onDestroy() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
        super.onDestroy();
    }
}
