package com.example.micrometertags;

import io.micrometer.core.aop.TimedAspect;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tag;
import io.micrometer.core.instrument.Tags;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.HashSet;
import java.util.Set;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MetricsConfig {
    @Bean
    public TimedAspect timedAspect(MeterRegistry meterRegistry) {
        return new TimedAspect(meterRegistry, this::tagFactory);
    }

    private Iterable<Tag> tagFactory(ProceedingJoinPoint pjp) {
        return Tags.of(
            "class", pjp.getStaticPart().getSignature().getDeclaringTypeName(),
            "method", pjp.getStaticPart().getSignature().getName()
        )
        .and(getParameterTags(pjp))
        .and(ExtraTagsPropagation.getTagsAndReset());
    }

    private Iterable<Tag> getParameterTags(ProceedingJoinPoint pjp) {
        Set<Tag> tags = new HashSet<>();

        Method method = ((MethodSignature) pjp.getSignature()).getMethod();
        Parameter[] parameters = method.getParameters();
        for (int i = 0; i < parameters.length; i++) {
            for (Annotation annotation : parameters[i].getAnnotations()) {
                if (annotation instanceof ExtraTag) {
                    ExtraTag extraTag = (ExtraTag) annotation;
                    tags.add(Tag.of(extraTag.value(), String.valueOf(pjp.getArgs()[i])));
                }
            }
        }

        return tags;
    }
}
