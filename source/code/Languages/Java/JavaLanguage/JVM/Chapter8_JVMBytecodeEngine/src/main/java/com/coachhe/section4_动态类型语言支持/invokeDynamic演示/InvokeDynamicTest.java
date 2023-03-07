package com.coachhe.section4_动态类型语言支持.invokeDynamic演示;

import java.lang.invoke.*;

/**
 * @author CoachHe
 * @date 2022/12/28 23:38
 **/
public class InvokeDynamicTest {

    public static void testMethod(String s) {
        System.out.println("hello String:" + s);
    }

    public static CallSite BootstrapMethod(MethodHandles.Lookup lookup, String name, MethodType mt) throws Throwable {
        return new ConstantCallSite(lookup.findStatic(InvokeDynamicTest.class, name, mt));
    }

    public static MethodType MT_BootstrapMethod() {
        return MethodType.
                fromMethodDescriptorString(
                        "(Ljava/lang/invoke/MethodHandle$Loopup;Ljava/lang/String; Ljava/lang/invoke/MethodType;)Ljava/lang/invoke/CallSite;", null
                );
    }

    private static MethodHandle MH_BootstrapMethod() throws Throwable {
        return MethodHandles.lookup().findStatic(InvokeDynamicTest.class, "BootstrapMethod", MT_BootstrapMethod());
    }

    private static MethodHandle INDY_BootstrapMethod() throws Throwable {
        CallSite cs = (CallSite) MH_BootstrapMethod().invokeWithArguments(MethodHandles.lookup(), "testMethod",
                MethodType.fromMethodDescriptorString("(Ljava/lang/String;)V", null));
        return cs.dynamicInvoker();
    }

}
