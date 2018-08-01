package com.lick.intercptor;

import net.bytebuddy.implementation.bind.annotation.Origin;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.bytebuddy.implementation.bind.annotation.SuperCall;

import java.lang.reflect.Method;
import java.util.concurrent.Callable;

/**
 * @author lichengkai
 * 2018-08-01: 下午3:13
 * Copyright(c) for dianwoda
 */
public class TimeInterceptor {
    @RuntimeType
    public static Object intercept(@Origin Method method,
                                   @SuperCall Callable<?> callable) throws Exception {
        long start = System.currentTimeMillis();
        System.out.printf("start intercept\n");
        try {
            return callable.call();
        }catch (Exception e) {
            System.out.printf("\nerror:"+e);
        } finally{
            System.out.printf(method+": took " +(System.currentTimeMillis() - start)+"\n");
        }
        return null;
    }
}
