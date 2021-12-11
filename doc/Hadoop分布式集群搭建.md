## Hadoop分布式集群搭建

### 1. 克隆虚拟机

利用模板机hadoop100，克隆三台虚拟机：hadoop102 hadoop103 hadoop104

注意：克隆时，要先关闭hadoop100，选择克隆的时候要注意选择完整克隆

hadoop102 hadoop103 hadoop104原理相同，这里以修改hadoop102说明：

```shell
# 1. 修改克隆机虚拟机的静态ip
[root@hadoop100 ~]# vim /etc/sysconfig/network-scripts/ifcfg-ens33
# 改成
DEVICE=ens33
TYPE=Ethernet
ONBOOT=yes
BOOTPROTO=static
NAME="ens33"
IPADDR=192.168.10.102
PREFIX=24
GATEWAY=192.168.10.2
DNS1=192.168.10.2

# 2. 修改克隆机主机名
[root@hadoop100 ~]# vim /etc/hostname
hadoop102

# 3. 完成后重启克隆机hadoop102
[root@hadoop100 ~]# reboot

# 4. 安装jdk 注意：安装JDK前，一定确保提前删除了虚拟机自带的JDK 用XShell传输工具将JDK导入到opt目录下面的software文件夹下面
[lelandyan@hadoop102 software]$ tar -zxvf jdk-8u212-linux-x64.tar.gz -C /opt/module/

# 5. 配置jdk环境变量
# 新建/etc/profile.d/my_env.sh文件，并添加内容，并生效
[lelandyan@hadoop102 ~]$ sudo vim /etc/profile.d/my_env.sh

#JAVA_HOME
export JAVA_HOME=/opt/module/jdk1.8.0_212
export PATH=$PATH:$JAVA_HOME/bin

[lelandyan@hadoop102 ~]$ source /etc/profile

# 6. 测试jdk是否安装成功
[lelandyan@hadoop102 ~]$ java -version


# 7. 安装hadoop3 用XShell文件传输工具将hadoop-3.1.3.tar.gz导入到opt目录下面的software文件夹下面，hadoop-3.1.3.tar.gz可以自行下载
[lelandyan@hadoop102 software]$ tar -zxvf hadoop-3.1.3.tar.gz -C /opt/module/

# 配置hadoop环境变量，，加入下面内容，并生效，测试是否安装成功
[lelandyan@hadoop102 hadoop-3.1.3]$ sudo vim /etc/profile.d/my_env.sh

#HADOOP_HOME
export HADOOP_HOME=/opt/module/hadoop-3.1.3
export PATH=$PATH:$HADOOP_HOME/bin
export PATH=$PATH:$HADOOP_HOME/sbin

[lelandyan@hadoop102 hadoop-3.1.3]$ source /etc/profile

[lelandyan@hadoop102 hadoop-3.1.3]$ hadoop version
```

### 2. 配置hadoop103、hadoop104，步骤于hadoop102类似，只是主机名和ip地址不同

编写集群分发脚本：循环复制文件到所有节点的相同目录下

```shell
# 在/home/atguigu/bin目录下创建xsync文件
[lelandyan@hadoop102 opt]$ cd /home/atguigu
[lelandyan@hadoop102 ~]$ mkdir bin
[lelandyan@hadoop102 ~]$ cd bin
[lelandyan@hadoop102 bin]$ vim xsync
# 加入下面代码
#!/bin/bash

#1. 判断参数个数
if [ $# -lt 1 ]
then
    echo Not Enough Arguement!
    exit;
fi

#2. 遍历集群所有机器
for host in hadoop102 hadoop103 hadoop104
do
    echo ====================  $host  ====================
    #3. 遍历所有目录，挨个发送

    for file in $@
    do
        #4. 判断文件是否存在
        if [ -e $file ]
            then
                #5. 获取父目录
                pdir=$(cd -P $(dirname $file); pwd)

                #6. 获取当前文件的名称
                fname=$(basename $file)
                ssh $host "mkdir -p $pdir"
                rsync -av $pdir/$fname $host:$pdir
            else
                echo $file does not exists!
        fi
    done
done

# 修改脚本 xsync 具有执行权限 将脚本复制到/bin中，以便全局调用，同步环境变量配置（root所有者）
[lelandyan@hadoop102 bin]$ chmod +x xsync
[lelandyan@hadoop102 ~]$ xsync /home/atguigu/bin
[lelandyan@hadoop102 bin]$ sudo cp xsync /bin/
[lelandyan@hadoop102 ~]$ sudo ./bin/xsync /etc/profile.d/my_env.sh

# 让环境变量生效
[lelandyan@hadoop103 bin]$ source /etc/profile
[lelandyan@hadoop104 opt]$ source /etc/profile
```

