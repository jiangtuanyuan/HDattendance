package com.hd.attendance.activity.main;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hd.attendance.R;
import com.hd.attendance.activity.ManagementActivity;
import com.hd.attendance.activity.attendancem.AttendType;
import com.hd.attendance.base.BaseActivity;
import com.hd.attendance.db.AttendancemTable;
import com.hd.attendance.db.FingerInfoTable;
import com.hd.attendance.db.HealthTable;
import com.hd.attendance.utils.DateUtils;
import com.hd.attendance.utils.FingerUtils;
import com.hd.attendance.utils.SystemLog;
import com.hd.attendance.utils.ToastUtil;
import com.zkteco.android.biometric.core.device.ParameterHelper;
import com.zkteco.android.biometric.core.device.TransportType;
import com.zkteco.android.biometric.core.utils.LogHelper;
import com.zkteco.android.biometric.core.utils.ToolUtils;
import com.zkteco.android.biometric.module.fingerprintreader.FingerprintCaptureListener;
import com.zkteco.android.biometric.module.fingerprintreader.FingerprintSensor;
import com.zkteco.android.biometric.module.fingerprintreader.FingprintFactory;
import com.zkteco.android.biometric.module.fingerprintreader.ZKFingerService;
import com.zkteco.android.biometric.module.fingerprintreader.exception.FingerprintException;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

public class HDMainActivity extends BaseActivity implements Toolbar.OnMenuItemClickListener {
    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    //头部卫生安排和日期周次的展示
    @BindView(R.id.tv_top_weeks)
    TextView tvTopWeeks;//【周一】
    @BindView(R.id.tv_date)
    TextView tvDate;//2018-11-09
    @BindView(R.id.tv_week)
    TextView tvWeek;//【第44周】

    //卫生安排
    @BindView(R.id.tv_health_A_user)
    TextView tvHealthAuser;//一楼打扫人员
    @BindView(R.id.tv_health_A_user_info)
    TextView tvHealthAuserInfo;//一楼打扫人员的详细安排
    @BindView(R.id.tv_health_B_user)
    TextView tvHealthBuser;//二楼打扫人员
    @BindView(R.id.tv_health_B_user_info)
    TextView tvHealthBuserInfo;//二楼打扫人员的详细安排

    //中间的提示部分
    @BindView(R.id.tv_center_time)
    TextView tvCenterTime;//中间的时间Time
    @BindView(R.id.iv_center_time_below)
    ImageView ivCenter_time_below;//时间下面的上下午的Logo
    @BindView(R.id.tv_center_attendance_prompt)
    TextView tvCenterAttenPrompt;//中间考勤打卡提示
    @BindView(R.id.tv_center_repast_prompt)
    TextView tvCenterRepastPrompt;//中间就餐提示

    //考勤打卡--指纹显示
    @BindView(R.id.iv_left_finger_logo)
    ImageView ivLeftFingerLogo;//默认显示的蓝色指纹logo
    @BindView(R.id.iv_left_user_finger)
    ImageView ivLeftUserFinger;//用户按下指纹需要显示的ImageView控件
    @BindView(R.id.tv_left_finger_state)
    TextView tvLeftFingetState;//考勤的指纹状态


    //考勤打卡--打卡成功
    @BindView(R.id.ll_center_left_success)
    LinearLayout llCenterLeftSuccess;//打卡成功的布局
    @BindView(R.id.tv_left_success_name)
    TextView tvLeftSuccessName;//打卡成功-姓名
    @BindView(R.id.tv_left_success_shift)
    TextView tvLeftSuccessShift;//打卡成功-打卡班次
    @BindView(R.id.tv_left_success_state)
    TextView tvLeftSuccessState;//打卡成功-打卡状态
    @BindView(R.id.tv_left_success_time)
    TextView tvLeftSuccessTime;//打卡成功-打卡时间

    //考勤打卡--今日打卡记录
    @BindView(R.id.ll_center_left_log)
    LinearLayout llCenterLeftLog;//今日打卡记录父布局
    @BindView(R.id.tv_left_log_name)
    TextView tvLeftLogName;//打卡记录-姓名
    @BindView(R.id.tv_left_log_morning_start)
    TextView tvLeftLogMorningStart;//打卡记录-上午上班打卡
    @BindView(R.id.tv_left_log_morning_end)
    TextView tvLeftLogMorningEnd;//打卡记录-上午下班打卡
    @BindView(R.id.tv_left_log_afternoon_start)
    TextView tvLeftLogAfternoonStart;//打卡记录-下午上班打卡
    @BindView(R.id.tv_left_log_afternoon_end)
    TextView tvLeftLogAfternoonEnd;//打卡记录-下午下班打卡

