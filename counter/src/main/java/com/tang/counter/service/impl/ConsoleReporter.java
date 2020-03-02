package com.tang.counter.service.impl;

import com.tang.counter.dto.RequestInfo;
import com.tang.counter.dto.RequestStat;
import com.tang.counter.service.MetricsStorage;
import com.tang.counter.service.ScheduledReporter;
import com.tang.counter.service.StatViewer;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 命令行的记录
 *
 * @author tang
 */
public class ConsoleReporter extends ScheduledReporter {

    private ScheduledExecutorService executor;

    /**
     * 兼顾灵活性和代码的可测试性，这个构造函数继续保留
     */
    public ConsoleReporter(MetricsStorage metricsStorage, Aggregator aggregator, StatViewer viewer) {
        super(metricsStorage, aggregator, viewer);
        this.executor = Executors.newSingleThreadScheduledExecutor();
    }

    /**
     * 兼顾代码的易用性，新增一个封装了默认依赖的构造函数
     */
    public ConsoleReporter() {
        this(new RedisMetricsStorage(), new Aggregator(), new ConsoleViewer());
    }

    /**
     * 第 4 个代码逻辑：定时触发第 1、2、3 代码逻辑的执行；
     *
     * @param periodInSeconds   定时任务周期时间
     * @param durationInSeconds 获取数据的周期时间
     */
    public void startRepeatedReport(long periodInSeconds, long durationInSeconds) {
        executor.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                // 第 1 个代码逻辑：根据给定的时间区间，从数据库中拉取数据；
                long durationInMillis = durationInSeconds * 1000;
                long endTimeInMillis = System.currentTimeMillis();
                long startTimeInMillis = endTimeInMillis - durationInMillis;
                Map<String, List<RequestInfo>> requestInfos =
                        metricsStorage.getAllRequestInfosByDuration(startTimeInMillis, endTimeInMillis);
                // 第 2 个代码逻辑：根据原始数据，计算得到统计数据；
                Map<String, RequestStat> requestStats = aggregator.aggregate(requestInfos, durationInMillis);
                // 第 3 个代码逻辑：将统计数据显示到终端（命令行或邮件）；
                viewer.output(requestStats, startTimeInMillis, endTimeInMillis);
            }
        }, 0L, periodInSeconds, TimeUnit.SECONDS);
    }
}