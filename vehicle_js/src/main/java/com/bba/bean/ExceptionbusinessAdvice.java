package com.bba.bean;


import com.bba.common.vo.ResultVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.CannotCreateTransactionException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 异常拦截器
 * 统一拦截所有的controller请求,并对所有的异常进行捕获处理
 * 项目所有的业务错误或是终止处理，都应该抛出自定义的业务异常，并针对进行异常处理和格式化
 *
 * @author ych
 */
@ControllerAdvice
@Slf4j
public class ExceptionbusinessAdvice {

    @ResponseBody
    @ExceptionHandler({CannotCreateTransactionException.class})
    public ResultVO baseHandler(CannotCreateTransactionException ex) {
        log.error(ex.getMessage(), ex);
        ex.printStackTrace();
        return ResultVO.failResult("服务器繁忙,请重新操作");
    }

    @ResponseBody
    @ExceptionHandler({Exception.class})
    public ResultVO errorHandler(Exception ex) {
        log.error(ex.getMessage(), ex);
        ex.printStackTrace();
        return ResultVO.failResult("系统错误,请联系管理员", ex.getMessage());
    }
}
