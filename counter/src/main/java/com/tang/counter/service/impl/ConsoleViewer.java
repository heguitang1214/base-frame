package com.tang.counter.service.impl;

import com.tang.common.utils.JsonUtils;
import com.tang.counter.dto.RequestStat;
import com.tang.counter.service.StatViewer;

import java.util.Map;

/**
 * 命令行视图
 */
public class ConsoleViewer implements StatViewer {

    @Override
    public void output(Map<String, RequestStat> requestStats,
                       long startTimeInMillis, long endTimeInMills) {
        System.out.println("Time Span: [" + startTimeInMillis + ", " + endTimeInMills + "]");
        System.out.println(JsonUtils.toJson(requestStats));
    }
}
