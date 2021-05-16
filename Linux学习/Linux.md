##  Linux

### **第一章** **Linux 的目录结构**  

#### **1.1 基本介绍**

linux 的文件系统是采用级层式的树状目录结构，在此结构中的最上层是根目录“/”，然后在此目录下再创建其他的目录。 在 Linux 世界里，一切皆文件。

树状目录结构：

![image-20210228211558770](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\image-20210228211558770.png)

#### **1.2 Linux具体目录结构**

- **/lib** 系统开机所需要最基本的动态链接共享库，其作用类似于Windows里的DLL文件。几乎所有的应用程序都需要用到这些共享库。

- **/lost+found** 一般情况下是空的，当系统非法关机后，这里就存放了一些文件。
- <font color='orange'>/etc </font>所有系统管理所需要的配置文件和子目录。my.conf
- <font color='orange'>/usr</font> 用户的很多应用程序和文件都放在这个目录下。类似于Windows下的program.files目录

- <font color='orange'>/bin</font> (**usr/bin、/usr/local/bin**)这个目录存放着经常使用的命令

- **/sbin (usr/sbin、/usr/local/sbin)** 这里存放的是系统管理员使用的系统管理程序

- <font color='orange'>/home</font> 存放普通用户的主目录，在Linux中的每个用户都有一个自己的目录，一般该目录一用户的账号名命名

- <font color='orange'>/root</font>  该目录为系统管理员，超级权限者的用户目录

- **/boot** 存放的启动Linux时使用的一些核心文件，包括一些链接文件和镜像文件

- **/proc** 虚拟目录，是系统内存的映射，访问这个目录来获取系统信息。

- **/srv** service 的缩写，该目录是存放一些服务启动之后需要提取的数据

- **/sys** Linux2.6内核的一个很大变化，该目录安装了2.6内核中新出现的一个文件系统

- **/tmp** 存放临时文件

- **/dev** 类似于 windows的设备管理器,把所有的硬件用文件的形式存储

- <font color='orange'>/media</font> Linux系统会自动识别一些设备,例如U盘、光驱等等,当识别后,Linux 会把识别的设备挂载到这个目录下。
- <font color='orange'>/mnt </font>系统提供该目录是为了让用户临时挂载别的文件系统的,我们可以将外部的存储挂载在/mnt/上,然后进入该目录就可以查看里的内容了。

- **/opt** 这是给主机额外安装软件所摆放的目录。如安装 ORACLE数据库就可放到该目录下。默认为空。
- <font color='orange'>/usr/local</font>  这是另个给主机额外安装软件所安装的目录。一般是通过编译源码方式安装的程序。
- <font color='orange'>/var</font> 这个目录中存放着在不断扩充着的东西,习惯将经常被修改的目录放在这个目录下。包括各种日志文件。

- **/selinux** [security-enhanced linux] Selinux是一种安全子系统,它能控制程序只能访同特定文件。

### 第二章 Linux常用命令

> 命令格式 ：命令 [-选项] [参数] 

说明：

1. 个别命令使用不遵循此格式
2. 当有多个选项时，可以写在一起
3. 简化选项与完整选项-a 等于 --all

#### 2.1 文件或目录处理命令

##### **ls**

>功能描述：显示目录文件

命令英文原意：list

命令所在路径：/bin/ls 

执行权限：所有用户

语法：ls  选项[-ald]  [文件或目录] 

-a    显示所有文件，包括隐藏文件

-l     详细信息显示

-d    查看目录属性

```sh
[root@VM-0-16-centos /]# ls -l
total 72
lrwxrwxrwx.  1 root root     7 Aug  8  2018 bin -> usr/bin
dr-xr-xr-x.  5 root root  4096 Jan 30 13:37 boot
drwxr-xr-x   2 root root  4096 Jan 21  2019 data
drwxr-xr-x  19 root root  2980 Jan 30 13:47 dev
```



#####  **mkdir**

> 命令英文原意：make directories 
>
> 命令所在路径：/bin/mkdir 
>
> 执行权限：所有用户

语法：mkdir -p  [目录名] 

> 功能描述：创建新目录<font color='orange'>-p</font>  递归创建

范例：

