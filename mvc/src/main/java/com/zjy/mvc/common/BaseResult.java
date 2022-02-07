package com.zjy.mvc.common;

import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;

public class BaseResult<T> implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private ResultStatus status;
    private String message;
    private T value;

    public ResultStatus getStatus() {
        return status;
    }

    public void setStatus(ResultStatus status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }

    /// <summary>
    /// 无参构造函数
    /// </summary>
    public BaseResult() {
        this(ResultStatus.OK, null, null);
    }

    /// <summary>
    /// 有参构造函数
    /// </summary>
    /// <param name="status">返回状态</param>
    public BaseResult(ResultStatus status) {
        this(status, null, null);
    }


    /// <summary>
    /// 有参构造函数
    /// </summary>
    /// <param name="status">返回状态</param>
    /// <param name="message">返回信息</param>
    public BaseResult(ResultStatus status, String message) {
        this(status, message, null);
    }


    /// <summary>
    /// 有参构造函数
    /// </summary>
    /// <param name="status">返回状态</param>
    /// <param name="val">记录</param>
    public BaseResult(ResultStatus status, T val) {
        this(status, null, val);
    }


    /// <summary>
    /// 有参构造函数
    /// </summary>
    /// <param name="status">返回状态</param>
    /// <param name="message">返回信息</param>
    /// <param name="val">记录</param>
    public BaseResult(ResultStatus status, String message, T val) {
        this.status = status;
        this.message = message;
        this.value = val;
    }

    /// <summary>
    /// OK
    /// </summary>
    /// <returns></returns>
    public static <T> BaseResult<T> ok() {
        return ok(null);
    }

    /// <summary>
    /// OK
    /// </summary>
    /// <param name="value">返回值</param>
    /// <returns></returns>
    public static <T> BaseResult<T> ok(T value) {
        return ok(value, null);
    }

    /// <summary>
    /// OK
    /// </summary>
    /// <param name="message">提示信息</param>
    /// <param name="value">返回值</param>
    /// <returns></returns>
    public static <T> BaseResult<T> ok(T value, String message) {
        return new BaseResult<>(ResultStatus.OK, message, value);
    }

    /// <summary>
    /// NO
    /// </summary>
    /// <returns></returns>
    public static <T> BaseResult<T> no() {
        return no(StringUtils.EMPTY);
    }

    /// <summary>
    /// NO
    /// </summary>
    /// <param name="message">提示信息</param>
    /// <returns></returns>
    public static <T> BaseResult<T> no(String message) {
        return no(message, null);
    }

    /// <summary>
    /// NO
    /// </summary>
    /// <param name="message">提示信息</param>
    /// <param name="value">返回值</param>
    /// <returns></returns>
    public static <T> BaseResult<T> no(String message, T value) {
        return new BaseResult<>(ResultStatus.NO, message, value);
    }

    /// <summary>
    /// ERROR
    /// </summary>
    /// <param name="message">提示信息</param>
    /// <returns></returns>
    public static <T> BaseResult<T> error(String message) {
        return error(message, null);
    }

    /// <summary>
    /// ERROR
    /// </summary>
    /// <param name="message">提示信息</param>
    /// <param name="value">返回值</param>
    /// <returns></returns>
    public static <T> BaseResult<T> error(String message, T value) {
        return new BaseResult<>(ResultStatus.ERROR, message, value);
    }
}
