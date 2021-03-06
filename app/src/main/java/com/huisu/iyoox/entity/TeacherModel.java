package com.huisu.iyoox.entity;

/**
 * Function:
 * Date: 2018/7/21
 *
 * @author dinglai
 * @since JDK 1.8
 */
public class TeacherModel {
    private int xueke_id;
    private String xueke_name;
    private int teacher_id;
    private String teacher_name;
    private String classroom_teacher_mapping_id;
    private boolean isSelect;

    public int getXueke_id() {
        return xueke_id;
    }

    public void setXueke_id(int xueke_id) {
        this.xueke_id = xueke_id;
    }

    public String getXueke_name() {
        return xueke_name;
    }

    public void setXueke_name(String xueke_name) {
        this.xueke_name = xueke_name;
    }

    public int getTeacher_id() {
        return teacher_id;
    }

    public void setTeacher_id(int teacher_id) {
        this.teacher_id = teacher_id;
    }

    public String getTeacher_name() {
        return teacher_name;
    }

    public void setTeacher_name(String teacher_name) {
        this.teacher_name = teacher_name;
    }

    public String getClassroom_teacher_mapping_id() {
        return classroom_teacher_mapping_id;
    }

    public void setClassroom_teacher_mapping_id(String classroom_teacher_mapping_id) {
        this.classroom_teacher_mapping_id = classroom_teacher_mapping_id;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }
}
