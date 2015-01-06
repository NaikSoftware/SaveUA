package ua.naiksoftware.libgdx.ui;

import com.badlogic.gdx.scenes.scene2d.ui.Widget;

/**
 * 
 * @author Naik
 */
public interface WidgetAdapter {
    int getCount();
    Widget getWidget(int n);
}
