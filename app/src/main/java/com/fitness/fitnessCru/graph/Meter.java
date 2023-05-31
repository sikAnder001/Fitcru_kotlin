package com.fitness.fitnessCru.graph;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.SweepGradient;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;

import com.fitness.fitnessCru.R;

public class Meter extends View {

    public Meter(Context context) {
        super(context);
    }

    public Meter(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public Meter(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public Meter(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float width = (float) getWidth();
        float height = (float) getWidth() - 40;
        float radius;

        if (width > height) {
            radius = (height / 2) - 20;
        } else {
            radius = (width / 2) - 20;
        }

        Path circle;
        Paint tPaint;
        circle = new Path();
        circle.addCircle((width / 2), (height / 2), radius - 20, Path.Direction.CW);
        tPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        tPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        tPaint.setColor(Color.LTGRAY);
        tPaint.setTextSize(20);
        tPaint.setTypeface(ResourcesCompat.getFont(getContext(), R.font.manrope_medium));

        if (android.os.Build.VERSION.SDK_INT <= 30) {
            canvas.drawTextOnPath("Underweight", circle, 780, -45, tPaint);
            canvas.drawTextOnPath("Normal", circle, 1400, -27, tPaint);
            canvas.drawTextOnPath("Overweight", circle, 1680, -45, tPaint);

            canvas.drawTextOnPath("16.0", circle, 780, 35, tPaint);
            canvas.drawTextOnPath("18.5", circle, 1030, 42, tPaint);
            canvas.drawTextOnPath("25.0", circle, 1270, 40, tPaint);
            canvas.drawTextOnPath("40.0", circle, 1500, 42, tPaint);
            canvas.drawTextOnPath("50.0", circle, 1695, 35, tPaint);
        } else {
            canvas.drawTextOnPath("Underweight", circle, 1095, -45, tPaint);
            canvas.drawTextOnPath("Normal", circle, 1620, -27, tPaint);
            canvas.drawTextOnPath("Overweight", circle, 2110, -45, tPaint);

            canvas.drawTextOnPath("16.0", circle, 1098, 35, tPaint);
            canvas.drawTextOnPath("18.5", circle, 1350, 42, tPaint);
            canvas.drawTextOnPath("25.0", circle, 1630, 40, tPaint);
            canvas.drawTextOnPath("40.0", circle, 1920, 42, tPaint);
            canvas.drawTextOnPath("50.0", circle, 2170, 35, tPaint);
        }

        Path path = new Path();
        path.addCircle((width / 2), (height / 2), radius, Path.Direction.CW);

        Paint paint = new Paint();
        paint.setStrokeWidth(40);
        paint.setStyle(Paint.Style.FILL);

        Paint paint1 = new Paint();
        paint1.setStrokeWidth(40);
        paint1.setStyle(Paint.Style.FILL);

        Paint paint2 = new Paint();
        paint2.setStrokeWidth(40);
        paint2.setStyle(Paint.Style.FILL);

        Paint paint3 = new Paint();
        paint3.setStrokeWidth(40);
        paint3.setStyle(Paint.Style.FILL);

        float center_x, center_y;
        final RectF oval = new RectF();

        paint.setStyle(Paint.Style.STROKE);
        paint1.setStyle(Paint.Style.STROKE);
        paint2.setStyle(Paint.Style.STROKE);
        paint3.setStyle(Paint.Style.STROKE);

        paint.setShader(new SweepGradient(getMeasuredWidth() / 2F, getMeasuredHeight() / 2F, Color.argb(255, 60, 166, 115), Color.argb(255, 72, 167, 101)));
        paint1.setShader(new SweepGradient(getMeasuredWidth() / 2F, getMeasuredHeight() / 2F, Color.argb(255, 60, 166, 115), Color.argb(255, 72, 167, 101)));
        paint2.setShader(new SweepGradient(getMeasuredWidth() / 2F, getMeasuredHeight() / 2F, Color.argb(255, 68, 135, 85), Color.argb(255, 78, 137, 75)));
        paint3.setShader(new SweepGradient(getMeasuredWidth() / 2F, getMeasuredHeight() / 2F, Color.argb(255, 68, 103, 64), Color.argb(255, 70, 104, 62)));

        paint.setShader(new SweepGradient(getMeasuredWidth() / 2F, getMeasuredHeight() / 2F, Color.argb(255, 252, 88, 192), Color.argb(255, 252, 88, 192)));
        paint1.setShader(new SweepGradient(getMeasuredWidth() / 2F, getMeasuredHeight() / 2F, Color.argb(255, 250, 55, 156), Color.argb(255, 250, 55, 156)));
        paint2.setShader(new SweepGradient(getMeasuredWidth() / 2F, getMeasuredHeight() / 2F, Color.argb(255, 252, 30, 104), Color.argb(255, 252, 30, 104)));
        paint3.setShader(new SweepGradient(getMeasuredWidth() / 2F, getMeasuredHeight() / 2F, Color.argb(255, 250, 20, 74), Color.argb(255, 250, 20, 74)));


        center_x = width / 2;
        center_y = (height / 2);

        oval.set(center_x - radius, center_y - radius + 20, center_x + radius, center_y + radius);

        canvas.drawArc(oval, 180, 44, false, paint);
        canvas.drawArc(oval, 225, 44, false, paint1);
        canvas.drawArc(oval, 270, 44, false, paint2);
        canvas.drawArc(oval, 315, 44, false, paint3);

        Paint paint4 = new Paint();
        paint4.setStyle(Paint.Style.FILL);
        paint4.setColor(Color.WHITE);
        canvas.drawCircle((getWidth() / 2), getHeight() - 15, 15, paint4);
    }
}
