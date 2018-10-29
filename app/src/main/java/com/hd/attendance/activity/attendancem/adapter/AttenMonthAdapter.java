package com.hd.attendance.activity.attendancem.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hd.attendance.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by 蒋 on 2018/8/24.
 * 横向滚动适配器
 */

public class AttenMonthAdapter extends RecyclerView.Adapter<AttenMonthAdapter.ViewHolder> {

    private Context mContext;
    private List<String> mDataBeans;
    private OnItemClickListener mOnItemClickListener;
    private int defItem = -1;

    public AttenMonthAdapter(List<String> dataBeans) {
        mDataBeans = dataBeans;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (mContext == null) {
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.user_finger_top_textview, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        holder.tv_date.setText(mDataBeans.get(position));
        if (defItem != -1) {
            if (defItem == position) {
                //选中
                holder.tv_date.setTextColor(ContextCompat.getColor(mContext, R.color.white));
                holder.tv_date.setBackgroundColor(ContextCompat.getColor(mContext, R.color.blue));
            } else {
                holder.tv_date.setTextColor(ContextCompat.getColor(mContext, R.color.gray));
                holder.tv_date.setBackgroundColor(ContextCompat.getColor(mContext, R.color.white));
            }
        }
        holder.tv_date.setOnClickListener(v -> mOnItemClickListener.onItemClickListener(position));
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return mDataBeans.isEmpty() ? 0 : mDataBeans.size();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClickListener(int position);
    }

    public void setDefSelect(int position) {
        this.defItem = position;
        notifyDataSetChanged();
    }

    protected class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_date)
        TextView tv_date;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}