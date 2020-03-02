package com.tang.counter.dto;

/**
 * 数据采集信息
 *
 * @author tang
 */
public class RequestInfo {

    /**
     * 接口名称
     */
    private String apiName;

    /**
     * 接口请求的响应时间
     */
    private double responseTime;

    /**
     * 接口请求时间
     */
    private long timestamp;

    public RequestInfo() {
    }

    public RequestInfo(String apiName, double responseTime, long timestamp) {
        this.apiName = apiName;
        this.responseTime = responseTime;
        this.timestamp = timestamp;
    }

    public String getApiName() {
        return apiName;
    }

    public void setApiName(String apiName) {
        this.apiName = apiName;
    }

    public double getResponseTime() {
        return responseTime;
    }

    public void setResponseTime(double responseTime) {
        this.responseTime = responseTime;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}