package com.hd.attendance.activity.health.ui;


import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.hd.attendance.R;
import com.hd.attendance.activity.fingerprint.adapter.FingerGroupAdapter;
import com.hd.attendance.activity.group.user.UserBean;
import com.hd.attendance.activity.group.user.UserChooseActivity;
import com.hd.attendance.base.BaseActivity;
import com.hd.attendance.db.HealthTable;
import com.hd.attendance.utils.DateUtils;
import com.hd.attendance.utils.SystemLog;
import com.hd.attendance.utils.ToastUtil;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class HealthMainActivity extends BaseActivity implements FingerGroupAdapter.OnItemClickListener, RadioGroup.OnCheckedChangeListener {
    @BindView(R.id.recycler_week)
    RecyclerView recyclerWeek;
    @BindView(R.id.tv_a_users)
    TextView tvAUsers;
    @BindView(R.id.tv_a_user_choose)
    TextView tvAUserChoose;
    @BindView(R.id.et_a_info)
    EditText etAInfo;
    @BindView(R.id.tv_b_users)
    TextView tvBUsers;
    @BindView(R.id.tv_b_user_choose)
    TextView tvBUserChoose;
    @BindView(R.id.et_b_info)
    EditText etBInfo;

    @BindView(R.id.bt_save)
    Button btSave;

    //周六大扫除相关
    @BindView(R.id.rb_single_weeks)
    RadioButton rbSingleWeeks;
    @BindView(R.id.rb_double_weeks)
    RadioButton rbDoubleWeeks;
    @BindView(R.id.rg_weeks)
    RadioGroup rgWeeks;
    @BindView(R.id.ll_weeks_layout)
    LinearLayout llWeeksLayout;
    @BindView(R.id.tv_week_info)
    TextView tvWeekInfo;
    @BindView(R.id.ll_this_weeks_layout)
    LinearLayout llThisWeeksLayout;


    private int position = 0;
    //日期横向滚动
    private FingerGroupAdapter WeekAdapter;
    private List<String> WeekList = new ArrayList<>();

    //显示的
    private HealthTable healt;

    private int number = 0;

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
        rgWeeks.setOnCheckedChangeListener(this);
        number = DateUtils.getThisWeeks();
        tvAUserChoose.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        tvBUserChoose.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);

    }

    @Override
    protected void initData() {
        WeekList.add("星期一");//0
        WeekList.add("星期二");//1
        WeekList.add("星期三");//2
        WeekList.add("星期四");//3
        WeekList.add("星期五");//4
        WeekList.add("星期六");//5
        WeekList.add("星期日");//6

        WeekAdapter.notifyDataSetChanged();

        try {
            int week = DateUtils.getWeek();
            position = week - 1;

            switch (week) {
                case 0://星期天
                    WeekAdapter.setDefSelect(6);
                    GetHealth(7 + "");
                    break;
                case 6://星期六 大扫除
                    WeekAdapter.setDefSelect(5);
                    //1.显示布局
                    llWeeksLayout.setVisibility(View.VISIBLE);
                    llThisWeeksLayout.setVisibility(View.VISIBLE);

                    if (number % 2 == 0) {
                        //双周
                        tvWeekInfo.setText("双周 [当年第" + number + "周]");
                        rbDoubleWeeks.setChecked(true);
                        //加载双周的数据
                        GetHealth(2);
                    } else {
                        tvWeekInfo.setText("单周 [当年第" + number + "周]");
                        rbSingleWeeks.setChecked(true);
                        //加载单周的数据
                        GetHealth(1);
                    }

                    break;
                default://1-5
                    WeekAdapter.setDefSelect(position);
                    GetHealth(week + "");
                    break;
            }

            recyclerWeek.scrollToPosition(position);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置星期
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

    @Override
    public void onItemClickListener(View view, int position) {
        this.position = position;
        WeekAdapter.setDefSelect(position);
        if (position == 5) {
            llWeeksLayout.setVisibility(View.VISIBLE);
            llThisWeeksLayout.setVisibility(View.VISIBLE);

            if (number % 2 == 0) {
                //双周
                tvWeekInfo.setText("双周 [当年第" + number + "周]");
                rbDoubleWeeks.setChecked(true);
                //加载双周的数据
                GetHealth(2);
            } else {
                tvWeekInfo.setText("单周 [当年第" + number + "周]");
                rbSingleWeeks.setChecked(true);
                //加载单周的数据
                GetHealth(1);
            }

        } else {
            llWeeksLayout.setVisibility(View.GONE);
            llThisWeeksLayout.setVisibility(View.GONE);

            GetHealth(String.valueOf(position + 1));
        }

    }


    @OnClick({R.id.bt_save, R.id.tv_a_user_choose, R.id.tv_b_user_choose})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_a_user_choose://选择一楼卫生人员
                Intent onIntent = new Intent(this, UserChooseActivity.class);
                onIntent.putExtra("mChooseNums", "more");
                onIntent.putExtra("isalls", "1");
                startActivityForResult(onIntent, 101);

                break;
            case R.id.tv_b_user_choose://选择二楼卫生人员
                Intent TwonIntent = new Intent(this, UserChooseActivity.class);
                TwonIntent.putExtra("mChooseNums", "more");
                TwonIntent.putExtra("isalls", "1");
                startActivityForResult(TwonIntent, 102);
                break;
            case R.id.bt_save:
                healt.setWeekid(position + 1);
                healt.setWeeki(WeekList.get(position));
                healt.setOnFloorUserID(AIDstring.toString());
                healt.setOnFloorUserName(tvAUsers.getText().toString());
                healt.setOnFloorInfo(etAInfo.getText().toString());

                healt.setTwoFloorUserID(BIDstring.toString());
                healt.setTwoFloorUserName(tvBUsers.getText().toString());
                healt.setTwoFloorInfo(etBInfo.getText().toString());

                if (this.position == 5) {
                    //设置大扫除
                    if (rbSingleWeeks.isChecked()) {
                        healt.setWeeks(1);//设置单周为1
                    } else {
                        healt.setWeeks(2);//设置双周为2
                    }
                }

                healt.save();
                ToastUtil.showToast("保存成功!");

                SystemLog.getInstance().AddLog("[管理员] 修改了 [" + WeekList.get(position) + "]的卫生!");

                if (position == 5) {
                    if (rbSingleWeeks.isChecked()) {
                        GetHealth(1);
                    } else {
                        GetHealth(2);
                    }
                } else {
                    GetHealth(String.valueOf(position + 1));
                }

                break;
            default:
                break;
        }
    }


    private List<UserBean> mCheckListA = new ArrayList<>();
    private StringBuffer AIDstring = new StringBuffer();
    private List<UserBean> mCheckListB = new ArrayList<>();
    private StringBuffer BIDstring = new StringBuffer();

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //选择的用户回调 多
        if (requestCode == 101 && resultCode == RESULT_OK) {
            if (data != null) {
                Bundle bundle = data.getExtras();
                mCheckListA.clear();
                mCheckListA.addAll((List<UserBean>) bundle.getSerializable("mCheckList"));
                StringBuffer stringBuffer = new StringBuffer();

                AIDstring.delete(0, AIDstring.length());
                for (int i = 0; i < mCheckListA.size(); i++) {
                    if (i != mCheckListA.size() - 1) {
                        stringBuffer.append(mCheckListA.get(i).getUser_name()).append("、");
                        AIDstring.append(mCheckListA.get(i).getUser_id() + "").append(",");
                    } else {
                        stringBuffer.append(mCheckListA.get(i).getUser_name());
                        AIDstring.append(mCheckListA.get(i).getUser_id() + "");
                    }
                }
                tvAUsers.setText(stringBuffer.toString());
            }
        }

        //选择的用户回调 多
        if (requestCode == 102 && resultCode == RESULT_OK) {
            if (data != null) {
                Bundle bundle = data.getExtras();
                mCheckListB.clear();
                mCheckListB.addAll((List<UserBean>) bundle.getSerializable("mCheckList"));
                StringBuffer stringBuffer = new StringBuffer();

                BIDstring.delete(0, AIDstring.length());
                for (int i = 0; i < mCheckListB.size(); i++) {
                    if (i != mCheckListB.size() - 1) {
                        stringBuffer.append(mCheckListB.get(i).getUser_name()).append("、");
                        BIDstring.append(mCheckListB.get(i).getUser_id() + "").append(",");

                    } else {
                        stringBuffer.append(mCheckListB.get(i).getUser_name());
                        BIDstring.append(mCheckListB.get(i).getUser_id() + "");
                    }
                }
                tvBUsers.setText(stringBuffer.toString());
            }
        }
    }

    private List<HealthTable> HList = new ArrayList<>();

    private void GetHealth(String x) {
        HList.clear();
        HList.addAll(LitePal.where("weekid = ?", x).find(HealthTable.class));

        if (HList.size() > 0) {
            healt = HList.get(0);
            tvAUsers.setText(healt.getOnFloorUserName());
            etAInfo.setText(healt.getOnFloorInfo());

            tvBUsers.setText(healt.getTwoFloorUserName());
            etBInfo.setText(healt.getTwoFloorInfo());
        } else {
            healt = new HealthTable();
            tvAUsers.setText("无");
            etAInfo.setText("");
            etAInfo.setHint("未设置!");

            tvBUsers.setText("无");
            etBInfo.setText("");
            etBInfo.setHint("未设置!");
        }
    }

    /**
     * 获取周六大扫除单双周的数据情况
     */
    private void GetHealth(int ds) {
        HList.clear();
        HList.addAll(LitePal.where("weekid = 6 and weeks= ?", ds + "").find(HealthTable.class));

        if (HList.size() > 0) {
            healt = HList.get(0);
            tvAUsers.setText(healt.getOnFloorUserName());
            etAInfo.setText(healt.getOnFloorInfo());

            tvBUsers.setText(healt.getTwoFloorUserName());
            etBInfo.setText(healt.getTwoFloorInfo());
        } else {
            healt = new HealthTable();
            tvAUsers.setText("无");
            etAInfo.setText("");
            etAInfo.setHint("未设置!");

            tvBUsers.setText("无");
            etBInfo.setText("");
            etBInfo.setHint("未设置!");
        }
    }


    /**
     * 选择单双周的按钮组
     *
     * @param group
     * @param checkedId
     */
    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.rb_single_weeks:
                GetHealth(1);
                break;
            case R.id.rb_double_weeks:
                GetHealth(2);
                break;
            default:
                break;
        }

    }
}
