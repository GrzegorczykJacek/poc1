package dev.jacek.grzegorczyk;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
public class NotifierApplication {


    public static void main(String[] args) {
        SpringApplication.run(NotifierApplication.class, args);
    }
}
