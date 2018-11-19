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
import com.hd.attendance.activity.ManagementActivity;
import com.hd.attendance.activity.attendancem.adapter.AttenMonthAdapter;
import com.hd.attendance.activity.repast.adapter.RepastListAdapter;
import com.hd.attendance.activity.repast.dialog.SummRepastDialog;
import com.hd.attendance.base.BaseActivity;
import com.hd.attendance.db.AttendancemTable;
import com.hd.attendance.db.RepastTable;
import com.hd.attendance.utils.DateUtils;
import com.hd.attendance.utils.ToastUtil;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RepastMainActivity extends BaseActivity implements Toolbar.OnMenuItemClickListener {
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.recycler_year)
    RecyclerView recyclerYear;//年
    @BindView(R.id.recycler_month)
    RecyclerView recyclerMonth;//月
    @BindView(R.id.recycler_day)
    RecyclerView recyclerDay;//日
    @BindView(R.id.tv_nodata)
    TextView tvNodata;//没有数据
    @BindView(R.id.tv_info)
    TextView tvInfo;//详情
    @BindView(R.id.recycler_data)
    RecyclerView recyclerData;//数据

    //选择的年月日 默认当年当月当天
    private int selectYear = DateUtils.getThisYear();
    private int selectMonth = DateUtils.getThisMonth();
    private int selectDay = DateUtils.getThisDay();//是当月多少天就多少天

    //年份
    private AttenMonthAdapter YearAdapter;
    private List<String> YearList = new ArrayList<>();

    //月份
    private AttenMonthAdapter MonthAdapter;
    private List<String> MonthList = new ArrayList<>();

    //天数
    private AttenMonthAdapter DayAdapter;
    private List<String> DayList = new ArrayList<>();

    //数据列表
    private RepastListAdapter repastListAdapter;
    private List<RepastTable> repastList = new ArrayList<>();


    //弹框
    private FragmentManager fragmentManager;
    private SummRepastDialog summRepastDialog;

    @Override
    protected void initVariables() {

    }


    @Override
    protected void onResume() {
        super.onResume();
        initListData();//加载列表数据
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        setContentView(R.layout.activity_repast_main);
        ButterKnife.bind(this);
        setTitle("就餐管理");
        initToolbarNav();

        mToolbar.inflateMenu(R.menu.menu_attendancem_sum);
        mToolbar.setOnMenuItemClickListener(this);

        fragmentManager = getSupportFragmentManager();

        setSelectYear();//设置年
        setMonthRecyclerw();//设置月
        setDayRecyclerw();//设置天数
        setAttenRecyclerw();
    }

    @Override
    protected void initData() {
        initMonth(true);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.atten_add:
                startActivity(new Intent(this, RepastAddActivity.class));
                return true;
            case R.id.atten_sum:
                if (summRepastDialog == null) {
                    summRepastDialog = new SummRepastDialog();
                }
                showProgressDialog("正在汇总,请稍后");
                summRepastDialog.setRepastList(getMonthData());
                summRepastDialog.setMonthinfo(selectMonth + "月份 [共" + sumday + "天,星期日: " + sun + "天.]");
                summRepastDialog.show(fragmentManager, "summaryAttenDialog");
                closeProgressDialog();

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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
            iniDayData(selectYear, position + 1);
            initListData();
        });
        recyclerMonth.setAdapter(MonthAdapter);
    }

    /**
     * 设置日 天数
     */
    private void setDayRecyclerw() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerDay.setLayoutManager(layoutManager);
        DayAdapter = new AttenMonthAdapter(DayList);
        DayAdapter.setDefSelect(0);
        DayAdapter.setOnItemClickListener(position -> {
            DayAdapter.setDefSelect(position);
            selectDay = position + 1;
            initListData();

        });
        recyclerDay.setAdapter(DayAdapter);
    }

    /**
     * 设置年月日 的数据
     */
    private void initMonth(boolean isthisyear) {
        MonthList.clear();
        DayList.clear();
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

            //设置当前月份的当前天 选中当前天
            int day = DateUtils.getThisDay();//今天是当前月的第day 天
            selectDay = day;
            for (int i = 1; i <= day; i++) {
                String weeks = selectYear + "-" + (selectMonth > 9 ? selectMonth + "" : "0" + selectMonth) + "-" + (i > 9 ? i + "" : "0" + i);
                DayList.add(i + "日\n[" + DateUtils.DateToDayB(weeks) + "]");
            }

            DayAdapter.notifyDataSetChanged();
            DayAdapter.setDefSelect(day - 1);
            recyclerDay.scrollToPosition(day - 1);

        } else {

            //加载月份
            for (int i = 1; i <= 12; i++) {
                MonthList.add(i + "月");
            }
            selectMonth = 1;
            MonthAdapter.notifyDataSetChanged();
            MonthAdapter.setDefSelect(0);
            recyclerMonth.scrollToPosition(0);

            //加载天
            iniDayData(selectYear, selectMonth);
            //3.加载数据
        }
    }

    /**
     * 通过年月 加载天数横向滚动
     */
    public void iniDayData(int year, int month) {
        DayList.clear();

        //1.判断是否是今年的当前月
        if (year == DateUtils.getThisYear() && month == DateUtils.getThisMonth()) {
            //设置当前月份的当前天 选中当前天
            int day = DateUtils.getThisDay();//今天是当前月的第day 天
            selectDay = day;
            for (int i = 1; i <= day; i++) {
                String weeks = selectYear + "-" + (selectMonth > 9 ? selectMonth + "" : "0" + selectMonth) + "-" + (i > 9 ? i + "" : "0" + i);
                DayList.add(i + "日\n[" + DateUtils.DateToDayB(weeks) + "]");
            }

            DayAdapter.notifyDataSetChanged();
            DayAdapter.setDefSelect(day - 1);
            recyclerDay.scrollToPosition(day - 1);
            return;
        }

        //2.判断年的这个月有多少天
        int sumday = DateUtils.getMonthLastDay(year, month);


        String weeksstr = year + "-" + (month > 9 ? month + "" : "0" + month) + "-";
        for (int i = 1; i <= sumday; i++) {
            String str = weeksstr + (i > 9 ? i + "" : "0" + i);
            DayList.add(i + "日\n[" + DateUtils.DateToDayB(str) + "]");
        }

        selectDay = 1;
        DayAdapter.notifyDataSetChanged();
        DayAdapter.setDefSelect(0);
        recyclerDay.scrollToPosition(0);
    }


    /**
     * 设置数据的Re
     */
    private void setAttenRecyclerw() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerData.setLayoutManager(layoutManager);
        repastListAdapter = new RepastListAdapter(this, repastList);
        recyclerData.setAdapter(repastListAdapter);
    }

    /**
     * 加载数据库的考勤数据
     */
    private int noon_meal = 0;//中午报餐人数
    private int noon_eat = 0;
    private int evening_meal = 0;
    private int evening_eat = 0;

    private void initListData() {
        String selectDBdateStr = selectYear + "-" + (selectMonth > 9 ? selectMonth + "" : "0" + selectMonth) + "-" + (selectDay > 9 ? selectDay + "" : "0" + selectDay);

        repastList.clear();
        repastList.addAll(LitePal.where("Date = ?", selectDBdateStr).find(RepastTable.class));

        noon_meal = 0;
        noon_eat = 0;
        evening_meal = 0;
        evening_eat = 0;

        if (repastList.size() > 0) {
            tvNodata.setVisibility(View.GONE);
            for (RepastTable e : repastList) {
                if (e.isAfternoon_Report()) {
                    noon_meal++;
                }
                if (e.isAfternoon_Eat()) {
                    noon_eat++;
                }
                if (e.isEvening_Report()) {
                    evening_meal++;
                }
                if (e.isEvening_Eat()) {
                    evening_eat++;
                }

                tvInfo.setText("【中餐报餐人数:" + noon_meal + "人,中餐就餐人数:" + noon_eat + "人】\n【晚餐报餐人数:" + evening_meal + "人,晚餐就餐人数:" + evening_eat + "人】");
            }
        } else {
            tvInfo.setText("");
            tvNodata.setVisibility(View.VISIBLE);
        }
        repastListAdapter.notifyDataSetChanged();

        ShowMonthInfo();
    }


    /**
     * 查找
     *
     * @return
     */
    private List<RepastTable> getMonthData() {
        List<RepastTable> MonthList = new ArrayList<>();
        String sdate = selectYear + "-" + (selectMonth > 9 ? selectMonth + "" : "0" + selectMonth);
        MonthList.clear();
        MonthList.addAll(LitePal.where("Date >= ? and Date <= ?",
                sdate + "-01", sdate + "-31")
                .order("Date")
                .find(RepastTable.class));
        return MonthList;
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
