package org.tiger.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.tiger.test.service.StarApp;

/**
 * Created by tiger on 16-4-11.
 */

@EnableAutoConfiguration
@SpringBootApplication
public class Application implements CommandLineRunner {

    @Autowired
    private StarApp starApp;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Override
    public void run(String... strings) throws Exception {
        starApp.run();
    }
}
