package com.tang.counter.controller;


import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.tang.counter.dto.RequestInfo;
import com.tang.counter.service.MetricsStorage;
import com.tang.counter.service.impl.RedisMetricsStorage;
import org.apache.commons.lang3.StringUtils;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * MetricsCollector 类负责提供 API，来采集接口请求的原始数据。
 *
 * @author tang
 */
public class MetricsCollector {

    private static final int DEFAULT_STORAGE_THREAD_POOL_SIZE = 20;

    /**
     * 采集和存储要异步来执行
     */
    private EventBus eventBus;

    /**
     * 基于接口而非实现编程，数据存储接口
     */
    private MetricsStorage metricsStorage;

    /**
     * 兼顾代码的易用性，新增一个封装了默认依赖的构造函数
     */
    public MetricsCollector() {
        this(new RedisMetricsStorage());
    }

    /**
     * 兼顾灵活性和代码的可测试性，这个构造函数继续保留
     * 依赖注入
     *
     * @param metricsStorage 数据存储接口
     */
    public MetricsCollector(MetricsStorage metricsStorage) {
        this(metricsStorage, DEFAULT_STORAGE_THREAD_POOL_SIZE);
    }

    public MetricsCollector(MetricsStorage metricsStorage, int threadNumToSaveData) {
        this.metricsStorage = metricsStorage;
        // TODO: 2020/3/2
        Executor executor = Executors.newFixedThreadPool(threadNumToSaveData);
        this.eventBus = new AsyncEventBus(executor);
//        this.eventBus = new AsyncEventBus(Executors.newFixedThreadPool(threadNumToSaveData));
        this.eventBus.register(new EventListener());
    }

    /**
     * 用一个函数代替了最小原型中的两个函数
     *
     * @param requestInfo 封装接口请求的数据
     */
    public void recordRequest(RequestInfo requestInfo) {
        if (requestInfo == null || StringUtils.isBlank(requestInfo.getApiName())) {
            return;
        }
        eventBus.post(requestInfo);
    }

    public class EventListener {
        @Subscribe
        public void saveRequestInfo(RequestInfo requestInfo) {
            metricsStorage.saveRequestInfo(requestInfo);
        }
    }


}