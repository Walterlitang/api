package com.app.interceptor;


import cn.dev33.satoken.stp.StpUtil;
import com.app.exception.BaseException;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;

@Component
public class informationNetworkInterceptor implements HandlerInterceptor {

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }

//    @Override
//    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//        //刷新过期时间
//        String token = request.getHeader("token");
//        //++logger.info(request.getRequestURI());
//        //++logger.info(request.getQueryString());
//
//        List<String> openPaths = Arrays.asList(
//                "/api/adminUser/captcha",
//                "/api/adminUser/login",
//                "/api/adminUser/register",
//                "/api/articleComment/webByArticleId",
//                "/api/article/page",
//                "/api/article/FocusAndCurrentPolitics",
//                "/api/article/StrongMilitaryInformation",
//                "/api/article/NewDevelopments",
//                "/api/article/categoryCounts",
//                "/api/article/info",
//                "/api/banner/webPage",
//                "/api/friendshipLink/webList",
//                "/api/banner/webList",
//                "/api/mailbox/saveOrUpdate",
//                "/api/sysCategory/getCategoryTreeById",
//                "/api/sysCategory/getNavigationTree",
//                "/api/sysCategory/getArticleTree",
//                "/api/adminUser/getChiefList",
//                "/api/adminUser/getUnitList",
//                "/api/video/webList",
//                "/api/websiteConfig/detail",
//                "/api/article/FocusAndCurrentPolitics",
//                "/api/article/StrongMilitaryInformation",
//                "/api/article/NewDevelopments",
//                "/api/article/categoryCounts",
//                "/api/friendshipLink/webList"
//        );
//
//        //物联网设备推送接口放行
//        String uri = request.getRequestURI();
//
//        System.err.println("当前请求uri: " + uri);
//
//        //登录接口，放行
//        if (uri.contains("/login") || uri.contains("getUserCode") || uri.contains("oauth2")) {
//            return true;
//        }
//
//        //文件，放行
//        if (uri.contains("/file/") || uri.contains("/template/")) {
//            return true;
//        }
//
//        if (!StpUtil.isLogin()) {
//            throw new BaseException(405, "用户未登录");
//        }
//
//
//        //PC端纵向越权,以下路径为PC端调用
//        if (openPaths.contains(uri)) {
//            return true;
//        }
//
//        throw new BaseException(20001, "无权限访问");
//    }

}