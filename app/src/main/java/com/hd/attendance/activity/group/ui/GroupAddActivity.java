package com.hd.attendance.activity.group.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.hd.attendance.R;
import com.hd.attendance.activity.group.user.UserBean;
import com.hd.attendance.activity.group.user.UserChooseActivity;
import com.hd.attendance.base.BaseActivity;
import com.hd.attendance.db.EmployeesTable;
import com.hd.attendance.db.GroupTable;
import com.hd.attendance.utils.ToastUtil;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class GroupAddActivity extends BaseActivity {


    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.et_group_name)
    EditText etGroupName;
    @BindView(R.id.tv_group_boss)
    TextView tvGroupBoss;
    @BindView(R.id.tv_group_boss_choose)
    TextView tvGroupBossChoose;
    @BindView(R.id.tv_groups)
    TextView tvGroups;
    @BindView(R.id.tv_groups_choose)
    TextView tvGroupsChoose;
    @BindView(R.id.bt_save)
    Button btSave;



    private List<GroupTable> groupList = new ArrayList<>();

    @Override
    protected void initVariables() {

    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        setContentView(R.layout.activity_group_add);
        ButterKnife.bind(this);
        initToolbarNav();
        setTitle("新增小组");
    }

    @Override
    protected void initData() {


    }


    @OnClick({R.id.tv_group_boss_choose, R.id.tv_groups_choose, R.id.bt_save})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_group_boss_choose:
                Intent intent = new Intent(this, UserChooseActivity.class);
                intent.putExtra("mChooseNums", "single");
                startActivityForResult(intent, 100);
                break;
            case R.id.tv_groups_choose:
                Intent groupsIn = new Intent(this, UserChooseActivity.class);
                groupsIn.putExtra("mChooseNums", "more");
                startActivityForResult(groupsIn, 101);
                break;
            case R.id.bt_save:
                String groupName = etGroupName.getText().toString()
                        .replace(" ", "");
                if (TextUtils.isEmpty(groupName)) {
                    ToastUtil.showToast("名称必填!");
                    return;
                }

                if (mCheckListS.size() == 0) {
                    ToastUtil.showToast("请选择组长!");
                    return;
                }

                showProgressDialog("正在新增小组",false);
                GroupTable groupTable = new GroupTable();
                groupTable.setGroupName(groupName);
                groupTable.setGroupBoss_ID(mCheckListS.get(0).getUser_id());
                groupTable.setGroupBoss(mCheckListS.get(0).getUser_name());
                groupTable.save();

                if (mCheckListM.size() > 0) {
                    showProgressDialog("正在新增成员",false);
                    groupList.clear();
                    groupList.addAll(LitePal.findAll(GroupTable.class));

                    GroupTable maxG=groupList.get(groupList.size()-1);

                    Log.e("fafafafID",maxG.getGroupName()+"，"+maxG.getId());

                    mCheckListM.add(mCheckListS.get(0));
                    for(UserBean u:mCheckListM){
                        EmployeesTable em = new EmployeesTable();
                        em.setGroup_ID(maxG.getId());
                        em.update(u.getUser_id());
                    }

                } else {
                    ToastUtil.showToast("新增小组成功!");
                }
                closeProgressDialog();
                finish();
                break;
            default:
                break;
        }
    }

    private List<UserBean> mCheckListS = new ArrayList<>();
    private List<UserBean> mCheckListM = new ArrayList<>();

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
                    tvGroupBoss.setText(mCheckListS.get(0).getUser_name());
                }
            }
        }

        //选择的用户回调 多
        if (requestCode == 101 && resultCode == RESULT_OK) {
            if (data != null) {
                Bundle bundle = data.getExtras();
                mCheckListM.clear();
                mCheckListM.addAll((List<UserBean>) bundle.getSerializable("mCheckList"));
                tvGroups.setText("");
                StringBuffer stringBuffer = new StringBuffer();
                for (int i = 0; i < mCheckListM.size(); i++) {
                    if (i != mCheckListM.size() - 1) {
                        stringBuffer.append(mCheckListM.get(i).getUser_name()).append("、");
                    } else {
                        stringBuffer.append(mCheckListM.get(i).getUser_name());
                    }
                }
                tvGroups.setText(stringBuffer.toString());
            }
        }
    }
}
