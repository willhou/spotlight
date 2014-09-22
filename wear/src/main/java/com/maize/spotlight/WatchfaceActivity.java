package com.maize.spotlight;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.wearable.view.WatchViewStub;

import com.maize.watchface.spotlight.WatchfaceView;

/**
 * @author Will Hou (will@ezi.am)
 * @date Sept 9, 2014
 */
public class WatchfaceActivity extends Activity {

    private static final IntentFilter INTENT_FILTER_TIME;

    static {
        INTENT_FILTER_TIME = new IntentFilter();
        INTENT_FILTER_TIME.addAction(Intent.ACTION_TIME_TICK);
        INTENT_FILTER_TIME.addAction(Intent.ACTION_TIMEZONE_CHANGED);
        INTENT_FILTER_TIME.addAction(Intent.ACTION_TIME_CHANGED);
    }

    private WatchfaceView mWatchfaceView;

    public BroadcastReceiver mTimeInfoReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context arg0, Intent intent) {
            if (mWatchfaceView != null) {
                mWatchfaceView.invalidate();
            }
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
                mWatchfaceView = (WatchfaceView) stub.findViewById(R.id.face);
            }
        });

        mTimeInfoReceiver.onReceive(this, registerReceiver(null, INTENT_FILTER_TIME));
        registerReceiver(mTimeInfoReceiver, INTENT_FILTER_TIME);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mTimeInfoReceiver);
    }
}
