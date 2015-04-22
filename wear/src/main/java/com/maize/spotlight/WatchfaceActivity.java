package com.maize.spotlight;

import android.app.Activity;

/**
 * @author Will Hou (will@ezi.am)
 * @date Sept 9, 2014
 */
public class WatchfaceActivity extends Activity {
//        implements ConnectionCallbacks, OnConnectionFailedListener, DataListener {

//    private static final String TAG = WatchfaceActivity.class.getSimpleName();
//    private static final IntentFilter INTENT_FILTER_TIME;
//
//    private GoogleApiClient mGoogleApiClient;
//
//    static {
//        INTENT_FILTER_TIME = new IntentFilter();
//        INTENT_FILTER_TIME.addAction(Intent.ACTION_TIME_TICK);
//        INTENT_FILTER_TIME.addAction(Intent.ACTION_TIMEZONE_CHANGED);
//        INTENT_FILTER_TIME.addAction(Intent.ACTION_TIME_CHANGED);
//    }
//
//    private WatchfaceView mWatchfaceView;
//
//    public BroadcastReceiver mTimeInfoReceiver = new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context arg0, Intent intent) {
//            if (mWatchfaceView != null) {
//                mWatchfaceView.invalidate();
//            }
//        }
//    };
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_watchface);
//
//        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
//        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
//            @Override
//            public void onLayoutInflated(WatchViewStub stub) {
//                mWatchfaceView = (WatchfaceView) stub.findViewById(R.id.face);
//            }
//        });
//
//        mTimeInfoReceiver.onReceive(this, registerReceiver(null, INTENT_FILTER_TIME));
//        registerReceiver(mTimeInfoReceiver, INTENT_FILTER_TIME);
//
//        mGoogleApiClient = new GoogleApiClient.Builder(this)
//                .addConnectionCallbacks(this)
//                .addOnConnectionFailedListener(this)
//                .addApi(Wearable.API)
//                .build();
//    }
//
//    @Override
//    protected void onStart() {
//        super.onStart();
//        mGoogleApiClient.connect();
//    }
//
//    @Override
//    protected void onStop() {
//        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
//            Log.e(TAG, "onStop, Remove DataListener");
//            Wearable.DataApi.removeListener(mGoogleApiClient, this);
//            mGoogleApiClient.disconnect();
//        }
//        super.onStop();
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        unregisterReceiver(mTimeInfoReceiver);
//    }
//
//    @Override
//    public void onConnected(Bundle bundle) {
//        Log.e(TAG, "onConnected, Add DataListener");
//        Wearable.DataApi.addListener(mGoogleApiClient, this)
//                .setResultCallback(new ResultCallback<Status>() {
//                    @Override
//                    public void onResult(Status status) {
//                        if(status.isSuccess()) {
//                            Log.e(TAG, "success");
//                        } else {
//                            Log.e(TAG, "fail: " + status.getStatusMessage());
//                        }
//                    }
//                });
//    }
//
//    @Override
//    public void onConnectionSuspended(int i) {
//        Log.e(TAG, "onConnectionSuspended");
//    }
//
//    @Override
//    public void onDataChanged(DataEventBuffer dataEvents) {
//        Log.e(TAG, "onDataChanged");
//        for (DataEvent event : dataEvents) {
//            Uri uri = event.getDataItem().getUri();
//            String path = uri.getPath();
//
//            if (event.getType() == DataEvent.TYPE_CHANGED && path.equals("/update")) {
//                final DataMapItem item = DataMapItem.fromDataItem(event.getDataItem());
//                final int backgroundColor = item.getDataMap().getInt(WatchfaceView.KEY_BACKGROUND);
//                final int accentColor = item.getDataMap().getInt(WatchfaceView.KEY_ACCENT);
//                Log.e("asdf", "bg: " + backgroundColor);
//                Log.e("asdf", "ac: " + accentColor);
//
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        Log.e("asdf", "updating on ui thread");
//                        mWatchfaceView.setBackgroundColor(backgroundColor);
//                        mWatchfaceView.setAccentColor(accentColor);
//                    }
//                });
//            }
//        }
//    }
//
//    @Override
//    public void onConnectionFailed(ConnectionResult connectionResult) {
//        Log.e(TAG, "onConnectionFailed");
//    }
}
