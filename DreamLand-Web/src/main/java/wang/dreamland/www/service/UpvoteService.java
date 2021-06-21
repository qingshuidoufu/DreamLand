package wang.dreamland.www.service;

import org.springframework.stereotype.Service;

import wang.dreamland.www.entity.Upvote;
public interface UpvoteService {
//    根据用户id和文章id查询
    Upvote findByUidAndConId(Upvote upvote);
//    添加upvote
    int add(Upvote upvote);
//    根据用户id查询最后一次登录的Upvote
    Upvote getByUid(Upvote upvote);
//    更新upvote
    void update(Upvote upvote);
    // 删除点赞
    void deleteByContentId(Long cid);
}