### 3. SSH无密码登陆配置

````shell
[lelandyan@hadoop102 .ssh]$ pwd
[lelandyan@hadoop102 .ssh]$ ssh-keygen -t rsa
# 然后敲（三个回车），就会生成两个文件id_rsa（私钥）、id_rsa.pub（公钥）

# 将公钥拷贝到要免密登录的目标机器上
[lelandyan@hadoop102 .ssh]$ ssh-copy-id hadoop102
[lelandyan@hadoop102 .ssh]$ ssh-copy-id hadoop103
[lelandyan@hadoop102 .ssh]$ ssh-copy-id hadoop104
````

还需要在hadoop103上采用lelandyan账号配置一下无密登录到hadoop102、hadoop103、hadoop104服务器上。

还需要在hadoop104上采用lelandyan账号配置一下无密登录到hadoop102、hadoop103、hadoop104服务器上。

还需要在hadoop102上采用root账号，配置一下无密登录到hadoop102、hadoop103、hadoop104；

### 4. 集群配置

注意： 

NameNode和SecondaryNameNode不要安装在同一台服务器

ResourceManager也很消耗内存，不要和NameNode、SecondaryNameNode配置在同一台机器上。

|      | hadoop102          | hadoop103                    | hadoop104                   |
| ---- | ------------------ | ---------------------------- | --------------------------- |
| HDFS | NameNode  DataNode | DataNode                     | SecondaryNameNode  DataNode |
| YARN | NodeManager        | ResourceManager  NodeManager | NodeManager                 |

配置集群配置文件：

配置core-site.xml

```shell
[lelandyan@hadoop102 ~]$ cd $HADOOP_HOME/etc/hadoop
[lelandyan@hadoop102 hadoop]$ vim core-site.xml

# 文件内容如下
<?xml version="1.0" encoding="UTF-8"?>
<?xml-stylesheet type="text/xsl" href="configuration.xsl"?>

<configuration>
    <!-- 指定NameNode的地址 -->
    <property>
        <name>fs.defaultFS</name>
        <value>hdfs://hadoop102:8020</value>
    </property>

    <!-- 指定hadoop数据的存储目录 -->
    <property>
        <name>hadoop.tmp.dir</name>
        <value>/opt/module/hadoop-3.1.3/data</value>
    </property>

    <!-- 配置HDFS网页登录使用的静态用户为lelandyan -->
    <property>
        <name>hadoop.http.staticuser.user</name>
        <value>lelandyan</value>
    </property>
</configuration>
```

配置hdfs-site.xml

```shell
[lelandyan@hadoop102 hadoop]$ vim hdfs-site.xml

# 文件内容如下
<?xml version="1.0" encoding="UTF-8"?>
<?xml-stylesheet type="text/xsl" href="configuration.xsl"?>

<configuration>
	<!-- nn web端访问地址-->
	<property>
        <name>dfs.namenode.http-address</name>
        <value>hadoop102:9870</value>
    </property>
	<!-- 2nn web端访问地址-->
    <property>
        <name>dfs.namenode.secondary.http-address</name>
        <value>hadoop104:9868</value>
    </property>
</configuration>
```

配置yarn-site.xml

