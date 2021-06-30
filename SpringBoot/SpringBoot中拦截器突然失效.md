## 拦截器突然不能正常工作，请求都走不到拦截器

项目中的权限是基于拦截器实现了，某天拦截器突然就不能正常拦截请求了:rage:,经过许久的排查风险是因为最近配置了静态资源代理导致的。

静态资源代理是通过自定义资源映射类实现`WebMvcConfigurationSupport`接口，重写`addResourceHandlers()`方法。

```java

@Configuration
public class WebStaticResourceConfig extends WebMvcConfigurationSupport{

        @Override
        public void addResourceHandlers(ResourceHandlerRegistry registry) {
            registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
            registry.addResourceHandler("/tmp/**").addResourceLocations("file:\\D:\\resource\\");
        }
  
}
```

<u>问题就在这个`WebMvcConfigurationSupport`上，`WebMvcConfigurationSupport`还有一个`注册` `拦截器`的方法需要重写否则，我们自定义的所有拦截器都不生效。</u>

```javaa
   @Override
    protected void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new AuthenticationInterceptor())
                .addPathPatterns("/**");
        super.addInterceptors(registry);
    }
```



项目之前已经通过另一种方式（实现`WebMvcConfigurer`接口，重写`addInterceptors`方法）配置了过滤器，但是因为`WebStaticResourceConfig`这个后来加入的配置类没有重写addInterceptors方法。导致配置的过滤器不能用。

```java
package com.xietong.phoenix.userservice.config;

import com.xietong.phoenix.userservice.support.jwt.AuthenticationInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

    /**
     * 解决跨域问题
     */
    @Bean
    public CrosInterceptor getAreaInterceptor() {
        return new CrosInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(getAreaInterceptor());
        registry.addInterceptor(authenticationInterceptor()).addPathPatterns("/**"); 
    }
		
    /**
     * 权限控制
     */
    @Bean
    public AuthenticationInterceptor authenticationInterceptor() {
        return new AuthenticationInterceptor();
    }

}

```

