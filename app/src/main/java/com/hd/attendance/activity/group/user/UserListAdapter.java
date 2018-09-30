package com.hd.attendance.activity.group.user;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.hd.attendance.R;
import com.hd.attendance.utils.ToastUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by 蒋 on 2018/7/9.
 * 用户列表
 */

public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.ViewHolder> {

    private List<UserBean> userList;
    private Context mContext;
    private LayoutInflater mInflater;
    private boolean single = false;//是否只能选择一个 默认否
    private int ischeckUser = 0;//没用选择用 0 或者 1

    public void setIscheckUser(int ischeckUser) {
        this.ischeckUser = ischeckUser;
    }

    public UserListAdapter(Context mContext, List<UserBean> userList, boolean single) {
        this.userList = userList;
        this.mContext = mContext;
        this.mInflater = LayoutInflater.from(mContext);
        this.single = single;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View contentView = LayoutInflater.from(mContext).inflate(R.layout.user_list_item, parent, false);
        ViewHolder contentViewHolder = new ViewHolder(contentView);
        return contentViewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
        final UserBean userBean = userList.get(position);
        viewHolder.tvLeftName.setText(userBean.getUser_name().substring(userBean.getUser_name().length() - 1, userBean.getUser_name().length()));

        if (userBean.getUser_sex().equals("女")) {
            viewHolder.tvLeftName.setBackgroundResource(R.drawable.shape_rectangle_pink_bg);
        }

        viewHolder.tvUserName.setText(userBean.getUser_name());
        viewHolder.tvUserJobs.setText(userBean.getJobs());
        viewHolder.tvUserGroupName.setText(userBean.getGroup_name());
        viewHolder.cbCheckbox.setChecked(userBean.isChecked());
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (!userBean.getGroup_name().equals("无")) {
                        ToastUtil.showToast("该成员已属于 " + userBean.getGroup_name() + " 小组,无法选择!");
                        return;
                    }

                    if (userBean.isChecked()) {

                        ischeckUser = 0;
                        viewHolder.cbCheckbox.setChecked(false);
                        userBean.setChecked(false);

                    } else {
                        //这里需要深深的思考一下
                        if (single) {
                            if (ischeckUser == 1) {
                                ToastUtil.showToast("只能选择一个用户!");
                            } else {
                                ischeckUser = 1;
                                viewHolder.cbCheckbox.setChecked(true);
                                userBean.setChecked(true);
                            }
                        } else {
                            viewHolder.cbCheckbox.setChecked(true);
                            userBean.setChecked(true);
                        }
                    }
                    mItemClickListener.onItemClick(position, userBean);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    protected class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_left_name)
        TextView tvLeftName;
        @BindView(R.id.tv_user_name)
        TextView tvUserName;
        @BindView(R.id.tv_user_jobs)
        TextView tvUserJobs;
        @BindView(R.id.tv_user_group_name)
        TextView tvUserGroupName;
        @BindView(R.id.cb_checkbox)
        CheckBox cbCheckbox;
        View itemView;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            itemView = view;
        }
    }

    private ItemClickListener mItemClickListener;

    public void setmItemClickListener(ItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    public interface ItemClickListener {
        void onItemClick(int position, UserBean item);
    }
}
