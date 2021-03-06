package com.huisu.iyoox.fragment.teacher;


import android.os.Build;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.huisu.iyoox.Interface.MyOnItemClickListener;
import com.huisu.iyoox.R;
import com.huisu.iyoox.activity.teacher.TeacherLookTaskDetailsActivity;
import com.huisu.iyoox.adapter.TeacherLookTaskListAdapter;
import com.huisu.iyoox.adapter.TeacherRemarkListAdapter;
import com.huisu.iyoox.constant.Constant;
import com.huisu.iyoox.entity.DianPingListModel;
import com.huisu.iyoox.entity.TaskTeacherListModel;
import com.huisu.iyoox.entity.User;
import com.huisu.iyoox.entity.base.BaseDianPingListModel;
import com.huisu.iyoox.fragment.base.BaseFragment;
import com.huisu.iyoox.http.RequestCenter;
import com.huisu.iyoox.manager.UserManager;
import com.huisu.iyoox.okhttp.listener.DisposeDataListener;
import com.huisu.iyoox.swipetoloadlayout.OnLoadMoreListener;
import com.huisu.iyoox.swipetoloadlayout.OnRefreshListener;
import com.huisu.iyoox.swipetoloadlayout.SwipeToLoadLayout;
import com.huisu.iyoox.util.TabToast;

import java.util.ArrayList;

/**
 * 老师端-点评Fragment
 */
public class TeacherRemarkFragment extends BaseFragment implements MyOnItemClickListener, OnLoadMoreListener, OnRefreshListener {

    private View view;
    private TextView titleTv;
    private RecyclerView mRecyclerView;
    private SwipeToLoadLayout swipeToLoadLayout;
    private TeacherRemarkListAdapter mAdapter;
    private int page = 1;
    private boolean init = false;
    private User user;
    private ArrayList<DianPingListModel> models = new ArrayList<>();
    private View emptyView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_teacher_correct, container, false);
        initTab();
        initView();
        setEvent();
        initData();
        return view;
    }

    private void initView() {
        titleTv = view.findViewById(R.id.title_bar_tv);
        swipeToLoadLayout = view.findViewById(R.id.swipeToLoadLayout);
        emptyView = view.findViewById(R.id.teacher_remark_empty_view);
        mRecyclerView = view.findViewById(R.id.swipe_target);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        mAdapter = new TeacherRemarkListAdapter(getContext(), models);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void setEvent() {
        swipeToLoadLayout.setOnLoadMoreListener(this);
        swipeToLoadLayout.setOnRefreshListener(this);
        mAdapter.setOnItemClickListener(this);
    }

    private void initData() {
        titleTv.setText("点评");
        user = UserManager.getInstance().getUser();
    }


    private void postTaskListHttp() {
        RequestCenter.teacherDianPingList(user.getUserId(), page + "", new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) {
                closeLoading();
                if (page == 1) {
                    models.clear();
                }
                BaseDianPingListModel baseModel = (BaseDianPingListModel) responseObj;
                if (baseModel.code == Constant.POST_SUCCESS_CODE) {
                    if (baseModel.data != null && baseModel.data.size() > 0) {
                        models.addAll(baseModel.data);
                        emptyView.setVisibility(View.GONE);
                    } else {
                        if (page != 1) {
                            page--;
                            TabToast.showMiddleToast(getContext(), "暂无更多数据");
                        } else {
                            emptyView.setVisibility(View.VISIBLE);
                        }
                    }
                }
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Object reasonObj) {
                closeLoading();
            }
        });

    }

    @Override
    public void onItemClick(int position, View view) {
        DianPingListModel model = models.get(position);
        TeacherLookTaskDetailsActivity.start(getContext(), model.getWork_id());
    }

    private void closeLoading() {
        if (swipeToLoadLayout != null) {
            swipeToLoadLayout.setLoadingMore(false);
            swipeToLoadLayout.setRefreshing(false);
        }
    }

    @Override
    public void onLoadMore() {
        page++;
        postTaskListHttp();
    }

    @Override
    public void onRefresh() {
        page = 1;
        postTaskListHttp();
    }

    @Override
    public void onShow() {
        if (!init) {
            swipeToLoadLayout.setRefreshing(true);
            init = true;
        }
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