```shell
[lelandyan@hadoop102 hadoop]$ vim yarn-site.xml

# 文件内容如下
<?xml version="1.0" encoding="UTF-8"?>
<?xml-stylesheet type="text/xsl" href="configuration.xsl"?>

<configuration>
    <!-- 指定MR走shuffle -->
    <property>
        <name>yarn.nodemanager.aux-services</name>
        <value>mapreduce_shuffle</value>
    </property>

    <!-- 指定ResourceManager的地址-->
    <property>
        <name>yarn.resourcemanager.hostname</name>
        <value>hadoop103</value>
    </property>

    <!-- 环境变量的继承 -->
    <property>
        <name>yarn.nodemanager.env-whitelist</name>
<value>JAVA_HOME,HADOOP_COMMON_HOME,HADOOP_HDFS_HOME,HADOOP_CONF_DIR,CLASSPATH_PREPEND_DISTCACHE,HADOOP_YARN_HOME,HADOOP_MAPRED_HOME</value>
    </property>
</configuration>
```

配置mapred-site.xml

```shell
[lelandyan@hadoop102 hadoop]$ vim mapred-site.xml

# 文件内容如下：
<?xml version="1.0" encoding="UTF-8"?>
<?xml-stylesheet type="text/xsl" href="configuration.xsl"?>

<configuration>
	<!-- 指定MapReduce程序运行在Yarn上 -->
    <property>
        <name>mapreduce.framework.name</name>
        <value>yarn</value>
    </property>
</configuration>
```

```shell
# 在集群上分发配置好的Hadoop配置文件
[lelandyan@hadoop102 hadoop]$ xsync /opt/module/hadoop-3.1.3/etc/hadoop/
```

配置workers

```shell
[lelandyan@hadoop102 hadoop]$ vim /opt/module/hadoop-3.1.3/etc/hadoop/workers

# 文件加入下面内容 注意：该文件中添加的内容结尾不允许有空格，文件中不允许有空行。
hadoop102
hadoop103
hadoop104

# 同步所有节点配置文件
[lelandya@hadoop102 hadoop]$ xsync /opt/module/hadoop-3.1.3/etc
```

启动集群

```shell
# 如果集群是第一次启动，需要执行namenode格式化操作
[lelandya@hadoop102 hadoop-3.1.3]$ hdfs namenode -format

# 启动HDFS
[lelandya@hadoop102 hadoop-3.1.3]$ sbin/start-dfs.sh
# 在配置了ResourceManager的节点（hadoop103）启动YARN
[lelandya@hadoop103 hadoop-3.1.3]$ sbin/start-yarn.sh
```

Web端查看HDFS的NameNode

（a）浏览器中输入：http://hadoop102:9870

（b）查看HDFS上存储的数据信息

Web端查看YARN的ResourceManager

（a）浏览器中输入：http://hadoop103:8088

（b）查看YARN上运行的Job信息

### 5. 配置历史服务器

配置mapred-site.xml

```shell
[lelandyan@hadoop102 hadoop]$ vim mapred-site.xml

# 加入下面内容
<!-- 历史服务器端地址 -->
<property>
    <name>mapreduce.jobhistory.address</name>
    <value>hadoop102:10020</value>
</property>

<!-- 历史服务器web端地址 -->
<property>
    <name>mapreduce.jobhistory.webapp.address</name>
    <value>hadoop102:19888</value>
</property>

# 分发配置
[lelandyan@hadoop102 hadoop]$ xsync $HADOOP_HOME/etc/hadoop/mapred-site.xml

# 在hadoop102启动历史服务器
[lelandyan@hadoop102 hadoop]$ mapred --daemon start historyserver

# 查看历史服务器是否启动
[lelandyan@hadoop102 hadoop]$ jps
```

查看JobHistory

http://hadoop102:19888/jobhistory

### 6. 配置日志的聚集

日志聚集概念：应用运行完成以后，将程序运行日志信息上传到HDFS系统上。

日志聚集功能好处：可以方便的查看到程序运行详情，方便开发调试。

注意：开启日志聚集功能，需要重新启动NodeManager 、ResourceManager和HistoryServer。

配置yarn-site.xml

