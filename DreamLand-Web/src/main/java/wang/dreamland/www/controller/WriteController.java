package wang.dreamland.www.controller;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import wang.dreamland.www.entity.User;
import wang.dreamland.www.entity.UserContent;
import wang.dreamland.www.service.UserContentService;

import java.util.Date;

@Controller
public class WriteController extends BaseController{
    private final static Logger log = Logger.getLogger(WriteController.class);
    @Autowired
    private UserContentService userContentService;

    /**
     * 进入writedream
     * @param model
     * @return
     */
    @RequestMapping("/writedream")
    public String writedream(Model model,@RequestParam(value = "cid",required = false) Long cid){
        User user=(User) getSession().getAttribute("user");
        if(cid!=null){  //如果文章id有就是修改, 查出来并传给前端
            UserContent content=userContentService.findById(cid);
            model.addAttribute("cont",content);
        }
        //将user放入model中
        model.addAttribute("user",user);
        return "write/writedream";
    }
    /**
     * 写文章
     * @param model
     * @param id
     * @param category
     * @param txtT_itle
     * @param content
     * @param private_dream
     * @return
     */
    @RequestMapping("/doWritedream")
    public String doWritedream(Model model, @RequestParam(value = "id",required = false) String id,
                               @RequestParam(value = "cid",required = false) Long cid,
                               @RequestParam(value = "category",required = false) String category,
                               @RequestParam(value = "txtT_itle",required = false) String txtT_itle,
                               @RequestParam(value = "content",required = false) String content,
                               @RequestParam(value = "private_dream",required = false) String private_dream) {
        log.info( "进入写梦Controller" );
        //获取用户信息,未登录直接跳到登录页面
        User user = (User)getSession().getAttribute("user");
        if(user == null){
            //未登录
            model.addAttribute( "error","请先登录！" );
            return "../login";
        }
        //创建usercontent对象
        UserContent userContent = new UserContent();
        if(cid!=null){//判断前台的文章id非空, 非空修改,赋值给userContent
            userContent = userContentService.findById(cid);
        }
        userContent.setCategory( category );
        userContent.setContent( content );
        userContent.setRptTime( new Date(  ) );
        String imgUrl = user.getImgUrl();
        if(StringUtils.isBlank( imgUrl )){
            userContent.setImgUrl( "/images/icon_m.jpg" );
        }else {
            userContent.setImgUrl( imgUrl );
        }
        if("on".equals( private_dream )){
            userContent.setPersonal( "1" );
        }else{
            userContent.setPersonal( "0" );
        }
        userContent.setTitle( txtT_itle );
        userContent.setuId( user.getId() );
        userContent.setNickName( user.getNickName() );

        if(cid ==null){//cid未空,插入数据库
            userContent.setUpvote( 0 );
            userContent.setDownvote( 0 );
            userContent.setCommentNum( 0 );
            //添加内容
            userContentService.addContent( userContent );
        }else {//cid不为空, 修改数据库
            //已有更新
            userContentService.updateById(userContent);
        }
        //传到前端
        model.addAttribute("content",userContent);
        return "write/writesuccess";
    }
    /**
     * 根据文章id查看文章(发布文章成功后查看文章)
     * @param model
     * @param cid
     * @return
     */
    @RequestMapping("/watch")
    public String watchContent(Model model, @RequestParam(value = "cid",required = false) Long cid){
        User user = (User)getSession().getAttribute("user");
        if(user == null){
            //未登录
            model.addAttribute( "error","请先登录！" );
            return "../login";
        }
        UserContent userContent = userContentService.findById(cid);
        model.addAttribute("cont",userContent);
        return "personal/watch";
    }
}
