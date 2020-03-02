package com.tang.counter.service.impl;

import com.tang.counter.dto.RequestStat;
import com.tang.counter.service.StatViewer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 邮件发送显示
 *
 * @author tang
 */
public class EmailViewer implements StatViewer {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private EmailSender emailSender;

    private List<String> toAddresses = new ArrayList<>();


    public EmailViewer() {
        // 省略参数
        this.emailSender = new EmailSender();
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
        logger.info("发送邮件数据");
    }
}