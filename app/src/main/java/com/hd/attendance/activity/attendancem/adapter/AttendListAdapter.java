package com.hd.attendance.activity.attendancem.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hd.attendance.R;
import com.hd.attendance.activity.attendancem.AttendType;
import com.hd.attendance.activity.attendancem.WorkType;
import com.hd.attendance.activity.attendancem.ui.AttendancemEditorActivity;
import com.hd.attendance.db.AttendancemTable;
import com.hd.attendance.utils.NoMoreClickListener;
import com.hd.attendance.utils.ToastUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 考勤的列表信息
 */
public class AttendListAdapter extends RecyclerView.Adapter<AttendListAdapter.ViewHolder> {

    private Context mContext;
    private List<AttendancemTable> mDataBeans;
    private boolean isShowEditor = true;//是否显示编辑 默认显示
    private boolean isAdddateTop = false;//是否添加时间到头部
    private boolean isShowName = true;

    public void setShowName(boolean showName) {
        isShowName = showName;
    }

    public void setShowEditor(boolean showEditor) {
        isShowEditor = showEditor;
    }

    public void isAddTime(boolean isAdddateTop) {
        this.isAdddateTop = isAdddateTop;
    }

    public AttendListAdapter(Context mContext, List<AttendancemTable> dataBeans) {
        this.mContext = mContext;
        this.mDataBeans = dataBeans;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.attendancem_list_item,
                parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        AttendancemTable atten = mDataBeans.get(position);

        if (isShowName) {
            holder.tvName.setText(atten.getUser_Name());
        } else {
            holder.tvName.setText(atten.getDate().substring(atten.getDate().length() - 2, atten.getDate().length())
                    + "日 \n 【" + atten.getWeek() + "】");
        }

        //1.设置头部信息 姓名  出勤情况:正常上班 打卡情况:
        StringBuffer sTop = new StringBuffer();

        sTop.append("出勤情况:");
        sTop.append(" (上午:");
        switch (atten.getMorningWorkType()) {
            case 0:
                sTop.append(" 正常上班 ");
                break;
            case 1:
                sTop.append(" 请 假 ");
                break;
            case 2:
                sTop.append(" 旷 工 ");
                break;
            case 3:
                sTop.append(" 加 班 ");
                break;
            case 4:
                sTop.append(" 出 差 ");
                break;
            default:
                break;
        }
        sTop.append(")");

        sTop.append(" (下午:");
        switch (atten.getAfternoonWorkType()) {
            case 0:
                sTop.append(" 正常上班 ");
                break;
            case 1:
                sTop.append(" 请 假 ");
                break;
            case 2:
                sTop.append(" 旷 工 ");
                break;
            case 3:
                sTop.append(" 加 班 ");
                break;
            case 4:
                sTop.append(" 出 差 ");
                break;
            default:
                break;
        }
        sTop.append(")");


        sTop.append("\n打卡情况: ");
        if (atten.getMorning_start_type() == 0 && atten.getMorning_end_type() == 0 && atten.getAfternoon_start_type() == 0 && atten.getAfternoon_end_type() == 0) {
            sTop.append("正 常");
        } else {
            sTop.append("异 常 ");
        }


        if (atten.isDeductions()) {
            sTop.append("\n扣款情况: 有 (金额:" + atten.getDeductions() + " ￥)");

            holder.tvIsDeductions.setText("扣款情况: 有");
            holder.tvDeductions.setText("扣款金额:" + atten.getDeductions() + " ￥");
            holder.tvDeductionsInfo.setText("扣款说明:" + atten.getDeductionsInfo());
        } else {
            sTop.append("\n扣款情况: 无");

            holder.tvIsDeductions.setText("扣款情况: 无");
            holder.tvDeductions.setText("扣款金额: " + atten.getDeductions());
            holder.tvDeductionsInfo.setText("扣款说明: " + atten.getDeductionsInfo());

        }

        holder.tvTopInfo.setText(sTop.toString());

        //2.设置上午上班时间
        holder.tvMorningStartTime.setText("打卡时间:  " + (atten.getMorning_start_time() == null ? "无" : atten.getMorning_start_time()));
        switch (atten.getMorning_start_type()) {
            case 0:
                holder.tvMorningStartType.setText("状        态:  正常打卡");
                holder.tvMorningStartType.setTextColor(mContext.getResources().getColor(R.color.green));

                break;
            case 1:
                holder.tvMorningStartType.setText("状        态:  迟到打卡");
                holder.tvMorningStartType.setTextColor(mContext.getResources().getColor(R.color.red));

                break;
            case 2:
                holder.tvMorningStartType.setText("状        态:  早退打卡");
                holder.tvMorningStartType.setTextColor(mContext.getResources().getColor(R.color.red));

                break;
            case 3:
                holder.tvMorningStartType.setText("状        态:  补 卡");
                holder.tvMorningStartType.setTextColor(mContext.getResources().getColor(R.color.yell));

                break;
            default:
                holder.tvMorningStartType.setText("状        态:  正常打卡");
                holder.tvMorningStartType.setTextColor(mContext.getResources().getColor(R.color.green));
                break;
        }
        holder.tvMorningStartNote.setText("备注内容:  " + atten.getMorning_start_note());

        //3.设置上午下班时间
        holder.tvMorningEndTime.setText("打卡时间:  " + (atten.getMorning_end_time() == null ? "无" : atten.getMorning_end_time()));
        switch (atten.getMorning_end_type()) {
            case 0:
                holder.tvMorningEndType.setText("状        态:  正常打卡");

                holder.tvMorningEndType.setTextColor(mContext.getResources().getColor(R.color.green));
                break;
            case 1:
                holder.tvMorningEndType.setText("状        态:  迟到打卡");
                holder.tvMorningEndType.setTextColor(mContext.getResources().getColor(R.color.red));

                break;
            case 2:
                holder.tvMorningEndType.setText("状        态:  早退打卡");
                holder.tvMorningEndType.setTextColor(mContext.getResources().getColor(R.color.red));

                break;
            case 3:
                holder.tvMorningEndType.setText("状        态:  补 卡");
                holder.tvMorningEndType.setTextColor(mContext.getResources().getColor(R.color.yell));
                break;
            default:
                holder.tvMorningEndType.setText("状        态:  正常打卡");
                holder.tvMorningEndType.setTextColor(mContext.getResources().getColor(R.color.green));

                break;
        }
        holder.tvMorningEndNote.setText("备注内容:  " + atten.getMorning_end_note());

        //4.设置下午上班时间
        holder.tvAfternoonStartTime.setText("打卡时间:  " + (atten.getAfternoon_start_time() == null ? "无" : atten.getAfternoon_start_time()));
        switch (atten.getAfternoon_start_type()) {
            case 0:
                holder.tvAfternoonStartType.setText("状        态:  正常打卡");
                holder.tvAfternoonStartType.setTextColor(mContext.getResources().getColor(R.color.green));

                break;
            case 1:
                holder.tvAfternoonStartType.setText("状        态:  迟到打卡");
                holder.tvAfternoonStartType.setTextColor(mContext.getResources().getColor(R.color.red));

                break;
            case 2:
                holder.tvAfternoonStartType.setText("状        态:  早退打卡");
                holder.tvAfternoonStartType.setTextColor(mContext.getResources().getColor(R.color.red));

                break;
            case 3:
                holder.tvAfternoonStartType.setText("状        态:  补 卡");
                holder.tvAfternoonStartType.setTextColor(mContext.getResources().getColor(R.color.yell));

                break;

            default:
                holder.tvAfternoonStartType.setText("状        态:  正常打卡");
                holder.tvAfternoonStartType.setTextColor(mContext.getResources().getColor(R.color.green));

                break;
        }
        holder.tvAfternoonStartNote.setText("备注内容:  " + atten.getAfternoon_start_note());

        //5.设置下午下班时间
        holder.tvAfternoonEndTime.setText("打卡时间:  " + (atten.getAfternoon_end_time() == null ? "无" : atten.getAfternoon_end_time()));
        switch (atten.getAfternoon_end_type()) {
            case 0:
                holder.tvAfternoonEndType.setText("状        态:  正常打卡");
                holder.tvAfternoonEndType.setTextColor(mContext.getResources().getColor(R.color.green));
                break;
            case 1:
                holder.tvAfternoonEndType.setText("状        态:  迟到打卡");
                holder.tvAfternoonEndType.setTextColor(mContext.getResources().getColor(R.color.red));
                break;
            case 2:
                holder.tvAfternoonEndType.setText("状        态:  早退打卡");
                holder.tvAfternoonEndType.setTextColor(mContext.getResources().getColor(R.color.red));
                break;
            case 3:
                holder.tvAfternoonEndType.setText("状        态:  补 卡");
                holder.tvAfternoonEndType.setTextColor(mContext.getResources().getColor(R.color.yell));
                break;
            default:
                holder.tvAfternoonEndType.setText("状        态:  正常打卡");
                holder.tvAfternoonEndType.setTextColor(mContext.getResources().getColor(R.color.green));

                break;
        }
        holder.tvAfternoonEndNote.setText("备注内容:  " + atten.getAfternoon_end_note());


        holder.reLayout.setOnClickListener(
                new NoMoreClickListener(2000) {
                    @Override
                    public void OnMoreClick(View view) {
                        super.OnMoreClick(view);
                        if (holder.Ll_data_layout.getVisibility() == View.GONE) {
                            holder.Ll_data_layout.setVisibility(View.VISIBLE);
                            holder.ivStatus.setImageResource(R.drawable.icon_xiala);
                        } else {
                            holder.Ll_data_layout.setVisibility(View.GONE);
                            holder.ivStatus.setImageResource(R.drawable.icon_shangla);
                        }
                    }
                    @Override
                    public void OnMoreErrorClick() {
                        ToastUtil.showToast("操作过快!");
                    }
                }
        );


        if (isShowEditor) {
            holder.tvEditorInfo.setVisibility(View.VISIBLE);
        } else {
            holder.tvEditorInfo.setVisibility(View.GONE);
        }

        holder.tvEditorInfo.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        holder.tvEditorInfo.setOnClickListener(v -> {
            Intent intent = new Intent(mContext, AttendancemEditorActivity.class);
            intent.putExtra("id", atten.getId() + "");
            mContext.startActivity(intent);
        });


    }


