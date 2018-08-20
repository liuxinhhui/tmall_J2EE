package servlet;


import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bean.User;
import dao.UserDAO;
import util.Page;

public class UserServlet extends BaseBackServlet{
	UserDAO userDAO = new UserDAO();
	
	public String list(HttpServletRequest request, HttpServletResponse response, Page page){
		/*
		 * 这里考虑到list的sql写法是 start<=id<=end 而不是mysql支持的limit语句:start开始返回count个数据
		 * 因此，第二个参数需要写成如下样子 而不能直接写成page.count
		 * 
		 * 实际测试下来，还是需要使用limit语法 因为id是不会重复的，当删除了某条数据后，id会变得不连续，那么没页
		 * 显示的数目就可能会少于page.count，例如，传入start=0 count=5 加入数据库中只有 1 4那么第一页就只显示这两条数据
		 */
//		ArrayList<User> us = userDAO.list(page.getStart(),page.getStart()+page.getCount()-1);
		ArrayList<User> us = userDAO.list(page.getStart(),page.getCount());
		page.setTotal(userDAO.getTotal());
		
		request.setAttribute("us", us);
		request.setAttribute("page", page);
		
		return "admin/listUser.jsp";
	}
	
}
