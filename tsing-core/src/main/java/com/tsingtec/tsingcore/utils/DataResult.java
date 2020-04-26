package com.tsingtec.tsingcore.utils;

import com.tsingtec.tsingcore.exception.code.BaseExceptionType;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 全局返回数据
 * @param <T>
 */
@Data
@Api("全局返回数据")
public class DataResult<T>{

    /**
     * 请求响应code，200为成功 其他为失败
     */
    @ApiModelProperty(value = "请求响应code，200为成功 其他为失败", name = "code")
    private int code;

    /**
     * 响应异常码详细信息
     */
    @ApiModelProperty(value = "响应异常码详细信息", name = "msg")
    private String msg;

    /**
     * 响应内容 ， code 200 时为 返回 数据
     */
    @ApiModelProperty(value = "需要返回的数据", name = "data")
    private T data;

    public DataResult(int code, T data) {
        this.code = code;
        this.data = data;
        this.msg=null;
    }

    public DataResult(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public DataResult(int code, String msg) {
        this.code = code;
        this.msg = msg;
        this.data=null;
    }


    public DataResult() {
        this.code = BaseExceptionType.SUCCESS.getCode();
        this.msg=BaseExceptionType.SUCCESS.getMsg();
        this.data=null;
    }

    public DataResult(T data) {
        this.data = data;
        this.code=BaseExceptionType.SUCCESS.getCode();
        this.msg=BaseExceptionType.SUCCESS.getMsg();
    }

    public DataResult(BaseExceptionType baseExceptionType) {
        this.data = null;
        this.code = baseExceptionType.getCode();
        this.msg = baseExceptionType.getMsg();
    }

    public DataResult(BaseExceptionType baseExceptionType, T data) {
        this.data = data;
        this.code = baseExceptionType.getCode();
        this.msg = baseExceptionType.getMsg();
    }
    
    public static <T>DataResult success(){
        return new <T>DataResult();
    }
    public static <T>DataResult success(T data){
        return new <T>DataResult(data);
    }
    public static <T>DataResult getResult(int code,String msg,T data){
        return new <T>DataResult(code,msg,data);
    }
    public static <T>DataResult getResult(int code,String msg){
        return new <T>DataResult(code,msg);
    }
    public static <T>DataResult getResult(BaseExceptionType baseExceptionType){
        return new <T>DataResult(baseExceptionType);
    }
    public static <T>DataResult getResult(BaseExceptionType baseExceptionType,T data){

        return new <T>DataResult(baseExceptionType,data);
    }
}
