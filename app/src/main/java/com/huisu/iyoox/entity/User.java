package com.huisu.iyoox.entity;


import org.litepal.crud.LitePalSupport;

public class User extends LitePalSupport {
    private int user_id;
    private String name;
    private String phone;
    private String email;
    private int sex;
    private int isvalid;
    private int grade;

    public String getUserId() {
        return user_id + "";
    }

    public void setUserId(int user_id) {
        this.user_id = user_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public int getIsvalid() {
        return isvalid;
    }

    public void setIsvalid(int isvalid) {
        this.isvalid = isvalid;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public String getGradeName() {
        switch (grade) {
            case 1:
                return "一年级";
            case 2:
                return "二年级";
            case 3:
                return "三年级";
            case 4:
                return "四年级";
            case 5:
                return "五年级";
            case 6:
                return "六年级";
            case 7:
                return "七年级";
            case 8:
                return "八年级";
            case 9:
                return "九年级";
            default:
                return "一年级";
        }
    }
}
