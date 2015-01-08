package ua.naiksoftware.libgdx.ui;

import com.badlogic.gdx.scenes.scene2d.ui.*;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;

/**
 *
 * @author Naik
 */
public class WidgetList extends WidgetGroup {

    private List.ListStyle style;
    private int size;
    private WidgetAdapter adapter;
    private ScrollPane scrollPane;

    private float prefWidth, prefHeight;

    private int selected, last;
    private Array<Actor> items;

    public WidgetList(WidgetAdapter adapter, Skin skin) {
        this(adapter, skin.get(List.ListStyle.class));
    }

    public WidgetList(WidgetAdapter adapter, List.ListStyle style) {
        this.adapter = adapter;
        this.style = style;
        size = adapter.getCount();
        items = new Array<Actor>(size);
        Table t = new Table();
        t.defaults().uniformX().fillX();
        for (int i = 0; i < size; i++) {
            Actor a = adapter.createItem(i);
            if (style.background == null && a instanceof Table) {
                style.background = ((Table) a).getBackground();
            }
            t.add(a).row();
            items.add(a);
            a.addListener(selectedListener);
        }
        scrollPane = new ScrollPane(t);
        scrollPane.pack();
        addActor(scrollPane);
        selected = last = 0;
    }

    /**
     * Вызывается не напрямую, а после validate()
     */
    @Override
    public void layout() {
        prefWidth = scrollPane.getPrefWidth();
        prefHeight = scrollPane.getPrefHeight();
        if (size > 0) {
            Actor a = items.get(last), a2 = items.get(selected);
            if (a instanceof Table) {
                ((Table) a).setBackground(style.background);
            } else {
                a.setColor(style.fontColorUnselected);
            }
            if (a2 instanceof Table) {
                ((Table) a2).setBackground(style.selection);
            } else {
                a2.setColor(style.fontColorSelected);
            }
        }
    }

    @Override
    public final float getPrefWidth() {
        validate();
        return prefWidth;
    }

    @Override
    public final float getPrefHeight() {
        validate();
        return prefHeight;
    }

    private final ClickListener selectedListener = new ClickListener() {
        @Override
        public void clicked(InputEvent event, float x, float y) {
            last = selected;
            selected = items.indexOf(event.getListenerActor(), true);
            invalidate();
        }
    };

    public int getSelectedIndex() {
        return selected;
    }

    /**
     * @param <T> - элементы для WidgetList - например Table, Label
     */
    public interface WidgetAdapter<T extends Actor> {

        int getCount();

        T createItem(int n);
    }
}
