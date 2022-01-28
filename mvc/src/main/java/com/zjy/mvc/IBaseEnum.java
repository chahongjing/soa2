package com.zjy.mvc;

import org.apache.commons.lang3.StringUtils;

/**
 * Created by Administrator on 2018/5/15.
 */
public interface IBaseEnum {
    default int getValue() {
        throw new UnsupportedOperationException("未实现getValue方法");
    }

    default String getCode() {
        return StringUtils.EMPTY;
    }

    default String getName() {
        return StringUtils.EMPTY;
    }

    default int getOrder() {
        return 0;
    }

    static <E extends Enum<E> & IBaseEnum> E getByValue(Class<E> enumClass, Integer value) {
        if (value == null) return null;
        E[] enumConstants = enumClass.getEnumConstants();
        for (E item : enumConstants) {
            if (value.equals(item.getValue())) {
                return item;
            }
        }
        return null;
    }

    static <E extends Enum<E> & IBaseEnum> E getByCode(Class<E> enumClass, String code) {
        if (StringUtils.isBlank(code)) return null;
        E[] enumConstants = enumClass.getEnumConstants();
        for (E item : enumConstants) {
            if (code.equals(item.getCode())) {
                return item;
            }
        }
        return null;
    }
}