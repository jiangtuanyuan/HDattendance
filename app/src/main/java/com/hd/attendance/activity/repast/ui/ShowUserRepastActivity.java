package com.hd.attendance.activity.repast.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.hd.attendance.R;
import com.hd.attendance.activity.attendancem.adapter.AttenMonthAdapter;
import com.hd.attendance.activity.repast.adapter.RepastListAdapter;
import com.hd.attendance.activity.repast.dialog.SummRepastDialog;
import com.hd.attendance.base.BaseActivity;
import com.hd.attendance.db.RepastTable;
import com.hd.attendance.utils.DateUtils;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ShowUserRepastActivity extends BaseActivity implements Toolbar.OnMenuItemClickListener {
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.recycler_year)
    RecyclerView recyclerYear;
    @BindView(R.id.recycler_month)
    RecyclerView recyclerMonth;
    @BindView(R.id.tv_nodata)
    TextView tvNodata;
    @BindView(R.id.recycler_data)
    RecyclerView recyclerData;

    //选择的年月日 默认当年当月
    private int selectYear = DateUtils.getThisYear();
    private int selectMonth = DateUtils.getThisMonth();


    //年份
    private AttenMonthAdapter YearAdapter;
    private List<String> YearList = new ArrayList<>();

    //月份
    private AttenMonthAdapter MonthAdapter;
    private List<String> MonthList = new ArrayList<>();

    //数据列表
    private RepastListAdapter repastListAdapter;
    private List<RepastTable> repastList = new ArrayList<>();

    //弹框
    private FragmentManager fragmentManager;
    private SummRepastDialog summRepastDialog;

    private String UserID = "";
    private String UserName = "";

    @Override
    protected void onResume() {
        super.onResume();
        initListData();//加载列表数据
    }

    @Override
    protected void initVariables() {
        if (getIntent() != null) {
            UserID = getIntent().getStringExtra("id");
            UserName = getIntent().getStringExtra("name");
        }

    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        setContentView(R.layout.activity_show_user_repast);
        ButterKnife.bind(this);
        setTitle("【" + UserName + "】的就餐信息");
        initToolbarNav();

        mToolbar.inflateMenu(R.menu.menu_repast_sum);
        mToolbar.setOnMenuItemClickListener(this);
        fragmentManager = getSupportFragmentManager();

        setSelectYear();//设置年
        setMonthRecyclerw();//设置月
        setAttenRecyclerw();
    }

    @Override
    protected void initData() {
        initMonth(true);
    }

    /**
     * 设置年份
     */
    private void setSelectYear() {
        //加载4年的
        int year = DateUtils.getThisYear();
        YearList.add((year - 2) + "年");
        YearList.add((year - 1) + "年");
        YearList.add(year + "年");

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerYear.setLayoutManager(layoutManager);
        YearAdapter = new AttenMonthAdapter(YearList);
        YearAdapter.setDefSelect(YearList.size() - 1);
        YearAdapter.setOnItemClickListener(position -> {
            YearAdapter.setDefSelect(position);

            String years = YearList.get(position).replace("年", "");
            selectYear = Integer.parseInt(years);
            if (selectYear == DateUtils.getThisYear()) {
                initMonth(true);
            } else {
                initMonth(false);
            }
            initListData();
        });
        recyclerYear.setAdapter(YearAdapter);
    }

    /**
     * 设置月份
     */
    private void setMonthRecyclerw() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerMonth.setLayoutManager(layoutManager);
        MonthAdapter = new AttenMonthAdapter(MonthList);
        MonthAdapter.setDefSelect(0);
        MonthAdapter.setOnItemClickListener(position -> {
            MonthAdapter.setDefSelect(position);
            selectMonth = position + 1;
            initListData();
        });
        recyclerMonth.setAdapter(MonthAdapter);
    }

    /**
     * 设置年月日 的数据
     */
    private void initMonth(boolean isthisyear) {
        MonthList.clear();
        if (isthisyear) {
            //设置默认的月份数据 选中当前月份
            int x = DateUtils.getThisMonth();
            selectMonth = x;
            for (int i = 1; i <= x; i++) {
                MonthList.add(i + "月");
            }

            MonthAdapter.notifyDataSetChanged();
            MonthAdapter.setDefSelect(x - 1);
            recyclerMonth.scrollToPosition(x - 1);
        } else {

            //加载月份
            for (int i = 1; i <= 12; i++) {
                MonthList.add(i + "月");
            }
            selectMonth = 1;
            MonthAdapter.notifyDataSetChanged();
            MonthAdapter.setDefSelect(0);
            recyclerMonth.scrollToPosition(0);
        }
    }

    /**
     * 设置数据的Re
     */
    private void setAttenRecyclerw() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerData.setLayoutManager(layoutManager);
        repastListAdapter = new RepastListAdapter(this, repastList);
        repastListAdapter.setShowDate(true);
        recyclerData.setAdapter(repastListAdapter);

    }

    private void initListData() {
        repastList.clear();
        String sdate = selectYear + "-" + (selectMonth > 9 ? selectMonth + "" : "0" + selectMonth);
        repastList.addAll(LitePal.where("User_ID = ? and Date >= ? and Date <= ?",
                UserID, sdate + "-01", sdate + "-31")
                .order("Date")
                .find(RepastTable.class));

        if (repastList.size() > 0) {
            tvNodata.setVisibility(View.GONE);
        } else {
            tvNodata.setVisibility(View.VISIBLE);
        }
        repastListAdapter.notifyDataSetChanged();

        ShowMonthInfo();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.atten_sum:
                if (summRepastDialog == null) {
                    summRepastDialog = new SummRepastDialog();
                }
                showProgressDialog("正在汇总,请稍后");
                summRepastDialog.setRepastList(repastList);
                summRepastDialog.setMonthinfo(selectMonth + "月份 [共" + sumday + "天,星期日: " + sun + "天!]");
                summRepastDialog.show(fragmentManager, "summaryAttenDialog");
                closeProgressDialog();

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * 显示月份信息
     */
    private int sun = 0;
    private int sumday = 0;

    private void ShowMonthInfo() {
        //选中的年月有多少天
        sun = 0;
        sumday = DateUtils.getMonthLastDay(selectYear, selectMonth);
        String weeksstr = selectYear + "-" + (selectMonth > 9 ? selectMonth + "" : "0" + selectMonth) + "-";
        for (int i = 1; i <= sumday; i++) {
            String str = weeksstr + (i > 9 ? i + "" : "0" + i);
            if (DateUtils.DateToDayB(str).equals("星期日"))
                sun++;
        }
    }
}

