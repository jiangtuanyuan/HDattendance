package com.hd.attendance.activity.attendancem.ui;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.bigkoo.pickerview.TimePickerView;
import com.hd.attendance.R;
import com.hd.attendance.activity.group.user.UserBean;
import com.hd.attendance.activity.group.user.UserChooseActivity;
import com.hd.attendance.base.BaseActivity;
import com.hd.attendance.db.AttendancemTable;
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

public class AttendancemAddActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_date)
    TextView tvDate;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.sp_atten)
    Spinner spAtten;
    @BindView(R.id.tv_morning_start_time)
    TextView tvMorningStartTime;
    @BindView(R.id.tv_morning_start_time_update)
    TextView tvMorningStartTimeUpdate;
    @BindView(R.id.tv_morning_start_type)
    TextView tvMorningStartType;
    @BindView(R.id.tv_morning_start_type_update)
    TextView tvMorningStartTypeUpdate;
    @BindView(R.id.et_morning_start_note)
    EditText etMorningStartNote;
    @BindView(R.id.tv_morning_end_time)
    TextView tvMorningEndTime;
    @BindView(R.id.tv_morning_end_time_update)
    TextView tvMorningEndTimeUpdate;
    @BindView(R.id.tv_morning_end_type)
    TextView tvMorningEndType;
    @BindView(R.id.tv_morning_end_type_update)
    TextView tvMorningEndTypeUpdate;
    @BindView(R.id.et_morning_end_note)
    EditText etMorningEndNote;
    @BindView(R.id.tv_afternoon_start_time)
    TextView tvAfternoonStartTime;
    @BindView(R.id.tv_afternoon_start_time_update)
    TextView tvAfternoonStartTimeUpdate;
    @BindView(R.id.tv_afternoon_start_type)
    TextView tvAfternoonStartType;
    @BindView(R.id.tv_afternoon_start_type_update)
    TextView tvAfternoonStartTypeUpdate;
    @BindView(R.id.et_afternoon_start_note)
    EditText etAfternoonStartNote;
    @BindView(R.id.tv_afternoon_end_time)
    TextView tvAfternoonEndTime;
    @BindView(R.id.tv_afternoon_end_time_update)
    TextView tvAfternoonEndTimeUpdate;
    @BindView(R.id.tv_afternoon_end_type)
    TextView tvAfternoonEndType;
    @BindView(R.id.tv_afternoon_end_type_update)
    TextView tvAfternoonEndTypeUpdate;
    @BindView(R.id.et_afternoon_end_note)
    EditText etAfternoonEndNote;
    @BindView(R.id.tv_isDeductions)
    TextView tvIsDeductions;
    @BindView(R.id.rb_yes)
    RadioButton rbYes;
    @BindView(R.id.rb_no)
    RadioButton rbNo;
    @BindView(R.id.rg_sex)
    RadioGroup rgSex;
    @BindView(R.id.et_Deductions)
    EditText etDeductions;
    @BindView(R.id.et_deductionsInfo)
    EditText etDeductionsInfo;
    @BindView(R.id.bt_save)
    Button btSave;


    private AttendancemTable attendancem;

    @Override
    protected void initVariables() {

    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        setContentView(R.layout.activity_attendancem_add);
        ButterKnife.bind(this);
        initToolbarNav();
        setTitle("新  增");
        attendancem = new AttendancemTable();

        tvMorningStartTimeUpdate.getPaint().setFlags(Paint. UNDERLINE_TEXT_FLAG );
        tvMorningStartTypeUpdate.getPaint().setFlags(Paint. UNDERLINE_TEXT_FLAG );

        tvMorningEndTimeUpdate.getPaint().setFlags(Paint. UNDERLINE_TEXT_FLAG );
        tvMorningEndTypeUpdate.getPaint().setFlags(Paint. UNDERLINE_TEXT_FLAG );

        tvAfternoonStartTimeUpdate.getPaint().setFlags(Paint. UNDERLINE_TEXT_FLAG );
        tvAfternoonStartTypeUpdate.getPaint().setFlags(Paint. UNDERLINE_TEXT_FLAG );

        tvAfternoonEndTimeUpdate.getPaint().setFlags(Paint. UNDERLINE_TEXT_FLAG );
        tvAfternoonEndTypeUpdate.getPaint().setFlags(Paint. UNDERLINE_TEXT_FLAG );
    }

    @Override
    protected void initData() {

    }


    @OnClick({R.id.tv_date, R.id.tv_name, R.id.tv_morning_start_time_update, R.id.tv_morning_start_type_update, R.id.tv_morning_end_time_update, R.id.tv_morning_end_type_update, R.id.tv_afternoon_start_time_update, R.id.tv_afternoon_start_type_update, R.id.tv_afternoon_end_time_update, R.id.tv_afternoon_end_type_update, R.id.bt_save})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_name://选择员工
                Intent intent = new Intent(this, UserChooseActivity.class);
                intent.putExtra("mChooseNums", "single");
                intent.putExtra("isalls", "1");

                startActivityForResult(intent, 100);
                break;
            case R.id.tv_date://选择时间
                ShowSelectDate();
                break;
            case R.id.tv_morning_start_time_update://更改上午上班时间
                showSelectTime(1);
                break;
            case R.id.tv_morning_start_type_update://更改上午上班类型
                selevtType(1);
                break;
            case R.id.tv_morning_end_time_update://更改上午下班时间
                showSelectTime(2);
                break;
            case R.id.tv_morning_end_type_update://更改上午下班类型
                selevtType(2);
                break;
            case R.id.tv_afternoon_start_time_update://更改下午上班时间
                showSelectTime(3);
                break;
            case R.id.tv_afternoon_start_type_update://更改下午上班类型
                selevtType(3);
                break;
            case R.id.tv_afternoon_end_time_update://更改下午下班时间
                showSelectTime(4);
                break;
            case R.id.tv_afternoon_end_type_update://更改上下午下班类型
                selevtType(4);
                break;
            case R.id.bt_save://保存修改
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
                    attendancem.setUser_ID(mCheckListS.get(0).getUser_id());
                    attendancem.setUser_Name(mCheckListS.get(0).getUser_name());
                    attendancem.setDate(dateS);
                    attendancem.setWeek(DateUtils.DateToDayB(dateS));
                    attendancem.setWorkType(spAtten.getSelectedItemPosition() + 1);
                    if (rbYes.isChecked()) {
                        attendancem.setIsDeductions(true);
                    } else {
                        attendancem.setIsDeductions(false);
                    }
                    try {
                        Double money = Double.parseDouble(etDeductions.getText().toString());
                        attendancem.setDeductions(money);
                    } catch (Exception e) {
                        closeProgressDialog();
                        ToastUtil.showToast("扣款金额填写错误!");
                    }
                    attendancem.setDeductionsInfo(etDeductionsInfo.getText().toString());

                    attendancem.setMorning_start_note(etMorningStartNote.getText().toString());
                    attendancem.setMorning_end_note(etMorningEndNote.getText().toString());
                    attendancem.setAfternoon_start_note(etAfternoonStartNote.getText().toString());
                    attendancem.setAfternoon_end_note(etAfternoonEndNote.getText().toString());


                    attendancem.save();
                    closeProgressDialog();

                    ToastUtil.showToast("新增成功!");
                    finish();

                    SystemLog.getInstance().AddLog("[管理员] 新增了[" + tvName + "] " + tvDate + " 的考勤!");

                } else {
                    ToastUtil.showToast("当前员工在当前选择的时间下已有考勤信息!");
                }


                break;
            default:
                break;
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
                case 1://上午上班
                    tvMorningStartTime.setText("打卡时间: " + time);
                    attendancem.setMorning_start_time(time);

                    break;
                case 2://上午下班
                    tvMorningEndTime.setText("打卡时间: " + time);
                    attendancem.setMorning_end_time(time);

                    break;
                case 3://下午上班
                    tvAfternoonStartTime.setText("打卡时间: " + time);
                    attendancem.setAfternoon_start_time(time);

                    break;
                case 4://下午下班
                    tvAfternoonEndTime.setText("打卡时间: " + time);
                    attendancem.setAfternoon_end_time(time);

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
     * 选择打卡状态
     */
    private String[] areas = new String[]{"正常打卡", "迟到打卡", "早退打卡", "忘记打卡", "补卡","请假"};

    private void selevtType(int x) {
        new AlertDialog.Builder(this)
                .setTitle("选择打卡类型")
                .setItems(areas, (dialog, which) -> {
                    switch (x) {
                        case 1://上午上班打卡状态
                            tvMorningStartType.setText("状  态:" + areas[which]);
                            attendancem.setMorning_start_type(which);
                            break;
                        case 2://上午下班打卡状态
                            tvMorningEndType.setText("状  态:" + areas[which]);
                            attendancem.setMorning_end_type(which);
                            break;
                        case 3://下午上班打卡状态
                            tvAfternoonStartType.setText("状  态:" + areas[which]);
                            attendancem.setAfternoon_start_type(which);
                            break;
                        case 4://下午下班打卡状态
                            tvAfternoonEndType.setText("状  态:" + areas[which]);
                            attendancem.setAfternoon_end_type(which);
                            break;
                        default:
                            break;
                    }
                    dialog.dismiss();
                }).show();
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
            tvDate.setText(dateS);

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
     * 判断当前日期是否存在此员工的信息
     */
    private List<AttendancemTable> AttenList = new ArrayList<>();
    private boolean isadd = true;

    private void isDBUserInfo() {
        if (mCheckListS.size() > 0 && !TextUtils.isEmpty(dateS)) {
            int userID = mCheckListS.get(0).getUser_id();
            AttenList.clear();
            AttenList.addAll(LitePal.where("User_ID = ? and Date = ?", userID + "", dateS).find(AttendancemTable.class));
            if (AttenList.size() > 0) {
                ToastUtil.showToast("当天已经存在该员工的考勤信息!请重选!");
                isadd = false;
                btSave.setBackgroundColor(getResources().getColor(R.color.gray));
            } else {
                isadd = true;
                btSave.setBackgroundColor(getResources().getColor(R.color.blue));
            }
        }
    }
}
