package me.yoryor.zabbix4j.service;

import me.yoryor.zabbix4j.common.Config;
import me.yoryor.zabbix4j.transport.Filter;
import me.yoryor.zabbix4j.transport.ServiceFactory;
import org.junit.Test;

import java.util.ArrayList;

public class ApplicationServiceTest {
    @Test
    public void listApplicationIdsByHostId() throws Exception {
        Config config = new Config();
        config.setPackagePath("me.yoryor.zabbix4j.service");
        config.setRpcAddress("http://192.168.116.131/zabbix/api_jsonrpc.php");
        config.setUser("Admin");
        config.setPassword("zabbix");
        ArrayList<String> hostids = new ArrayList<>();
        hostids.add("10105");
        Filter filter = new Filter();
        filter.setKey("host");
        filter.setValue("New Host");
        String maps = ServiceFactory.getInstance(config)
                .getZabbixService(HostService.class).getHostIdByName(filter);
        // 获取id
        System.out.println(maps);
    }

}