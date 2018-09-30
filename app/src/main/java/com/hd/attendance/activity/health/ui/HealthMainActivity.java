package com.hd.attendance.activity.health.ui;


import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.hd.attendance.R;
import com.hd.attendance.activity.fingerprint.adapter.FingerGroupAdapter;
import com.hd.attendance.base.BaseActivity;
import com.hd.attendance.db.EmployeesTable;
import com.hd.attendance.utils.DateUtils;
import com.hd.attendance.utils.SPUtils;
import com.hd.attendance.utils.ToastUtil;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class HealthMainActivity extends BaseActivity implements FingerGroupAdapter.OnItemClickListener {

    @BindView(R.id.recycler_week)
    RecyclerView recyclerWeek;
    @BindView(R.id.et_content)
    EditText etContent;
    @BindView(R.id.bt_save)
    Button btSave;

    private int position = 0;
    //日期横向滚动
    private FingerGroupAdapter WeekAdapter;
    private List<String> WeekList = new ArrayList<>();

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    @Override
    protected void initVariables() {
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        setContentView(R.layout.activity_health_main);
        ButterKnife.bind(this);
        initToolbarNav();
        setTitle("卫生管理");
        setGroupRecyclerw();

    }

    @Override
    protected void initData() {
        WeekList.add("星期一");//0
        WeekList.add("星期二");//1
        WeekList.add("星期三");///
        WeekList.add("星期四");//3
        WeekList.add("星期五");//4
        WeekList.add("星期六");//5
        WeekList.add("星期日");//6

        WeekAdapter.notifyDataSetChanged();

        try {
            int week = DateUtils.getWeek();
            position = week - 1;
            WeekAdapter.setDefSelect(position);
            etContent.setText(SPUtils.getInstance().getString(week + ""));
        } catch (Exception e) {
            e.printStackTrace();
            etContent.setText("");
            etContent.setHint("未设置卫生安排!");
        }
    }

    /**
     * 设置小组
     */
    private void setGroupRecyclerw() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerWeek.setLayoutManager(layoutManager);
        WeekAdapter = new FingerGroupAdapter(WeekList);
        WeekAdapter.setDefSelect(0);
        WeekAdapter.setOnItemClickListener(this);
        recyclerWeek.setAdapter(WeekAdapter);
    }

    @OnClick(R.id.bt_save)
    public void onViewClicked() {
        try {
            SPUtils.getInstance().putString((position + 1) + "", etContent.getText().toString());
            ToastUtil.showToast("保存成功！");
        } catch (Exception e) {
            e.printStackTrace();
            ToastUtil.showToast("保存失败,清重试！");
        }
    }

    @Override
    public void onItemClickListener(View view, int position) {
        WeekAdapter.setDefSelect(position);
        this.position = position;
        try {
            etContent.setText(SPUtils.getInstance().getString((position + 1) + ""));
        } catch (Exception e) {
            e.printStackTrace();
            etContent.setText("");
            etContent.setHint("未设置卫生安排!");
        }
    }
}
