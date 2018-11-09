package com.hd.attendance.activity.employees.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.hd.attendance.R;
import com.hd.attendance.activity.employees.adapter.EmployeesItemAdapter;
import com.hd.attendance.activity.fingerprint.adapter.FingerItemAdapter;
import com.hd.attendance.base.BaseActivity;
import com.hd.attendance.db.EmployeesTable;
import com.hd.attendance.utils.ToastUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EmployeesActivity extends BaseActivity {
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.recycler_view)
    RecyclerView mRecycler;
    @BindView(R.id.refresh_layout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.tv_nodata)
    TextView tvNodata;//没有数据

    private EmployeesItemAdapter adapter;
    private List<EmployeesTable> mList = new ArrayList<>();

    @Override
    protected void initVariables() {

    }

    @Override
    protected void onResume() {
        super.onResume();

        mList.clear();
        mList.addAll(LitePal.order("id desc").find(EmployeesTable.class));
        adapter.notifyDataSetChanged();

        if (mList.size() == 0) {
            tvNodata.setVisibility(View.VISIBLE);
        } else {
            tvNodata.setVisibility(View.GONE);
        }

    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        setContentView(R.layout.activity_employees);
        ButterKnife.bind(this);
        initToolbarNav();
        setTitle("员工管理");
        setSupportActionBar(mToolbar);
        refreshLayout.setEnableLoadMore(false);
        refreshLayout.setEnableRefresh(false);
        setEmployeesRecyclerw();
    }

    @Override
    protected void initData() {
        mList.clear();
        mList.addAll(LitePal.order("id desc").find(EmployeesTable.class));
        adapter.notifyDataSetChanged();
    }

    /**
     * 设置指纹列表
     */
    private void setEmployeesRecyclerw() {
        mRecycler.setLayoutManager(new LinearLayoutManager(this));
        adapter = new EmployeesItemAdapter(this, mList);
        mRecycler.setAdapter(adapter);
        mRecycler.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_emp_add, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.add:
                startActivity(new Intent(this, EmployeesAddActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
