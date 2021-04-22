package wang.dreamland.www.service;

import wang.dreamland.www.entity.User;

public interface UserService {
    /*用户注册*/
    int regist(User user);
    /*用户登录*/
    User login(String email,String password);
    /*根据用户邮箱查询用户*/
    User findByEmail(String email);
    /*根据手机号查询用户*/
    User findByPhone(String phone);
    /*根据ID查询用户*/
    User findById(Long id);
    /*根基邮箱删除用户*/
    void deleteByEmail(String email);
    /*更新用户信息*/
    void update(User user);
}
