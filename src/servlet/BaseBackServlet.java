package servlet;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import util.Page;

public class BaseBackServlet extends HttpServlet{
	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		//获取分页信息参数 默认值start=0 count=5
		int start=0;
		int count=5;
		try {
			start = Integer.parseInt(request.getParameter("page.start"));
		} catch (Exception e) {
		}
		try {
			count = Integer.parseInt(request.getParameter("page.count"));
		} catch (Exception e) {
		}
		Page page = new Page(start,count);
		
		
		String method = (String) request.getAttribute("method");
		try {
			
			//反射调用响应方法
			Method m = this.getClass().getMethod(method, HttpServletRequest.class, HttpServletResponse.class, Page.class);
			String redirect = m.invoke(this, request, response, page).toString();
			
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
	
	/*
	 * 把表单上传的数据 一般数据保存到params参数中，file数据通过InputStream形式返回 只适用于一个file
	 */
	public InputStream parseUpload(HttpServletRequest request, Map<String, String> params) {
		InputStream is =null;
		try {
            DiskFileItemFactory factory = new DiskFileItemFactory();
            ServletFileUpload upload = new ServletFileUpload(factory);
            // 最大上传10M
            factory.setSizeThreshold(1024 * 10240);
             
            List items = upload.parseRequest(request);
            Iterator iter = items.iterator();
            while (iter.hasNext()) {
                FileItem item = (FileItem) iter.next();
                if (!item.isFormField()) {
                    is = item.getInputStream();
                } else {
                	String paramName = item.getFieldName();
                	String paramValue = item.getString();
                	paramValue = new String(paramValue.getBytes("ISO-8859-1"), "UTF-8");
                	params.put(paramName, paramValue);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
		return is;
	}
}
