package nl.wilron.auth.jwt;

import nl.wilron.auth.jwt.config.PresentationConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {


    public static void main(String[] args) {
        org.springframework.context.ApplicationContext context =
                SpringApplication.run(new Object[]{Application.class,
                        PresentationConfig.class}, args);
    }
}