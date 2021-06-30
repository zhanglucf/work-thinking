> 思考:IDEA 是如何部署WEB项目到tomcat的？

[相关博客](https://blog.csdn.net/qq_22627687/article/details/76555886)

简要概括：

IDEA 会从tomcat的安装目录中复制一些必要的文件到指定目录，`lib`和`bin` 除外，因为`lib`和`bin`这两个文件夹内容是所有tomcat实例共享的，不需要复制。

这个目录默认是：C:\Users\zzh\.IntelliJIdea2018.3\system\tomcat  当然和IDEA的版本也有关系

然后在C:\Users\zzh\.IntelliJIdea2018.3\system\tomcat\Unnamed_springmvc-examples\conf\Catalina\localhost下生成对应的xml文件，这个文件会指定web项目实际的位置。

```xml
<Context path="/springmvc_1" docBase="D:\springmvc-examples\springmvc-1\target\springmvc-1-1.0-SNAPSHOT" />
```

最后tomcat启动用到的配置文件所在目录是：`C:\Users\zzh\.IntelliJIdea2018.3\system\tomcat\Unnamed_springmvc-examples\conf`