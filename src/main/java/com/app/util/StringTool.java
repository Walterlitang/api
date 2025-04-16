package com.app.util;

import org.apache.commons.lang3.StringUtils;

public class StringTool extends StringUtils {

    public static final String EMPTY_STRING = "";

    public static boolean isNotEmpty(String str) {
        return ((str != null) && (!(str.equals(""))));
    }

    public static boolean isEmpty(String str) {
        return (!(isNotEmpty(str)));
    }

}
