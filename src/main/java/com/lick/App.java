package com.lick;

import com.lick.intercptor.AdviceInterceptor;
import com.lick.intercptor.TimeInterceptor;
import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.asm.Advice;
import net.bytebuddy.asm.AsmVisitorWrapper;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.matcher.ElementMatchers;
import net.bytebuddy.utility.JavaModule;

import java.lang.instrument.Instrumentation;

import static net.bytebuddy.matcher.ElementMatchers.takesArguments;

/**
 * Hello world!
 *
 */
public class App 
{

   public static void premain(String agentArgs, Instrumentation inst) {
      /***
       * 使用javassist进行字节码增强
       */
       System.out.printf("this is an agent.");
//       PerformMonitorTransformer transformer = new PerformMonitorTransformer();
//       inst.addTransformer(transformer);
//       System.out.printf("args:"+agentArgs+"\n");

       /**
        * 使用bytebuddy进行字节码增强
        */
       System.out.printf("this is an agent premain info\n");
       AgentBuilder.Transformer transformer = new AgentBuilder.Transformer() {
           @Override
           public DynamicType.Builder<?> transform(DynamicType.Builder<?> builder, TypeDescription typeDescription, ClassLoader classLoader, JavaModule module) {
//               return builder
//                       //指定需要拦截的对象,当前拦截所有任意对象
//                       .method(ElementMatchers.<MethodDescription>any())
//                       //指定使用拦截的方法
//                       .intercept(MethodDelegation.to(TimeInterceptor.class));
               System.out.printf("ll:"+typeDescription.getName()+"\n");
               final AsmVisitorWrapper asm = Advice.to(AdviceInterceptor.class)
                       .on(ElementMatchers.isMethod()
                               .and(ElementMatchers.named("load"))
                       .and(takesArguments(1)));
               builder = builder.visit(asm);
               return  builder;
           }
       };
       //设置对象的状态监听
       AgentBuilder.Listener listener = new AgentBuilder.Listener() {
           @Override
           public void onDiscovery(String typeName, ClassLoader classLoader, JavaModule module, boolean loaded) {
//               System.out.printf("onDiscovery:"+typeName+"\n");
           }

           @Override
           public void onTransformation(TypeDescription typeDescription, ClassLoader classLoader, JavaModule module, boolean loaded, DynamicType dynamicType) {
//               System.out.printf("onTransformation:"+typeDescription.getName()+"\n");

           }

           @Override
           public void onIgnored(TypeDescription typeDescription, ClassLoader classLoader, JavaModule module, boolean loaded) {
//               System.out.printf("onIgnored:"+typeDescription.getName()+"\n");

           }

           @Override
           public void onError(String typeName, ClassLoader classLoader, JavaModule module, boolean loaded, Throwable throwable) {
               System.out.printf("onError:"+typeName+";erorr:"+throwable);

           }

           @Override
           public void onComplete(String typeName, ClassLoader classLoader, JavaModule module, boolean loaded) {
//               System.out.printf("onComplete:"+typeName+"\n");

           }
       };
       new AgentBuilder
               .Default()
               // 指定需要拦截的类
               .type(ElementMatchers.nameStartsWith("com.lick.agenttest.example"))
               .transform(transformer)
               .with(listener)
               .installOn(inst);
   }
    public static void premain(String agentArgs) {

   }


}
