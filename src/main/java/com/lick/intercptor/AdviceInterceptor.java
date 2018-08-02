package com.lick.intercptor;

import net.bytebuddy.asm.Advice;

/**
 * @author lichengkai
 * 2018-08-02: 下午2:51
 * Copyright(c) for dianwoda
 */
public class AdviceInterceptor {

    @Advice.OnMethodEnter
    public static void enter(@Advice.Origin("#m") String method, @Advice.Argument(0) String name ) {
        System.out.printf("method:"+method+":"+name+"\n");
    }
}
