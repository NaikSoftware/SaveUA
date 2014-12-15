package ua.naiksoftware.waronline.desktop;

import java.awt.Dimension;
import java.awt.Toolkit;

import ua.naiksoftware.waronline.screenmanager.DesktopManager;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class DesktopLauncher {
	public static void main(String... arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		config.width = (int) screenSize.getWidth();
		config.height = (int) screenSize.getHeight();
		config.fullscreen = true;
		config.samples = 2; // MSAA
		DesktopManager manager = new DesktopManager(new Language());
		new LwjglApplication(manager, config);
	}
}
