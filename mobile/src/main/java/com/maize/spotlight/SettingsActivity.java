package com.maize.spotlight;

import android.app.Activity;
import android.os.Bundle;

public class SettingsActivity extends Activity {
//        implements ConnectionCallbacks, OnConnectionFailedListener, Saveable {

    private static final String TAG = SettingsActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
    }

    //    private GoogleApiClient mGoogleApiClient;
//    private boolean mBroadcasting;
//    private State mState;
//    private WatchfaceView mWatchfaceView;
//
//    private final int[] BACKGROUND_COLORS_ID = new int[] {
//            R.id.bgWhite, R.id.bgBlack
//    };
//
//    private final int[] BACKGROUND_COLORS = new int[] {
//            R.color.white, R.color.black
//    };
//
//    private final int[] ACCENT_COLORS_ID = new int[] {
//            R.id.accentBlue, R.id.accentGreen, R.id.accentRed, R.id.accentOrange,
//            R.id.accentYellow, R.id.accentCyan, R.id.accentNavy, R.id.accentViolet,
//            R.id.accentPurple, R.id.accentPink
//    };
//
//    private final int[] ACCENT_COLORS = new int[] {
//            R.color.blue, R.color.green, R.color.red, R.color.orange,
//            R.color.yellow, R.color.cyan, R.color.navy, R.color.violet,
//            R.color.purple, R.color.pink
//    };
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_settings);
//
//        mState = new State();
//        mGoogleApiClient = new GoogleApiClient.Builder(this)
//                .addApi(Wearable.API)
//                .addConnectionCallbacks(this)
//                .addOnConnectionFailedListener(this)
//                .build();
//
//        ActionBar actionBar = getActionBar();
//        actionBar.setDisplayShowHomeEnabled(false);
//        actionBar.setDisplayHomeAsUpEnabled(false);
//        actionBar.setDisplayShowTitleEnabled(false);
//        actionBar.setDisplayShowCustomEnabled(true);
//        actionBar.setCustomView(R.layout.actionbar_buttons);
//
//        View customView = actionBar.getCustomView();
//        final View btnUpdate = customView.findViewById(R.id.btnUpdate);
//        btnUpdate.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // Save state to SharedPrefs
//                mState.save();
//
//                // Transfer changes to watch
//                PutDataMapRequest dataMap = PutDataMapRequest.create("/update");
//                dataMap.getDataMap().putLong("time", new Date().getTime());
//                dataMap.getDataMap().putInt(WatchfaceView.KEY_BACKGROUND, Color.BLACK);
//                dataMap.getDataMap().putInt(WatchfaceView.KEY_ACCENT, getResources().getColor(R.color.red));
//                PutDataRequest request = dataMap.asPutDataRequest();
//                PendingResult<DataItemResult> pendingResult = Wearable.DataApi
//                        .putDataItem(mGoogleApiClient, request);
//                pendingResult.setResultCallback(new ResultCallback<DataItemResult>() {
//                    @Override
//                    public void onResult(DataItemResult dataItemResult) {
//                        if(dataItemResult.getStatus().isSuccess()) {
//                            Log.e(TAG, "success");
//                        } else {
//                            Log.e(TAG, "fail: " + dataItemResult.getStatus().getStatusMessage());
//                        }
//                    }
//                });
//            }
//        });
//        final View btnCancel = customView.findViewById(R.id.btnCancel);
//        btnCancel.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });
//
//        mWatchfaceView = (WatchfaceView) findViewById(R.id.watchface);
//
//        // Background colors
//        for (int i = 0; i < BACKGROUND_COLORS_ID.length; i++) {
//            int id = BACKGROUND_COLORS_ID[i];
//            CheckImageView view = (CheckImageView) findViewById(id);
//            view.setOnCheckedChangeListener(mBackgroundChangeListener);
//        }
//
//        // Accent colors
//        for (int i = 0; i < ACCENT_COLORS_ID.length; i++) {
//            int id = ACCENT_COLORS_ID[i];
//            CheckImageView view = (CheckImageView) findViewById(id);
//            view.setOnCheckedChangeListener(mAccentChangeListener);
//        }
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
//        if (null != mGoogleApiClient && mGoogleApiClient.isConnected()) {
//            mGoogleApiClient.disconnect();
//        }
//        super.onStop();
//    }
//
//    @Override
//    public void onConnected(Bundle bundle) {
//        Log.e(TAG, "onConnected");
//    }
//
//    @Override
//    public void onConnectionSuspended(int i) {
//        Log.e(TAG, "onConnectionSuspended");
//    }
//
//    @Override
//    public void onConnectionFailed(ConnectionResult connectionResult) {
//        GooglePlayServicesUtil.getErrorDialog(
//                connectionResult.getErrorCode(),
//                this,
//                0).show();
//    }
//
//    private OnCheckedChangeListener mBackgroundChangeListener = new OnCheckedChangeListener() {
//        @Override
//        public void onCheckedChanged(CheckImageView imageView, boolean isChecked) {
//            if (mBroadcasting) return;
//            mBroadcasting = true;
//
//            for (int i = 0; i < BACKGROUND_COLORS_ID.length; i++) {
//                int id = BACKGROUND_COLORS_ID[i];
//                CheckImageView view = (CheckImageView) findViewById(id);
//                if (id == imageView.getId()) {
//                    view.setEnabled(!isChecked);
//                    if (isChecked) {
//                        mState.backgroundColor = getResources().getColor(BACKGROUND_COLORS[i]);
//                        mWatchfaceView.setBackgroundColor(mState.backgroundColor);
//                        mWatchfaceView.invalidate();
//                    }
//                } else {
//                    view.setEnabled(isChecked);
//                    view.setChecked(!isChecked);
//                }
//            }
//
//            mBroadcasting = false;
//        }
//    };
//
//    private OnCheckedChangeListener mAccentChangeListener = new OnCheckedChangeListener() {
//        @Override
//        public void onCheckedChanged(CheckImageView imageView, boolean isChecked) {
//            if (mBroadcasting) return;
//            mBroadcasting = true;
//
//            for (int i = 0; i < ACCENT_COLORS_ID.length; i++) {
//                int id = ACCENT_COLORS_ID[i];
//                CheckImageView view = (CheckImageView) findViewById(id);
//                if (id == imageView.getId()) {
//                    view.setEnabled(!isChecked);
//                    if (isChecked) {
//                        mState.accentColor = getResources().getColor(ACCENT_COLORS[i]);
//                        mWatchfaceView.setAccentColor(mState.accentColor);
//                        mWatchfaceView.invalidate();
//                    }
//                } else {
//                    view.setEnabled(isChecked);
//                    view.setChecked(!isChecked);
//                }
//            }
//
//            mBroadcasting = false;
//        }
//    };
//
//    private class State {
//        private int backgroundColor;
//        private int accentColor;
//
//        public State() {
//            backgroundColor = getResources().getColor(R.color.black);
//            accentColor = getResources().getColor(R.color.red);
//        }
//
//        public void save() {
//            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(SettingsActivity.this);
//            prefs.edit()
//                    .putInt(BACKGROUND_COLOR_KEY, backgroundColor)
//                    .putInt(ACCENT_COLOR_KEY, accentColor)
//                    .commit();
//        }
//    }
}
