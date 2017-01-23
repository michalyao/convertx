package me.yoryor.zabbix4j.common;

/**
 * 所有异常包装成RuntimeException，避免未检查异常带来的琐碎的异常处理
 */
public class ZabbixApiException extends RuntimeException {
    public ZabbixApiException(String message) {
        super(message);
    }

    public ZabbixApiException(String message, Throwable cause) {
        super(message, cause);
    }

    public ZabbixApiException(Throwable cause) {
        super(cause);
    }
}
