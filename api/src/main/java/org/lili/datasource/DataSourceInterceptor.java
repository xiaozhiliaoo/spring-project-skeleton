package org.lili.datasource;

import org.aspectj.lang.JoinPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

/**
 * DAO层数据源拦截器
 */
public class DataSourceInterceptor {

    private final static String[] READS_METHOD = {"countByExample", "selectByExample", "selectByPrimaryKey"};

    private static final ThreadLocal<Boolean> masterFlag = new ThreadLocal<Boolean>();

    public void setDataSourceServiceMaster(JoinPoint jp) {
        String dataSource = "dataSource_exchange_master";
        if (!getMasterFlag() && containsValue(READS_METHOD, jp.getSignature().getName())) {
            dataSource = dataSource + "_read";
        } else {
            setMasterFlag(true);
        }
        CustomerContextHolder.setCustomerType(dataSource);
    }

    public void removedataSource(JoinPoint jp) {
        CustomerContextHolder.clearCustomerType();
    }

    private static boolean containsValue(String[] arr, String targetValue) {
        return Arrays.asList(arr).contains(targetValue);
    }


    private static boolean getMasterFlag() {
        return masterFlag.get() == null ? false : masterFlag.get();
    }


    private static void setMasterFlag(boolean flag) {
        masterFlag.set(flag);
    }
}
