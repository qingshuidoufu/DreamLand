import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import wang.dreamland.www.entity.Comment;
import wang.dreamland.www.entity.UserContent;
import wang.dreamland.www.service.CommentService;
import wang.dreamland.www.service.UserContentService;
import wang.dreamland.www.service.UserService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

//替换main程序
@RunWith(SpringJUnit4ClassRunner.class)
//必须把spring所有的配置些出来,不然铁出错
@ContextConfiguration(locations = {"classpath:spring-mybatis.xml","classpath:spring-mvc.xml","classpath:applicationContext-redis.xml"})
public class TestDao  {
    @Autowired
    private CommentService commentService;

    @Autowired
   private UserContentService userContentService;

    //测试通过文章id查找所有评论和相关的user
    @Test
    public void testFindAll(){
//        System.out.println("unit test success");
        System.out.println("---start---");
        System.out.println(commentService);
        System.out.println("___mid___");
        List<Comment> list=commentService.findAll(1l);
        System.out.println(list);
        System.out.println("-----end____");
    }
    //测试新增一条评论
    @Test
    public void  testAdd(){
        Comment comment=new Comment();
        comment.setConId(1l);
        comment.setComId(11l);
        comment.setComContent("hello 一级评论");
        int cout=commentService.add(comment);
        System.out.println("影响行数目:"+cout);

    }
    //测试查找所有一级评论
    @Test
    public void testFinaAllFirstComment(){
        List<Comment> comments=commentService.findAllFirstComment(1l);
        System.out.println(comments.toArray());
        for (Comment comment : comments) {
            System.out.println(comment.toString());
        }
    }

    //测试查找所有子评论(由于传两个参数出问题了, 魔改成封装到map中传递向sql查询)
    @Test
    public void testFindAllChildrenComment(){
        Map<String,String> map=new HashMap<>();
        map.put("content_id","12l");
        map.put("children","5,6");
        System.out.println(map);
        List<Comment> comments=commentService.findAllChildrenComment(map);
        for (Comment comment : comments) {
            System.out.println(comment.toString());
        }
    }

    /**
     * 测试根据用户id查询分类
     */
    @Test
    public void testFindCategoryByUid(){
        System.out.println(userContentService.findCategoryByUid(1l));
    }
    /**
     * 测试根据分类实现分页展示
     */
    @Test
    public void testFindCategory(){
        System.out.println(userContentService.findCategoryByUid(1l));
    }


}
