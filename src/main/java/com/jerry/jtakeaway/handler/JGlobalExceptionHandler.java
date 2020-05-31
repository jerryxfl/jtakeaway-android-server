package com.jerry.jtakeaway.handler;

import com.jerry.jtakeaway.exception.JException;
import com.jerry.jtakeaway.utils.RUtils;
import com.jerry.jtakeaway.utils.bean.Renum;
import com.jerry.jtakeaway.utils.bean.Result;
import io.swagger.annotations.ApiResponse;
import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@ControllerAdvice
public class JGlobalExceptionHandler {

    @ExceptionHandler(JException.class)
    @ResponseBody
    public Result handleException(JException e){
        return RUtils.Err(Integer.valueOf(e.getErrorCode()),e.getErrorMsg());
    }


    @ExceptionHandler(RuntimeException.class)
    @ResponseBody
    public Result runtimeExceptionHandler(RuntimeException ex){
        return RUtils.Err(-1,"服务器内部错误");
    }

    //空指针异常
    @ExceptionHandler(NullPointerException.class)
    @ResponseBody
    public Result nullPointerExceptionHandler(NullPointerException ex) {
        return RUtils.Err(-1,"服务器内部错误");
    }

    //类型转换异常
    @ExceptionHandler(ClassCastException.class)
    @ResponseBody
    public Result classCastExceptionHandler(ClassCastException ex) {
        return RUtils.Err(-1,"服务器内部错误");
    }

    //IO异常
    @ExceptionHandler(IOException.class)
    @ResponseBody
    public Result iOExceptionHandler(IOException ex) {
        return RUtils.Err(-1,"服务器内部错误");
    }

    //未知方法异常
    @ExceptionHandler(NoSuchMethodException.class)
    @ResponseBody
    public Result noSuchMethodExceptionHandler(NoSuchMethodException ex) {
        return RUtils.Err(-1,"服务器内部错误");

    }

    //数组越界异常
    @ExceptionHandler(IndexOutOfBoundsException.class)
    @ResponseBody
    public Result indexOutOfBoundsExceptionHandler(IndexOutOfBoundsException ex) {
        return RUtils.Err(-1,"服务器内部错误");
    }

    //400错误
    @ExceptionHandler({HttpMessageNotReadableException.class})
    @ResponseBody
    public Result requestNotReadable(HttpMessageNotReadableException ex) {
        return RUtils.Err(-1,"页面未找到");

    }

    //400错误
    @ExceptionHandler({TypeMismatchException.class})
    @ResponseBody
    public Result requestTypeMismatch(TypeMismatchException ex) {
        return RUtils.Err(-1,"页面未找到");

    }

    //404错误
    @ExceptionHandler({NoHandlerFoundException.class})
    @ResponseBody
    public Result noHandlerFoundException(NoHandlerFoundException ex) {
        return RUtils.Err(-1,"页面丢失");
    }

    //400错误
    @ExceptionHandler({MissingServletRequestParameterException.class})
    @ResponseBody
    public Result requestMissingServletRequest(MissingServletRequestParameterException ex) {
        return RUtils.Err(-1,"页面丢失");
    }

    //405错误
    @ExceptionHandler({HttpRequestMethodNotSupportedException.class})
    @ResponseBody
    public Result request405(HttpRequestMethodNotSupportedException ex) {
        return RUtils.Err(-1,"页面丢失");
    }

    //406错误
    @ExceptionHandler({HttpMediaTypeNotAcceptableException.class})
    @ResponseBody
    public Result request406(HttpMediaTypeNotAcceptableException ex) {
        return RUtils.Err(-1,"页面丢失");
    }

    //500错误
    @ExceptionHandler({ConversionNotSupportedException.class, HttpMessageNotWritableException.class})
    @ResponseBody
    public Result server500(RuntimeException ex) {
        return RUtils.Err(-1,"页面丢失");
    }

    //栈溢出
    @ExceptionHandler({StackOverflowError.class})
    @ResponseBody
    public Result requestStackOverflow(StackOverflowError ex) {
        return RUtils.Err(-1,"服务器内部错误");
    }

    //除数不能为0
    @ExceptionHandler({ArithmeticException.class})
    @ResponseBody
    public Result arithmeticException(ArithmeticException ex) {
        return RUtils.Err(-1,"服务器内部错误");
    }


    //其他错误
    @ExceptionHandler({Exception.class})
    @ResponseBody
    public Result exception(Exception ex) {
        return RUtils.Err(-1,"服务器内部错误");
    }

}
