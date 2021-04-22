package wang.dreamland.www.service;


import wang.dreamland.www.common.PageHelper;
import wang.dreamland.www.entity.Comment;
import wang.dreamland.www.entity.UserContent;

import java.util.List;

public interface UserContentService {
//    查询所有content并分页
    PageHelper.Page<UserContent> findAll(UserContent content, Integer pageNum, Integer pageSize);
    PageHelper.Page<UserContent> findAll(UserContent content, Comment comment, Integer pageNum, Integer pageSize);
    PageHelper.Page<UserContent> findAllByUpvote(UserContent content, Integer pageNum, Integer pageSize);
//    添加文章
    void addContent(UserContent content);
//    根据用户id查询文章集合
    List<UserContent> findByUserId(Long id);
//      查询所有文章
    List<UserContent> findALL();
//  根据文章id查找文章
    UserContent findById(Long id);
//  根据文章id更新文章
    void updateById(UserContent content);
}
