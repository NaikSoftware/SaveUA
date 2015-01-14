package ua.naiksoftware.waronline;

import android.widget.*;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.CompoundButton.OnCheckedChangeListener;
import com.badlogic.gdx.utils.Array;
import java.util.ArrayList;
import java.util.List;
import ua.naiksoftware.waronline.map.MapEntry;
import ua.naiksoftware.waronline.map.MapUtils;

public class SettingsActivity extends Activity {

    private SharedPreferences prefs;

    private LayoutInflater inflater;
    private AlertDialog dialogNewMap;
    private AlertDialog dialogEditMap;

    private Spinner mapSpinner;
    private CheckBox checkBoxGdxMenu;

    private Array<MapEntry> maps;
    private MapEntry currMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prefs = getSharedPreferences(Prefs.PREFS_NAME, MODE_PRIVATE);
        inflater = LayoutInflater.from(this);

        setContentView(R.layout.settings);
        mapSpinner = (Spinner) findViewById(R.id.settingsSpinner_maps);
        checkBoxGdxMenu = (CheckBox) findViewById(R.id.settingsCheckBox_gdx_menu);
        checkBoxGdxMenu.setChecked(prefs.getBoolean(Prefs.ANDROID_GDX_MENU, false));
        checkBoxGdxMenu.setOnCheckedChangeListener(checkListener);
    }

    @Override
    protected void onStart() {
        super.onStart();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.spinner_simple_item, R.id.spinner_item_label, getLevelList());
        mapSpinner.setAdapter(adapter);
        mapSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> arg0, View arg1, int pos, long arg3) {
                currMap = maps.get(pos);
            }

            public void onNothingSelected(AdapterView<?> arg0) {
                currMap = maps.get(0);
            }
        });
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
                                Toast.makeText(SettingsActivity.this,
                                        "Дурак штоле?", Toast.LENGTH_SHORT)
                                .show();
                            } else {
                                Intent i = new Intent(SettingsActivity.this,
                                        GdxLauncher.class);
                                i.putExtra(GdxLauncher.MODE, GdxLauncher.EDIT);
                                i.putExtra(GdxLauncher.MAP_NAME, name);
                                i.putExtra(GdxLauncher.MAP_PATH, currMap.getPath());
                                i.putExtra(GdxLauncher.MAP_LOCAL, currMap.isLocal());
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
                                Toast.makeText(SettingsActivity.this,
                                        "Дурак штоле?", Toast.LENGTH_SHORT)
                                .show();
                            } else {
                                Intent i = new Intent(SettingsActivity.this,
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
                    Toast.makeText(SettingsActivity.this, "Restart required", Toast.LENGTH_SHORT).show();
                }
            }
        }
    };

    private List<String> getLevelList() {
        List<String> list = new ArrayList<String>();
        maps = MapUtils.readMapList();
        currMap = maps.get(0);
        for (MapEntry e : maps) {
            list.add(e.toString());
        }
        return list;
    }
}
