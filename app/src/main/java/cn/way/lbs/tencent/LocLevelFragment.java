package cn.way.lbs.tencent;

import android.widget.Button;

import com.tencent.map.geolocation.TencentLocationRequest;

public class LocLevelFragment extends LocStatusFragment {
    private static final String[] NAMES = new String[]{
            "GEO", "NAME", "ADMIN AREA", "POI"
    };

    private static final int[] LEVELS = new int[]{
            TencentLocationRequest.REQUEST_LEVEL_GEO,
            TencentLocationRequest.REQUEST_LEVEL_NAME,
            TencentLocationRequest.REQUEST_LEVEL_ADMIN_AREA,
            TencentLocationRequest.REQUEST_LEVEL_POI
    };
    private static final int DEFAULT = 2;
    private int mIndex = DEFAULT;

    public static LocLevelFragment newInstance() {
        LocLevelFragment fragment = new LocLevelFragment();

        return fragment;
    }

    @Override
    protected void setSettings(Button btnSettings) {
        btnSettings.setText("Level");
    }

    @Override
    protected void onSettingsClick() {
        SettingFragment fragment =
                SettingFragment.show(getChildFragmentManager(),
                        NAMES, mIndex);
        fragment.setOnItemSelectedListener((pos) -> {
            mIndex = pos;
            fragment.dismiss();
        });
    }

    @Override
    protected TencentLocationRequest createRequest() {
        mRequestLevel = LEVELS[mIndex];
        // 创建定位请求
        TencentLocationRequest request = TencentLocationRequest.create()
                .setInterval(5 * 1000) // 设置定位周期
                .setAllowGPS(true)  //当为false时，设置不启动GPS。默认启动
                .setQQ("10001")
                .setRequestLevel(mRequestLevel); // 设置定位level

        return request;
    }
}
