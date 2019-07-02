package cn.way.lbs.abs.recycler;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.IdRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.collection.SparseArrayCompat;
import androidx.recyclerview.widget.RecyclerView;

public final class ViewHolder extends RecyclerView.ViewHolder {
    private SparseArrayCompat<View> mViews;

    public ViewHolder(@NonNull View itemView) {
        super(itemView);
        mViews = new SparseArrayCompat<>();
    }

    public static ViewHolder create(@NonNull View itemView) {
        ViewHolder holder = new ViewHolder(itemView);

        return holder;
    }

    public static ViewHolder create(@NonNull ViewGroup parent, @LayoutRes int layoutId) {
        Context context = parent.getContext();
        View itemView = LayoutInflater.from(context).inflate(layoutId, parent, false);
        ViewHolder holder = new ViewHolder(itemView);

        return holder;
    }

    public <T extends View> T getView(@IdRes int viewId) {
        View view = mViews.get(viewId);
        if (view == null) {
            view = itemView.findViewById(viewId);
            mViews.put(viewId, view);
        }

        return (T) view;
    }

    public ViewHolder setText(@IdRes int viewId, String text) {
        TextView tv = getView(viewId);
        tv.setText(text);

        return this;
    }

    public ViewHolder setImageDrawable(@IdRes int viewId, Drawable drawable) {
        ImageView iv = getView(viewId);
        iv.setImageDrawable(drawable);

        return this;
    }
}
