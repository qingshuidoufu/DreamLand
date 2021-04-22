package wang.dreamland.www.service;

import wang.dreamland.www.entity.UserInfo;

public interface UserInfoService {
//    根据用户id查找用户详细信息
    UserInfo findByUid(Long id);
//    更新用户信息
    void update(UserInfo userInfo);
//    添加用户详细信息
    void add (UserInfo userInfo);
}
