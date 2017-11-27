package net.iryndin.blog.example.bpp1;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BppConfig {

    @Bean
    public SimpleBean simpleBean() {
        return new SimpleBean();
    }

    @Bean
    public InjectRandomIntBeanPostProcessor injectRandomIntBeanPostProcessor() {
        return new InjectRandomIntBeanPostProcessor();
    }
}