package net.iryndin.blog.example.bpp3;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.atomic.AtomicLong;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class AnotherPrototypeBean implements DisposableBean {

    public static final AtomicLong counter = new AtomicLong(1000);

    private final long id;

    public AnotherPrototypeBean() {
        this.id = counter.incrementAndGet();
        System.out.println("AnotherPrototypeBean(id="+id+") created");
    }

    @PostConstruct
    private void heavyInit() {
        System.out.println("AnotherPrototypeBean(id="+id+"): expensive init");
    }

    @Override
    public void destroy() throws Exception {
        System.out.println("AnotherPrototypeBean(id="+id+"): destroy");
    }
}