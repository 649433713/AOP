package yinywf.springbootdemo.handle;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import yinywf.springbootdemo.config.ProjectUrlConfig;
import yinywf.springbootdemo.exceptions.SellException;
import yinywf.springbootdemo.exceptions.SellerAuthorizeException;
import yinywf.springbootdemo.utils.ResultVOUtil;
import yinywf.springbootdemo.vo.ResultVO;

/**
 * @author yinywf
 * Created on 2017/10/16
 */
@ControllerAdvice
@Slf4j
public class ExceptionHandle {

    @Autowired
    private ProjectUrlConfig projectUrlConfig;

    //拦截登录异常
    //http://sell.natapp4.cc/sell/wechat/qrAuthorize?returnUrl=http://sell.natapp4.cc/sell/seller/login
    @ExceptionHandler(value = SellerAuthorizeException.class)
    public ModelAndView handlerAuthorizeException() {
        return new ModelAndView("redirect:"
                .concat(projectUrlConfig.getWechatOpenAuthorize())
                .concat("/sell/wechat/qrAuthorize")
                .concat("?returnUrl=")
                .concat(projectUrlConfig.getSell())
                .concat("/sell/seller/login"));
    }

    //拦截异常
    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public ResultVO handle(Exception e){
        if (e instanceof SellException){
            SellException sellException=(SellException)e;
            return ResultVOUtil.error(sellException.getCode(), sellException.getMessage());
        }else {
            log.error("[系统异常] {}",e);
            return ResultVOUtil.error(-1,"未知错误");
        }
    }
}