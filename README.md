# 基于Hadoop3的电信客服大数据项目

项目应用技术：Hadoop、Flume、Kafka、Hbase、Zookeeper、Maven、Spring、SpringMVC、Mybatis、Mysql、Redies、Echart。

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

| 框架      | 版本  |
| --------- | ----- |
| Hadoop    | 3.1.3 |
| Zookeeper | 3.5.7 |
| Hbase     | 2.0.5 |
| Flume     | 1.9.0 |
| Kafka     | 2.4.1 |

虚拟机环境：

| 主机名称         | hadoop102    | hadoop103 | hadoop104    |
| ---------------- | ------------ | --------- | ------------ |
| 内存             | 4G           | 2G        | 2G           |
| CPU              | 2核          | 2核       | 2核          |
| 硬盘             | 50G          | 50G       | 50G          |
| 安装数据库、缓存 | Mysql-8.0.27 |           | Redies-3.0.0 |

### 3.2 项目运行环境搭建

#### 模板虚拟机环境搭建

#### Hadoop分布式集群搭建

#### Zookeeper分布式集群搭建

#### Hbase分布式集群搭建

#### Flume环境搭建

#### Kafka环境搭建

#### Mysql环境搭建

#### Redies环境搭建

## 4.项目实现

### 4.1 数据模拟

### 4.2 数据采集

### 4.3 数据分析

### 4.4 数据展示

## 5.项目总结

