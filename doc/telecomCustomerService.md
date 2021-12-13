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

硬件环境：

|      | hadoop102(mysql) | hadoop103 | hadoop104(redies) |
| ---- | ---------------- | --------- | ----------------- |
| 内存 | 4G               | 2G        | 2G                |
| CPU  | 2核              | 1核       | 1核               |
| 硬盘 | 50G              | 50G       | 50G               |

[lelandyan@hadoop102 data]$ java -jar ct-producer.jar /opt/module/data/contact.log /opt/module/data/call.log



bin/kafka-topics.sh --zookeeper hadoop102:2181 --topic ct --create --replication-factor 2 --partitions 3



bin/kafka-topics.sh --zookeeper hadoop102:2181 --list ct



bin/kafka-console-consumer.sh --bootstrap-server hadoop102:9092 --topic ct --from-beginning



bin/flume-ng agent -c conf/ -n a1 -f /opt/module/data/flume-2-kafka.conf

start-hbase.sh 

ct-consumer-coprocessor-1.0-SNAPSHOT.jar



/opt/module/hadoop-3.1.3/bin/yarn jar ct_analysis_jar/ct-analysis.jar



systemctl start mysqld

	cd /usr/local/redis/
	./bin/redis-server ./redis.conf

  cd /usr/local/redis  ./bin/redis-cli shutdown



config set stop-writes-on-bgsave-error no

或者

请在/etc/sysctl.conf 添加一项 'vm.overcommit_memory = 1' ，然后重启（或者运行命令'sysctl vm.overcommit_memory=1' ）使其生效。）

项目开发文档：

学习笔记：
