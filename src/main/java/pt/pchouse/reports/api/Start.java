/*
  Copyright (C) 2022  PChouse - Reflexão Estudos e Sistemas Informáticos, lda

  This program is free software: you can redistribute it and/or modify
  it under the terms of the GNU Affero General Public License as
  published by the Free Software Foundation, either version 3 of the
  License, or (at your option) any later version.

  This program is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  GNU Affero General Public License for more details.

  You should have received a copy of the GNU Affero General Public License
  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package pt.pchouse.reports.api;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;


/**
 * @since 1.0.0
 */
@EnableAsync
@SpringBootApplication
public class Start {

    /**
     * @since 1.0.0
     */
    @Value("${task.executor.core.pool.size}")
    private int taskExecutorCorePoolSize;

    /**
     * @since 1.0.0
     */
    @Value("${task.executor.max.pool.size}")
    private int taskExecutorMaxPoolSize;

    /**
     * @since 1.0.0
     */
    @Value("${task.executor.queue}")
    private int taskExecutorQueue;

    /**
     * @since 1.0.0
     * @param args startup args
     */
    public static void main(String[] args) {
        SpringApplication.run(Start.class, args);
    }

    /**
     * @since 1.0.0
     * @return The default Task Executor
     */
    @Bean
    public Executor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(taskExecutorCorePoolSize);
        executor.setMaxPoolSize(taskExecutorMaxPoolSize);
        executor.setQueueCapacity(taskExecutorQueue);
        executor.setThreadNamePrefix("ReportsAPI-");
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.setAllowCoreThreadTimeOut(true);
        executor.setKeepAliveSeconds(60 * 5);
        executor.initialize();
        return executor;
    }
}
