package wang.dreamland.www.controller;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import wang.dreamland.www.common.PageHelper;
import wang.dreamland.www.entity.User;
import wang.dreamland.www.entity.UserContent;

@Controller
public class IndexJspController extends BaseController{
    private final static Logger log=Logger.getLogger(IndexJspController.class);
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
        PageHelper.Page<UserContent> page =findAll(null,pageNum,pageSize);
        model.addAttribute("page",page);
        return "../index";
    }

}