```shell
[lelandyan@hadoop102 hadoop]$ vim yarn-site.xml

# 加入下面内容
<!-- 开启日志聚集功能 -->
<property>
    <name>yarn.log-aggregation-enable</name>
    <value>true</value>
</property>
<!-- 设置日志聚集服务器地址 -->
<property>  
    <name>yarn.log.server.url</name>  
    <value>http://hadoop102:19888/jobhistory/logs</value>
</property>
<!-- 设置日志保留时间为7天 -->
<property>
    <name>yarn.log-aggregation.retain-seconds</name>
    <value>604800</value>
</property>

# 分发配置,关闭NodeManager 、ResourceManager和HistoryServer
[lelandyan@hadoop102 hadoop]$ xsync $HADOOP_HOME/etc/hadoop/yarn-site.xml
[lelandyan@hadoop103 hadoop-3.1.3]$ sbin/stop-yarn.sh
[lelandyan@hadoop103 hadoop-3.1.3]$ mapred --daemon stop historyserver

# 重启NodeManager 、ResourceManager和HistoryServer
[lelandyan@hadoop103 ~]$ start-yarn.sh
[lelandyan@hadoop102 ~]$ mapred --daemon start historyserver

# 执行WordCount程序 测试集群
[lelandyan@hadoop102 hadoop-3.1.3]$ hadoop jar share/hadoop/mapreduce/hadoop-mapreduce-examples-3.1.3.jar wordcount /input /output
```

编写Hadoop集群常用脚本，myhadoop启动脚本，jpsall的java进程查看脚本

```shell
[lelandyan@hadoop102 ~]$ cd /home/atguigu/bin
[lelandyan@hadoop102 bin]$ vim myhadoop.sh

# 文件内容
#!/bin/bash

if [ $# -lt 1 ]
then
    echo "No Args Input..."
    exit ;
fi

case $1 in
"start")
        echo " =================== 启动 hadoop集群 ==================="

        echo " --------------- 启动 hdfs ---------------"
        ssh hadoop102 "/opt/module/hadoop-3.1.3/sbin/start-dfs.sh"
        echo " --------------- 启动 yarn ---------------"
        ssh hadoop103 "/opt/module/hadoop-3.1.3/sbin/start-yarn.sh"
        echo " --------------- 启动 historyserver ---------------"
        ssh hadoop102 "/opt/module/hadoop-3.1.3/bin/mapred --daemon start historyserver"
;;
"stop")
        echo " =================== 关闭 hadoop集群 ==================="

        echo " --------------- 关闭 historyserver ---------------"
        ssh hadoop102 "/opt/module/hadoop-3.1.3/bin/mapred --daemon stop historyserver"
        echo " --------------- 关闭 yarn ---------------"
        ssh hadoop103 "/opt/module/hadoop-3.1.3/sbin/stop-yarn.sh"
        echo " --------------- 关闭 hdfs ---------------"
        ssh hadoop102 "/opt/module/hadoop-3.1.3/sbin/stop-dfs.sh"
;;
*)
    echo "Input Args Error..."
;;
esac

[lelandyan@hadoop102 bin]$ chmod +x myhadoop.sh
[lelandyan@hadoop102 ~]$ cd /home/atguigu/bin
[lelandyan@hadoop102 bin]$ vim jpsall

# 加入文件内容
#!/bin/bash

for host in hadoop102 hadoop103 hadoop104
do
        echo =============== $host ===============
        ssh $host jps 
done

[lelandyan@hadoop102 bin]$ chmod +x jpsall

# 分发/home/lelandyan/bin目录，保证自定义脚本在三台机器上都可以使用
[lelandyan@hadoop102 ~]$ xsync /home/lelandyan/bin/
```

| 端口名称                  | Hadoop3.x              |
| ------------------------- | ---------------------- |
| NameNode内部通信端口      | **8020** /  9000/ 9820 |
| NameNode HTTP UI          | 9870                   |
| MapReduce查看执行任务端口 | 8088                   |
| 历史服务器通信端口        | 19888                  |

### 7.集群时间同步

如果服务器在公网环境（能连接外网），可以不采用集群时间同步，因为服务器会定期和公网时间进行校准；如果服务器在内网环境，必须要配置集群时间同步，否则时间久了，会产生时间偏差，导致集群执行任务时间不同步。

这里我们采用连接公网的模式，不需要进行时间同步校准

