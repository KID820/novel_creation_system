package com.gdut.aigc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@ServletComponentScan
@SpringBootApplication
public class AigcApplication {

    public static void main(String[] args) {
        SpringApplication.run(AigcApplication.class, args);
    }

}
