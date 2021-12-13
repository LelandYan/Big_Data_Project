# 基于Hadoop3的电信客服大数据项目

> 本项目是基于电信客服大数据项目，将其原有使用的大数据框架进行升级，最后实现了一个可以自动采集大量数据、进行分布式存储并可以提供一个可供数据分析可视化的web操作例子。

项目采用技术：Hadoop、Flume、Kafka、Hbase、Zookeeper、Maven、Spring、SpringMVC、Mybatis、Mysql、Redis、Echart。

项目运行环境：Win10+16G+I7+VMware Workstation+Centos7

| 项目要求                           | 是否完成  |
| :--------------------------------- | :-------: |
| 虚拟机搭建分布式大数据相关框架环境 | :ok_hand: |
| 编写数据模拟生成器                 | :ok_hand: |
| 采集数据生成器数据并消费           | :ok_hand: |
| 对模拟生成的数据进行分析           | :ok_hand: |
| 数据可视化                         | :ok_hand: |

最后实现查询和结果页面为：

<div align=center><img src="imgs\2.png" alt="Image text" /></div>

<div align=center><img src="imgs\3.png" alt="Image text" /></div>

## 1.项目背景

通信运营商每时每刻会产生大量的通信数据，例如通话记录，短信记录，彩信记录，第三方服务资费等等繁多信息。数据量如此巨大，除了要满足用户的实时查询和展示之外，还需要定时定期的对已有数据进行离线的分析处理。例如，当日话单，月度话单，季度话单，年度话单，通话详情，通话记录等等。

本项目就是统计每天、每月以及每年的每个人的通话次数及时长并进行可视化。

## 2.项目架构

项目架构图如下图：

<div align=center><img src="imgs\1.png" alt="Image text" /></div>

## 3.项目运行环境与搭建

### 3.1 项目运行环境

系统环境：

| 系统    | 版本           |
| ------- | -------------- |
| windows | win10          |
| linux   | CentOS-7.5-x86 |

开发工具：

| 工具  | 版本         |
| ----- | ------------ |
| idea  | 2021.2专业版 |
| maven | 3.6.1        |
| JDK   | 1.8          |

集群环境：

| 框架      | 版本  | 作用                                                         |
| --------- | ----- | ------------------------------------------------------------ |
| Hadoop    | 3.1.3 | 负责数据底层分布式存储（HDFS）、大数据框架调度（YARN）和离线计算功能（MapReduce） |
| Zookeeper | 3.5.7 | 负责管理分布式服务的大数据框架                               |
| Hbase     | 2.0.5 | 负责离线数据存储，是一种NoSQL数据库，主要用于实时计算        |
| Flume     | 1.9.0 | 负责数据的采集                                               |
| Kafka     | 2.4.1 | 负责数据的收集和提供消费队列                                 |

虚拟机环境：

| 主机名称         | hadoop102    | hadoop103 | hadoop104   |
| ---------------- | ------------ | --------- | ----------- |
| 内存             | 4G           | 2G        | 2G          |
| CPU              | 2核          | 2核       | 2核         |
| 硬盘             | 50G          | 50G       | 50G         |
| 安装数据库、缓存 | Mysql-8.0.27 |           | Redis-3.0.0 |

### 3.2 项目运行环境搭建

