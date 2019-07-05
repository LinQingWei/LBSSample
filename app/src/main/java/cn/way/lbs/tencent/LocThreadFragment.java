package cn.way.lbs.tencent;

import android.os.Bundle;
import android.os.HandlerThread;
import android.os.Looper;
import android.view.View;
import android.widget.Button;

import com.tencent.map.geolocation.TencentLocation;
import com.tencent.map.geolocation.TencentLocationRequest;

import androidx.annotation.Nullable;

public class LocThreadFragment extends LocStatusFragment {

    private static final String[] THREADS = new String[]{"后台线程", "主线程"};
    private static final int BG_THREAD = 0;
    private static final int MAIN_THREAD = 1;

    private int mIndex = BG_THREAD;
    private HandlerThread mThread;

    public static LocThreadFragment newInstance() {
        LocThreadFragment fragment = new LocThreadFragment();

        return fragment;
    }

    @Override
    protected void init(@Nullable View rootView, @Nullable Bundle savedInstanceState) {
        super.init(rootView, savedInstanceState);
        mThread = new HandlerThread("Thread_loc_" + (int) (Math.random() * 10));
        mThread.start();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mThread.getLooper().quit();
    }

    @Override
    protected int requestLocationUpdates(TencentLocationRequest request) {
        Looper otherLooper = mThread.getLooper();
        int error = TencentLocation.ERROR_UNKNOWN;
        if (mIndex == BG_THREAD) {
            // 开始定位, 在 mThread 线程中
            error = mLocationManager.requestLocationUpdates(request, this, otherLooper);
        } else if (mIndex == MAIN_THREAD) {
            // 开始定位, 在主线程中
            error = mLocationManager.requestLocationUpdates(request, this);
        }

        return error;
    }

    @Override
    protected void setSettings(Button btnSettings) {
        btnSettings.setText("Thread");
    }

    @Override
    protected void onSettingsClick() {
        SettingFragment fragment =
                SettingFragment.show(getChildFragmentManager(),
                        THREADS, mIndex);
        fragment.setOnItemSelectedListener((pos) -> {
            mIndex = pos;
            fragment.dismiss();
        });
    }
}
