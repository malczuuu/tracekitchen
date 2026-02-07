package io.github.malczuuu.tracekitchen.spring.aop.autoconfigure;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@AutoConfiguration
@EnableConfigurationProperties(TracekitchenAspectProperties.class)
public final class TracekitchenAspectAutoConfiguration {}
