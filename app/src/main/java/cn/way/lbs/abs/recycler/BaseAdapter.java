package cn.way.lbs.abs.recycler;

import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public abstract class BaseAdapter<T> extends RecyclerView.Adapter<ViewHolder> {
    private int mItemLayoutId;
    protected List<T> mDataList;
    private OnItemClickListener mClickListener;

    public BaseAdapter(List<T> dataList, int itemLayoutId) {
        mDataList = dataList;
        mItemLayoutId = itemLayoutId;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ViewHolder holder = ViewHolder.create(parent, mItemLayoutId);
        onViewHolderCreated(holder);
        setListener(holder, viewType);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        convert(holder, mDataList.get(position), position);
    }

    @Override
    public int getItemCount() {
        int count = mDataList == null ? 0 : mDataList.size();
        return count;
    }

    protected void onViewHolderCreated(ViewHolder holder) {
    }

    protected abstract void convert(@NonNull ViewHolder holder, T t, final int position);

    public List<T> getDataList() {
        return mDataList;
    }

    public void setDataList(List<T> dataList) {
        mDataList = dataList;
        notifyDataSetChanged();
    }

    protected boolean isEnabled(final int viewType) {
        return true;
    }

    protected void setListener(final ViewHolder holder, int viewType) {
        if (!isEnabled(viewType)) {
            return;
        }

        holder.itemView.setOnClickListener(v -> {
            if (mClickListener != null) {
                final int position = holder.getAdapterPosition();
                mClickListener.onItemClick(v, holder, position);
            }
        });

        holder.itemView.setOnLongClickListener(v -> {
            if (mClickListener != null) {
                final int position = holder.getAdapterPosition();
                return mClickListener.onItemLongClick(v, holder, position);
            }

            return false;
        });
    }

    public void setOnItemClickListener(OnItemClickListener clickListener) {
        mClickListener = clickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, ViewHolder holder, int position);

        boolean onItemLongClick(View view, ViewHolder holder, int position);
    }
}
