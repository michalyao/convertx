package me.yoryor.zabbix4j.transport;

import lombok.Data;

import java.util.List;

/**
 * 用于构建JSON PRC的payload
 */
@Data
public class RpcAttribute {
    private String auth;
    private List<Param> params;
    private ReturnClass returnClass;
    private String method;

    public static class ReturnClass {
        private Class<?> clazz;
        private boolean isList;

        public Class<?> getClazz() {
            return clazz;
        }

        public void setClazz(Class<?> clazz) {
            this.clazz = clazz;
        }

        public boolean isList() {
            return isList;
        }

        public void setList(boolean list) {
            isList = list;
        }
    }
}
