package rea.ohyeahworld.laundryserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class LaundryServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(LaundryServerApplication.class, args);
    }

}
