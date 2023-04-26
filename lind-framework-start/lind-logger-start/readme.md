# 基于SpEL的日志系统
## 参考：
* https://tech.meituan.com/2021/09/16/operational-logbook.html
* https://github.com/bfyxzls/spring-boot-koala/tree/main/spring-boot-koala-starters/spring-boot-koala-starter-log-auto
* https://javamana.com/2021/05/20210506201813627y.html

## SpEL 模型设计
为了实现 SpEL, 我们需要如下几个模型:
* LogRootObject: 运行数据来源
* LogEvaluationContext: 解析上下文, 用于整个解析环境
* LogEvaluator: 解析器, 解析 SpEL, 获取数据
### LogRootObject
LogRootObject 是 SpEL 表达式的数据来源, 即业务描述,上文提及在业务描述中需要记录业务数据的 id, 它可以通过方法参数获得, 那么:
```java
@Getter
@AllArgsConstructor
public class LogRootObject {
    /**
     * 目标方法
     */
    private final Method method;
    /**
     * 方法参数
     */
    private final Object[] args;
    /**
     * 目标类的类型信息
     */
    private final Class<?> targetClass;
}
```
### LogEvaluationContext
Spring 提供了MethodBasedEvaluationContext, 我们只需要继承它, 并实现对应的构造方法:
```java
public class LogEvaluationContext extends MethodBasedEvaluationContext {
    /**
     * 构造方法
     *
     * @param rootObject 数据来源对象
     * @param discoverer 参数解析器
     */
    public LogEvaluationContext(LogRootObject rootObject, ParameterNameDiscoverer discoverer) {
        super(rootObject, rootObject.getMethod(), rootObject.getArgs(), discoverer);
    }
}
```
### LogEvaluator
这是我们最核心的解析器, 用于解析 SpEL, 返回真正期望的数据内容，我们需要初始化表达式编译器/表达式编译模板/参数解析器
```java
@Getter
public class LogEvaluator {
    /**
     * SpEL解析器
     */
    private final SpelExpressionParser parser = new SpelExpressionParser();
    /**
     * 参数解析器
     */
    private final ParameterNameDiscoverer discoverer = new DefaultParameterNameDiscoverer();
    /**
     * 表达式模板
     */
    private final ParserContext template = new TemplateParserContext("${", "}");
    /**
     * 解析
     *
     * @param expression 表达式
     * @param context    日志表达式上下文
     * @return 表达式结果
     */
    public Object parse(String expression, LogEvaluationContext context) {
        return getExpression(expression).getValue(context);
    }
    /**
     * 获取翻译后表达式
     *
     * @param expression 字符串表达式
     * @return 翻译后表达式
     */
    private Expression getExpression(String expression) {
        return getParser().parseExpression(expression, template);
    }
    
}
```
> 到此为止, 整个 SpEL 表达式的全部内容搞定, 再次强调下它的逻辑:设计 RootObject->设计 SpEL 表达式的运行上下文->设计 SpEL 解析器(包括表达式解析模板和参数解析器)
