



针对mysql5.5.x版本，目录如下表格所示：

| 参数         | 路径                            | 解释                           | 备注                        |
| ------------ | ------------------------------- | ------------------------------ | --------------------------- |
| --datadir    | /var/lib/mysql/                 | mysql 数据库文件的存放路径     |                             |
| --basedir    | /usr/bin                        | 相关命令目录                   | mysqladmin mysqldump 等命令 |
| --plugin-dir | /usr/lib64/mysql/plugin         | mysql 插件存放路径             |                             |
| --log-error  | /var/lib/mysql/jack.atguigu.err | mysql 错误日志路径             |                             |
| --pid-file   | /var/lib/mysql/jack.atguigu.pid | 进程 pid 文件                  |                             |
| --socke      | /var/lib/mysql/mysql.sock       | 本地连接时用的 unix 套接字文件 |                             |
|              | /usr/share/mysql                | 配置文件目录                   |                             |
|              | /etc/init.d/mysql               | 服务启停相关脚本               | mysql 脚本及配置文件        |



mysql 5.7 /usr/share/mysql/目录下没有 没有<font color='orange'>my-huge.cnf</font>,在/etx/目录下有my.cnf，这是mysql的配置文件路径

