package wang.dreamland.www.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import wang.dreamland.www.dao.UpvoteMapper;
import wang.dreamland.www.entity.Upvote;
import wang.dreamland.www.service.UpvoteService;
@Service
public class UpvoteServiceImpl implements UpvoteService {
    @Autowired
    private UpvoteMapper upvoteMapper;
    @Override
    public Upvote findByUidAndConId(Upvote upvote) {
        return upvoteMapper.selectOne(upvote);
    }

    @Override
    public int add(Upvote upvote) {
        return upvoteMapper.insert(upvote);
    }

    @Override
    public Upvote getByUid(Upvote upvote) {
        return null;
    }

    @Override
    public void update(Upvote upvote) {
        upvoteMapper.updateByPrimaryKey(upvote);
    }
}
