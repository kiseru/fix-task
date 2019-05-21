package com.kiseru.fix.fixtask;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@ServletComponentScan
@SpringBootApplication
public class FixTaskApplication {

    public static void main(String[] args) {
        SpringApplication.run(FixTaskApplication.class, args);
    }
}