```sh
[root@VM-0-16-centos tmp]# mkdir aa/bb/cc
mkdir: cannot create directory ‘aa/bb/cc’: No such file or directory
[root@VM-0-16-centos tmp]# mkdir -p aa/bb/cc
[root@VM-0-16-centos tmp]# ll
total 8
drwxr-xr-x 3 root root 4096 Feb 28 22:00 aa
```

##### **rmdir**

> 功能描述： 删除空目录

命令英文原意：remove empty directories 

命令所在路径：/bin/rmdir 

执行权限：所有用户

语法：rmdir [目录名] 

```sh
[root@VM-0-16-centos tmp]# rmdir aa
rmdir: failed to remove ‘aa’: Directory not empty
[root@VM-0-16-centos tmp]#
drwxr-xr-x 2 root root 4096 Feb 28 22:00 cc
[root@VM-0-16-centos bb]# rmdir cc
[root@VM-0-16-centos bb]#
```

##### **cp**

> 功能描述：**复制文件或目录**

命令英文原意：copy 

命令所在路径：/bin/cp 

执行权限：所有用户

语法：cp  -rp  [原文件或目录] [目标目录] 

<font color='orange'>-r  复制目录</font>

<font color='orange'>-p  保留文件属性</font>

```sh
$ cp  -r /tmp/Japan/cangjing  /root 
$ cp  -rp /tmp/Japan/boduo /tmp/Japan/longze /root 
```

##### **rm** 

> 功能描述：删除文件或目录

命令英文原意：remove 

命令所在路径：/bin/rm 

执行权限：所有用户

语法：rm  -rf   [文件或目录] 

<font color='orange'>-r  删除目录</font>

<font color='orange'>-f  强制执行</font>

```sh
$ rm  /tmp/yum.log 
$ rm -rf  /tmp/Japan/longze 
```

##### touch

> 功能描述：创建空文件

命令所在路径：/bin/touch 

执行权限：所有用户 

语法：touch  [文件名]               

```sh
$ touch Japanlovestory.list
```

##### cat

> 功能描述：显示文件内容  

命令所在路径：/bin/cat 

执行权限：所有用户 

语法：cat [文件名] 

<font color='orange'>-n  显示行号 </font>

```sh
[root@VM-0-16-centos etc]# cat -n my.cnf
     1	# For advice on how to change settings please see
     2	# http://dev.mysql.com/doc/refman/5.7/en/server-configuration-defaults.html
     3	[client]
     4	#客户端设置
     5	port=3306
```

##### more

> 功能描述：分页显示文件内容 

命令所在路径：/bin/more 

执行权限：所有用户 

语法：more  [文件名] 

| <font color='orange'>翻页</font> | 空格或f |
| -------------------------------- | ------- |
| <font color='orange'>换行</font> | Enter   |
| <font color='orange'>退出</font> | q或Q    |



```sh
[root@VM-0-16-centos etc]# more rsyslog.conf
# rsyslog configuration file

# For more information see /usr/share/doc/rsyslog-*/rsyslog_conf.html
# If you experience problems, see http://www.rsyslog.com/doc/troubleshoot.html

#### MODULES ####

# The imjournal module bellow is now used as a message source instead of imuxsock.
$ModLoad imuxsock # provides support for local system logging (e.g. via logger command)
$ModLoad imjournal # provides access to the systemd journal
#$ModLoad imklog # reads kernel messages (the same are read from journald)

```

##### less

> 功能描述：分页显示文件内容（可向上翻页）

命令所在路径：/usr/bin/less 

执行权限：所有用户 

语法：less  [文件名] 

```sh
$  less  /etc/services
```

<font color='red'>tips: 按下 / 后可以搜索 会反显高亮  按q退出（more也可以）</font>

##### head

> 功能描述：显示文件前面几行    

命令所在路径：/usr/bin/head 

执行权限：所有用户 

语法：head  [文件名] 

<font color='orange'>-n 指定行数 </font>

```sh
$ head -n 10 /etc/services
```

##### tail

> 功能描述：显示文件后面几行    

命令所在路径：/usr/bin/tail 

执行权限：所有用户 

语法：tail  [文件名] 

<font color='orange'>-n 指定行数</font>    

<font color='orange'>-f  动态显示文件末尾内容 </font>

```sh
$ tail -n 10 /etc/services
```

#### 2.2 链接命令

##### link 

