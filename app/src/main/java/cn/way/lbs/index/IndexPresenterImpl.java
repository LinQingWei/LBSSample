package cn.way.lbs.index;

import android.content.Context;

import java.util.Arrays;
import java.util.List;

import cn.way.lbs.R;

public class IndexPresenterImpl implements IndexContract.IndexPresenter {
    private IndexContract.IndexView mIndexView;

    public IndexPresenterImpl(IndexContract.IndexView indexView) {
        mIndexView = indexView;
        mIndexView.setPresenter(this);
    }

    @Override
    public void start() {
        List<String> indexList = getIndexList(mIndexView.getContext());
        mIndexView.loadFinish(indexList);
    }

    private List<String> getIndexList(Context context) {
        String[] array = context.getResources().getStringArray(R.array.lbs_index);
        return Arrays.asList(array);
    }
}
