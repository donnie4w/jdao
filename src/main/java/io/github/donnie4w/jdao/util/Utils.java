
package io.github.donnie4w.jdao.util;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.stream.Collectors;

/**
 * @Copyright 2012-2013 donnie(donnie4w@gmail.com)
 * @date 2013-1-10
 * @verion 1.0
 */
public class Utils {
    @Deprecated
    public static String delUnderline(String str) {
        String[] parts = str.split("_");
        if (parts.length == 1) {
            return str;
        }
        return Arrays.stream(parts, 1, parts.length)
                .map(Utils::upperFirstChar)
                .collect(Collectors.joining("", parts[0], ""));
    }

    @Deprecated
    public static String upperFirstChar(String word) {
        if (word == null || word.isEmpty()) {
            return word;
        }
        char firstChar = word.charAt(0);
        if (Character.isLowerCase(firstChar)) {
            return Character.toUpperCase(firstChar) + word.substring(1);
        }
        return word;
    }

    @Deprecated
    public static String dateFormat() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(new Date());
    }

    @Deprecated
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

    //java8
    public static String getPackageName(Class<?> clazz) {
        String className = clazz.getName();
        int lastDotIndex = className.lastIndexOf('.');
        if (lastDotIndex != -1) {
            return className.substring(0, lastDotIndex);
        } else {
            return "";
        }
    }

    public static boolean stringValid(String str) {
        return str != null && str.trim().length() > 0;
    }
}
