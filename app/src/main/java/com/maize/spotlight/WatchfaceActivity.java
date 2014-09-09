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

public class WatchfaceActivity extends Activity implements SurfaceHolder.Callback {

    private static final IntentFilter INTENT_FILTER_TIME;
    private static final int HALF_DAY_IN_MIN = 720;
    private static final int ONE_HOUR_IN_MIN = 60;
    private static final float MIN_PER_DEGREE = HALF_DAY_IN_MIN / 360f;

    private SurfaceHolder mSurfaceHolder;
    private SurfaceView mSurfaceView;
    private Canvas mCanvas;
    private Paint mLinePaint;

    static {
        INTENT_FILTER_TIME = new IntentFilter();
        INTENT_FILTER_TIME.addAction(Intent.ACTION_TIME_TICK);
        INTENT_FILTER_TIME.addAction(Intent.ACTION_TIMEZONE_CHANGED);
        INTENT_FILTER_TIME.addAction(Intent.ACTION_TIME_CHANGED);
    }

    public BroadcastReceiver mTimeInfoReceiver = new BroadcastReceiver() {
        final private String TAG = "WearFaceMatrix/timeInfoReceiver";
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
        mLinePaint.setColor(0xFFFF6600);
        mLinePaint.setStyle(Style.STROKE);
        mLinePaint.setStrokeWidth(2f);
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
        Log.d("asdf", "===============================");

        // Draw background color
        mCanvas.drawColor(Color.WHITE);

        // Draw red minute "needle"
        Path path = new Path();
        path.moveTo(0, 160);
        path.lineTo(455, 160);
        mCanvas.save();
        mCanvas.rotate(degrees, 160, 160);
        mCanvas.drawPath(path, mLinePaint);
        mCanvas.restore();

        //Draw clock face

        mSurfaceHolder.unlockCanvasAndPost(mCanvas);
    }
}
