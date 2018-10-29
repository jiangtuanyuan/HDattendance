package com.hd.attendance.activity.fingerprint.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.hd.attendance.R;
import com.hd.attendance.db.EmployeesTable;
import com.hd.attendance.db.FingerInfoTable;
import com.hd.attendance.utils.ToastUtil;
import com.zkteco.android.biometric.module.fingerprintreader.ZKFingerService;

import org.litepal.LitePal;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by 蒋 on 2018/9/22.
 * 指纹列表
 */

public class FingerItemAdapter extends RecyclerView.Adapter<FingerItemAdapter.ViewHolder> {

    private Context mContext;
    private List<FingerInfoTable> mList;

    public FingerItemAdapter(Context context, List<FingerInfoTable> mList) {
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
        final FingerInfoTable bean = mList.get(position);

        holder.tvLeftName.setText(bean.getUser_Name().substring(bean.getUser_Name().length() - 1, bean.getUser_Name().length()));
        holder.tvNum.setText(bean.getId() + "");
        holder.tvName.setText(bean.getUser_Name());

        if (bean.getStauts() == 1) {
            holder.tvState.setText("禁用");
            holder.tvState.setTextColor(mContext.getResources().getColor(R.color.red));

            holder.btDisable.setText("启用");
            holder.btDisable.setBackgroundResource(R.drawable.shape_rectangle_blue_bg);

        } else {
            holder.tvState.setText("启用");
            holder.tvState.setTextColor(mContext.getResources().getColor(R.color.blue));

            holder.btDisable.setText("禁用");
            holder.btDisable.setBackgroundResource(R.drawable.shape_rectangle_gray_bg);

        }

        holder.btDisable.setOnClickListener(v -> {
            if (bean.getStauts() == 0) {
                bean.setStauts(1);
                bean.save();
                ToastUtil.showToast("禁用成功!");
            } else {
                bean.setStauts(0);
                bean.save();
                ToastUtil.showToast("启用成功!");
            }

            mList.clear();
            mList.addAll(LitePal.order("id desc").find(FingerInfoTable.class));
            notifyDataSetChanged();
        });

        holder.btEditor.setVisibility(View.GONE);
        holder.btEditor.setOnClickListener(v -> ToastUtil.showToast("编辑"));

        holder.btDelete.setOnClickListener(v -> DeleteFinger(bean));
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    protected class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_left_name)
        TextView tvLeftName;
        @BindView(R.id.tv_num)
        TextView tvNum;
        @BindView(R.id.tv_name)
        TextView tvName;
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


    private void DeleteFinger(final FingerInfoTable e) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setMessage("是否删除该枚指纹?");
        builder.setTitle("是否删除?");

        builder.setPositiveButton("确定", (dialog, which) -> {
            dialog.dismiss();

            int ss = ZKFingerService.del(e.getUser_ID() + "_" + e.getUser_Name());
            if (ss == 0) {
                e.delete();
                ToastUtil.showToast("删除成功");

                mList.clear();
                mList.addAll(LitePal.order("id desc").find(FingerInfoTable.class));
                notifyDataSetChanged();

            }


        });
        builder.setNegativeButton("取消", (dialog, which) -> dialog.dismiss());
        builder.show();
    }

}
