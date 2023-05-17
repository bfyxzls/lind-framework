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
icon
复制
FactoryBean和BeanFactory是Spring框架中重要的两个核心接口，它们都是实现IoC容器的基础。它们的职责如下：
## FactoryBean
* FactoryBean是一个工厂Bean，用于创建某些类型的对象。一般情况下，我们使用Spring容器创建的对象都是由Spring框架在底层自动处理，但是某些情况下我们需要手动实例化对象，这时我们可以使用FactoryBean来实现。FactoryBean接口的核心方法是getObject()，这个方法用于返回FactoryBean创建的对象实例。Spring容器在处理FactoryBean时会自动调用getObject()方法返回对象，并将其纳入容器管理。同时，FactoryBean还可以控制创建出的对象是“单例”还是“多例”，通过实现isSingleton()方法来实现。延迟加载也是FactoryBean的一个特性，通过实现isLazyInit()方法来控制延迟加载。

## BeanFactory
* BeanFactory是一个IoC容器，管理着所有需要创建、组装和初始化的Bean对象。BeanFactory是Spring容器的顶层父接口，它定义了IoC容器接口的基本功能和规范，包括获取Bean、注册Bean等功能，并且支持不同种类、不同定义方式的对象实例化和获取，例如从XML、注解或Java框架（如Hibernate、Struts 2等）中定义的Bean。BeanFactory的核心方法是getBean()，用于获取Bean实例，BeanFactory充当Bean的工厂和容器的角色，它会负责维护Bean的生命周期、作用域和依赖等关系。与FactoryBean不同的是，BeanFactory只提供了Bean的管理和实例化功能，而不参与Bean的实例化。所有的Bean都是通过BeanFactoryPostProcessor来完成实例化的。除此之外，BeanFactory还支持对Bean进行后置处理、消息发送等高级功能。
* 总之，FactoryBean和BeanFactory都是Spring框架中重要的接口，FactoryBean负责创建Bean实例并控制其“单例/多例”、延迟加载等特性，BeanFactory则是IoC容器的核心接口，负责管理Bean对象的生命周期和存在周期，并提供高级特性和扩展方式，由它来维护整个Bean体系的完整性。

## 关系
* beanFactory,bean的工厂
  * beanFactory是所有spring bean容器的顶级接口，它为spring的容器定义了一套规范，并提供像getBean这样的方法从容器中获取指定的bean实例
  * beanFactory在产生Bean的同时，还提供了解决Bean之间的依赖注入的能力，也就是所谓的DI.
* factoryBean,工厂bean，生产bean实例
  * factoryBean是一个工厂bean，它是一个接口，它的主要功能是动态生成某一类型的bean的一个实例.
  * 通过实现factoryBean，我们可以自定义一个bean，并且加载到ioc容器里面
  * 它里面有一个重要的方法叫getObject()，这个方法就是实现动态构建bean的一个过程
  * spring cloud里的openFeign组件，就是使用factoryBean来实现的
    BeanFactory和BeanDefinition是Spring中两个很重要的概念，它们是Spring中实现IoC容器的基础。BeanFactory是一个顶层接口，定义了IoC容器的核心功能，可以理解为IoC容器的基础设施；而BeanDefinition则是描述IoC容器要管理的Bean对象的元信息，可以理解为Bean的具体定义。它们之间的关系可以简述如下：
# BeanFactory和BeanDefinition
* BeanFactory是IoC容器的接口，是一个工厂模式的典型实现，提供了获取Bean、注册Bean等基本功能，同时还支持不同种类和不同定义方式的对象实例化和获取，例如从XML、注解或Java框架（如Hibernate、Struts 2等）中定义的Bean。BeanFactory的核心方法是getBean()，用于获取Bean实例。BeanFactory主要负责创建Bean对象，并控制Bean的生命周期、作用域和依赖等关系。

* BeanDefinition是描述IoC容器要管理的Bean对象的元信息，它包含了Bean的定义信息，例如Bean的类型、属性、依赖等信息。Spring会将我们在配置中定义的Bean信息封装为一个BeanDefinition对象，并提供一些API（比如setScope、getPropertyValues等方法）来获取和设置元信息，从而控制Bean的创建、初始化和依赖注入等行为。BeanDefinition是描述Bean信息的抽象模型，但并不创建Bean实例本身，它主要负责Bean的定义和组装，并在BeanFactory中保存和管理Bean定义。BeanDefinition通过BeanName和BeanDefinitionHolder关联到BeanFactory中，存储在BeanFactory的BeanDefinitionMap中，BeanFactory负责解析和执行BeanDefinition。

* 总之，BeanFactory是IoC容器的基本接口，负责创建和管理Bean实例；而BeanDefinition是保存Bean元信息的抽象模型，负责控制Bean的创建和组装��为。BeanDefinition提供了对Bean类型、作用域、属性、依赖、初始化和销毁等方面的控制，而BeanFactory则负责解析和执行BeanDefinition，创建出Bean实例并负责管理Bean实例的声明周期和作用域等。在Spring中，BeanFactory和BeanDefinition密不可分，BeanFactory是BeanDefinition的执行者，并根据BeanDefinition中的元信息进行Bean的实例化和管理。
