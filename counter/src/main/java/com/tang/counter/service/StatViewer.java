package com.tang.counter.service;

import com.tang.counter.dto.RequestStat;

import java.util.Map;

/**
 * 数据显示
 *
 * @author tang
 */
public interface StatViewer {

    void output(Map<String, RequestStat> requestStats, long startTimeInMillis, long endTimeInMills);
    
}