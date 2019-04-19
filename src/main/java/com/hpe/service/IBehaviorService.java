package com.hpe.service;

import com.hpe.entity.Behavior;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hpe.utils.datasource.DataSource;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zxj
 * @since 2019-04-17
 */
public interface IBehaviorService extends IService<Behavior> {

    /**
     * 对用户的行为进行记录
     * @param behavior
     * @return
     * @throws RuntimeException
     */
    @DataSource("dataSource1")
    int insertBehavior(Behavior behavior) throws RuntimeException;

    /**
     * 获取到所有用户操作记录
     * @return
     */
    @DataSource("dataSource1")
    List<Behavior> selectAllBehavior(String user) throws RuntimeException;

    /**
     * 对某用户做统计
     * @param user
     * @param type
     * @return
     */
    @DataSource("dataSource1")
    Map getEcharts(String user, String type) throws RuntimeException;


    /**
     * 对某用户做统计
     * @param user
     * @param type
     * @return
     */
    @DataSource("dataSource1")
    Map getEchartsNew(String user,String type,String sTime,String eTime) throws RuntimeException;
}
