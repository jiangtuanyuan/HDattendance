package com.hd.attendance.activity.attendancem.ui;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.hd.attendance.R;
import com.hd.attendance.activity.attendancem.adapter.AttenMonthAdapter;
import com.hd.attendance.activity.attendancem.adapter.AttendListAdapter;
import com.hd.attendance.activity.attendancem.dialog.BarChartDialog;
import com.hd.attendance.activity.attendancem.dialog.CanlendarDialog;
import com.hd.attendance.activity.attendancem.dialog.PieChartDialog;
import com.hd.attendance.activity.attendancem.dialog.SummaryAttenDialog;
import com.hd.attendance.activity.group.user.UserBean;
import com.hd.attendance.activity.group.user.UserChooseActivity;
import com.hd.attendance.base.BaseActivity;
import com.hd.attendance.db.AttendancemTable;
import com.hd.attendance.utils.DateUtils;
import com.hd.attendance.utils.ToastUtil;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AttendancemSummaryActivity extends BaseActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_choose_user)
    TextView tvChooseUser;
    @BindView(R.id.tv_month_info)
    TextView tvMonthInfo;
    @BindView(R.id.recycler_year)
    RecyclerView recyclerYear;
    @BindView(R.id.recycler_month)
    RecyclerView recyclerMonth;
    @BindView(R.id.recycler_data)
    RecyclerView recyclerData;
    @BindView(R.id.tv_nodata)
    TextView tvNodata;//没有数据

    @BindView(R.id.tv_show_calendar)
    TextView tvCaendar;//日历
    @BindView(R.id.tv_show_chart)
    TextView tvChart;//饼状图
    @BindView(R.id.tv_show_bar)
    TextView tvBar;//饼状图
    @BindView(R.id.tv_show_sum)
    TextView tvSum;//汇总


    //选择的年月日 默认当年当月当天
    private int selectYear = DateUtils.getThisYear();
    private int selectMonth = DateUtils.getThisMonth();

    //年份
    private AttenMonthAdapter YearAdapter;
    private List<String> YearList = new ArrayList<>();

    //月份
    private AttenMonthAdapter MonthAdapter;
    private List<String> MonthList = new ArrayList<>();

    //数据列表
    private AttendListAdapter attendListAdapter;
    private List<AttendancemTable> AttenList = new ArrayList<>();

    //弹框
    private FragmentManager fragmentManager;
    private SummaryAttenDialog summaryAttenDialog;

    //日历的数据dialog
    private CanlendarDialog mCanlendarDialog;

    //饼图
    private PieChartDialog mPieChartDialog;

    //柱状图
    private BarChartDialog mBarChartDialog;


    //主界面跳转过来的用户查询 不能显示编辑
    private String UserID = "";
    private String UserNmae = "";


    @Override
    protected void onResume() {
        super.onResume();
        isDBUserInfo();
    }


    @Override
    protected void initVariables() {
        if (getIntent() != null) {
            UserID = getIntent().getStringExtra("id");
            UserNmae = getIntent().getStringExtra("name");
        }
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        setContentView(R.layout.activity_attendancem_summary);
        ButterKnife.bind(this);
        initToolbarNav();
        setTitle("考勤数据汇总");
        fragmentManager = getSupportFragmentManager();

        if (!TextUtils.isEmpty(UserID)
                && !TextUtils.isEmpty(UserNmae)) {
            UserBean userBean = new UserBean();
            userBean.setUser_id(Integer.parseInt(UserID));
            userBean.setUser_name(UserNmae);
            tvChooseUser.setText(UserNmae);
            mCheckListS.clear();
            mCheckListS.add(userBean);
        }

        setSelectYear();//设置年
        setMonthRecyclerw();//设置月
        setAttenRecyclerw();

        tvChooseUser.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        tvCaendar.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        tvChart.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        tvSum.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        tvBar.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
    }

    @Override
    protected void initData() {
        initMonth(true);
    }


    /**
     * 设置数据
     */
    private void setAttenRecyclerw() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerData.setLayoutManager(layoutManager);
        attendListAdapter = new AttendListAdapter(this, AttenList);
        attendListAdapter.isAddTime(true);
        attendListAdapter.setShowName(false);
        recyclerData.setAdapter(attendListAdapter);

        if (!TextUtils.isEmpty(UserID) && !TextUtils.isEmpty(UserNmae)) {
            attendListAdapter.setShowEditor(false);
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
            isDBUserInfo();
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
            isDBUserInfo();
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


    @OnClick({R.id.tv_choose_user, R.id.tv_show_calendar, R.id.tv_show_chart, R.id.tv_show_bar, R.id.tv_show_sum})
    public void onViewClicked(View v) {
        switch (v.getId()) {
            case R.id.tv_choose_user:
                if (!TextUtils.isEmpty(UserID)
                        && !TextUtils.isEmpty(UserNmae)) {
                    ToastUtil.showToast("只能查看自己的数据!");
                } else {
                    Intent intent = new Intent(this, UserChooseActivity.class);
                    intent.putExtra("mChooseNums", "single");
                    intent.putExtra("isalls", "1");
                    startActivityForResult(intent, 100);
                }
                break;
            case R.id.tv_show_calendar:
                if (mCheckListS.size() == 0) {
                    ToastUtil.showToast("请选择一个员工!");
                    return;
                }
                if (AttenList.size() == 0) {
                    ToastUtil.showToast("无数据!");
                    return;
                }

                if (mCanlendarDialog == null) {
                    mCanlendarDialog = new CanlendarDialog();
                }
                mCanlendarDialog.setAttenList(AttenList, selectYear, selectMonth);
                mCanlendarDialog.show(fragmentManager, "mCanlendarDialog");

                break;
            case R.id.tv_show_chart:
                if (mCheckListS.size() == 0) {
                    ToastUtil.showToast("请选择一个员工!");
                    return;
                }
                if (AttenList.size() == 0) {
                    ToastUtil.showToast("无数据!");
                    return;
                }

                if (mPieChartDialog == null) {
                    mPieChartDialog = new PieChartDialog();
                }
                mPieChartDialog.setAttenList(AttenList, selectYear, selectMonth);
                mPieChartDialog.show(fragmentManager, "mPieChartDialog");

                break;
            case R.id.tv_show_bar:
                if (mCheckListS.size() == 0) {
                    ToastUtil.showToast("请选择一个员工!");
                    return;
                }
                if (AttenList.size() == 0) {
                    ToastUtil.showToast("无数据!");
                    return;
                }
                if (mBarChartDialog == null) {
                    mBarChartDialog = new BarChartDialog();
                }
                mBarChartDialog.setAttenList(AttenList, selectYear, selectMonth);
                mBarChartDialog.show(fragmentManager, "mBarChartDialog");
                break;
            case R.id.tv_show_sum:
                if (mCheckListS.size() == 0) {
                    ToastUtil.showToast("请选择一个员工!");
                    return;
                }

                if (AttenList.size() == 0) {
                    ToastUtil.showToast("无数据!");
                    return;
                }

                if (summaryAttenDialog == null) {
                    summaryAttenDialog = new SummaryAttenDialog();
                }
                summaryAttenDialog.setName(mCheckListS.get(0).getUser_name());
                summaryAttenDialog.setMonthinfo(selectMonth + "月份 [共" + sumday + "天,星期日: " + sun + "天.]");
                summaryAttenDialog.setAttenList(AttenList);
                summaryAttenDialog.show(fragmentManager, "summaryAttenDialog");

                break;
            default:
                break;

        }
    }

    private List<UserBean> mCheckListS = new ArrayList<>();

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //选择的用户回调 单
        if (requestCode == 100 && resultCode == RESULT_OK) {
            if (data != null) {
                Bundle bundle = data.getExtras();
                mCheckListS.clear();
                mCheckListS.addAll((List<UserBean>) bundle.getSerializable("mCheckList"));
                if (mCheckListS.size() > 0) {
                    tvChooseUser.setText(mCheckListS.get(0).getUser_name());
                    isDBUserInfo();
                }
            }
        }
    }


    /**
     * 加载数据库的数据到列表
     */
    private void isDBUserInfo() {
        if (mCheckListS.size() == 0) {
            ToastUtil.showToast("请选择一个员工!");
            return;
        }

        AttenList.clear();
        String sdate = selectYear + "-" + (selectMonth > 9 ? selectMonth + "" : "0" + selectMonth);
        AttenList.addAll(LitePal.where("User_ID = ? and Date >= ? and Date <= ?",
                mCheckListS.get(0).getUser_id() + "", sdate + "-01", sdate + "-31")
                .order("Date desc")
                .find(AttendancemTable.class));

        if (AttenList.size() == 0) {
            tvNodata.setVisibility(View.VISIBLE);

            tvCaendar.setTextColor(this.getResources().getColor(R.color.gray));
            tvChart.setTextColor(this.getResources().getColor(R.color.gray));
            tvSum.setTextColor(this.getResources().getColor(R.color.gray));
            tvBar.setTextColor(this.getResources().getColor(R.color.gray));

        } else {
            tvNodata.setVisibility(View.GONE);

            tvCaendar.setTextColor(this.getResources().getColor(R.color.blue));
            tvChart.setTextColor(this.getResources().getColor(R.color.blue));
            tvSum.setTextColor(this.getResources().getColor(R.color.blue));
            tvBar.setTextColor(this.getResources().getColor(R.color.blue));

        }

        attendListAdapter.notifyDataSetChanged();

        ShowMonthInfo();
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
        tvMonthInfo.setText("[ 共:" + AttenList.size() + "条记录! ]");
    }
}
