package com.tsingtec.tsingcore.exception.handler;

import com.tsingtec.tsingcore.exception.BusinessException;
import com.tsingtec.tsingcore.exception.code.BaseExceptionType;
import com.tsingtec.tsingcore.utils.DataResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.UnauthenticatedException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
@Slf4j
public class RestExceptionHandler {


    @ExceptionHandler(value = BusinessException.class)
    <T> DataResult<T> businessExceptionHandler(BusinessException e) {
        return new DataResult<>(e.getCode(),e.getMsg());
    }

    @ExceptionHandler(value = AuthorizationException.class)
    public <T> DataResult<T>  erroPermission(AuthorizationException e){
        return new DataResult<>(BaseExceptionType.PERMISSION_ERROR.getCode(),"您没有权限进行访问,请联系管理员");
    }

    @ExceptionHandler(value = UnauthenticatedException.class)
    public <T> DataResult<T>  unauthenticatedException(UnauthenticatedException e){
        return new DataResult<>(BaseExceptionType.UNAUTHORIZED_ERROR.getCode(),"您未登录,请先进行登录");

    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    <T> DataResult<T> methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException e) {
        log.error("methodArgumentNotValidExceptionHandler bindingResult.allErrors():{},exception:{}", e.getBindingResult().getAllErrors(), e);
        List<ObjectError> errors = e.getBindingResult().getAllErrors();
        return createValidExceptionResp(errors);
    }

    private <T> DataResult<T> createValidExceptionResp(List<ObjectError> errors) {
        String[] msgs = new String[errors.size()];
        int i = 0;
        for (ObjectError error : errors) {
            msgs[i] = error.getDefaultMessage();
            log.info("msg={}",msgs[i]);
            i++;
        }
        return DataResult.getResult(BaseExceptionType.USER_ERROR.getCode(), msgs[0]);
    }

    @ExceptionHandler(Exception.class)
    <T> DataResult<T> exception(Exception e) {
        log.error(e.toString());
        return new DataResult<>(BaseExceptionType.OTHER_ERROR);
    }
}
