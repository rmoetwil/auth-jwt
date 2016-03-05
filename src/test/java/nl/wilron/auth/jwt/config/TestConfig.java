package nl.wilron.auth.jwt.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

/**
 * Test Config to make Spring Context loading work.
 *
 * @author Ronald Moetwil
 */
@Configuration
@PropertySource("classpath:application.properties")
public class TestConfig {
    /**
     * This bean is needed to support @Value annotations.
     *
     * @return PropertySourcesPlaceholderConfigurer bean
     */
    @Bean
    public PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }
}
