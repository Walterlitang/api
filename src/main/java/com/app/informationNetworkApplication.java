package com.app;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.util.StringUtils;

import java.net.InetAddress;


@EnableScheduling
@SpringBootApplication
@ServletComponentScan(basePackages = "com.app.conf.SqlInjectFilter")
@MapperScan("com.app.mapper")
public class informationNetworkApplication {

    public static void main(String[] args) throws Exception {
        // 禁用 JMX 自动注册以避免 MBean 名称冲突
        System.setProperty("spring.jmx.enabled", "false");

        ConfigurableApplicationContext application = SpringApplication.run(informationNetworkApplication.class, args);
        Environment env = application.getEnvironment();
        String ip = InetAddress.getLocalHost().getHostAddress();
        String port = env.getProperty("server.port");
        String path = env.getProperty("server.servlet.context-path");
        if (StringUtils.isEmpty(path)) {
            path = "";
        }
        System.err.println("\n\t" + "Local访问地址: \t\thttp://localhost:" + port + path + "\n\t" +
                "External访问地址: \thttp://" + ip + ":" + port + path);
        System.err.println("\n\t" + "swagger3访问路径：http://" + ip + ":" + port + path + "/doc.html#/home");
    }
}

