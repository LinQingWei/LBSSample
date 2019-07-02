package cn.way.lbs.index;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import cn.way.lbs.R;
import cn.way.lbs.abs.recycler.BaseAdapter;
import cn.way.lbs.abs.recycler.DividerItemDecoration;
import cn.way.lbs.abs.recycler.ViewHolder;

public class IndexFragment extends Fragment implements IndexContract.IndexView {
    private RecyclerView mRecyclerView;
    private BaseAdapter<String> mAdapter;
    private IndexContract.IndexPresenter mIndexPresenter;

    public static IndexFragment newInstance() {
        IndexFragment fragment = new IndexFragment();

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_index, container, false);
        initView(rootView, savedInstanceState);
        return rootView;
    }

    @Override
    public void setPresenter(IndexContract.IndexPresenter presenter) {
        mIndexPresenter = presenter;
    }

    @Override
    public void loadFinish(List<String> indexList) {
        mAdapter.setDataList(indexList);
    }

    private void initView(View rootView, @Nullable Bundle savedInstanceState) {
        mRecyclerView = rootView.findViewById(android.R.id.list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL_LIST));
        mAdapter = new BaseAdapter<String>(null, R.layout.item_index) {
            @Override
            protected void convert(@NonNull ViewHolder holder, String s, int position) {
                holder.setText(R.id.tv_title, s);
            }
        };
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, ViewHolder holder, int position) {
                Intent intent = getIndexIntent(position);
                startActivity(intent);
            }

            @Override
            public boolean onItemLongClick(View view, ViewHolder holder, int position) {
                return false;
            }
        });

        new IndexPresenterImpl(this).start();
    }

    private static final String ACTION_LBS_INDEX_PREFIX = "cn.way.lbs.intent.action.LBS_INDEX_";

    private Intent getIndexIntent(final int position) {
        String action = ACTION_LBS_INDEX_PREFIX + position;
        Intent intent = new Intent(action);

        return intent;
    }
}
