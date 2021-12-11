## Kafka环境搭建

**这里需要注意，kafka环境分布式环境是要基于zookeeper分布式环境之上的才能使用，所以需要首先安装zookeeper分布式环境。**

### 1. Kafka分布式环境搭建

```shell
# 首先启动hadoop102，hadoop103，hadoop104上的zookeeper分布式集群
[lelandyan@hadoop102 software]$ zk.sh start

# 解压并修改文件名称
[lelandyan@hadoop102 software]$ tar -zxvf kafka_2.11-2.4.1.tgz -C /opt/module/
[lelandyan@hadoop102 module]$ mv kafka_2.11-2.4.1/ kafka

# 在/opt/module/kafka目录下创建logs文件夹
[lelandyan@hadoop102 kafka]$ mkdir logs

# 修改配置文件
[lelandyan@hadoop102 kafka]$ cd config/
[lelandyan@hadoop102 config]$ vi server.properties
# 修改并添加下列内容
broker.id=0
log.dirs=/opt/module/kafka_2.11-2.4.1/kafka-logs
zookeeper.connect=hadoop102:2181,hadoop103:2181,hadoop104:2181

# 将kafka同步到hadoop103,hadoop104上
[lelandyan@hadoop102 module]$ xsync kafka/
# 分别在hadoop103和hadoop104上修改配置文件/opt/module/kafka/config/server.properties中的broker.id=1、broker.id=2

# 启动集群 依次在hadoop102、hadoop103、hadoop104节点上启动kafka
[lelandyan@hadoop102 kafka]$ /opt/module/kafka/bin/kafka-server-start.sh -daemon /opt/module/kafka/config/server.properties
[lelandyan@hadoop103 kafka]$ /opt/module/kafka/bin/kafka-server-start.sh -daemon /opt/module/kafka/config/server.properties
[lelandyan@hadoop104 kafka]$ /opt/module/kafka/bin/kafka-server-start.sh -daemon /opt/module/kafka/config/server.properties

# 关闭集群
[lelandyan@hadoop102 kafka]$ /opt/module/kafka/bin/kafka-server-stop.sh
[lelandyan@hadoop103 kafka]$ /opt/module/kafka/bin/kafka-server-stop.sh
[lelandyan@hadoop104 kafka]$ /opt/module/kafka/bin/kafka-server-stop.sh

```

### 2. 编写kafka集群启动和停止脚本

```shell
[lelandyan@hadoop102 bin]$ vim kafka.sh
# 文件内容如下
#!/bin/bash

case $1 in
"start"){
   for i in hadoop102 hadoop103 hadoop104
   do
      echo ---------- kafka $i 启动 ------------
      ssh $i "/opt/module/kafka/bin/kafka-server-start.sh -daemon /opt/module/kafka/config/server.properties"
   done
};;
"stop"){
   for i in hadoop102 hadoop103 hadoop104
   do
      echo ---------- kafka $i 停止 ------------
      ssh $i "/opt/module/kafka/bin/kafka-server-stop.sh"
   done
};;
esac

# 加入权限
[lelandyan@hadoop102 bin]$ chmod +x kafka.sh
[lelandyan@hadoop102 module]$ kafka.sh start
[lelandyan@hadoop102 module]$ kafka.sh stop
```

