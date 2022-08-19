package com.alorsfaim.iot.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * Configuration the task executor required by Broker producer - consumer
 *
 * @author jwang
 */
@Configuration
public class BrokerConfig {

    @Bean(destroyMethod = "shutdown")
    public ThreadPoolTaskExecutor brokerExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(2);
        executor.setMaxPoolSize(2); // one for producer, another for consumer
        executor.setThreadNamePrefix("SerialBroker-");
        executor.setWaitForTasksToCompleteOnShutdown(false);
        executor.initialize();
        return executor;
    }
}
