package wang.dreamland.www.dao;

import tk.mybatis.mapper.common.Mapper;
import wang.dreamland.www.entity.User;
import wang.dreamland.www.entity.UserContent;

import java.util.List;

public interface UserContentMapper  extends Mapper<UserContent> {
    /**
     * 根据用户id查询出梦分类
     * @param uid
     * @return
     */
    List<UserContent> findCategoryByUid(Long uid);
}
