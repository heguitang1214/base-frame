package com.tang.counter.common;

import com.tang.counter.dto.RequestInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 数据模拟
 *
 * @author tang
 */
public class Storage {

    private static Map<String, List<RequestInfo>> requestInfoMap = new HashMap<>();
    
    public static void add(RequestInfo requestInfo) {
        List<RequestInfo> requestInfos = requestInfoMap.get(requestInfo.getApiName());
        if (requestInfos == null) {
            requestInfos = new ArrayList<>();
        }
        requestInfos.add(requestInfo);

        requestInfoMap.put(requestInfo.getApiName(), requestInfos);
    }

    public static Map<String, List<RequestInfo>> findAll() {
        return requestInfoMap;
    }

}