    //就餐打卡-指纹显示
    @BindView(R.id.iv_right_finger_logo)
    ImageView ivRightFingerLogo;//默认的黄色指纹的Logo
    @BindView(R.id.iv_right_user_finger)
    ImageView ivRightUserFinger;//用户按下就餐指纹仪显示的ImageView
    @BindView(R.id.tv_right_finger_state)
    TextView tvRightFingetState;//就餐的指纹状态

    //就餐打卡-打卡成功
    @BindView(R.id.tv_right_success_name)
    TextView tvRightSuccessName;//打卡成功-姓名
    @BindView(R.id.tv_right_success_type)
    TextView tvRightSuccessType;//打卡成功-打卡类型
    @BindView(R.id.tv_right_success_time)
    TextView tvRightSuccessTime;//打卡成功-打卡时间
    @BindView(R.id.ll_center_right_success)
    LinearLayout llCenterRightSuccess;//打卡成功的父布局

    //就餐打卡-今日就餐记录
    @BindView(R.id.tv_right_log_name)
    TextView tvRightLogName;//就餐记录-姓名
    @BindView(R.id.tv_right_log_afternoon_meal)
    TextView tvRightLogAfternoonMeal;//就餐记录-中午是否报餐
    @BindView(R.id.tv_right_log_afternoon_eat_meal)
    TextView tvRightLogAfternoonEatMeal;//就餐记录-中午是否就餐
    @BindView(R.id.tv_right_log_evening_meal)
    TextView tvRightLogEveningMeal;//就餐记录-晚边是否报餐
    @BindView(R.id.tv_right_log_evening_eat_meal)
    TextView tvRightLogEveningEatMeal;//就餐记录-晚边是否报餐
    @BindView(R.id.ll_center_right_log)
    LinearLayout llCenterRightLog;//就餐记录的父布局

    //今日搞卫生的人员
    private HealthTable healthTable;

    //定时器
    private Disposable mDisposable;
    private String Time = "";

    //考勤相关属性
    private int ATTEN_CODE = 4;//默认禁止打卡


    //就餐相关属性
    private int REPAST_CODE = 0;//1.中午报餐 2.中午就餐 3.晚边报餐 4.晚边就餐 0:禁止打卡

    //指纹仪相关
    private static final int VID = 6997;
    private static final int PID = 288;
    private FingerprintSensor fingerprintSensor;
    private FingerprintCaptureListener FingerListener1;//考勤指纹1监听
    private FingerprintCaptureListener FingerListener2;//就餐指纹仪监听

    //本地数据库指纹数据
    private List<FingerInfoTable> FingerList = new ArrayList<>();

    @Override
    protected void onResume() {
        super.onResume();
        MainUtils.showTopTime(tvTopWeeks, tvDate, tvWeek);
        healthTable = MainUtils.showHealth(tvHealthAuser, tvHealthAuserInfo, tvHealthBuser, tvHealthBuserInfo);
        openFingers();//启动监听 时时刻刻
        iniDBtoFinfer();//将本地数据库的指纹注册到指纹仪中
    }

    @Override
    protected void onStop() {
        super.onStop();
        stopFingers();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mDisposable != null && !mDisposable.isDisposed()) {
            mDisposable.dispose();
        }


