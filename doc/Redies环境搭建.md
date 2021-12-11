## Redis环境搭建

### 1. Redis安装

这里为了减少hadoop102的压力，将redis缓存安装于hadoop104机器上.

```shell
# 首先需要安装gcc环境，redis是依赖于gcc环境的
[lelandyan@hadoop104 software]$ yum install gcc-c++

# 下载redis-3.0.0.tar.gz,这里自行下载
[lelandyan@hadoop104 software]$ tar -zxf redis-3.0.0.tar.gz

# 编译redis源码
[lelandyan@hadoop104 software] $ cd redis-3.0.0
[lelandyan@hadoop104 software] $ make

# 安装redis
[lelandyan@hadoop104 software] $ make install PREFIX=/usr/local/redis

# 启动redis
# 前端启动 执行  ./redis-server
# 后端启动 修改redis.conf，将daemonize由no改为yes
[lelandyan@hadoop104 redis] $ vim redis.conf
[lelandyan@hadoop104 redis] $ cd /usr/local/redis/
[lelandyan@hadoop104 redis] $ ./bin/redis-server ./redis.conf
# 关闭
[lelandyan@hadoop104 redis] $ cd /usr/local/redis  
[lelandyan@hadoop104 redis] $ ./bin/redis-cli shutdown

# 开启远程
# 开放后，也许会发现死活连不上的现象，这时候是因为安装目录bin下redis.conf文件里面的bin绑定了127.0.0.1的缘故，这时候，需要我们把这个改成0.0.0.0，然后重启redis （./redis-server redis.conf）,这时候执行netstart -tnlp会发现如下

# 如果使用java远程写入报错,可以在redis命令行窗口中尝试执行下面代码
config set stop-writes-on-bgsave-error no
# 或者 请在/etc/sysctl.conf 添加一项 'vm.overcommit_memory = 1' ，然后重启（或者运行命令'sudo sysctl vm.overcommit_memory=1' ）使其生效。）
```

