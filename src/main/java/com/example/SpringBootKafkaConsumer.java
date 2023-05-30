package com.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
public class SpringBootKafkaConsumer {

    private static Logger logger = LoggerFactory.getLogger(SpringBootKafkaConsumer.class);

    public static void main(String[] args) {
        try {
            ConfigurableApplicationContext ac = SpringApplication.run(SpringBootKafkaConsumer.class, args);
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                logger.info("SIGTERM issued");
                ac.close();
                logger.info("Exit");
            }
            ));
        } catch (Exception e) {
            logger.error("Error Starting spring", e);
        }
    }
}