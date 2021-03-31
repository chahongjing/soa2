package com.zjy.agent;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;
import java.util.Objects;

public class MyTransformer implements ClassFileTransformer {
    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined,
                            ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {

        //java自带的方法不进行处理
        if (className.startsWith("java") || className.startsWith("sun")) {
            return classfileBuffer;
        }

        /**
         * 好像使用premain这个className是没问题的，但使用attach时className的.变成了/，所以如果是attach，那么这里需要替换
         */
        className = className.replace('/', '.');

        // todo: 只处理MyApplication类
        if (!className.endsWith("App")) {
            return classfileBuffer;
        }

        try {
            ClassPool classPool = ClassPool.getDefault();

            CtClass ctClass = classPool.get(className);

            CtMethod[] declaredMethods = ctClass.getDeclaredMethods();

            for (CtMethod declaredMethod : declaredMethods) {
                // todo: 只处理testPrint方法
                if (Objects.equals("testPrint", declaredMethod.getName())) {

                    /**
                     * 在方法执行之前加入打印语句
                     */
                    declaredMethod.insertBefore("System.out.println(\"oh my dear,\");");

                    /**
                     * 在方法执行之后加入打印语句
                     */
                    declaredMethod.insertAfter("System.out.println(\"good luck!\\n\");");
                }
            }

            return ctClass.toBytecode();

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
        System.out.println("end");
        return classfileBuffer;
    }

//    @Override
//    public byte[] transform111111(ClassLoader arg0, String arg1, Class<?> arg2,
//                            ProtectionDomain arg3, byte[] arg4)
//            throws IllegalClassFormatException {
//        ClassReader cr = new ClassReader(arg4);
//        ClassNode cn = new ClassNode();
//        cr.accept(cn, 0);
//        for (Object obj : cn.methods) {
//            MethodNode md = (MethodNode) obj;
//            if ("<init>".endsWith(md.name) || "<clinit>".equals(md.name)) {
//                continue;
//            }
//            InsnList insns = md.instructions;
//            InsnList il = new InsnList();
//            il.add(new FieldInsnNode(Opcodes.GETSTATIC, "java/lang/System",
//                    "out", "Ljava/io/PrintStream;"));
//            il.add(new LdcInsnNode("Enter method-> " + cn.name+"."+md.name));
//            il.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL,
//                    "java/io/PrintStream", "println", "(Ljava/lang/String;)V"));
//            insns.insert(il);
//            md.maxStack += 3;
//        }
//        ClassWriter cw = new ClassWriter(0);
//        cn.accept(cw);
//        return cw.toByteArray();
//    }
}
