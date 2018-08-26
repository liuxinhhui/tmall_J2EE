package filter;

import java.io.IOException;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import bean.Category;
import bean.OrderItem;
import bean.User;
import dao.CategoryDAO;
import dao.OrderItemDAO;

public class ForeServletFilter implements Filter {

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
		
		//用于显示购物车有多少件东西s
		User user =(User) request.getSession().getAttribute("user");
        int cartTotalItemNumber= 0;
        if(null!=user){
            List<OrderItem> ois = new OrderItemDAO().listByUser(user.getId());
            for (OrderItem oi : ois) {
                cartTotalItemNumber+=oi.getNumber();
            }
        }
        request.setAttribute("cartTotalItemNumber", cartTotalItemNumber);
      //用于显示购物车有多少件东西e
        
		//是用于在simpleSearch.jsp 简单搜索栏下显示分类链接用的s
		List<Category> cs=(List<Category>) request.getAttribute("cs");
        if(null==cs){
            cs = new CategoryDAO().list();
            request.setAttribute("cs", cs);
        }
        //是用于在simpleSearch.jsp 简单搜索栏下显示分类链接用的e
		
		String uri = request.getRequestURI();
		String contextPath = request.getServletContext().getContextPath();
		request.getServletContext().setAttribute("contextPath", contextPath);	//当前页
		uri = StringUtils.remove(uri, contextPath);
		//和后台稍有不同的是，前台的所有处理都交由ForeServlet处理
		if(uri.startsWith("/fore")&&!uri.startsWith("/foreServlet")&&!uri.startsWith("/fore/")){
			String method = StringUtils.substringAfterLast(uri,"/fore" );
			
			request.setAttribute("method", method);
			request.getRequestDispatcher("/foreServlet").forward(request, response);
			return;
		}
		
		chain.doFilter(req, res);
	}

	@Override
	public void init(FilterConfig config) throws ServletException {
		// TODO Auto-generated method stub
		
	}

}
