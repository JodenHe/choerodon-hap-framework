package io.choerodon.hap.core.util;

import java.util.regex.Pattern;

/**
 * 格式校验工具类.
 *
 * @author lijian.yin@hand-china.com
 * @since 2017/12/26
 **/
public class ValidateUtils {

    /**
     * 正则表达式-phone.
     */
    private static final Pattern PATTERN_PHONE_REGEX = Pattern.compile("^1[3|4|5|8][0-9]\\d{4,8}");

    /**
     * 正则表达式-email.
     */
    private static final Pattern PATTERN_EMAIL_REGEX = Pattern.compile("^([\\s\\S]*)+@([\\S\\s]*)+(\\.([\\S\\s]*)+)+$");
    /**
     * 正则表达式-loginName.
     */
    private static final Pattern UESR_NAME_REGEX = Pattern.compile("^[A-Za-z0-9]{6,20}$");

    /**
     * 校验邮箱格式.
     *
     * @param email 邮箱
     * @return 判定结果
     */
    public static boolean validateEmail(String email) {
        return PATTERN_EMAIL_REGEX.matcher(email).matches();
    }

    /**
     * 校验电话格式.
     *
     * @param phone 电话
     * @return 判定结果
     */
    public static boolean validatePhone(String phone) {
        return PATTERN_PHONE_REGEX.matcher(phone).matches();
    }

    /**
     * 校验用户名格式.
     *
     * @param userName 用户名
     * @return 判定结果
     */
    public static boolean validateUserName(String userName) {
        return UESR_NAME_REGEX.matcher(userName).matches();
    }

}
