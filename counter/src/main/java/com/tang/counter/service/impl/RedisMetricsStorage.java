package com.tang.counter.service.impl;

import com.tang.counter.dto.RequestInfo;
import com.tang.counter.service.MetricsStorage;

import java.util.List;
import java.util.Map;

public class RedisMetricsStorage implements MetricsStorage {
    //... 省略属性和构造函数等...
    @Override
    public void saveRequestInfo(RequestInfo requestInfo) {
        //...
    }

    @Override
    public List<RequestInfo> getRequestInfosByDuration(String apiName, long startTimestamp, long endTimestamp) {
        //...
        return null;
    }

    @Override
    public Map<String, List<RequestInfo>> getAllRequestInfosByDuration(long startTimestamp, long endTimestamp) {
        //...
        return null;
    }
}