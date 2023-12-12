# webjars类型的前端jar包

我们可以将公用的js,css,html,vue,shtml打包成一个jar，然后在其他项目中引用，这样就不用每个项目都去引用一遍了，这样就可以实现前端的公用了。

## 1.创建一个maven项目，添加依赖和插件

```xml
  <dependencies>
        <!-- 依赖webjars-locator-core -->
        <dependency>
            <groupId>org.webjars</groupId>
            <artifactId>webjars-locator-core</artifactId>
            <version>0.46</version>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <!-- 使用maven-resources-plugin将静态资源打包到JAR文件中 -->
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-resources-plugin</artifactId>
                <version>3.2.0</version>
                <executions>
                    <execution>
                        <id>copy-resources</id>
                        <phase>validate</phase><!-- mvn package or mvn install之后将会把静态static文件夹打包到jar里-->
                        <goals>
                            <goal>copy-resources</goal>
                        </goals>
                        <configuration>
                            <outputDirectory>
                                ${project.build.directory}/classes/META-INF/resources/webjars/${artifactId}/${version}/
                            </outputDirectory>
                            <resources>
                                <resource>
                                    <directory>src/main/resources/static</directory>
                                </resource>
                            </resources>
                        </configuration>
                    </execution>
                </executions>
            </plugin>
        </plugins>
    </build>
```

我们在resources目录下，添加static目录，把我们的js,css都放到这个目录下，然后使用maven-resources-plugin插件将静态资源打包到JAR文件中，这样就可以将静态资源打包到jar里了。

# 在其它项目中直接引用包里的文件

```
<script th:src="@{/webjars/lind-schedule-web/1.1.3-SNAPSHOT/js/index.js}"></script>
```
