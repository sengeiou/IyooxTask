package com.huisu.iyoox.http;

import com.huisu.iyoox.entity.base.BaseAddClassRoomModel;
import com.huisu.iyoox.entity.base.BaseArtBookModel;
import com.huisu.iyoox.entity.base.BaseArtBookZSDModel;
import com.huisu.iyoox.entity.base.BaseBookDetailsModel;
import com.huisu.iyoox.entity.base.BaseBookEditionModel;
import com.huisu.iyoox.entity.base.BaseCheckMsgCode;
import com.huisu.iyoox.entity.base.BaseClassRankingModel;
import com.huisu.iyoox.entity.base.BaseClassRoomModel;
import com.huisu.iyoox.entity.base.BaseClassRoomResultModel;
import com.huisu.iyoox.entity.base.BaseCollectModel;
import com.huisu.iyoox.entity.base.BaseDianPingListModel;
import com.huisu.iyoox.entity.base.BaseExercisesModel;
import com.huisu.iyoox.entity.base.BaseGradeListModel;
import com.huisu.iyoox.entity.base.BaseGuoxueYishuVodModel;
import com.huisu.iyoox.entity.base.BaseHomeWorkResultModel;
import com.huisu.iyoox.entity.base.BaseLearningCardModel;
import com.huisu.iyoox.entity.base.BaseMoreCollectModel;
import com.huisu.iyoox.entity.base.BaseNotificationMsgModel;
import com.huisu.iyoox.entity.base.BaseOtherBookModel;
import com.huisu.iyoox.entity.base.BasePhoneModel;
import com.huisu.iyoox.entity.base.BasePrePayWeChatEntity;
import com.huisu.iyoox.entity.base.BaseRegisterResultModel;
import com.huisu.iyoox.entity.base.BaseScreenSubjectVersionModel;
import com.huisu.iyoox.entity.base.BaseSendMsgCodeModel;
import com.huisu.iyoox.entity.base.BaseSendTaskResultModel;
import com.huisu.iyoox.entity.base.BaseStudentModel;
import com.huisu.iyoox.entity.base.BaseSubjectModel;
import com.huisu.iyoox.entity.base.BaseTaskResultModel;
import com.huisu.iyoox.entity.base.BaseTaskStudentListModel;
import com.huisu.iyoox.entity.base.BaseTaskTeacherListModel;
import com.huisu.iyoox.entity.base.BaseTaskTeacherLookClassModel;
import com.huisu.iyoox.entity.base.BaseTaskTeacherLookTimuModel;
import com.huisu.iyoox.entity.base.BaseTeacherModel;
import com.huisu.iyoox.entity.base.BaseTrialCardModel;
import com.huisu.iyoox.entity.base.BaseUser;
import com.huisu.iyoox.entity.base.BaseUserContentModel;
import com.huisu.iyoox.entity.base.BaseVersionModel;
import com.huisu.iyoox.entity.base.BaseVideoGroupModel;
import com.huisu.iyoox.entity.base.BaseVideoModel;
import com.huisu.iyoox.entity.base.BaseVideoTimuModel;
import com.huisu.iyoox.entity.base.BaseVipCardListModel;
import com.huisu.iyoox.entity.base.BaseVipCardModel;
import com.huisu.iyoox.entity.base.BaseXscVideoModel;
import com.huisu.iyoox.okhttp.CommonOkHttpClient;
import com.huisu.iyoox.okhttp.listener.DisposeDataHandle;
import com.huisu.iyoox.okhttp.listener.DisposeDataListener;
import com.huisu.iyoox.okhttp.listener.DisposeDownloadListener;
import com.huisu.iyoox.okhttp.request.CommonRequest;
import com.huisu.iyoox.okhttp.request.RequestParams;
import com.huisu.iyoox.util.LogUtil;
import com.huisu.iyoox.util.MD5Util;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * 请求链接方法
 */
public class RequestCenter {

    /**
     * get请求
     */
    public static void getRequest(String url, RequestParams params, DisposeDataListener listener, Class<?> clazz) {
        CommonOkHttpClient.get(CommonRequest.createGetRequest(url, params),
                new DisposeDataHandle(listener, clazz));
    }

