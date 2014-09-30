package com.maize.watchface.spotlight;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.Calendar;

/**
 * @author Will Hou (will@ezi.am)
 * @date Sept 11, 2014
 */
public class WatchfaceView extends View {

    private static final int HALF_DAY_IN_MIN = 720;
    private static final int ONE_HOUR_IN_MIN = 60;
    private static final float MIN_PER_DEGREE = HALF_DAY_IN_MIN / 360f;
    private static final float CENTER_OFFSET = 0.75f;
    private static final float CLOCKFACE_RADIUS = 0.93f;

    private static final float HOUR_INDICATOR_SIZE_FACTOR = 0.1f;
    private static final float HOUR_TEXT_SIZE_FACTOR = 0.2f;
    private static final float HALF_HOUR_INDICATOR_SIZE_FACTOR = 0.05f;
    private static final float TEN_MIN_INDICATOR_SIZE_FACTOR = 0.025f;

    public static final String KEY_BACKGROUND = WatchfaceView.class.getSimpleName() + ".KEY_BACKGROUND";
    public static final String KEY_ACCENT = WatchfaceView.class.getSimpleName() + ".KEY_ACCENT";

    private Paint mLinePaint;
    private Paint mHourPaint;
    private Paint mTenMinutePaint;
    private Paint mHourTextPaint;
    private int mBackgroundColor;
    private int mAccentColor;

    public WatchfaceView(Context context) {
        super(context);
        init();
    }

    public WatchfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public WatchfaceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mBackgroundColor = Color.BLACK;
        mAccentColor = 0xAAFF6600;

        mLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mLinePaint.setAntiAlias(true);
        mLinePaint.setColor(mAccentColor);
        mLinePaint.setStyle(Style.STROKE);
        mLinePaint.setStrokeWidth(3f);

        mHourPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mHourPaint.setAntiAlias(true);
        mHourPaint.setColor(Color.WHITE);
        mHourPaint.setStyle(Style.STROKE);
        mHourPaint.setStrokeWidth(3f);

        mTenMinutePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTenMinutePaint.setAntiAlias(true);
        mTenMinutePaint.setColor(Color.WHITE);
        mTenMinutePaint.setStyle(Style.STROKE);
        mTenMinutePaint.setStrokeWidth(2f);

        mHourTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mHourTextPaint.setAntiAlias(true);
        mHourTextPaint.setColor(Color.WHITE);
        mHourTextPaint.setStyle(Style.FILL);
        mHourTextPaint.setTextSize(getResources().getDimensionPixelSize(R.dimen.hour_text_size));
        mHourTextPaint.setTextAlign(Paint.Align.CENTER);

        setBackgroundColor(mBackgroundColor);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR);
        int minute = c.get(Calendar.MINUTE);

        Log.d("asdf", "hour: " + hour);
        Log.d("asdf", "minute: " + minute);

        final int totalMinutes = hour * ONE_HOUR_IN_MIN + minute;
        final float degrees = totalMinutes / MIN_PER_DEGREE - 90f;

        Log.d("asdf", "totalMinutes: " + totalMinutes);
        Log.d("asdf", "degrees: " + degrees);

        // Draw background color
//        canvas.drawColor(Color.BLACK);

        Path path = new Path();
        
        int width = getWidth();
        int height = getHeight();
        
        int viewCenterX = getWidth() / 2;
        int viewCenterY = getHeight() / 2;

        float centerX, centerY;

        if ((hour == 0 || hour == 12) && minute == 0) {
            centerX = viewCenterX;
            centerY = viewCenterY + getHeight() * CENTER_OFFSET;
        } else if (hour == 6 && minute == 0) {
            centerX = viewCenterX;
            centerY = viewCenterY - getHeight() * CENTER_OFFSET;
        } else {
            boolean beforeSix = hour < 6;
            centerX = beforeSix ?
                    viewCenterX + (float) (getWidth() * CENTER_OFFSET * Math.cos(Math.toRadians(180 - degrees))) :
                    viewCenterX + (float) (getWidth() * CENTER_OFFSET * Math.cos(Math.toRadians(degrees - 180)));
            centerY = beforeSix ?
                    viewCenterY - (float) (getHeight() * CENTER_OFFSET * Math.sin(Math.toRadians(180 - degrees))) :
                    viewCenterY + (float) (getHeight() * CENTER_OFFSET * Math.sin(Math.toRadians(degrees - 180)));
        }

        Log.d("asdf", "center: " + centerX + ", " + centerY);

        //Draw clock face
        for (int i = 0; i < 12; i++) {
            float rotation = i * 30;
            // Draw hour indicators
            path.reset();
            path.moveTo(centerX, centerY - getHeight() * CLOCKFACE_RADIUS);
            path.lineTo(centerX, centerY - getHeight() * (CLOCKFACE_RADIUS - HOUR_INDICATOR_SIZE_FACTOR));
            canvas.save();
            canvas.rotate(rotation, centerX, centerY);
            canvas.drawPath(path, mHourPaint);

            // Draw hour text
            float textCenterX = centerX;
            float textCenterY = centerY - getHeight() * (CLOCKFACE_RADIUS - HOUR_TEXT_SIZE_FACTOR);
            canvas.save();
            canvas.rotate(-rotation, centerX, textCenterY);
            canvas.drawText(
                    String.valueOf(i > 0 ? i : 12),
                    textCenterX,
                    textCenterY + 12,
                    mHourTextPaint);
            canvas.restore();

            // Draw half-hour indicators
            path.reset();
            path.moveTo(centerX, centerY - getHeight() * CLOCKFACE_RADIUS);
            path.lineTo(centerX, centerY - getHeight() * (CLOCKFACE_RADIUS - HALF_HOUR_INDICATOR_SIZE_FACTOR));
            canvas.rotate(15, centerX, centerY);
            canvas.drawPath(path, mHourPaint);
            canvas.restore();
        }

        for (int i = 0; i < 24; i++) {
            // Draw 10min indicators
            path.reset();
            path.moveTo(centerX, centerY - getHeight() * CLOCKFACE_RADIUS);
            path.lineTo(centerX, centerY - getHeight() * (CLOCKFACE_RADIUS - TEN_MIN_INDICATOR_SIZE_FACTOR));
            canvas.save();
            canvas.rotate(i * 15 + 5, centerX, centerY);
            canvas.drawPath(path, mTenMinutePaint);
            canvas.rotate(5, centerX, centerY);
            canvas.drawPath(path, mTenMinutePaint);
            canvas.restore();
        }

        // Draw red minute "needle"
        path.reset();
        path.moveTo(-getWidth(), viewCenterY);
        path.lineTo(getWidth() * 2, viewCenterY);
        canvas.save();
        canvas.rotate(degrees, viewCenterX, viewCenterY);
        canvas.drawPath(path, mLinePaint);
        canvas.restore();

        Log.d("asdf", "===============================");
    }

    @Override
    public void setBackgroundColor(int color) {
        super.setBackgroundColor(color);
        switch (color) {
            case Color.BLACK:
                mHourPaint.setColor(Color.WHITE);
                mHourTextPaint.setColor(Color.WHITE);
                mTenMinutePaint.setColor(Color.WHITE);
                break;
            case Color.WHITE:
                mHourPaint.setColor(Color.BLACK);
                mHourTextPaint.setColor(Color.BLACK);
                mTenMinutePaint.setColor(Color.BLACK);
                break;
        }
        invalidate();
    }

    public void setAccentColor(int accentColor) {
        mAccentColor = accentColor;
        mLinePaint.setColor(mAccentColor);
        invalidate();
    }
}
