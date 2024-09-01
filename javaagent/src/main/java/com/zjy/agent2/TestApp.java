package com.zjy.agent2;

import com.sun.tools.attach.VirtualMachine;

import java.io.File;
import java.lang.instrument.Instrumentation;
import java.lang.management.ManagementFactory;
import java.nio.file.Paths;

public class TestApp {

    //main执行前调用
    public static void premain(String agentArgs, Instrumentation inst) {
        System.out.println("execute premain method");
    }
    //main主方法入口
    public static void main(String... args) {
        System.out.println("execute main method ");
        attach();
    }
    //main执行后调用
    public static void agentmain(String agentArgs, Instrumentation inst) {
        System.out.println("execute agentmain method");
    }

    private static void attach() {
        File agentFile = Paths.get("D:\\app\\tinywhale\\tinywhale-deploy\\target\\tinywhale-deploy-1.0-SNAPSHOT.jar").toFile();
        try {
            String name = ManagementFactory.getRuntimeMXBean().getName();
            String pid = name.split("@")[0];
            VirtualMachine jvm = com.sun.tools.attach.VirtualMachine.attach(pid);
            jvm.loadAgent(agentFile.getAbsolutePath());
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
