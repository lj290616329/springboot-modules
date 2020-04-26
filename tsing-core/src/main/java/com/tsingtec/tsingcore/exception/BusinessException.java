package com.tsingtec.tsingcore.exception;


import com.tsingtec.tsingcore.exception.code.BaseExceptionType;

/**
 * 全局错误异常
 */
public class BusinessException extends RuntimeException{
    //异常错误编码
    private int code ;
    //异常信息
    private String msg;

    private BusinessException(){}

    public BusinessException(BaseExceptionType baseExceptionType, String msg) {
        this.code = baseExceptionType.getCode();
        this.msg = msg;
    }

    public BusinessException(BaseExceptionType baseExceptionType) {
        this.code = baseExceptionType.getCode();
        this.msg = baseExceptionType.getMsg();
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
