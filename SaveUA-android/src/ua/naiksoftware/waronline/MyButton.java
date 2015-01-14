package ua.naiksoftware.waronline;

import android.content.Context;
import android.graphics.Point;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.Button;
import android.view.WindowManager;
import android.view.Display;

public class MyButton extends Button {

    private static int fontSize;

    public MyButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    public MyButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MyButton(Context context) {
        super(context);
        init(context);
    }

    private void init(Context c) {
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(),
                "fonts/albionic.ttf");
        setTypeface(tf);

        if (fontSize == 0) {
            Point p = new Point();
            WindowManager wm = (WindowManager) c.getSystemService(Context.WINDOW_SERVICE);
            Display display = wm.getDefaultDisplay();
            display.getSize(p);
            fontSize = Math.min(p.x, p.y) / 15;
            if (fontSize < 30) {
                fontSize = 30;
            }
        }
        setTextSize(fontSize);
    }
}
