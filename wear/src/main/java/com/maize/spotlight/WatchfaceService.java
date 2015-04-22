package com.maize.spotlight;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.wearable.watchface.CanvasWatchFaceService;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.SurfaceHolder;

import java.util.Calendar;

/**
 * @author Will Hou (will@ezi.am)
 * @date April 19, 2015
 */
public class WatchfaceService extends CanvasWatchFaceService {

//    public static final String KEY_BACKGROUND = WatchfaceEngine.class.getSimpleName() + ".KEY_BACKGROUND";
//    public static final String KEY_ACCENT = WatchfaceEngine.class.getSimpleName() + ".KEY_ACCENT";

    @Override
    public Engine onCreateEngine() {
        return new WatchfaceEngine();
    }

    /* implement service callback methods */
    private class WatchfaceEngine extends CanvasWatchFaceService.Engine {

        private static final int HALF_DAY_IN_MIN = 720;
        private static final int ONE_HOUR_IN_MIN = 60;
        private static final float MIN_PER_DEGREE = HALF_DAY_IN_MIN / 360f;
        private static final float CENTER_OFFSET = 0.75f;
        private static final float CLOCKFACE_RADIUS = 0.93f;

        private static final float HOUR_INDICATOR_SIZE_FACTOR = 0.1f;
        private static final float HOUR_TEXT_SIZE_FACTOR = 0.2f;
        private static final float HALF_HOUR_INDICATOR_SIZE_FACTOR = 0.05f;
        private static final float TEN_MIN_INDICATOR_SIZE_FACTOR = 0.025f;

        private Paint mLinePaint;
        private Paint mHourPaint;
        private Paint mTenMinutePaint;
        private Paint mHourTextPaint;
        private int mBackgroundColor;
        private int mAccentColor;

        @Override
        public void onCreate(SurfaceHolder holder) {
            super.onCreate(holder);
            /* initialize your watch face */
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

            DisplayMetrics metrics = getResources().getDisplayMetrics();

            mHourTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            mHourTextPaint.setAntiAlias(true);
            mHourTextPaint.setColor(Color.WHITE);
            mHourTextPaint.setStyle(Style.FILL);
            mHourTextPaint.setTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 20, metrics));
            mHourTextPaint.setTextAlign(Paint.Align.CENTER);
        }

        @Override
        public void onPropertiesChanged(Bundle properties) {
            super.onPropertiesChanged(properties);
            /* get device features (burn-in, low-bit ambient) */
        }

        @Override
        public void onTimeTick() {
            super.onTimeTick();
            /* the time changed */
            invalidate();
        }

        @Override
        public void onAmbientModeChanged(boolean inAmbientMode) {
            super.onAmbientModeChanged(inAmbientMode);
            /* the wearable switched between modes */
        }

        @Override
        public void onDraw(Canvas canvas, Rect bounds) {
            /* draw your watch face */
            Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR);
            int minute = c.get(Calendar.MINUTE);

            final int totalMinutes = hour * ONE_HOUR_IN_MIN + minute;
            final float degrees = totalMinutes / MIN_PER_DEGREE - 90f;

            // Draw background color
            canvas.drawColor(mBackgroundColor);

            Path path = new Path();

            int width = bounds.width();
            int height = bounds.height();

            int viewCenterX = width / 2;
            int viewCenterY = height / 2;

            float centerX, centerY;

            if ((hour == 0 || hour == 12) && minute == 0) {
                centerX = viewCenterX;
                centerY = viewCenterY + height * CENTER_OFFSET;
            } else if (hour == 6 && minute == 0) {
                centerX = viewCenterX;
                centerY = viewCenterY - height * CENTER_OFFSET;
            } else {
                boolean beforeSix = hour < 6;
                centerX = beforeSix ?
                        viewCenterX + (float) (width * CENTER_OFFSET * Math.cos(Math.toRadians(180 - degrees))) :
                        viewCenterX + (float) (width * CENTER_OFFSET * Math.cos(Math.toRadians(degrees - 180)));
                centerY = beforeSix ?
                        viewCenterY - (float) (height * CENTER_OFFSET * Math.sin(Math.toRadians(180 - degrees))) :
                        viewCenterY + (float) (height * CENTER_OFFSET * Math.sin(Math.toRadians(degrees - 180)));
            }

            //Draw clock face
            for (int i = 0; i < 12; i++) {
                float rotation = i * 30;
                // Draw hour indicators
                path.reset();
                path.moveTo(centerX, centerY - height * CLOCKFACE_RADIUS);
                path.lineTo(centerX, centerY - height * (CLOCKFACE_RADIUS - HOUR_INDICATOR_SIZE_FACTOR));
                canvas.save();
                canvas.rotate(rotation, centerX, centerY);
                canvas.drawPath(path, mHourPaint);

                // Draw hour text
                float textCenterX = centerX;
                float textCenterY = centerY - height * (CLOCKFACE_RADIUS - HOUR_TEXT_SIZE_FACTOR);
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
                path.moveTo(centerX, centerY - height * CLOCKFACE_RADIUS);
                path.lineTo(centerX, centerY - height * (CLOCKFACE_RADIUS - HALF_HOUR_INDICATOR_SIZE_FACTOR));
                canvas.rotate(15, centerX, centerY);
                canvas.drawPath(path, mHourPaint);
                canvas.restore();
            }

            for (int i = 0; i < 24; i++) {
                // Draw 10min indicators
                path.reset();
                path.moveTo(centerX, centerY - height * CLOCKFACE_RADIUS);
                path.lineTo(centerX, centerY - height * (CLOCKFACE_RADIUS - TEN_MIN_INDICATOR_SIZE_FACTOR));
                canvas.save();
                canvas.rotate(i * 15 + 5, centerX, centerY);
                canvas.drawPath(path, mTenMinutePaint);
                canvas.rotate(5, centerX, centerY);
                canvas.drawPath(path, mTenMinutePaint);
                canvas.restore();
            }

            // Draw red minute "needle"
            path.reset();
            path.moveTo(-width, viewCenterY);
            path.lineTo(width * 2, viewCenterY);
            canvas.save();
            canvas.rotate(degrees, viewCenterX, viewCenterY);
            canvas.drawPath(path, mLinePaint);
            canvas.restore();
        }

        @Override
        public void onVisibilityChanged(boolean visible) {
            super.onVisibilityChanged(visible);
            /* the watch face became visible or invisible */
        }
    }
}
