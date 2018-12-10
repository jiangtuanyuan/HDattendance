package com.hd.attendance.activity.attendancem.ui;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hd.attendance.R;
import com.hd.attendance.activity.attendancem.AttendType;
import com.hd.attendance.activity.attendancem.WorkType;
import com.hd.attendance.activity.attendancem.adapter.AttenMonthAdapter;
import com.hd.attendance.activity.attendancem.adapter.AttendListAdapter;
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


/**
 * 考勤管理主界面
 */
public class AttendancemMainActivity extends BaseActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.recycler_year)
    RecyclerView recyclerYear;//年
    @BindView(R.id.recycler_month)
    RecyclerView recyclerMonth;//月
    @BindView(R.id.recycler_day)
    RecyclerView recyclerDay;//日
    @BindView(R.id.tv_nodata)
    TextView tvNodata;//没有数据
    @BindView(R.id.recycler_data)
    RecyclerView recyclerData;//数据
    @BindView(R.id.sp_stauts_type)
    TextView spStautsType;//状态
    @BindView(R.id.tv_list_size)
    TextView tvListSize;//共N条数据
    @BindView(R.id.ll_stauts_layout)
    LinearLayout llStautsLayout;//状态布局

    @BindView(R.id.tv_atten_type)
    TextView tvAttenType;//类型
    @BindView(R.id.tv_atten_size)
    TextView tvAttenSize;//出勤总数
    @BindView(R.id.ll_atten_layout)
    LinearLayout llAttenLayout;//布局

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
    private AttendListAdapter attendListAdapter;
    private List<AttendancemTable> AttenList = new ArrayList<>();

    private List<AttendancemTable> SumAttenList = new ArrayList<>();//保存数库里面的总数据

    @Override
    protected void onResume() {
        super.onResume();
        initListData();//加载列表数据
        ScreebListData();
    }

    @Override
    protected void initVariables() {

    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        setContentView(R.layout.activity_attendancem_main);
        ButterKnife.bind(this);
        initToolbarNav();
        setTitle("考勤管理");
        setSupportActionBar(toolbar);

        setSelectYear();//设置年
        setMonthRecyclerw();//设置月
        setDayRecyclerw();//设置天数
        setAttenRecyclerw();

        spStautsType.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        tvAttenType.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);

    }

    @Override
    protected void initData() {
        initMonth(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_attendancem_sum, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.atten_add:
                startActivity(new Intent(this, AttendancemAddActivity.class));
                return true;
            case R.id.atten_sum:
                startActivity(new Intent(this, AttendancemSummaryActivity.class));
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
     * 设置数据的Re
     */
    private void setAttenRecyclerw() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerData.setLayoutManager(layoutManager);
        attendListAdapter = new AttendListAdapter(this, AttenList);
        recyclerData.setAdapter(attendListAdapter);
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
     * 按照出勤情况赛选数据
     */
    private String[] AttenAray = new String[]{"全部", "正常上班", "请假", "旷工", "加班", "出差"};
    private AlertDialog.Builder AttenDialog;
    private int selectAtten = 0;

    private void showAttenDialog() {
        if (AttenDialog == null) {
            AttenDialog = new AlertDialog.Builder(this)
                    .setTitle("选择状态类型筛选")
                    .setItems(AttenAray, (dialog, which) -> {
                        tvAttenType.setText(AttenAray[which]);
                        selectAtten = which;

                        QuereyAttenListData();
                        dialog.dismiss();
                    });
        }
        AttenDialog.show();
    }

    /**
     * 根据出勤情况 筛选数据
     */
    private void QuereyAttenListData() {
        switch (selectAtten) {
            case 0://全部
                AttenList.clear();
                AttenList.addAll(SumAttenList);
                break;
            case 1://正常上班
                AttenList.clear();
                for (AttendancemTable a : SumAttenList) {
                    if (a.getMorningWorkType() == WorkType.NORMAL_CODE && a.getAfternoonWorkType() == WorkType.NORMAL_CODE) {
                        AttenList.add(a);
                    }
                }
                break;
            case 2://请假
                AttenList.clear();
                for (AttendancemTable a : SumAttenList) {
                    if (a.getMorningWorkType() == WorkType.LEAVE_CODE || a.getAfternoonWorkType() == WorkType.LEAVE_CODE) {
                        AttenList.add(a);
                    }
                }
                break;
            case 3://旷工
                AttenList.clear();
                for (AttendancemTable a : SumAttenList) {
                    if (a.getMorningWorkType() == WorkType.ABSENTEEISM_CODE || a.getAfternoonWorkType() == WorkType.ABSENTEEISM_CODE) {
                        AttenList.add(a);
                    }
                }
                break;
            case 4://加班
                AttenList.clear();
                for (AttendancemTable a : SumAttenList) {
                    if (a.getMorningWorkType() == WorkType.OVERTIME_CODE || a.getAfternoonWorkType() == WorkType.OVERTIME_CODE) {
                        AttenList.add(a);
                    }
                }
                break;
            case 5://出差
                AttenList.clear();
                for (AttendancemTable a : SumAttenList) {
                    if (a.getMorningWorkType() == WorkType.TRIP_CODE || a.getAfternoonWorkType() == WorkType.TRIP_CODE) {
                        AttenList.add(a);
                    }
                }
                break;
            default:
                break;
        }
        if (AttenList.size() > 0) {
            tvNodata.setVisibility(View.GONE);
        } else {
            tvNodata.setVisibility(View.VISIBLE);
        }
        tvAttenSize.setText("(共" + AttenList.size() + "条数据!)");
        attendListAdapter.notifyDataSetChanged();
    }


    /**
     * 按照打卡状态-筛选数据
     */
    private String[] areas = new String[]{"全部", "正常打卡", "迟到打卡", "早退打卡", "补卡"};
    private AlertDialog.Builder TypeDialog;
    private int selectType = 0;

    private void setStautsType() {
        if (TypeDialog == null) {
            TypeDialog = new AlertDialog.Builder(this)
                    .setTitle("选择状态类型筛选")
                    .setItems(areas, (dialog, which) -> {
                        spStautsType.setText(areas[which]);
                        selectType = which;

                        ScreebListData();
                        dialog.dismiss();
                    });
        }
        TypeDialog.show();
    }

    /**
     * 加载数据库的考勤数据
     */
    private void initListData() {
        String selectDBdateStr = selectYear + "-" + (selectMonth > 9 ? selectMonth + "" : "0" + selectMonth) + "-" + (selectDay > 9 ? selectDay + "" : "0" + selectDay);

        SumAttenList.clear();
        AttenList.clear();
        AttenList.addAll(LitePal.where("Date = ?", selectDBdateStr).find(AttendancemTable.class));

        //保存到从数据
        for (AttendancemTable a : AttenList) {
            SumAttenList.add(a);
        }

        if (AttenList.size() > 0) {
            tvNodata.setVisibility(View.GONE);
            llStautsLayout.setVisibility(View.VISIBLE);
            llAttenLayout.setVisibility(View.VISIBLE);
        } else {
            tvNodata.setVisibility(View.VISIBLE);

            llStautsLayout.setVisibility(View.GONE);
            llAttenLayout.setVisibility(View.GONE);
        }
        attendListAdapter.notifyDataSetChanged();


        spStautsType.setText("全部");

        selectType = 0;
        ScreebListData();
    }

    /**
     * 对已经显示的数据做筛选
     */
    private void ScreebListData() {
        switch (selectType) {
            case 0://显示全部
                AttenList.clear();
                for (AttendancemTable a : SumAttenList) {
                    AttenList.add(a);
                }
                break;
            case 1://显示正常打卡
                AttenList.clear();
                for (AttendancemTable a : SumAttenList) {
                    if (a.getMorning_start_type() == AttendType.NORMAL_CODE && a.getMorning_end_type() == AttendType.NORMAL_CODE && a.getAfternoon_start_type() == AttendType.NORMAL_CODE && a.getAfternoon_end_type() == AttendType.NORMAL_CODE) {
                        AttenList.add(a);
                    }
                }
                break;

            case 2://显示迟到打卡
                AttenList.clear();
                for (AttendancemTable a : SumAttenList) {
                    if (a.getMorning_start_type() == AttendType.LATE_CODE || a.getAfternoon_start_type() == AttendType.LATE_CODE) {
                        AttenList.add(a);
                    }
                }

                break;

            case 3://显示早退打卡
                AttenList.clear();
                for (AttendancemTable a : SumAttenList) {
                    if (a.getMorning_end_type() == AttendType.LEAVE_CODE || a.getAfternoon_end_type() == AttendType.LEAVE_CODE) {
                        AttenList.add(a);
                    }
                }
                break;

            case 4://显示补卡
                AttenList.clear();
                for (AttendancemTable a : SumAttenList) {
                    if (a.getMorning_start_type() == AttendType.FILL_CODE || a.getMorning_end_type() == AttendType.FILL_CODE || a.getAfternoon_start_type() == AttendType.FILL_CODE || a.getAfternoon_end_type() == AttendType.FILL_CODE) {
                        AttenList.add(a);
                    }
                }
                break;

            default:
                break;
        }

        if (AttenList.size() > 0) {
            tvNodata.setVisibility(View.GONE);
        } else {
            tvNodata.setVisibility(View.VISIBLE);
        }
        tvListSize.setText("(共" + AttenList.size() + "条数据!)");
        attendListAdapter.notifyDataSetChanged();
    }

    @OnClick({R.id.sp_stauts_type, R.id.tv_atten_type})
    public void onViewClicked(View v) {
        switch (v.getId()) {
            case R.id.sp_stauts_type:
                setStautsType();
                break;
            case R.id.tv_atten_type:
                showAttenDialog();
                break;
            default:
                break;
        }

    }

}
