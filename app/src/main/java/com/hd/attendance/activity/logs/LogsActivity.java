package com.hd.attendance.activity.logs;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.hd.attendance.R;
import com.hd.attendance.activity.fingerprint.adapter.FingerGroupAdapter;
import com.hd.attendance.base.BaseActivity;
import com.hd.attendance.db.SystemLogTable;
import com.hd.attendance.utils.DateUtils;
import com.hd.attendance.utils.SystemLog;
import com.hd.attendance.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LogsActivity extends BaseActivity implements FingerGroupAdapter.OnItemClickListener {

    @BindView(R.id.recycler_month)
    RecyclerView recyclerMonth;
    @BindView(R.id.recycler_day)
    RecyclerView recyclerDay;
    @BindView(R.id.tv_nodata)
    TextView tvNodata;
    @BindView(R.id.sp_year)
    Spinner spinner;

    //日期横向滚动
    private int position = 0;
    private FingerGroupAdapter MonthAdapter;
    private List<String> MonthList = new ArrayList<>();
    //数据
    private LogsListAdapter LosgAdapter;
    private List<SystemLogTable> mLosgList = new ArrayList<>();

    //选择的年月
    private String selectYear = DateUtils.getThisYear() + "";
    private int selectMonth = DateUtils.getThisMonth();

    @Override
    protected void initVariables() {

    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        setContentView(R.layout.activity_logs);
        ButterKnife.bind(this);
        initToolbarNav();
        setTitle("系统日志");

        setSelectYear();
        setRecyclerw();
        setDataRecyclerw();
    }

    @Override
    protected void initData() {
        initMonth(true);
    }

    /**
     * 加载月份
     *
     * @param isthisyear 是否是今年
     */
    private void initMonth(boolean isthisyear) {
        MonthList.clear();
        if (isthisyear) {
            int x = DateUtils.getThisMonth();
            selectMonth = x;
            if (x > 0) {
                for (int i = 1; i <= x; i++) {
                    MonthList.add(i + "月");
                }
                MonthAdapter.notifyDataSetChanged();
                MonthAdapter.setDefSelect(x - 1);
                recyclerMonth.scrollToPosition(x - 1);

                getDayData(selectYear, selectMonth);
            }
        } else {

            for (int i = 1; i <= 12; i++) {
                MonthList.add(i + "月");
            }
            selectMonth = 1;
            MonthAdapter.notifyDataSetChanged();
            MonthAdapter.setDefSelect(0);
            recyclerMonth.scrollToPosition(0);

            getDayData(selectYear, selectMonth);
        }

    }


    /**
     * 根据月份拿系统日志
     *
     * @param month
     */
    private void getDayData(String year, int month) {
        mLosgList.clear();
        String monthstr = "";
        if (month > 9) {
            monthstr = month + "";
        } else {
            monthstr = "0" + month;
        }

        mLosgList.addAll(SystemLog.getInstance().getMonthLog(year + "", monthstr));
        LosgAdapter.notifyDataSetChanged();

        if (mLosgList.size() == 0) {
            tvNodata.setVisibility(View.VISIBLE);
        } else {
            tvNodata.setVisibility(View.GONE);
        }

    }

    /**
     * 设置年份
     */

    private void setSelectYear() {
        int year = DateUtils.getThisYear();
        final String[] arr = {year + "", (year - 1) + "", (year - 2) + ""};

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, arr);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectYear = arr[position];
                if (selectYear.equals(DateUtils.getThisYear() + "")) {
                    initMonth(true);
                } else {
                    initMonth(false);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    /**
     * 设置月份
     */
    private void setRecyclerw() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerMonth.setLayoutManager(layoutManager);
        MonthAdapter = new FingerGroupAdapter(MonthList);
        MonthAdapter.setDefSelect(0);
        MonthAdapter.setOnItemClickListener(this);
        recyclerMonth.setAdapter(MonthAdapter);
    }

    /**
     * 设置数据
     */
    private void setDataRecyclerw() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerDay.setLayoutManager(layoutManager);
        LosgAdapter = new LogsListAdapter(this, mLosgList);
        recyclerDay.setAdapter(LosgAdapter);
    }


    @Override
    public void onItemClickListener(View view, int position) {
        MonthAdapter.setDefSelect(position);
        this.position = position;
        this.selectMonth = position + 1;

        getDayData(selectYear, selectMonth);
    }

}
