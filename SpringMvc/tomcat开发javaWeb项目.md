1、升级web.xml约束的版本为4.0

![image-20210508091428291](C:\Users\zzh\AppData\Roaming\Typora\typora-user-images\image-20210508091428291.png)



![image-20210508091500108](C:\Users\zzh\AppData\Roaming\Typora\typora-user-images\image-20210508091500108.png)



这里应该是idea的bug,需要修改下web.xml名，具体修改成什么，任意

![image-20210508091719451](C:\Users\zzh\AppData\Roaming\Typora\typora-user-images\image-20210508091719451.png)

如下图所示，版本已经成功修改成4.0

![image-20210508091755809](C:\Users\zzh\AppData\Roaming\Typora\typora-user-images\image-20210508091755809.png)

接着，重命名1web.xml为web.xml.

删除自动生成的index.jsp文件，重新手动创建index.jsp

![image-20210508092134238](C:\Users\zzh\AppData\Roaming\Typora\typora-user-images\image-20210508092134238.png)



配置tomcat

![image-20210508092256158](C:\Users\zzh\AppData\Roaming\Typora\typora-user-images\image-20210508092256158.png)



![image-20210508092329205](C:\Users\zzh\AppData\Roaming\Typora\typora-user-images\image-20210508092329205.png)



到这里，项目已经可以启动，被访问了

![image-20210508092818853](C:\Users\zzh\AppData\Roaming\Typora\typora-user-images\image-20210508092818853.png)

删除pom文件中自动生成的用不到的配置，并引入springmvc相关的依赖

![image-20210508092746461](C:\Users\zzh\AppData\Roaming\Typora\typora-user-images\image-20210508092746461.png)



鼠标定位在pom文件内，Alt+Insert调出下面的小窗口，添加项目依赖

![image-20210508093123169](C:\Users\zzh\AppData\Roaming\Typora\typora-user-images\image-20210508093123169.png)



```xml
<!--servlet-->
<dependency>
  <groupId>javax.servlet</groupId>
  <artifactId>javax.servlet-api</artifactId>
  <version>3.1.0</version>
  <scope>provided</scope>
</dependency>
<!--spring mvc-->
<dependency>
  <groupId>org.springframework</groupId>
  <artifactId>spring-webmvc</artifactId>
  <version>5.2.5.RELEASE</version>
</dependency>
```

配置springmvc.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:context="http://www.springframework.org/schema/context"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd http://www.springframework.org/schema/context https://www.springframework.org/schema/context/spring-context.xsd">
    <!--声明组件扫描器-->
    <context:component-scan base-package="com.example"></context:component-scan>
    <!--jsp文件为什么要放到WEB-INF下面?WEB-INF下的资源是受保护的,不会被随意访问到-->
    <!--配置视图解析器，用于简化Controller层开发-->
    <bean class="org.springframework.web.servlet.view.InternalResourceViewResolver">
        <property name="prefix" value="/WEB-INF/view/"/>
        <property name="suffix" value=".jsp"/>
    </bean>
</beans>
```

配置web.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<web-app xmlns="http://xmlns.jcp.org/xml/ns/javaee"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://xmlns.jcp.org/xml/ns/javaee http://xmlns.jcp.org/xml/ns/javaee/web-app_4_0.xsd"
         version="4.0">

    <servlet>
        <servlet-name>myWeb</servlet-name>
        <servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
        <!--这个时候如果不配置load-on-startup =1 项目是可以正常启动的，但是不会立马创建DispatcherServlet对象-->
        <init-param>
            <param-name>contextConfigLocation</param-name>
            <param-value>classpath:springmvc.xml</param-value>
        </init-param>
        <load-on-startup>1</load-on-startup>
        <!--配置了load-on-startup,还需要指定spingmvc配置文件的位置，否则报下面的异常 springmvc默认到WEB-INF下找servlet-name-servlet.xml-->
        <!--Caused by: java.io.FileNotFoundException: Could not open ServletContext resource [/WEB-INF/myWeb-servlet.xml]-->
    </servlet>
    <servlet-mapping>
        <servlet-name>myWeb</servlet-name>
        <url-pattern>*.do</url-pattern>
    </servlet-mapping>
</web-app>
```

项目结构：

![image-20210508123836406](C:\Users\zzh\AppData\Roaming\Typora\typora-user-images\image-20210508123836406.png)

> 
