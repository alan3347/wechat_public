package icu.groot;

import icu.groot.controller.Pusher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * @JavaVersion : java version 1.8+
 * @Author : Alan
 * @ClassName : WechatPushApplication
 * @Version : 1.0.0
 * @Date : 2022/8/25 18:09
 * @Description :
 */
@SpringBootApplication
@EnableScheduling // 开启定时任务
@ConfigurationPropertiesScan("icu.groot.properties")
//@EnableConfigurationProperties(Info.class)
public class WechatPushApplication extends SpringBootServletInitializer {

    @Autowired
    Pusher pusher;

    public static void main(String[] args) {
        SpringApplication.run(WechatPushApplication.class, args);
//        ConfigurableApplicationContext context = SpringApplication.run(WechatPushApplication.class, args);
//        Pusher bean = context.getBean(Pusher.class);
//        bean.show();
//        bean.push();
    }

    // 定时
    @Scheduled(cron = "0 58 8 * * ?")
//    @Scheduled(fixedRate = 1000 * 60)
    public void goodMorning(){
        pusher.push();
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(this.getClass());
    }
}
