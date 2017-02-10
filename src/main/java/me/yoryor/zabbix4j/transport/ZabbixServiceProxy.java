package me.yoryor.zabbix4j.transport;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import me.yoryor.zabbix4j.annotation.MethodName;
import me.yoryor.zabbix4j.annotation.ParamEntry;
import me.yoryor.zabbix4j.common.Config;
import me.yoryor.zabbix4j.common.ZabbixApiException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;
import java.util.concurrent.atomic.LongAdder;

public class ZabbixServiceProxy implements InvocationHandler {
    private static final Logger LOG = LoggerFactory.getLogger(ZabbixServiceProxy.class);
    private static final LongAdder counter = new LongAdder();
    private volatile String auth;
    // rpc methodName -> RpcAttribute.
    private Map<String, RpcAttribute> rpcAttributeMap;
    private HttpInvoker httpInvoker;
    private Config config;

    public ZabbixServiceProxy(Set<Class<?>> zabbixApis, HttpInvoker httpInvoker, Config config) {
        if (zabbixApis.size() == 0) {
            throw new ZabbixApiException("服务初始化失败, api列表为空");
        }
        this.rpcAttributeMap = new HashMap<>();
        this.httpInvoker = httpInvoker;
        this.config = config;
        initApi(zabbixApis);
        initAuth(config.getUser(), config.getPassword());
    }

    private void initAuth(String user, String password) {
        Map<String, Object> params = new HashMap<>();
        params.put("user", user);
        params.put("password", password);
        RequestData requestData = RequestData.builder()
                .auth(null)
                .id(Integer.MAX_VALUE)
                .jsonrpc("2.0")
                .method("user.login")
                .params(params)
                .build();
        JSONObject response = httpInvoker.execute(requestData);
        this.auth = (String) response.get("result");
        Objects.requireNonNull(auth, "获取授权失败，请检查用户名和密码");
    }

    private void initApi(Set<Class<?>> zabbixApis) {
        for (Class clazz : zabbixApis) {
            Method[] methods = clazz.getMethods();
            for (Method method: methods) {
                RpcAttribute rpcAttribute = new RpcAttribute();
                String methodName = method.getAnnotation(MethodName.class).value();
                if (rpcAttributeMap.get(methodName) != null) {
                    throw new ZabbixApiException(String.format("rpc方法%s重复,请检查注释%s", methodName, clazz.getSimpleName()+ "." + method.getName()));
                }
                this.initParams(rpcAttribute, method, clazz);
                this.initReturnClass(rpcAttribute, method, clazz);
                this.rpcAttributeMap.put(methodName, rpcAttribute);
            }
        }
    }

    private void initReturnClass(RpcAttribute rpcAttribute, Method method, Class clazz) {
        RpcAttribute.ReturnClass returnClass = new RpcAttribute.ReturnClass();
        Type genericReturnType = method.getGenericReturnType();
        if(ParameterizedType.class.isAssignableFrom(genericReturnType.getClass())) {
            Type rawType = ((ParameterizedType)genericReturnType).getRawType();
            if(!List.class.equals(rawType)) {
                throw new ZabbixApiException(String.format("初始化失败,API%s中的返回类型定义为泛型,目前仅支持[List<T>]",  clazz.getCanonicalName() + "." + method.getName()));
            }

            returnClass.setList(true);
            Type argumentsType = ((ParameterizedType)genericReturnType).getActualTypeArguments()[0];
            returnClass.setClazz((Class)argumentsType);
        } else {
            returnClass.setList(false);
            returnClass.setClazz((Class)genericReturnType);
        }
        rpcAttribute.setReturnClass(returnClass);
    }

    private void initParams(RpcAttribute rpcAttribute, Method method, Class clazz) {
        String apiName = method.getDeclaringClass().getSimpleName() + "." + method.getName();
        List<Param> params = new ArrayList<>();
        for (int i = 0; i < method.getParameterAnnotations().length; i++) {
            Annotation[] paramAnnotations = method.getParameterAnnotations()[i];
            if (paramAnnotations.length == 0) {
                throw new ZabbixApiException(String.format("%s方法中必须设置@ParamEntry注解", apiName));
            }
            Param param = new Param();
            param.setIndex(i);
            boolean hasParam = false;
            for (Annotation annotation : paramAnnotations) {
                if (annotation instanceof ParamEntry) {
                    ParamEntry paramEntry = (ParamEntry) annotation;
                    String key = paramEntry.paramKey();
                    if (key.isEmpty()) {
                        throw new ZabbixApiException(String.format("%s方法中必须设置@ParamEntry注解的paramKey属性", apiName));
                    }
                    param.setKey(key);
                    hasParam = true;
                    break;
                }
            }
            if (!hasParam) {
                throw new ZabbixApiException(String.format("%s方法中必须设置@ParamEntry注解", apiName));
            }
            params.add(param);
        }
        rpcAttribute.setParams(params);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        String apiName = method.getDeclaringClass().getSimpleName() + "." + method.getName();
        MethodName methodName = method.getAnnotation(MethodName.class);
        if (methodName == null) {
            throw new ZabbixApiException(String.format("api方法%s上缺少@MethodName注解，无法进行解析", apiName));
        }
        String rpcMethod = methodName.value();
        RpcAttribute rpcAttribute = rpcAttributeMap.get(rpcMethod);
        rpcAttribute.setMethod(rpcMethod);
        if (rpcAttribute == null) {
            throw new ZabbixApiException(String.format("方法未正常初始化", apiName));
        }

        int paramNum = rpcAttribute.getParams().size();
        if (paramNum != args.length) {
            throw new ZabbixApiException(String.format("方法未正常初始化,参数个数不匹配", apiName));
        }
        int argIndex = 0;
        Map<String, Object> params = new HashMap<>();
        Object result = null;
        while (true) {
            if (argIndex >= paramNum) {
                RequestData requestData = RequestData.builder()
                        .auth(auth)
                        .id(counter.intValue())
                        .jsonrpc("2.0")
                        .method(rpcMethod)
                        .params(params)
                        .build();
                JSONObject response = httpInvoker.execute(requestData);
                Objects.requireNonNull(response, "Zabbix 返回空值，Zabbix Server 运行可能出现异常!");
                String jsonStr = JSON.toJSONString(response.get("result"));
                // api调用发生异常，处理.
                // TODO: 2017/2/9 Zabbix api 调用异常处理
                if (response.getJSONObject("error") != null) {
                    System.out.println(JSON.toJSONString(response.getJSONObject("error")));
                }

                if (!Void.class.equals(rpcAttribute.getReturnClass().getClazz()) && !Void.TYPE.equals(rpcAttribute.getReturnClass().getClazz())) {
                    if (rpcAttribute.getReturnClass().isList()) {
                        result = JSON.parseArray(jsonStr, rpcAttribute.getReturnClass().getClazz());
                        break;
                    }
                    result = JSON.parseObject(jsonStr, rpcAttribute.getReturnClass().getClazz());
                    break;
                }
                result = null;
                break;
            }
            // 构建 param
            Param param = rpcAttribute.getParams().get(argIndex);
            params.put(param.getKey(), args[argIndex]);
            ++argIndex;
        }
        counter.increment();
        return result;
    }
}
