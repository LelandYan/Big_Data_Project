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

| 项目模块名称 | 项目对应模块名称                  |
| :----------- | --------------------------------- |
| 数据模拟     | /project/ct-producer              |
| 数据采集消费 | /project/ct-consumer              |
| 数据分析     | /project/ct-analysis              |
| 数据展示     | /project/ct-web2                  |
| 通用工具     | /project/ct-common                |
| 数据缓存     | /project/ct-cache                 |
| 协处理       | /project/ct-consumer-corprocessor |

## 5.项目设计文档

| 项目实现 |                       详细解释跳转链接                       |
| :------- | :----------------------------------------------------------: |
| 数据模拟 | [:thumbsup:](https://github.com/LelandYan/Big_Data_Project/blob/main/doc/%E6%95%B0%E6%8D%AE%E6%A8%A1%E6%8B%9F.md) |
| 数据采集 | [:thumbsup:](https://github.com/LelandYan/Big_Data_Project/blob/main/doc/%E6%95%B0%E6%8D%AE%E9%87%87%E9%9B%86.md) |
| 数据分析 | [:thumbsup:](https://github.com/LelandYan/Big_Data_Project/blob/main/doc/%E6%95%B0%E6%8D%AE%E5%88%86%E6%9E%90.md) |
| 数据展示 | [:thumbsup:](https://github.com/LelandYan/Big_Data_Project/blob/main/doc/%E6%95%B0%E6%8D%AE%E5%B1%95%E7%A4%BA.md) |

## 

