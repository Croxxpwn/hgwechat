package com.croxx.hgwechat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
public class HgwechatApplication {

    public static void main(String[] args) {
        SpringApplication.run(HgwechatApplication.class, args);
    }
}
