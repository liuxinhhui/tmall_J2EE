package servlet;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class BaseBackServlet extends HttpServlet{
	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String method = (String) request.getAttribute("method");
		try {
			
			//反射调用响应方法
			Method m = this.getClass().getMethod(method, HttpServletRequest.class, HttpServletResponse.class);
			String redirect = m.invoke(this, request, response).toString();
			
			//通过返回的字符串开头 采取不同的响应
			if(redirect.startsWith("@")){
				response.sendRedirect(redirect.substring(1));
			}else if(redirect.startsWith("%")){
				response.getWriter().print(redirect.substring(1));
			}else{
				request.getRequestDispatcher(redirect).forward(request, response);
			}
		
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
	}
}
