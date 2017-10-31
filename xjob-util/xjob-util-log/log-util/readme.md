#   日志记录工具
V0.0.3<br>
1.删除Logfactory中脑残的代码<br>
2.增加@ContentType,@Channel用来分别指定日志内容的内容,以及实时日志队列的Topic.<br>
3.去掉色彩日志输出.<br>

#版本变更<br>
V0.0.21<br>
1.提供日志级别配置
V0.0.2<br>
1.提供kafka日志对接.(使用Kafka Appender时,请加上FileAppender做好Kafka连接不通时的日志备份)<br>
V0.0.1<br>
1.提供Console日志输出<br>
2.提供File日志输出<br>

#使用说明
#### 在项目资源文件夹添加log.yml文件
```+yaml
logLevel:   Debug //日志级别
targets:    #记录器
  - File    #启用文件Appender
  - Sout    #启用控制台Appender
  - Kafka   #启用Kafka Appender
kafka:
  kafkaServer:  192.168.2.56:8084   #日志服务器地址
  kafkaKey: test                    #kafka key(向日志服务器申请)
  kafkaCert:  test                  #kafka Cert(向日志服务器申请)
  kafkaBackDir: D:/test/kafka/      #Kafka链接失败备份文件路径
host: 0.0.0.0   # 项目启动占用IP
port: 2222      # 项目启动占用端口
application:  "springboot application"  #项目名称
fileProperties:         #如果设置了Targets-File一定要配置该项
  fileDir:  D:/test/    #日志输出目录
  fileName: log.log     #日志输出文件
  rolling:  Hour        #日志切割按照间隔时间
  fileSize: 1MB         #日志切割按照安件大小
#日志输出模板 
#   %t  时间
#   %l  日志等级
#   %p  进程号
#   %mn 方法名
#   %ln 行号
#   %cn 类名
#   %c  日志内容
partten:  "%-23{yyyy-MM-dd HH:mm:ss.sss}t %5l %-5p --- [%15mn:%ln] %-41cn: %c"    

```

#### pom.xml添加
```+xml
<dependency>
  <groupId>com.daqsoft.log</groupId>
  <artifactId>log-util</artifactId>
  <version>0.0.21</version>
</dependency>
```
#### 使用方式
```+java
    public class LoggerDemo {
        Logger logger = LogFactory.getLogger(LoggerDemo.class);
        @Test
        //这里添加LogModel注解标识 当前方法是属于什么模块.Eg:用户模块
        @LogModel("测试demo")
        //这里用来指定实时日志队列topic,放在方法上,那么这个方法所有的日志类型都会被输出到这个topic上
        @Channel("Jikebao")
        public void log() throws InterruptedException {
            logger.info("测试info");
            logger.info("测试%s","info");
            logger.debug("测试debug");
            logger.debug("测试%s","debug");
            logger.error("测试error");
            //如果需要单条输出到日志队列中,在info中指定.无法指定其他日志级别单条输出.
            logger.info("Jikebao","测试日志队列");
            logger.error("测试%s","error");
            logger.warn("测试warn");
            logger.warn("测试%s","warn");
            logger.log("测试自定级别", Constans.ERROR);
            //这里休眠2秒,便于日志处理器有时间去处理记录的日志
            TimeUnit.SECONDS.sleep(2);
        }
    }
```