package io.github.malczuuu.tracekit.common.autoconfigure;

import io.github.malczuuu.tracekit.boot4.TracingTaskDecorator;
import java.util.concurrent.ThreadPoolExecutor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

@Configuration(proxyBeanMethods = false)
final class TaskExecutorConfiguration {

  @Bean
  public TaskScheduler taskScheduler() {
    ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
    scheduler.setPoolSize(10);
    scheduler.setThreadNamePrefix("Sched-");
    scheduler.initialize();
    return scheduler;
  }

  @Bean(name = "queueTaskExecutor")
  public TaskExecutor queueTaskExecutor(TracingTaskDecorator tracingTaskDecorator) {
    ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
    executor.setThreadNamePrefix("QueueExec-");
    executor.setCorePoolSize(1);
    executor.setMaxPoolSize(1);
    executor.setQueueCapacity(1000);
    executor.setRejectedExecutionHandler(new ThreadPoolExecutor.AbortPolicy());
    executor.setTaskDecorator(tracingTaskDecorator);
    executor.initialize();
    return executor;
  }
}
