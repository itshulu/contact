package com.sl.contact.aop;

import com.sl.contact.entity.Contact;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author 舒露
 */
//@Component
//@Aspect
public class DaoAop {
    private static final Logger logger = LoggerFactory.getLogger(DaoAop.class);
    private static final String NAME = "name";
    private static final String TEL = "tel";
    private static final String CONTACT = "contact:";
    private static final String FIND_ALL = "findAll";
    private static final String FIND = "find";
    private static final String DEL = "del";
    private static final String SAVE = "save";
    private static final String UPDATE = "update";

    private final Jedis jedis;

    @Autowired
    public DaoAop(Jedis jedis) {
        this.jedis = jedis;
    }

    /**
     * 指定切入点表单式： 拦截哪些方法； 即为哪些类生成代理对象
     */
    @Pointcut("execution(* com.sl.contact.service.impl.*.*(..))")
    public void pointCut() {
    }

    @Around("pointCut()")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        String methodName = pjp.getSignature().getName();
        logger.info("aop method: " + methodName);
        if (methodName.startsWith(FIND_ALL)) {
            return findAll(pjp);
        } else if (methodName.startsWith(FIND)) {
            return find(pjp);
        } else if (methodName.startsWith(DEL)) {
            return del(pjp);
        } else if (methodName.startsWith(SAVE)) {
            return save(pjp);
        } else if (methodName.startsWith(UPDATE)) {
            return update(pjp);
        } else {
            throw new RuntimeException("none match method name : " + methodName);
        }
    }

    private Object update(ProceedingJoinPoint pjp) throws Throwable {
        logger.info("aop update method args: " + Arrays.toString(pjp.getArgs()));
        return saveCacheAndInvokeMethod(pjp);
    }

    private Object del(ProceedingJoinPoint pjp) throws Throwable {
        logger.info("aop del method args: " + Arrays.toString(pjp.getArgs()));
        Object proceed = pjp.proceed();
        jedis.hdel(CONTACT + pjp.getArgs()[0], NAME, TEL);
        logger.debug("del cache key: " + pjp.getArgs()[0] + " success");
        return proceed;
    }

    private Object find(ProceedingJoinPoint pjp) throws Throwable {
        logger.info("aop find method args: " + Arrays.toString(pjp.getArgs()));
        String name = jedis.hget(CONTACT + pjp.getArgs()[0], NAME);
        if (name != null) {
            logger.debug("found cache in redis, cache key is: " + pjp.getArgs()[0]);
            String tel = jedis.hget(CONTACT + pjp.getArgs()[0], TEL);
            Contact contact = new Contact();
            contact.setName(name);
            contact.setTel(tel);
            contact.setId(String.valueOf(pjp.getArgs()[0]));
            return contact;
        } else {
            logger.debug("not found cache in redis, now invoke method: " + pjp.getSignature().getName());
            return saveCacheAndInvokeMethod(pjp);
        }
    }

    @SuppressWarnings("unchecked")
    private Object findAll(ProceedingJoinPoint pjp) throws Throwable {
        logger.info("aop findAll method args: " + Arrays.toString(pjp.getArgs()));
        Set<String> keys = jedis.keys("contact:*");
        //缓存中没有数据
        if (keys.size() == 0) {
            logger.debug("not found cache in redis, now invoke method: " + pjp.getSignature().getName());
            Object proceed = pjp.proceed();
            if (proceed instanceof ArrayList<?>) {
                ((ArrayList<Contact>) proceed).forEach(contact -> {
                    jedis.hset(CONTACT + contact.getId(), NAME, contact.getName());
                    jedis.hset(CONTACT + contact.getId(), TEL, contact.getTel());
                });
                return proceed;
            }
            throw new RuntimeException("return type illegal, return type is: " + proceed.getClass().getSimpleName());
        } else {
            return keys.stream().map(key -> {
                logger.debug("found cache in redis, cache key is: " + key);
                String name = jedis.hget(key, NAME);
                String tel = jedis.hget(key, TEL);
                Contact contact = new Contact();
                contact.setName(name);
                contact.setTel(tel);
                contact.setId(key.split(":")[1]);
                return contact;
            }).collect(Collectors.toList());
        }
    }

    private Object save(ProceedingJoinPoint pjp) throws Throwable {
        logger.info("aop save method args: " + Arrays.toString(pjp.getArgs()));
        return saveCacheAndInvokeMethod(pjp);
    }

    private Object saveCacheAndInvokeMethod(ProceedingJoinPoint pjp) throws Throwable {
        Object proceed = pjp.proceed();
        if (proceed instanceof Contact) {
            Contact contact = (Contact) proceed;
            jedis.hset(CONTACT + contact.getId(), NAME, contact.getName());
            jedis.hset(CONTACT + contact.getId(), TEL, contact.getTel());
            logger.debug("save entity to redis success, now return");
            return contact;
        }
        throw new RuntimeException("return type illegal, return type is: " + proceed.getClass().getSimpleName());
    }

}
