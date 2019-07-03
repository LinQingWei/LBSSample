package cn.way.lbs.abs;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public abstract class BaseLazyFragment extends BaseFragment {
    protected boolean mViewCreated;
    protected boolean mDataLoaded;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        prepareLoadData();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewCreated = true;
        prepareLoadData();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mDataLoaded = false;
        mViewCreated = false;
    }

    public boolean prepareLoadData() {
        return prepareLoadData(false);
    }

    public boolean prepareLoadData(boolean force) {
        if (force || (getUserVisibleHint() && mViewCreated && !mDataLoaded)) {
            loadData();
            mDataLoaded = true;

            return true;
        }

        return false;
    }

    protected abstract void loadData();
}
