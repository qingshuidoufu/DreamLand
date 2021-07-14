package wang.dreamland.www.interceptor;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import tk.mybatis.mapper.entity.Example;
import wang.dreamland.www.common.PageHelper;
import wang.dreamland.www.controller.IndexJspController;
import wang.dreamland.www.dao.UserContentMapper;
import wang.dreamland.www.dao.UserMapper;
import wang.dreamland.www.entity.UserContent;

import javax.servlet.*;
import java.io.IOException;
import java.util.List;

public class IndexJspFilter implements Filter{
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    /**
     * 给首次进入网站设置过滤器,过滤后调用分页插件拦截器,拦截sql语句后封装刀到page中
     * @param servletRequest
     * @param servletResponse
     * @param filterChain
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        System.out.println("------自定义过滤器-------");
        //获取servletcontext(一个web容器一个servletContext)
        ServletContext context=servletRequest.getServletContext();
        //通过Servletcontext获取applicationContext(spring的东西,用于获取spring容器的内容)
        ApplicationContext ctx= WebApplicationContextUtils.getWebApplicationContext(context);
        //获取userContentsMapper操作数据库的对象
        UserContentMapper userContentMapper=ctx.getBean(UserContentMapper.class);
        PageHelper.startPage(null,null);//开始分页，默认分一页7个
        System.out.println("------分页开始:拦截器启动-----");
        UserContent topUserContent =new UserContent();
        topUserContent.setId(31l);
        List<UserContent> topList=userContentMapper.findByJoin(topUserContent);
        if(topList!=null && topList.size()>0){
            topUserContent= topList.get(0);
        }else {
            topUserContent=null;
        }
        List<UserContent> list = userContentMapper.findByJoin(null);//查出所有内容,进行数据的第二次封装,mybatis拦截了(PageHelper是拦截类)后封装到了endPage中的result
        PageHelper.Page endPage=PageHelper.endPage();//分页结果
        System.out.println("------首页分页结束,Page为:-----"+endPage);
        servletRequest.setAttribute("page",endPage);//然后将 Page 对象放在 request 域中，前台可通过 EL 表达式 ${page}
        servletRequest.setAttribute("topContent",topUserContent);
        filterChain.doFilter(servletRequest,servletResponse);
    }

    @Override
    public void destroy() {

    }
}
