import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import wang.dreamland.www.entity.User;
import wang.dreamland.www.service.UserService;

@ContextConfiguration(locations = {"classpath:spring-mybatis.xml","classpath:spring-mvc.xml"})
public class TestTransaction extends AbstractJUnit4SpringContextTests {
    @Autowired
    private UserService userService;

//    测试插入数据(顺便测试事务回滚)
    @Test
    public void testSave(){
        User user =new User();
        user.setNickName("严旺镇");
        user.setEmail("123546977@qq.com");
        userService.regist(user);
    }
}
