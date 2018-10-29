package com.hd.attendance.activity.fingerprint.adapter;

import android.content.Context;
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

public class FingerGroupAdapter extends RecyclerView.Adapter<FingerGroupAdapter.ViewHolder> {

    private Context mContext;
    private List<String> mDataBeans;
    private OnItemClickListener mOnItemClickListener;
    private int defItem = -1;

    public FingerGroupAdapter(List<String> dataBeans) {
        mDataBeans = dataBeans;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mContext == null) {
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.user_finger_top_textview, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
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
        holder.tv_date.setOnClickListener(v -> mOnItemClickListener.onItemClickListener(v, position));
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
        void onItemClickListener(View view, int position);
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