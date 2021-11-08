# 项目技术说明

| 描述                   | 名称                                          | 版本号（boot：2.4.3）           |
| ---------------------- | --------------------------------------------- | ------------------------------- |
| Http 请求              | OkHttp + Retrofit + converter-jackson         | v2.9.0                          |
| 邮件发送               | spring-boot-starter-mail                      |                                 |
| Html解析器             | Jsoup                                         | v1.13.1                         |
| 全文检索               | ElasticSearch                                 |                                 |
| ElasticSearch 日志推送 | LogStash                                      | v5.3                            |
| SkyWalking 日志推送    | apm-toolkit-logback-1.x                       | v8.7.0                          |
| RabbitMQ               | spring-boot-starter-amqp                      |                                 |
| WebSocket              | Netty                                         | v4.1.52.Final                   |
| Redis                  | Redis + commons-pool2                         |                                 |
| 数据库部分             | MySQL + Druid + PageHelperv--                 | v-- \| v1.1.22 \| v1.3.0        |
| Api文档                | Knife4j                                       | v3.0.2                          |
| 权限认证               | Oauth2                                        | autoconfigure：v2.1.3.RELEASE   |
| OSS文件存储            | Minio                                         | v7.0.2                          |
| 实体映射               | MapStruct                                     | v1.4.2.Final                    |
| 其他工具               | Lombok + commons-lang3 + EasyExcel + FastJson | v-- \| v-- \| v2.2.6 \| v1.2.60 |

# 多数据源配置及事务注意事项

> 配置均在**config - mybatis**中

> 涉及事务处理时仿照controller - test - TestController处理
>
> 主数据源使用@Transactional不需要指定value，其他数据源需要指定
>
> 多数据源事务使用自定义注解@MultiTransactional value传入需要事务的数据源

> 修改默认配置时应修改以下位置
>
> - **config - mybatis - DataSourceConfig**
>   - 注入数据库配置与yml中一致
> - **config - mybatis - SqlSessionConfig** 
>   - 数据库配置扫描路径与实际一致
>   - sqlSessionTemplateRef指定与本类中注入的名称一直
>   - 必须指定主数据源@Primary
> - **config - mybatis - transaction**
>   - MultiTransactionAop切入点指定为自定义注解MultiTransaction
>   - DbTxConstants中事务管理器名称与SqlSessionConfig 中一致
>   - 对应增删或修改Db1TxBroker，Db2TxBroker，ComboTransaction
# 开发注意事项

- POJO 类中的任何布尔类型的变量，都不要加 is 前缀，否则部分框架解析会引起序列化错误。
- 所有整型包装类对象之间值的比较，全部使用equals 方法比较
  > 对于Integer var = ? 在-128 至127 之间的赋值，Integer 对象是在IntegerCache.cache 产生，
    会复用已有对象，这个区间内的Integer 值可以直接使用==进行判断，但是这个区间之外的所有数据，
    都会在堆上产生，并不会复用已有对象
- 浮点数之间的等值判断使用 BigDecimal.compareTo()
- 使用 BigDecimal.valueOf(double) 而不是 new BigDecimal(double)，精度会存在丢失
- 避免 Random 实例被多线程使用。
  在 JDK7 之后，可以直接使用API ThreadLocalRandom，而在 JDK7 之前，需要编码保证每个线程持有一个单独的 Random 实例