package com.zjy.attach;

import java.lang.instrument.Instrumentation;
import java.lang.instrument.UnmodifiableClassException;

public class JpAgent {

    public static void premain(String agentArgs){
        System.out.println("我是一个参数的 Java Agent premain");
    }

    public static void agentmain (String agentArgs, Instrumentation inst) throws UnmodifiableClassException {
        inst.addTransformer(new JpClassFileTransformerDemo(), true);
        // retransformClasses 是 Java SE 6 里面的新方法，它跟 redefineClasses 一样，可以批量转换类定义
        inst.retransformClasses(Dog.class);
        System.out.println("我是两个参数的 Java Agent agentmain");

    }

    public static void agentmain (String agentArgs){
        System.out.println("我是一个参数的 Java Agent agentmain");
    }
}