| 项目运行虚拟机环境              |                        百度云下载地址                        |
| :------------------------------ | :----------------------------------------------------------: |
| hadoop100（模板机）             | [:clap:](https://pan.baidu.com/s/1c8rC9pFfdiJBt_h-wJ1EBA)  提取码: lzym |
| hadoop102、hadoop103、hadoop104 | [:clap:](https://pan.baidu.com/s/1EYyziLLapPbOnwpvlnws-w) 提取码: uw2a |

| 环境搭建相关文档                     |                       详细文档跳转链接                       |
| :----------------------------------- | :----------------------------------------------------------: |
| 模板虚拟机（hadoop100）环境搭建      | [:thumbsup:](https://github.com/LelandYan/Big_Data_Project/blob/main/doc/%E6%A8%A1%E6%9D%BF%E8%99%9A%E6%8B%9F%E6%9C%BA%E7%8E%AF%E5%A2%83%E6%90%AD%E5%BB%BA.md) |
| Hadoop分布式集群搭建                 | [:thumbsup:](https://github.com/LelandYan/Big_Data_Project/blob/main/doc/Hadoop%E5%88%86%E5%B8%83%E5%BC%8F%E9%9B%86%E7%BE%A4%E6%90%AD%E5%BB%BA.md) |
| Zookeeper分布式集群搭建              | [:thumbsup:](https://github.com/LelandYan/Big_Data_Project/blob/main/doc/Zookeeper%E5%88%86%E5%B8%83%E5%BC%8F%E9%9B%86%E7%BE%A4%E6%90%AD%E5%BB%BA.md) |
| Hbase分布式集群搭建                  | [:thumbsup:](https://github.com/LelandYan/Big_Data_Project/blob/main/doc/Hbase%E5%88%86%E5%B8%83%E5%BC%8F%E9%9B%86%E7%BE%A4%E6%90%AD%E5%BB%BA.md) |
| Flume环境搭建                        | [:thumbsup:](https://github.com/LelandYan/Big_Data_Project/blob/main/doc/Flume%E7%8E%AF%E5%A2%83%E6%90%AD%E5%BB%BA.md) |
| Kafka环境搭建                        | [:thumbsup:](https://github.com/LelandYan/Big_Data_Project/blob/main/doc/Kafka%E7%8E%AF%E5%A2%83%E6%90%AD%E5%BB%BA.md) |
| Mysql环境搭建                        | [:thumbsup:](https://github.com/LelandYan/Big_Data_Project/blob/main/doc/MySql%E7%8E%AF%E5%A2%83%E6%90%AD%E5%BB%BA.md) |
| Redis环境搭建                        | [:thumbsup:](https://github.com/LelandYan/Big_Data_Project/blob/main/doc/Redies%E7%8E%AF%E5%A2%83%E6%90%AD%E5%BB%BA.md) |
| 集群开启关闭各大数据组件服务命令集合 | [:thumbsup:](https://github.com/LelandYan/Big_Data_Project/blob/main/doc/%E5%BC%80%E5%90%AF%E9%9B%86%E7%BE%A4%E4%B8%AA%E5%90%84%E5%A4%A7%E6%95%B0%E6%8D%AE%E6%A1%86%E6%9E%B6%E5%91%BD%E4%BB%A4.md) |

## 4.项目实现

| 项目模块名称 | 项目对应模块名称                     |
| :----------- | ------------------------------------ |
| 数据模拟     | /project-ct/ct-producer              |
| 数据采集消费 | /project-ct/ct-consumer              |
| 数据分析     | /project-ct/ct-analysis              |
| 数据展示     | /project-ct/ct-web2                  |
| 通用工具     | /project-ct/ct-common                |
| 数据缓存     | /project-ct/ct-cache                 |
| 协处理       | /project-ct/ct-consumer-corprocessor |

## 5.项目运行

**下载hadoop102，hadoop103，hadoop104虚拟机环境和ct-project项目后**：

1. 首先导入虚拟机并启动集群和，在本地主机idea（win）上导入ct-project项目配置环境

   ```shell
   # 开启hadoop集群
   [lelandyan@hadoop102 ~]$ myhadoop start
   
   # 开启zookeeper集群
   [lelandyan@hadoop102 ~]$ zk.sh start
   
   # 开启kafka
   [lelandyan@hadoop102 ~]$ kafka.sh start
   
   # 开启hbase集群
   [lelandyan@hadoop102 ~]$ start-hbase.sh
   
   # 开启mysql服务
   [lelandyan@hadoop102 ~]$ systemctl start mysqld
   
   # 开启redis服务
   [lelandyan@hadoop104 ~]$ cd /usr/local/redis/
   [lelandyan@hadoop104 ~]$ ./bin/redis-server ./redis.conf
   
   # 查看hadoop102,hadoop103,hadoop104上所有的java进程
   [lelandyan@hadoop102 ~]$ jpsall
   
   # 第一列为进程号,可以不相同,但是第二列必须与下面相同,第二列代表的是服务进程名称,如果没有启动成功,需要重启服务或者检测前面是否成功配置
   =============== hadoop102 ===============
   4498 HMaster
   3763 JobHistoryServer
   3860 QuorumPeerMain
   4676 HRegionServer
   3255 DataNode
   3117 NameNode
   3581 NodeManager
   4253 Kafka
   4909 Jps
   =============== hadoop103 ===============
   2768 DataNode
   3872 Kafka
   3475 QuorumPeerMain
   4195 Jps
   3092 NodeManager
   2957 ResourceManager
   3997 HRegionServer
   =============== hadoop104 ===============
   3650 HRegionServer
   2981 NodeManager
   3526 Kafka
   2888 SecondaryNameNode
   3848 Jps
   3134 QuorumPeerMain
   2767 DataNode
   ```

2. 在打开本地主机idea（win）navicat数据库远程连接，并建立ct数据库，导入ct.sql文件，建立数据库表和数据

3. 环境和项目配置完成后，在执行下列操作执行操作

   ```shell
   # 开始生产数据
   [lelandyan@hadoop102 ~]$ cd /opt/module/data
   [lelandyan@hadoop102 data]$ java -jar ct-producer.jar /opt/module/data/contact.log /opt/module/data/call.log
   
   # flume 采集数据，并放入kafka中
   [lelandyan@hadoop102 flume]$ bin/flume-ng agent -c conf/ -n a1 -f /opt/module/data/flume-2-kafka.conf
   
   # 打开本地主机idea（win）项目中的数据采集消费/project-ct/ct-consumer，并运行Bootstrap文件，将kafka数据存储到hbase中 或者可以打包/project-ct/ct-consumer然后在虚拟机hadoop102执行
   java -jar ct-consumer.jar
   
   # 打开本地主机idea（win）项目中的数据采集消费/project-ct/ct-cache，并运行Bootstrap文件，将数据库中的两张关联表，存入redis缓存,或者也可以通过打包后在虚拟机hadoop102上通过java -jar +jar名称执行
   java -jar ct-cache.jar
   
   # 执行分析任务，将hbase里面的数据经过mapreduce任务处理，存入mysql数据中，以便web项目读取，
   [lelandyan@hadoop102 ~]$ /opt/module/hadoop-3.1.3/bin/yarn jar ct_analysis_jar/ct-analysis.jar
   
   # 打开本地主机idea（win）项目中的数据采集消费/project-ct/ct-web2，配置tomcat，运行index.jsp，输入上面图片的地址，即可得到结果
   ```
   
4. 关闭所有服务执行脚本

   ```shell
   # 关闭mysql服务
   [lelandyan@hadoop102 ~]$ systemctl stop mysqld
   
   # 关闭redis服务
   [lelandyan@hadoop104 ~]$ cd /usr/local/redis/
   [lelandyan@hadoop104 ~]$ ./bin/redis-cli shutdown
   
   # 关闭hbase集群
   [lelandyan@hadoop102 ~]$ stop-hbase.sh
   
   # 关闭kafka
   [lelandyan@hadoop102 ~]$ kafka.sh stop
   
   # 关闭zookeeper集群
   [lelandyan@hadoop102 ~]$ zk.sh stop
   
   # 关闭hadoop集群
   [lelandyan@hadoop102 ~]$ myhadoop stop
   
   # 查看hadoop102,hadoop103,hadoop104上所有的java进程
   [lelandyan@hadoop102 ~]$ jpsall
   ```

## 6.项目设计文档

| 项目实现 |                       详细解释跳转链接                       |
| :------- | :----------------------------------------------------------: |
| 数据模拟 | [:thumbsup:](https://github.com/LelandYan/Big_Data_Project/blob/main/doc/%E6%95%B0%E6%8D%AE%E6%A8%A1%E6%8B%9F.md) |
| 数据采集 | [:thumbsup:](https://github.com/LelandYan/Big_Data_Project/blob/main/doc/%E6%95%B0%E6%8D%AE%E9%87%87%E9%9B%86.md) |
| 数据分析 | [:thumbsup:](https://github.com/LelandYan/Big_Data_Project/blob/main/doc/%E6%95%B0%E6%8D%AE%E5%88%86%E6%9E%90.md)（未完成） |
| 数据展示 | [:thumbsup:](https://github.com/LelandYan/Big_Data_Project/blob/main/doc/%E6%95%B0%E6%8D%AE%E5%B1%95%E7%A4%BA.md)（未完成） |

## 7. 项目不足

1. 首先kafka的数据存储的位置可以进行修改，因为项目中将kafka中的安装根路径修改，导致数据存储和kafka安装目录不是同一个目录
2. maven的依赖关系有点乱，maven的学习还停留在使用阶段，一个项目中子模块之间的依赖关系不清晰，可以重写熟悉项目中各个子模块的依赖关系，删除不必要的依赖关系，减少jar的大小
3. mysql的jar和redis对应jar的版本问题，项目中使用的是mysql8和redis3，但是hadoop3需要的依赖则是老版本的jar依赖，但是这里没有出现问题，连接正常。但是在后面的ssm的web项目中，则需要更新为对应的jar包，这里可能因为ssm项目依赖冲突导致，这里可以修改前面hadoop里面的mysql和redis的jar包进行测试
4. 数据可视化不足，只是简单的测试了从数据库mysql和缓存redis中拿出数据存入mysql以便后续数据可视化的使用
5. 封装不足，虽然项目中使用注解封装对象，但是后面的使用协处理的使用依然使用的手动编写对象
6. 业务不足，模拟的生成的数据，可以模拟生成更多的指标，例如亲密度等
7. 数据流依然不是一直通的，模拟数据是不断生成的，数据采集也是动态的，但是分析不是动态的，数据展示也不是动态，所以可以设置定时器，每隔一段时间，就从hbase中取数据，进行分析，存入mysql和redis中，进行数据更新展示

