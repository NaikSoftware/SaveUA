package ua.naiksoftware.waronline.client;

import ua.naiksoftware.waronline.screenmanager.DesktopManager;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;

public class HtmlLauncher extends GwtApplication {

        @Override
        public GwtApplicationConfiguration getConfig () {
                return new GwtApplicationConfiguration(480, 320);
        }

        @Override
        public ApplicationListener getApplicationListener () {
                return new DesktopManager(new Language());
        }
}