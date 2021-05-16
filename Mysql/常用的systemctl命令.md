## 常用的systemctl命令

- **以sshd服务为例，列出常用systemctl命令：**

1. 启动sshd服务：`systemctl start ssh.service`
2. 停止sshd服务：`systemctl stop ssh.service`
3. 查看sshd服务状态：`systemctl status ssh.service`
4. 重启sshd服务：`systemctl restart ssh.service`
5. 设置开机自启动：`systemctl enable ssh.service`
6. 禁止开机自启动：`systemctl disable ssh.service`
7. 查看所有已经启动的服务：`systemctl list-units --type=service`
8. 重新加载配置文件：`systemctl daemon-reload`



[systemctl命令说明](https://blog.csdn.net/tl4832194/article/details/109781230)