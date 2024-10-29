package com.tpisoftware.org.stlucia.ecommerce.util;

import java.util.List;

public class ListUtil {

    private final static String INPUT_PARAMETER_MUST_NOT_BE_NULL = "The input parameter must not be null.";
    private static final String INSTANCE_OF_ERROR_MESSAGE = "The provided object is not an instance of: %s.";

    @SuppressWarnings("unchecked")
    public static <T> List<T> convertTo(Object objs, Class<T> clazz) {
        List<T> result;

        try {
            checkInputParameter(objs, clazz);

            if (isElementInstanceOf(objs, clazz)) {
                result = (List<T>) objs;
            } else {
                throw new IllegalArgumentException(String.format(INSTANCE_OF_ERROR_MESSAGE, clazz.getName()));
            }
        } catch (IllegalArgumentException e) {
            if (objs == null && clazz != null) {
                result = null;
            } else {
                throw e;
            }
        }
        return result;
    }

    /**
     * If the list is empty, it always evaluates to the same class.
     */
    public static boolean isElementInstanceOf(Object objs, Class<?> clazz) {
        checkInputParameter(objs, clazz);

        boolean result = false;
        if (objs instanceof List<?> list) {
            if (list.isEmpty()) {
                result = true;
            } else {
                result = clazz.isInstance(list.get(0));
            }
        }

        return result;
    }

    private static void checkInputParameter(Object objs, Class<?> clazz) {
        if (objs == null || clazz == null) {
            throw new IllegalArgumentException(INPUT_PARAMETER_MUST_NOT_BE_NULL);
        }
    }

}