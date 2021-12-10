# 基于Hadoop3的电信客服大数据项目

该项目使用的技术与框架为：Hadoop、Flume、Kafka、Hbase、Zookeeper、Maven、Spring、SpringMVC、Mybatis、Mysql、Redies、echart，最后实现查询和结果页面为：

![](imgs\2.png)

![](imgs\3.png)

## 1.项目背景

通信运营商每时每刻会产生大量的通信数据，例如通话记录，短信记录，彩信记录，第三方服务资费等等繁多信息。数据量如此巨大，除了要满足用户的实时查询和展示之外，还需要定时定期的对已有数据进行离线的分析处理。例如，当日话单，月度话单，季度话单，年度话单，通话详情，通话记录等等。

本项目就是统计每天、每月以及每年的每个人的通话次数及时长并进行可视化。

## 2.项目架构

项目架构图如下图：

![](imgs\1.png)

## 3.项目运行环境与搭建

系统环境：

| 系统    | 版本                |
| ------- | ------------------- |
| windows | win10               |
| linux   | CentOS-7.5-x86-1804 |

开发工具：

| 工具  | 版本         |
| ----- | ------------ |
| idea  | 2021.2专业版 |
| maven | 3.6.1        |
| JDK   | 1.8          |

集群环境：

| 框架      | 版本        |
| --------- | ----------- |
| hadoop    | 3.1.3  完成 |
| zookeeper | 3.5.7  完成 |
| hbase     | 2.0.5 完成  |
| flume     | 1.9.0完成   |
| kafka     | 2.4.1完成   |

虚拟机环境：

|                  | hadoop102    | hadoop103 | hadoop104    |
| ---------------- | ------------ | --------- | ------------ |
| 内存             | 4G           | 2G        | 2G           |
| CPU              | 2核          | 2核       | 2核          |
| 硬盘             | 50G          | 50G       | 50G          |
| 安装数据库、缓存 | mysql-8.0.27 |           | redies-3.0.0 |

## 4.项目实现

### 4.1 数据模拟

### 4.2 数据采集

### 4.3 数据分析

### 4.4 数据展示

## 5.项目总结

