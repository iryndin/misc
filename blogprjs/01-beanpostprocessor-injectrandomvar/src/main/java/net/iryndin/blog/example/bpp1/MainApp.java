package net.iryndin.blog.example.bpp1;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * Inject random integer value into beans with int fields (marked with proper annotation)
 */
public class MainApp {

    public static void main(String[] args) {
        ApplicationContext ctx = new AnnotationConfigApplicationContext(BppConfig.class);
        SimpleBean sb = ctx.getBean(SimpleBean.class);
        System.out.println("SimpleBean.intField=" + sb.getIntField());
    }
}