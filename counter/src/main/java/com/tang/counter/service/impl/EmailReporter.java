package com.tang.counter.service.impl;

import com.tang.counter.dto.RequestInfo;
import com.tang.counter.dto.RequestStat;
import com.tang.counter.service.MetricsStorage;
import com.tang.counter.service.ScheduledReporter;
import com.tang.counter.service.StatViewer;

import java.util.*;

/**
 * 邮件显示
 *
 * @author tang
 */
public class EmailReporter extends ScheduledReporter {

    private static final Long DAY_HOURS_IN_SECONDS = 86400L;

    /**
     * 兼顾灵活性和代码的可测试性，这个构造函数继续保留
     *
     * @param metricsStorage 数据存储
     * @param aggregator     数据统计
     * @param viewer         数据显示
     */
    public EmailReporter(MetricsStorage metricsStorage, Aggregator aggregator, StatViewer viewer) {
        super(metricsStorage, aggregator, viewer);
    }

    /**
     * 兼顾代码的易用性，新增一个封装了默认依赖的构造函数
     *
     * @param emailToAddresses email地址
     */
    public EmailReporter(List<String> emailToAddresses) {
        this(new RedisMetricsStorage(), new Aggregator(), new EmailViewer(emailToAddresses));
    }


    /**
     * 邮件发送的定时任务
     */
    public void startDailyReport() {
        // new Date()可以获取当前时间
        Date firstTime = trimTimeFieldsToZeroOfNextDay(new Date());

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                long durationInMillis = DAY_HOURS_IN_SECONDS * 1000;
                long endTimeInMillis = System.currentTimeMillis();
                long startTimeInMillis = endTimeInMillis - durationInMillis;
                doStatAndReport(startTimeInMillis, endTimeInMillis);
            }
        }, firstTime, DAY_HOURS_IN_SECONDS * 1000);
    }

    // 设置成protected而非private是为了方便写单元测试
//    @VisibleForTesting
    protected Date trimTimeFieldsToZeroOfNextDay(Date date) {
        // 这里可以获取当前时间
        Calendar calendar = Calendar.getInstance();
        // 重新设置时间
        calendar.setTime(date);
        calendar.add(Calendar.DATE, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }
}