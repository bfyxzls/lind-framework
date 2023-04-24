# oauth2登录
* POST /oauth/token?grant_type=password&username=liuer&password=123&scope=all&client_id=union_platform&client_secret=123
响应
```
{
    "access_token": "eb534a13-16f0-4071-9324-afe5161c593a",
    "token_type": "bearer",
    "refresh_token": "fa8762b2-5a36-4f22-9f8b-e05ceaf8e55c",
    "expires_in": 43199,
    "scope": "all"
}
```
# oauth2刷新token
修改权限之后，前端需要调用刷新token接口，来获取新的access_token，这样才能拿到新的权限
* POST /oauth/token?grant_type=refresh_token&refresh_token=5491eed7-9d5f-4c94-bfe6-30b612eab1bf&client_id=system&client_secret=system
响应
```
{
    "access_token": "aca9eb2a-fb7f-4e9a-971b-42af8d0365d7",
    "token_type": "bearer",
    "refresh_token": "5491eed7-9d5f-4c94-bfe6-30b612eab1bf",
    "expires_in": 28799,
    "scope": "app"
}
```
> 注意：配置方法`public void configure(AuthorizationServerEndpointsConfigurer endpoints)`需要指定`UserDetailsService`的实现
```
@Override
public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
    //...
    //设置userDetailsService刷新token时候会用到
    endpoints.userDetailsService(userDetailsService);
    //...
}
```
# 引用依赖包
```$xslt
 <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-security</artifactId>
            <version>${spring-boot-starter-security-version}</version>
        </dependency>
        <dependency>
            <groupId>org.springframework.security.oauth</groupId>
            <artifactId>spring-security-oauth2</artifactId>
            <version>${spring-security-oauth2-version}</version>
        </dependency>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-oauth2</artifactId>
            <version>${spring-cloud-starter-oauth2-version}</version>
        </dependency>
        <dependency>
            <groupId>com.lind</groupId>
            <artifactId>lind-mybatis-start</artifactId>
            <version>${lind-mybatis-start-version}</version>

        </dependency>
        <!-- redis -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-redis</artifactId>
        </dependency>
         <dependency>
            <groupId>redis.clients</groupId>
            <artifactId>jedis</artifactId>
            <version>2.9.0</version>
        </dependency>
```
# 依赖指定的springboot和springcloud包
```$xslt
 <dependencyManagement>
        <dependencies>
            <!--spring cloud 版本-->
            <dependency>
                <groupId>org.springframework.cloud</groupId>
                <artifactId>spring-cloud-dependencies</artifactId>
                <version>${spring-cloud-version}</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>

            <dependency>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-dependencies</artifactId>
                <version>${spring-boot-version}</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>
        </dependencies>
 </dependencyManagement>
```
# 注意它们的版本
```$xslt
    <properties>
        <spring-boot-version>2.0.4.RELEASE</spring-boot-version>
        <spring-cloud-version>Finchley.RELEASE</spring-cloud-version>
        <spring-boot-starter-security-version>2.0.0.RELEASE</spring-boot-starter-security-version>
        <spring-security-oauth2-version>2.3.3.RELEASE</spring-security-oauth2-version>
        <spring-cloud-starter-oauth2-version>2.0.0.RELEASE</spring-cloud-starter-oauth2-version>
        <lind-mybatis-start-version>1.0.0</lind-mybatis-start-version>
    </properties>
```
# 客户端请求带上client_id和client_secret否则401
```$xslt
# url parameters
username:admin
password:123456
loginType:userLogin
grant_type:password
scope:app
client_id:system
client_secret:system
```
# 集成方式
直接通过lind-uaa-start包，然后建立对应的数据表，保存与uaa的实体对应即可，在application里配置对应的数据库链接串。