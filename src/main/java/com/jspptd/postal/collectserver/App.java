package com.jspptd.postal.collectserver;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



/**
 * jar need
 *
 */
@RestController
@SpringBootApplication
@EnableCaching
@ServletComponentScan
public class App {

    public static ApplicationContext applicationContext;

    @RequestMapping("/")
    String home() {
        return "this is a home page and the server is ok ,you can go website anywhere";
    }

    public static void main(String[] args) {
        applicationContext = SpringApplication.run(App.class, args);
    }

}
