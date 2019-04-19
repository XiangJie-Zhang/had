package com.hpe.mapper;

import com.hpe.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author zxj
 * @since 2019-04-10
 */
public interface UserMapper extends BaseMapper<User> {

    User findUserByUserCode(String userCode) throws RuntimeException;

    int addUser(@Param("user") User user) throws RuntimeException;

    int updateUser(@Param("user") User user) throws RuntimeException;
}
