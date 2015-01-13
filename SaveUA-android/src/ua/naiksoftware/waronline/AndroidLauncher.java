package ua.naiksoftware.waronline;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CompoundButton.OnCheckedChangeListener;
import java.util.ArrayList;
import java.util.List;
import ua.naiksoftware.waronline.map.MapEntry;
import ua.naiksoftware.waronline.map.MapUtils;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.badlogic.gdx.utils.Array;
import java.util.Random;
import ua.naiksoftware.widget.NumberPicker;

public class AndroidLauncher extends Activity {

    private static final Random RND = new Random();

    private int screen;
    private static final int SCREEN_SPLASH = 0;
    private static final int SCREEN_MENU = 1;
    private static final int SCREEN_SETTINGS = 2;
    private boolean gdxInit;

    private SharedPreferences prefs;

    private LayoutInflater inflater;
    private AlertDialog dialogNewMap;
    private AlertDialog dialogEditMap;
    private AlertDialog dialogStartGame;

    private List<String> levelList;
    private Array<MapEntry> maps;
    private MapEntry currMap;

    private CheckBox checkBoxGdxMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prefs = getSharedPreferences(Prefs.PREFS_NAME, MODE_PRIVATE);
        if (prefs.getBoolean(Prefs.ANDROID_GDX_MENU, false)) {
            screen = -1;
            Intent i = new Intent(this, GdxLauncher.class);
            i.putExtra(GdxLauncher.MODE, GdxLauncher.GDX_MENU);
            startActivityForResult(i, 0);
        } else {
            screen = SCREEN_SPLASH;
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
            showMainMenu();
        }
        gdxInit = true;
    }

    private void showMainMenu() {
        levelList = getLevelList();
        dialogStartGame = null; // for reload levels list
        screen = SCREEN_MENU;
        setContentView(R.layout.menu);
        Point p = new Point();
        getWindowManager().getDefaultDisplay().getSize(p);
        int size = Math.min(p.x, p.y) / 15;
        ViewGroup group = (ViewGroup) findViewById(R.id.menu_container);
        for (int count = group.getChildCount(), i = 0; i < count; i++) {
            ((Button) group.getChildAt(i)).setTextSize(size < 30 ? 30 : size);
        }
        ((ImageView) findViewById(R.id.menu_bg)).setImageResource(R.drawable.bg0 + RND.nextInt(4));
    }

    public void onClickStart(View view) {
        final boolean online = view.getId() == R.id.btn_online;
        if (dialogStartGame == null) {
            final View v = inflater.inflate(R.layout.dialog_start_game, null);
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setView(v);
            final NumberPicker pickGamers = (NumberPicker) v.findViewById(R.id.gamersNumberPicker);
            pickGamers.setMinValue(2);
            pickGamers.setMaxValue(maps.get(0).getMaxGamers());
            pickGamers.setValue(2);
            final ListView levels = (ListView) v.findViewById(R.id.start_level_list_view);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.spinner_simple_item, R.id.spinner_item_label, levelList);
            levels.setAdapter(adapter);
            levels.setSelection(0);
            levels.setItemChecked(0, true);
            levels.setOnItemClickListener(new OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> p1, View p2, int pos, long id) {
                    pickGamers.setMaxValue(maps.get(levels.getCheckedItemPosition()).getMaxGamers());
                }
            });
            builder.setNegativeButton(R.string.cancel, null);
            builder.setPositiveButton(R.string.ok,
                    new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface p1, int p2) {
                            //Intent i = new Intent(this, GdxLauncher.class);
                            //startActivityForResult(i, 0);
                        }
                    });
            dialogStartGame = builder.create();
        }

        dialogStartGame.show();
    }

    public void onClickSettings(View v) {
        screen = SCREEN_SETTINGS;
        setContentView(R.layout.settings);
        Spinner mapSpinner = (Spinner) findViewById(R.id.settingsSpinner_maps);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.spinner_simple_item, R.id.spinner_item_label, levelList);
        mapSpinner.setAdapter(adapter);
        mapSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> arg0, View arg1, int pos, long arg3) {
                currMap = maps.get(pos);
            }

            public void onNothingSelected(AdapterView<?> arg0) {
                currMap = maps.get(0);
            }
        });
        checkBoxGdxMenu = (CheckBox) findViewById(R.id.settingsCheckBox_gdx_menu);
        checkBoxGdxMenu.setChecked(prefs.getBoolean(Prefs.ANDROID_GDX_MENU, false));
        checkBoxGdxMenu.setOnCheckedChangeListener(checkListener);
    }

    public void onClickDeleteMap(View v) {

    }

    public void onClickEditMap(View view) {
        if (dialogEditMap == null) {
            final View v = inflater.inflate(R.layout.dialog_new_map, null);
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setView(v);
            v.findViewById(R.id.new_map_container).setVisibility(View.GONE);
            builder.setNegativeButton(R.string.cancel, null);
            builder.setPositiveButton(R.string.ok,
                    new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface p1, int p2) {
                            String name = ((TextView) v
                            .findViewById(R.id.dialog_new_mapEditText_name))
                            .getText().toString();
                            if (name.isEmpty()) {
                                Toast.makeText(AndroidLauncher.this,
                                        "Дурак штоле?", Toast.LENGTH_SHORT)
                                .show();
                            } else {
                                Intent i = new Intent(AndroidLauncher.this,
                                        GdxLauncher.class);
                                i.putExtra(GdxLauncher.MODE, GdxLauncher.EDIT);
                                i.putExtra(GdxLauncher.MAP_NAME, name);
                                i.putExtra(GdxLauncher.MAP_PATH, currMap.getPath());
                                startActivityForResult(i, 0);
                            }
                        }
                    });
            dialogEditMap = builder.create();
        }
        dialogEditMap.show();
    }

    public void onClickMakeMap(View view) {
        if (dialogNewMap == null) {
            final View v = inflater.inflate(R.layout.dialog_new_map, null);
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setView(v);
            builder.setNegativeButton(R.string.cancel, null);
            builder.setPositiveButton(R.string.ok,
                    new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface p1, int p2) {
                            String name = ((TextView) v
                            .findViewById(R.id.dialog_new_mapEditText_name))
                            .getText().toString();
                            int w, h;
                            try {
                                w = Integer.parseInt("0"
                                        + ((TextView) v.findViewById(R.id.dialog_new_mapEditText_w))
                                        .getText().toString());
                                h = Integer.parseInt("0"
                                        + ((TextView) v.findViewById(R.id.dialog_new_mapEditText_h))
                                        .getText().toString());
                            } catch (NumberFormatException e) {
                                w = h = 0;
                            }
                            if (w < 5 || h < 5 || w > 1000 || h > 1000
                            || name.isEmpty()) {
                                Toast.makeText(AndroidLauncher.this,
                                        "Дурак штоле?", Toast.LENGTH_SHORT)
                                .show();
                            } else {
                                Intent i = new Intent(AndroidLauncher.this,
                                        GdxLauncher.class);
                                i.putExtra(GdxLauncher.MODE, GdxLauncher.EDIT);
                                i.putExtra(GdxLauncher.MAP_NAME, name);
                                i.putExtra(GdxLauncher.MAP_W, w);
                                i.putExtra(GdxLauncher.MAP_H, h);
                                startActivityForResult(i, 0);
                            }
                        }
                    });
            dialogNewMap = builder.create();
        }
        dialogNewMap.show();
    }

    private final OnCheckedChangeListener checkListener = new OnCheckedChangeListener() {

        @Override
        public void onCheckedChanged(CompoundButton btn, boolean checked) {
            SharedPreferences.Editor editor = prefs.edit();
            if (btn == checkBoxGdxMenu) {
                editor.putBoolean(Prefs.ANDROID_GDX_MENU, checked);
                editor.commit();
                if (checked) {
                    Toast.makeText(AndroidLauncher.this, "Restart required", Toast.LENGTH_SHORT).show();
                }
            }
        }
    };

    @Override
    public void onBackPressed() {
        if (screen == SCREEN_SETTINGS) {
            screen = SCREEN_MENU;
            showMainMenu();
        } else {
            super.onBackPressed();
        }
    }

    private List<String> getLevelList() {
        List<String> list = new ArrayList<String>();
        maps = MapUtils.readMapList();
        for (MapEntry e : maps) {
            list.add(e.toString());
        }
        return list;
    }
}
