package com.hpe.utils.datasource;

public class DynamicDataSourceHolder {

    public static final String dataSource1 = "dataSource1";

    public static final String dataSource2 = "dataSource2";

    /*
        如果使用静态变量，则会引发并发问题，使用ThreadLocal
    */
    private static final ThreadLocal<String> thread_data_source = new ThreadLocal<String>();

    public static String getDataSource(){
        return thread_data_source.get();
    }

    public static void setDataSource(String dataSource){
        thread_data_source.set(dataSource);
    }

    public static void clearDataSource(){
        thread_data_source.remove();
    }
}
