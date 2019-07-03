package cn.way.lbs.tencent;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import cn.way.lbs.R;
import cn.way.lbs.abs.BaseActivity;

public class LocationActivity extends BaseActivity {
    private static final String TAG = LocationActivity.class.getSimpleName();

    private ViewPager mViewPager;
    private FmPagerAdapter mPagerAdapter;
    private List<Fragment> mFragmentList;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_tencent_location;
    }

    @Override
    protected void init(@Nullable Bundle savedInstanceState) {
        super.init(savedInstanceState);
        mViewPager = findViewById(R.id.view_pager);
        mFragmentList = loadFragments();
        mPagerAdapter = new FmPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mPagerAdapter);
    }

    private List<Fragment> loadFragments() {
        ArrayList<Fragment> fragments = new ArrayList<>(1);
        fragments.add(LocSdkInfoFragment.newInstance());

        return fragments;
    }

    private class FmPagerAdapter extends FragmentPagerAdapter {

        public FmPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList == null ? null : mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return (mFragmentList == null || mFragmentList.isEmpty()) ? 0 : mFragmentList.size();
        }
    }
}
