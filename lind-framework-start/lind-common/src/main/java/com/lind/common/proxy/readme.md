# jdk动态代理
* 只能代理接口，可以定义一类需要代理的接口，可以通过注解去标识他们，后台在代理时根据注解去添加代理类
* ProviderProxy代理类里写核心逻辑
* ProviderProxyFactoryBean用来构建proxy的工厂bean
* ProviderBeanDefinitionRegistry用来注册bean
* 本例中定义了两个注解，用来代理了消息生产者和生产方法，分别是`@MessageProvider`和`@MessageSend`,其中@MessageProvider标识的接口将会被代理
* 本例中代理类`MessageProxy`去代理了接口`PeopleMessageService`,并以它做为`MessageService`对象里，方法 `send`的参数

# ImportBeanDefinitionRegistrar
* `ImportBeanDefinitionRegistrar`是spring对外提供动态注册beanDefinition的接口，spring内部大部分套路也是用该接口进行动态注册beanDefinition的。
* `ImportBeanDefinitionRegistrar`接口不是直接注册Bean到IOC容器，它的执行时机比较早，准确的说更像是注册Bean的定义信息以便后面的Bean的创建。
* `ImportBeanDefinitionRegistrar`接口提供了`registerBeanDefinitions`方便让子类进行重写。该方法提供`BeanDefinitionRegistry`类型的参数，
让开发者调用BeanDefinitionRegistry的registerBeanDefinition方法传入`BeanDefinitionName`和对应的`BeanDefinition`对象，直接往容器中注册。
* ImportBeanDefinitionRegistrar通常和@Import注解配合使用。`@Import`会将ImportBeanDefinitionRegistrar的实现类注入到spring 容器中。
从而spring 容器可以遍历调用 ImportBeanDefinitionRegistrar接口的 registerBeanDefinitions方法。
> 注意：这里我们可以注册FactoryBean的BeanDefinition，给FactoryBean设置动态参数。将要注册的真正要注入的bean作为参数，然后让spring容器利用这个FactoryBean的构造方法进行实例化。（通过setAutowireMode(3)）mybatis整合spring的底层就是这么实现的。

# 为什么类可以动态的生成？
这就涉及到Java虚拟机的类加载机制了，推荐翻看《深入理解Java虚拟机》7.3节 类加载的过程。
Java虚拟机类加载过程主要分为五个阶段：加载、验证、准备、解析、初始化。其中加载阶段需要完成以下3件事情：
1. 通过一个类的全限定名来获取定义此类的二进制字节流
2. 将这个字节流所代表的静态存储结构转化为方法区的运行时数据结构
3. 在内存中生成一个代表这个类的 java.lang.Class 对象，作为方法区这个类的各种数据访问入口

由于虚拟机规范对这3点要求并不具体，所以实际的实现是非常灵活的，关于第1点，获取类的二进制字节流（class字节码）就有很多途径：

1. 从ZIP包获取，这是JAR、EAR、WAR等格式的基础
2. 从网络中获取，典型的应用是 Applet
3. 运行时计算生成，这种场景使用最多的是动态代理技术，在 java.lang.reflect.Proxy 类中，就是用了 ProxyGenerator.generateProxyClass 来为特定接口生成形式为 *$Proxy 的代理类的二进制字节流
4. 由其它文件生成，典型应用是JSP，即由JSP文件生成对应的Class类
4. 从数据库中获取等等

所以，动态代理就是想办法，根据接口或目标对象，计算出代理类的字节码，然后再加载到JVM中使用。但是如何计算？如何生成？情况也许比想象的复杂得多，我们需要借助现有的方案。

# 常见的字节码操作类库
> 这里有一些介绍：https://java-source.net/open-source/bytecode-libraries
* Apache BCEL (Byte Code Engineering Library)：是Java classworking广泛使用的一种框架，它可以深入到JVM汇编语言进行类操作的细节。
* ObjectWeb ASM：是一个Java字节码操作框架。它可以用于直接以二进制形式动态生成stub根类或其他代理类，或者在加载时动态修改类。
* CGLIB(Code Generation Library)：是一个功能强大，高性能和高质量的代码生成库，用于扩展JAVA类并在运行时实现接口。
* Javassist：是Java的加载时反射系统，它是一个用于在Java中编辑字节码的类库; 它使Java程序能够在运行时定义新类，并在JVM加载之前修改类文件。

# 实现动态代理的思考方向
为了让生成的代理类与目标对象（真实主题角色）保持一致性，从现在开始将介绍以下两种最常见的方式：

* 通过实现接口的方式 -> JDK动态代理
* 通过继承类的方式 -> CGLIB动态代理
> 注：使用ASM对使用者要求比较高，使用Javassist会比较麻烦

# JDK动态代理
JDK动态代理主要涉及两个类：java.lang.reflect.Proxy 和 java.lang.reflect.InvocationHandler，我们仍然通过案例来学习.

编写一个调用逻辑处理器 LogHandler 类，提供日志增强功能，并实现 InvocationHandler 接口；在 `LogHandler`中维护一个目标对象，这个对象是被代理的对象（真实主题角色）；在 `invoke`方法中编写方法调用的逻辑处理.
```
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Date;

public class LogHandler implements InvocationHandler {
    Object target;  // 被代理的对象，实际的方法执行者

    public LogHandler(Object target) {
        this.target = target;
    }
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        before();
        Object result = method.invoke(target, args);  // 调用 target 的 method 方法
        after();
        return result;  // 返回方法的执行结果
    }
    // 调用invoke方法之前执行
    private void before() {
        System.out.println(String.format("log start time [%s] ", new Date()));
    }
    // 调用invoke方法之后执行
    private void after() {
        System.out.println(String.format("log end time [%s] ", new Date()));
    }
}
```
编写客户端，获取动态生成的代理类的对象须借助 Proxy 类的 newProxyInstance 方法，具体步骤可见代码和注释
```
import proxy.UserService;
import proxy.UserServiceImpl;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

public class Client2 {
    public static void main(String[] args) throws IllegalAccessException, InstantiationException {
        // 设置变量可以保存动态代理类，默认名称以 $Proxy0 格式命名
        // System.getProperties().setProperty("sun.misc.ProxyGenerator.saveGeneratedFiles", "true");
        // 1. 创建被代理的对象，UserService接口的实现类
        UserServiceImpl userServiceImpl = new UserServiceImpl();
        // 2. 获取对应的 ClassLoader
        ClassLoader classLoader = userServiceImpl.getClass().getClassLoader();
        // 3. 获取所有接口的Class，这里的UserServiceImpl只实现了一个接口UserService，
        Class[] interfaces = userServiceImpl.getClass().getInterfaces();
        // 4. 创建一个将传给代理类的调用请求处理器，处理所有的代理对象上的方法调用
        //     这里创建的是一个自定义的日志处理器，须传入实际的执行对象 userServiceImpl
        InvocationHandler logHandler = new LogHandler(userServiceImpl);
        /*
		   5.根据上面提供的信息，创建代理对象 在这个过程中，
               a.JDK会通过根据传入的参数信息动态地在内存中创建和.class 文件等同的字节码
               b.然后根据相应的字节码转换成对应的class，
               c.然后调用newInstance()创建代理实例
		 */
        UserService proxy = (UserService) Proxy.newProxyInstance(classLoader, interfaces, logHandler);
        // 调用代理的方法
        proxy.select();
        proxy.update();
        
        // 保存JDK动态代理生成的代理类，类名保存为 UserServiceProxy
        // ProxyUtils.generateClassFile(userServiceImpl.getClass(), "UserServiceProxy");
    }
}
```