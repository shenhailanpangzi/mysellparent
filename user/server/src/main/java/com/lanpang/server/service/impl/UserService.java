package com.lanpang.server.service.impl;


import com.lanpang.server.dao.UserDao;
import com.lanpang.server.dataobject.UserInfo;
import com.lanpang.server.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService implements IUserService {

    @Autowired
    private UserDao userDao;

    @Override
    public UserInfo findUserByOpenid(String uid) {
        return userDao.findByOpenid(uid);
    }
}
