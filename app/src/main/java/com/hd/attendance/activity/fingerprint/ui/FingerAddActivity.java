package com.hd.attendance.activity.fingerprint.ui;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.hd.attendance.R;
import com.hd.attendance.activity.fingerprint.chooseuser.FingerUserBean;
import com.hd.attendance.activity.fingerprint.chooseuser.FingerUserChooseActivity;
import com.hd.attendance.activity.group.user.UserBean;
import com.hd.attendance.activity.group.user.UserChooseActivity;
import com.hd.attendance.base.BaseActivity;
import com.hd.attendance.db.FingerInfoTable;
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

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 添加指纹
 */
public class FingerAddActivity extends BaseActivity {
    @BindView(R.id.tv_finger_user)
    TextView tvFingerUser;
    @BindView(R.id.tv_finger_user_choose)
    TextView tvFingerUserChoose;
    @BindView(R.id.iv_finger_one)
    ImageView ivFingerOne;
    @BindView(R.id.tv_finger_one_msg)
    TextView tvFingerOneMsg;
    @BindView(R.id.iv_finger_two)
    ImageView ivFingerTwo;
    @BindView(R.id.tv_finger_two_msg)
    TextView tvFingerTwoMsg;
    @BindView(R.id.iv_finger_three)
    ImageView ivFingerThree;
    @BindView(R.id.tv_finger_three_msg)
    TextView tvFingerThreeMsg;
    @BindView(R.id.iv_finger_merge)
    ImageView ivFingerMerge;
    @BindView(R.id.tv_finger_merge_msg)
    TextView tvFingerMergeMsg;
    @BindView(R.id.cb_finger_dance)
    CheckBox cbFingerDance;
    @BindView(R.id.cb_finger_meal)
    CheckBox cbFingerMeal;
    @BindView(R.id.bt_save)
    Button btSave;

