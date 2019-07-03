package cn.way.lbs.abs;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import cn.way.lbs.util.VLog;

public abstract class BaseFragment extends Fragment {
    private static final String TAG = BaseFragment.class.getSimpleName();

    protected Activity mActivity;
    protected Context mAppContext;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mAppContext = context.getApplicationContext();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = activity;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView;
        final int layoutId = getLayoutId();
        if (layoutId > 0) {
            rootView = inflater.inflate(layoutId, container, false);
        } else {
            rootView = super.onCreateView(inflater, container, savedInstanceState);
            VLog.w(TAG, "layout id maybe wrong:" + layoutId + ", rootView:" + rootView);
        }

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view, savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        onCustomizeActionBar(getActivity().getActionBar());
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mActivity = null;
        mAppContext = null;
    }

    protected abstract int getLayoutId();

    protected abstract void init(@Nullable View rootView, @Nullable Bundle savedInstanceState);

    protected void onCustomizeActionBar(ActionBar actionBar) {
        // TODO
    }

    protected <T extends View> T findViewById(@IdRes int id) {
        final View rootView = getView();
        if (rootView == null) {
            return null;
        }

        return rootView.findViewById(id);
    }
}
