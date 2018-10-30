package org.dark;

import lombok.extern.slf4j.Slf4j;

/**
 * @author xiaozefeng
 * @date 2018/10/30 10:03 AM
 */
@Slf4j
public class StringUtil {

    /**
     * 判断是否有指定后缀的字符字串
     */
    public static boolean hasSuffix(String origin, String suffix) {
        if (origin == null || "".equals(origin)) {
            return false;
        }
        if (suffix == null || "".equals(suffix)) {
            return false;
        }
        int i = origin.indexOf(".");
        if (i == -1) {
            return false;
        }
        String temp = origin.substring(i+1);
        return suffix.equals(temp);
    }

    public static String getFileName(String fullName) {
        if (fullName == null || "".equals(fullName)) {
            throw new RuntimeException("fullName required not null and not empty");
        }
        int i = fullName.indexOf(".");
        if (i == -1) {
            throw new RuntimeException("fullName required contains .");
        }
        return fullName.substring(0, i);
    }
}