    private final String TAG = "FingerAddActivity";

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopFingers();
    }

    @Override
    protected void initVariables() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        inDBtoFinfer();
    }

    /**
     * 将数据库的指纹注册到指纹仪中
     */
    private List<FingerInfoTable> FingerList = new ArrayList<>();

    private void inDBtoFinfer() {
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

    @Override
    protected void initViews(Bundle savedInstanceState) {
        setContentView(R.layout.activity_finger_add);
        ButterKnife.bind(this);
        initToolbarNav();
        setTitle("添加指纹");
        iniFinger();
    }

    @Override
    protected void initData() {

    }

    @OnClick({R.id.tv_finger_user_choose, R.id.bt_save})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_finger_user_choose:
                Intent fingerIntent = new Intent(this, FingerUserChooseActivity.class);
                startActivityForResult(fingerIntent, 101);

                break;
            case R.id.bt_save://保存指纹
                if (enrollidx != 3) {
                    ToastUtil.showToast("保存失败,请按操作进行指纹录入!");
                    return;
                }

                showProgressDialog("保存中..");
                //1.保存到指纹仪
                //2.保存到本地数据库
                FingerInfoTable fingerInfoTable = new FingerInfoTable();
                fingerInfoTable.setUser_ID(mCheckListS.get(0).getUser_id());
                fingerInfoTable.setUser_Name(mCheckListS.get(0).getUser_name());
                fingerInfoTable.setFinger(lastRegTemp);
                fingerInfoTable.setFinger_1(regtemparray[0]);
                fingerInfoTable.setFinger_2(regtemparray[1]);
                fingerInfoTable.setFinger_3(regtemparray[2]);
                if (cbFingerDance.isChecked()) {
                    fingerInfoTable.setIsdance(true);
                } else {
                    fingerInfoTable.setIsdance(false);
                }
                if (cbFingerMeal.isChecked()) {
                    fingerInfoTable.setIsdance(true);
                } else {
                    fingerInfoTable.setIsdance(false);
                }
                fingerInfoTable.save();


                ZKFingerService.save(lastRegTemp, mCheckListS.get(0).getUser_id() + "_" + mCheckListS.get(0).getUser_name());//ID_姓名

                closeProgressDialog();
                ToastUtil.showToast("保存成功!");

                SystemLog.getInstance().AddLog(fingerInfoTable.getUser_Name() + "添加了指纹!");
                finish();
                break;
            default:
                break;
        }
    }

    private List<FingerUserBean> mCheckListS = new ArrayList<>();

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //选择的用户回调 单
        if (requestCode == 101 && resultCode == RESULT_OK) {
            if (data != null) {
                Bundle bundle = data.getExtras();
                mCheckListS.addAll((List<FingerUserBean>) bundle.getSerializable("mCheckList"));
                if (mCheckListS.size() > 0) {
                    tvFingerUser.setText(mCheckListS.get(0).getUser_name());
                    //启动指纹仪
                    openFingers();

                }
            }
        }

    }

    /********************* 指纹仪操作 ************************/
    private static final int VID = 6997;
    private static final int PID = 288;
    private FingerprintSensor fingerprintSensor = null;
    private FingerprintCaptureListener FingerListener1, FingerListener2;

    private byte[][] regtemparray = new byte[3][2048];  //register template buffer array
    //注册需要三次
    private int enrollidx = 0;
    //三枚指纹合并之后的指纹
    private byte[] lastRegTemp = new byte[2048];


    /**
     * 初始化指纹仪 和事件回调
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

        //指纹仪1 指纹采集回调
        FingerListener1 = new FingerprintCaptureListener() {
            @Override
            public void captureOK(final byte[] fpImage) {
                final int width = fingerprintSensor.getImageWidth();
                final int height = fingerprintSensor.getImageHeight();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (null != fpImage) {
                            ToolUtils.outputHexString(fpImage);//处理字节
                            Log.e(TAG, "width=" + width + "\nHeight=" + height);//高度宽度
                            Bitmap bitmapFp = ToolUtils.renderCroppedGreyScaleBitmap(fpImage, width, height);

                            if (enrollidx == 0) {
                                ivFingerOne.setImageBitmap(bitmapFp);//第一次的图像
                                tvFingerOneMsg.setText("采集中..");
                            }
                            if (enrollidx == 1) {
                                ivFingerTwo.setImageBitmap(bitmapFp);//第二次的图像
                                tvFingerTwoMsg.setText("采集中..");
                            }
                            if (enrollidx == 2) {
                                ivFingerThree.setImageBitmap(bitmapFp);//第三次的图像
                                tvFingerThreeMsg.setText("采集中..");
                            }
                        }
                    }
                });
            }

            @Override
            public void captureError(FingerprintException e) {
            }

            @Override
            public void extractOK(final byte[] fpTemplate) {
                final byte[] tmpBuffer = fpTemplate;
                runOnUiThread(new Runnable() {
                    @TargetApi(Build.VERSION_CODES.FROYO)
                    @Override
                    public void run() {
                        byte[] bufids = new byte[256];
                        //1.检测该枚指纹是否被注册了
                        int ret = ZKFingerService.identify(tmpBuffer, bufids, 55, 1);
                        if (ret > 0) {
                            //表示该枚指纹被注册过
                            String strRes[] = new String(bufids).split("\t");
                            if (enrollidx == 0) {
                                tvFingerOneMsg.setText("该枚指纹被注册过了,被" + strRes[0] + "注册过,请重试~");
                                tvFingerOneMsg.setTextColor(getResources().getColor(R.color.red));

                            }
                            if (enrollidx == 1) {
                                tvFingerTwoMsg.setText("该枚指纹被注册过了,被" + strRes[0] + "注册过,请重试~");
                                tvFingerTwoMsg.setTextColor(getResources().getColor(R.color.red));

                            }
                            if (enrollidx == 2) {
                                tvFingerThreeMsg.setText("该枚指纹被注册过了,被" + strRes[0] + "注册过,请重试~");
                                tvFingerThreeMsg.setTextColor(getResources().getColor(R.color.red));
                            }
                            enrollidx = 0;
                            return;
                        }

                        //效验指纹是否合格
                        if (enrollidx > 0 && ZKFingerService.verify(regtemparray[enrollidx - 1], tmpBuffer) <= 0) {
                            ToastUtil.showToast("请再按一次!");
                            if (enrollidx == 0) {
                                tvFingerOneMsg.setText("此次指纹异常,请使用同一手指再按一次!");
                                tvFingerOneMsg.setTextColor(getResources().getColor(R.color.red));
                            }
                            if (enrollidx == 1) {
                                tvFingerTwoMsg.setText("此次指纹异常,请使用同一手指再按一次!");
                                tvFingerTwoMsg.setTextColor(getResources().getColor(R.color.red));

                            }
                            if (enrollidx == 2) {
                                tvFingerThreeMsg.setText("此次指纹异常,请使用同一手指再按一次!");
                                tvFingerThreeMsg.setTextColor(getResources().getColor(R.color.red));
                            }
                            return;
                        }

                        //2.记录指纹
                        System.arraycopy(tmpBuffer, 0, regtemparray[enrollidx], 0, 2048);
                        if (enrollidx == 0) {
                            tvFingerOneMsg.setText("成功!");
                            tvFingerOneMsg.setTextColor(getResources().getColor(R.color.green));
                        }
                        if (enrollidx == 1) {
                            tvFingerTwoMsg.setText("成功!");
                            tvFingerTwoMsg.setTextColor(getResources().getColor(R.color.green));
                        }
                        if (enrollidx == 2) {
                            tvFingerThreeMsg.setText("成功!");
                            tvFingerThreeMsg.setTextColor(getResources().getColor(R.color.green));
                        }
                        enrollidx++;

                        try {
                            if (enrollidx == 3) {
                                showProgressDialog("正在合并指纹,请稍后..");
                                byte[] regTemp = new byte[2048];
                                if (0 < (ret = ZKFingerService.merge(regtemparray[0], regtemparray[1], regtemparray[2], regTemp))) {
                                    //
                                    System.arraycopy(regTemp, 0, lastRegTemp, 0, ret);
                                    //显示合并之后的指纹图像

                                    ToastUtil.showToast("合并成功");
                                    tvFingerMergeMsg.setText("合并成功!");
                                    tvFingerMergeMsg.setTextColor(getResources().getColor(R.color.green));

                                    fingerprintSensor.stopCapture(0);//停止采集 成功之后

                                } else {
                                    tvFingerMergeMsg.setText("合并失败,请重试!");
                                    tvFingerMergeMsg.setTextColor(getResources().getColor(R.color.red));

                                    ivFingerOne.setImageResource(R.drawable.ic_main_finger);
                                    tvFingerOneMsg.setText("");
                                    ivFingerTwo.setImageResource(R.drawable.ic_main_finger);
                                    tvFingerTwoMsg.setText("");
                                    ivFingerThree.setImageResource(R.drawable.ic_main_finger);
                                    tvFingerThreeMsg.setText("");

                                    regtemparray = new byte[3][2048];
                                    //注册需要三次
                                    enrollidx = 0;
                                    //三枚指纹合并之后的指纹
                                    lastRegTemp = new byte[2048];
                                }

                                closeProgressDialog();
                            }

                        } catch (Exception e) {
                            tvFingerUser.setText(e.toString());
                        }

                    }
                });
            }

            @Override
            public void extractError(final int e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (enrollidx == 0) {
                            tvFingerOneMsg.setText("录入异常,请重试");
                        }
                        if (enrollidx == 1) {
                            tvFingerTwoMsg.setText("录入异常,请重试");
                        }
                        if (enrollidx == 2) {
                            tvFingerThreeMsg.setText("录入异常,请重试");
                        }
                        Log.e(TAG, "extract fail, errorcode:" + e);
                    }
                });
            }
        };
    }

    /**
     * 开启事件监听
     */
    private void openFingers() {
        regtemparray = new byte[3][2048];
        //注册需要三次
        enrollidx = 0;
        //三枚指纹合并之后的指纹
        lastRegTemp = new byte[2048];

        try {
            //打开指纹仪1
            fingerprintSensor.open(0);
            fingerprintSensor.setFingerprintCaptureListener(0, FingerListener1);
            //启动监听
            fingerprintSensor.startCapture(0);

            ToastUtil.showToast("指纹仪已经准备就绪,请开始录入指纹~");
        } catch (FingerprintException f) {
            f.fillInStackTrace();
            ToastUtil.showToast("指纹仪启动异常,请重新进入此模块!");
        }
    }

    /**
     * 停 止 指纹仪1采 集
     */
    private void stopFingers() {
        try {
            fingerprintSensor.stopCapture(0);

        } catch (FingerprintException e) {
            e.fillInStackTrace();
        }
    }


/********************* 指纹仪操作 ************************/

}
