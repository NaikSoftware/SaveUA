package ua.naiksoftware.waronline.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import ua.naiksoftware.waronline.MyGame;

import java.awt.*;

public class DesktopLauncher {
	public static void main (String... arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		config.width = (int) screenSize.getWidth();
		config.height = (int) screenSize.getHeight();
		config.fullscreen = true;
		config.samples = 2; // MSAA
		MyGame.getInstance().lng = new Language();
		new LwjglApplication(MyGame.getInstance(), config);
	}
}
