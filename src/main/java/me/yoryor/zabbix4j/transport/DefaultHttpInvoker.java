package me.yoryor.zabbix4j.transport;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ning.http.client.*;
import me.yoryor.zabbix4j.common.Config;
import me.yoryor.zabbix4j.common.ZabbixApiException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import static com.ning.http.client.AsyncHttpClientConfig.Builder;

public class DefaultHttpInvoker implements HttpInvoker {
    private static final String RPC_ADDR = "http://192.168.116.131/zabbix/api_jsonrpc.php";

    private static final Logger LOG = LoggerFactory.getLogger(DefaultHttpInvoker.class);
    private AsyncHttpClient asyncHttpClient;

    public DefaultHttpInvoker(Config config) {
        AsyncHttpClientConfig asyncHttpClientConfig = new Builder().setConnectTimeout(config.getConnectTimeout())
                .setReadTimeout(config.getReadTimeout())
                .setMaxRequestRetry(config.getMaxRequestRetry())
                .setRequestTimeout(config.getRequestTimeout())
                .setAcceptAnyCertificate(true)
                .build();
        this.asyncHttpClient = new AsyncHttpClient(asyncHttpClientConfig);
    }

    public DefaultHttpInvoker() {
        this.asyncHttpClient = new AsyncHttpClient();
    }

    @Override
    public JSONObject execute(RequestData requestData) {
        return httpRequestJson(requestData);
    }

    @Override
    public void close() throws IOException {
        this.asyncHttpClient.close();
    }

    private JSONObject httpRequestJson(RequestData requestData) {
        RequestBuilder requestBuilder = new RequestBuilder();
        Request request = requestBuilder.setUrl(RPC_ADDR)
                .setHeader("Content-Type", "application/json")
                .setMethod("POST")
                .setBody(JSON.toJSONString(requestData))
                .setBodyEncoding("utf-8")
                .build();
        try {
            Response response = asyncHttpClient.executeRequest(request).get();
            return (JSONObject) JSON.parse(response.getResponseBody("utf-8"));
        } catch (Throwable e) {
            throw new ZabbixApiException(e);
        }
    }
}
