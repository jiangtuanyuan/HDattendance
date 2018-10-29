package com.hd.attendance.activity.fingerprint.ui;


import android.content.Intent;
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
import com.hd.attendance.activity.health.ui.HealthMainActivity;
import com.hd.attendance.base.BaseActivity;
import com.hd.attendance.db.FingerInfoTable;
import com.hd.attendance.db.GroupTable;
import com.hd.attendance.utils.ToastUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 指纹管理
 */
public class FingerMainActivity extends BaseActivity{
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.recycler_view)
    RecyclerView mRecycler;
    @BindView(R.id.refresh_layout)
    SmartRefreshLayout refreshLayout;
    //指纹列表
    private FingerItemAdapter FingerAdapter;
    private List<FingerInfoTable> FingerList = new ArrayList<>();

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
        setFingerRecyclerw();
    }

    @Override
    protected void onResume() {
        super.onResume();
        FingerList.clear();
        FingerList.addAll(LitePal.findAll(FingerInfoTable.class));
        FingerAdapter.notifyDataSetChanged();
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
        FingerList.clear();
        FingerList.addAll(LitePal.findAll(FingerInfoTable.class));
        FingerAdapter.notifyDataSetChanged();

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
                startActivity(new Intent(this, FingerAddActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
