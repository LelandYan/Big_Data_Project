## Zookeeper分布式集群搭建

### 1. 集群规划

在 hadoop102、 hadoop103 和 hadoop104 三个节点上都部署 Zookeeper。

### 2. 集群安装

```shell
# 在 hadoop102 解压 Zookeeper 安装包到/opt/module/目录下，apache-zookeeper-3.5.7-bin.tar.gz可以自行下载
[lelandyan@hadoop102 software]$ tar -zxvf apache-zookeeper-3.5.7-bin.tar.gz -C /opt/module/

# 修改 apache-zookeeper-3.5.7-bin 名称为 zookeeper-3.5.7
[lelandyan@hadoop102 module]$ mv apache-zookeeper-3.5.7-bin/zookeeper-3.5.7

# 配置服务器编号
# 在/opt/module/zookeeper-3.5.7/这个目录下创建 zkData
# 在/opt/module/zookeeper-3.5.7/zkData 目录下创建一个 myid 的文件
[lelandyan@hadoop102 zookeeper-3.5.7]$ mkdir zkData
[lelandyan@hadoop102 zkData]$ vi myid
# 文件内容为 2

# 拷贝配置好的 zookeeper 到其他机器上
[lelandyan@hadoop102 module ]$ xsync zookeeper-3.5.7
# 并分别在 hadoop103、 hadoop104 上修改 myid 文件中内容为 3、 4

# 重命名/opt/module/zookeeper-3.5.7/conf 这个目录下的 zoo_sample.cfg 为 zoo.cfg
[lelandyan@hadoop102 conf]$ mv zoo_sample.cfg zoo.cfg
[lelandyan@hadoop102 conf]$ vim zoo.cfg
# 修改数据存储路径配置
dataDir=/opt/module/zookeeper-3.5.7/zkData
# 增加下面配置
#######################cluster##########################
server.2=hadoop102:2888:3888
server.3=hadoop103:2888:3888
server.4=hadoop104:2888:3888

# 配置参数解读
# server.A=B:C:D。
# A 是一个数字，表示这个是第几号服务器；
# 集群模式下配置一个文件 myid， 这个文件在 dataDir 目录下，这个文件里面有一个数据
# 就是 A 的值， Zookeeper 启动时读取此文件，拿到里面的数据与 zoo.cfg 里面的配置信息比
# 较从而判断到底是哪个 server。
# B 是这个服务器的地址；
# C 是这个服务器 Follower 与集群中的 Leader 服务器交换信息的端口；
# D 是万一集群中的 Leader 服务器挂了，需要一个端口来重新进行选举，选出一个新的
# Leader，而这个端口就是用来执行选举时服务器相互通信的端口。

# 同步 zoo.cfg 配置文件
[lelandyan@hadoop102 conf]$ xsync zoo.cfg

# 启动 Zookeeper
[lelandyan@hadoop102 zookeeper-3.5.7]$ bin/zkServer.sh start
[lelandyan@hadoop103 zookeeper-3.5.7]$ bin/zkServer.sh start
[lelandyan@hadoop104 zookeeper-3.5.7]$ bin/zkServer.sh start

# 查看状态
[lelandyan@hadoop102 zookeeper-3.5.7]$ bin/zkServer.sh status
[lelandyan@hadoop103 zookeeper-3.5.7]$ bin/zkServer.sh status
[lelandyan@hadoop104 zookeeper-3.5.7]$ bin/zkServer.sh status
```

### 2. ZK集群启动停止脚本

```shell
# 在 hadoop102 的/home/atguigu/bin 目录下创建脚本
[lelandyan@hadoop102 bin]$ vim zk.sh

# 文件内容如下：
#!/bin/bash
case $1 in
"start"){
   for i in hadoop102 hadoop103 hadoop104
   do
      echo ---------- zookeeper $i 启动 ------------
      ssh $i "/opt/module/zookeeper-3.5.7/bin/zkServer.sh
start"
   done
};;
"stop"){
   for i in hadoop102 hadoop103 hadoop104
   do
      echo ---------- zookeeper $i 停止 ------------
      ssh $i "/opt/module/zookeeper-3.5.7/bin/zkServer.sh
stop"
   done
};;
"status"){
   for i in hadoop102 hadoop103 hadoop104
   do
      echo ---------- zookeeper $i 状态 ------------
      ssh $i "/opt/module/zookeeper-3.5.7/bin/zkServer.sh
status"
   done
};;
esac

# 增加脚本执行权限，并启动Zookeeper 集群
[lelandyan@hadoop102 bin]$ chmod u+x zk.sh
[lelandyan@hadoop102 module]$ zk.sh start
[lelandyan@hadoop102 module]$ zk.sh stop
```





