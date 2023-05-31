package com.fitness.fitnessCru.graph;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

public class Indicator extends View {

    private Path wallpath;
    private Paint paint;

    public Indicator(Context context) {
        super(context);
    }

    public Indicator(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public Indicator(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public Indicator(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        final float[] x = {canvas.getWidth() / 2, 0, canvas.getWidth(), canvas.getWidth() / 2};
        final float[] y = {50, (getMeasuredHeight() / 2) - 10, (getMeasuredHeight() / 2) - 10, 50};

        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.WHITE);

        wallpath = new Path();

        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        wallpath.reset();

        for (int i = 0; i < x.length; i++) {
            wallpath.lineTo(x[i], y[i]);
        }
        canvas.drawPath(wallpath, paint);
    }
}
