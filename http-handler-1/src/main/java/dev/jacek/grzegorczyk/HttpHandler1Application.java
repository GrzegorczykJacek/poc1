package dev.jacek.grzegorczyk;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class HttpHandler1Application {
    public static void main(String[] args) {
        SpringApplication.run(HttpHandler1Application.class, args);
    }
}
