package com.hd.attendance.activity.fingerprint.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.hd.attendance.R;
import com.hd.attendance.activity.fingerprint.bean.UserFingerBean;
import com.hd.attendance.utils.ToastUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by 蒋 on 2018/9/22.
 */

public class FingerItemAdapter extends RecyclerView.Adapter<FingerItemAdapter.ViewHolder> {

    private Context mContext;
    private List<UserFingerBean> mList;

    public FingerItemAdapter(Context context, List<UserFingerBean> mList) {
        this.mContext = context;
        this.mList = mList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (mContext == null) {
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.user_finger_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        UserFingerBean bean = mList.get(position);

        holder.tv_LeftName.setText(bean.getUserNmae().substring(bean.getUserNmae().length()-1,bean.getUserNmae().length()));
        holder.tvName.setText(bean.getUserNmae());
        holder.tvGrouping.setText(bean.getGroupName());
        holder.tvState.setText(bean.getFingerState());

        holder.btDisable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtil.showToast("禁用");
            }
        });

        holder.btEditor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtil.showToast("编辑");
            }
        });
        holder.btDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtil.showToast("删除");
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    protected class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_left_name)
        TextView tv_LeftName;
        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.tv_grouping)
        TextView tvGrouping;
        @BindView(R.id.tv_state)
        TextView tvState;
        @BindView(R.id.bt_disable)
        Button btDisable;
        @BindView(R.id.bt_editor)
        Button btEditor;
        @BindView(R.id.bt_delete)
        Button btDelete;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
