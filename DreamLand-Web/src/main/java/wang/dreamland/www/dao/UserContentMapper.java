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

    /**
     *  插入文章并返回主键id 返回类型只是影响行数  id在UserContent对象中
     * @param userContent
     * @return
     */
    int inserContent(UserContent userContent);

    /**
     * 再UserContent与user链接查询
     * @param userContent
     * @return
     */
    List<UserContent> findByJoin(UserContent userContent);

}
