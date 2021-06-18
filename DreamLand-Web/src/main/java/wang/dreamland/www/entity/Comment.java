package wang.dreamland.www.entity;

import lombok.Data;
import lombok.Getter;
import lombok.ToString;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;
import java.util.Date;
import java.util.List;
@Data //lambok语法糖使用
public class Comment {
    @Id//标识主键
    @GeneratedValue(strategy = GenerationType.IDENTITY) //自增长策略
    //评论表id
    private Long id;
    //评论的文章内容id
    private Long conId;
    //评论者id
    private Long comId;
    //被评论者id
    private Long byId;
    //评论时间
    private Date commTime;
    //子评论id字符串
    private String children;
    //点赞数量
    private Integer upvote;
    //评论的内容
    private String comContent;
    @Transient //不能被序列化, 不能持久化, 不能写进数据库(数据库没有的字段, 通过联表查询出来封装到这里)
    private User user;

    @Transient
    private User byUser;

    @Transient
    private List<Comment> comList;


}