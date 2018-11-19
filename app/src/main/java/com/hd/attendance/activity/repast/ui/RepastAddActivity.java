package com.hd.attendance.activity.repast.ui;


import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.bigkoo.pickerview.TimePickerView;
import com.hd.attendance.R;
import com.hd.attendance.activity.group.user.UserBean;
import com.hd.attendance.activity.group.user.UserChooseActivity;
import com.hd.attendance.base.BaseActivity;
import com.hd.attendance.db.RepastTable;
import com.hd.attendance.utils.DateUtils;
import com.hd.attendance.utils.SystemLog;
import com.hd.attendance.utils.ToastUtil;

import org.litepal.LitePal;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RepastAddActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_date)
    TextView tvDate;
    @BindView(R.id.rb_1_yes)
    RadioButton rb1Yes;
    @BindView(R.id.rb_1_no)
    RadioButton rb1No;
    @BindView(R.id.rg1)
    RadioGroup rg1;
    @BindView(R.id.tv_afternoon_Report_time)
    TextView tvAfternoonReportTime;
    @BindView(R.id.rb_2_yes)
    RadioButton rb2Yes;
    @BindView(R.id.rb_2_no)
    RadioButton rb2No;
    @BindView(R.id.rg2)
    RadioGroup rg2;
    @BindView(R.id.tv_afternoon_eat_time)
    TextView tvAfternoonEatTime;
    @BindView(R.id.rb_3_yes)
    RadioButton rb3Yes;
    @BindView(R.id.rb_3_no)
    RadioButton rb3No;
    @BindView(R.id.rg3)
    RadioGroup rg3;
    @BindView(R.id.tv_Evening_Report_time)
    TextView tvEveningReportTime;
    @BindView(R.id.rb_4_yes)
    RadioButton rb4Yes;
    @BindView(R.id.rb_4_no)
    RadioButton rb4No;
    @BindView(R.id.rg4)
    RadioGroup rg4;
    @BindView(R.id.tv_Evening_eat_time)
    TextView tvEveningEatTime;
    @BindView(R.id.et_note)
    EditText etNote;
    @BindView(R.id.bt_save)
    Button btSave;


    private RepastTable repastTable;

    private String RepastID = "";
    private boolean isUpdate = false;

    @Override
    protected void initVariables() {
        if (getIntent() != null) {
            RepastID = getIntent().getStringExtra("RepastID");
        }

    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        setContentView(R.layout.activity_repast_add);
        ButterKnife.bind(this);
        initToolbarNav();
        setTitle("新增就餐记录");

        //添加下划线
        tvName.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        tvDate.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);

        tvAfternoonReportTime.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        tvAfternoonEatTime.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        tvEveningReportTime.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        tvEveningEatTime.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);

    }

    @Override
    protected void initData() {

        if (!TextUtils.isEmpty(RepastID)) {
            setTitle("修改就餐数据");
            btSave.setText("修  改");
            isUpdate = true;

            List<RepastTable> re = new ArrayList<>();
            re.clear();
            re.addAll(LitePal.where("id = ?", RepastID).find(RepastTable.class));
            if (re.size() == 1) {
                repastTable = re.get(0);
                //填充到试图
                tvName.setText(repastTable.getUser_Name());
                tvName.setEnabled(false);

                tvDate.setText(repastTable.getDate() + "[" + repastTable.getWeek() + "]");
                tvDate.setEnabled(false);

                tvAfternoonReportTime.setText(repastTable.getAfternoon_Report_time() + "");
                tvAfternoonEatTime.setText(repastTable.getAfternoon_Eat_time() + "");
                tvEveningReportTime.setText(repastTable.getEvening_Report_time() + "");
                tvEveningEatTime.setText(repastTable.getEvening_Eat_time() + "");

                etNote.setText(repastTable.getNote() + "");

                if (repastTable.isAfternoon_Report()) {
                    rb1Yes.setChecked(true);
                } else {
                    rb1No.setChecked(true);
                }
                if (repastTable.isAfternoon_Eat()) {
                    rb2Yes.setChecked(true);
                } else {
                    rb2No.setChecked(true);
                }
                if (repastTable.isEvening_Report()) {
                    rb3Yes.setChecked(true);
                } else {
                    rb3No.setChecked(true);
                }
                if (repastTable.isEvening_Eat()) {
                    rb4Yes.setChecked(true);
                } else {
                    rb4No.setChecked(true);
                }
            } else {
                ToastUtil.showToast("该条数据不存在!");
                finish();
            }
        } else {
            repastTable = new RepastTable();
        }
    }

    @OnClick({R.id.tv_name, R.id.tv_date, R.id.tv_afternoon_Report_time, R.id.tv_afternoon_eat_time, R.id.tv_Evening_Report_time, R.id.tv_Evening_eat_time, R.id.bt_save})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_name:
                Intent intent = new Intent(this, UserChooseActivity.class);
                intent.putExtra("mChooseNums", "single");
                intent.putExtra("isalls", "1");
                startActivityForResult(intent, 100);
                break;
            case R.id.tv_date:
                ShowSelectDate();
                break;
            case R.id.tv_afternoon_Report_time:
                showSelectTime(1);
                break;
            case R.id.tv_afternoon_eat_time:
                showSelectTime(2);
                break;
            case R.id.tv_Evening_Report_time:
                showSelectTime(3);
                break;
            case R.id.tv_Evening_eat_time:
                showSelectTime(4);
                break;
            case R.id.bt_save:
                if (isUpdate) {
                    showProgressDialog("修改中..");
                    //修改
                    repastTable.setNote(etNote.getText().toString());

                    //中午是否报餐
                    if (rb1Yes.isChecked()) {
                        repastTable.setAfternoon_Report(true);
                    } else {
                        repastTable.setAfternoon_Report(false);
                    }
                    //中午是否就餐
                    if (rb2Yes.isChecked()) {
                        repastTable.setAfternoon_Eat(true);
                    } else {
                        repastTable.setAfternoon_Eat(false);
                    }

                    //晚餐是否报餐
                    if (rb3Yes.isChecked()) {
                        repastTable.setEvening_Report(true);
                    } else {
                        repastTable.setEvening_Report(false);
                    }

                    //晚餐是否就餐
                    if (rb4Yes.isChecked()) {
                        repastTable.setEvening_Eat(true);
                    } else {
                        repastTable.setEvening_Eat(false);
                    }

                    repastTable.setAfternoon_Report_time(tvAfternoonReportTime.getText().toString());
                    repastTable.setAfternoon_Eat_time(tvAfternoonEatTime.getText().toString());
                    repastTable.setEvening_Report_time(tvEveningReportTime.getText().toString());
                    repastTable.setEvening_Eat_time(tvEveningEatTime.getText().toString());

                    repastTable.save();

                    ToastUtil.showToast("修改成功!");
                    SystemLog.getInstance().AddLog("[管理员] 修改了[" + tvName.getText().toString() + "] " + tvDate.getText().toString() + " 的就餐记录!");

                    finish();
                    return;
                }

                if (isadd) {
                    if (mCheckListS.size() == 0) {
                        ToastUtil.showToast("请选择用户!");
                        return;
                    }

                    if (TextUtils.isEmpty(dateS)) {
                        ToastUtil.showToast("请选择时间!");
                        return;
                    }

                    showProgressDialog("新增中..");

                    repastTable.setUser_ID(mCheckListS.get(0).getUser_id());
                    repastTable.setUser_Name(mCheckListS.get(0).getUser_name());
                    repastTable.setDate(dateS);
                    repastTable.setWeek(DateUtils.DateToDayB(dateS));

                    repastTable.setNote(etNote.getText().toString());

                    //中午是否报餐
                    if (rb1Yes.isChecked()) {
                        repastTable.setAfternoon_Report(true);
                    } else {
                        repastTable.setAfternoon_Report(false);
                    }
                    //中午是否就餐
                    if (rb2Yes.isChecked()) {
                        repastTable.setAfternoon_Eat(true);
                    } else {
                        repastTable.setAfternoon_Eat(false);
                    }

                    //晚餐是否报餐
                    if (rb3Yes.isChecked()) {
                        repastTable.setEvening_Report(true);
                    } else {
                        repastTable.setEvening_Report(false);
                    }

                    //晚餐是否就餐
                    if (rb4Yes.isChecked()) {
                        repastTable.setEvening_Eat(true);
                    } else {
                        repastTable.setEvening_Eat(false);
                    }


                    repastTable.save();
                    closeProgressDialog();

                    ToastUtil.showToast("新增成功!");
                    SystemLog.getInstance().AddLog("[管理员] 新增了[" + tvName.getText().toString() + "] " + tvDate.getText().toString() + " 的就餐记录!");

                    finish();

                } else {
                    ToastUtil.showToast("当前员工在当前选择的时间下已有就餐信息!");
                }

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
                    tvName.setText(mCheckListS.get(0).getUser_name());
                    isDBUserInfo();
                }
            }
        }
    }

    /**
     * 显示时间选择
     */
    private boolean[] Timetype = new boolean[]{false, false, false, true, true, false};//显示类型 默认全部显示

    private void showSelectTime(int i) {
        //时间选择器
        TimePickerView pvTime = new TimePickerView.Builder(this, (date, v) -> {//选中事件回调
            String time = getTime(date);
            switch (i) {
                case 1://中午报餐时间
                    tvAfternoonReportTime.setText(time);
                    repastTable.setAfternoon_Report_time(time);

                    break;
                case 2://中午就餐时间
                    tvAfternoonEatTime.setText(time);
                    repastTable.setAfternoon_Eat_time(time);

                    break;
                case 3://晚饭报餐时间
                    tvEveningReportTime.setText(time);
                    repastTable.setEvening_Report_time(time);

                    break;
                case 4://晚饭就餐时间
                    tvEveningEatTime.setText(time);
                    repastTable.setEvening_Eat_time(time);
                    break;
                default:
                    break;
            }

        })//默认全部显示
                .setContentSize(20)//滚轮文字大小
                .setOutSideCancelable(true)//点击屏幕，点在控件外部范围时，是否取消显示
                .setCancelText("取消")//取消按钮文字
                .setSubmitText("确定")//确认按钮文字
                .setType(Timetype)
                .build();
        pvTime.setDate(Calendar.getInstance());//注：根据需求来决定是否使用该方法（一般是精确到秒的情况），此项可以在弹出选择器的时候重新设置当前时间，避免在初始化之后由于时间已经设定，导致选中时间与当前时间不匹配的问题。
        pvTime.show();

    }

    /**
     * 选择日期
     */
    private boolean[] Datetype = new boolean[]{true, true, true, false, false, false};//显示类型 默认全部显示
    String dateS = "";

    private void ShowSelectDate() {
        //时间选择器
        TimePickerView pvTime = new TimePickerView.Builder(this, (date, v) -> {//选中事件回调
            dateS = getDate(date);
            tvDate.setText(dateS + " [" + DateUtils.DateToDayB(dateS) + "]");

            isDBUserInfo();
        })//默认全部显示
                .setContentSize(20)//滚轮文字大小
                .setOutSideCancelable(true)//点击屏幕，点在控件外部范围时，是否取消显示
                .setCancelText("取消")//取消按钮文字
                .setSubmitText("确定")//确认按钮文字
                .setType(Datetype)
                .build();
        pvTime.setDate(Calendar.getInstance());//注：根据需求来决定是否使用该方法（一般是精确到秒的情况），此项可以在弹出选择器的时候重新设置当前时间，避免在初始化之后由于时间已经设定，导致选中时间与当前时间不匹配的问题。
        pvTime.show();
    }

    /**
     * 格式化时间
     *
     * @param date
     * @return
     */
    public String getDate(Date date) {
        //可根据需要自行截取数据显示
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(date);
    }

    /**
     * 格式化时间
     *
     * @param date
     * @return
     */
    public String getTime(Date date) {
        //可根据需要自行截取数据显示
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        return format.format(date);
    }


    /**
     * 判断当前日期是否存在此员工的信息
     */
    private List<RepastTable> AttenList = new ArrayList<>();
    private boolean isadd = true;

    private void isDBUserInfo() {
        if (mCheckListS.size() > 0 && !TextUtils.isEmpty(dateS)) {
            int userID = mCheckListS.get(0).getUser_id();
            AttenList.clear();
            AttenList.addAll(LitePal.where("User_ID = ? and Date = ?", userID + "", dateS).find(RepastTable.class));
            if (AttenList.size() > 0) {
                ToastUtil.showToast("当天已经存在该员工的就餐信息!请重选!");
                isadd = false;
                btSave.setBackgroundColor(getResources().getColor(R.color.gray));
            } else {
                isadd = true;
                btSave.setBackgroundColor(getResources().getColor(R.color.blue));
            }
        }
    }

}
