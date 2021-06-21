package wang.dreamland.www.controller;


import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import wang.dreamland.www.common.Constants;
import wang.dreamland.www.common.MD5Util;
import wang.dreamland.www.entity.User;
import wang.dreamland.www.service.UserService;


@Controller
public class LoginController extends BaseController{
    private final static Logger log=Logger.getLogger(LoginController.class);

    @Autowired
    private UserService userService;

    /**
     * 进入页面时候判断用户是否已经登录,已经登录则直接进入个人页面
     * @param model
     * @return
     */
    @RequestMapping("/login")
    public String login(Model model){
        User user=(User)getSession().getAttribute("user");
        if(user!=null) {
            return "/personal/personal";
        }else{
            return "../login";
        }
    }
    //匹配验证码的正确性
    public int checkValidateCode(String code){
        Object vercode=getRequest().getSession().getAttribute("VERCODE_KEY");
        if(null==vercode){
            //session中的验证码为空
            return -1;
        }
        //前端传入的验证码不等于session中的
        if(!code.equalsIgnoreCase(vercode.toString())){
            return 0;
        }else{
            //验证成功
            return 1;
        }
    }

    /**
     * 点击登录按钮,  从无到有的登录
     * @param model
     * @param email
     * @param password
     * @param code
     * @param state
     * @param pageNum
     * @param pageSize
     * @return
     */
    @RequestMapping("/doLogin")
    public String doLogin(Model model, @RequestParam(value = "username",required = false) String email,
                          @RequestParam(value = "password",required = false) String password,
                          @RequestParam(value="code",required = false) String code,
                          @RequestParam(value="state",required = false) String state,
                          @RequestParam(value="pageNum",required = false) Integer pageNum,
                          @RequestParam(value = "pageSize",required = false ) Integer pageSize
                          ){
        if(StringUtils.isBlank(code)){
            model.addAttribute("error","fail");
            return "../login";
        }
        int b=checkValidateCode(code);
        if(b==-1){
            model.addAttribute("error","fails");
            return "../login";
        }else if(b==0){
            model.addAttribute("error","fail");
            return "../login";
        }
        password= MD5Util.encodeToHex(Constants.SALT+password);
        //从数据库中查询出用户
        User user=userService.login(email,password);
        if(user!=null){
            if("0".equals(user.getState())){
                //未激活
                model.addAttribute("email",email);
                model.addAttribute("error","active");
                return "../login";
            }else{
                log.info("用户登录成功");
                getSession().setAttribute("user",user);
                model.addAttribute("user",user);
                return "redirect:/list";
            }
        }else{
            log.info("用户登录失败");
            model.addAttribute("email",email);
            model.addAttribute("error","fail");
            return "../login";
        }

    }
    @RequestMapping("/loginout")
    public String exit(Model model){
        log.info("退出登录");
        //清楚session 中用户信息
        getSession().removeAttribute("user");
        //让session失效
        getSession().invalidate();
        return "../login";
    }
}
