package ua.naiksoftware.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 *
 * @author Naik
 */
public class NumberPicker extends LinearLayout implements View.OnClickListener {

    private Button btnPlus, btnMinus;
    private TextView twValue;

    private int value, min, max;

    public NumberPicker(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    public NumberPicker(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public NumberPicker(Context context) {
        super(context);
        init(context);
    }

    private void init(Context c) {
        btnPlus = new Button(c);
        btnPlus.setText("+");
        btnPlus.setOnClickListener(this);

        btnMinus = new Button(c);
        btnMinus.setText("-");
        btnMinus.setOnClickListener(this);

        twValue = new TextView(c);
        twValue.setText(String.valueOf(value));

        setOrientation(HORIZONTAL);
        addView(btnPlus);
        addView(twValue);
        addView(btnMinus);
    }

    public void setMinValue(int m) {
        min = m;
        setValue(min);
    }

    public void setMaxValue(int m) {
        max = m;
        setValue(max);
    }

    public void setValue(int v) {
        if (v > max) {
            v = max;
        } else if (v < min) {
            v = min;
        }
        value = v;
        twValue.setText(String.valueOf(value));

        if (value == max) {
            btnPlus.setEnabled(false);
        } else {
            btnPlus.setEnabled(true);
        }

        if (value == min) {
            btnMinus.setEnabled(false);
        } else {
            btnMinus.setEnabled(true);
        }
    }

    public int getValue() {
        return value;
    }

    @Override
    public void onClick(View btn) {
        if (btn == btnPlus) {
            setValue(value + 1);
        } else {
            setValue(value - 1);
        }
    }
}
