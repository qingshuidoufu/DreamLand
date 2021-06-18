package wang.dreamland.www.dao;

import org.springframework.data.repository.query.Param;
import org.springframework.security.access.method.P;
import tk.mybatis.mapper.common.Mapper;
import wang.dreamland.www.entity.Comment;
import wang.dreamland.www.entity.User;

import java.util.List;
import java.util.Map;

public interface CommentMapper extends Mapper<Comment> {
    //根据文章id查询所有评论
    List<Comment> selectAll(@Param("cid")Long cid);
    //根据文章id查询所有一级评论
    List<Comment> findAllFirstComment(@Param("cid") Long cid);
    //根据文章id和二级评论ids字符串查询出所有二级评论
    List<Comment> findAllChildrenComment(Map<String,String> map);
    //插入评论并返回主键id 返回时是影响行数 id在Comment对象中
    int insertComment(Comment comment);

}
