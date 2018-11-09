package com.hd.attendance.activity.group.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.hd.attendance.R;
import com.hd.attendance.activity.fingerprint.adapter.FingerGroupAdapter;
import com.hd.attendance.activity.group.adapter.UserItemAdapter;
import com.hd.attendance.activity.group.user.UserBean;
import com.hd.attendance.activity.group.user.UserChooseActivity;
import com.hd.attendance.base.BaseActivity;
import com.hd.attendance.db.EmployeesTable;
import com.hd.attendance.db.GroupTable;
import com.hd.attendance.utils.SystemLog;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class GroupMainActivity extends BaseActivity implements FingerGroupAdapter.OnItemClickListener {
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.RecyclerView_Group)
    RecyclerView RecyclerViewGroup;
    @BindView(R.id.tv_group_name)
    TextView tvGroupName;
    @BindView(R.id.tv_group_boss)
    TextView tvGroupBoss;
    @BindView(R.id.tv_group_user)
    TextView tvGroupUser;
    @BindView(R.id.rv_group_user)
    RecyclerView mRecycler;
    @BindView(R.id.bt_add)
    Button btAdd;
    //小组横向滚动
    private FingerGroupAdapter GroupAdapter;
    private List<String> GropList = new ArrayList<>();
    private List<GroupTable> groupList = new ArrayList<>();
    private int GropuPotion = 0;
    //纵
    private List<EmployeesTable> emList = new ArrayList<>();
    private UserItemAdapter userItemAdapter;

    @Override
    protected void initVariables() {

    }


    @Override
    protected void initViews(Bundle savedInstanceState) {
        setContentView(R.layout.activity_group_main);
        ButterKnife.bind(this);
        initToolbarNav();
        setTitle("小组管理");
        setSupportActionBar(mToolbar);
        setGroupRecyclerw();
    }

    @Override
    protected void onResume() {
        super.onResume();
        groupList.clear();
        groupList.addAll(LitePal.findAll(GroupTable.class));

        if (groupList.size() == 0) {
            btAdd.setVisibility(View.GONE);
        }

        GropList.clear();
        for (GroupTable g : groupList) {
            GropList.add("#" + g.getGroupName());
        }
        GroupAdapter.notifyDataSetChanged();

    }

    @Override
    protected void initData() {
        groupList.clear();
        groupList.addAll(LitePal.findAll(GroupTable.class));
        if (groupList.size() > 0) {
            getGroupUsersInfo(groupList.get(0));
        } else {
            tvGroupName.setText("小 组 名 称:  无");
            tvGroupBoss.setText("小 组 组 长:  无");
        }

        GropList.clear();
        for (GroupTable g : groupList) {
            GropList.add("#" + g.getGroupName());
        }
        GroupAdapter.notifyDataSetChanged();


    }

    /**
     * 设置小组
     */
    private void setGroupRecyclerw() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        RecyclerViewGroup.setLayoutManager(layoutManager);
        GroupAdapter = new FingerGroupAdapter(GropList);
        GroupAdapter.setDefSelect(0);
        GroupAdapter.setOnItemClickListener(this);
        RecyclerViewGroup.setAdapter(GroupAdapter);


        mRecycler.setLayoutManager(new LinearLayoutManager(this));
        userItemAdapter = new UserItemAdapter(this, emList);
        mRecycler.setAdapter(userItemAdapter);
        mRecycler.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_emp_add, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.add:
                startActivity(new Intent(this, GroupAddActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onItemClickListener(View view, int position) {
        GroupAdapter.setDefSelect(position);
        this.GropuPotion = position;
        getGroupUsersInfo(groupList.get(position));
    }

    /**
     * 根据小组ID获取成员数据
     */

    private void getGroupUsersInfo(GroupTable groupTable) {
        tvGroupName.setText("小 组 名 称:  " + groupTable.getGroupName());
        tvGroupBoss.setText("小 组 组 长:  " + groupTable.getGroupBoss());

        emList.clear();
        emList.addAll(LitePal.where("group_ID=?", groupTable.getId() + "").find(EmployeesTable.class));
        userItemAdapter.notifyDataSetChanged();
    }

    @OnClick(R.id.bt_add)
    public void onViewClicked() {
        Intent intent = new Intent(this, UserChooseActivity.class);
        intent.putExtra("mChooseNums", "single");
        startActivityForResult(intent, 100);
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

                    EmployeesTable em = new EmployeesTable();
                    em.setGroup_ID(groupList.get(this.GropuPotion).getId());
                    em.update(mCheckListS.get(0).getUser_id());


                    SystemLog.getInstance().AddLog("管理员-将" + mCheckListS.get(0).getUser_name() + "加入了" + groupList.get(this.GropuPotion).getGroupName() + "小组");

                    emList.clear();
                    emList.addAll(LitePal.where("group_ID=?", groupList.get(this.GropuPotion).getId() + "").find(EmployeesTable.class));
                    userItemAdapter.notifyDataSetChanged();
                }
            }
        }
    }
}
