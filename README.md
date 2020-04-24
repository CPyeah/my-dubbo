# my-dubbo
dubbo demo

springboot搭建

zookeeper做为注册中心

dubbo-admin-master.zip  管理控制台


    dubbo-api  封装公共接口

    pom-parent  同一管理jar包

    consumer  消费者

    provider  服务提供者1

    provider2 服务提供者2（负载均衡）


dubbo 消费者访问地址

http://localhost:7000/getuserbyid?id=1


1、dubbo是什么？

    是阿里开发的，高性能的支持RPC远程调用的分布式框架。
    三大特点：
    1）面向接口的远程方法调用
    2）智能容错和负载均衡
    3）服务自动注册和发现

2、为什么要用dubbo？
    
    高性能的分布式框架，支持高并发。中文文档充足。很多大公司（包括阿里系）都有实际应用，
    经历过实际考验，很可靠。
    
3、Dubbo支持哪些协议？推荐哪一个？为什么？

    1、dubbo 协议 (默认)（并发量最高）
    2、rmi 协议
    3、hessian 协议
    4、http 协议
    5、webservice 协议
    6、thrift 协议
    7、memcached 协议
    8、redis 协议
    9、rest ( 就是 RestFull)
    
    dubbo协议基于TCP，使用NIO异步传输。比较适用于小数据传输（100K），一般服务方法调用。
    经过测试dubbo序列化+netty并发量最高。
    
    https://blog.csdn.net/xiaojin21cen/article/details/79834222
    
4、Dubbo与SpringCloud的对比？

    Dubbo是RPC远程调用框架，直接基于socket，速度更快。
    无消息总线，各个服务之间直接调用。使用比较简单
    SpringCloud是微服务框架，使用HTTP RestFul。
    有消息总线。还有批量任务等组件。比较庞杂。
    
5、Dubbo需要容器吗？

    Dubbo不需要web容器，会增加复杂性，浪费内存，端口。
    只需要服务容器，内置容器有Spring、Netty等。直接main方法调用。
    
6、Dubbo框架里的角色。
    注册中心（Registry） 一般使用zookeeper
    监控中心（Monitor） 一般使用源码提供的dubbo-monitor
    服务提供者（Provider） 业务代码
    服务消费者（Consumer） 业务代码
    服务容器（Container） Spring Container spring框架
    可视化监控管理（admin） 源码里面的dubbo-admin
    
7、★ 服务发现流程（核心图解）
    
    1）启动注册中心，监控中心，可视化监控管理
    2）Container启动服务提供者
    3）provider向注册中心 注册服务信息
    4）consumer向注册中心找服务提供者信息
    5）注册中心向consumer返回provider信息
    6）consumer根据信息，向provider发送请求（同步invoke）
    7）请求同时，consumer和provider想监控中心发送消息，记录请求信息
    注：其中只有invoke请求为同步请求，其他的全为异步请求

8、dubbo的注册中心。

    默认推荐使用zookeeper，Apacahe 的子项目，支持订阅模式，变更自动推送。
    但是阿里内部dubbo并没有使用zookeeper做注册中心，使用的基于数据库的另外一个实现。
    其他的注册中心可以是redis、multicast、simple。
    
9、dubbo的核心配置。
    
    service     （@service）
        用于暴露服务
        
    reference   （@Reference(loadbalance = "random", timeout = 300, check = false)）
        用户consumer引用服务   引用策略                  超时时间      是否启动检查
        
    dubbo.application.name=user-server-provider2
    dubbo.registry.address=zookeeper://localhost:2181
    dubbo.protocol.name=dubbo
    dubbo.protocol.port=20882
    
10、Provider的配置有哪些？

    @Service(interfaceClass = UserService.class, timeout = 300, retries = 1, loadbalance = "", actives = 5)
    1、timeout：方法调用超时
    2、retries：失败重试次数
    3、loadbalance：负载均衡算法，默认随机
    4、actives 消费者端，最大并发调用限制
    
11、有哪几种负载均衡的策略？

    @Reference(loadbalance = "random", timeout = 300, check = false)
    1、random loadbalance：安权重设置随机概率（默认）；
    2、roundrobin loadbalance：轮寻，按照公约后权重设置轮训比例；
    3、lastactive loadbalance：最少活跃调用数，若相同则随机；
    4、consistenthash loadbalance：一致性hash，相同参数的请求总是发送到同一提供者。
    
12、当provider不可用时，启动消费者时会怎样？

    看引用provider时，配置check是否为true（默认为true）
    @Reference(check = false)
    如果配置为true，启动时会抛异常
    如果为false，能正常启动，调用是会抛异常
    
13、dubbo使用的序列化框架有哪些？添加进使用哪一种？

    hessian2：推荐使用。 默认使用
        dubbo里面使用的hessian是阿里修改过的，跨语言，基于二进制传输的序列化方式。
    dubbo序列化： 尚不成熟
        阿里开发的java高性能的序列化框架，不推荐，以后可能替换成这个。
    json序列化： 不成熟，性能一般
        基于json文本的序列化方式，性能不如二进制。
    java序列化： 性能很不理想
        java语言自带的序列化方式。性能不行。
        
14、Dubbo使用的是什么通信框架？有没有其他的？

    默认使用Netty，基于NIO，异步通信，效率高，并发高，支持自定义协议
    http头信息，无效信息太多，简单的方法调用不需要。
    另外还有Mina、Grizzly。
    
15、Dubbo的容错有哪些？默认是哪一种？

    Dubbo集群容错是靠配置cluster属性来做
    支持改属性的标签为<dubbo:service>,<dubbo:referece>,<dubbo:consumer>,<dubbo:provider>
    但是后两个粒度太粗,一般不采用
    该属性是可选的,默认值是failover
    @Service(cluster = "failfast")
    @Reference(cluster = "failover") 
    
    failover    失败自动切换      默认
        出现失败时，自动尝试其他服务器。可以配置尝试次数（retries）。
        通常用户读操作。写操作可能出现重复写。
    failfast    快速失败
        只发一次请求，失败即报错。
        用于非幂等请求，如新增。
    failsafe    失败安全
        发送一起请求，失败即忽略。
        一般用户日志记录
    failback    失败返回
        失败后记录信息，定时发送
        一般不用
    forKing     并行调用
        同时调用多个服务，个数通过forks配置，只需要一个成功返回。
        一般用户高时效的请求，通过资源换取效率。
    broadcast   广播调用
        发送请求给所有服务器，如果有一个失败，即全部失败。
        通常用于同步provider，缓存、本地资源等。

16、Dubbo的Provider的失效踢出原理。

    基于zookeeper的临时节点原理。
    
17、Dubbo的控制台能做些什么？

    通过dubbo中的源码，打包成war/jar包，部署在自己的服务器上。
    1、路由规则
    2、动态配置
    3、服务降级
    4、访问控制
    5、权重调整
    6、负载均衡

18、同一个接口有不同的实现类怎么区分？

    可以使用分组属性
    @Service(interfaceClass = UserService.class,group = "")
    相同组的consumer和provider相互调用
    
19、服务上线怎么兼容新旧版本？

    可以使用version属性
    @Service(interfaceClass = UserService.class,version = "")
    相同版本的相互调用，和group类似
    
20、Dubbo和Dubbox的异同点？

    Dubbox是Dubbo的拓展版本，当当网在原来的基础上添加了Rest调用，一些组件。
    
21、出现调用超时com.alibaba.dubbo.remoting.TimeoutException异常怎么办？

    业务处理太慢，造成调用超时。
    1、优化业务代码，提高provider响应速度。
    2、增长超时时间

    
     

    

