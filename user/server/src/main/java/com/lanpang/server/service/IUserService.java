package com.lanpang.server.service;


import com.lanpang.server.dataobject.UserInfo;

public interface IUserService {

    public UserInfo findUserByOpenid(String uid);

}
