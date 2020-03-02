package com.tang.counter.service.impl;

import com.tang.common.utils.JsonUtils;
import com.tang.counter.dto.RequestInfo;
import com.tang.counter.dto.RequestStat;
import com.tang.counter.service.MetricsStorage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 命令行的显示
 *
 * @author tang
 */
public class ConsoleReporter {

    private MetricsStorage metricsStorage;
    private ScheduledExecutorService executor;

    public ConsoleReporter(MetricsStorage metricsStorage) {
        this.metricsStorage = metricsStorage;
        this.executor = Executors.newSingleThreadScheduledExecutor();
    }

    /**
     * 第 4 个代码逻辑：定时触发第 1、2、3 代码逻辑的执行；
     *
     * @param periodInSeconds   定时任务周期时间
     * @param durationInSeconds 获取数据的周期时间
     */
    public void startRepeatedReport(long periodInSeconds, long durationInSeconds) {
        executor.scheduleAtFixedRate(new Runnable() {
            // 定时任务的执行
            @Override
            public void run() {
                // 第 1 个代码逻辑：根据给定的时间区间，从数据库中拉取数据；
                long durationInMillis = durationInSeconds * 1000;
                long endTimeInMillis = System.currentTimeMillis();
                long startTimeInMillis = endTimeInMillis - durationInMillis;
                Map<String, List<RequestInfo>> requestInfos =
                        metricsStorage.getAllRequestInfosByDuration(startTimeInMillis, endTimeInMillis);
                Map<String, RequestStat> stats = new HashMap<>();

                for (Map.Entry<String, List<RequestInfo>> entry : requestInfos.entrySet()) {
                    String apiName = entry.getKey();
                    List<RequestInfo> requestInfosPerApi = entry.getValue();
                    // 第 2 个代码逻辑：根据原始数据，计算得到统计数据；
                    RequestStat requestStat = Aggregator.aggregate(requestInfosPerApi, durationInMillis);
                    stats.put(apiName, requestStat);
                }
                // 第 3 个代码逻辑：将统计数据显示到终端（命令行或邮件）；
                System.out.println("Time Span: [" + startTimeInMillis + ", " + endTimeInMillis + "]");
                System.out.println(JsonUtils.toJson(stats));
            }
        }, 0, periodInSeconds, TimeUnit.SECONDS);
    }
}