> 功能描述：生成链接文件

命令英文原意：link 

命令所在路径：/bin/ln 

执行权限：所有用户 

语法：ln  -s  [原文件]  [目标文件]                 

<font color='orange'> -s  创建软链接 </font>

范例：创建文件/etc/issue的软链接/tmp/issue.soft    

```sh
$ ln -s  /etc/issue  /tmp/issue.soft        
```

 创建文件/etc/issue的硬链接/tmp/issue.hard

```sh
$ ln  /etc/issue  /tmp/issue.hard
```

**软链接特征：**

类似Windows快捷方式 

1、lrwxrwxrwx    l 软链接 

软链接文件权限都为rwxrwxrwx 

2、文件大小-只是符号链接 

3、/tmp/issue.soft -> /etc/issue 箭头指向原文件



**硬链接特征：** 

1、拷贝cp -p + 同步更新 

echo "this is a test" >> /etc/motd 

2、可通过i节点识别 

3、不能跨分区

4、不能针对目录使用

#### 2.3 **权限管理命令**

##### chmod

> 功能描述：改变文件或目录权限

命令英文原意：change the permissions mode of a file 

命令所在路径：/bin/chmod 

执行权限：所有用户 

语法：chmod  [{ugoa}{+-=}{rwx}] [文件或目录]                         

  [mode=421 ]  [文件或目录]                          

<font color='orange'> -R  递归修改 </font>

赋予文件testfile所属组写权限      

```sh
$ chmod  g+w  testfile 
```

修改目录testfile及其目录下文件为所有用户具有全部权限

```sh
$ chmod  -R 777  testdir      
```



| 代表字符 | 权限     | 对文件的含义     | 对目录的含义                   |
| -------- | -------- | ---------------- | ------------------------------ |
| r        | 读权限   | 可以查看文件内容 | **可以列出目录中的内容**       |
| w        | 写权限   | 可以修改文件内容 | **可以在目录中创建、删除文件** |
| x        | 执行权限 | 可以执行文件     | **可以进入目录**               |



> rwx 作用在文件或者目录上时意义不同

**1.rwx作用在文件时**

1. r:read，可以读取，查看
2. w:writte, 可以修改，但是不代表可以删除该文件，删除一个文件的前提是对该文件所在的目录（文件夹）具有写的权限，才能删该文件
3. x:execute，可以被执行

**2.rwx作用在目录时**

1. r，ls查看目录内容
2. w, 可以修改，目录内创建+删除+重命名目录
3. x, 可以进入该目录 



##### chown

> 功能描述：改变文件或目录的所有者

命令英文原意：change file ownership

命令所在路径：/bin/chown 

执行权限：所有用户 

语法：`chown`  [用户] [文件或目录]  

 改变文件fengjie的所有者为shenchao

```sh
$ chown  shenchao fengjie
```

##### chgrp

> 功能描述：改变文件或目录的所属组

命令英文原意：change file group ownership 

命令所在路径：/bin/chgrp

执行权限：所有用户 

语法：chgrp  [用户组]  [文件或目录] 

改变文件fengjie的所属组为lampbrother

```sh
$ chgrp lampbrother fengjie      
```

##### umask

> 功能描述：显示、设置文件的缺省权限 

命令英文原意：the user file-creation mask 

命令所在路径：Shell内置命令 

执行权限：所有用户 

语法：umask [-S]             

-S   以rwx形式显示新建文件缺省权限 

```sh
$ umask -S
```

tips: 新建文件是默认没有x权限（比如：防止木马病毒攻击）



### 第三章

> 网卡 与mac地址一一对应

[桥接、NAT、Host-only上网方式的区别](https://www.cnblogs.com/saryli/p/9762771.htm)

| 网络连接类型 | 网卡                 | 和真实机通信 | 局域网内同网段的其他机器通信 | 访问互联网 | 是否占用真实IP |
| ------------ | -------------------- | ------------ | ---------------------------- | ---------- | -------------- |
| 桥接         | 使用真实机的真实网卡 | 可以         | 可以                         | 可以       | 是             |
| Host-Only    | VMnet1这块虚拟网卡   | 可以         | 不可以                       | 不可以     | 否             |
| NAT          | VMnet8这块虚拟网卡   | 可以         | 不可以                       | 可以       | 否             |



