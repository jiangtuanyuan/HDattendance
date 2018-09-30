package com.hd.attendance.activity.group.user;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.hd.attendance.R;
import com.hd.attendance.base.BaseActivity;
import com.hd.attendance.db.EmployeesTable;
import com.hd.attendance.db.GroupTable;
import com.hd.attendance.utils.EditTextChangedListenerUtlis;
import com.hd.attendance.utils.ToastUtil;
import com.jakewharton.rxbinding2.widget.RxTextView;

import org.litepal.LitePal;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.functions.Consumer;

public class UserChooseActivity extends BaseActivity implements Toolbar.OnMenuItemClickListener, UserListAdapter.ItemClickListener {
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.search_edt)
    EditText mSearchEdt;
    @BindView(R.id.search_edt_clear)
    ImageView mSearchEdtClear;
    @BindView(R.id.search_img)
    ImageView searchImg;
    @BindView(R.id.ecyclerView)
    RecyclerView mRecyclerview;

    private String mChooseNums = "more";//单："single" 多:"more"   只能选择一个用户或者多个用户

    private UserListAdapter adapter;

    private List<UserBean> mList = new ArrayList<>();//用户显示选择列表
    private List<UserBean> mSumList = new ArrayList<>();//用户查询总列表
    private List<UserBean> mCheckList = new ArrayList<>();//已经选项的用户列表

    private List<GroupTable> groupList = new ArrayList<>();
    private List<EmployeesTable> employeesList = new ArrayList<>();


    @Override
    protected void initVariables() {
        if (getIntent().getExtras() != null) {
            Bundle bundle = getIntent().getExtras();
            mChooseNums = bundle.getString("mChooseNums");
        }
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        setContentView(R.layout.activity_user_choose);
        ButterKnife.bind(this);
        initToolbarNav();

        EditTextChangedListenerUtlis.EditTextChangedListener(mSearchEdt, mSearchEdtClear);
    }

    @Override
    protected void initData() {
        mToolbar.inflateMenu(R.menu.menu_ok);
        mToolbar.setOnMenuItemClickListener(this);

        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(this);
        mRecyclerview.setLayoutManager(mLinearLayoutManager);
        if (mChooseNums.equals("single")) {
            setTitle("单 选");
            adapter = new UserListAdapter(this, mList, true);
        } else {
            setTitle("多 选");
            adapter = new UserListAdapter(this, mList, false);
        }

        adapter.setmItemClickListener(this);
        mRecyclerview.setAdapter(adapter);

        initDBData();//加载数据库的用户数据
    }

    /**
     * 加载数据库的用户数据
     */
    private void initDBData() {
        groupList.addAll(LitePal.findAll(GroupTable.class));
        employeesList.addAll(LitePal.findAll(EmployeesTable.class));

        for (EmployeesTable e : employeesList) {
            UserBean userBean = new UserBean();
            userBean.setUser_id(e.getId());
            userBean.setUser_name(e.getName());
            userBean.setUser_sex(e.getSex());
            userBean.setJobs(e.getJobs());
            userBean.setGroup_id(e.getGroup_ID());
            userBean.setGroup_name(getDBGroupName(e.getGroup_ID()));
            mList.add(userBean);
            mSumList.add(userBean);
        }
        adapter.notifyDataSetChanged();


        RxTextView.textChanges(mSearchEdt)
                .subscribe(new Consumer<CharSequence>() {
                    @Override
                    public void accept(CharSequence charSequence) throws Exception {

                        if (charSequence.length() > 0) {
                            mSearchEdtClear.setVisibility(View.VISIBLE);
                            Search(charSequence.toString() + "");

                        } else {
                            mList.clear();
                            mList.addAll(mSumList);
                            adapter.notifyDataSetChanged();
                            mSearchEdtClear.setVisibility(View.INVISIBLE);
                        }
                    }
                });

    }

    private String getDBGroupName(int id) {
        for (GroupTable g : groupList) {
            if (g.getId() == id) {
                return g.getGroupName();
            }
        }
        return "无";
    }


    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.ok://确定
                Bundle bundle = new Bundle();
                bundle.putString("mChooseNums", mChooseNums);
                bundle.putSerializable("mCheckList", (Serializable) mCheckList);
                setResult(RESULT_OK, getIntent().putExtras(bundle));
                finish();
                break;
            case android.R.id.home:
                onBackPressed();
                break;
            default:
                break;
        }
        return false;
    }

    @Override
    public void onItemClick(int position, UserBean item) {
        for (int i = 0; i < mCheckList.size(); i++) {
            UserBean user = mCheckList.get(i);
            //判断是否存在
            if (user.getUser_id() == item.getUser_id()) {
                mCheckList.remove(i);
            }
        }
        //添加进选择的集合
        if (item.isChecked()) {
            mCheckList.add(item);
        }
    }

    /**
     * 在总数据里面搜索用户名mSumList
     */
    private void Search(String search) {
        mList.clear();
        for (UserBean user : mSumList) {
            if (user.getUser_name().contains(search)) {
                mList.add(user);
            }
        }
        adapter.notifyDataSetChanged();
    }
}
