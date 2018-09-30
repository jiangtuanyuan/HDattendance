package com.hd.attendance.activity.fingerprint.ui;


import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.hd.attendance.R;
import com.hd.attendance.activity.fingerprint.adapter.FingerGroupAdapter;
import com.hd.attendance.activity.fingerprint.adapter.FingerItemAdapter;
import com.hd.attendance.activity.fingerprint.bean.UserFingerBean;
import com.hd.attendance.base.BaseActivity;
import com.hd.attendance.utils.ToastUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 指纹管理
 */
public class FingerMainActivity extends BaseActivity implements FingerGroupAdapter.OnItemClickListener {
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.recycler_view)
    RecyclerView mRecycler;
    @BindView(R.id.refresh_layout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.RecyclerView_Group)
    RecyclerView RecyclerViewGroup;
    //指纹列表
    private FingerItemAdapter FingerAdapter;
    private List<UserFingerBean> FingerList = new ArrayList<>();
    //小组横向滚动
    private FingerGroupAdapter GroupAdapter;
    private List<String> GropList = new ArrayList<>();

    @Override
    protected void initVariables() {

    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        setContentView(R.layout.activity_finger_main);
        ButterKnife.bind(this);
        initToolbarNav();
        setTitle("指纹管理");
        setSupportActionBar(mToolbar);
        setGroupRecyclerw();
        setFingerRecyclerw();
    }

    /**
     * 设置小组
     */
    private void setGroupRecyclerw() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        RecyclerViewGroup.setLayoutManager(layoutManager);
        GroupAdapter = new FingerGroupAdapter(GropList);
        GroupAdapter.setDefSelect(0);
        GroupAdapter.setOnItemClickListener(this);
        RecyclerViewGroup.setAdapter(GroupAdapter);
    }

    /**
     * 设置指纹列表
     */
    private void setFingerRecyclerw() {
        refreshLayout.setEnableLoadMore(false);
        mRecycler.setLayoutManager(new LinearLayoutManager(this));
        FingerAdapter = new FingerItemAdapter(this, FingerList);
        mRecycler.setAdapter(FingerAdapter);
        mRecycler.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
    }


    @Override
    protected void initData() {
        for (int i = 0; i < 10; i++) {
            UserFingerBean bean = new UserFingerBean();
            bean.setFingerID(i);
            bean.setUserId(i);
            bean.setFingerState("启用");
            bean.setGroupID(i);
            bean.setGroupName("第" + i + "小组");
            bean.setUserNmae("员工" + i);
            FingerList.add(bean);
        }
        FingerAdapter.notifyDataSetChanged();

        GropList.add("# 全 部");
        GropList.add("#第一小组");
        GropList.add("#第二小组");
        GropList.add("#第三小组");
        GropList.add("#第四小组");
        GropList.add("#第五小组");
        GroupAdapter.notifyDataSetChanged();
    }

    /**
     * 横向滚动回调事件
     * @param view
     * @param position
     */
    @Override
    public void onItemClickListener(View view, int position) {
        ToastUtil.showToast(GropList.get(position));
        GroupAdapter.setDefSelect(position);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.add:
                ToastUtil.showToast("添加指纹");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
