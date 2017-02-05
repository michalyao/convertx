package me.yoryor.zabbix4j.service;

import me.yoryor.zabbix4j.common.Config;
import me.yoryor.zabbix4j.transport.ServiceFactory;
import org.junit.Test;

public class AuthServiceTest {
    Config config = new Config();
    @Test
    public void getAuth() throws Exception {
        config.setPackagePath("me.yoryor.zabbix4j.service");
        config.setRpcAddress("http://192.168.116.131/zabbix/api_jsonrpc.php");
        String auth = ServiceFactory.getInstance(config).getZabbixService(AuthService.class).getAuth("Admin", "zabbix");
        System.out.println(auth);
    }

}