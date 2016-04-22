package com.bleyl.pictorial.widgets;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class FractionView extends View {

    private Paint mPaint;
    private int mCurrentNumber;
    private int mMaxNumber;

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
        mPaint = new Paint();
        mPaint.setColor(Color.WHITE);
        mPaint.setAntiAlias(true);
        mPaint.setStrokeWidth(4);
        mPaint.setTextAlign(Paint.Align.CENTER);
        final float scale = getContext().getResources().getDisplayMetrics().density;
        mPaint.setTextSize((int) (12.0f * scale + 0.5f));
    }

    @Override
    public void onDraw(Canvas canvas) {
        canvas.drawText(Integer.toString(mCurrentNumber), getWidth() / 2, 30, mPaint);
        canvas.drawLine(10, getHeight() / 2, getWidth() - 10, getHeight() / 2, mPaint);
        canvas.drawText(Integer.toString(mMaxNumber), getWidth() / 2, getHeight(), mPaint);
    }

    public void setCurrentNumber(int number) {
        mCurrentNumber = number;
        invalidate();
    }

    public void setMaxNumber(int number) {
        mMaxNumber = number;
        invalidate();
    }
}