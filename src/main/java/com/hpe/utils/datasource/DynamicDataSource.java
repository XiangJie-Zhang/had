package com.hpe.utils.datasource;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

public class DynamicDataSource extends AbstractRoutingDataSource {
    /*1.在项目启动时加载配置中的多种数据源，以自定义的名字作为key
    2.继承框架中的AbstractRoutingDataSource类实现提供key的方法，框架源码
    会在每次访问数据库时都会调用这个方法获得数据源的key
    3.在访问数据库前设置key
    */
    @Override
    protected Object determineCurrentLookupKey() {
        // 从自定义位置获取数据源标识
        return DynamicDataSourceHolder.getDataSource();
    }
}
