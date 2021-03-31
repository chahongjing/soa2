package com.zjy.agent;

import java.lang.instrument.Instrumentation;

public class PremainMain {
    public static void premain(String agentArgs, Instrumentation instrumentation) {
        System.out.println("premain");
        instrumentation.addTransformer(new MyTransformer());
    }
}
