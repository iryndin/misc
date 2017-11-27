package net.iryndin.blog.example.bpp1;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Random;

import static java.lang.String.format;

/**
 * Bean PostProcessor that injects random int value for fields marked with annotation <link>@InjectRandomInt</link>
 */
public class InjectRandomIntBeanPostProcessor implements BeanPostProcessor {

    static final Class[] allowedTypes = new Class[]{Integer.class, Long.class, int.class, long.class};

    private Random random = new Random();

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        Field[] fields = bean.getClass().getDeclaredFields();
        for (Field field : fields) {
            InjectRandomInt annotation = field.getAnnotation(InjectRandomInt.class);
            if (annotation != null) {
                if (!isAllowedType(field)) {
                    throw new RuntimeException("Don't put @InjectRandomInt above " + field.getType());
                }
                if (Modifier.isFinal(field.getModifiers())) {
                    throw new RuntimeException("Can't inject to final fields");
                }
                int randomInt = generateRadomIntValue(annotation);
                try {
                    field.setAccessible(true);
                    field.set(bean, randomInt);
                    System.out.println(format("Init bean '%s.%s' with value %d", beanName, field.getName(), randomInt));
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    private int generateRadomIntValue(InjectRandomInt annotation) {
        int min = annotation.min();
        int max = annotation.max();
        int randomInt = min + random.nextInt(max - min);
        return randomInt;
    }

    private boolean isAllowedType(Field field) {
        for (Class c : allowedTypes) {
            if (field.getType().equals(c)) {
                return true;
            }
        }
        return false;
    }
}