package wang.dreamland.www.service.impl;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import wang.dreamland.www.dao.UserMapper;
import wang.dreamland.www.entity.User;
import wang.dreamland.www.service.UserService;
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;

    @Transactional
    public int regist(User user) {
       /*故意制造异常测试事务回滚操作
       int i= userMapper.insert(user);
       i=i/0;
       return i;*/
    return userMapper.insert(user);
    }

    @Override
    public User login(String name, String password) {
        User user =new User();
        user.setEmail(name);
        user.setPassword(password);
        return userMapper.selectOne(user);
        //return userMapper.findUserByNameAndPwd( name,password );
    }

    @Override
    public User findByEmail(String email) {
        User user =new User();
        user.setEmail(email);
        return userMapper.selectOne(user);
        // return userMapper.findByEmail(email);
    }

    @Override
    public User findByPhone(String phone) {
        User user=new User();
        user.setPhone(phone);
        return userMapper.selectOne(user);
    }

    @Override
    public User findById(Long id) {
        User  user =new User();
        user.setId(id);
        return userMapper.selectOne(user);
    }
    public User findByEmailActive(String email){
        User user=new User();
        user.setEmail(email);
        return userMapper.selectOne(user);
//        return userMapper.findByEmail(email);
    }
    public User findById(String id){
        User user=new User();
        Long uid=Long.parseLong(id);
        user.setId(uid);
        return userMapper.selectOne(user);
    }
    public User findById(long id){
        User user=new User();
        user.setId(id);
        return userMapper.selectOne(user);
    }
    @Transactional
    public void deleteByEmail(String email) {
        User user=new User();
        user.setEmail(email);
        userMapper.delete(user);
    }
   @Transactional
    public void deleteByEmailAndFalse(String email){
        User user=new User();
        user.setEmail(email);
        userMapper.delete(user);
    }
    @Transactional
    public void update(User user) {
        userMapper.updateByPrimaryKeySelective(user);
    }
}
