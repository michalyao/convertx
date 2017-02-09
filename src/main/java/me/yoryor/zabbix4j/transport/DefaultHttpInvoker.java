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
    private static final Logger LOG = LoggerFactory.getLogger(DefaultHttpInvoker.class);
    private final AsyncHttpClient asyncHttpClient;
    private final String baseUrl;

    public DefaultHttpInvoker(Config config) {
        Config.HttpConfig httpConfig = config.getHttpConfig();
        AsyncHttpClientConfig asyncHttpClientConfig = new Builder().setConnectTimeout(httpConfig.getConnectTimeout())
                .setReadTimeout(httpConfig.getReadTimeout())
                .setMaxRequestRetry(httpConfig.getMaxRequestRetry())
                .setRequestTimeout(httpConfig.getRequestTimeout())
                .setAcceptAnyCertificate(true)
                .build();
        this.asyncHttpClient = new AsyncHttpClient(asyncHttpClientConfig);
        this.baseUrl = config.getRpcAddress();
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
            Request request = requestBuilder.setUrl(baseUrl)
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
