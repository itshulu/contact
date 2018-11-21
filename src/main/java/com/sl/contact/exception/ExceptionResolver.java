package com.sl.contact.exception;

import com.sl.contact.entity.JsonMsg;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * @author 舒露
 */
@ControllerAdvice
public class ExceptionResolver {
    private static final Logger logger = LoggerFactory.getLogger(ExceptionResolver.class);
    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public JsonMsg noFindHandler(HttpServletRequest request, Exception e) {
        JsonMsg jsonMsg = new JsonMsg();
        jsonMsg.setContact(null);
        jsonMsg.setMsg(e.getMessage() + request.getRequestURL().toString());
        jsonMsg.setStatus(404);
        logger.error("404:"+e.getMessage());
        return jsonMsg;
    }
}
