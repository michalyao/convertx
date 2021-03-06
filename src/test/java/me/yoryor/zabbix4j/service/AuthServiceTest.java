package me.yoryor.zabbix4j.service;

import me.yoryor.zabbix4j.common.Config;
import me.yoryor.zabbix4j.transport.ServiceFactory;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class AuthServiceTest {
    Config config = new Config();
    @Test
    public void getAuth() throws Exception {
        config.setPackagePath("me.yoryor.zabbix4j.service");
        config.setRpcAddress("http://192.168.116.131/zabbix/api_jsonrpc.php");
        config.setUser("Admin");
        config.setPassword("zabbix");
        ArrayList<String> hostids = new ArrayList<>();
        hostids.add("10105");
        List<Object> maps = ServiceFactory.getInstance(config)
                .getZabbixService(ApplicationService.class).listApplicationIdsByHostId(hostids);
        System.out.println(maps);
    }

}