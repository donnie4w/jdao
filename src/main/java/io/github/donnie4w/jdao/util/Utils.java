package io.github.donnie4w.jdao.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Copyright 2012-2013 donnie(donnie4w@gmail.com)
 * @date 2013-1-10
 * @verion 1.0
 */
public class Utils {
    public static String delUnderline(String str) {
        String[] ss = str.split("_");
        if (ss.length == 1) {
            return str;
        } else {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < ss.length; i++) {
                if (i != 0) {
                    sb.append(upperFirstChar(ss[i].toLowerCase()));
                } else {
                    sb.append(ss[i]);
                }
            }
            return sb.toString();
        }
    }

    public static String upperFirstChar(String str) {
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    public static String dateFormat() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String ret = sdf.format(new Date());
        return ret.toUpperCase();
    }

    public static boolean isContainsLowerCase(String str) {
        if (str == null)
            return false;
        int length = str.length();
        if (length == 0)
            return false;
        for (int i = 0; i < length; i++) {
            if (Character.isLowerCase(str.charAt(i))) {
                return true;
            }
        }
        return false;
    }
}
