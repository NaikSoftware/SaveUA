package ua.naiksoftware.waronline;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.content.SharedPreferences;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import com.badlogic.gdx.utils.Array;
import java.util.ArrayList;
import java.util.List;
import ua.naiksoftware.waronline.map.MapEntry;
import ua.naiksoftware.waronline.map.MapUtils;

public class AndroidLauncher extends Activity {

    private int screen;
    private static final int SCREEN_SPLASH = 0;
    private static final int SCREEN_MENU = 1;
    private static final int SCREEN_SETTINGS = 2;

    private SharedPreferences prefs;

    private LayoutInflater inflater;
    private AlertDialog dialogNewMap;
    private AlertDialog dialogEditMap;

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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (prefs.getBoolean(Prefs.ANDROID_GDX_MENU, false)) {
            finish();
        } else {
            screen = SCREEN_MENU;
            setContentView(R.layout.menu);
        }
    }

    public void onClickPAP(View v) {
        Intent i = new Intent(this, GdxLauncher.class);
        startActivityForResult(i, 0);
    }

    public void onClickSettings(View v) {
        screen = SCREEN_SETTINGS;
        setContentView(R.layout.settings);
        Spinner mapSpinner = (Spinner) findViewById(R.id.settingsSpinner_maps);
        List<String> list = new ArrayList<String>();
        for (MapEntry e : MapUtils.readMapList()){
            list.add(e.toString());
        }
        ArrayAdapter adapter = new ArrayAdapter(this, R.layout.spinner_simple_item, R.id.spinner_item_label, list);
        mapSpinner.setAdapter(adapter);
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
            setContentView(R.layout.menu);
        } else {
            super.onBackPressed();
        }
    }
}
