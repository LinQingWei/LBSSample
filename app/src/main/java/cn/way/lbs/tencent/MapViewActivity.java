package cn.way.lbs.tencent;

import android.os.Bundle;

import com.tencent.tencentmap.mapsdk.maps.MapView;
import com.tencent.tencentmap.mapsdk.maps.TencentMap;

import cn.way.lbs.BaseActivity;
import cn.way.lbs.R;

public class MapViewActivity extends BaseActivity {
    private MapView mMapView;
    private TencentMap mTencentMap;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_tencent_map_view;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        mMapView = findViewById(R.id.map_view);
        mTencentMap = mMapView.getMap();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        mMapView.onRestart();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mMapView.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mMapView.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }
}
