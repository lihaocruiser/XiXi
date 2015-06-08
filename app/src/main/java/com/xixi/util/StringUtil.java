package com.xixi.util;

import java.util.Collection;

public class StringUtil {

    public static boolean isInCollection(String str, Collection<String> collection) {

        for (String item : collection) {
            if (str.equals(item)) {
                return true;
            }
        }
        return false;

    }

}
