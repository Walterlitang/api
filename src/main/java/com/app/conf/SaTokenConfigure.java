package com.app.conf;


import cn.dev33.satoken.exception.SaTokenException;
import cn.dev33.satoken.interceptor.SaInterceptor;
import cn.dev33.satoken.stp.StpUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@Slf4j
public class SaTokenConfigure implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new SaInterceptor(handle -> StpUtil.checkLogin()))
                .addPathPatterns("/api/**")
                .excludePathPatterns("/adminUser/captcha")
                .excludePathPatterns("/adminUser/login")
                .excludePathPatterns("/adminUser/register")
                .excludePathPatterns("/articleComment/webByArticleId")
                .excludePathPatterns("/article/page")
                .excludePathPatterns("/article/FocusAndCurrentPolitics")
                .excludePathPatterns("/article/StrongMilitaryInformation")
                .excludePathPatterns("/article/NewDevelopments")
                .excludePathPatterns("/article/categoryCounts")
                .excludePathPatterns("/article/info")
                .excludePathPatterns("/banner/webPage")
                .excludePathPatterns("/friendshipLink/webList")
                .excludePathPatterns("/banner/webList")
                .excludePathPatterns("/mailbox/saveOrUpdate")
                .excludePathPatterns("/sysCategory/getCategoryTreeById")
                .excludePathPatterns("/sysCategory/getNavigationTree")
                .excludePathPatterns("/sysCategory/getArticleTree")
                .excludePathPatterns("/adminUser/getChiefList")
                .excludePathPatterns("/adminUser/getUnitList")
                .excludePathPatterns("/video/webList")
                .excludePathPatterns("/websiteConfig/detail")
                //文件放行
                .excludePathPatterns("/file/**")
                //放行接口对接
                 .excludePathPatterns("/requestLogs/**")
                // 添加 Swagger 3 的常见排除路径
                .excludePathPatterns("/doc.html")
                .excludePathPatterns("/swagger-ui/index.html")
                .excludePathPatterns("/swagger-ui/**")
                .excludePathPatterns("/swagger-ui.html**") // 排除 Swagger UI 的主页面
                .excludePathPatterns("/swagger-resources/**") // 排除 Swagger 资源（API分组信息）
                .excludePathPatterns("/v3/api-docs**") // 排除 OpenAPI 3 规范文档
                .excludePathPatterns("/webjars/**"); // Swagger UI 可能会使用 webjars 来加载前端资源;
    }
}


