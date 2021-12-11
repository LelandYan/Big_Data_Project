##  MySql环境搭建

MySql数据库这里是存储的是对hbase数据分析后的结果，所以MySql数据库只需要安装一台机器上即可，这里选择安装在hadoop102上，因为hadoop102分配的内存较大

### 1.MySQL安装并配置远程登陆

```shell
# 进入/opt/software
[lelandyan@hadoop102 ~]$ cd /opt/software

# 下载yum数据源
[lelandyan@hadoop102 software]$ wget https://dev.mysql.com/get/mysql80-community-release-el7-3.noarch.rpm
[lelandyan@hadoop102 software]$ yum localinstall mysql80-community-release-el7-3.noarch.rpm

# 安装mysql服务 安装过程中有询问输入y回车即可。
yum install mysql-community-server

# 启动mysql服务
systemctl start mysqld

# 查看系统随机生成的密码
grep 'temporary password' /var/log/mysqld.log

# 使用随机密码登陆
mysql -uroot -p

# 用命令修改密码 长度不得小于8位 必须包含至少一个数字，一个小写字母，一个大写字母和一个特殊字符，这里我们将密码设置为Yanxiangpei.123
ALTER USER 'root'@'localhost' IDENTIFIED BY 'Yanxiangpei.123';
#如果存在%root则 localhost改为%
ALTER USER 'root'@'%' IDENTIFIED BY 'Yanxiangpei.123';

# 修改root账号远程访问权限
use mysql
select host,user from user;

#如果存在了root host是%则无需执行
update user set host="%" where user='root';

GRANT ALL ON *.* TO 'root'@'%';
flush privileges;
```

### 2. 数据库远程登陆

这里可以自行使用数据库远程登陆软件，例如navicat等

