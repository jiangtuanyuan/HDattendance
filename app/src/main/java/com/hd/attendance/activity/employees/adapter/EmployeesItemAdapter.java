package com.hd.attendance.activity.employees.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.hd.attendance.R;
import com.hd.attendance.activity.employees.ui.EmployeesAddActivity;
import com.hd.attendance.activity.fingerprint.bean.UserFingerBean;
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

public class EmployeesItemAdapter extends RecyclerView.Adapter<EmployeesItemAdapter.ViewHolder> {

    private Context mContext;
    private List<EmployeesTable> mList;

    public EmployeesItemAdapter(Context context, List<EmployeesTable> mList) {
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
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
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

        holder.btEditor.setOnClickListener(v -> StartEditorUI(bean));
        holder.btDelete.setOnClickListener(v -> DeleteUser(bean));
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
     * 启动到编辑界面
     *
     * @param e
     */
    private void StartEditorUI(EmployeesTable e) {
        Intent intent = new Intent(mContext, EmployeesAddActivity.class);
        intent.putExtra("id", e.getId());
        intent.putExtra("name", e.getName());
        intent.putExtra("sex", e.getSex());
        intent.putExtra("jobs", e.getJobs());
        mContext.startActivity(intent);
    }

    private void DeleteUser(final EmployeesTable e) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setMessage("删除之后不可恢复,请谨慎操作!");
        builder.setTitle("是否删除?");

        builder.setPositiveButton("确定", (dialog, which) -> {
            dialog.dismiss();
            e.delete();
            ToastUtil.showToast("删除成功");

            mList.clear();
            mList.addAll(LitePal.order("id desc").find(EmployeesTable.class));
            notifyDataSetChanged();

        });
        builder.setNegativeButton("取消", (dialog, which) -> dialog.dismiss());
        builder.show();
    }

}
