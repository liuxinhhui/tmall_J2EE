package filter;

import java.io.IOException;
import java.util.Arrays;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import bean.User;

public class ForeAuthFilter implements Filter{

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
			throws IOException, ServletException {
		// TODO Auto-generated method stub
		HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        String contextPath=request.getServletContext().getContextPath();
 
        String[] noNeedAuthPage = new String[]{
                "home",
                "checkLogin",
                "register",
                "loginAjax",
                "login",
                "product",
                "category",
                "search"};
         
        String uri = request.getRequestURI();
        uri =StringUtils.remove(uri, contextPath);
        if(uri.startsWith("/fore")&&!uri.startsWith("/foreServlet")&&!uri.startsWith("/fore/")){
            String method = StringUtils.substringAfterLast(uri,"/fore" );
            if(!Arrays.asList(noNeedAuthPage).contains(method)){
                User user =(User) request.getSession().getAttribute("user");
                if(null==user){
                    response.sendRedirect("/fore/loginPage.jsp");
                    return;
                }
            }
        }
         
        chain.doFilter(req, res);
	}

	@Override
	public void init(FilterConfig config) throws ServletException {
		// TODO Auto-generated method stub
		
	}

}
