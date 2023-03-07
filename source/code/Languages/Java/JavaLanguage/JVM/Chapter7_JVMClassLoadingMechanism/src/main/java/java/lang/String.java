package java.lang;

/**
 * @Author: CoachHe
 * @Date: 2022/11/23 13:22
 * 证明双亲委派机制
 */
public class String {
    static {
        System.out.println("自定义的java.lang.String类被加载了");
    }

    // 这里会报错,原因是实际加载的String对象是由启动类加载器加载的String，这里定义的String是不合法的
    // /Library/Java/JavaVirtualMachines/jdk1.8.0_292_fiber.jdk/Contents/Home/bin/java -javaagent:/Users/coachhe/Library/Application Support/JetBrains/Toolbox/apps/IDEA-U/ch-0/222.4345.14/IntelliJ IDEA.app/Contents/lib/idea_rt.jar=58753:/Users/coachhe/Library/Application Support/JetBrains/Toolbox/apps/IDEA-U/ch-0/222.4345.14/IntelliJ IDEA.app/Contents/bin -Dfile.encoding=UTF-8 -classpath /Library/Java/JavaVirtualMachines/jdk1.8.0_292_fiber.jdk/Contents/Home/jre/lib/charsets.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_292_fiber.jdk/Contents/Home/jre/lib/ext/cldrdata.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_292_fiber.jdk/Contents/Home/jre/lib/ext/dnsns.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_292_fiber.jdk/Contents/Home/jre/lib/ext/jaccess.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_292_fiber.jdk/Contents/Home/jre/lib/ext/localedata.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_292_fiber.jdk/Contents/Home/jre/lib/ext/nashorn.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_292_fiber.jdk/Contents/Home/jre/lib/ext/sunec.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_292_fiber.jdk/Contents/Home/jre/lib/ext/sunjce_provider.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_292_fiber.jdk/Contents/Home/jre/lib/ext/sunpkcs11.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_292_fiber.jdk/Contents/Home/jre/lib/ext/tencentsm_provider.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_292_fiber.jdk/Contents/Home/jre/lib/ext/zipfs.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_292_fiber.jdk/Contents/Home/jre/lib/jce.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_292_fiber.jdk/Contents/Home/jre/lib/jfr.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_292_fiber.jdk/Contents/Home/jre/lib/jsse.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_292_fiber.jdk/Contents/Home/jre/lib/management-agent.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_292_fiber.jdk/Contents/Home/jre/lib/resources.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_292_fiber.jdk/Contents/Home/jre/lib/rt.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_292_fiber.jdk/Contents/Home/lib/dt.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_292_fiber.jdk/Contents/Home/lib/jconsole.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_292_fiber.jdk/Contents/Home/lib/sa-jdi.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_292_fiber.jdk/Contents/Home/lib/tools.jar:/Users/coachhe/programs/java/coachhe-jvm/target/classes:/Users/coachhe/.m2/repository/org/testng/testng/7.5/testng-7.5.jar:/Users/coachhe/.m2/repository/com/google/code/findbugs/jsr305/3.0.1/jsr305-3.0.1.jar:/Users/coachhe/.m2/repository/org/slf4j/slf4j-api/1.7.32/slf4j-api-1.7.32.jar:/Users/coachhe/.m2/repository/com/beust/jcommander/1.78/jcommander-1.78.jar:/Users/coachhe/.m2/repository/org/webjars/jquery/3.5.1/jquery-3.5.1.jar java.lang.String
    // 错误: 在类 java.lang.String 中找不到 main 方法, 请将 main 方法定义为:
    // public static void main(String[] args)
    // 否则 JavaFX 应用程序类必须扩展javafx.application.Application
    public static void main(String[] args) {
        System.out.println("Hello String");
    }
}
