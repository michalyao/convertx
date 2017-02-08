package me.yoryor.zabbix4j.transport;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class DefaultHttpInvokerTest {
    private final DefaultHttpInvoker httpInvoker = new DefaultHttpInvoker();
    @Test
    public void execute() throws Exception {
        Map<String, Object> params = new HashMap<>();
        params.put("user", "Admin");
        params.put("password", "zabbix");
        RequestData requestData = RequestData.builder()
                .method("user.login").auth(null).id(1).jsonrpc("2.0").params(params).build();
        JSONObject execute = httpInvoker.execute(requestData);
        System.out.println(JSON.toJSONString(execute));
    }

    @Test
    public void close() throws Exception {

    }

}