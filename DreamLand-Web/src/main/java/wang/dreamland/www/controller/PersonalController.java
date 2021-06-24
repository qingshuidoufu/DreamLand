package wang.dreamland.www.controller;

import com.sun.org.apache.xpath.internal.operations.Mod;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import wang.dreamland.www.common.Constants;
import wang.dreamland.www.common.DateUtils;
import wang.dreamland.www.common.MD5Util;
import wang.dreamland.www.common.PageHelper;
import wang.dreamland.www.entity.User;
import wang.dreamland.www.entity.UserContent;
import wang.dreamland.www.entity.UserInfo;
import wang.dreamland.www.service.*;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class PersonalController extends BaseController {
    private final static Logger log = Logger.getLogger(PersonalController.class);
    @Autowired
    private UserContentService userContentService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private UpvoteService upvoteService;

    @Autowired
    private UserInfoService userInfoService;

    @Autowired
    private UserService userService;
    /**
     * 初始化个人主页数据
     * @param model
     * @param id
     * @param pageNum
     * @param pageSize
     * @return
     */
    @RequestMapping("/list")
    public String findList(Model model, @RequestParam(value = "id",required = false) String id,
                           @RequestParam(value = "manage",required = false) String manage ,
                           @RequestParam(value = "pageNum",required = false) Integer pageNum,
                           @RequestParam(value = "pageSize",required = false) Integer pageSize) {
        User user = (User)getSession().getAttribute("user");
        UserContent content = new UserContent();
        UserContent uc = new UserContent();
        if(user!=null){
            model.addAttribute( "user",user );
            content.setuId( user.getId() );
            uc.setuId(user.getId());
        }else{
            return "../login";
        }
        log.info("初始化个人主页信息");

        if(StringUtils.isNotBlank(manage)){
            model.addAttribute("manage",manage);
        }

        //查询梦分类
        List<UserContent> categorys = userContentService.findCategoryByUid(user.getId());
        model.addAttribute( "categorys",categorys );
        //发布的梦 不含私密梦
        content.setPersonal("0");
        pageSize = 10; //默认每页显示10条数据
        PageHelper.Page<UserContent> page =  findAll(content,pageNum,  pageSize); //分页

        model.addAttribute( "page",page );

        //查询私密梦
        uc.setPersonal("1");
        PageHelper.Page<UserContent> page2 =  findAll(uc,pageNum,  pageSize);
        model.addAttribute( "page2",page2 );

        //查询热梦
        pageSize = 15; //默认每页显示15条数据
        UserContent uct = new UserContent();
        uct.setPersonal("0");
        PageHelper.Page<UserContent> hotPage =  findAllByUpvote(uct,pageNum,  pageSize);
        model.addAttribute( "hotPage",hotPage );
        return "personal/personal";
    }
    /**
     * 根据分类名称查询所有文章
     * @param model
     * @param category
     * @return
     */
    @RequestMapping("/findByCategory")
    @ResponseBody
    public Map<String,Object> findByCategory(Model model, @RequestParam(value = "category",required = false) String category, @RequestParam(value = "pageNum",required = false) Integer pageNum ,
                                             @RequestParam(value = "pageSize",required = false) Integer pageSize) {
        Map map = new HashMap<String, Object>();
        User user = (User) getSession().getAttribute("user");
        if (user == null) {
            map.put("pageCate", "fail");
            return map;
        }
        pageSize = 10;//默认显示4条数据
        PageHelper.Page<UserContent> pageCate=userContentService.findByCategory(category,user.getId(),pageNum,pageSize);
        map.put("pageCate",pageCate);
        return map;
    }
    /**
     * 根据用户id查询私密梦
     * @param model
     * @param pageNum
     * @param pageSize
     * @return
     */
    @RequestMapping("/findPersonal")
    @ResponseBody
    public Map<String,Object> findPersonal(Model model,@RequestParam(value = "pageNum",required = false) Integer pageNum , @RequestParam(value = "pageSize",required = false) Integer pageSize) {
    Map map=new HashMap<String,Object>();
    User user =(User)  getSession().getAttribute("user");
    if(user==null){
        map.put("page2","fail");
        return map;
    }
    pageSize=10;//默认煤业显示4条数据
    PageHelper.Page<UserContent> page=userContentService.findPersonal(user.getId(),pageNum,pageSize);
    map.put("page2",page);
    return map;


    }
    /**
     * 查询出所有文章并分页 根据点赞数倒序排列
     * @param model
     * @param pageNum
     * @param pageSize
     * @return
     */
    @RequestMapping("/findAllHotContents")
    @ResponseBody
    public Map<String,Object> findAllHotContents(Model model, @RequestParam(value = "pageNum",required = false) Integer pageNum , @RequestParam(value = "pageSize",required = false) Integer pageSize) {

        Map map = new HashMap<String,Object>(  );
        User user = (User)getSession().getAttribute("user");
        if(user==null) {
            map.put("hotPage","fail");
            return map;
        }
        pageSize = 15; //默认每页显示4条数据
        UserContent uct = new UserContent();
        uct.setPersonal("0");
        PageHelper.Page<UserContent> hotPage =  findAllByUpvote(uct,pageNum,  pageSize);
        map.put("hotPage",hotPage);
        return map;
    }
    @RequestMapping("/deleteContent")
    public String deleteContent(Model model, @RequestParam(value = "cid",required = false) Long cid) {
        //判断用户是否登录,未登录跳转到登录页面
        User user = (User)getSession().getAttribute("user");
        if(user==null) {
            return "../login";
        }
        commentService.deleteByContentId(cid); //删评论
        upvoteService.deleteByContentId(cid); //删点赞
        userContentService.deleteById(cid);  //删内容
        return "redirect:/list?manage=manage";  //重定向到个人页面
    }

    /**
     * 进入个人页面修改页面
     * @param model
     * @return
     */
    @RequestMapping("/profile")
    public String profile(Model model){
        User user=(User) getSession().getAttribute("user");
        if(user==null){
            return "../login";
        }
        UserInfo userInfo= userInfoService.findByUid(user.getId());
        model.addAttribute("user",user);
        model.addAttribute("userInfo",userInfo);
        return "personal/profile";
    }

    @RequestMapping("/saveImage")
    @ResponseBody
    public Map<String,Object> saveImage(Model model,@RequestParam(value = "url",required = false) String url

                                        ){
        Map map=new HashMap<String,Object>();
        User user=(User) getSession().getAttribute("user");
        user.setImgUrl(url);
        userService.update(user);
        map.put("msg","success");
        return map;
    }

    /**
     * 保存用户信息
     * @param model
     * @param name
     * @param nickName
     * @param sex
     * @param address
     * @param birthday
     * @return
     */
    @RequestMapping("/saveUserInfo")
    public String saveUserInfo(Model model, @RequestParam(value = "name",required = false) String name ,
                               @RequestParam(value = "nick_name",required = false) String nickName,
                               @RequestParam(value = "sex",required = false) String sex,
                               @RequestParam(value = "address",required = false) String address,
                               @RequestParam(value = "birthday",required = false) String birthday){
        User user=(User) getSession().getAttribute("user");
        if(user==null){
            return "../login";
        }
        //尝试查出userInfo
        UserInfo userInfo=userInfoService.findByUid(user.getId());
        boolean flag=false;
        if(userInfo==null){
            //没查到就新建呗
            userInfo=new UserInfo();
        }else{
            flag=true;
        }
        userInfo.setName(name);
        userInfo.setAddress(address);
        userInfo.setSex(sex);
        Date bir= DateUtils.StringToDate(birthday,"yyyy-MM-dd");
        userInfo.setBirthday(bir);
        userInfo.setuId(user.getId());
        if(!flag){//flag不为真则是没有userInfo数据
            userInfoService.add(userInfo);
        }else{//flag为真则是已有数据,更新即可
            userInfoService.update(userInfo);
        }
        user.setNickName(nickName);
        userService.update(user);
        model.addAttribute("user",user);
        model.addAttribute("userInfo",userInfo);
        return "personal/profile";
    }

    /**
     * 更改密码页面跳转
     * @param model
     * @return
     */
    @RequestMapping("/repassword")
    public String repassword(Model model){
        User user=(User)  getSession().getAttribute("user");
        if(user!=null){
            model.addAttribute("user",user);
            return "personal/repassword";
        }
        return "..login";

    }

    /**
     * 修改密码事件
     * @param model
     * @param oldPassword
     * @param password
     * @return
     */
    @RequestMapping("/updatePassword")
    public String updatePassword(Model model, @RequestParam(value = "old_password",required = false) String oldPassword,
                                 @RequestParam(value = "password",required = false) String password){
        User user=(User)  getSession().getAttribute("user");
        if(user!=null){
            oldPassword= MD5Util.encodeToHex(Constants.SALT+oldPassword);
            if(user.getPassword().equals(oldPassword)){
                password=MD5Util.encodeToHex(Constants.SALT+password);
                user.setPassword(password);
                //成功修改密码,更新user
                userService.update(user);
                model.addAttribute("message","success");
            }else{
                model.addAttribute("message","fail");
            }
        }
        model.addAttribute("user",user);
        return "personal/passwordSuccess";
    }



}
