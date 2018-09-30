package com.hd.attendance.activity.main;


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
import android.widget.ImageView;
import android.widget.TextView;

import com.hd.attendance.R;
import com.hd.attendance.activity.ManagementActivity;
import com.hd.attendance.activity.fingerprint.ui.FingerMainActivity;
import com.hd.attendance.base.BaseActivity;
import com.hd.attendance.db.EmployeesTable;
import com.hd.attendance.utils.DateUtils;
import com.hd.attendance.utils.MarqueTextView;
import com.hd.attendance.utils.SPUtils;
import com.hd.attendance.utils.ToastUtil;
import com.zkteco.android.biometric.core.device.ParameterHelper;
import com.zkteco.android.biometric.core.device.TransportType;
import com.zkteco.android.biometric.core.utils.LogHelper;
import com.zkteco.android.biometric.core.utils.ToolUtils;
import com.zkteco.android.biometric.module.fingerprintreader.FingerprintCaptureListener;
import com.zkteco.android.biometric.module.fingerprintreader.FingerprintSensor;
import com.zkteco.android.biometric.module.fingerprintreader.FingprintFactory;
import com.zkteco.android.biometric.module.fingerprintreader.exception.FingerprintException;

import java.util.HashMap;
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
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.iv_left)
    ImageView ivLeft;
    @BindView(R.id.iv_right)
    ImageView ivRight;

    @BindView(R.id.MTV_info)
    MarqueTextView MAT_INFO;
    //rxJava定时器
    private Disposable mDisposable;

    //指纹仪相关
    private static final int VID = 6997;
    private static final int PID = 288;
    private FingerprintSensor fingerprintSensor = null;
    private FingerprintCaptureListener FingerListener1, FingerListener2;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //取消显示时间的定时操作
        if (mDisposable != null && !mDisposable.isDisposed()) {
            mDisposable.dispose();
        }

        try {
            fingerprintSensor.stopCapture(0);
            fingerprintSensor.close(0);
            fingerprintSensor.stopCapture(1);
            fingerprintSensor.close(1);
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
        startShowDate();
        iniFinger();//加载指纹仪
        openFingers();//启动监听 时时刻刻
        tvShowDate.setText("当前日期:" + DateUtils.getYearMonthDayWeek());
    }

    @Override
    protected void onResume() {
        super.onResume();
        MAT_INFO.setText("今日卫生安排: " + SPUtils.getInstance().getString((DateUtils.getWeek()) + ""));
    }

    @Override
    protected void initData() {
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
     * 时间展示
     */
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
                        MainUtils.isCharmTime(time, tvLetf);
                        if (time.equals("00:00:00")) {
                            //更新卫生
                            MAT_INFO.setText("今日卫生安排: " + SPUtils.getInstance().getString((DateUtils.getWeek()) + ""));
                            //更新日期
                            tvShowDate.setText("当前日期:" + DateUtils.getYearMonthDayWeek());
                            ToastUtil.showToast("更新日期");
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
     * 初始化指纹仪 和事件回调
     */
    private void iniFinger() {
        //设置日志等级
        LogHelper.setLevel(Log.WARN);
        Map fingerprintParams = new HashMap();
        //设置VID
        fingerprintParams.put(ParameterHelper.PARAM_KEY_VID, VID);
        //设置PID
        fingerprintParams.put(ParameterHelper.PARAM_KEY_PID, PID);
        fingerprintSensor = FingprintFactory.createFingerprintSensor(this, TransportType.USB, fingerprintParams);

        //指纹仪1
        FingerListener1 = new FingerprintCaptureListener() {
            @Override
            public void captureOK(final byte[] fpImage) {
                final int width = fingerprintSensor.getImageWidth();
                final int height = fingerprintSensor.getImageHeight();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtil.showToast("指纹仪1信息采集中..");
                        if (null != fpImage) {
                            ToolUtils.outputHexString(fpImage);//处理字节
                            LogHelper.i("width=" + width + "\nHeight=" + height);//高度宽度
                            Bitmap bitmapFp = ToolUtils.renderCroppedGreyScaleBitmap(fpImage, width, height);
                            ivLeft.setImageBitmap(bitmapFp);
                        }
                    }
                });
            }

            @Override
            public void captureError(FingerprintException e) {
            }

            @Override
            public void extractOK(byte[] bytes) {
            }

            @Override
            public void extractError(final int i) {
            }
        };
        //指纹仪2
        FingerListener2 = new FingerprintCaptureListener() {
            @Override
            public void captureOK(final byte[] fpImage) {
                final int width = fingerprintSensor.getImageWidth();
                final int height = fingerprintSensor.getImageHeight();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtil.showToast("指纹仪2信息采集中..");
                        if (null != fpImage) {
                            ToolUtils.outputHexString(fpImage);//处理字节
                            LogHelper.i("width=" + width + "\nHeight=" + height);//高度宽度
                            Bitmap bitmapFp = ToolUtils.renderCroppedGreyScaleBitmap(fpImage, width, height);
                            ivRight.setImageBitmap(bitmapFp);
                        }
                    }
                });
            }

            @Override
            public void captureError(FingerprintException e) {
            }

            @Override
            public void extractOK(byte[] bytes) {
            }

            @Override
            public void extractError(final int i) {

            }
        };
    }

    /**
     * 开启指纹仪器1和2
     */
    private void openFingers() {
        try {
            //打开指纹仪1
            fingerprintSensor.open(0);
            fingerprintSensor.setFingerprintCaptureListener(0, FingerListener1);
            //启动监听
            fingerprintSensor.startCapture(0);

            //打开指纹仪2
            fingerprintSensor.open(1);
            fingerprintSensor.setFingerprintCaptureListener(1, FingerListener2);
            //启动监听
            fingerprintSensor.startCapture(1);
            ToastUtil.showToast("指纹仪已经启动!");
        } catch (FingerprintException f) {
            f.fillInStackTrace();
            ToastUtil.showToast("指纹仪未启动!");
        }
    }



}
