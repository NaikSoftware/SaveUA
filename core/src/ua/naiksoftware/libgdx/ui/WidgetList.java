package ua.naiksoftware.libgdx.ui;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.Widget;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

/**
 *
 * @author Naik
 */
public class WidgetList extends Widget {

    private List.ListStyle style;
    private int size;
    private WidgetAdapter adapter;

    private Rectangle cullingArea;
    private float prefWidth, prefHeight;
    private float itemHeight;
    private float textOffsetX, textOffsetY;

    public WidgetList(WidgetAdapter adapter, List.ListStyle style) {
        this.adapter = adapter;
        this.style = style;
        size = adapter.getCount();
        addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (pointer == 0 && button != 0) {// так надо
                    return false;
                }
                WidgetList.this.touchDown(y);
                return true;
            }
        });
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        validate();

        float x = getX(), y = getY(), width = getWidth(), height = getHeight();
        float itemY = height;

        Drawable background = style.background;
        if (background != null) {
            background.draw(batch, x, y, width, height);
            float leftWidth = background.getLeftWidth();
            x += leftWidth;
            itemY -= background.getTopHeight();
            width -= leftWidth + background.getRightWidth();
        }

        for (int i = 0; i < size; i++) {
            if (cullingArea == null || (itemY - itemHeight <= cullingArea.y + cullingArea.height && itemY >= cullingArea.y)) {
                T item = items.get(i);
                boolean selected = selection.contains(item);
                if (selected) {
                    style.selectedDrawable.draw(batch, x, y + itemY - itemHeight, width, itemHeight);
                }
                font.draw(batch, item.toString(), x + textOffsetX, y + itemY - textOffsetY);
            } else if (itemY < cullingArea.y) {
                break;
            }
            itemY -= itemHeight;
        }
    }

    private void touchDown(float y) {
        if (size == 0) {
            return;
        }
        float height = getHeight();
        if (style.background != null) {
            height -= style.background.getTopHeight() + style.background.getBottomHeight();
            y -= style.background.getBottomHeight();
        }
        int index = (int) ((height - y) / itemHeight);
        index = Math.max(0, index);
        index = Math.min(size - 1, index);
        selection.choose(items.get(index));
    }

    /**
     * Вызывается не напрямую, а после validate()
     */
    @Override
    public void layout() {
        final Drawable selectedDrawable = style.selection;

        itemHeight = font.getCapHeight() - font.getDescent() * 2;
        itemHeight += selectedDrawable.getTopHeight() + selectedDrawable.getBottomHeight();

        textOffsetX = selectedDrawable.getLeftWidth();
        textOffsetY = selectedDrawable.getTopHeight() - font.getDescent();

        prefWidth = 0;
        for (int i = 0; i < items.size; i++) {
            TextBounds bounds = font.getBounds(items.get(i).toString());
            prefWidth = Math.max(bounds.width, prefWidth);
        }
        prefWidth += selectedDrawable.getLeftWidth() + selectedDrawable.getRightWidth();
        prefHeight = size * itemHeight;

        Drawable background = style.background;
        if (background != null) {
            prefWidth += background.getLeftWidth() + background.getRightWidth();
            prefHeight += background.getTopHeight() + background.getBottomHeight();
        }
    }
}
