package wang.dreamland.www.controller;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import wang.dreamland.www.common.DateUtils;
import wang.dreamland.www.common.PageHelper;
import wang.dreamland.www.common.StringUtil;
import wang.dreamland.www.entity.Comment;
import wang.dreamland.www.entity.Upvote;
import wang.dreamland.www.entity.User;
import wang.dreamland.www.entity.UserContent;
import wang.dreamland.www.service.CommentService;
import wang.dreamland.www.service.UpvoteService;
import wang.dreamland.www.service.UserContentService;
import wang.dreamland.www.service.UserService;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class IndexJspController extends BaseController{
    private final static Logger log=Logger.getLogger(IndexJspController.class);
    //注入userService
    @Autowired
    private UserService userService;
    //注入commentService
    @Autowired
    private CommentService commentService;

    //注入usercontentService,用于更新用户相关信息等
    @Autowired
    private UserContentService userContentService;
    //注入UpvoteService, 用于操作赞和踩等信息
    @Autowired
    private UpvoteService upvoteService;

    /**
     * 分页加载页面文章内容
     */

    @RequestMapping("/index_list")
    public String findAllList(Model model, @RequestParam(value = "id",required = false) String id,
                              @RequestParam(value = "pageNum",required = false) Integer pageNum,
                              @RequestParam(value = "pageSize",required = false) Integer pageSize
                              ){
        log.info("---------进入index_list--------");
        User user  =(User) getSession().getAttribute("user");
        if(user!=null){
            model.addAttribute("user",user);
        }
        //调用BaseController中的findAll,实际上转给service,service查数据库再实现了分页功能,封装到Page中
        PageHelper.Page<UserContent> page =findAll(pageNum,pageSize);
        model.addAttribute("page",page);
        return "../index";
    }

    /**
     * 点赞或者踩的后台处理程序
     * @param model
     * @param id
     * @param uid
     * @param upvote
     * @return
     */
    @RequestMapping("/upvote")
    //@ResponseBydy注解返回数据给前端的ajax请求
    @ResponseBody
    public Map<String,Object> upvote(Model model, @RequestParam(value ="id",required = false)Long id,
                                     @RequestParam(value = "uid",required = false) Long uid,
                                     @RequestParam(value = "upvote",required = false) int upvote
                         ){
        log.info( "id="+id+",uid="+uid+"upvote="+upvote );
        Map map=new HashMap<String,Object>();
        User user=(User) getSession().getAttribute("user");
        //当前用户没登陆直接返回
        if(user==null){
            map.put("data","fail");
            return map;
        }
        //点赞或踩的实体类
        Upvote up=new Upvote();
        up.setContentId(id);
        up.setuId(user.getId());
        //查找该文章id和对应的用户id的点赞和踩的数据
        Upvote upv=upvoteService.findByUidAndConId(up);
        if(upv!=null){
            log.info(upv.toString()+"-------------");

        }
        //通过文章id在数据库中查出这篇文章
        UserContent userContent=userContentService.findById(id);
        if(upvote==-1){ //点踩
            if(upv!=null){
                //获取点踩状态对比
                if("1".equals(upv.getDownvote())){
                    map.put("data","down");
                    //已经点过踩了直接返回
                    return map;
                }else{  //查询得踩的状态不为1, 用查到的upv更新数据库
                    //设置状态被踩
                    upv.setDownvote("1");
                    upv.setUpvoteTime(new Date());
                    upv.setIp(getClientIpAddress());
                    //给点赞/踩的数据库更新
                    upvoteService.update(upv);
                }
            }else {  //查询得upv状态为空,用新建的实体类up来加到数据库中
                up.setDownvote( "1" );
                up.setUpvoteTime( new Date(  ) );
                up.setIp( getClientIpAddress() );
                upvoteService.add(up);
            }
            //usercontent更新点踩数量(还没更到数据库)
            userContent.setDownvote(userContent.getDownvote()+upvote);
        }else{ //点赞
            if(upv!=null){  //查得数据库不为空,直接用查得的upv对象更新数据库
                if("1".equals(upv.getUpvote())){
                    map.put("data","done");
                    //已经点过赞了直接返回
                    return map;
                }else{ //查得upv点赞状态不为1
                    upv.setUpvote("1");
                    upv.setUpvoteTime(new Date());
                    upv.setIp(getClientIpAddress());
                    upvoteService.update(upv);
                }
            }else { //查得数据库为空,用新建的up对象来加进数据库
                up.setUpvote("1");
                up.setUpvoteTime(new Date());
                up.setIp(getClientIpAddress());
                upvoteService.add(up);
            }
            //userContent更新点赞数量(还没更到数据库)
            userContent.setUpvote(userContent.getUpvote()+upvote);
        }
        //点赞和点踩文章状态更新到数据库
        userContentService.updateById(userContent);
        map.put("data","success");
        //点赞或者点踩成功返回
        return map;
}
    @RequestMapping("/reply")
    @ResponseBody
    public Map<String,Object> reply(Model model,@RequestParam(value = "content_id",required = false) Long content_id) {
        Map map = new HashMap<String, Object>();
        //通过文章id查找所有一级评论
        List<Comment> list = commentService.findAllFirstComment(content_id);
        if (list != null && list.size() > 0) {
            //一级评论c
            for (Comment c : list) {
                Map findChildTem = new HashMap();
                findChildTem.put("content_id",c.getConId().toString());
                findChildTem.put("children", c.getChildren());
                //通过文章id和子文章中保存的子评论id字符串,查找所有子评论
                List<Comment> comments = commentService.findAllChildrenComment(findChildTem);
                if (comments != null && comments.size() > 0) {
                    //子评论com
                    for (Comment com : comments) {
                        if (com.getById() != null) {
                            //将子评论的用户id放到子评论的byuser属性中
                            User byUser = userService.findById(com.getById());
                            com.setByUser(byUser);
                        }
                    }
                }
                //将子评论放到一级评论的comlist中
                c.setComList(comments);
            }
        }
        map.put("list",list);
        return map;
    }
    /**
     * 点击评论按钮
     * @param model
     * @param id
     * @param content_id
     * @param uid
     * @param bid
     * @param oSize
     * @param comment_time
     * @param upvote
     * @return
     */
    @RequestMapping("/comment")
    @ResponseBody
    public Map<String,Object> comment(Model model, @RequestParam(value = "id",required = false) Long id ,
                                      @RequestParam(value = "content_id",required = false) Long content_id ,
                                      @RequestParam(value = "uid",required = false) Long uid ,
                                      @RequestParam(value = "by_id",required = false) Long bid ,
                                      @RequestParam(value = "oSize",required = false) String oSize,
                                      @RequestParam(value = "comment_time",required = false) String comment_time,
                                      @RequestParam(value = "upvote",required = false) Integer upvote) {
        Map map = new HashMap<String,Object>(  );
        User user = (User)getSession().getAttribute("user");
        //用户还没登录直接返回
        if(user == null){
            map.put( "data","fail" );
            return map;
        }
        if(id==null ){//评论id为空, 说明该评论是新的评论, 故往数据库中新增

            Date date = DateUtils.StringToDate( comment_time, "yyyy-MM-dd HH:mm:ss" );

            Comment comment = new Comment();
            comment.setComContent( oSize );
            comment.setCommTime( date );
            comment.setConId( content_id );
            comment.setComId( uid );
            if(upvote==null){
                upvote = 0;  //评论的点赞数初始化为0
            }
            comment.setById( bid );
            comment.setUpvote( upvote );
            User u = userService.findById( uid );  //在数据库中查出这个comment的用户信息
            comment.setUser( u );
            commentService.add( comment );  //往数据库中添加一条comment
            map.put( "data",comment ); //往前台传送comment相关信息

            UserContent userContent = userContentService.findById( content_id ); //查出该评论对应文章信息
            Integer num = userContent.getCommentNum();
            userContent.setCommentNum( num+1 );  //给该文章信息中的评论数加1
            userContentService.updateById( userContent );  //更新文章信息

        }else {
            //评论id不为null,则是点赞(给评论点的赞,与文章的点赞不同)
            Comment c = commentService.findById( id ); //查出该评论的信息
            c.setUpvote( upvote ); //给点赞数加1
            commentService.update( c ); //更新该评论的数据库表

        }
        return map; //往前台回传map

    }

    /**
     * 删除评论
     * @param model
     * @param id   评论id
     * @param uid   文章作者id
     * @param con_id  文章id
     * @param fid 父评论id
     * @return
     */
    @RequestMapping("deleteComment")
    @ResponseBody
    public Map<String,Object> deleteComment(Model model,@RequestParam(value = "id",required = false) Long id,
                                            @RequestParam(value = "uid",required = false) Long uid,
                                            @RequestParam(value = "con_id",required = false) Long con_id,
                                            @RequestParam(value = "fid",required = false) Long fid
                                            ){
        int num=0;
        Map map=new HashMap<String,Object>();
        User user=(User) getSession().getAttribute("user");
        if(user==null){
            map.put("data","fail");
        }else{
            if(user.getId().equals(uid)){//是当前用户在删除评论
                Comment comment=commentService.findById(id);
                if(StringUtils.isBlank(comment.getChildren())){//当前评论没有子评论
                    if(fid!=null){
                        //去除id
                        Comment fcomm=commentService.findById(fid);
                        String child= StringUtil.getString(fcomm.getChildren(),id);
                        fcomm.setChildren(child);
                        //更新父评论中的子评论字符串
                        commentService.update(fcomm);
                    }
                    //删掉该评论
                    commentService.deleteById(id);
                    num=num+1;
                }else{//当前评论有子评论
                    String children=comment.getChildren();
                    //把子评论也删掉
                    commentService.deleteChildrenComment(children);
                    String[] arr=children.split(",");
                    //删除当前评论
                    commentService.deleteById(id);
                    num=num+arr.length+1;
                }
                UserContent content =userContentService.findById(con_id);
                if(content!=null){//更新文章的评论数量
                    if(content.getCommentNum() - num >= 0){
                        content.setCommentNum( content.getCommentNum() - num );
                    }else {
                        content.setCommentNum( 0 );
                    }
                    //持久化到数据库
                    userContentService.updateById( content );
                }
                map.put("data",content.getCommentNum());
            }else{
                map.put("data","no-access");
            }
        }
        return map;
    }
    /**
     * 点击一级评论块的评论按钮
     * @param model
     * @param id  父评论id
     * @param content_id 文章id
     * @param uid 用户id
     * @param bid 被评论者id
     * @param oSize 评论内容
     * @param comment_time 时间
     * @param upvote 点赞数
     * @return
     */
    @RequestMapping("/comment_child")
    @ResponseBody
    public Map<String,Object> addCommentChild(Model model,@RequestParam(value="id",required = false)Long id,
                                              @RequestParam(value = "content_id",required = false) Long content_id ,
                                              @RequestParam(value = "uid",required = false) Long uid ,
                                              @RequestParam(value = "by_id",required = false) Long bid ,
                                              @RequestParam(value = "oSize",required = false) String oSize,
                                              @RequestParam(value = "comment_time",required = false) String comment_time,
                                              @RequestParam(value = "upvote",required = false) Integer upvote
                                              ){
        Map map=new HashMap<String,Object>();
        User user=(User) getSession().getAttribute("user");
        //用户没登录就想删除评论那不可能
        if(user==null){
            map.put("data","fail");
            return map;
        }
        //转日期
        Date date=DateUtils.StringToDate(comment_time,"yyyy-MM-dd HH:mm:ss" );
        Comment comment=new Comment();
        comment.setComContent(oSize);
        comment.setCommTime(date);
        comment.setConId(content_id);
        comment.setComId(uid);
        if(upvote==null){
            upvote=0;
        }
        comment.setById(bid);
        comment.setUpvote( upvote );
        User u = userService.findById( uid );
        comment.setUser( u );
        //插入comment到数据库
        commentService.add( comment );
        //查出父评论
        Comment com = commentService.findById( id );
        if(StringUtils.isBlank( com.getChildren() )){//没有子评论
            com.setChildren( comment.getId().toString() ); //添加到children中
        }else { //有子评论, 追加到children中
            com.setChildren( com.getChildren()+","+comment.getId() );
        }
        //更新到数据库
        commentService.update( com );
        map.put( "data",comment );
        //根据文章id查出usercontent,
        UserContent userContent = userContentService.findById( content_id );
        //获取评论数
        Integer num = userContent.getCommentNum();
        //文章数的评论加一
        userContent.setCommentNum( num+1 );
        //更新到数据库
        userContentService.updateById( userContent );
        return map;
    }
}

