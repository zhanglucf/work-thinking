## SpringBoot中如何解决跨域问题

方式有很多，下面只介绍项目中真实用到过的方式，经过检验的。

> SpringBoot的版本：2.1.13.RELEASE

### 第一步：自定义拦截器，在响应头中设置相应属性

```java
package com.xietong.phoenix.userservice.config;

import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 拦截器，解决跨域问题
 */
public class CrosInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type,Content-Length, Authorization, Accept,X-Requested-With");
        response.setHeader("Access-Control-Allow-Methods", "PUT,POST,GET,DELETE,OPTIONS");
        return true;
    }
}
```

### 第二步：注册拦截器

```java
package com.xietong.phoenix.userservice.config;

import com.xietong.phoenix.userservice.support.jwt.AuthenticationInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

    @Bean
    public CrosInterceptor getAreaInterceptor() {
        return new CrosInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(getAreaInterceptor());
    }

}

```