    @Override
    public int getItemCount() {
        return mDataBeans.isEmpty() ? 0 : mDataBeans.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.tv_top_info)
        TextView tvTopInfo;
        @BindView(R.id.iv_status)
        ImageView ivStatus;
        @BindView(R.id.re_layout)
        RelativeLayout reLayout;
        @BindView(R.id.tv_morning_start_time)
        TextView tvMorningStartTime;
        @BindView(R.id.tv_morning_start_type)
        TextView tvMorningStartType;
        @BindView(R.id.tv_morning_start_note)
        TextView tvMorningStartNote;
        @BindView(R.id.tv_morning_end_time)
        TextView tvMorningEndTime;
        @BindView(R.id.tv_morning_end_type)
        TextView tvMorningEndType;
        @BindView(R.id.tv_morning_end_note)
        TextView tvMorningEndNote;
        @BindView(R.id.tv_afternoon_start_time)
        TextView tvAfternoonStartTime;
        @BindView(R.id.tv_afternoon_start_type)
        TextView tvAfternoonStartType;
        @BindView(R.id.tv_afternoon_start_note)
        TextView tvAfternoonStartNote;
        @BindView(R.id.tv_afternoon_end_time)
        TextView tvAfternoonEndTime;
        @BindView(R.id.tv_afternoon_end_type)
        TextView tvAfternoonEndType;
        @BindView(R.id.tv_afternoon_end_note)
        TextView tvAfternoonEndNote;
        @BindView(R.id.tv_isDeductions)
        TextView tvIsDeductions;
        @BindView(R.id.tv_Deductions)
        TextView tvDeductions;
        @BindView(R.id.tv_deductionsInfo)
        TextView tvDeductionsInfo;
        @BindView(R.id.ll_data_layout)
        LinearLayout Ll_data_layout;
        @BindView(R.id.tv_editor_info)
        TextView tvEditorInfo;
        View itemView;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            this.itemView = itemView;
        }
    }


}
