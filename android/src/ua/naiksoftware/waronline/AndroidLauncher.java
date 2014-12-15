package ua.naiksoftware.waronline;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class AndroidLauncher extends Activity {

	public static final String MAP = "map";

	private int screen;
	private static final int SCREEN_SPLASH = 0;
	private static final int SCREEN_MENU = 1;
	private static final int SCREEN_SETTINGS = 2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent i = new Intent(this, GdxLauncher.class);
		i.putExtra(GdxLauncher.MODE, GdxLauncher.SPLASH);
		startActivityForResult(i, 0);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (screen == SCREEN_SPLASH) {
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
	}
	
	public void onClickDeleteMap(View v) {
		
	}
	
	public void onClickEditMap(View v) {
		
	}
	
	public void onClickMakeMap(View v) {
		Intent i = new Intent(this, GdxLauncher.class);
		i.putExtra(GdxLauncher.MODE, GdxLauncher.EDIT);
		//TODO: put map params, e.g. width and height
		startActivityForResult(i, 0);
	}

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
