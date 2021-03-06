package com.huisu.iyoox.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.huisu.iyoox.R;
import com.huisu.iyoox.activity.student.TaskStudentHomeWorkActivity;
import com.huisu.iyoox.constant.Constant;
import com.huisu.iyoox.entity.User;
import com.huisu.iyoox.entity.VideoTimuModel;
import com.huisu.iyoox.entity.VideoTitleModel;
import com.huisu.iyoox.entity.base.BaseVideoTimuModel;
import com.huisu.iyoox.http.RequestCenter;
import com.huisu.iyoox.manager.UserManager;
import com.huisu.iyoox.okhttp.listener.DisposeDataListener;
import com.huisu.iyoox.util.TabToast;
import com.huisu.iyoox.views.Loading;

import java.io.Serializable;
import java.util.List;

/**
 * @author:dl
 * @function:
 * @date: 2018/7/13
 */
public class ItemVideoListAdapter extends BaseAdapter {
    private Context context;
    private List<VideoTitleModel> data;
    private Loading loading;
    private User user;

    public ItemVideoListAdapter(Context context, List<VideoTitleModel> data) {
        this.context = context;
        this.data = data;
        user = UserManager.getInstance().getUser();
    }

    @Override
    public int getCount() {
        return data == null ? 0 : data.size();
    }

    @Override
    public VideoTitleModel getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_video_view_layout, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final VideoTitleModel titleModel = getItem(position);
        holder.videoIcon.setSelected(true);
        holder.videoState.setSelected(true);
        holder.videoName.setText(titleModel.getShipin_name());
        if (titleModel.getTimu_count() > 0) {
            holder.videoStateContent.setVisibility(View.VISIBLE);
            holder.videoStateContent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    postExercisesData(titleModel.getShipin_id() + "", titleModel.getZhishidian_id() + "");
                }
            });
        } else {
            holder.videoStateContent.setVisibility(View.GONE);
        }
        if (titleModel.getSort() > 0 && titleModel.getSort() <= 3) {
            holder.isSort.setVisibility(View.VISIBLE);
        } else {
            holder.isSort.setVisibility(View.GONE);
        }
        return convertView;
    }

    static class ViewHolder {
        private ImageView videoIcon;
        private TextView videoName;
        private TextView videoState;
        private View videoStateContent;
        private View isSort;

        public ViewHolder(View view) {
            videoIcon = view.findViewById(R.id.item_book_video_icon_iv);
            videoName = view.findViewById(R.id.item_video_name_tv);
            videoState = view.findViewById(R.id.item_video_state);
            videoStateContent = view.findViewById(R.id.item_video_state_content);
            isSort = view.findViewById(R.id.is_sort);
        }
    }

    /**
     * 获取知识点作业题目信息
     */
    private void postExercisesData(String videoId, final String zhishiId) {
        if (user == null) return;
        loading = Loading.show(null, context, context.getString(R.string.loading_one_hint_text));
        RequestCenter.getVideoTimu(user.getUserId(), videoId, zhishiId, new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) {
                loading.dismiss();
                BaseVideoTimuModel baseVideoUrlModel = (BaseVideoTimuModel) responseObj;
                if (baseVideoUrlModel.code == Constant.POST_SUCCESS_CODE && baseVideoUrlModel.data != null) {
                    VideoTimuModel urlModel = baseVideoUrlModel.data;
                    if (urlModel.getTimu_list() != null && urlModel.getTimu_list().size() > 0) {
                        //计时器清零
                        Intent intent = new Intent(context, TaskStudentHomeWorkActivity.class);
                        intent.putExtra("model", urlModel);
                        intent.putExtra("zhishiId", zhishiId);
                        intent.putExtra("type", Constant.STUDENT_DOING);
                        context.startActivity(intent);
                    } else {
                        TabToast.showMiddleToast(context, "暂无习题");
                    }
                } else {
                    TabToast.showMiddleToast(context, "暂无习题");
                }
            }

            @Override
            public void onFailure(Object reasonObj) {
                loading.dismiss();
            }
        });
    }
}
