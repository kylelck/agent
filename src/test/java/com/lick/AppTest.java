package com.lick;

import static net.bytebuddy.matcher.ElementMatchers.*;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.agent.ByteBuddyAgent;
import net.bytebuddy.dynamic.loading.ClassReloadingStrategy;
import net.bytebuddy.implementation.FixedValue;
import org.junit.BeforeClass;
import org.junit.Test;

import java.lang.reflect.Method;

/**
 * Unit test for simple App.
 */
public class AppTest 
{
    private ByteBuddy bytebuddy;

    @BeforeClass
    public static void setupByteBuddyAgent() {
        ByteBuddyAgent.install();
    }
    /**
     * Rigorous Test :-)
     */
    @Test
    public void shouldAnswerWithTrue()
    {
        assertTrue( true );
    }

    /**
     * 测试bytebuddy动态创建子类并修改其方法
     */
    @Test
    public void byteBuddyTest() throws Exception {
        try {
            ByteBuddyAgent.install();
            Foo foo =
                    new ByteBuddy()
                            .redefine(Foo.class)
                            .name(Foo.class.getName())
                            .method(named("foo").and(takesArguments(0))).intercept(FixedValue.value("one"))
                            .method(named("foo").and(takesArguments(1))).intercept(FixedValue.value("two"))
//                            .method(isDeclaredBy(Foo.class)).intercept(FixedValue.value("three"))
                            .make()
                            .load(Foo.class.getClassLoader(), ClassReloadingStrategy.fromInstalledAgent()).getLoaded().newInstance();
            System.out.printf("true:"+foo.foo()+";"+foo.foo("1")+";"+foo.bar());
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static  class Foo {
        public String bar() { return null; }
        public String foo() { return null; }
        public String foo(Object o) { return null; }
        String m() { return "foo"; }
    }
    public static class Bar {
        String m() { return "bar"; }
    }
}
