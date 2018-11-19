package com.hd.attendance.activity.repast.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hd.attendance.R;
import com.hd.attendance.activity.repast.ui.RepastAddActivity;
import com.hd.attendance.db.RepastTable;
import com.hd.attendance.utils.ToastUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 考勤的列表信息
 */
public class RepastListAdapter extends RecyclerView.Adapter<RepastListAdapter.ViewHolder> {
    private Context mContext;
    private List<RepastTable> mDataBeans;
    private boolean isShowDate = false;//是否显示时间

    public void setShowDate(boolean showDate) {
        isShowDate = showDate;
    }

    public RepastListAdapter(Context mContext, List<RepastTable> dataBeans) {
        this.mContext = mContext;
        this.mDataBeans = dataBeans;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.repast_list_item,
                parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        RepastTable repastTable = mDataBeans.get(position);

        if (isShowDate) {
            //显示时间
            holder.tvName.setText(repastTable.getDate().substring(repastTable.getDate().length() - 2, repastTable.getDate().length()) + "日"
                    + "\n【" + repastTable.getWeek() + "】"
            );
        } else {
            holder.tvName.setText(repastTable.getUser_Name());
        }

        holder.tvNote.setText(repastTable.getNote() == null ? "" : repastTable.getNote());
        if (repastTable.isAfternoon_Report() && repastTable.isAfternoon_Eat()) {
            //同时报了餐 又吃了饭
            holder.tvAfternoonIsMeal.setTextColor(mContext.getResources().getColor(R.color.green));
            holder.tvAfternoonIsRepast.setTextColor(mContext.getResources().getColor(R.color.green));
        } else {
            if (repastTable.isAfternoon_Report() && !repastTable.isAfternoon_Eat()) {
                //报了餐 没吃饭
                holder.tvAfternoonIsMeal.setTextColor(mContext.getResources().getColor(R.color.green));
                holder.tvAfternoonIsRepast.setTextColor(mContext.getResources().getColor(R.color.red));
            }
            if (repastTable.isAfternoon_Eat() && !repastTable.isAfternoon_Report()) {
                //吃了饭 没报餐
                holder.tvAfternoonIsMeal.setTextColor(mContext.getResources().getColor(R.color.red));
                holder.tvAfternoonIsRepast.setTextColor(mContext.getResources().getColor(R.color.green));
            }
        }

        if (repastTable.isEvening_Report() && repastTable.isEvening_Eat()) {
            //同时报了餐 又吃了饭
            holder.tvEveningIsMeal.setTextColor(mContext.getResources().getColor(R.color.green));
            holder.tvEveningIsRepast.setTextColor(mContext.getResources().getColor(R.color.green));
        } else {
            if (repastTable.isEvening_Report() && !repastTable.isEvening_Eat()) {
                //报了餐 没吃饭
                holder.tvEveningIsMeal.setTextColor(mContext.getResources().getColor(R.color.green));
                holder.tvEveningIsRepast.setTextColor(mContext.getResources().getColor(R.color.red));
            }
            if (repastTable.isEvening_Eat() && !repastTable.isEvening_Report()) {
                //吃了饭 没报餐
                holder.tvEveningIsMeal.setTextColor(mContext.getResources().getColor(R.color.red));
                holder.tvEveningIsRepast.setTextColor(mContext.getResources().getColor(R.color.green));
            }
        }


        if (repastTable.isAfternoon_Report()) {
            holder.tvAfternoonIsMeal.setText("是否报餐:是 【" + repastTable.getAfternoon_Report_time() + "】");
        } else {
            holder.tvAfternoonIsMeal.setText("是否报餐:否");
        }

        if (repastTable.isAfternoon_Eat()) {
            holder.tvAfternoonIsRepast.setText("是否就餐:是 【" + repastTable.getAfternoon_Eat_time() + "】");
        } else {
            holder.tvAfternoonIsRepast.setText("是否就餐:否");
        }

        if (repastTable.isEvening_Report()) {
            holder.tvEveningIsMeal.setText("是否报餐:是 【" + repastTable.getEvening_Report_time() + "】");
        } else {
            holder.tvEveningIsMeal.setText("是否报餐:否");
        }

        if (repastTable.isEvening_Eat()) {
            holder.tvEveningIsRepast.setText("是否就餐:是 【" + repastTable.getEvening_Eat_time() + "】");
        } else {
            holder.tvEveningIsRepast.setText("是否就餐:否");
        }
        holder.itemView.setOnClickListener(v -> {
            if (!isShowDate) {
                Intent intent = new Intent(mContext, RepastAddActivity.class);
                intent.putExtra("RepastID", repastTable.getId() + "");
                mContext.startActivity(intent);
            }
        });

    }


    @Override
    public int getItemCount() {
        return mDataBeans.isEmpty() ? 0 : mDataBeans.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.tv_afternoon_is_meal)
        TextView tvAfternoonIsMeal;
        @BindView(R.id.tv_afternoon_is_repast)
        TextView tvAfternoonIsRepast;
        @BindView(R.id.tv_evening_is_meal)
        TextView tvEveningIsMeal;
        @BindView(R.id.tv_evening_is_repast)
        TextView tvEveningIsRepast;
        @BindView(R.id.tv_note)
        TextView tvNote;
        View itemView;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            this.itemView = itemView;
        }
    }


}
