package com.huisu.iyoox.fragment.home;


import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huisu.iyoox.R;
import com.huisu.iyoox.activity.SearchActivity;
import com.huisu.iyoox.activity.student.StudentMsgActivity;
import com.huisu.iyoox.entity.GradeListModel;
import com.huisu.iyoox.entity.SubjectModel;
import com.huisu.iyoox.entity.User;
import com.huisu.iyoox.entity.base.BaseGradeListModel;
import com.huisu.iyoox.fragment.base.BaseFragment;
import com.huisu.iyoox.http.RequestCenter;
import com.huisu.iyoox.manager.UserManager;
import com.huisu.iyoox.okhttp.listener.DisposeDataListener;
import com.huisu.iyoox.util.LogUtil;
import com.huisu.iyoox.util.StatusBarUtil;
import com.huisu.iyoox.util.TabToast;
import com.huisu.iyoox.views.GradeDialog;
import com.huisu.iyoox.views.GradeNewDialog;
import com.huisu.iyoox.views.LocationIndicatorView;
import com.huisu.iyoox.views.SelectGradeDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: dl
 * @function: 主页fragment框架
 * @date: 18/6/28
 */
public class HomeFragment extends BaseFragment implements ViewPager.OnPageChangeListener, View.OnClickListener {

    private View view;
    private LocationIndicatorView mTabView;
    private ViewPager mViewPager;
    private TextView studentGradeTv;
    private List<SubjectModel> subjectModels = new ArrayList<>();
    private User user;
    private ArrayList<GradeListModel> gradeListModels = new ArrayList<>();
    public static GradeListModel selectModel;
    private int selectPageIndexof = 0;
    private View msgView;
    private View newMsgView;
    private View searchIv;
    private MyPagerAdapter myPagerAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_hone, container, false);
        user = UserManager.getInstance().getUser();
        initTab();
        initView();
        setEvent();
        initPage();
        postHomeData();
        postMsgNewData();
        return view;
    }

    /**
     * 初始化控件
     */
    private void initView() {
        studentGradeTv = view.findViewById(R.id.student_grade_tv);
        searchIv = view.findViewById(R.id.search_icon_iv);
        msgView = view.findViewById(R.id.home_fragment_msg_ll);
        mTabView = view.findViewById(R.id.fragment_home_tab_view);
        mViewPager = view.findViewById(R.id.fragment_home_page);
        newMsgView = view.findViewById(R.id.home_fragment_msg_hint_view);

        subjectModels.clear();
        SubjectModel subjectModel = new SubjectModel();
        subjectModel.setName("首页");
        subjectModels.add(subjectModel);
        mTabView.init(subjectModels);
        //初始化 学生年级
        studentGradeTv.setText(user.getGradeName() + "上");
    }

    /**
     * 获取年级列表和教材信息
     */
    private void postHomeData() {
        RequestCenter.indexList(new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) {
                BaseGradeListModel baseGradeListModel = (BaseGradeListModel) responseObj;
                if (baseGradeListModel.data != null && baseGradeListModel.data.size() > 0) {
                    gradeListModels.clear();
                    gradeListModels.addAll(baseGradeListModel.data);
                    for (int i = 0; i < gradeListModels.size(); i++) {
                        GradeListModel gradeListModel = gradeListModels.get(i);
                        //获取默认勾选的position
                        if (gradeListModel.getGrade_id() == user.getGrade() && gradeListModel.getGrade_detail_id() == 1) {
                            selectModel = gradeListModel;
                            break;
                        }
                    }
                    if (selectModel == null) {
                        selectModel = gradeListModels.get(0);
                    }
                    getSelectGradeListModel();
                } else {
                    TabToast.showMiddleToast(getContext(), baseGradeListModel.msg);
                }
            }

            @Override
            public void onFailure(Object reasonObj) {
            }
        });
    }

    private void postMsgNewData() {
        RequestCenter.getNewMessageCount(user.getUserId(), new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) {
                JSONObject jsonObject = (JSONObject) responseObj;
                try {
                    int count = jsonObject.getInt("data");
                    if (count > 0) {
                        newMsgView.setVisibility(View.VISIBLE);
                    } else {
                        newMsgView.setVisibility(View.INVISIBLE);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Object reasonObj) {

            }
        });
    }

    /**
     * 根据学生年级 默认的选取科目信息
     */
    private void getSelectGradeListModel() {
        initData();
        initPage();
    }


    /**
     * 初始化 数据
     */
    private void initData() {
        subjectModels.clear();
        subjectModels.addAll(selectModel.getKemuArr());
        for (SubjectModel model : subjectModels) {
            model.setSelect(false);
        }
        SubjectModel subjectModel = new SubjectModel();
        subjectModel.setName("首页");
        subjectModels.add(0, subjectModel);
        mTabView.init(subjectModels);
        //跳转到上次记录的tab
        if (subjectModels.size() > selectPageIndexof) {
            mTabView.setSelection(selectPageIndexof);
        }
    }

    /**
     * 初始化viewpager
     */
    private void initPage() {
        if (myPagerAdapter == null) {
            myPagerAdapter = new MyPagerAdapter(getChildFragmentManager(), subjectModels);
            mViewPager.setAdapter(myPagerAdapter);
        } else {
            myPagerAdapter.notifyDataSetChanged();
        }
        if (selectModel != null) {
            //多缓存一个首页的界面
            int fragmentCount = selectModel.getKemuArr().size() + 1;
            mViewPager.setOffscreenPageLimit(fragmentCount - 1);
            //跳转到上次记录的界面
            if (fragmentCount > selectPageIndexof) {
                mViewPager.setCurrentItem(selectPageIndexof);
                if (selectPageIndexof != 0) {
                    //刷新当前fragment数据
                    BaseFragment baseFragment = fragments.get(selectPageIndexof);
                    String gradeId = selectModel.getGrade_id() + "";
                    String gradeDetailId = selectModel.getGrade_detail_id() + "";
                    SubjectModel subjectModel = selectModel.getKemuArr().get(selectPageIndexof - 1);
                    baseFragment.updateArguments(gradeId, gradeDetailId, subjectModel);
                    baseFragment.onShow();
                }
            } else {
                selectPageIndexof = 0;
                mViewPager.setCurrentItem(0);
            }
        }
        if (fragments.size() > 0) {
            BaseFragment baseFragment = fragments.get(0);
            baseFragment.onShow();
        }

    }

    /**
     * 初始化点击时间
     */
    private void setEvent() {
        mViewPager.addOnPageChangeListener(this);
        searchIv.setOnClickListener(this);
        studentGradeTv.setOnClickListener(this);
        msgView.setOnClickListener(this);
        mTabView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mViewPager.setCurrentItem(mTabView.getSelection());
            }
        });
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        selectPageIndexof = position;
        mTabView.setSelection(position);
        BaseFragment baseFragment = fragments.get(position);
        if (position != 0) {
            String gradeId = selectModel.getGrade_id() + "";
            String gradeDetailId = selectModel.getGrade_detail_id() + "";
            SubjectModel subjectModel = selectModel.getKemuArr().get(position - 1);
            baseFragment.updateArguments(gradeId, gradeDetailId, subjectModel);
        }
        baseFragment.onShow();
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.student_grade_tv:
                if (gradeListModels.size() == 0) {
                    return;
                }
                if (selectModel == null) return;
                GradeNewDialog gradeDialog = new GradeNewDialog(getContext(), gradeListModels, selectModel) {
                    @Override
                    public void getGradePosition(GradeListModel model) {
                        HomeFragment.this.selectModel = model;
                        studentGradeTv.setText(selectModel.getName());
                        initData();
                        initPage();
                    }
                };
                gradeDialog.show();
                gradeDialog.setCanceledOnTouchOutside(true);
                break;
            case R.id.home_fragment_msg_ll:
                newMsgView.setVisibility(View.INVISIBLE);
                StudentMsgActivity.start(getContext());
                break;
            case R.id.search_icon_iv:
                SearchActivity.start(getContext());
                break;
            default:
                break;
        }
    }

    private ArrayList<BaseFragment> fragments = new ArrayList<>();

    /**
     * 自定义适配器
     */
    class MyPagerAdapter extends FragmentPagerAdapter {

        private List<SubjectModel> models;

        MyPagerAdapter(FragmentManager fm, List<SubjectModel> models) {
            super(fm);
            this.models = models;
        }

        @Override
        public BaseFragment getItem(int position) {
            BaseFragment bookFragment;
            if (position == 0) {
                bookFragment = new NewHomePageFragment();
            } else {
                String gradeId = selectModel.getGrade_id() + "";
                String gradeDetailId = selectModel.getGrade_detail_id() + "";
                SubjectModel subjectModel = selectModel.getKemuArr().get(position - 1);
                bookFragment = getFragment(gradeId, gradeDetailId, subjectModel);
            }
            fragments.add(bookFragment);
            return bookFragment;
        }

        @Override
        public int getCount() {
            return models == null ? 0 : models.size();
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            BaseFragment baseFragment = (BaseFragment) super.instantiateItem(container, position);
            if (position != 0) {
                SubjectModel subjectModel = selectModel.getKemuArr().get(position - 1);
                baseFragment.updateArguments(selectModel.getGrade_id() + "",
                        selectModel.getGrade_detail_id() + "", subjectModel
                );
            }
            return baseFragment;
        }

    }

    private BookFragment getFragment(String gradeId, String gradeDetailId, SubjectModel model) {
        BookFragment fragment = new BookFragment();
        Bundle b = new Bundle();
        b.putString("gradeId", gradeId);
        b.putString("gradeDetailId", gradeDetailId);
        b.putSerializable("model", model);
        fragment.setArguments(b);
        return fragment;
    }

    private void initTab() {
        View tabView = view.findViewById(R.id.tab_view);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            tabView.setVisibility(View.VISIBLE);
        } else {
            tabView.setVisibility(View.GONE);
        }
    }

}
