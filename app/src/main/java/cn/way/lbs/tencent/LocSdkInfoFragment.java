package cn.way.lbs.tencent;

import android.content.Context;
import android.location.LocationManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.tencent.map.geolocation.TencentLocationManager;

import androidx.annotation.Nullable;
import cn.way.lbs.R;
import cn.way.lbs.abs.BaseLazyFragment;
import cn.way.lbs.util.Utils;

public class LocSdkInfoFragment extends BaseLazyFragment {
    private LocationManager mLocationManager;
    private WifiManager mWifiManager;

    private TextView mTvBuild;
    private TextView mTvVersion;
    private TextView mKey;
    private TextView mGps;
    private TextView mWifi;
    private Button mButton;

    public static LocSdkInfoFragment newInstance() {
        LocSdkInfoFragment fragment = new LocSdkInfoFragment();

        return fragment;
    }

    @Override
    protected void loadData() {
        updateUi();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_loc_sdk_info;
    }

    @Override
    protected void init(@Nullable View rootView, @Nullable Bundle savedInstanceState) {
        mLocationManager = (LocationManager) mAppContext.getSystemService(Context.LOCATION_SERVICE);
        mWifiManager = (WifiManager) mAppContext.getSystemService(Context.WIFI_SERVICE);

        mTvBuild = findViewById(R.id.build);
        mTvVersion = findViewById(R.id.version);
        mKey = findViewById(R.id.key);
        mGps = findViewById(R.id.gps);
        mWifi = findViewById(R.id.wifi);
        mButton = findViewById(R.id.button1);
        mButton.setOnClickListener((v) -> updateUi());
    }

    private void updateUi() {
        TencentLocationManager mgr = TencentLocationManager.getInstance(mActivity);
        // 显示 build 号
        mTvBuild.setText(String.format(getString(R.string.build), mgr.getBuild()));

        // 显示 版本号
        mTvVersion.setText(String.format(getString(R.string.version), mgr.getVersion()));

        mKey.setText(String.format(getString(R.string.key), Utils.getKey(mActivity)));

        // 显示 gps 状态
        boolean gpsEnabled;

        /* 防止BITA平台兼容性测试时潜在的权限禁止问题导致测试失败 */
        try {
            gpsEnabled = mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception e) {
            gpsEnabled = false;
        }
        mGps.setText(String.format(getString(R.string.gps), gpsEnabled ? "开启" : "关闭"));

        // 显示 wifi 状态
        mWifi.setText(String.format(getString(R.string.wifi), mWifiManager
                .isWifiEnabled() ? "开启" : "关闭", isWifiConnected() ? "连接"
                : "未连接"));
    }

    private boolean isWifiConnected() {
        WifiInfo wifiInfo = mWifiManager.getConnectionInfo();
        return wifiInfo != null && wifiInfo.getBSSID() != null;
    }
}
