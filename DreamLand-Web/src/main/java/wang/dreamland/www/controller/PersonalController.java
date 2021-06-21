package wang.dreamland.www.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import wang.dreamland.www.common.PageHelper;
import wang.dreamland.www.entity.User;
import wang.dreamland.www.entity.UserContent;
import wang.dreamland.www.service.UserContentService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class PersonalController extends BaseController {
    private final static Logger log = Logger.getLogger(PersonalController.class);
    @Autowired
    private UserContentService userContentService;
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
                           @RequestParam(value = "pageNum",required = false) Integer pageNum ,
                           @RequestParam(value = "pageSize",required = false) Integer pageSize){
        User user =(User) getSession().getAttribute("user");
        //用于查询公开的梦
        UserContent content=new UserContent();
        //用于查询私密的梦
        UserContent uc=new UserContent();
        if(user!=null){//用户已登录
            model.addAttribute("user",user);
            content.setuId(user.getId());
            uc.setuId(user.getId());
        }else{
            return "../login";
        }
        log.info("初始化个人主页信息");
        //查询梦分类
        List<UserContent> categorys=userContentService.findCategoryByUid(user.getId());
        model.addAttribute("categorys",categorys);
        //发布的梦 不包含私密梦
        content.setPersonal("0");
        pageSize=4; //默认每页显示4条数据
        PageHelper.Page<UserContent> page=findAll(content,pageNum,pageSize);//查询出来的内容分页

        model.addAttribute("page",page);

        //查询私密梦
        uc.setPersonal("1");
        PageHelper.Page<UserContent> page2=findAll(uc,pageNum,pageSize);
        model.addAttribute("page2",page2);

        //查询热梦
        UserContent uct=new UserContent();
        uct.setPersonal("0");
        PageHelper.Page<UserContent> hotPage=findAllByUpvote(uct,pageNum,pageSize);
        model.addAttribute("hotPage",hotPage);
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
        pageSize = 4;//默认显示4条数据
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
    pageSize=4;//默认煤业显示4条数据
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
        pageSize = 4; //默认每页显示4条数据
        UserContent uct = new UserContent();
        uct.setPersonal("0");
        PageHelper.Page<UserContent> hotPage =  findAllByUpvote(uct,pageNum,  pageSize);
        map.put("hotPage",hotPage);
        return map;
    }

}
