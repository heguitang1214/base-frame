package com.tang.counter.service.impl;

import com.tang.counter.dto.RequestStat;
import com.tang.counter.service.StatViewer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 邮件发送显示
 *
 * @author tang
 */
public class EmailViewer implements StatViewer {

    private EmailSender emailSender;

    private List<String> toAddresses = new ArrayList<>();


    public EmailViewer() {
        this.emailSender = new EmailSender(/*省略参数*/);
    }

    public EmailViewer(EmailSender emailSender) {
        this.emailSender = emailSender;
    }

    public EmailViewer(List<String> emailToAddresses) {

    }

    public void addToAddress(String address) {
        toAddresses.add(address);
    }

    @Override
    public void output(Map<String, RequestStat> requestStats,
                       long startTimeInMillis, long endTimeInMills) {
        // format the requestStats to HTML style.
        // send it to email toAddresses.
    }
}