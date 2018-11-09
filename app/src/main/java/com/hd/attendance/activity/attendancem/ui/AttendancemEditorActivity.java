package com.hd.attendance.activity.attendancem.ui;


import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.graphics.Paint;
import android.os.Bundle;

import android.support.v7.app.AlertDialog;
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

public class AttendancemEditorActivity extends BaseActivity {
    @BindView(R.id.tv_top_user_info)
    TextView tvTopUserInfo;
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
    @BindView(R.id.sp_morning_atten_type)
    Spinner spMorningAttenType;
    @BindView(R.id.sp_afternoon_atten_type)
    Spinner spAfternoonAttenType;
    @BindView(R.id.bt_save)
    Button btSave;

    private String ID = "";
    private AttendancemTable attendancem;

    @Override
    protected void initVariables() {
        if (getIntent() != null) {
            ID = getIntent().getStringExtra("id");
        }
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        setContentView(R.layout.activity_attendancem_editor);
        ButterKnife.bind(this);
        initToolbarNav();
        setTitle("编 辑");

        tvMorningStartTimeUpdate.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        tvMorningStartTypeUpdate.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);

        tvMorningEndTimeUpdate.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        tvMorningEndTypeUpdate.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);

        tvAfternoonStartTimeUpdate.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        tvAfternoonStartTypeUpdate.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);

        tvAfternoonEndTimeUpdate.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        tvAfternoonEndTypeUpdate.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);


    }

    @Override
    protected void initData() {
        if (!TextUtils.isEmpty(ID)) {
            FindAttenData();
        } else {
            ToastUtil.showToast("找不到该条记录!");
        }
    }

    /**
     * 查询该条数据
     */
    private void FindAttenData() {
        List<AttendancemTable> list = new ArrayList<>();
        list.addAll(LitePal.where("id = ?", ID).find(AttendancemTable.class));
        if (list.size() == 1) {
            attendancem = list.get(0);
            iniView();
        } else {
            ToastUtil.showToast("数据异常!");
        }
    }

    /**
     * 加载数据到界面
     */
    @SuppressLint("SetTextI18n")
    private void iniView() {
        //1.设置头部信息
        StringBuffer uSbuff = new StringBuffer();
        uSbuff.append("[");
        uSbuff.append(attendancem.getUser_Name());
        uSbuff.append("]  ");
        uSbuff.append(attendancem.getDate());
        uSbuff.append(" [");
        uSbuff.append(attendancem.getWeek());
        uSbuff.append("] 的考勤记录");
        tvTopUserInfo.setText(uSbuff.toString());

        spMorningAttenType.setSelection(attendancem.getMorningWorkType());
        spAfternoonAttenType.setSelection(attendancem.getAfternoonWorkType());


        //2.1设置上午上班
        tvMorningStartTime.setText(attendancem.getMorning_start_time() == null ? "打卡时间: " + "00:00" : "打卡时间: " + attendancem.getMorning_start_time());
        //2.2设置上午上班状态
        switch (attendancem.getMorning_start_type()) {
            case 0://正常
                tvMorningStartType.setText("状   态: 正常打卡");
                tvMorningStartType.setTextColor(getResources().getColor(R.color.green));
                break;
            case 1://迟到
                tvMorningStartType.setText("状   态: 迟到打卡");
                tvMorningStartType.setTextColor(getResources().getColor(R.color.red));
                break;
            case 2://早退打卡
                tvMorningStartType.setText("状   态: 早退打卡");
                tvMorningStartType.setTextColor(getResources().getColor(R.color.red));
                break;
            case 3://忘记打卡
                tvMorningStartType.setText("状   态: 忘记打卡");
                tvMorningStartType.setTextColor(getResources().getColor(R.color.yell));
                break;
            case 4://补卡
                tvMorningStartType.setText("状   态: 补 卡");
                tvMorningStartType.setTextColor(getResources().getColor(R.color.blue));
                break;
            case 5://上午请假
                tvMorningStartType.setText("状   态: 请 假");
                tvMorningStartType.setTextColor(getResources().getColor(R.color.yell));
                break;
            default:
                break;
        }

        //2.3 备注内容
        etMorningStartNote.setText("备注内容:" + attendancem.getMorning_start_note() == null ? "" : attendancem.getMorning_start_note());


        //上午下班
        //2.1设置上午上班
        tvMorningEndTime.setText(attendancem.getMorning_end_time() == null ? "打卡时间: " + "00:00" : "打卡时间: " + attendancem.getMorning_end_time());

        switch (attendancem.getMorning_end_type()) {
            case 0://正常
                tvMorningEndType.setText("状   态: 正常打卡");
                tvMorningEndType.setTextColor(getResources().getColor(R.color.green));
                break;
            case 1://迟到
                tvMorningEndType.setText("状   态: 迟到打卡");
                tvMorningEndType.setTextColor(getResources().getColor(R.color.red));
                break;
            case 2://早退打卡
                tvMorningEndType.setText("状   态: 早退打卡");
                tvMorningEndType.setTextColor(getResources().getColor(R.color.red));
                break;
            case 3://忘记打卡
                tvMorningEndType.setText("状   态: 忘记打卡");
                tvMorningEndType.setTextColor(getResources().getColor(R.color.yell));
                break;
            case 4://补卡
                tvMorningEndType.setText("状   态: 补 卡");
                tvMorningEndType.setTextColor(getResources().getColor(R.color.blue));
                break;
            case 5://上午请假
                tvMorningEndType.setText("状   态: 请 假");
                tvMorningEndType.setTextColor(getResources().getColor(R.color.yell));
                break;
            default:
                break;
        }

        etMorningEndNote.setText("备注内容:" + attendancem.getMorning_end_note() == null ? "" : attendancem.getMorning_end_note());

        //下午上班
        tvAfternoonStartTime.setText(attendancem.getAfternoon_start_time() == null ? "打卡时间: " + "00:00" : "打卡时间: " + attendancem.getAfternoon_start_time());

        switch (attendancem.getAfternoon_start_type()) {
            case 0://正常
                tvAfternoonStartType.setText("状   态: 正常打卡");
                tvAfternoonStartType.setTextColor(getResources().getColor(R.color.green));
                break;
            case 1://迟到
                tvAfternoonStartType.setText("状   态: 迟到打卡");
                tvAfternoonStartType.setTextColor(getResources().getColor(R.color.red));
                break;
            case 2://早退打卡
                tvAfternoonStartType.setText("状   态: 早退打卡");
                tvAfternoonStartType.setTextColor(getResources().getColor(R.color.red));
                break;
            case 3://忘记打卡
                tvAfternoonStartType.setText("状   态: 忘记打卡");
                tvAfternoonStartType.setTextColor(getResources().getColor(R.color.yell));
                break;
            case 4://补卡
                tvAfternoonStartType.setText("状   态: 补 卡");
                tvAfternoonStartType.setTextColor(getResources().getColor(R.color.blue));
                break;
            case 5://下午请假
                tvAfternoonStartType.setText("状   态: 请 假");
                tvAfternoonStartType.setTextColor(getResources().getColor(R.color.yell));
                break;
            default:
                break;
        }

        etAfternoonStartNote.setText("备注内容:" + attendancem.getAfternoon_start_note() == null ? "" : attendancem.getAfternoon_start_note());


        //下午下班
        tvAfternoonEndTime.setText(attendancem.getAfternoon_end_time() == null ? "打卡时间: " + "00:00" : "打卡时间: " + attendancem.getAfternoon_end_time());

        switch (attendancem.getAfternoon_end_type()) {
            case 0://正常
                tvAfternoonEndType.setText("状   态: 正常打卡");
                tvAfternoonEndType.setTextColor(getResources().getColor(R.color.green));
                break;
            case 1://迟到
                tvAfternoonEndType.setText("状   态: 迟到打卡");
                tvAfternoonEndType.setTextColor(getResources().getColor(R.color.red));
                break;
            case 2://早退打卡
                tvAfternoonEndType.setText("状   态: 早退打卡");
                tvAfternoonEndType.setTextColor(getResources().getColor(R.color.red));
                break;
            case 3://忘记打卡
                tvAfternoonEndType.setText("状   态: 忘记打卡");
                tvAfternoonEndType.setTextColor(getResources().getColor(R.color.yell));
                break;
            case 4://补卡
                tvAfternoonEndType.setText("状   态: 补 卡");
                tvAfternoonEndType.setTextColor(getResources().getColor(R.color.blue));
                break;
            case 5://下午请假
                tvAfternoonEndType.setText("状   态: 请 假");
                tvAfternoonEndType.setTextColor(getResources().getColor(R.color.yell));
                break;

            default:
                break;
        }

        etAfternoonEndNote.setText(attendancem.getAfternoon_end_note() == null ? "" : attendancem.getAfternoon_end_note());


        //判断是否有罚款
        if (attendancem.isDeductions()) {
            rbYes.setChecked(true);
            rbNo.setChecked(false);
        } else {
            rbYes.setChecked(false);
            rbNo.setChecked(true);
        }

        etDeductions.setText(attendancem.getDeductions() + "");
        etDeductionsInfo.setText(attendancem.getDeductionsInfo() + "");


    }


    @OnClick({R.id.tv_morning_start_time_update, R.id.tv_morning_start_type_update, R.id.tv_morning_end_time_update, R.id.tv_morning_end_type_update, R.id.tv_afternoon_start_time_update, R.id.tv_afternoon_start_type_update, R.id.tv_afternoon_end_time_update, R.id.tv_afternoon_end_type_update, R.id.bt_save})
    public void onViewClicked(View view) {
        switch (view.getId()) {
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
                showProgressDialog("保存中..");

                attendancem.setMorningWorkType(spMorningAttenType.getSelectedItemPosition());
                attendancem.setAfternoonWorkType(spAfternoonAttenType.getSelectedItemPosition());

                attendancem.setMorning_start_note(etMorningStartNote.getText().toString());
                attendancem.setMorning_end_note(etMorningEndNote.getText().toString());
                attendancem.setAfternoon_start_note(etAfternoonStartNote.getText().toString());
                attendancem.setAfternoon_end_note(etAfternoonEndNote.getText().toString());

                if (rbYes.isChecked()) {
                    attendancem.setIsDeductions(true);
                } else {
                    attendancem.setIsDeductions(false);
                }

                try {
                    Double moni = Double.parseDouble(etDeductions.getText().toString());
                    attendancem.setDeductions(moni);
                    attendancem.setDeductionsInfo(etDeductionsInfo.getText().toString());

                    attendancem.save();

                    closeProgressDialog();
                    ToastUtil.showToast("保存成功!");

                    SystemLog.getInstance().AddLog("[管理员] 修改了[" + attendancem.getUser_Name() + "]" + "[" + attendancem.getDate() + " " + attendancem.getWeek() + "] 的考勤记录!");

                    FindAttenData();
                } catch (Exception e) {
                    ToastUtil.showToast("金额填写错误!");
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
     * 选择打卡状态
     */
    private String[] areas = new String[]{"正常打卡", "迟到打卡", "早退打卡", "补卡"};

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


}
