package com.hpe.service;

import com.hpe.entity.Resources;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hpe.entity.resourceVo;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zxj
 * @since 2019-04-09
 */
public interface IResourcesService extends IService<Resources> {

    /**
     * 获取此用户下某个类型
     * @param user
     * @param type
     * @return
     * @throws RuntimeException
     */
    Map<String, List<Resources>> getTypeTimeline(String user, String type) throws RuntimeException;


    /**
     * 当用户添加资源时，更新数据库
     * @param resources
     * @return
     * @throws RuntimeException
     */
    int addResource(Resources resources) throws RuntimeException;

    /**
     * 当用户删除某个资源时，更新资源数据库
     * @param path
     * @return
     * @throws RuntimeException
     */
    int delResource(String path) throws RuntimeException;
}