        try {
            fingerprintSensor.stopCapture(0);
            fingerprintSensor.close(0);
            fingerprintSensor.stopCapture(1);
            fingerprintSensor.close(1);
            fingerprintSensor.destroy();//释放资源
        } catch (FingerprintException e) {
            e.printStackTrace();
            SystemLog.getInstance().AddLog("系统关闭!");
            SystemLog.getInstance().AddLog("指纹仪资源已释放！");
        }

    }

    @Override
    protected void initVariables() {

    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        setContentView(R.layout.activity_hdmain);
        ButterKnife.bind(this);
        setTitle("  黑 豆 考 勤 系 统");
        mToolbar.inflateMenu(R.menu.main_menu);
        mToolbar.setOnMenuItemClickListener(this);
        SystemLog.getInstance().AddLog("系统启动");
    }

    @Override
    protected void initData() {
        startTime();
        iniFinger();//加载指纹仪
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.menu_search:
                showHealthDialog("蒋团圆");
                return true;
            case R.id.menu_set:
                startActivity(new Intent(this, ManagementActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * 启动定时器 一直刷新时分秒
     */
    private void startTime() {
        Observable.interval(1000, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable disposable) {
                        mDisposable = disposable;
                    }

                    @Override
                    public void onNext(@NonNull Long number) {
                        Time = DateUtils.getTime();
                        ATTEN_CODE = MainUtils.isCharmTime(Time, tvCenterAttenPrompt, ivCenter_time_below);
                        if (Time.equals("00:00:00")) {
                            //更新卫生和头部的时间
                            MainUtils.showTopTime(tvTopWeeks, tvDate, tvWeek);
                            healthTable = MainUtils.showHealth(tvHealthAuser, tvHealthAuserInfo, tvHealthBuser, tvHealthBuserInfo);
                        }
                        if (!TextUtils.isEmpty(Time)) {
                            tvCenterTime.setText(Time);
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }


    /**
     * 按返回键，实现按home键
     *
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addCategory(Intent.CATEGORY_HOME);
            startActivity(intent);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

/*————————————————————————————指纹仪相关S——————————————————————————*/

    /**
     * 将数据库的指纹注册到指纹服务中
     */

    private void iniDBtoFinfer() {
        FingerList.clear();
        FingerList.addAll(LitePal.findAll(FingerInfoTable.class));

        for (FingerInfoTable f : FingerList) {
            try {
                ZKFingerService.save(f.getFinger(), f.getUser_ID() + "_" + f.getUser_Name());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 初始化指纹服务
     */
    private void iniFinger() {
        //设置日志等级
        LogHelper.setLevel(Log.WARN);
        Map<String, Object> fingerprintParams = new HashMap<>();
        //设置VID
        fingerprintParams.put(ParameterHelper.PARAM_KEY_VID, VID);
        //设置PID
        fingerprintParams.put(ParameterHelper.PARAM_KEY_PID, PID);
        fingerprintSensor = FingprintFactory.createFingerprintSensor(this, TransportType.USB, fingerprintParams);

        iniFingerA();//设置考勤指纹仪的监听事件
        iniFingerB();
    }


    /*——————————————考勤指纹仪————————————*/
    private boolean isFingerA = false;

    private void iniFingerA() {
        FingerListener1 = new FingerprintCaptureListener() {
            @Override
            public void captureOK(final byte[] fpImage) {
                int width = fingerprintSensor.getImageWidth();
                int height = fingerprintSensor.getImageHeight();
                //图片采集成功
                runOnUiThread(() -> {
                    if (null != fpImage) {
                        ToolUtils.outputHexString(fpImage);//处理字节 生成Bitmap
                        ivLeftFingerLogo.setVisibility(View.GONE);
                        ivLeftUserFinger.setVisibility(View.VISIBLE);
                        ivLeftUserFinger.setImageBitmap(ToolUtils.renderCroppedGreyScaleBitmap(fpImage, width, height));
                    }
                });
            }

            @Override
            public void extractOK(byte[] fpTemplate) {
                final byte[] tmpBuffer = fpTemplate;
                runOnUiThread(() -> {
                    if (ATTEN_CODE == 8) {
                        ToastUtil.showToast("抱歉,该段时间内禁止任何指纹操作!");
                        return;
                    }
                    if (isFingerA) {
                        return;
                    }
                    byte[] bufids = new byte[256];
                    int ret = ZKFingerService.identify(tmpBuffer, bufids, 55, 1);
                    if (ret > 0) {
                        isFingerA = true;

                        //1.strRes[0] 是自己设置的标志 strRes[1] 是匹配度
                        String strRes[] = new String(bufids).split("\t");

                        //2.names[0] 是用户ID  names[1] 是用户姓名
                        String names[] = strRes[0].split("_");

                        //3.写入日志数据库
                        SystemLog.getInstance().AddLog(names[1] + "按下了考勤指纹仪!匹配阈值:" + strRes[1]);

                        //4.根据用户ID拿相关用户指纹数据
                        FingerInfoTable f = FingerUtils.getFingerinfo(names[0]);

                        if (null == f) {
                            ToastUtil.showToast("未在指纹数据库找到[" + names[1] + "] 相关数据！");
                            isFingerA = false;
                        } else {
                            //5.设置今日打卡记录的相关信息
                            tvLeftLogName.setText("   姓 名 :" + f.getUser_Name());

                            //6.判断指纹是否被禁用

                            if (f.getStauts() == 1) {
                                //7.判断指纹是否被禁用.
                                ToastUtil.showToast(names[1] + ",抱歉,您的指纹被禁用了!");
                                isFingerA = false;
                                return;

                            } else {
                                //8.判断是否拥有考勤打卡的权限
                                if (f.getIsdance()) {
                                    //9.先拿到用户今天的考勤这条记录  没有就创建 有就拿唯一的过来
                                    AttendancemTable userAtten = MainUtils.AddAttendancem(Integer.parseInt(names[0]), names[1], DateUtils.getYMD());

                                    switch (ATTEN_CODE) {
                                        case 0://上午上班正常
                                            //判断是否打卡
                                            if (!TextUtils.isEmpty(userAtten.getMorning_start_time())) {
                                                ToastUtil.showToast("[" + names[1] + "] 您上午上班已经打过卡了,打卡时间:" + userAtten.getMorning_start_time());
                                                llCenterLeftSuccess.setVisibility(View.GONE);
                                            } else {
                                                //打卡
                                                userAtten.setMorning_start_time(DateUtils.getTimeHM());
                                                userAtten.setMorning_start_type(AttendType.NORMAL_CODE);//正常打卡
                                                userAtten.setMorning_start_note("无");
                                                userAtten.save();

                                                ToastUtil.showToast("[" + names[1] + "] 上午上班打卡成功!");
                                                //显示打卡成功的界面 设置相关信息
                                                llCenterLeftSuccess.setVisibility(View.VISIBLE);
                                                tvLeftSuccessName.setText(" 姓 名:" + f.getUser_Name());
                                                tvLeftSuccessShift.setText(" 打 卡 班 次:上午上班");
                                                tvLeftSuccessState.setText(" 打 卡 状 态:【正常】");
                                                tvLeftSuccessTime.setText(" 打 卡 时 间:" + userAtten.getMorning_start_time());

                                                //记录进日志文件
                                                SystemLog.getInstance().AddLog("[" + names[1] + "] 上午上班打卡成功!-【正常】");

                                            }
                                            isHealth(Integer.parseInt(names[0]));//判断改用户是否今天搞卫生
                                            MainUtils.showAttendance(userAtten,
                                                    tvLeftLogName,
                                                    tvLeftLogMorningStart,
                                                    tvLeftLogMorningEnd,
                                                    tvLeftLogAfternoonStart,
                                                    tvLeftLogAfternoonEnd);


                                            isFingerA = false;
                                            break;
                                        case 1://上午上班迟到
                                            if (!TextUtils.isEmpty(userAtten.getMorning_start_time())) {
                                                ToastUtil.showToast("[" + names[1] + "] 您上午上班已经打过卡了,打卡时间:" + userAtten.getMorning_start_time());
                                                isFingerA = false;
                                                llCenterLeftSuccess.setVisibility(View.GONE);
                                                return;
                                            }
                                            isFingerA = true;

                                            //弹出确定框
                                            AlertDialog.Builder builder = new AlertDialog.Builder(HDMainActivity.this);
                                            builder.setMessage("您上午上班已经迟到了");
                                            builder.setTitle("[" + names[1] + "] 您是否继续打卡?");
                                            builder.setCancelable(false);
                                            builder.setPositiveButton("打卡", (dialog, which) -> {
                                                dialog.dismiss();

                                                userAtten.setMorning_start_time(DateUtils.getTimeHM());
                                                userAtten.setMorning_start_type(AttendType.LATE_CODE);//迟到打卡
                                                userAtten.setMorning_start_note("迟 到");
                                                userAtten.save();
                                                ToastUtil.showToast("[" + names[1] + "] 上午上班 [迟到] 打卡成功!");

                                                //设置界面
                                                llCenterLeftSuccess.setVisibility(View.VISIBLE);
                                                tvLeftSuccessName.setText(" 姓 名:" + f.getUser_Name());
                                                tvLeftSuccessShift.setText(" 打 卡 班 次:上午上班");
                                                tvLeftSuccessState.setText(" 打 卡 状 态:【迟到】");
                                                tvLeftSuccessTime.setText(" 打 卡 时 间:" + userAtten.getMorning_start_time());


                                                //记录进日志文件
                                                SystemLog.getInstance().AddLog("[" + names[1] + "] 上午上班 【迟到】 打卡成功!");
                                                isFingerA = false;


                                                MainUtils.showAttendance(userAtten,
                                                        tvLeftLogName,
                                                        tvLeftLogMorningStart,
                                                        tvLeftLogMorningEnd,
                                                        tvLeftLogAfternoonStart,
                                                        tvLeftLogAfternoonEnd);
                                            });
                                            builder.setNegativeButton("取消", (dialog, which) -> {
                                                isFingerA = false;
                                                dialog.dismiss();
                                            });
                                            builder.show();

                                            break;
                                        case 2://上午上班早退
                                            if (!TextUtils.isEmpty(userAtten.getMorning_end_time())) {
                                                ToastUtil.showToast("[" + names[1] + "] 您上午下班已经打过 [早退] 卡了,打卡时间:" + userAtten.getMorning_end_time());
                                                isFingerA = false;
                                                llCenterLeftSuccess.setVisibility(View.GONE);
                                                MainUtils.showAttendance(userAtten,
                                                        tvLeftLogName,
                                                        tvLeftLogMorningStart,
                                                        tvLeftLogMorningEnd,
                                                        tvLeftLogAfternoonStart,
                                                        tvLeftLogAfternoonEnd);
                                                return;
                                            }
                                            isFingerA = true;
                                            AlertDialog.Builder builderB = new AlertDialog.Builder(HDMainActivity.this);
                                            builderB.setMessage("上午下班时间未到,打卡算早退卡处理!");
                                            builderB.setTitle("[" + names[1] + "] 您是否继续打卡?");
                                            builderB.setCancelable(false);
                                            builderB.setPositiveButton("打卡", (dialog, which) -> {

                                                dialog.dismiss();

                                                userAtten.setMorning_end_time(DateUtils.getTimeHM());
                                                userAtten.setMorning_end_type(AttendType.LEAVE_CODE);//早退打卡
                                                userAtten.setMorning_end_note("早 退");
                                                userAtten.save();
                                                ToastUtil.showToast("[" + names[1] + "] 上午下班 [早退] 打卡成功!");


                                                //设置界面
                                                llCenterLeftSuccess.setVisibility(View.VISIBLE);
                                                tvLeftSuccessName.setText(" 姓 名:" + f.getUser_Name());
                                                tvLeftSuccessShift.setText(" 打 卡 班 次:上午下班");
                                                tvLeftSuccessState.setText(" 打 卡 状 态:【早退】");
                                                tvLeftSuccessTime.setText(" 打 卡 时 间:" + userAtten.getMorning_end_time());


                                                //记录进日志文件
                                                SystemLog.getInstance().AddLog("[" + names[1] + "] 上午上班 [早退] 打卡成功!");
                                                isFingerA = false;

                                                MainUtils.showAttendance(userAtten,
                                                        tvLeftLogName,
                                                        tvLeftLogMorningStart,
                                                        tvLeftLogMorningEnd,
                                                        tvLeftLogAfternoonStart,
                                                        tvLeftLogAfternoonEnd);

                                            });
                                            builderB.setNegativeButton("取消", (dialog, which) -> {
                                                isFingerA = false;
                                                dialog.dismiss();
                                            });
                                            builderB.show();

                                            break;
                                        case 3://上午上下班 卡
                                            if (!TextUtils.isEmpty(userAtten.getMorning_end_time())) {
                                                ToastUtil.showToast("[" + names[1] + "] 您上午下班已经打过卡了,打卡时间:" + userAtten.getMorning_end_time());
                                                isFingerA = false;
                                                llCenterLeftSuccess.setVisibility(View.GONE);
                                                MainUtils.showAttendance(userAtten,
                                                        tvLeftLogName,
                                                        tvLeftLogMorningStart,
                                                        tvLeftLogMorningEnd,
                                                        tvLeftLogAfternoonStart,
                                                        tvLeftLogAfternoonEnd);
                                                return;
                                            }
                                            userAtten.setMorning_end_time(DateUtils.getTimeHM());
                                            userAtten.setMorning_end_type(AttendType.NORMAL_CODE);//正常打卡
                                            userAtten.setMorning_end_note("无");
                                            userAtten.save();
                                            ToastUtil.showToast("[" + names[1] + "] 上午下班打卡成功!");

                                            //设置界面
                                            llCenterLeftSuccess.setVisibility(View.VISIBLE);
                                            tvLeftSuccessName.setText(" 姓 名:" + f.getUser_Name());
                                            tvLeftSuccessShift.setText(" 打 卡 班 次:上午下班");
                                            tvLeftSuccessState.setText(" 打 卡 状 态:【正常】");
                                            tvLeftSuccessTime.setText(" 打 卡 时 间:" + userAtten.getMorning_end_time());

                                            //记录进日志文件
                                            SystemLog.getInstance().AddLog("[" + names[1] + "] 上午下班打卡成功!");

                                            MainUtils.showAttendance(userAtten,
                                                    tvLeftLogName,
                                                    tvLeftLogMorningStart,
                                                    tvLeftLogMorningEnd,
                                                    tvLeftLogAfternoonStart,
                                                    tvLeftLogAfternoonEnd);

                                            isFingerA = false;

                                            break;
                                        case 4://下午上班正常时间
                                            if (!TextUtils.isEmpty(userAtten.getAfternoon_start_time())) {
                                                ToastUtil.showToast("[" + names[1] + "] 您下午上班已经打过卡了,打卡时间:" + userAtten.getAfternoon_start_time());
                                                llCenterLeftSuccess.setVisibility(View.GONE);
                                            } else {
                                                userAtten.setAfternoon_start_time(DateUtils.getTimeHM());
                                                userAtten.setAfternoon_start_type(AttendType.NORMAL_CODE);//正常打卡
                                                userAtten.setAfternoon_start_note("无");
                                                userAtten.save();

                                                ToastUtil.showToast("[" + names[1] + "] 下午上班打卡成功!");

                                                //设置界面
                                                llCenterLeftSuccess.setVisibility(View.VISIBLE);
                                                tvLeftSuccessName.setText(" 姓 名:" + f.getUser_Name());
                                                tvLeftSuccessShift.setText(" 打 卡 班 次:下午上班");
                                                tvLeftSuccessState.setText(" 打 卡 状 态:【正常】");
                                                tvLeftSuccessTime.setText(" 打 卡 时 间:" + userAtten.getAfternoon_start_time());


                                                //记录进日志文件
                                                SystemLog.getInstance().AddLog("[" + names[1] + "] 下午上班打卡成功!");
                                            }
                                            MainUtils.showAttendance(userAtten,
                                                    tvLeftLogName,
                                                    tvLeftLogMorningStart,
                                                    tvLeftLogMorningEnd,
                                                    tvLeftLogAfternoonStart,
                                                    tvLeftLogAfternoonEnd);
                                            isFingerA = false;

                                            break;
                                        case 5://下午上班 迟到
                                            if (!TextUtils.isEmpty(userAtten.getAfternoon_start_time())) {
                                                ToastUtil.showToast("[" + names[1] + "] 您下午上班已经打过【迟到】卡了,打卡时间:" + userAtten.getMorning_start_time());
                                                isFingerA = false;
                                                llCenterLeftSuccess.setVisibility(View.GONE);
                                                MainUtils.showAttendance(userAtten,
                                                        tvLeftLogName,
                                                        tvLeftLogMorningStart,
                                                        tvLeftLogMorningEnd,
                                                        tvLeftLogAfternoonStart,
                                                        tvLeftLogAfternoonEnd);
                                                return;
                                            }
                                            isFingerA = true;
                                            AlertDialog.Builder builderC = new AlertDialog.Builder(HDMainActivity.this);
                                            builderC.setMessage("您下午上班已经迟到了");
                                            builderC.setTitle("[" + names[1] + "] 您是否继续打卡?");
                                            builderC.setCancelable(false);
                                            builderC.setPositiveButton("打卡", (dialog, which) -> {
                                                dialog.dismiss();


                                                userAtten.setAfternoon_start_time(DateUtils.getTimeHM());
                                                userAtten.setAfternoon_start_type(AttendType.LATE_CODE);//迟到打卡
                                                userAtten.setAfternoon_start_note("迟 到");
                                                userAtten.save();
                                                ToastUtil.showToast("[" + names[1] + "] 上午上班 【迟到】 打卡成功!");

                                                //设置界面
                                                llCenterLeftSuccess.setVisibility(View.VISIBLE);
                                                tvLeftSuccessName.setText(" 姓 名:" + f.getUser_Name());
                                                tvLeftSuccessShift.setText(" 打 卡 班 次:下午上班");
                                                tvLeftSuccessState.setText(" 打 卡 状 态:【迟到】");
                                                tvLeftSuccessTime.setText(" 打 卡 时 间:" + userAtten.getAfternoon_start_time());


                                                //记录进日志文件
                                                SystemLog.getInstance().AddLog("[" + names[1] + "] 上午上班 [迟到] 打卡成功! ");

                                                MainUtils.showAttendance(userAtten,
                                                        tvLeftLogName,
                                                        tvLeftLogMorningStart,
                                                        tvLeftLogMorningEnd,
                                                        tvLeftLogAfternoonStart,
                                                        tvLeftLogAfternoonEnd);

                                                isFingerA = false;
                                            });
                                            builderC.setNegativeButton("取消", (dialog, which) -> {
                                                isFingerA = false;
                                                dialog.dismiss();
                                            });
                                            builderC.show();

                                            break;
                                        case 6://下午早退
                                            if (!TextUtils.isEmpty(userAtten.getAfternoon_end_time())) {
                                                ToastUtil.showToast("[" + names[1] + "] 您下午下班已经打过[早退]卡了,打卡时间:" + userAtten.getAfternoon_end_time());
                                                isFingerA = false;
                                                llCenterLeftSuccess.setVisibility(View.GONE);
                                                MainUtils.showAttendance(userAtten,
                                                        tvLeftLogName,
                                                        tvLeftLogMorningStart,
                                                        tvLeftLogMorningEnd,
                                                        tvLeftLogAfternoonStart,
                                                        tvLeftLogAfternoonEnd);
                                                return;
                                            }
                                            isFingerA = true;
                                            AlertDialog.Builder builderD = new AlertDialog.Builder(HDMainActivity.this);
                                            builderD.setMessage("下午下班时间未到,打卡算早退卡处理!");
                                            builderD.setTitle("[" + names[1] + "] 您是否继续打卡?");
                                            builderD.setCancelable(false);
                                            builderD.setPositiveButton("打卡", (dialog, which) -> {
                                                dialog.dismiss();

                                                userAtten.setAfternoon_end_time(DateUtils.getTimeHM());
                                                userAtten.setAfternoon_end_type(AttendType.LEAVE_CODE);//早退
                                                userAtten.setAfternoon_end_note("早 退");
                                                userAtten.save();
                                                ToastUtil.showToast("[" + names[1] + "] 下午下班 [早退] 打卡成功!");

                                                //设置界面
                                                llCenterLeftSuccess.setVisibility(View.VISIBLE);
                                                tvLeftSuccessName.setText(" 姓 名:" + f.getUser_Name());
                                                tvLeftSuccessShift.setText(" 打 卡 班 次:下午上班");
                                                tvLeftSuccessState.setText(" 打 卡 状 态:【早退】");
                                                tvLeftSuccessTime.setText(" 打 卡 时 间:" + userAtten.getAfternoon_end_time());


                                                //记录进日志文件
                                                SystemLog.getInstance().AddLog("[" + names[1] + "] 下午下班 [早退] 打卡成功!");

                                                MainUtils.showAttendance(userAtten,
                                                        tvLeftLogName,
                                                        tvLeftLogMorningStart,
                                                        tvLeftLogMorningEnd,
                                                        tvLeftLogAfternoonStart,
                                                        tvLeftLogAfternoonEnd);
                                                isFingerA = false;
                                            });
                                            builderD.setNegativeButton("取消", (dialog, which) -> {
                                                isFingerA = false;
                                                dialog.dismiss();
                                            });
                                            builderD.show();

                                            break;
                                        case 7://下午正常下班
                                            if (!TextUtils.isEmpty(userAtten.getAfternoon_end_time())) {
                                                ToastUtil.showToast("[" + names[1] + "] 您下午下班已经打过卡了,打卡时间:" + userAtten.getAfternoon_end_time());
                                                isFingerA = false;
                                                llCenterLeftSuccess.setVisibility(View.GONE);
                                                MainUtils.showAttendance(userAtten,
                                                        tvLeftLogName,
                                                        tvLeftLogMorningStart,
                                                        tvLeftLogMorningEnd,
                                                        tvLeftLogAfternoonStart,
                                                        tvLeftLogAfternoonEnd);
                                                return;
                                            }
                                            userAtten.setAfternoon_end_time(DateUtils.getTimeHM());
                                            userAtten.setAfternoon_end_type(AttendType.NORMAL_CODE);//正常打卡
                                            userAtten.setAfternoon_end_note("无");
                                            userAtten.save();
                                            ToastUtil.showToast("[" + names[1] + "] 下午下班打卡成功!");

                                            //设置界面
                                            llCenterLeftSuccess.setVisibility(View.VISIBLE);
                                            tvLeftSuccessName.setText(" 姓 名:" + f.getUser_Name());
                                            tvLeftSuccessShift.setText(" 打 卡 班 次:下午上班");
                                            tvLeftSuccessState.setText(" 打 卡 状 态:【正常】");
                                            tvLeftSuccessTime.setText(" 打 卡 时 间:" + userAtten.getAfternoon_end_time());


                                            //记录进日志文件
                                            SystemLog.getInstance().AddLog("[" + names[1] + "] 下午下班打卡成功!");

                                            MainUtils.showAttendance(userAtten,
                                                    tvLeftLogName,
                                                    tvLeftLogMorningStart,
                                                    tvLeftLogMorningEnd,
                                                    tvLeftLogAfternoonStart,
                                                    tvLeftLogAfternoonEnd);
                                            isFingerA = false;

                                            break;
                                        case 8:
                                            ToastUtil.showToast("抱歉,该段时间内禁止任何指纹操作!");
                                            break;
                                        default:
                                            break;

                                    }

                                } else {
                                    isFingerA = false;
                                    ToastUtil.showToast(names[1] + ",抱歉,您的指纹无考勤打卡权限！");
                                }
                            }
                        }
                    } else {
                        isFingerA = false;
                        ToastUtil.showToast("抱歉,该枚指纹不存在,请重试!");
                    }

                });
            }

            @Override
            public void captureError(FingerprintException e) {

            }

            @Override
            public void extractError(int i) {
            }
        };
    }


    /*——————————————就餐指纹仪————————————*/
    private void iniFingerB() {
        //指纹仪2 指纹采集回调
        FingerListener2 = new FingerprintCaptureListener() {
            @Override
            public void captureOK(final byte[] fpImage) {
                int width = fingerprintSensor.getImageWidth();
                int height = fingerprintSensor.getImageHeight();

                runOnUiThread(() -> {
                    if (null != fpImage) {
                        ToolUtils.outputHexString(fpImage);//处理字节
                        Bitmap bitmapFp = ToolUtils.renderCroppedGreyScaleBitmap(fpImage, width, height);

                        ivRightFingerLogo.setVisibility(View.GONE);
                        ivRightUserFinger.setVisibility(View.VISIBLE);
                        ivRightUserFinger.setImageBitmap(bitmapFp);
                    }
                });
            }

            @Override
            public void captureError(FingerprintException e) {
            }

            @Override
            public void extractOK(byte[] fpTemplate) {
                final byte[] tmpBuffer = fpTemplate;
                runOnUiThread(() -> {
                    byte[] bufids = new byte[256];
                    int ret = ZKFingerService.identify(tmpBuffer, bufids, 55, 1);
                    if (ret > 0) {
                        String strRes[] = new String(bufids).split("\t");

                        String names[] = strRes[0].split("_");//0 为ID 1未姓名
                        // ToastUtil.showToast(names[1] + ":阈值(" + strRes[1] + ")");

                        SystemLog.getInstance().AddLog(names[1] + "按下了就餐指纹仪!匹配阈值:" + strRes[1]);

                        FingerInfoTable f = FingerUtils.getFingerinfo(names[0]);
                        assert f != null;
                        if (f.getStauts() == 1) {
                            ToastUtil.showToast(names[1] + ",您的指纹被禁用了!");
                            return;
                        } else {
                            //判断指纹仪是否用有就打卡的权限
                            if (f.getIsmeal()) {
                                //拥有考勤打卡权限
                                ToastUtil.showToast("滴," + names[1] + "就餐打卡成功!");
                                SystemLog.getInstance().AddLog(names[1] + "就餐打卡成功!");
                            } else {
                                ToastUtil.showToast(names[1] + ",您的指纹无就餐打卡权限！");
                            }
                        }
                    } else {
                        ToastUtil.showToast("就餐指纹仪识别失败,请重试!");
                    }
                });
            }

            @Override
            public void extractError(final int i) {

            }
        };
    }


    /*——————————————启动两个指纹仪绑定到服务上面————————————*/
    private void openFingers() {
        try {
            //打开指纹仪1
            fingerprintSensor.open(0);
            //设置监听 将监听绑定到指纹1上面 先绑定监听 在启动异步采集
            fingerprintSensor.setFingerprintCaptureListener(0, FingerListener1);
            //启动采集
            fingerprintSensor.startCapture(0);

            tvLeftFingetState.setText("启动正常!");
            SystemLog.getInstance().AddLog("考勤指纹仪启动成功!");
        } catch (FingerprintException f) {
            tvLeftFingetState.setText("启动异常!");

            SystemLog.getInstance().AddLog("考勤指纹仪启动异常!FingerprintException=" + f.getMessage());
        }

        try {

            //同上
            fingerprintSensor.open(1);
            fingerprintSensor.setFingerprintCaptureListener(1, FingerListener2);
            fingerprintSensor.startCapture(1);

            tvRightFingetState.setText("启动正常!");
            SystemLog.getInstance().AddLog("就餐指纹仪启动成功!");
        } catch (FingerprintException f) {
            tvRightFingetState.setText("启动异常!");
            SystemLog.getInstance().AddLog("就餐指纹仪启动异常!FingerprintException=" + f.getMessage());
        }
    }

    /*——————————————停止两个指纹仪的取像————————————*/
    private void stopFingers() {
        try {
            //停止指纹仪1的异步取像 不关闭指纹仪
            fingerprintSensor.stopCapture(0);
            //停止指纹仪2的异步取像
            fingerprintSensor.stopCapture(1);
        } catch (FingerprintException e) {
            SystemLog.getInstance().AddLog("考勤指纹仪启动异常!FingerprintException=" + e.getMessage());
        }

    }
/*————————————————————————————指纹仪相关E——————————————————————————*/


    /**
     * 判断用户ID是否是今天搞卫生的
     *
     * @param userID
     */
    private void isHealth(int userID) {
        if (healthTable != null) {
            try {
                String aids = healthTable.getOnFloorUserID();
                String[] aid = aids.split(",");
                String[] aname = healthTable.getOnFloorUserName().split("、");
                if (aid.length == aname.length) {
                    for (int i = 0; i < aid.length; i++) {
                        if (userID == Integer.parseInt(aid[i])) {
                            showHealthDialog(aname[i]);
                            break;
                        }
                    }
                }

                String bids = healthTable.getTwoFloorUserID();
                String[] bid = bids.split(",");
                String[] bname = healthTable.getTwoFloorUserName().split("、");
                if (bid.length == bname.length) {
                    for (int i = 0; i < bid.length; i++) {
                        if (userID == Integer.parseInt(bid[i])) {
                            showHealthDialog(bname[i]);
                            break;
                        }
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 弹出今天是谁搞卫生的对话框！
     */
    private AlertDialog.Builder HealthDialog;

    private void showHealthDialog(String name) {
        if (HealthDialog == null) {
            HealthDialog = new AlertDialog.Builder(HDMainActivity.this);
            HealthDialog.setCancelable(false);
            HealthDialog.setPositiveButton("我知道了", (dialog, which) -> {
                dialog.dismiss();
            });
        }
        HealthDialog.setTitle("[" + name + "] 今天轮到您打扫卫生!");
        HealthDialog.setMessage("今天轮到您打扫卫生!\n今天轮到您打扫卫生!\n今天轮到您打扫卫生!\n 切记!\n 详情请查看顶部通知栏！");
        HealthDialog.show();
    }


}
