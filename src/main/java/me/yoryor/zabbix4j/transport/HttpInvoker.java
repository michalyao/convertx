package me.yoryor.zabbix4j.transport;

import com.alibaba.fastjson.JSONObject;

import java.io.Closeable;

public interface HttpInvoker extends Closeable {
    /**
     * 执行JSON-RPC
     * @param requestData
     * @return
     */
    JSONObject execute(RequestData requestData);
}
