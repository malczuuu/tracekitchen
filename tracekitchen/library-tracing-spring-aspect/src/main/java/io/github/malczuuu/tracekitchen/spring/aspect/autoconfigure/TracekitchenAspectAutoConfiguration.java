package io.github.malczuuu.tracekitchen.spring.aspect.autoconfigure;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBooleanProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@AutoConfiguration
@ConditionalOnBooleanProperty(name = "tracekitchen.aspect.enabled", matchIfMissing = true)
@EnableConfigurationProperties(TracekitchenAspectProperties.class)
public final class TracekitchenAspectAutoConfiguration {}
