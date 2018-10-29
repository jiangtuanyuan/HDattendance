package com.hd.attendance.activity.logs;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hd.attendance.R;
import com.hd.attendance.db.SystemLogTable;
import com.hd.attendance.utils.DateUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 固定标准地调查列表适配器
 * jiang
 */
public class LogsListAdapter extends RecyclerView.Adapter<LogsListAdapter.ViewHolder> {
    private Context mContext;
    private List<SystemLogTable> mDataBeans;

    public LogsListAdapter(Context mContext, List<SystemLogTable> dataBeans) {
        this.mContext = mContext;
        this.mDataBeans = dataBeans;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.logs_list_item,
                parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        SystemLogTable log = mDataBeans.get(position);
        holder.tvJl.setText(DateUtils.getMD(log.getDate()) + "  [" + DateUtils.DateToDayB(log.getDate()) + "]");
        holder.tvData.setText(log.getInfo());

        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.tvData.getVisibility() == View.GONE) {
                    holder.tvData.setVisibility(View.VISIBLE);
                    holder.ivStatus.setImageResource(R.drawable.icon_xiala);

                } else {
                    holder.tvData.setVisibility(View.GONE);
                    holder.ivStatus.setImageResource(R.drawable.icon_shangla);
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return mDataBeans.isEmpty() ? 0 : mDataBeans.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_jl)
        TextView tvJl;
        @BindView(R.id.tv_data)
        TextView tvData;
        @BindView(R.id.iv_status)
        ImageView ivStatus;
        @BindView(R.id.re_layout)
        RelativeLayout relativeLayout;
        View itemView;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            this.itemView = itemView;
        }
    }


}
