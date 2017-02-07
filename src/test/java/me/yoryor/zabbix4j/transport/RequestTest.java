package me.yoryor.zabbix4j.transport;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class RequestTest {
    @Test
    public void testBuilder() {
        Map<String, Object> params = new HashMap<>();
        params.put("filter", new String[]{"New Host"});
        params.put("hostid", "10105");
        RequestData request = RequestData.builder().auth("a").id(1).method("hello").params(params).build();
        System.out.println(request);
    }

}