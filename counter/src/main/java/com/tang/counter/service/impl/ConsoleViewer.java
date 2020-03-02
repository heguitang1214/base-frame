package com.tang.counter.service.impl;

import com.tang.common.utils.JsonUtils;
import com.tang.counter.dto.RequestStat;
import com.tang.counter.service.StatViewer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * 命令行视图
 */
public class ConsoleViewer implements StatViewer {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void output(Map<String, RequestStat> requestStats,
                       long startTimeInMillis, long endTimeInMills) {
        logger.info("命令行打印 Time Span：[{}，{}]", startTimeInMillis, endTimeInMills);
        logger.info(JsonUtils.toJson(requestStats));
    }
}
