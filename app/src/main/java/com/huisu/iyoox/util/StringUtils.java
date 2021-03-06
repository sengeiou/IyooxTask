package com.huisu.iyoox.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.Log;
import android.widget.EditText;

import com.huisu.iyoox.R;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * Created by Administrator on 2018/6/28.
 */

public class StringUtils {
    public static DecimalFormat decimalFormat = new DecimalFormat("0.00");

    /**
     * 获取当前版本号
     *
     * @param context
     * @return
     */
    public static int getCurrentVersion(Context context) {
        int versionCode = 0;
        try {
            // ---get the package info---
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            versionCode = pi.versionCode;
        } catch (Exception e) {
            Log.e("VersionInfo", "Exception", e);
        }
        return versionCode;
    }

    /**
     * 判断是PAD还是手机
     *
     * @param context
     * @return
     */
    public static boolean isPad(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

    /**
     * 判断集合是否为空
     */
    public static boolean isEmpty(List list) {
        return list == null ? true : list.isEmpty();
    }

    /**
     * 小数格式化成保留两位并去掉最后无用的0
     *
     * @param f
     * @return
     */
    public static String formatFloat(double f) {
        try {
            String format = decimalFormat.format(f);
            return NumberFormat.getInstance().format(Float.parseFloat(format));
        } catch (Exception e) {
            e.printStackTrace();
            return "0";
        }
    }

    /**
     * @param list
     * @return
     */
    public static int getListSize(List list) {
        return list == null ? 0 : list.size();
    }

    /**
     * 大陆手机号码11位数，匹配格式：前三位固定格式+后8位任意数
     * 此方法中前三位格式有：
     * 13+任意数
     * 15+除4的任意数
     * 18+除1和4的任意数
     * 17+除9的任意数
     * 147
     */
    public static boolean isChinaPhoneLegal(String str) throws PatternSyntaxException {
        String regExp = "^((13[0-9])|(15[^4])|(18[0,2,3,5-9])|(17[0-8])|(147))\\d{8}$";
        Pattern p = Pattern.compile(regExp);
        Matcher m = p.matcher(str);
        return m.matches();
    }

    /**
     * dp转px
     *
     * @param value dp
     * @return px
     */
    public static int dp2px(Context context, float value) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (value * scale + 0.5f);
    }

    public static String getTimeString(String time) {
        if (TextUtils.isEmpty(time)) {
            return "";
        }
        if (time.contains(".")) {
            time = time.split("\\.")[0];
        }
        if (time.contains("T")) {
            time = time.split("T")[0] + " " + time.split("T")[1];
        }
        time = DateUtils.formatTimesMDHm(time);
        return time;
    }

    public static String getTimeStringMD(String time) {
        if (TextUtils.isEmpty(time)) {
            return "";
        }
        if (time.contains(".")) {
            time = time.split("\\.")[0];
        }
        if (time.contains("T")) {
            time = time.split("T")[0] + " " + time.split("T")[1];
        }
        time = DateUtils.formatTimesMD(time);
        return time;
    }

    public static String getTimeStringYMD(String time) {
        if (TextUtils.isEmpty(time)) {
            return "";
        }
        if (time.contains(".")) {
            time = time.split("\\.")[0];
        }
        if (time.contains("T")) {
            time = time.split("T")[0] + " " + time.split("T")[1];
        }
        time = DateUtils.formatTimesymd(time);
        return time;
    }

    /**
     * 判断姓名是否合法的姓名
     * 姓名必须是以汉字开头，后面只能包括汉字、字母 、数字和点号，长度在2-10个字符
     *
     * @param personName
     */
    public static boolean isRightName(String personName) {
        if (TextUtils.isEmpty(personName)) {
            return false;
        }
        boolean b = false;
        Pattern p = Pattern.compile("^([\\u4e00-\\u9fa5][\\u4e00-\\u9fa5a-zA-Z\\d\\s\\·]{1,9})$"); // 验证账号
        Matcher m = p.matcher(personName);
        return m.matches();
    }

    /**
     * VIP图片类型
     *
     * @param id VIP类别Id
     */
    public static int getCardResId(int id) {
        switch (id) {
            case 1://试用卡
                return R.drawable.learning_card_shi;
            case 2://月卡
                return R.drawable.icon_vip_one;
            case 3://半年
                return R.drawable.icon_vip_two;
            case 8://年卡
                return R.drawable.icon_vip_three;
            case 9://3年卡
                return R.drawable.icon_vip_four;
            default:
                return R.drawable.icon_vip_one;
        }
    }

    public static boolean isWifi(Context mContext) {
        ConnectivityManager connectivityManager = (ConnectivityManager) mContext
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info= connectivityManager.getActiveNetworkInfo();
        if (info!= null
                && info.getType() == ConnectivityManager.TYPE_WIFI) {
            return true;
        }
        return false;
    }

}
