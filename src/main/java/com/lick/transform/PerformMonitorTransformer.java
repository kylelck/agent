package com.lick.transform;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtBehavior;
import javassist.CtClass;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;
import java.util.HashSet;
import java.util.Set;

/** javassist实现字节码增强
 * @author lichengkai
 * 2018-07-10: 下午3:05
 * Copyright(c) for dianwoda
 */
public class PerformMonitorTransformer implements ClassFileTransformer {
    private static final Set<String> classNameSet = new HashSet<>();
    static {
        classNameSet.add("com.lick.agenttest.AgentTestApplication");
    }
    @Override
    public byte[] transform(ClassLoader loader,
                            String className,
                            Class<?> classBeingRedefined,
                            ProtectionDomain protectionDomain,
                            byte[] classfileBuffer) throws IllegalClassFormatException {
        try {
            String currentClassName = className.replace("/",".");
            if (!classNameSet.contains(currentClassName)) {
                return null;
            }
            System.out.printf("transform: ["+currentClassName+"]\n");
            CtClass ctClass = ClassPool.getDefault().get(currentClassName);
            System.out.printf(ctClass.getName());
            CtBehavior[] methods = ctClass.getDeclaredBehaviors();
            System.out.printf("methods size:"+methods.length);
            for (CtBehavior behavior : methods) {
                enhanceMethod(behavior);
                System.out.printf("methods:"+behavior.getName());
            }
            return ctClass.toBytecode();
        }catch (Exception e) {
            System.out.printf("error");
            e.printStackTrace();
        }
        return null;
    }
    private void enhanceMethod(CtBehavior behavior) throws Exception {
        if (behavior.isEmpty()) {
            return;
        }
        String methodName = behavior.getName();
        System.out.printf("methodName:"+methodName+"\n");
        if (methodName.equalsIgnoreCase("main")) { // 不提升main方法
            return;
        }
        final StringBuilder source = new StringBuilder();
        source.append("{")
                .append("long start = System.nanoTime();\n") // 前置增强: 打入时间戳
                .append("$_ = $proceed($$);\n") // 保留原有的代码处理逻辑
                .append("System.out.print(\"method:[" + methodName + "]\");").append("\n")
                .append("System.out.println(\" cost:[\" +(System.nanoTime() -start)+ \"ns]\");") // 后置增强
                .append("}");

        ExprEditor  editor = new ExprEditor() {
            @Override
            public void edit(MethodCall methodCall) throws CannotCompileException {
                methodCall.replace(source.toString());
            }
        };
        behavior.instrument(editor);
    }
}
