package com.hd.attendance.activity.group.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.hd.attendance.R;
import com.hd.attendance.activity.employees.ui.EmployeesAddActivity;
import com.hd.attendance.db.EmployeesTable;
import com.hd.attendance.utils.ToastUtil;

import org.litepal.LitePal;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by 蒋 on 2018/9/22.
 * 员工列表适配器
 */

public class UserItemAdapter extends RecyclerView.Adapter<UserItemAdapter.ViewHolder> {

    private Context mContext;
    private List<EmployeesTable> mList;

    public UserItemAdapter(Context context, List<EmployeesTable> mList) {
        this.mContext = context;
        this.mList = mList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (mContext == null) {
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.emplyees_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        final EmployeesTable bean = mList.get(position);
        holder.tv_LeftName.setText(bean.getName().substring(bean.getName().length() - 1, bean.getName().length()));
        holder.tvName.setText(bean.getName());
        holder.tvNum.setText("" + bean.getId());

        if (bean.getSex().equals("女")) {
            holder.tvSex.setBackgroundResource(R.drawable.ic_sex_famale);
            holder.tv_LeftName.setBackgroundResource(R.drawable.shape_rectangle_pink_bg);

        } else {
            holder.tvSex.setBackgroundResource(R.drawable.ic_sex_male);
            holder.tv_LeftName.setBackgroundResource(R.drawable.shape_rectangle_blue_bg);
        }

        holder.tvJobs.setText(bean.getJobs());

        holder.btEditor.setVisibility(View.GONE);
        holder.btDelete.setText("移 除");
        holder.btDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeUser(bean);

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
        @BindView(R.id.tv_num)
        TextView tvNum;
        @BindView(R.id.tv_sex)
        ImageView tvSex;
        @BindView(R.id.tv_jobs)
        TextView tvJobs;
        @BindView(R.id.bt_editor)
        Button btEditor;
        @BindView(R.id.bt_delete)
        Button btDelete;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    /**
     * 移除成员
     *
     * @param e
     */
    private void removeUser(final EmployeesTable e) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setMessage("是否移除此成员?");
        builder.setTitle("选  择");

        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                int gropuID = e.getGroup_ID();
                e.setGroup_ID(0);
                e.save();

                mList.clear();
                mList.addAll(LitePal.where("group_ID=?", gropuID + "").find(EmployeesTable.class));
                notifyDataSetChanged();

                ToastUtil.showToast("移除成功!");

            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }


}
