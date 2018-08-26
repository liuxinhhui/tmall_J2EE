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
		
		//������ʾ���ﳵ�ж��ټ�����s
		User user =(User) request.getSession().getAttribute("user");
        int cartTotalItemNumber= 0;
        if(null!=user){
            List<OrderItem> ois = new OrderItemDAO().listByUser(user.getId());
            for (OrderItem oi : ois) {
                cartTotalItemNumber+=oi.getNumber();
            }
        }
        request.setAttribute("cartTotalItemNumber", cartTotalItemNumber);
      //������ʾ���ﳵ�ж��ټ�����e
        
		//��������simpleSearch.jsp ������������ʾ���������õ�s
		List<Category> cs=(List<Category>) request.getAttribute("cs");
        if(null==cs){
            cs = new CategoryDAO().list();
            request.setAttribute("cs", cs);
        }
        //��������simpleSearch.jsp ������������ʾ���������õ�e
		
		String uri = request.getRequestURI();
		String contextPath = request.getServletContext().getContextPath();
		request.getServletContext().setAttribute("contextPath", contextPath);	//��ǰҳ
		uri = StringUtils.remove(uri, contextPath);
		//�ͺ�̨���в�ͬ���ǣ�ǰ̨�����д�������ForeServlet����
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
