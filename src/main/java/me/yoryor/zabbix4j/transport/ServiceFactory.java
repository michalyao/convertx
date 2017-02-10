package me.yoryor.zabbix4j.transport;

import me.yoryor.zabbix4j.annotation.ZabbixApi;
import me.yoryor.zabbix4j.common.Config;
import me.yoryor.zabbix4j.common.ZabbixApiException;
import org.reflections.Reflections;

import java.lang.reflect.Proxy;
import java.util.Set;

public class ServiceFactory {
    private static ServiceFactory ourInstance;
    private Object serviceProxy;
    private Set<Class<?>> zabbixApis;
    private HttpInvoker httpInvoker;
    private static Config config;

    public static ServiceFactory getInstance() {
        if (ourInstance == null) {
            synchronized (ServiceFactory.class) {
                if (ourInstance == null) {
                    ourInstance = new ServiceFactory(config);
                }
            }
        }
        return ourInstance;
    }

    public static ServiceFactory getInstance(Config config) {
        if (config == null) {
            config = new Config();
        }
        if (ourInstance == null) {
            ourInstance = new ServiceFactory(config);
            return ourInstance;
        }
        return ourInstance;
    }


    public <T> T getZabbixService(Class<T> clazz) {
        if (zabbixApis.contains(clazz)) {
            return (T) this.serviceProxy;
        } else {
            throw new ZabbixApiException("未实现的api");
        }
    }

    private ServiceFactory(Config config) {
        this.zabbixApis = scan(config.getPackagePath());
        this.httpInvoker = new DefaultHttpInvoker(config);
        this.serviceProxy = Proxy.newProxyInstance(ServiceFactory.class.getClassLoader(),
                zabbixApis.toArray(new Class[this.zabbixApis.size()]), new ZabbixServiceProxy(zabbixApis, httpInvoker, config));
    }

    private Set<Class<?>> scan(String packagePath) {
        Reflections reflections = new Reflections(packagePath);
        Set<Class<?>> classes = reflections.getTypesAnnotatedWith(ZabbixApi.class);
        return classes;
    }
}
