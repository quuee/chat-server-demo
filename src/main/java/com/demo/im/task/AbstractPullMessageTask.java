package com.demo.im.task;


import com.demo.im.netty.IMServersLaunch;
import com.demo.im.util.ThreadPoolExecutorFactory;
import jakarta.annotation.PreDestroy;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;

import java.util.concurrent.ExecutorService;

@Slf4j
public abstract class AbstractPullMessageTask implements CommandLineRunner {

    private static final ExecutorService EXECUTOR_SERVICE = ThreadPoolExecutorFactory.getThreadPoolExecutor();

    @Autowired
    private IMServersLaunch serversLaunch;

    @Override
    public void run(String... args) {
        EXECUTOR_SERVICE.execute(new Runnable() {
            @SneakyThrows
            @Override
            public void run() {
                try {
                    if (serversLaunch.isReady()) {
                        pullMessage();
                    }
                } catch (Exception e) {
                    log.error("任务调度异常", e);
                }
                if (!EXECUTOR_SERVICE.isShutdown()) {
                    Thread.sleep(500);
                    EXECUTOR_SERVICE.execute(this);
                }
            }
        });
    }

    @PreDestroy
    public void destroy() {
        log.info("{}线程任务关闭", this.getClass().getSimpleName());
        EXECUTOR_SERVICE.shutdown();
    }

    public abstract void pullMessage() throws InterruptedException;
}
