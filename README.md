# 基于Hadoop3的电信客服大数据项目

项目运行虚拟环境百度云下载：:clap::clap::clap:

项目采用技术：Hadoop、Flume、Kafka、Hbase、Zookeeper、Maven、Spring、SpringMVC、Mybatis、Mysql、Redies、Echart。

项目运行环境：Win10+16G+I7

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

| 相关环境搭建相关文档    |                       详细解释跳转链接                       |
| :---------------------- | :----------------------------------------------------------: |
| 模板虚拟机环境搭建      | [:thumbsup:](https://github.com/LelandYan/ResearchRelationshipNetwork_KnowledgeGraph/blob/main/doc/%E7%BD%91%E7%BB%9C%E7%88%AC%E8%99%AB%E6%8A%80%E6%9C%AF%E5%88%86%E6%9E%90.md) |
| Hadoop分布式集群搭建    | [:thumbsup:](https://github.com/LelandYan/ResearchRelationshipNetwork_KnowledgeGraph/blob/main/doc/BigGraph%E5%B5%8C%E5%85%A5%E5%90%91%E9%87%8F%E7%9B%B8%E4%BC%BC%E7%AD%89.md) |
| Zookeeper分布式集群搭建 | [:thumbsup:](https://github.com/LelandYan/ResearchRelationshipNetwork_KnowledgeGraph/blob/main/doc/Fasttext%E6%96%87%E6%9C%AC%E5%88%86%E7%B1%BB%E6%A8%A1%E5%9E%8B.md) |
| Hbase分布式集群搭建     | [:thumbsup:](https://github.com/LelandYan/ResearchRelationshipNetwork_KnowledgeGraph/blob/main/doc/%E5%AE%9E%E4%BD%93%E5%85%B3%E7%B3%BB%E6%8A%BD%E5%8F%96%E6%A8%A1%E5%9E%8B%E4%BB%8B%E7%BB%8D.md) |
| Flume环境搭建           | [:thumbsup:](https://github.com/LelandYan/ResearchRelationshipNetwork_KnowledgeGraph/blob/main/doc/%E5%85%B3%E9%94%AE%E8%AF%8D%E6%90%9C%E7%B4%A2%E7%9A%84%E5%AE%B9%E9%94%99%E6%80%A7%E5%92%8C%E5%88%86%E8%AF%8D%EF%BC%88%E5%81%8F%E5%90%91%E4%BA%8E%E5%8E%9F%E7%90%86%EF%BC%89.md) |
| Kafka环境搭建           | [:thumbsup:](https://github.com/LelandYan/ResearchRelationshipNetwork_KnowledgeGraph/blob/main/doc/%E4%B8%8D%E5%90%8C%E7%B1%BB%E5%9E%8B%E5%85%B3%E9%94%AE%E8%AF%8D%E6%90%9C%E7%B4%A2%E7%9F%A5%E8%AF%86.md) |
| Mysql环境搭建           | [:thumbsup:](https://github.com/LelandYan/ResearchRelationshipNetwork_KnowledgeGraph/blob/main/doc/%E6%90%9C%E7%B4%A2%E6%A1%86%E6%A3%80%E7%B4%A2%E5%8A%9F%E8%83%BD%EF%BC%88%E6%B5%81%E7%A8%8B%E5%9B%BE%EF%BC%89.md) |
| Redies环境搭建          | [:thumbsup:](https://github.com/LelandYan/ResearchRelationshipNetwork_KnowledgeGraph/blob/main/doc/%E5%9B%BE%E6%95%B0%E6%8D%AE%E5%BA%93neo4j.md) |

## 4.项目实现

### 4.1 数据模拟

### 4.2 数据采集

### 4.3 数据分析

### 4.4 数据展示

## 5.项目总结

