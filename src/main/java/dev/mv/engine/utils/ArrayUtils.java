package dev.mv.engine.utils;

import java.util.ArrayList;

public class ArrayUtils {

    public static <T extends Object> T[] arrayListToArray(ArrayList<T> array) {
        T[] ret = (T[]) new Object[array.size()];

        for (int i = 0; i < array.size(); i++) {
            ret[i] = array.get(i);
        }

        return ret;
    }

}
