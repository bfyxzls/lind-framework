# ApplicationContextAware
  *     void setApplicationContext(ApplicationContext applicationContext) throws BeansException;
> 当一个类实现了这个接口之后，这个类就可以方便的获得ApplicationContext对象（spring上下文），Spring发现某个Bean实现了
> ApplicationContextAware接口，Spring容器会在创建该Bean之后，自动调用该Bean的setApplicationContext（参数）方法，
> 调用该方法时，会将容器本身ApplicationContext对象作为参数传递给该方法。

# InitializingBean
  * void afterPropertiesSet() throws Exception;
> 当一个类实现这个接口之后，Spring启动后，初始化Bean时，若该Bean实现InitialzingBean接口，会自动调用afterPropertiesSet()方法，
> 完成一些用户自定义的初始化操作。

# DisposableBean 
  * void destroy() throws Exception;
> 当一个类实现这个接口之后，允许在容器销毁该bean的时候获得一次回调。DisposableBean接口也只规定了一个方法：destroy

# beanFactory和factoryBean
* beanFactory,bean的工厂
  * beanFactory是所有spring bean容器的顶级接口，它为spring的容器定义了一套规范，并提供像getBean这样的方法从容器中获取指定的bean实例
  * beanFactory在产生Bean的同时，还提供了解决Bean之间的依赖注入的能力，也就是所谓的DI.
* factoryBean,工厂bean，生产bean实例
  * factoryBean是一个工厂bean，它是一个接口，它的主要功能是动态生成某一类型的bean的一个实例.
  * 通过实现factoryBean，我们可以自定义一个bean，并且加载到ioc容器里面
  * 它里面有一个重要的方法叫getObject()，这个方法就是实现动态构建bean的一个过程
  * spring cloud里的openFeign组件，就是使用factoryBean来实现的
