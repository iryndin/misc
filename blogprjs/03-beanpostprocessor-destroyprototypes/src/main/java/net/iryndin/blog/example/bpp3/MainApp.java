package net.iryndin.blog.example.bpp3;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * Destroy prototype-scoped beans
 */
public class MainApp {

    public static void main(String[] args) {
        ConfigurableApplicationContext ctx = new AnnotationConfigApplicationContext(BppConfig.class);
        for (int i=0; i<2; i++) {
            SomePrototypeBean b = ctx.getBean(SomePrototypeBean.class);
        }
        for (int i=0; i<3; i++) {
            AnotherPrototypeBean b = ctx.getBean(AnotherPrototypeBean.class);
        }
        ctx.close();
    }
}