# 版本
* 1.1.3 去掉parser包，将它移动到common-core项目，因为它与es无关，只做将数据按着类型进行粘贴
# es7的部署
```
docker run --restart=always -p 9200:9200 -p 9300:9300 -e discovery.type=single-node -e ES_JAVA_OPTS="-Xms512m -Xmx512m" --name=docker-elasticsearch -d  elasticsearch:7.6.2

```
# springboot与es兼容性
* springboot2.7与spring-boot-starter-data-elasticsearch2.7对于es2.17.6有冲突，导致`ElasticsearchRepository`不可用
* springboot2.6对于es2.17.6,对于`ElasticsearchRepository`没有出现上面问题

# 客户端版本问题
* es依赖包我们通常指两个org.elasticsearch:elasticsearch和org.elasticsearch.client:elasticsearch-rest-high-level-client，这两个包的版本需要对应上，你的索引初始化才能生效，否则你在实体上添加的@Setting,@Mapping是不会生效的。
```
<dependency>
      <groupId>org.elasticsearch.client</groupId>
      <artifactId>elasticsearch-rest-high-level-client</artifactId>
      <version>7.6.2</version>
  </dependency>
  <!--  elasticsearch对应兼容版本为7.6.2-->
  <dependency>
      <groupId>org.elasticsearch</groupId>
      <artifactId>elasticsearch</artifactId>
      <version>7.6.2</version>
  </dependency>
```
* 实体定义
```
@Document(indexName = "kc_event_logger")
@Setting(settingPath = "mapping/es-setting.json")
@Data
public class EventRecord implements Serializable {
 public static final String dateTimeFormat =
      "yyyy||yyyyMM||yyyy.MM||yyyy/MM||yyyy-MM||yyyyMMdd||yyyy.MM.dd||yyyy/MM/dd||yyyy-MM-dd||yyyy-MM-dd HH:mm:ss||" +
          "yyyy.MM.dd HH:mm:ss||yyyy/MM/dd HH:mm:ss||yyyy-MM-dd HH:mm:ss.SSS||yyyy.MM.dd HH:mm:ss.SSS||yyyy/MM/dd HH:mm:ss.SSS";
  @Id
  @JsonIgnore
  private final String id = UUID.randomUUID().toString();
  @Field(type = FieldType.Keyword)
  private String realmId;
  @Field(type = FieldType.Keyword)
  private String type;
  /**
   * 时间
   */
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
  @Field(type = FieldType.Date, format = DateFormat.custom, pattern = dateTimeFormat)
  private Date createTime;
}
```
* 生成的索引是正常的，keycloak与text是明确指明的
```
{
    "mappings":{
        "eventrecord":{
            "properties":{
                "clientId":{
                    "type":"keyword"
                },
                "realmId":{
                    "type":"keyword"
                },
                "createTime":{
                    "format":"yyyy||yyyyMM||yyyy.MM||yyyy/MM||yyyy-MM||yyyyMMdd||yyyy.MM.dd||yyyy/MM/dd||yyyy-MM-dd||yyyy-MM-dd HH:mm:ss||yyyy.MM.dd HH:mm:ss||yyyy/MM/dd HH:mm:ss||yyyy-MM-dd HH:mm:ss.SSS||yyyy.MM.dd HH:mm:ss.SSS||yyyy/MM/dd HH:mm:ss.SSS",
                    "type":"date"
                },
                "ipAddress":{
                    "type":"keyword"
                }    
            }
        }
    }
}
```
# 作用
对es的增删改进行封装，提取了实体基类`EsBaseEntity`，提供了公用的字段，id统一赋值，`createUser,createTime,updateUser,updateTime`通过拦截器统一进行赋值；提出了`EsAuditorAware`接口，使用者可以实现这个接口来返回当前登录的用户信息。
# 两个仓储对象
* EsDataUtils,使用`RestHighLevelClient`进行json格式索引的构建
* `ElasticsearchRestTemplate`直接对索引实体进行操作，实体继承`EsBaseEntity`

# 依赖引用
```
<dependency>
 <groupId>com.lind</groupId>
 <artifactId>lind-elasticsearch-start</artifactId>
 <version>1.0.0</version>
</dependency>
```
# 配置
使用`spring-boot-starter-data-elasticsearch`的标准配置即可
# 使用
开启es审计功能（为公用字段赋值）
```
@EnableEsAuditing
@SpringBootApplication
public class ElasticsearchApplication {
    public static void main(String[] args) {
        SpringApplication.run(ElasticsearchApplication.class, args);
    }
}
```
实现EsAuditorAware接口，返回当前用户Id
```
@Component
public class UserAuditorAware implements EsAuditorAware<String> {
    @Override
    public Optional<String> getCurrentAuditor() {
        return Optional.of("1"); //这块根据个别系统决定
    }
}
```
# 几个核心类
* ElasticsearchRepository(org.springframework.data:spring.data.elasticsearch:4.4.7)
* ElasticsearchRestTemplate(org.springframework.data:spring.data.elasticsearch:4.4.7)
* RestHighLevelClient(org.elasticsearch.client:rest-high-level:7.6.2)
* 
# es使用注意点
1. text类型里的keyword属性，对于它的`ignore_abore`的大小应该做好限制，避免长文本的索引
1. 对于text分词检索的高亮功能，可以使用`"term_vector": "with_positions_offsets"`来实现
1. es中存储的数据分为两种，原始文档和索引文档（倒排索引），这两个内容都可以分别配置的
1. es中默认的设置_source是enable的配置决定了字段是否被存储，它默认是`true`的，即存储整个文档
1. 当_source.enable配置为false后，你还希望存储某些字段，可以使用`mappings.type._source.includes:[]`来实现
1. 对于一些只需要查询、不需要返回的字段，可以通过`mappings.type._source.excludes:[]`来进行排除
1. 如果希望某个字段不索引，即不存储进倒排索引里，可以在字段中添加属性`"index":false`即可；而如果希望不存储字段内容，可以添加`"store":false`
1. 对于es索引的mapping，为了安全，我们杜绝动态生成字段，需要配置这个属性`mappings.type.dynamic:"strict"`，如果字段没有mapping将直接报错
1. 对于上面的`"strict"`，也可以让它不报错，新字段不写入，可以把值设成"false"即可，`mappings.type.dynamic:"false"`，这个值支持随时更新的
1. 一个高度灵活的日期型字段，可以通过format来配置它`"format": "yyyy||yyyyMM||yyyy.MM||yyyy/MM||yyyy-MM||yyyyMMdd||yyyy.MM.dd||yyyy/MM/dd||yyyy-MM-dd||yyyy-MM-dd HH:mm:ss||yyyy.MM.dd HH:mm:ss||yyyy/MM/dd HH:mm:ss||yyyy-MM-dd HH:mm:ss.SSS||yyyy.MM.dd HH:mm:ss.SSS||yyyy/MM/dd HH:mm:ss.SSS"`
1. es分片的数据需要和数据大小进行匹配，每个片官方推荐是30G，例如你的数据大小是300G，那你应该分10个片、3000G应该分100个片
1. es的分页查询，对于大页码应该杜绝，因为它会对所有分片进行查询，最后再进行合并，所以对于查询第1000页的10条数据，如果是5个分片，它会把5*1000*10的数据进行skip limit的计算
