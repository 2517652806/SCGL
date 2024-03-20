package com.cddr.szd.exception;

import com.cddr.szd.result.Result;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author ZY
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public Result handleException(Exception e){
        return Result.fail(StringUtils.hasLength(e.getMessage())? e.getMessage():"操作失败");
    }
}
