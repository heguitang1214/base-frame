package com.tang.counter.service;

import com.tang.counter.dto.RequestInfo;

import java.util.List;
import java.util.Map;

/**
 * MetricsStorage 接口负责原始数据存储
 *
 * @author tang
 */
public interface MetricsStorage {

    void saveRequestInfo(RequestInfo requestInfo);

    List<RequestInfo> getRequestInfosByDuration(String apiName, long startTimestamp,
                                                long endTimestamp);

    Map<String, List<RequestInfo>> getAllRequestInfosByDuration(long startTimestamp,
                                                                long endTimestamp);
}