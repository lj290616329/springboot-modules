package com.tsingtec.tsingcore.exception.code;

/**
 * 错误类型枚举
 */
public enum BaseExceptionType {
    /**
     * -1 未登录 跳转至登录页面
     * 200 成功
     * 400 输入错误,无需跳转
     * 403 没有权限,需要跳转
     * 500 系统错误
     * 999 未知错误
     */
    UNAUTHORIZED_ERROR (-1,"登录凭证已过期，请重新登录"),
    SUCCESS(200,"操作成功"),
    USER_ERROR(400,"用户输入异常"),
    PERMISSION_ERROR(403,"您没有权限进行此操作"),
    SYSTEM_ERROR (500,"系统服务异常"),
    OTHER_ERROR(999,"未知异常,请联系管理员"),
    TOKEN_ERROR(401,"token错误");

    BaseExceptionType(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    private String msg;//异常类型中文描述

    private int code; //code

    public String getMsg() {
        return msg;
    }

    public int getCode() {
        return code;
    }
}