    /**
     * post请求
     */
    public static void postRequest(String url, RequestParams params, DisposeDataListener listener, Class<?> clazz) {
        //打印请求地址
        LogUtil.e(url);
        CommonOkHttpClient.post(CommonRequest.createPostRequest(url, params),
                new DisposeDataHandle(listener, clazz));
    }

    /**
     * 文件上传
     */
    public static void downloadFile(String url, String filePath, DisposeDownloadListener listener) {
        CommonOkHttpClient.downloadFile(CommonRequest.createPostRequest(url, null),
                new DisposeDataHandle(listener, filePath));
    }

    /**
     * 文件上传
     */
    public static void postUpFile(String url, RequestParams params, DisposeDataListener listener, Class<?> clazz) {
        CommonOkHttpClient.get(CommonRequest.createMultiPostRequest(url, params),
                new DisposeDataHandle(listener, clazz));
    }

    /**
     * 阿里云获取PlayAuth
     */
    public static void getplayAuth(String vodId, DisposeDataListener listener) {
        RequestParams params = new RequestParams();
        params.put("video_id", vodId);
        RequestCenter.getRequest(HttpConstants.getplayAuth, params, listener, null);
    }

    /**
     * 更换个人头像
     */
    public static void updateAvatar(String userId, File file, DisposeDataListener listener) {
        RequestParams params = new RequestParams();
        params.put("user_id", userId);
        try {
            params.put("file", file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        RequestCenter.postUpFile(HttpConstants.updateAvatar, params, listener, null);
    }

    /**
     * 判断手机号 是否注册
     *
     * @param phone 手机号
     */
    public static void isExistphone(String phone, DisposeDataListener listener) {
        RequestParams params = new RequestParams();
        params.put("phone", phone);
        RequestCenter.postRequest(HttpConstants.IS_EXISTPHONE, params, listener, BasePhoneModel.class);
    }

    /**
     * 发送短信验证码
     *
     * @param phone 手机号
     */
    public static void sendMsgCode(String phone, String source, DisposeDataListener listener) {
        RequestParams params = new RequestParams();
        params.put("phone", phone);
        params.put("source", source);
        RequestCenter.postRequest(HttpConstants.SEND_MSG_CODE, params, listener, BaseSendMsgCodeModel.class);
    }

    /**
     * 判断验证是否正确
     *
     * @param phone 手机号
     */
    public static void checkMsgCode(String phone, String code, DisposeDataListener listener) {
        RequestParams params = new RequestParams();
        params.put("phone", phone);
        params.put("code", code);
        RequestCenter.postRequest(HttpConstants.CHECK_MSG_CODE, params, listener, BaseCheckMsgCode.class);
    }

    /**
     * 用户注册
     */
    public static void register(String phone, String password, String type, DisposeDataListener listener) {
        RequestParams params = new RequestParams();
        params.put("phone", phone);
        params.put("password", password);
        params.put("type", type);
        RequestCenter.postRequest(HttpConstants.REGISTER, params, listener, BaseRegisterResultModel.class);
    }

    /**
     * 设置用户信息
     */
    public static void setUserInfo(String userId, String name, String sex, String grade, DisposeDataListener listener) {
        RequestParams params = new RequestParams();
        params.put("id", userId);
        params.put("name", name);
        params.put("sex", sex);
        params.put("grade", grade);
        RequestCenter.postRequest(HttpConstants.SET_USER_INFO, params, listener, BaseUser.class);
    }

    /**
     * 设置老师用户信息
     */
    public static void setTeacherUserInfo(String userId, String name, String grade, String xuekeId, String jiaocaiId, String detailId, DisposeDataListener listener) {
        RequestParams params = new RequestParams();
        params.put("id", userId);
        params.put("name", name);
        params.put("grade", grade);
        params.put("xueke_id", xuekeId);
        params.put("jiaocai_id", jiaocaiId);
        params.put("grade_detail_id", detailId);
        RequestCenter.postRequest(HttpConstants.SET_USER_INFO, params, listener, BaseUser.class);
    }


    /**
     * 老师根据年级获取科目列表
     */
    public static void GET_KEMU_BY_GRADE(String gradeId, DisposeDataListener listener) {
        RequestParams params = new RequestParams();
        params.put("grade_id", gradeId);
        RequestCenter.postRequest(HttpConstants.GET_KEMU_BY_GRADE, params, listener, BaseSubjectModel.class);
    }

    /**
     * 老师根据科目获取教材版本列表
     */
    public static void getBookVersion(String xuekeId, String gradeId, DisposeDataListener listener) {
        RequestParams params = new RequestParams();
        params.put("xueke_id", xuekeId);
        params.put("grade_id", gradeId);
        RequestCenter.postRequest(HttpConstants.getBookVersion, params, listener, BaseBookEditionModel.class);
    }

    /**
     * 用户登陆请求
     */
    public static void login(String phone, String password, DisposeDataListener listener) {
        RequestParams params = new RequestParams();
        params.put("phone", phone);
        params.put("password", MD5Util.getMD5(password));
        RequestCenter.postRequest(HttpConstants.LOGIN, params, listener, BaseUser.class);
    }

    /**
     * 年级列表
     */
    public static void getGrades(DisposeDataListener listener) {
        RequestCenter.postRequest(HttpConstants.GET_GRADES, null, listener, BaseGradeListModel.class);
    }

    /**
     * 重置密码
     */
    public static void ModifyPassword(String phone, String password, DisposeDataListener listener) {
        RequestParams params = new RequestParams();
        params.put("phone", phone);
        params.put("password", password);
        RequestCenter.postRequest(HttpConstants.MODIFY_PASSWORD, params, listener, null);
    }

    /**
     * 学习界面 年级列表和教材列表
     */
    public static void indexList(DisposeDataListener listener) {
        RequestCenter.postRequest(HttpConstants.INDEX_LIST, null, listener, BaseGradeListModel.class);
    }

    /**
     * 学习界面 章节 和 知识点列表
     */
    public static void getOptionlist(String grade_id, String gradeDetailId, String kemu_id, DisposeDataListener listener) {
        RequestParams params = new RequestParams();
        params.put("grade_id", grade_id);
        params.put("grade_detail_id", gradeDetailId);
        params.put("kemu_id", kemu_id);
        RequestCenter.postRequest(HttpConstants.GET_OPTIONLIST, params, listener, BaseBookDetailsModel.class);
    }

    /**
     * 学习界面 章节 和 知识点列表
     */
    public static void getShipinlist(String grade_id, String kemu_id, String jiaocai_id, String grade_detail_id,
                                     String zhangjie_id, String zhishidian, String page,
                                     DisposeDataListener listener) {
        RequestParams params = new RequestParams();
        params.put("grade_id", grade_id);
        params.put("kemu_id", kemu_id);
        params.put("jiaocai_id", jiaocai_id);
        params.put("grade_detail_id", grade_detail_id);
        params.put("zhangjie_id", zhangjie_id);
        params.put("zhishidian", zhishidian);
        params.put("page_index", page);
        RequestCenter.postRequest(HttpConstants.GET_SHIPINLIST, params, listener, BaseVideoModel.class);
    }

    /**
     * 获取视频列表
     */
    public static void getVideoData(String userId, String zhishidianId, String shipinId, DisposeDataListener listener) {
        RequestParams params = new RequestParams();
        params.put("shipin_id", shipinId);
        params.put("zhishidian_id", zhishidianId);
        params.put("student_id", userId);
        RequestCenter.postRequest(HttpConstants.getVideoData, params, listener, BaseVideoTimuModel.class);
    }

    /**
     * 获取视频題目
     */
    public static void getVideoTimu(String userId, String shipinId, String zhishidianId, DisposeDataListener listener) {
        RequestParams params = new RequestParams();
        params.put("shipin_id", shipinId);
        params.put("student_id", userId);
        params.put("zhishidian_id", zhishidianId);
        RequestCenter.postRequest(HttpConstants.getVideoTimu, params, listener, BaseVideoTimuModel.class);
    }

    /**
     * 获取有错题的科目
     */
    public static void getErrorSubject(String studentId, DisposeDataListener listener) {
        RequestParams params = new RequestParams();
        params.put("student_id", studentId);
        RequestCenter.postRequest(HttpConstants.getErrorSubject, params, listener, BaseSubjectModel.class);
    }

    /**
     * 获取有错题的科目
     */
    public static void getErrorSubjectDetilas(String studentId, String subjectId, String jiaoCaiId, String zhishidianId
            , String time, DisposeDataListener listener) {
        RequestParams params = new RequestParams();
        params.put("student_id", studentId);
        params.put("kemu_id", subjectId);
        params.put("jiaocao_id", jiaoCaiId);
        params.put("zhishidian_id", zhishidianId);
        params.put("time_id", time);
        RequestCenter.postRequest(HttpConstants.getErrorSubjectDetails, params, listener, BaseExercisesModel.class);
    }

    /**
     * 获取错题筛选数据
     */
    public static void getscreenErrorListData(String studentId, String subjectId, DisposeDataListener listener) {
        RequestParams params = new RequestParams();
        params.put("student_id", studentId);
        params.put("kemu_id", subjectId);
        RequestCenter.postRequest(HttpConstants.getscreenErrorListData, params, listener, BaseScreenSubjectVersionModel.class);
    }

    /**
     * 做完题库题目返回报告
     */
    public static void getTaskResultData(String studentId, String zhishiId, String times, String json, DisposeDataListener listener) {
        RequestParams params = new RequestParams();
        params.put("student_id", studentId);
        params.put("zhishidian_id", zhishiId);
        params.put("answer_times", times);
        params.put("answer_details", json);
        RequestCenter.postRequest(HttpConstants.getTaskResultData, params, listener, BaseTaskResultModel.class);
    }

    /**
     * 学生作业列表
     */
    public static void getStudentTaskListData(String studentId, String status, String page, DisposeDataListener listener) {
        RequestParams params = new RequestParams();
        params.put("student_id", studentId);
        params.put("status", status);
        params.put("page_index", page);
        RequestCenter.postRequest(HttpConstants.getStudentTaskListData, params, listener, BaseTaskStudentListModel.class);
    }

    /**
     * 学生作业列表
     */
    public static void getStudentTaskDetails(String work_id, DisposeDataListener listener) {
        RequestParams params = new RequestParams();
        params.put("work_id", work_id);
        RequestCenter.postRequest(HttpConstants.getStudentTaskDetails, params, listener, BaseExercisesModel.class);
    }

    /**
     * 学生提交作业
     */
    public static void getStudentTaskResult(String studentId, String work_id, String times, String json, DisposeDataListener listener) {
        RequestParams params = new RequestParams();
        params.put("work_id", work_id);
        params.put("student_id", studentId);
        params.put("answer_times", times);
        params.put("answer_details", json);
        RequestCenter.postRequest(HttpConstants.getStudentTaskResult, params, listener, null);
    }

    /**
     * 学生作业报告
     */
    public static void getStudentTaskBaoGao(String studentId, String work_id, DisposeDataListener listener) {
        RequestParams params = new RequestParams();
        params.put("work_id", work_id);
        params.put("student_id", studentId);
        RequestCenter.postRequest(HttpConstants.getStudentTaskBaoGao, params, listener, BaseHomeWorkResultModel.class);
    }

    /**
     * 用户信息
     */
    public static void userInfo(String user_id, DisposeDataListener listener) {
        RequestParams params = new RequestParams();
        params.put("user_id", user_id);
        RequestCenter.postRequest(HttpConstants.userInfo, params, listener, BaseUser.class);
    }

    /**
     * 学生获取班级详情信息
     */
    public static void getClassRanking(String student_id, DisposeDataListener listener) {
        RequestParams params = new RequestParams();
        params.put("student_id", student_id);
        RequestCenter.postRequest(HttpConstants.getClassRanking, params, listener, BaseClassRankingModel.class);
    }

    /**
     * 学生添加班级
     */
    public static void addClassRoom(String student_id, String classId, DisposeDataListener listener) {
        RequestParams params = new RequestParams();
        params.put("student_id", student_id);
        params.put("classroom_no", classId);
        RequestCenter.postRequest(HttpConstants.addClassRoom, params, listener, BaseAddClassRoomModel.class);
    }

    /**
     * 获取激活码
     */
    public static void getJiHuoCode(String user_id, String phone, DisposeDataListener listener) {
        RequestParams params = new RequestParams();
        params.put("user_id", user_id);
        params.put("phone", phone);
        RequestCenter.postRequest(HttpConstants.getJiHuoCode, params, listener, BaseTrialCardModel.class);
    }

    /**
     * 绑定激活码
     */
    public static void getStudentBindCard(String userId, String code, DisposeDataListener listener) {
        RequestParams params = new RequestParams();
        params.put("student_id", userId);
        params.put("no", code);
        RequestCenter.postRequest(HttpConstants.getStudentBindCard, params, listener, BaseVipCardModel.class);
    }

    /**
     * 老师创建班级
     */
    public static void teacherCreateClassroom(String teacherId, String gradeId, String className, DisposeDataListener listener) {
        RequestParams params = new RequestParams();
        params.put("teacher_id", teacherId);
        params.put("grade_id", gradeId);
        params.put("name", className);
        RequestCenter.postRequest(HttpConstants.teacherCreateClassroom, params, listener, BaseClassRoomResultModel.class);
    }

    /**
     * 老师获取班级列表
     */
    public static void teacherClassroomList(String teacherId, DisposeDataListener listener) {
        RequestParams params = new RequestParams();
        params.put("teacher_id", teacherId);
        RequestCenter.postRequest(HttpConstants.teacherClassroomList, params, listener, BaseClassRoomModel.class);
    }

    /**
     * 老师锁定或解锁班级
     */
    public static void teacherLockClassRoom(String classroomId, String islock, DisposeDataListener listener) {
        RequestParams params = new RequestParams();
        params.put("banji_id", classroomId);
        params.put("islock", islock);
        RequestCenter.postRequest(HttpConstants.teacherLockClassRoom, params, listener, null);
    }

    /**
     * 老师获取班级列表
     */
    public static void teacherClassroomListDeatail(String classroomId, DisposeDataListener listener) {
        RequestParams params = new RequestParams();
        params.put("banji_id", classroomId);
        RequestCenter.postRequest(HttpConstants.teacherClassroomListDeatail, params, listener, BaseStudentModel.class);
    }

    /**
     * 老师获取班级列表
     */
    public static void classroomTeacherList(String classroomId, DisposeDataListener listener) {
        RequestParams params = new RequestParams();
        params.put("class_room_id", classroomId);
        RequestCenter.postRequest(HttpConstants.CLASSROOM_TEACHER_LIST, params, listener, BaseTeacherModel.class);
    }

    /**
     * 老师布置作业教材章节列表
     */
    public static void getZhangjieDetail(String gradeId, String kemuId, String jiaocaiId,
                                         String detailId, String page,
                                         DisposeDataListener listener) {
        RequestParams params = new RequestParams();
        params.put("grade_id", gradeId);
        params.put("kemu_id", kemuId);
        params.put("jiaocai_id", jiaocaiId);
        params.put("grade_detail_id", detailId);
        params.put("page_index", page);
        RequestCenter.postRequest(HttpConstants.getZhangjieDetail, params, listener, BaseVideoGroupModel.class);
    }

    /**
     * 老师布置作业 选择题目
     */
    public static void teacherSelectExercisesData(String shipinId, DisposeDataListener listener) {
        RequestParams params = new RequestParams();
        params.put("id", shipinId);
        RequestCenter.postRequest(HttpConstants.teacherSelectExercisesData, params, listener, BaseExercisesModel.class);
    }

    /**
     * 老师布置作业成功 提醒家长
     */
    public static void notifyParents(String recieverIds, String message_body, String messageType, String messageAction,
                                     String workIds, DisposeDataListener listener) {
        RequestParams params = new RequestParams();
        params.put("receiver_ids", recieverIds);
        params.put("message_body", message_body);
        params.put("message_type", messageType);
        params.put("message_business_type", messageAction);
        params.put("message_business_ids", workIds);
        RequestCenter.postRequest(HttpConstants.notifyParents, params, listener, null);
    }

    /**
     * 老师删除班级
     */
    public static void deleteClassroom(String userId, String classId, DisposeDataListener listener) {
        RequestParams params = new RequestParams();
        params.put("teacher_id", userId);
        params.put("classroom_id", classId);
        RequestCenter.postRequest(HttpConstants.deleteClassroom, params, listener, null);
    }

    /**
     * 老师布置作业
     */
    public static void teacherSendTask(String userId, String taskName, String classIds, String timuIds,
                                       String createdate, String endDate, String desc, String zhishidianId,
                                       String xuekeId, String taskType, DisposeDataListener listener) {
        RequestParams params = new RequestParams();
        params.put("teacher_id", userId);
        params.put("name", taskName);
        params.put("classroom_ids", classIds);
        params.put("timu_ids", timuIds);
        params.put("createdate", createdate);
        params.put("end_time", endDate);
        params.put("zhishidian_id", zhishidianId);
        params.put("xueke_id", xuekeId);
        params.put("source_type", taskType);
        params.put("desc", desc);
        RequestCenter.postRequest(HttpConstants.teacherSendTask, params, listener, BaseSendTaskResultModel.class);
    }

    /**
     * 老师删除班级
     */
    public static void joinClassroom(String userId, String classNo, DisposeDataListener listener) {
        RequestParams params = new RequestParams();
        params.put("teacher_id", userId);
        params.put("classroom_no", classNo);
        RequestCenter.postRequest(HttpConstants.joinClassroom, params, listener, null);
    }

    /**
     * 老师管理班级移除老师
     */
    public static void classRemoveTeacher(String teacherIds, String classId, DisposeDataListener listener) {
        RequestParams params = new RequestParams();
        params.put("teacher_ids", teacherIds);
        params.put("class_room_id", classId);
        RequestCenter.postRequest(HttpConstants.classRemoveTeacher, params, listener, null);
    }

    /**
     * 老师查看班级作业列表
     */
    public static void teacherTaskList(String userId, String classId, String type, String page, DisposeDataListener listener) {
        RequestParams params = new RequestParams();
        params.put("teacher_id", userId);
        params.put("classroom_id", classId);
        params.put("type", type);
        params.put("pageindex", page);
        RequestCenter.postRequest(HttpConstants.teacherTaskList, params, listener, BaseTaskTeacherListModel.class);
    }

    /**
     * 老师查看班级作业详情
     */
    public static void teacherTaskDetail(String workId, DisposeDataListener listener) {
        RequestParams params = new RequestParams();
        params.put("work_id", workId);
        RequestCenter.postRequest(HttpConstants.teacherTaskDetail, params, listener, BaseTaskTeacherLookClassModel.class);
    }

    /**
     * 老师查看班级作业详情全班学生题目选择情况
     */
    public static void teacherLookTaskTimuDetails(String workId, DisposeDataListener listener) {
        RequestParams params = new RequestParams();
        params.put("work_id", workId);
        RequestCenter.postRequest(HttpConstants.teacherLookTaskTimuDetails, params, listener, BaseExercisesModel.class);
    }

    /**
     * 老师查看某个学生的某个作业的详情
     */
    public static void teacherLookStudentDetails(String workId, String studentId, DisposeDataListener listener) {
        RequestParams params = new RequestParams();
        params.put("work_id", workId);
        params.put("student_id", studentId);
        RequestCenter.postRequest(HttpConstants.teacherLookStudentDetails, params, listener, BaseTaskTeacherLookTimuModel.class);
    }

    /**
     * 老师点评某次作业
     */
    public static void dianpingStudentWork(String workId, String studentId, String dianping, DisposeDataListener listener) {
        RequestParams params = new RequestParams();
        params.put("work_id", workId);
        params.put("student_id", studentId);
        params.put("dianping", dianping);
        RequestCenter.postRequest(HttpConstants.dianpingStudentWork, params, listener, null);
    }

    /**
     * 老师点评集合
     */
    public static void teacherDianPingList(String userId, String pageindex, DisposeDataListener listener) {
        RequestParams params = new RequestParams();
        params.put("teacher_id", userId);
        params.put("page_index", pageindex);
        RequestCenter.postRequest(HttpConstants.teacherDianPingList, params, listener, BaseDianPingListModel.class);
    }

    /**
     * 学生收藏视频
     */
    public static void collectZhishidian(String userId, String zhishidianId, String shipin_type, DisposeDataListener listener) {
        RequestParams params = new RequestParams();
        params.put("student_id", userId);
        params.put("zhishidian_id", zhishidianId);
        params.put("shipin_type", shipin_type);
        RequestCenter.postRequest(HttpConstants.collectZhishidian, params, listener, null);
    }

    /**
     * 老师修改信息
     */
    public static void modifyTeacherInfo(String userId, String teacherName, String xuekeId,
                                         String jiaocaiId, String gradeDetailId, String gradeId, DisposeDataListener listener) {
        RequestParams params = new RequestParams();
        params.put("teacher_id", userId);
        params.put("teacher_name", teacherName);
        params.put("xueke_id", xuekeId);
        params.put("jiaocai_id", jiaocaiId);
        params.put("grade_detail_id", gradeDetailId);
        params.put("grade_id", gradeId);
        RequestCenter.postRequest(HttpConstants.modifyTeacherInfo, params, listener, null);
    }

    /**
     * 学生收藏列表
     */
    public static void getCollectList(String userId, DisposeDataListener listener) {
        RequestParams params = new RequestParams();
        params.put("student_id", userId);
        RequestCenter.postRequest(HttpConstants.getCollectList, params, listener, BaseCollectModel.class);
    }

    /**
     * 新消息
     */
    public static void getNewMessageCount(String userId, DisposeDataListener listener) {
        RequestParams params = new RequestParams();
        params.put("receiver_id", userId);
        RequestCenter.postRequest(HttpConstants.getNewMessageCount, params, listener, null);
    }

    /**
     * 消息列表
     */
    public static void getMsgList(String userId, String type, String pageindex, DisposeDataListener listener) {
        RequestParams params = new RequestParams();
        params.put("receiver_id", userId);
        params.put("message_type", type);
        params.put("pageindex", pageindex);
        RequestCenter.postRequest(HttpConstants.getMsgList, params, listener, BaseNotificationMsgModel.class);
    }

    /**
     * 学习卡列表
     */
    public static void getLearningCardList(String userId, DisposeDataListener listener) {
        RequestParams params = new RequestParams();
        params.put("user_id", userId);
        params.put("is_valid", "1");
        RequestCenter.postRequest(HttpConstants.getLearningCardList, params, listener, BaseLearningCardModel.class);
    }

    /**
     * VIP会员列表
     */
    public static void getVipList(DisposeDataListener listener) {
        RequestParams params = new RequestParams();
        RequestCenter.postRequest(HttpConstants.getVipList, params, listener, BaseVipCardListModel.class);
    }

    /**
     * 支付接口
     */
    public static void getPayJson(String userId, String typeId, DisposeDataListener listener) {
        RequestParams params = new RequestParams();
        params.put("user_id", userId);
        params.put("jihuoma_type", typeId);
        RequestCenter.postRequest(HttpConstants.getPayJson, params, listener, null);
    }

    /**
     * 支付接口
     */
    public static void getWXPayJson(String userId, String typeId, DisposeDataListener listener) {
        RequestParams params = new RequestParams();
        params.put("user_id", userId);
        params.put("jihuoma_type", typeId);
        RequestCenter.postRequest(HttpConstants.getWXPayJson, params, listener, BasePrePayWeChatEntity.class);
    }

    /**
     * 判断更新
     */
    public static void judgeVersionUpdate(DisposeDataListener listener) {
        RequestParams params = new RequestParams();
        RequestCenter.postRequest(HttpConstants.judgeVersionUpdate, params, listener, BaseVersionModel.class);
    }

    /**
     * 国学
     */
    public static void guoxueList(DisposeDataListener listener) {
        RequestParams params = new RequestParams();
        RequestCenter.postRequest(HttpConstants.guoxueList, params, listener, BaseOtherBookModel.class);
    }

    /**
     * 国学类型
     */
    public static void artGuoXueList(DisposeDataListener listener) {
        RequestParams params = new RequestParams();
        RequestCenter.postRequest(HttpConstants.guoxueList, params, listener, BaseArtBookModel.class);
    }

    /**
     * 国学详情
     */
    public static void guoxueDetail(String gradeId, String zhangjie, DisposeDataListener listener) {
        RequestParams params = new RequestParams();
        params.put("grade_id", gradeId);
        params.put("zhangjie", zhangjie);
        RequestCenter.postRequest(HttpConstants.guoxueDetail, params, listener, BaseArtBookZSDModel.class);
    }

    /**
     * 艺术
     */
    public static void yishuList(DisposeDataListener listener) {
        RequestParams params = new RequestParams();
        RequestCenter.postRequest(HttpConstants.yishuList, params, listener, BaseArtBookModel.class);
    }

    /**
     * 艺术详情
     */
    public static void yishuDetail(String gradeId, String zhangjie, DisposeDataListener listener) {
        RequestParams params = new RequestParams();
        params.put("grade_id", gradeId);
        params.put("zhangjie", zhangjie);
        RequestCenter.postRequest(HttpConstants.yishuDetail, params, listener, BaseArtBookZSDModel.class);
    }

    /**
     * 删除收藏
     */
    public static void deleteCollect(String userId, String zhishidianId, DisposeDataListener listener) {
        RequestParams params = new RequestParams();
        params.put("student_id", userId);
        params.put("zhishidians", zhishidianId);
        RequestCenter.postRequest(HttpConstants.deleteCollect, params, listener, BaseOtherBookModel.class);
    }

    /**
     * 获取学生VIP 做题数 完成作业次数
     */
    public static void getUserCenterInfo(String userId, DisposeDataListener listener) {
        RequestParams params = new RequestParams();
        params.put("user_id", userId);
        RequestCenter.postRequest(HttpConstants.getUserCenterInfo, params, listener, BaseUserContentModel.class);
    }

    /**
     * 获取学生VIP 做题数 完成作业次数
     */
    public static void addShiPinCount(String userId, String zhishidianId, String type, DisposeDataListener listener) {
        RequestParams params = new RequestParams();
        params.put("student_id", userId);
        params.put("zhishidian_id", zhishidianId);
        params.put("shipin_type", type);
        RequestCenter.postRequest(HttpConstants.addShiPinCount, params, listener, null);
    }

    /**
     * 首页点击
     */
    public static void get_video_from_index(String userId, String zhishidianId, DisposeDataListener listener) {
        RequestParams params = new RequestParams();
        params.put("student_id", userId);
        params.put("zhishidian_id", zhishidianId);
        RequestCenter.postRequest(HttpConstants.get_video_from_index, params, listener, null);
    }

    /**
     * 点击国学艺术
     */
    public static void get_guoxue_yishu_vedio(String userId, String id, String type, DisposeDataListener listener) {
        RequestParams params = new RequestParams();
        params.put("id", id);
        params.put("student_id", userId);
        params.put("type", type);
        RequestCenter.postRequest(HttpConstants.get_guoxue_yishu_vedio, params, listener, BaseGuoxueYishuVodModel.class);
    }

    /**
     * 学习记录
     */
    public static void get_xuexi_record(String userId, DisposeDataListener listener) {
        RequestParams params = new RequestParams();
        params.put("student_id", userId);
        RequestCenter.postRequest(HttpConstants.get_xuexi_record, params, listener, BaseMoreCollectModel.class);
    }

    /**
     * 小升初
     */
    public static void xiaoshengchu(DisposeDataListener listener) {
        RequestParams params = new RequestParams();
        RequestCenter.postRequest(HttpConstants.xiaoshengchu, params, listener, BaseXscVideoModel.class);
    }

    /**
     * 小升初列表
     */
    public static void get_xiaoshengchu_vedio(String userId, String id, DisposeDataListener listener) {
        RequestParams params = new RequestParams();
        params.put("module_name", "小升初专题");
        params.put("id", id);
        params.put("student_id", userId);
        RequestCenter.postRequest(HttpConstants.get_xiaoshengchu_vedio, params, listener, BaseGuoxueYishuVodModel.class);
    }

    /**
     * 小升初详情
     */
    public static void get_vedio_play_info(String userId, String id, String type, DisposeDataListener listener) {
        RequestParams params = new RequestParams();
        params.put("id", id);
        params.put("student_id", userId);
        params.put("type", type);
        RequestCenter.postRequest(HttpConstants.get_vedio_play_info, params, listener, BaseGuoxueYishuVodModel.class);
    }
}
