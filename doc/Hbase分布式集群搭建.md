## Hbase分布式集群搭建

### 1. Hbase分布式集群搭建

```shell
# 解压并修改文件名称
[lelandyan@hadoop102 software]$ tar -zxvf hbase-2.0.5-bin.tar.gz -C /opt/module/
[lelandyan@hadoop102 module]$ mv hbase-2.0.5-bin/ hbase

# 配置hbase环境变量，，加入下面内容，并生效，测试是否安装成功
[lelandyan@hadoop102 hbase]$ sudo vim /etc/profile.d/my_env.sh

#HBASE_HOME
export HADOOP_HOME=/opt/module/hbase
export PATH=$PATH:$HADOOP_HOME/bin
export PATH=$PATH:$HADOOP_HOME/sbin

# 使环境变量生效
[lelandyan@hadoop102 hbase]$ source /etc/profile

# 同步到hadoop102，hadoop103上
[lelandya@hadoop102 hbase]$ xsync /opt/module/hbase
[lelandya@hadoop102 hbase]$ xsync /etc/profile.d/my_env.sh

# 修改hbase-env.sh文件
[lelandya@hadoop102 hbase]$ cd conf
[lelandya@hadoop102 conf]$ vim hbase-env.sh 
# 不使用 hbase 自身集成的 zookeepe
export HBASE_MANAGES_ZK=false


# 修改hbase-site.xml
[lelandya@hadoop102 conf]$ vim hbase-site.xml 
# 加入下面内容
<configuration>
<!-- 是否开启集群模式 -->
  <property>
    <name>hbase.cluster.distributed</name>
    <value>true</value>
  </property>
 <!-- hbase的临时文件路径 -->
 <property>
    <name>hbase.tmp.dir</name>
    <value>./tmp</value>
  </property>
  <!-- 是否启用不安全流能力强制执行 -->
  <property>
    <name>hbase.unsafe.stream.capability.enforce</name>
    <value>false</value>
  </property>

  <property>
    <name>hbase.rootdir</name>
    <value>hdfs://hadoop102:8020/hbase</value>
  </property>
  <property>
    <name>hbase.zookeeper.quorum</name>
    <value>hadoop102,hadoop103,hadoop104</value>
  </property>
</configuration>


# 修改regionservers文件
[lelandya@hadoop102 conf]$ vim regionservers 
# 加入下面内容
hadoop102
hadoop103
hadoop104

# 添加backup-masters文件,这里可以不操作，如果有空闲机器可以添加
[lelandya@hadoop102 conf]$ vim backup-masters
# 加入
hadoop102

# 拷贝hdfs-site.xml 文件
[lelandya@hadoop102 conf]$ cp /opt/model/hadoop-3.1.3/etc/hadoop/hdfs-site.xml ./

# 将配置文件分发到hadoop102，hadoop103上
[lelandya@hadoop102 hbase]$ xsync /opt/module/hbase
```

### 2. 开启Hbase分布式集群

开启Habse分布式集群首先要关闭防火墙和开启zookeeper集群和hadoop集群

```shell
[lelandya@hadoop102 hbase]$ zk.sh start
[lelandya@hadoop102 hbase]$ myhadoop start
[lelandya@hadoop102 hbase]$ bin/start-hbase.sh
```

验证是否成功开启：

访问 16010 端口  http://hadoop102:16010/