package com.personalize.diet;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * 个性化食谱助手应用启动类
 * 
 * @author PersonalizeDiet Team
 * @version 1.0.0
 * @since 2024-01-01
 */
@SpringBootApplication
@EnableCaching
@EnableAsync
@EnableScheduling
@EnableTransactionManagement
@MapperScan("com.personalize.diet.mapper")
public class PersonalizeDietApplication {

    public static void main(String[] args) {
        SpringApplication.run(PersonalizeDietApplication.class, args);
        System.out.println("");
        System.out.println("  ____                                 _ _          ____  _      _   ");
        System.out.println(" |  _ \\ ___ _ __ ___  ___  _ __   __ _| (_)_______ |  _ \\(_) ___| |_ ");
        System.out.println(" | |_) / _ \\ '__/ __|/ _ \\| '_ \\ / _` | | |_  / _ \\| | | | |/ _ \\ __|");
        System.out.println(" |  __/  __/ |  \\__ \\ (_) | | | | (_| | | |/ /  __/| |_| | |  __/ |_ ");
        System.out.println(" |_|   \\___|_|  |___/\\___/|_| |_|\\__,_|_|_/___\\___||____/|_|\\___|\\__|");
        System.out.println("");
        System.out.println("个性化食谱助手后端服务启动成功！");
        System.out.println("API文档地址: http://localhost:8080/api/doc.html");
        System.out.println("Druid监控: http://localhost:8080/api/druid/");
        System.out.println("");
    }
}