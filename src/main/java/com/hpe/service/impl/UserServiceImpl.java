package com.hpe.service.impl;

import com.hpe.entity.User;
import com.hpe.mapper.UserMapper;
import com.hpe.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hpe.utils.HDFSUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.UUID;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zxj
 * @since 2019-04-10
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public User findUserByUserCode(String userCode) {
        User user = userMapper.findUserByUserCode(userCode);
        SimpleDateFormat format =  new SimpleDateFormat( " yyyy-MM-dd HH:mm:ss " );
//        if (user!=null && user.getCreateTime()!=null){
//
//        }
        return user;
    }

    @Override
    public int addUser(User user) {
        user.setUserId(UUID.randomUUID().toString());
        user.setRoomSize(HDFSUtils.setSize(1073741824.00));
        user.setRealName("");
        user.setInitLocation("/" + user.getUserName());
        return userMapper.addUser(user);
    }

    @Override
    public int updateUser(User user) {
        user.setInitLocation("/" + user.getUserName());
        return userMapper.updateUser(user);
    }
}
