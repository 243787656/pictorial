package com.bleyl.pictorial.widgets;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class FractionView extends View {

    private Paint paint;
    private int currentNumber;
    private int maxNumber;

    public FractionView(Context context) {
        super(context);
        init();
    }

    public FractionView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public FractionView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public void init() {
        paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setAntiAlias(true);
        paint.setStrokeWidth(4);
        paint.setTextAlign(Paint.Align.CENTER);
        final float scale = getContext().getResources().getDisplayMetrics().density;
        paint.setTextSize((int) (12.0f * scale + 0.5f));
    }

    @Override
    public void onDraw(Canvas canvas) {
        canvas.drawText(Integer.toString(currentNumber), getWidth() / 2, 30, paint);
        canvas.drawLine(10, getHeight() / 2, getWidth() - 10, getHeight() / 2, paint);
        canvas.drawText(Integer.toString(maxNumber), getWidth() / 2, getHeight(), paint);
    }

    public void setCurrentNumber(int number) {
        currentNumber = number;
        invalidate();
    }

    public void setMaxNumber(int number) {
        maxNumber = number;
        invalidate();
    }
}