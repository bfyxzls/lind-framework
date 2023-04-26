# 介绍
SPI（service provider interface）机制是JDK内置的一种服务发现机制，可以动态的发现服务，即服务提供商，它通过在ClassPath路径下的
META-INF/services文件夹查找文件，自动加载文件里所定义的类。目前这种大部分都利用SPI的机制进行服务提供，比如:dubbo、spring、JDBC、等;

# spi解决了什么问题？
由于classLoader加载类的时候采用是【双亲委托模式】，意思是：首先委托父类去加载器获取，若父类加载器存在则直接返回，若加载器无法完成此加载任务，
自己才去加载。该加载存在的弊端就是上层的类加载永远无法加载下层的类加载器所加载的类，所以通过spi解决了该问题。
spi是一种将服务接口与服务实现分离以达到解耦、大大提升了程序可扩展性的机制。引入服务提供者就是引入了spi接口的实现者，通过本地的注册发现获取
到具体的实现类，轻松可插拔spi实现了动态加载，插件化。

# sdk spi
* 文件路是：META-INF/services/com.lind.interfaces.HelloProvider
这里的文件名称是接口的全名，如com.lind.interfaces包下面的HelloProvider接口，它的名称就是`com.lind.interfaces.HelloProvider`,它里面的内容为这个接口的实现类.
* java里的调用
```
  ServiceLoader<ProviderFactory> s = ServiceLoader.load(ProviderFactory.class);
        Iterator<ProviderFactory> iterable = s.iterator();
        while (iterable.hasNext()) {
            Provider provider = iterable.next().create();
            log.info("hello:{}", provider.login());
        }
```

# spring spi
> 文件路径：META-INF/spring.factories

# SpiFactory使用,不需要META-INF/spring.factories
插件目录监控
```
SpiFactory.watchDir("d:\\jar");
```
程序中获取工厂
```
List<String> result = new ArrayList<>();
    for (ProviderFactory u : SpiFactory.getProviderFactory(ProviderFactory.class)) {
            result.add(u.create().login());
}
```
