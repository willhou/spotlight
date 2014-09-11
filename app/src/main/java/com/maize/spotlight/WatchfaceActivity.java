package com.maize.spotlight;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.os.Bundle;
import android.support.wearable.view.WatchViewStub;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.Calendar;

/**
 * @author Will Hou (will@ezi.am)
 * @date Sept 9, 2014
 */
public class WatchfaceActivity extends Activity implements SurfaceHolder.Callback {

    private static final IntentFilter INTENT_FILTER_TIME;
    private static final int HALF_DAY_IN_MIN = 720;
    private static final int ONE_HOUR_IN_MIN = 60;
    private static final float MIN_PER_DEGREE = HALF_DAY_IN_MIN / 360f;
    private static final int CENTER_OFFSET = 240;
    private static final int CLOCKFACE_RADIUS = 300;

    private SurfaceHolder mSurfaceHolder;
    private SurfaceView mSurfaceView;
    private Canvas mCanvas;
    private Paint mLinePaint;
    private Paint mHourPaint;
    private Paint mHalfHourPaint;
    private Paint mTenMinutePaint;
    private Paint mHourTextPaint;

    static {
        INTENT_FILTER_TIME = new IntentFilter();
        INTENT_FILTER_TIME.addAction(Intent.ACTION_TIME_TICK);
        INTENT_FILTER_TIME.addAction(Intent.ACTION_TIMEZONE_CHANGED);
        INTENT_FILTER_TIME.addAction(Intent.ACTION_TIME_CHANGED);
    }

    public BroadcastReceiver mTimeInfoReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context arg0, Intent intent) {
            onDraw();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watchface);

        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub stub) {
                mSurfaceView = (SurfaceView) stub.findViewById(R.id.surfaceView);
                mSurfaceView.getHolder().addCallback(WatchfaceActivity.this);
            }
        });

        mTimeInfoReceiver.onReceive(this, registerReceiver(null, INTENT_FILTER_TIME));
        registerReceiver(mTimeInfoReceiver, INTENT_FILTER_TIME);

        mLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mLinePaint.setAntiAlias(true);
        mLinePaint.setColor(0xAAFF6600);
        mLinePaint.setStyle(Style.STROKE);
        mLinePaint.setStrokeWidth(3f);

        mHourPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mHourPaint.setAntiAlias(true);
        mHourPaint.setColor(Color.WHITE);
        mHourPaint.setStyle(Style.STROKE);
        mHourPaint.setStrokeWidth(3f);

        mHalfHourPaint = mHourPaint;

        mTenMinutePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTenMinutePaint.setAntiAlias(true);
        mTenMinutePaint.setColor(Color.WHITE);
        mTenMinutePaint.setStyle(Style.STROKE);
        mTenMinutePaint.setStrokeWidth(2f);

        mHourTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mHourTextPaint.setAntiAlias(true);
        mHourTextPaint.setColor(Color.WHITE);
        mHourTextPaint.setStyle(Paint.Style.FILL);
        mHourTextPaint.setTextSize(getResources().getDimensionPixelSize(R.dimen.hour_text_size));
        mHourTextPaint.setTextAlign(Paint.Align.CENTER);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mTimeInfoReceiver);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        mSurfaceHolder = holder;
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        mSurfaceHolder = holder;
        onDraw();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        mCanvas = null;
    }

    private void onDraw() {
        if (mSurfaceHolder == null) return;
        mCanvas = mSurfaceHolder.lockCanvas();
        if (mCanvas == null) return;

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
        mCanvas.drawColor(Color.BLACK);

        Path path = new Path();

        float centerX, centerY;

        if ((hour == 0 || hour == 12) && minute == 0) {
            centerX = 160;
            centerY = 160 + CENTER_OFFSET;
        } else if (hour == 6 && minute == 0) {
            centerX = 160;
            centerY = 160 - CENTER_OFFSET;
        } else {
            boolean beforeSix = hour < 6;
            centerX = beforeSix ?
                    160 + (float) (CENTER_OFFSET * Math.cos(Math.toRadians(180 - degrees))) :
                    160 + (float) (CENTER_OFFSET * Math.cos(Math.toRadians(degrees - 180)));
            centerY = beforeSix ?
                    160 - (float) (CENTER_OFFSET * Math.sin(Math.toRadians(180 - degrees))) :
                    160 + (float) (CENTER_OFFSET * Math.sin(Math.toRadians(degrees - 180)));
        }

        Log.d("asdf", "center: " + centerX + ", " + centerY);

        //Draw clock face
        for (int i = 0; i < 12; i++) {
            float rotation = i * 30;
            // Draw hour indicators
            path.reset();
            path.moveTo(centerX, centerY - CLOCKFACE_RADIUS);
            path.lineTo(centerX, centerY - CLOCKFACE_RADIUS + 32);
            mCanvas.save();
            mCanvas.rotate(rotation, centerX, centerY);
            mCanvas.drawPath(path, mHourPaint);

            // Draw hour text
            float textCenterX = centerX;
            float textCenterY = centerY - CLOCKFACE_RADIUS + 64;
            mCanvas.save();
            mCanvas.rotate(-rotation, centerX, textCenterY);
            mCanvas.drawText(
                    String.valueOf(i > 0 ? i : 12),
                    textCenterX,
                    textCenterY + 12,
                    mHourTextPaint);
            mCanvas.restore();

            // Draw half-hour indicators
            path.reset();
            path.moveTo(centerX, centerY - CLOCKFACE_RADIUS);
            path.lineTo(centerX, centerY - CLOCKFACE_RADIUS + 16);
            mCanvas.rotate(15, centerX, centerY);
            mCanvas.drawPath(path, mHalfHourPaint);
            mCanvas.restore();
        }

        for (int i = 0; i < 24; i++) {
            // Draw 10min indicators
            path.reset();
            path.moveTo(centerX, centerY - CLOCKFACE_RADIUS);
            path.lineTo(centerX, centerY - CLOCKFACE_RADIUS + 8);
            mCanvas.save();
            mCanvas.rotate(i * 15 + 5, centerX, centerY);
            mCanvas.drawPath(path, mTenMinutePaint);
            mCanvas.rotate(5, centerX, centerY);
            mCanvas.drawPath(path, mTenMinutePaint);
            mCanvas.restore();
        }

        // Draw red minute "needle"
        path.reset();
        path.moveTo(-66, 160);
        path.lineTo(455, 160);
        mCanvas.save();
        mCanvas.rotate(degrees, 160, 160);
        mCanvas.drawPath(path, mLinePaint);
        mCanvas.restore();

        Log.d("asdf", "===============================");

        mSurfaceHolder.unlockCanvasAndPost(mCanvas);
    }
}
