package com.huisu.iyoox.activity.student;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.huisu.iyoox.Interface.TaskStatus;
import com.huisu.iyoox.R;
import com.huisu.iyoox.activity.base.BaseActivity;
import com.huisu.iyoox.fragment.base.BaseFragment;
import com.huisu.iyoox.fragment.student.StudentPurchaseHistoryFragment;
import com.huisu.iyoox.fragment.student.StudentTaskListFragment;
import com.huisu.iyoox.views.MyFragmentLayout_line;

import java.util.ArrayList;

/**
 * 学生购买记录
 */
public class StudentPurchaseHistoryActivity extends BaseActivity {

    private MyFragmentLayout_line mFragmentLayout;
    private ArrayList<BaseFragment> fragments = new ArrayList<>();

    @Override
    protected void initView() {
        mFragmentLayout = findViewById(R.id.student_purchase_content_layout);
        initFragment();
    }

    @Override
    protected void initData() {
        setTitle("购买记录");
    }

    @Override
    protected void setEvent() {
        setBack();
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_student_purchase_history;
    }

    private void initFragment() {
        fragments.clear();
        fragments.add(getFragment(TaskStatus.FINISH));
        fragments.add(getFragment(TaskStatus.UNFINISH));
        mFragmentLayout.setScorllToNext(true);
        mFragmentLayout.setScorll(true);
        mFragmentLayout.setWhereTab(1);
        mFragmentLayout.setTabHeight(6, getResources().getColor(R.color.main_text_color), false);
        mFragmentLayout
                .setOnChangeFragmentListener(new MyFragmentLayout_line.ChangeFragmentListener() {
                    @Override
                    public void change(int lastPosition, int positon,
                                       View lastTabView, View currentTabView) {
                        ((TextView) lastTabView.findViewById(R.id.tab_text))
                                .setTextColor(getResources().getColor(R.color.color333));
                        ((TextView) currentTabView.findViewById(R.id.tab_text))
                                .setTextColor(getResources().getColor(R.color.main_text_color));
                        fragments.get(positon).onShow();
                    }
                });
        mFragmentLayout.setAdapter(fragments, R.layout.tablayout_student_purchase_history, 0x223);
    }

    private StudentPurchaseHistoryFragment getFragment(String type) {
        StudentPurchaseHistoryFragment fragment = new StudentPurchaseHistoryFragment();
        Bundle b = new Bundle();
        b.putString("type", type);
        fragment.setArguments(b);
        return fragment;
    }

    public static void start(Context context) {
        Intent intent = new Intent(context, StudentPurchaseHistoryActivity.class);
        context.startActivity(intent);
    }

}
