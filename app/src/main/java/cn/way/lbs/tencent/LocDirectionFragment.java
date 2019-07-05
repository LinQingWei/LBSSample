package cn.way.lbs.tencent;

import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.view.View;

import com.tencent.map.geolocation.TencentLocation;
import com.tencent.map.geolocation.TencentLocationRequest;

import androidx.annotation.Nullable;

public class LocDirectionFragment extends LocStatusFragment {
    private Handler mHandler;
    private HandlerThread mThread;

    public static LocDirectionFragment newInstance() {
        LocDirectionFragment fragment = new LocDirectionFragment();

        return fragment;
    }

    @Override
    protected void init(@Nullable View rootView, @Nullable Bundle savedInstanceState) {
        super.init(rootView, savedInstanceState);
        mDirectionView.setVisibility(View.VISIBLE);
        mThread = new HandlerThread("Thread_demo_" + (int) (Math.random() * 10));
        mThread.start();
        mHandler = new Handler(mThread.getLooper());
    }

    @Override
    protected TencentLocationRequest createRequest() {
        TencentLocationRequest request = super.createRequest();
        request.setAllowDirection(true)
                .setInterval(500);

        return request;
    }

    @Override
    protected int requestLocationUpdates(TencentLocationRequest request) {
        mHandler.post(() -> mLocationManager.requestLocationUpdates(request, LocDirectionFragment.this));
        return 0;
    }

    @Override
    public void onLocationChanged(TencentLocation location, int error, String reason) {
        super.onLocationChanged(location, error, reason);
        if (error == TencentLocation.ERROR_OK) {
            double direction = location.getExtra().getDouble(TencentLocation.EXTRA_DIRECTION);
            updateLocationStatus("\n方向=" + (int) direction, false);
            mDirectionView.updateDirection(direction);
        } else {
            mDirectionView.updateDirection(0);
        }
    }

    @Override
    public void onDestroy() {
        // 清空
        mHandler.removeCallbacksAndMessages(null);
        // 停止线程
        mThread.getLooper().quit();
        super.onDestroy();
    }
}
