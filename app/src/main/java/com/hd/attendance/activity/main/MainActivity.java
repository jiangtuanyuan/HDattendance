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
import android.view.Menu;
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

/**
 * 考勤主界面
 */
public class MainActivity extends BaseActivity {
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.tv_show_date)
    TextView tvShowDate;
    @BindView(R.id.tv_show_time)
    TextView tvShowTime;
    @BindView(R.id.tv_letf)
    TextView tvLetf;
    @BindView(R.id.iv_left)
    ImageView ivLeft;
    @BindView(R.id.iv_right)
    ImageView ivRight;
    //考勤打卡显示的
    @BindView(R.id.tv_left_name)
    TextView tvLeftName;
    @BindView(R.id.tv_left_atten_type)
    TextView tvLeftAttenType;
    @BindView(R.id.tv_left_atten_time)
    TextView tvLeftAttenTime;
    @BindView(R.id.ll_left_layout)
    LinearLayout llLeftLayout;
    @BindView(R.id.ll_left_layout_info)
    LinearLayout llLeftLayoutinfo;


    @BindView(R.id.tv_left_info_top)
    TextView tvLeftInfoTop;
    @BindView(R.id.tv_atten_morning_start)
    TextView tvAttenMorningStart;
    @BindView(R.id.tv_atten_morning_end)
    TextView tvAttenMorningEnd;
    @BindView(R.id.tv_atten_afternoon_start)
    TextView tvAttenAfternoonStart;
    @BindView(R.id.tv_atten_afternoon_end)
    TextView tvAttenAfternoonEnd;
    //卫生
    @BindView(R.id.tv_health_Ausers)
    TextView tvHealthAusers;
    @BindView(R.id.tv_health_Ainfo)
    TextView tvHealthAinfo;
    @BindView(R.id.tv_health_Busers)
    TextView tvHealthBusers;
    @BindView(R.id.tv_health_Binfo)
    TextView tvHealthBinfo;


    //考勤打卡显示的

    //定时器
    private Disposable mDisposable;

    //指纹仪相关
    private static final int VID = 6997;
    private static final int PID = 288;
    private FingerprintSensor fingerprintSensor = null;
    private FingerprintCaptureListener FingerListener1, FingerListener2;

    //本地数据库指纹数据
    private List<FingerInfoTable> FingerList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        showHealth();
        openFingers();//启动监听 时时刻刻
        iniDBtoFinfer();

    }

    @Override
    protected void onStop() {
        super.onStop();
        stopFingers();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SystemLog.getInstance().AddLog("系统关闭");
        //取消显示时间的定时操作
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
        }

    }

    @Override
    protected void initVariables() {
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setTitle("考 勤 系 统");
        initToolbarIcon(R.mipmap.hd_logo);
        setSupportActionBar(mToolbar);

        SystemLog.getInstance().AddLog("系统启动");
    }

    @Override
    protected void initData() {
        startShowDate();
        iniFinger();//加载指纹仪
        tvShowDate.setText(DateUtils.getYearMonthDayWeek());
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_query_mannagement, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.query:
                ToastUtil.showToast("查询");
                return true;
            case R.id.manger:
                startActivity(new Intent(this, ManagementActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * 显示今天的卫生情况
     */
    private List<HealthTable> HList = new ArrayList<>();
    private HealthTable healthTable;

    private void showHealth() {
        int weekid = DateUtils.getWeek();
        HList.clear();
        HList.addAll(LitePal.where("weekid = ?", String.valueOf(weekid)).find(HealthTable.class));
        if (HList.size() > 0) {
            healthTable = HList.get(0);
            tvHealthAusers.setText("打扫人员:" + healthTable.getOnFloorUserName());
            tvHealthAinfo.setText("详细安排:" + healthTable.getOnFloorInfo());
            tvHealthBusers.setText("打扫人员:" + healthTable.getTwoFloorUserName());
            tvHealthBinfo.setText("详细安排:" + healthTable.getTwoFloorInfo());
        } else {
            tvHealthAusers.setText("打扫人员:无！");
            tvHealthAinfo.setText("详细安排:无！");
            tvHealthBusers.setText("打扫人员:无！");
            tvHealthBinfo.setText("详细安排:无！");
        }
    }

    /**
     * 弹出今天是 ？ 搞卫生的对话框！
     */
    private AlertDialog.Builder HealthDialog;

    private void showHealthDialog(String name) {
        if (HealthDialog == null) {
            HealthDialog = new AlertDialog.Builder(MainActivity.this);
            HealthDialog.setCancelable(false);
            HealthDialog.setPositiveButton("我知道了！", (dialog, which) -> {
                dialog.dismiss();
            });
        }
        HealthDialog.setTitle("[" + name + "] 今天轮到您打扫卫生!");
        HealthDialog.setMessage("今天轮到您打扫卫生,切记！切记！切记！\n 详情请看顶部通知栏！");
        HealthDialog.show();
    }

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
     * 启动时间效果CODE
     */
    private int ATTEN_CODE = 4;//默认禁止打卡

    // ATTEN_CODE值的说明: 控 制
    // 0：上午上班正常时间 在7:00 之后 8:45之前   上午正常: ATTEN_CODE 为 0
    // 1: 上午上班迟到    8:45之后 09:00 之前    上午迟到：ATTEN_CODE 为 1
    // 2: 上午上班早退    09:00 之后 12:00之前   上午上班早退卡: ATTEN_CODE 2
    // 3：上午下班打卡时间 在12:00之后 13:00之前  ATTEN_CODE为 3

    // 4：下午上班正常时间  在13:00之后 13:30之前  ATTEN_CODE为 4
    // 5: 下午上班迟到    13:30之后 14:00 之前    下午午迟到：ATTEN_CODE 为 5
    // 6:  下午上班早退   14:00 之后 18：00 之前   下午上班早退 ATTEN_CODE 为 6

    // 7：下午下班时间    在18:00之后 23:59之前  ATTEN_CODE为7

    // 8:禁止任何打卡时间  在00:00 之后 7:00之前  ATTEN_CODE为8
    private void startShowDate() {
        Observable.interval(1000, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable disposable) {
                        mDisposable = disposable;
                    }

                    @Override
                    public void onNext(@NonNull Long number) {
                        String time = DateUtils.getTime();
                        isCharmTime(time);
                        if (time.equals("00:00:00")) {
                            //更新卫生
                            showHealth();
                            //更新日期
                            tvShowDate.setText(DateUtils.getYearMonthDayWeek());
                        }
                        if (!TextUtils.isEmpty(time)) {
                            tvShowTime.setText(time);
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


/**---------------------------------------指纹仪相关 S--------------------------------------*/
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

        iniFingerA();
        iniFingerB();
    }

    /**
     * 添加指纹仪A监听
     */
    private boolean isFingerA = false;

    private void iniFingerA() {
        FingerListener1 = new FingerprintCaptureListener() {
            @Override
            public void captureOK(final byte[] fpImage) {
                final int width = fingerprintSensor.getImageWidth();
                final int height = fingerprintSensor.getImageHeight();
                runOnUiThread(() -> {
                    if (null != fpImage) {
                        ToolUtils.outputHexString(fpImage);//处理字节 生成Bitmap
                        ivLeft.setImageBitmap(ToolUtils.renderCroppedGreyScaleBitmap(fpImage, width, height));
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

                        String strRes[] = new String(bufids).split("\t");
                        //1.strRes[0] 是自己设置的标志 strRes[1] 是匹配度

                        String names[] = strRes[0].split("_");
                        //2.names[0] 是用户ID  names[1] 是用户姓名

                        //3.写入日志数据库
                        SystemLog.getInstance().AddLog(names[1] + "按下了考勤指纹仪!匹配阈值:" + strRes[1]);

                        //4.根据用户ID拿相关用户指纹数据
                        FingerInfoTable f = FingerUtils.getFingerinfo(names[0]);

                        if (f == null) {;;;
                            ToastUtil.showToast("未在指纹数据库找到[" + names[1] + "] 相关数据！");
                            isFingerA = false;
                        } else {
                            //设置相关信息
                            tvLeftName.setText("   姓 名 :" + f.getUser_Name());

                            if (f.getStauts() == 1) {
                                //判断指纹是否被禁用.
                                ToastUtil.showToast(names[1] + ",抱歉,您的指纹被禁用了!");
                                isFingerA = false;
                                return;
                            } else {

                                if (f.getIsdance()) {
                                    //6.拥有考勤打卡权限
                                    //1.先拿到用户今天的考勤这条记录
                                    AttendancemTable userAtten = MainUtils.AddAttendancem(Integer.parseInt(names[0]), names[1], DateUtils.getYMD());

                                    //2.判断时间类型
                                    switch (ATTEN_CODE) {
                                        case 0://上午正常
                                            if (!TextUtils.isEmpty(userAtten.getMorning_start_time())) {
                                                ToastUtil.showToast("[" + names[1] + "] 您上午上班已经打过卡了,打卡时间:" + userAtten.getMorning_start_time());
                                                //打过卡就显示信息
                                                //隐藏成功的布局 现在打卡信息的布局
                                                llLeftLayout.setVisibility(View.GONE);

                                            } else {
                                                userAtten.setMorning_start_time(DateUtils.getTimeHM());
                                                userAtten.setMorning_start_type(AttendType.NORMAL_CODE);//正常打卡
                                                userAtten.setMorning_start_note("无");
                                                userAtten.save();
                                                ToastUtil.showToast("[" + names[1] + "] 上午上班打卡成功!");

                                                isHealth(Integer.parseInt(names[0]));//判断是否今天搞卫生


                                                //设置界面
                                                tvLeftAttenType.setText("打开类型:[上午上班卡]");
                                                tvLeftAttenTime.setText("打卡时间：" + userAtten.getMorning_start_time());

                                                //记录进日志文件
                                                SystemLog.getInstance().AddLog("[" + names[1] + "] 上午上班打卡成功!");

                                                llLeftLayout.setVisibility(View.VISIBLE);
                                            }
                                            setThisAttendancem(userAtten);
                                            isFingerA = false;
                                            break;
                                        case 1://上午上班迟到

                                            if (!TextUtils.isEmpty(userAtten.getMorning_start_time())) {
                                                ToastUtil.showToast("[" + names[1] + "] 您上午上班已经打过卡了,打卡时间:" + userAtten.getMorning_start_time());
                                                isFingerA = false;

                                                llLeftLayout.setVisibility(View.GONE);
                                                setThisAttendancem(userAtten);
                                                return;
                                            }

                                            isFingerA = true;
                                            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
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
                                                llLeftLayout.setVisibility(View.VISIBLE);
                                                tvLeftAttenType.setText("打卡类型:[上午上班 迟到 卡]");
                                                tvLeftAttenTime.setText("打卡时间 :" + userAtten.getMorning_start_time());


                                                //记录进日志文件
                                                SystemLog.getInstance().AddLog("[" + names[1] + "] 上午上班 [迟到] 打卡成功!");
                                                isFingerA = false;


                                                setThisAttendancem(userAtten);
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
                                                llLeftLayout.setVisibility(View.GONE);
                                                setThisAttendancem(userAtten);
                                                return;
                                            }
                                            isFingerA = true;
                                            AlertDialog.Builder builderB = new AlertDialog.Builder(MainActivity.this);
                                            builderB.setMessage("上午下班时间未到,打卡算早退卡处理!");
                                            builderB.setTitle("[" + names[1] + "] 您是否继续打卡?");
                                            builderB.setCancelable(false);
                                            builderB.setPositiveButton("打卡", (dialog, which) -> {

                                                dialog.dismiss();

                                                userAtten.setMorning_end_time(DateUtils.getTimeHM());
                                                userAtten.setMorning_end_type(AttendType.LEAVE_CODE);//早退打卡
                                                userAtten.setMorning_end_note("早 退");
                                                userAtten.save();
                                                ToastUtil.showToast("[" + names[1] + "] 上午上班 [早退] 打卡成功!");


                                                //设置界面
                                                llLeftLayout.setVisibility(View.VISIBLE);
                                                tvLeftAttenType.setText("打开类型:[上午上班 早退 卡]");
                                                tvLeftAttenTime.setText("打卡时间：" + userAtten.getMorning_end_time());


                                                //记录进日志文件
                                                SystemLog.getInstance().AddLog("[" + names[1] + "] 上午上班 [早退] 打卡成功!");
                                                isFingerA = false;

                                                setThisAttendancem(userAtten);
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
                                                llLeftLayout.setVisibility(View.GONE);
                                                setThisAttendancem(userAtten);
                                                return;
                                            }
                                            userAtten.setMorning_end_time(DateUtils.getTimeHM());
                                            userAtten.setMorning_end_type(0);//正常打卡
                                            userAtten.setMorning_end_note("无");
                                            userAtten.save();
                                            ToastUtil.showToast("[" + names[1] + "] 上午下班打卡成功!");


                                            //设置界面
                                            llLeftLayout.setVisibility(View.VISIBLE);
                                            tvLeftAttenType.setText("打开类型:[上午下班卡]");
                                            tvLeftAttenTime.setText("打卡时间：" + userAtten.getMorning_end_time());


                                            //记录进日志文件
                                            SystemLog.getInstance().AddLog("[" + names[1] + "] 上午下班打卡成功!");

                                            setThisAttendancem(userAtten);
                                            isFingerA = false;
                                            break;
                                        case 4://下午上班正常时间
                                            if (!TextUtils.isEmpty(userAtten.getAfternoon_start_time())) {
                                                ToastUtil.showToast("[" + names[1] + "] 您下午上班已经打过卡了,打卡时间:" + userAtten.getAfternoon_start_time());
                                                llLeftLayout.setVisibility(View.GONE);
                                            } else {
                                                userAtten.setAfternoon_start_time(DateUtils.getTimeHM());
                                                userAtten.setAfternoon_start_type(AttendType.NORMAL_CODE);//正常打卡
                                                userAtten.setAfternoon_start_note("无");
                                                userAtten.save();

                                                ToastUtil.showToast("[" + names[1] + "] 下午上班打卡成功!");

                                                //设置界面
                                                llLeftLayout.setVisibility(View.VISIBLE);
                                                tvLeftAttenType.setText("打开类型:[下午上班卡]");
                                                tvLeftAttenTime.setText("打卡时间：" + userAtten.getAfternoon_start_time());

                                                //记录进日志文件
                                                SystemLog.getInstance().AddLog("[" + names[1] + "] 下午上班打卡成功!");
                                            }
                                            setThisAttendancem(userAtten);
                                            isFingerA = false;
                                            break;
                                        case 5://下午上班 迟到
                                            if (!TextUtils.isEmpty(userAtten.getAfternoon_start_time())) {
                                                ToastUtil.showToast("[" + names[1] + "] 您下午上班已经打过[迟到]卡了,打卡时间:" + userAtten.getMorning_start_time());
                                                isFingerA = false;
                                                llLeftLayout.setVisibility(View.GONE);
                                                setThisAttendancem(userAtten);
                                                return;
                                            }
                                            isFingerA = true;
                                            AlertDialog.Builder builderC = new AlertDialog.Builder(MainActivity.this);
                                            builderC.setMessage("您下午上班已经迟到了");
                                            builderC.setTitle("[" + names[1] + "] 您是否继续打卡?");
                                            builderC.setCancelable(false);
                                            builderC.setPositiveButton("打卡", (dialog, which) -> {
                                                dialog.dismiss();


                                                userAtten.setAfternoon_start_time(DateUtils.getTimeHM());
                                                userAtten.setAfternoon_start_type(AttendType.LATE_CODE);//迟到打卡
                                                userAtten.setAfternoon_start_note("迟 到");
                                                userAtten.save();
                                                ToastUtil.showToast("[" + names[1] + "] 上午上班 [迟到] 打卡成功!");


                                                //设置界面
                                                llLeftLayout.setVisibility(View.VISIBLE);
                                                tvLeftAttenType.setText("打开类型:[上午上班 迟到 打卡]");
                                                tvLeftAttenTime.setText("打卡时间：" + userAtten.getAfternoon_start_time());


                                                //记录进日志文件
                                                SystemLog.getInstance().AddLog("[" + names[1] + "] 上午上班 [迟到] 打卡成功! ");

                                                setThisAttendancem(userAtten);
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
                                                llLeftLayout.setVisibility(View.GONE);
                                                setThisAttendancem(userAtten);
                                                return;
                                            }
                                            isFingerA = true;
                                            AlertDialog.Builder builderD = new AlertDialog.Builder(MainActivity.this);
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
                                                llLeftLayout.setVisibility(View.VISIBLE);
                                                tvLeftAttenType.setText("打开类型:[下午下班 早退 卡]");
                                                tvLeftAttenTime.setText("打卡时间：" + userAtten.getAfternoon_end_time());

                                                //记录进日志文件
                                                SystemLog.getInstance().AddLog("[" + names[1] + "] 下午下班 [早退] 打卡成功!");

                                                setThisAttendancem(userAtten);

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
                                                llLeftLayout.setVisibility(View.GONE);
                                                setThisAttendancem(userAtten);
                                                return;
                                            }
                                            userAtten.setAfternoon_end_time(DateUtils.getTimeHM());
                                            userAtten.setAfternoon_end_type(AttendType.NORMAL_CODE);//正常打卡
                                            userAtten.setAfternoon_end_note("无");
                                            userAtten.save();
                                            ToastUtil.showToast("[" + names[1] + "] 下午下班打卡成功!");

                                            //设置界面
                                            llLeftLayout.setVisibility(View.VISIBLE);
                                            tvLeftAttenType.setText("打开类型:[下午下班卡]");
                                            tvLeftAttenTime.setText("打卡时间：" + userAtten.getAfternoon_end_time());

                                            //记录进日志文件
                                            SystemLog.getInstance().AddLog("[" + names[1] + "] 下午下班打卡成功!");

                                            setThisAttendancem(userAtten);

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
                        ToastUtil.showToast("抱歉,该指纹不存在,请重试!");
                    }


                });
            }

            @Override
            public void extractError(final int i) {
                runOnUiThread(() -> {
                    //记录进日志文件
                    SystemLog.getInstance().AddLog("[考勤指纹仪:] 录入异常:");
                });
            }
        };
    }

    /**
     * 添加指纹仪B监听
     */

    private void iniFingerB() {
        //指纹仪2 指纹采集回调
        FingerListener2 = new FingerprintCaptureListener() {
            @Override
            public void captureOK(final byte[] fpImage) {
                final int width = fingerprintSensor.getImageWidth();
                final int height = fingerprintSensor.getImageHeight();
                runOnUiThread(() -> {
                    if (null != fpImage) {
                        ToolUtils.outputHexString(fpImage);//处理字节
                        LogHelper.i("width=" + width + "\nHeight=" + height);//高度宽度
                        Bitmap bitmapFp = ToolUtils.renderCroppedGreyScaleBitmap(fpImage, width, height);
                        ivRight.setImageBitmap(bitmapFp);
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

    /**
     * 启动两个指纹仪绑定到服务上面
     */
    private void openFingers() {
        try {
            //打开指纹仪1
            fingerprintSensor.open(0);
            //设置监听 将监听绑定到指纹1上面 先绑定监听 在启动异步采集
            fingerprintSensor.setFingerprintCaptureListener(0, FingerListener1);
            //启动采集
            fingerprintSensor.startCapture(0);

            ToastUtil.showToast("考勤指纹仪启动成功!");
            SystemLog.getInstance().AddLog("考勤指纹仪启动成功!");
        } catch (FingerprintException f) {
            f.fillInStackTrace();
            ToastUtil.showToast("考勤指纹仪启动异常!");
            SystemLog.getInstance().AddLog("考勤指纹仪启动异常!");

        }

        try {

            //同上
            fingerprintSensor.open(1);
            fingerprintSensor.setFingerprintCaptureListener(1, FingerListener2);
            fingerprintSensor.startCapture(1);

            ToastUtil.showToast("就餐指纹仪启动成功!");
            SystemLog.getInstance().AddLog("就餐指纹仪启动成功!");

        } catch (FingerprintException f) {
            f.fillInStackTrace();
            ToastUtil.showToast("就餐指纹仪启动异常!");
            SystemLog.getInstance().AddLog("就餐指纹仪启动异常!");

        }
    }

    /**
     * 停止监听与采像
     */
    private void stopFingers() {
        try {
            //停止指纹仪1的异步取像
            fingerprintSensor.stopCapture(0);
            //停止指纹仪2的异步取像
            fingerprintSensor.stopCapture(1);
        } catch (FingerprintException e) {
            e.fillInStackTrace();
        }

    }

    /**
     * 设置今日打卡记录
     */
    private void setThisAttendancem(AttendancemTable attendancem) {
        tvLeftInfoTop.setText("[ " + attendancem.getUser_Name() + "] 今天打卡记录!");
        //上午上班卡
        if (!TextUtils.isEmpty(attendancem.getMorning_start_time())) {
            switch (attendancem.getMorning_start_type()) {
                case 0://正常
                    tvAttenMorningStart.setText("  上午上班卡: 已打(" + attendancem.getMorning_start_time() + ")");
                    tvAttenMorningStart.setTextColor(getResources().getColor(R.color.green));
                    break;
                case 1://迟到
                    tvAttenMorningStart.setText("  上午上班卡: 迟到(" + attendancem.getMorning_start_time() + ")");
                    tvAttenMorningStart.setTextColor(getResources().getColor(R.color.red));
                    break;
                case 2://早退打卡
                    tvAttenMorningStart.setText("  上午上班卡: 早退(" + attendancem.getMorning_start_time() + ")");
                    tvAttenMorningStart.setTextColor(getResources().getColor(R.color.red));
                    break;
                case 3://忘记打卡
                    tvAttenMorningStart.setText("  上午上班卡: 未打(" + attendancem.getMorning_start_time() + ")");
                    tvAttenMorningStart.setTextColor(getResources().getColor(R.color.red));
                    break;
                case 4:
                    tvAttenMorningStart.setText("  上午上班卡: 补卡(" + attendancem.getMorning_start_time() + ")");
                    tvAttenMorningStart.setTextColor(getResources().getColor(R.color.yell));
                    break;
                default:
                    break;
            }
        } else {
            tvAttenMorningStart.setText("  上午上班卡: 暂未打卡");
            tvAttenMorningStart.setTextColor(getResources().getColor(R.color.yell));
        }

        //上午下班
        if (!TextUtils.isEmpty(attendancem.getMorning_end_time())) {
            switch (attendancem.getMorning_end_type()) {
                case 0://正常
                    tvAttenMorningEnd.setText("  上午下班卡: 已打(" + attendancem.getMorning_end_time() + ")");
                    tvAttenMorningEnd.setTextColor(getResources().getColor(R.color.green));
                    break;
                case 1://迟到
                    tvAttenMorningEnd.setText("  上午下班卡: 迟到(" + attendancem.getMorning_end_time() + ")");
                    tvAttenMorningEnd.setTextColor(getResources().getColor(R.color.red));
                    break;
                case 2://早退打卡
                    tvAttenMorningEnd.setText("  上午下班卡: 早退(" + attendancem.getMorning_end_time() + ")");
                    tvAttenMorningEnd.setTextColor(getResources().getColor(R.color.red));
                    break;
                case 3://忘记打卡
                    tvAttenMorningEnd.setText("  上午下班卡: 未打(" + attendancem.getMorning_end_time() + ")");
                    tvAttenMorningEnd.setTextColor(getResources().getColor(R.color.red));
                    break;
                case 4:
                    tvAttenMorningEnd.setText("  上午下班卡: 补卡(" + attendancem.getMorning_end_time() + ")");
                    tvAttenMorningEnd.setTextColor(getResources().getColor(R.color.yell));
                    break;
                default:
                    break;
            }
        } else {
            tvAttenMorningEnd.setText("  上午下班卡: 暂未打卡");
            tvAttenMorningEnd.setTextColor(getResources().getColor(R.color.yell));
        }

        //下午上班卡
        if (!TextUtils.isEmpty(attendancem.getAfternoon_start_time())) {
            switch (attendancem.getAfternoon_start_type()) {
                case 0://正常
                    tvAttenAfternoonStart.setText("  上午上班卡: 已打(" + attendancem.getAfternoon_start_time() + ")");
                    tvAttenAfternoonStart.setTextColor(getResources().getColor(R.color.green));
                    break;
                case 1://迟到
                    tvAttenAfternoonStart.setText("  上午上班卡: 迟到(" + attendancem.getAfternoon_start_time() + ")");
                    tvAttenAfternoonStart.setTextColor(getResources().getColor(R.color.red));
                    break;
                case 2://早退打卡
                    tvAttenAfternoonStart.setText("  上午上班卡: 早退(" + attendancem.getAfternoon_start_time() + ")");
                    tvAttenAfternoonStart.setTextColor(getResources().getColor(R.color.red));
                    break;
                case 3://忘记打卡
                    tvAttenAfternoonStart.setText("  上午上班卡: 未打(" + attendancem.getAfternoon_start_time() + ")");
                    tvAttenAfternoonStart.setTextColor(getResources().getColor(R.color.red));
                    break;
                case 4:
                    tvAttenAfternoonStart.setText("  上午上班卡: 补卡(" + attendancem.getAfternoon_start_time() + ")");
                    tvAttenAfternoonStart.setTextColor(getResources().getColor(R.color.yell));
                    break;
                default:
                    break;
            }
        } else {
            tvAttenAfternoonStart.setText("  上午上班卡: 暂未打卡");
            tvAttenAfternoonStart.setTextColor(getResources().getColor(R.color.yell));
        }

        //下午下班
        if (!TextUtils.isEmpty(attendancem.getAfternoon_end_time())) {
            switch (attendancem.getAfternoon_end_type()) {
                case 0://正常
                    tvAttenAfternoonEnd.setText("  下午下班卡: 已打(" + attendancem.getAfternoon_end_time() + ")");
                    tvAttenAfternoonEnd.setTextColor(getResources().getColor(R.color.green));
                    break;
                case 1://迟到
                    tvAttenAfternoonEnd.setText("  下午下班卡: 迟到(" + attendancem.getAfternoon_end_time() + ")");
                    tvAttenAfternoonEnd.setTextColor(getResources().getColor(R.color.red));
                    break;
                case 2://早退打卡
                    tvAttenAfternoonEnd.setText("  下午下班卡: 早退(" + attendancem.getAfternoon_end_time() + ")");
                    tvAttenAfternoonEnd.setTextColor(getResources().getColor(R.color.red));
                    break;
                case 3://忘记打卡
                    tvAttenAfternoonEnd.setText("  下午下班卡: 未打(" + attendancem.getAfternoon_end_time() + ")");
                    tvAttenAfternoonEnd.setTextColor(getResources().getColor(R.color.red));
                    break;
                case 4:
                    tvAttenAfternoonEnd.setText("  下午下班卡: 补卡(" + attendancem.getAfternoon_end_time() + ")");
                    tvAttenAfternoonEnd.setTextColor(getResources().getColor(R.color.yell));
                    break;
                default:
                    break;
            }
        } else {
            tvAttenAfternoonEnd.setText("  下午下班卡: 暂未打卡");
            tvAttenAfternoonEnd.setTextColor(getResources().getColor(R.color.yell));
        }


    }


/**---------------------------------------指纹仪相关 END--------------------------------------*/


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


    /**
     * 时间效验
     *
     * @param Timestr
     */
    private void isCharmTime(String Timestr) {
        String[] tims = Timestr.split(":");
        if (tims.length == 3) {
            try {
                int h = Integer.parseInt(tims[0]);//时
                int m = Integer.parseInt(tims[1]);//分
                if (h > 0 && h < 7) {
                    //打开在7点之后开始 这段时间禁止打卡的
                    tvLetf.setText("00:00-7:00 禁止任何指纹操作！");

                    ATTEN_CODE = 8;
                    return;
                }

                //上午 打卡为 为0
                if (h < 8) {
                    tvLetf.setText("第一批次员工上班时间\n(打卡在08:00之前)");
                    ATTEN_CODE = 0;

                    return;
                }
                if (h == 8 && m <= 15) {
                    tvLetf.setText("第二批次员工上班时间\n(打卡在08:15之前)");

                    ATTEN_CODE = 0;
                    return;
                }
                if (h == 8 && m <= 30) {
                    tvLetf.setText("第三批次员工上班时间\n(打卡在08:30之前)");

                    ATTEN_CODE = 0;
                    return;
                }
                if (h == 8 && m <= 45) {
                    tvLetf.setText("第四批次员工上班时间\n(打卡在08:45之前)");

                    ATTEN_CODE = 0;
                    return;
                }
                if (h == 8 && m > 45) {
                    tvLetf.setText("上午上班打卡时间已结束!\n如果继续打卡算迟到处理!");

                    ATTEN_CODE = 1;//上午迟到为1
                    return;
                }
                if (h > 8 && h < 12) {
                    tvLetf.setText("上午上班时间!\n禁止打卡!\n否则算早退处理!");

                    ATTEN_CODE = 2;//上午早退为2
                    return;
                }


                //下午
                if (h == 12 && m <= 59) {
                    tvLetf.setText("上午下班时间,请打卡!");

                    ATTEN_CODE = 3;//上午下班 为 3
                    return;
                }

                if (h == 13 && m <= 30) {
                    tvLetf.setText("下午上班时间(13:30之前)\n请打卡!");

                    ATTEN_CODE = 4;//下午上班 为 4
                    return;
                }
                if (h == 13 && m > 30) {
                    tvLetf.setText("下午上班打卡时间已结束!\n如果继续打卡算迟到处理!");

                    ATTEN_CODE = 5;//下午迟到 为 5
                    return;
                }

                if (h > 13 && h < 18) {
                    tvLetf.setText("下午上班时间!\n禁止打卡!\n否则算早退处理!");

                    ATTEN_CODE = 6;//下午早退 为 6
                    return;
                }
                if (h >= 18) {
                    tvLetf.setText("下午下班时间,请打卡~");

                    ATTEN_CODE = 7;//下午下班 为 7
                    return;
                }


            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
    }

}
