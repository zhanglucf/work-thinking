[MYSQL YUM安装官方文档](https://dev.mysql.com/doc/mysql-yum-repo-quick-guide/en/)

查看Linux内核版本命令

```shell
[root@VM-0-16-centos mysql]# uname -a
Linux VM-0-16-centos 3.10.0-1127.19.1.el7.x86_64 #1 SMP Tue Aug 25 17:23:54 UTC 2020 x86_64 x86_64 x86_64 GNU/Linux
[root@VM-0-16-centos mysql]#

这里的el7决定我们后面下载
```

为什么使用yum安装而不使用rpm

	rpm是由红帽开发的软件包管理方式，使用rpm可以很方便的帮我们安装、更新、删除相应的软件，但是rpm包在安装的时候，
	存在一定的包依赖问题，在安装一个软件的时候，要对这个软件包含的所有分支rpm都要进行安装，一个安装不成功，就会导致后续软件的运行失败。
	
	yum是一个shell软件包管理系统，基于rpm包管理程序，可以自动的解决包依赖问题，可以自动的从本地或者网络下载软件所依赖的数据包。
	
	相比较而言，yum安装相对rpm安装，更加的人性化，更能满足我们开发设备和软件的目的(在一定时间内高效的完成工作)。
	
	最新的包管理系统是DNF，现在适用于redhat8和centos7，最先出现在Fedora 18这个发行版中。它克服了一些yum包管理的问题，包括用户体验、运行速度提高等。
[[Linux软件安装中RPM与YUM 区别和联系](https://www.cnblogs.com/LiuChunfu/p/8052890.html)]



# 第一步：linux卸载mysql（完全卸载）



先停掉mysql进程  没有安装过的可以直接跳过

```shell
netstat -antp
pkill -9 mysqld

//rpm包安装方式卸载
查包名：rpm -qa|grep -i mysql
删除命令：rpm -e –nodeps 包名
 
//yum安装方式下载
1.查看已安装的mysql
命令：rpm -qa | grep -i mysql
2.卸载mysql
命令：yum remove mysql-community-server-5.6.36-2.el7.x86_64
查看mysql的其它依赖：rpm -qa | grep -i mysql
 
//卸载依赖
yum remove mysql-libs
yum remove mysql-server
yum remove perl-DBD-MySQL
yum remove mysql
```



# 第二步：卸载mariadb

centos7默认安装的是mariadb，需要先卸载mariadb，先查看是否安装mariadb

```shell
rpm -qa | grep mariadb
```

如果找到，则拷贝结果，使用下面命令删除，如删除mariadb-libs-5.5.35-3.el7.x86_64

```shell
rpm -e --nodeps mariadb-libs-5.5.35-3.el7.x86_64
```



# 第三步：下载 MySQL Yum Repository

这里首先要查看下当前linux的内核版本

查看Linux内核版本命令

```shell
[root@VM-0-16-centos mysql]# uname -a
Linux VM-0-16-centos 3.10.0-1127.19.1.el7.x86_64 #1 SMP Tue Aug 25 17:23:54 UTC 2020 x86_64 x86_64 x86_64 GNU/Linux
[root@VM-0-16-centos mysql]#
这里的el7决定我们后面下载
```

[MySQL Yum Repository](https://dev.mysql.com/downloads/repo/yum/)

![Snipaste_2021-02-19_10-56-49](E:\sublime\Snipaste_2021-02-19_10-56-49.png)



![image-20210219105845501](E:\sublime\image-20210219105845501.png)





# 第四步：选择mysql的版本



在MySQL Yum存储库（https://repo.mysql.com/yum/）中，不同版本的MySQL Community Server托管在不同的子存储库中。默认情况下，默认启用最新GA系列（当前为MySQL 8.0）的子存储库，而所有其他系列（例如，MySQL 5.7系列）的子存储库均被禁用。

```shell
查看mysql所有版本的命令
shell> yum repolist all | grep mysql
```

指定mysql的版本，例如，要安装MySQL 5.7

- [x] 方式一：通过 yum-config-manager 命令指定

```shell
shell> sudo yum-config-manager --disable mysql80-community
shell> sudo yum-config-manager --enable mysql57-community
```

- [x] 方式二：还可以通过手动编辑`/etc/yum.repos.d/mysql-community.repo` 指定mysql版本

```sh
[mysql80-community]
name=MySQL 8.0 Community Server
baseurl=http://repo.mysql.com/yum/mysql-8.0-community/el/6/$basearch/
enabled=1
gpgcheck=1
gpgkey=file:///etc/pki/rpm-gpg/RPM-GPG-KEY-mysql

# Enable to use MySQL 5.7
[mysql57-community]
name=MySQL 5.7 Community Server
baseurl=http://repo.mysql.com/yum/mysql-5.7-community/el/6/$basearch/
enabled=1
gpgcheck=1
gpgkey=file:///etc/pki/rpm-gpg/RPM-GPG-KEY-mysql
```

通过运行以下命令输出来验证来检查mysql版本指定是否生效

```sh
shell> yum repolist enabled | grep mysql
```



# 第五步：安装mysql



```sh
shell> sudo yum install mysql-community-server
```



# 第六步：启动MySQL服务器



```sh
shell> systemctl start mysqld
```

启动可能会报错：

首先作为常识，我们要知道mysql 启动日志默认在/var/log/mysqld.log

如果目录/var/lib/mysql/下被以前初始化过则会保如下异常

```sh
cat mysqld.log

2021-02-19T02:14:02.559455Z 0 [Note] Server socket created on IP: '::'.
2021-02-19T02:14:02.559650Z 0 [Note] InnoDB: Loading buffer pool(s) from /var/lib/mysql/ib_buffer_pool
2021-02-19T02:14:02.559725Z 0 [Note] InnoDB: Buffer pool(s) load completed at 210219 10:14:02
2021-02-19T02:14:02.567142Z 0 [Warning] Failed to open optimizer cost constant tables
2021-02-19T02:14:02.567234Z 0 [ERROR] Fatal error: Can't open and lock privilege tables: Table 'mysql.user' doesn't exist
2021-02-19T02:14:02.567244Z 0 [ERROR] Fatal error: Failed to initialize ACL/grant/time zones structures or failed to remove temporary table files.
2021-02-19T02:14:02.567273Z 0 [ERROR] Aborting
```

如何解决：

```sh
rm -rf /var/lib/mysql
```

insert into user  values(1,'张三风')；

查看MySQL服务器的状态：

```sh
shell> systemctl status mysqld
```

如果启用了systemd操作系统，则应使用标准的 **systemctl**（或 带有相反参数的**服务**）命令（例如**stop**， **start**，**status**和 **restart）**来管理MySQL服务器服务。该`mysqld`服务默认情况下处于启用状态，并在系统重新启动时启动。有关其他信息，请参见 [使用systemd管理MySQL服务器](https://dev.mysql.com/doc/refman/8.0/en/using-systemd.html)。

# 第七步：首次登录与roo用户密码重置

设置超级用户的密码并将其存储在错误日志文件中。要显示它，请使用以下命令：

```sh
shell> sudo grep 'temporary password' /var/log/mysqld.log
```

通过使用生成的临时密码登录并尽快更改超级用户帐户的root密码，以更改root密码：

```sh
shell> mysql -uroot -p
```

```sh
shell> ALTER USER 'root'@'localhost' IDENTIFIED BY 'zL@123456';
ALTER USER 'root'@'%' IDENTIFIED BY '333';
grant all privileges on *.* to testuser@"%" identified by "123456"
```

密码要求:至少包含一个大写字母，一个小写字母，一位数字和一个特殊字符，并且密码总长度至少为8个字符。