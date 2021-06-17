package com.syc.blog.utils;


import java.io.Serializable;

public class ResultHelper<T> implements Serializable {
    private T data;
    private boolean success;
    private Integer code;
    private String message;
    private ResultHelper() {}
    public static <T> ResultHelper<T> wrapSuccessfulResult(T data) {
        ResultHelper<T> result = new ResultHelper<T>();
        result.data = data;
        result.success = true;
        result.code = 0;
        return result;
    }
    public static <T> ResultHelper<T> wrapSuccessfulResult(String message, T data) {
        ResultHelper<T> result = new ResultHelper<T>();
        result.data = data;
        result.success = true;
        result.code = 0;
        result.message = message;
        return result;
    }

    public static <T> ResultHelper<T> wrapErrorResult(Integer code, String message) {
        ResultHelper<T> result = new ResultHelper<T>();
        result.success = false;
        result.code = code;
        result.message = message;
        return result;
    }
    public T getData() {
        return this.data;
    }
    public ResultHelper<T> setData(T data) {
        this.data = data;
        return this;
    }
    public boolean isSuccess() {
        return this.success;
    }
    public ResultHelper<T> setSuccess(boolean success) {
        this.success = success;
        return this;
    }
    public Integer getCode() {
        return this.code;
    }
    public ResultHelper<T> setCode(Integer code) {
        this.code = code;
        return this;
    }
    public String getMessage() {
        return this.message;
    }
    public ResultHelper<T> setMessage(String message) {
        this.message = message;
        return this;
    }
    @Override
    public String toString() {
        return "{" +
                "success=" +
                this.success +
                "," +
                "code=" +
                this.code +
                "," +
                "message=" +
                this.message +
                "," +
                "data=" +
                this.data +
                "}";
    }
}

