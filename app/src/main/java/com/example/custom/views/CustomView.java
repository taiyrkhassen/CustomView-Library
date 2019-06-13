package com.example.custom.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.constraint.solver.widgets.Rectangle;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import com.example.custom.R;

import org.jetbrains.annotations.Nullable;

public class CustomView extends View{
    private int maxValue = 100;
    private int currentValue = 75;
    private int barHeight;
    private int circleRadius;
    private int spaceAfterBar;
    private int circleTextSize;
    private int maxValueTextSize;
    private int labelTextSize;
    private int labelTextColor;
    private int currentValueTextColor;
    private int circleTextColor;
    private int baseColor;
    private int fillColor;
    private String labelText;


    private Paint labelPaint, maxValuePaint, barBasePaint, barFillPaint, circlePaint, currentValuePaint;

    public CustomView(Context context) {
        super(context);
    }

    public CustomView(Context context,  AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);

    }




    public void init(Context context, AttributeSet attrs){
        //read attributes with typed array
        TypedArray ta = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CustomView, 0, 0);
        barHeight = ta.getDimensionPixelSize(R.styleable.CustomView_barHeight, 0);
        circleRadius = ta.getDimensionPixelSize(R.styleable.CustomView_circleRadius, 0);
        spaceAfterBar = ta.getDimensionPixelSize(R.styleable.CustomView_spaceAfterBar, 0);
        circleTextSize = ta.getDimensionPixelSize(R.styleable.CustomView_circleTextSize, 0);
        maxValueTextSize = ta.getDimensionPixelSize(R.styleable.CustomView_maxValueTextSize, 0);
        labelTextSize = ta.getDimensionPixelSize(R.styleable.CustomView_labelTextSize, 0);
        labelTextColor = ta.getColor(R.styleable.CustomView_labelTextColor, Color.BLACK);
        currentValueTextColor = ta.getColor(R.styleable.CustomView_maxValueTextColor, Color.BLACK);
        circleTextColor = ta.getColor(R.styleable.CustomView_circleTextColor, Color.BLACK);
        baseColor = ta.getColor(R.styleable.CustomView_baseColor, Color.BLACK);
        fillColor = ta.getColor(R.styleable.CustomView_fillColor, Color.BLACK);
        labelText = ta.getString(R.styleable.CustomView_labelText);
        maxValue = ta.getInteger(R.styleable.CustomView_maxValue, maxValue);
        ta.recycle();

        // initialise objects that we need to draw CustomView
        labelPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        labelPaint.setTextSize(labelTextSize);
        labelPaint.setColor(labelTextColor);
        labelPaint.setTextAlign(Paint.Align.LEFT);
        labelPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));

        maxValuePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        maxValuePaint.setTextSize(maxValueTextSize);
        maxValuePaint.setColor(currentValueTextColor);
        maxValuePaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        maxValuePaint.setTextAlign(Paint.Align.RIGHT);

        barBasePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        barBasePaint.setColor(baseColor);

        barFillPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        barFillPaint.setColor(fillColor);

        circlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        circlePaint.setColor(fillColor);

        currentValuePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        currentValuePaint.setTextSize(circleTextSize);
        currentValuePaint.setColor(circleTextColor);
        currentValuePaint.setTextAlign(Paint.Align.CENTER);


    }

    @Override
    protected void onMeasure (int widthMeasureSpec, int heightMeasureSpec) {

        setMeasuredDimension(measureWidth(widthMeasureSpec), measureHeight(heightMeasureSpec));
    }

    @Override
    protected void onDraw (Canvas canvas) {
        drawLabel(canvas);
        drawBar(canvas);
        drawMaxValue(canvas);
    }

    public void setMaxValue(int maxValue) {
        this.maxValue = maxValue;
        invalidate();
        requestLayout();
    }
    public void setValue(int newValue) {
        if(newValue < 0) {
            currentValue = 0;
        } else if (newValue > maxValue) {
            currentValue = maxValue;
        } else {
            currentValue = newValue;
        }
        invalidate();
    }

    //determine height
    private int measureHeight(int measureSpec) {
        //top and bottom padding + label height + maximum height of bar
        int size = getPaddingTop() + getPaddingBottom();
        size += labelPaint.getFontSpacing();
        float maxValueTextSpacing = maxValuePaint.getFontSpacing();
        size += Math.max(maxValueTextSpacing, Math.max(barHeight, circleRadius * 2));
        return resolveSizeAndState(size, measureSpec, 0);
    }

    //determine width
    private int measureWidth(int measureSpec) {
        //left and right padding + text bounds + maxValue paint size
        int size = getPaddingLeft() + getPaddingRight();
        Rect bounds = new Rect();
        labelPaint.getTextBounds(labelText, 0, labelText.length(), bounds);
        size += bounds.width();

        bounds = new Rect();
        String maxValueText = String.valueOf(maxValue);
        maxValuePaint.getTextBounds(maxValueText, 0, maxValueText.length(), bounds);
        size += bounds.width();

        return resolveSizeAndState(size, measureSpec, 0);
    }

    //draw the label
    private void drawLabel(Canvas canvas) {
        float x = getPaddingLeft();
        //the y coordinate marks the bottom of the text, so we need to factor in the height
        Rect bounds = new Rect();
        labelPaint.getTextBounds(labelText, 0, labelText.length(), bounds);
        float y = getPaddingTop() + bounds.height();
        canvas.drawText(labelText, x, y, labelPaint);
    }


    //draw the center
    private float getBarCenter() {
        //position the bar slightly below the middle of the drawable area
        float barCenter = (getHeight() - getPaddingTop() - getPaddingBottom()) / 2; //this is the center
        barCenter += getPaddingTop() + .1f * getHeight(); //move it down a bit
        return barCenter;
    }

    //draw the value with its maximum
    private void drawMaxValue(Canvas canvas) {
        String maxValue = String.valueOf(this.maxValue);
        Rect maxValueRect = new Rect();
        maxValuePaint.getTextBounds(maxValue, 0, maxValue.length(), maxValueRect);

        float xPos = getWidth() - getPaddingRight();
        float yPos = getBarCenter() + maxValueRect.height() / 2;
        canvas.drawText(maxValue, xPos, yPos, maxValuePaint);
    }

    private void drawBar(Canvas canvas) {
        String maxValueString = String.valueOf(maxValue);
        Rect maxValueRect = new Rect();
        maxValuePaint.getTextBounds(maxValueString, 0, maxValueString.length(), maxValueRect);
        float barLength = getWidth() - getPaddingRight() - getPaddingLeft() - circleRadius - maxValueRect.width() - spaceAfterBar;

        float barCenter = getBarCenter();

        float halfBarHeight = barHeight / 2;
        float top = barCenter - halfBarHeight;
        float bottom = barCenter + halfBarHeight;
        float left = getPaddingLeft();
        float right = getPaddingLeft() + barLength;
        RectF rect = new RectF(left, top, right, bottom);
        canvas.drawRoundRect(rect, halfBarHeight, halfBarHeight, barBasePaint);

        //drawRoundRect are the radii for drawing the corners of the rectangle
        float percentFilled = (float) currentValue / (float) maxValue;
        float fillLength = barLength * percentFilled;
        float fillPosition = left + fillLength;
        RectF fillRect = new RectF(left, top, fillPosition, bottom);
        canvas.drawRoundRect(fillRect, halfBarHeight, halfBarHeight, barFillPaint);
        //The circle indicator and the text inside of it will be drawn at the same location
        canvas.drawCircle(fillPosition, barCenter, circleRadius, circlePaint);
        // draw the text for the current value inside the circle
        Rect bounds = new Rect();
        String valueString = String.valueOf(Math.round(currentValue));
        currentValuePaint.getTextBounds(valueString, 0, valueString.length(), bounds);
        float y = barCenter + (bounds.height() / 2);
        canvas.drawText(valueString, fillPosition, y, currentValuePaint);
    }

    public int getCurrentValue() {
        return currentValue;
    }

    // didn't understand very well((
    /* @Override
    public boolean onTouchEvent(MotionEvent event) {
        boolean value = super.onTouchEvent(event);
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                return true;
            case MotionEvent.ACTION_MOVE:
                float x = event.getX();
        }

        return value;
    }

    private double convertTouchEventPointToValue(float xPos){
        // transform touch coordinate into component coordinate
       return 1.5;
    }*/
}



