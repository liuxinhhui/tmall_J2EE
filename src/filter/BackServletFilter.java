package filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang.StringUtils;

/*
 * filter + servlet + ����
 * ʵ��������һ��Servlet�ദ�����ݲ�����ͬ���ò�ͬ����
 */
public class BackServletFilter implements Filter {

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
		
		String uri = request.getRequestURI();
		String contextPath = request.getServletContext().getContextPath();
		uri = StringUtils.remove(uri, contextPath);
		
		if(uri.startsWith("/admin")){
			String servletPath = "/" + StringUtils.substringBetween(uri, "_", "_") + "Servlet";	//web.xml��servlet���õķ���·��
			String method = StringUtils.substringAfterLast(uri, "_");	//��Ӧ����ķ���
			
			request.setAttribute("method", method);
			request.getRequestDispatcher(servletPath).forward(request, response);
			return;
		}
		
		chain.doFilter(req, res);
	}

	@Override
	public void init(FilterConfig config) throws ServletException {
		// TODO Auto-generated method stub
		
	}
	
}
