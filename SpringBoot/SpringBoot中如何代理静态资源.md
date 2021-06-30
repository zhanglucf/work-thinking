# 研究SpringBoot对静态资源的处理

> SpringBoot版本：2.1.13.RELEASE

:dog2:

```java
@Configuration
public class ResourceConfiguration implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/files/**")
                .addResourceLocations("file:/data/price-review/static-resource/");
    }
}

```

