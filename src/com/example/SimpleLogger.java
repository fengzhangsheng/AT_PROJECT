package com.example;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 简易日志工具类，输出到 catalina.out (System.out)
 * 支持带时间戳、IP、操作的格式化日志
 */
public class SimpleLogger {

    private static final DateTimeFormatter DT_FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private static String now() {
        return LocalDateTime.now().format(DT_FMT);
    }

    /**
     * 基础 INFO 日志
     */
    public static void info(String message) {
        System.out.println("[" + now() + "] [INFO] " + message);
    }

    /**
     * 带标签的 INFO 日志
     */
    public static void info(String tag, String message) {
        System.out.println("[" + now() + "] [" + tag + "] " + message);
    }

    /**
     * 基础 ERROR 日志
     */
    public static void error(String message) {
        System.err.println("[" + now() + "] [ERROR] " + message);
    }

    /**
     * 带标签的 ERROR 日志
     */
    public static void error(String tag, String message) {
        System.err.println("[" + now() + "] [" + tag + "] [ERROR] " + message);
    }

    /**
     * 带异常堆栈的 ERROR 日志
     */
    public static void error(String message, Throwable t) {
        System.err.println("[" + now() + "] [ERROR] " + message);
        if (t != null) {
            t.printStackTrace(System.err);
        }
    }

    /**
     * SQL 执行日志（带 IP 和操作上下文）
     */
    public static void sql(String ip, String action, String sql) {
        System.out.println("[" + now() + "] [IP: " + ip + "] [操作: " + action + "] 📝 执行SQL: " + sql);
    }

    /**
     * SQL 执行日志（带参数）
     */
    public static void sql(String ip, String action, String sql, String params) {
        System.out.println("[" + now() + "] [IP: " + ip + "] [操作: " + action + "] 📝 执行SQL: " + sql);
        if (params != null && !params.isEmpty()) {
            System.out.println("[" + now() + "] [IP: " + ip + "] [操作: " + action + "]    参数: " + params);
        }
    }

    /**
     * 操作日志（带 IP 和操作上下文）
     */
    public static void action(String ip, String action, String message) {
        System.out.println("[" + now() + "] [IP: " + ip + "] [操作: " + action + "] " + message);
    }

    /**
     * 警告日志
     */
    public static void warn(String message) {
        System.out.println("[" + now() + "] [WARN] " + message);
    }

    /**
     * 警告日志（带标签）
     */
    public static void warn(String tag, String message) {
        System.out.println("[" + now() + "] [" + tag + "] [WARN] " + message);
    }
}
