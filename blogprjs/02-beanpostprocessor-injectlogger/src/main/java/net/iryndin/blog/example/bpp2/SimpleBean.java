package net.iryndin.blog.example.bpp2;

import org.slf4j.Logger;
import org.springframework.stereotype.Component;

@Component
public class SimpleBean {

    @InjectLog
    private Logger log;

    public void doSmth() {
        log.info("doSmth is called");
    }
}