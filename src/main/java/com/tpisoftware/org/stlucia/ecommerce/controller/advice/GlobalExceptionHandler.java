package com.tpisoftware.org.stlucia.ecommerce.controller.advice;

import com.tpisoftware.org.stlucia.ecommerce.exception.UserInvalidException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value={UserInvalidException.class})
    public String handleException(Exception e, HttpServletRequest request){
        Map<String,Object> map = new HashMap<>();
        //傳入自己定義的錯誤狀態碼  4xx 5xx，否則不會進入訂製錯誤頁面的解析流程
        /*
         * Integer statusCode = (Integer) request
         .getAttribute("javax.servlet.error.status_code");
         */
        request.setAttribute("javax.servlet.error.status_code",403);
        map.put("code", e.getMessage());
        map.put("message","本帳號無使用權限");

        request.setAttribute("ext",map);
        //轉發到 /error
        return "forward:/error";
    }

}