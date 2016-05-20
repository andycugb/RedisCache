package com.andycugb.storage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by andycugb on 15-5-9.
 */
public class FutureTaskFactory {
    private static final Logger logger = LoggerFactory.getLogger(FutureTaskFactory.class);

    private static final ThreadPoolExecutor executor = new ThreadPoolExecutor(100, 500, 3600,
            TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(500), new ThreadFactory() {
                private AtomicInteger id = new AtomicInteger(0);

                @Override
                public Thread newThread(Runnable r) {
                    Thread thread = new Thread(r);
                    thread.setName("FutureTaskFactory-" + id.addAndGet(1));
                    return thread;
                }
            }, new ThreadPoolExecutor.CallerRunsPolicy());
}
