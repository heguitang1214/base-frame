package com.tang.counter;

import com.tang.counter.controller.MetricsCollector;
import com.tang.counter.dto.RequestInfo;
import com.tang.counter.service.impl.ConsoleReporter;
import com.tang.counter.service.impl.EmailReporter;

import java.util.ArrayList;
import java.util.List;

public class PerfCounterV31Test {
    public static void main(String[] args) {
        ConsoleReporter consoleReporter = new ConsoleReporter();
        consoleReporter.startRepeatedReport(60, 60);

        List<String> emailToAddresses = new ArrayList<>();
        emailToAddresses.add("he_guitang@163.com");
        EmailReporter emailReporter = new EmailReporter(emailToAddresses);
        emailReporter.startDailyReport();

        MetricsCollector collector = new MetricsCollector();
        collector.recordRequest(new RequestInfo("register", 123, 10234));
        collector.recordRequest(new RequestInfo("register", 223, 11234));
        collector.recordRequest(new RequestInfo("register", 323, 12334));
        collector.recordRequest(new RequestInfo("login", 23, 12434));
        collector.recordRequest(new RequestInfo("login", 1223, 14234));

        try {
            Thread.sleep(100000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}