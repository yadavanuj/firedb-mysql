package com.pravisht.firedb.mysql;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import java.util.UUID;
import java.util.function.Function;

public interface PollingTaskExecutor {
    void start();
    void stop();
    String getPollerId();

    static PollingTaskExecutor from(PollerConfig pollerConfig, Runnable task) {
        return new TaskExecutor(pollerConfig, task);
    }

    @Data
    @Builder
    public class PollerConfig {
        @Builder.Default
        @NonNull
        private String pollerId = UUID.randomUUID().toString();
        @Builder.Default
        private boolean enableDither = true;
        @Builder.Default
        private long frequencyInMillis = 500;
        @Builder.Default
        private long minimumBackoffInMillis = 1000;
        @Builder.Default
        private long maximumBackoffInMillis = 5000;
    }

    public class PollerRunnableWrapper implements Runnable {
        private final PollerConfig pollerConfig;
        private final Runnable task;
        private final Function<PollerConfig, Boolean> shouldRunFunc;
        private final Function<PollerConfig, Long> schedulerFunc;

        public PollerRunnableWrapper(PollerConfig pollerConfig,
                                     Runnable task,
                                     Function<PollerConfig, Boolean> shouldRunFunc,
                                     Function<PollerConfig, Long> schedulerFunc) {
            this.pollerConfig = pollerConfig;
            this.task = task;
            this.shouldRunFunc = shouldRunFunc;
            this.schedulerFunc = schedulerFunc;
        }

        @Override
        public void run() {
            while (!shouldRunFunc.apply(pollerConfig)) {
                try {
                    task.run();
                    Thread.sleep(schedulerFunc.apply(pollerConfig));
                } catch (InterruptedException e) {
                    // TODO: Handle Better
                }
            }
        }
    }

    public class TaskExecutor implements PollingTaskExecutor {
        private final PollerConfig config;
        private final Thread poller;
        private static boolean shouldRun;

        public TaskExecutor(PollerConfig config, Runnable task) {
            this(config, new PollerRunnableWrapper(config, task, TaskExecutor::shouldRunSupplier, TaskExecutor::schedulerSupplier));
        }

        TaskExecutor(PollerConfig config, PollerRunnableWrapper pollerRunnableWrapper) {
            this(config, new Thread(pollerRunnableWrapper));
        }

        TaskExecutor(PollerConfig config, Thread poller) {
            this.config = config;
            this.poller = poller;
        }

        @Override
        public void start() {
            poller.start();
        }

        @Override
        public void stop() {
            shouldRun = false;
        }

        @Override
        public String getPollerId() {
            return this.config.getPollerId();
        }

        // TODO: Backoff is not implemented yet
        private static long schedulerSupplier(PollerConfig config) {
            if (!shouldRun) {
                return 2000L;
            }
            return config.getFrequencyInMillis();
        }

        private static boolean shouldRunSupplier(PollerConfig config) {
            return shouldRun;
        }
    }
}
