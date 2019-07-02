package cn.way.lbs.abs;

import android.content.Context;

public interface IView<T> {
    Context getContext();

    void setPresenter(T presenter);
}
