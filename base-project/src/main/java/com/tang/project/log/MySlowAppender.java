package com.tang.project.log;

import ch.qos.logback.core.ConsoleAppender;

import java.util.concurrent.TimeUnit;

/**
 * 自定义一个继承自 ConsoleAppender 的 MySlowAppender，
 * 作为记录到控制台的输出器，写入日志时休眠 1 秒。
 */
public class MySlowAppender extends ConsoleAppender {
    @Override
    protected void subAppend(Object event) {
        try {
            // 模拟慢日志
            TimeUnit.MILLISECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        super.subAppend(event);
    }
}