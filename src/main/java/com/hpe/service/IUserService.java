package com.hpe.service;

import com.hpe.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hpe.utils.datasource.DataSource;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zxj
 * @since 2019-04-10
 */
public interface IUserService extends IService<User> {

    /**
     * 通过userName寻找用户
     * @param userCode
     * @return
     */
    @DataSource("dataSource2")
    User findUserByUserCode(String userCode);

    /**
     * 添加用户，此时密码是经过md5+salt加密后的
     * @param user
     * @return
     */
    @DataSource("dataSource2")
    int addUser(User user);

    /**
     * 修改用户信息
     * @param user
     * @return
     */
    @DataSource("dataSource2")
    int updateUser(User user);
}
