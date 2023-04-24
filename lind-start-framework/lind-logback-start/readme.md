# 日志格式化
> 加载优先级为： logback.xml--->application.properties--->logback-spring.xml，所以logback-spring.xml是可以读到spring的配置信息
* 位置：sources/logback-spring.xml
* 样本：<pattern>%d{yyyy-MM-dd HH:mm:ss.SSS} ${springAppName} %ip [%thread] %-5level - %msg</pattern>

# 系统变量
* %thread 当前线程
* %d{yyyy-MM-dd HH:mm:ss.SSS} 当前日期时间
* %level 日志级别
* %msg 日志主体
* %n 换行

# 本组件提供的变量
* %ip 显示客户端ip地址
* %color 日志级别颜色个性化
* %currentUser 显示当前用户名
* %os 显示客户端操作系统
* %browserType 显示客户端浏览器类型和版本
* %uri 显示当前客户端请求的url
* %traceId 显示调用链跟踪ID，不同的httpclient需要添加不同的Interceptor,本组件集成了okhttp,feign等。

# transmittable-thread-local组件
这个是阿里开发的组件，主要解决跨线程的数据转值问题，目前由于openfeign中开启熔断之后，默认使用thread模式，
所以需要希望通过MDC转traceId，需要使用transmittable这个组件。
```
<dependency>
    <groupId>com.alibaba</groupId>
    <artifactId>transmittable-thread-local</artifactId>
    <version>2.14.2</version>
</dependency>
```
# 自定义变量ClassicConverter
ClassicConverter对象负责从ILoggingEvent 提取信息，并产生一个字符串。例如，LoggerConverter，它是处理“% logger”转换符的转换器，
它从ILoggingEvent提取logger 的名字并作为字符串返回。
* 例如，一个按日志级别实现的颜色转化器
```
/**
 * 日志模板：基于日志级别的颜色的转换器
 */
public class LevelColorClassicConverter extends ClassicConverter {

	private static final String END_COLOR = "\u001b[m";

	private static final String ERROR_COLOR = "\u001b[0;31m";

	private static final String WARN_COLOR = "\u001b[0;33m";

	@Override
	public String convert(ILoggingEvent event) {
		StringBuffer sbuf = new StringBuffer();
		sbuf.append(getColor(event.getLevel()));
		String result = String.format("%5s", event.getLevel());
		sbuf.append(result);
		sbuf.append(END_COLOR);
		return sbuf.toString();
	}

	/**
	 * Returns the appropriate characters to change the color for the specified logging
	 * level.
	 */
	private String getColor(Level level) {
		switch (level.toInt()) {
			case Level.ERROR_INT:
				return ERROR_COLOR;
			case Level.WARN_INT:
				return WARN_COLOR;
			default:
				return "";
		}
	}
}

```
* 配置和模板
```
<conversionRule conversionWord="color"  converterClass="com.lind.logback.convert.LevelColorConverter" />
<appender name="consoleLog" class="ch.qos.logback.core.ConsoleAppender">
    <layout class="ch.qos.logback.classic.PatternLayout">
        <pattern>%d{yyyy-MM-dd HH:mm:ss.SSS} ${springAppName} %ip [%thread] %color %msg%n</pattern>
    </layout>
</appender>
```
* 运行截图
![](./assets/log-format-1674869046549.png)

# 自定义PatternLayout
如果你的自定义classicConvert比较多时，又不想在xml中通过conversionRule一个个注册，就需要自定义PatternLayout了，接着上面的LevelColorConverter来写一个PatternLayout.
* StandardPatternLayout
```
public class StandardPatternLayout extends PatternLayout {

	static {
		defaultConverterMap.put("color", LevelColorConverter.class.getName());
	}
}
```
* 添加对应的配置项，不需要添加conversionRule项，在pattern中，也是可以直接使用定义好的常量的
```
<layout class="com.lind.logback.StandardPatternLayout">
    <pattern>
        %d{yyyy-MM-dd HH:mm:ss.SSS} ${springAppName} %browserType %ip %currentUser [%thread] %color %msg%n
    </pattern>
</layout>
```
# 添加spring变量
```
<springProperty scope="context" name="springAppName" source="spring.application.name"/>
# 通过${springAppName}来调用它
```
# 定义模板
## INFO级别
日期时间 应用名称 线程 日志级别 日志详情
