package com.cddr.szd.exception;

import com.cddr.szd.result.Result;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;;

/**
 * @author ZY
 */
@ControllerAdvice//定义全局的异常处理、数据绑定以及全局的预处理逻辑。
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)//捕捉到Exception类型的异常进行处理
    @ResponseBody//将结果转成JSON格式返回给前端
    public Result error(Exception e){
        e.printStackTrace();
        return Result.fail(e.getMessage());
    }


    @ExceptionHandler(SZDException.class)//捕捉到自定义SZDException类型的异常进行处理
    @ResponseBody//将结果转成JSON格式返回给前端
    public Result error(SZDException e){
        return Result.build(null,e.getCode(),e.getMessage());
    }
}
