package cn.way.lbs.tencent;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.tencent.map.geolocation.TencentLocation;
import com.tencent.map.geolocation.TencentLocationListener;
import com.tencent.map.geolocation.TencentLocationManager;
import com.tencent.map.geolocation.TencentLocationRequest;
import com.tencent.tencentmap.mapsdk.maps.CameraUpdateFactory;
import com.tencent.tencentmap.mapsdk.maps.MapView;
import com.tencent.tencentmap.mapsdk.maps.TencentMap;
import com.tencent.tencentmap.mapsdk.maps.model.BitmapDescriptorFactory;
import com.tencent.tencentmap.mapsdk.maps.model.Circle;
import com.tencent.tencentmap.mapsdk.maps.model.CircleOptions;
import com.tencent.tencentmap.mapsdk.maps.model.LatLng;
import com.tencent.tencentmap.mapsdk.maps.model.Marker;
import com.tencent.tencentmap.mapsdk.maps.model.MarkerOptions;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import cn.way.lbs.R;
import cn.way.lbs.abs.BaseLazyFragment;
import cn.way.lbs.util.ToastUtils;
import cn.way.lbs.util.Utils;
import cn.way.lbs.util.VLog;
import cn.way.lbs.widget.DirectionView;

public class LocStatusFragment extends BaseLazyFragment implements TencentLocationListener,
        View.OnClickListener {
    private static final String TAG = LocStatusFragment.class.getSimpleName();
    private static final long[] INTERVALS = new long[]{
            2 * 1000, 3 * 1000, 5 * 1000, 10 * 1000
    };
    private static final int DEFAULT_INDEX = 2;
    private int mIntervalIndex = DEFAULT_INDEX;

    private MapView mMapView;
    private TencentMap mTencentMap;
    private Marker mLocationMarker;
    private Circle mAccuracyCircle;
    private TextView mTvLocationStatus;
    private Button mBtnStartLocation;
    private Button mBtnStopLocation;
    private Button mBtnClearStatus;
    private Button mBtnSettings;
    protected DirectionView mDirectionView;
    private boolean mIsOnTop;
    private boolean mStarted;
    protected TencentLocationManager mLocationManager;
    protected int mRequestLevel = TencentLocationRequest.REQUEST_LEVEL_GEO;

    public static LocStatusFragment newInstance() {
        LocStatusFragment fragment = new LocStatusFragment();

        return fragment;
    }

    @Override
    protected void loadData() {
        startLocation();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_loc_status;
    }

    @Override
    protected void init(@Nullable View rootView, @Nullable Bundle savedInstanceState) {
        mLocationManager = TencentLocationManager.getInstance(getContext());
        // 设置坐标系为 gcj-02, 缺省坐标为 gcj-02, 所以通常不必进行如下调用
        mLocationManager.setCoordinateType(TencentLocationManager.COORDINATE_TYPE_GCJ02);
        mTvLocationStatus = findViewById(R.id.status);
        mMapView = findViewById(R.id.map_view);
        mMapView.setOnTop(mIsOnTop);
        mTencentMap = mMapView.getMap();
        mDirectionView = findViewById(R.id.direction_view);

        mBtnStartLocation = findViewById(R.id.startLocation);
        mBtnStartLocation.setOnClickListener(this);
        mBtnStopLocation = findViewById(R.id.stopLocation);
        mBtnStopLocation.setOnClickListener(this);
        mBtnClearStatus = findViewById(R.id.clear);
        mBtnClearStatus.setOnClickListener(this);
        mBtnSettings = findViewById(R.id.settings);
        mBtnSettings.setOnClickListener(this);
        setSettings(mBtnSettings);
    }

    @Override
    public void onStart() {
        mMapView.onStart();
        super.onStart();
    }

    @Override
    public void onResume() {
        mMapView.onResume();
        super.onResume();
    }

    @Override
    public void onPause() {
        mMapView.onPause();
        super.onPause();
    }

    @Override
    public void onStop() {
        mMapView.onStop();
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        mMapView.onDestroy();
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        mMapView.onDestroy();
        super.onDestroy();
        stopLocation();
        mMapView = null;
    }

    @Override
    public void onLocationChanged(TencentLocation location, int error, String reason) {
        String msg;
        if (error == TencentLocation.ERROR_OK) {
            // 定位成功
            msg = Utils.getLocationString(location, mRequestLevel);
            LatLng latLngLocation = new LatLng(location.getLatitude(), location.getLongitude());
            // 更新 location Marker
            if (mLocationMarker == null) {
                mLocationMarker =
                        mTencentMap.addMarker(new MarkerOptions(latLngLocation).
                                icon(BitmapDescriptorFactory.fromResource(R.drawable.mark_location)));
            } else {
                mLocationMarker.setPosition(latLngLocation);
            }
            mTencentMap.moveCamera(CameraUpdateFactory.newLatLng(latLngLocation));

            if (mAccuracyCircle == null) {
                mAccuracyCircle = mTencentMap.addCircle(new CircleOptions().
                        center(latLngLocation).
                        radius(location.getAccuracy()).
                        fillColor(0x884433ff).
                        strokeColor(0xaa1122ee).
                        strokeWidth(1));
            } else {
                mAccuracyCircle.setCenter(latLngLocation);
                mAccuracyCircle.setRadius(location.getAccuracy());
            }
        } else {
            // 定位失败
            msg = "定位失败: " + reason;
        }

        updateLocationStatus(msg);
        VLog.i(TAG, msg);
    }

    @Override
    public void onStatusUpdate(String name, int status, String desc) {
        String message = "{name=" + name + ", new status=" + status + ", desc="
                + desc + "}";

        if (status == STATUS_DENIED) {
            /* 检测到定位权限被内置或第三方的权限管理或安全软件禁用, 导致当前应用**很可能无法定位**
             * 必要时可对这种情况进行特殊处理, 比如弹出提示或引导
             */
            ToastUtils.toast("定位权限被禁用!");
        }

        updateLocationStatus(message);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.startLocation:
                startLocation();
                break;
            case R.id.stopLocation:
                stopLocation();
                break;
            case R.id.clear:
                clearStatus();
                break;
            case R.id.settings:
                onSettingsClick();
                break;
            default:
                break;
        }
    }

    public void setOnTop(boolean onTop) {
        mIsOnTop = onTop;
    }

    protected void setSettings(Button btnSettings) {
        btnSettings.setText("周期");
    }

    protected void onSettingsClick() {
        SettingFragment fragment =
                SettingFragment.show(getChildFragmentManager(),
                        new String[]{"2s", "3s", "5s", "10s"}, mIntervalIndex);
        fragment.setOnItemSelectedListener((pos) -> {
            mIntervalIndex = pos;
            fragment.dismiss();
        });
    }

    protected void startLocation() {
        if (mStarted) {
            VLog.w(TAG, "already started location");
            return;
        }

        mStarted = true;
        TencentLocationRequest request = createRequest();
        int error = requestLocationUpdates(request);
        updateLocationStatus("开始定位: " + request + ", 坐标系="
                + Utils.getCoordinateTypeString(mLocationManager.getCoordinateType())
                + ", error:" + error);
    }

    protected TencentLocationRequest createRequest() {
        // 创建定位请求
        TencentLocationRequest request = TencentLocationRequest.create();
        request.setRequestLevel(mRequestLevel);

        // 修改定位请求周期参数, 默认周期为 5000 ms
        request.setInterval(INTERVALS[mIntervalIndex]);

        return request;
    }

    protected int requestLocationUpdates(TencentLocationRequest request) {
        // 开始定位
        int error = mLocationManager.requestLocationUpdates(request, this);

        return error;
    }

    protected void stopLocation() {
        if (mStarted) {
            mStarted = false;
            mLocationManager.removeUpdates(this);
            updateLocationStatus("停止定位");
        }
    }

    protected void clearStatus() {
        mTvLocationStatus.setText(null);
    }

    protected void updateLocationStatus(String message) {
        updateLocationStatus(message, true);
    }

    protected void updateLocationStatus(String message, final boolean withThreadInfo) {
        String status;
        if (withThreadInfo) {
            String threadName = Thread.currentThread().getName();
            status = message + (", 当前线程=" + threadName);
        } else {
            status = message;
        }

        mTvLocationStatus.post(() -> {
            mTvLocationStatus.append(status);
            mTvLocationStatus.append("\n---\n");
        });
    }

    public static class SettingFragment extends DialogFragment {
        private static final String KEY_ITEMS = "items";
        private static final String KEY_DEF_INDEX = "def_index";

        public static SettingFragment show(FragmentManager fm, String[] items, int defIndex) {
            SettingFragment fragment = new SettingFragment();
            Bundle args = new Bundle();
            args.putStringArray(KEY_ITEMS, items);
            args.putInt(KEY_DEF_INDEX, defIndex);
            fragment.setArguments(args);
            fragment.show(fm, "settings");

            return fragment;
        }

        @NonNull
        @Override
        public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            String[] items = getArguments().getStringArray(KEY_ITEMS);
            final int defIndex = getArguments().getInt(KEY_DEF_INDEX, 0);
            builder.setSingleChoiceItems(items, defIndex,
                    (dialogInterface, i) -> {
                        if (mOnItemSelectedListener != null) {
                            mOnItemSelectedListener.onSelected(i);
                        }
                    });
            return builder.create();
        }

        private OnItemSelectedListener mOnItemSelectedListener;

        public void setOnItemSelectedListener(OnItemSelectedListener onItemSelectedListener) {
            mOnItemSelectedListener = onItemSelectedListener;
        }

        public interface OnItemSelectedListener {
            void onSelected(int pos);
        }
    }
}
