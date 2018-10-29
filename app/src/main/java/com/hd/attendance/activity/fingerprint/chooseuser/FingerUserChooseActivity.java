package com.hd.attendance.activity.fingerprint.chooseuser;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.hd.attendance.R;
import com.hd.attendance.activity.group.user.UserBean;
import com.hd.attendance.base.BaseActivity;
import com.hd.attendance.db.EmployeesTable;
import com.hd.attendance.db.FingerInfoTable;
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

public class FingerUserChooseActivity extends BaseActivity implements Toolbar.OnMenuItemClickListener, FingerUserChoosedapter.ItemClickListener {
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


    private List<FingerUserBean> mList = new ArrayList<>();
    private FingerUserChoosedapter adapter;

    private List<FingerUserBean> mSumList = new ArrayList<>();//用户查询总列表
    private List<FingerUserBean> mCheckList = new ArrayList<>();//已经选项的用户列表

    private List<EmployeesTable> EList = new ArrayList<>();
    private List<FingerInfoTable> FList = new ArrayList<>();

    @Override
    protected void initVariables() {

    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        setContentView(R.layout.activity_finger_user_choose);
        ButterKnife.bind(this);
        initToolbarNav();
        setTitle("选 择");

        EditTextChangedListenerUtlis.EditTextChangedListener(mSearchEdt, mSearchEdtClear);
    }

    @Override
    protected void initData() {
        mToolbar.inflateMenu(R.menu.menu_ok);
        mToolbar.setOnMenuItemClickListener(this);

        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(this);
        mRecyclerview.setLayoutManager(mLinearLayoutManager);
        adapter = new FingerUserChoosedapter(this, mList);
        adapter.setmItemClickListener(this);
        mRecyclerview.setAdapter(adapter);
        initDBData();//加载数据库的用户数据
    }

    /**
     * 加载数据库的用户数据
     */
    private void initDBData() {
        EList.addAll(LitePal.findAll(EmployeesTable.class));
        FList.addAll(LitePal.findAll(FingerInfoTable.class));

        for (EmployeesTable e : EList) {
            FingerUserBean userBean = new FingerUserBean();
            userBean.setUser_id(e.getId());
            userBean.setUser_name(e.getName());
            userBean.setUser_sex(e.getSex());
            userBean.setIs_finger(isFinger(e.getId()));
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

    /**
     * 判断指纹库里面是否存在这个员工的指纹ID
     *
     * @param UserID
     * @return
     */
    private boolean isFinger(int UserID) {
        for (FingerInfoTable f : FList) {
            if (f.getUser_ID() == UserID) {
                return true;
            }
        }
        return false;
    }


    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.ok://确定
                Bundle bundle = new Bundle();
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
    public void onItemClick(int position, FingerUserBean item) {
        for (int i = 0; i < mCheckList.size(); i++) {
            FingerUserBean user = mCheckList.get(i);
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
        for (FingerUserBean user : mSumList) {
            if (user.getUser_name().contains(search)) {
                mList.add(user);
            }
        }
        adapter.notifyDataSetChanged();
    }
}
