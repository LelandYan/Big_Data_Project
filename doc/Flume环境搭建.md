## Flume环境搭建

### 1. Flume安装部署

Flume 官网地址：http://flume.apache.org/

```shell
# 将 apache-flume-1.9.0-bin.tar.gz 上传到 linux 的/opt/software 目录下，并解压
[lelandyan@hadoop102 software]$ tar -zxf /opt/software/apache- flume-1.9.0-bin.tar.gz -C /opt/module/

# （3）	修改 apache-flume-1.9.0-bin 的名称为 flume 
[lelandyan@hadoop102 module]$ mv /opt/module/apache-flume-1.9.0-bin
/opt/module/flume

# 将 lib 文件夹下的guava-11.0.2.jar 删除以兼容 Hadoop 3.1.3 
[lelandyan@hadoop102 lib]$	rm /opt/module/flume/lib/guava- 11.0.2.jar

# 编写/opt/module/data/flume-2-kafka.conf文件后，可以启动flume数据采集
bin/flume-ng agent -c conf/ -n a1 -f /opt/module/data/flume-2-kafka.conf
```



