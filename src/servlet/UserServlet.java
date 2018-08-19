package servlet;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.UserDAO;

public class UserServlet extends BaseBackServlet{
	UserDAO userDao = new UserDAO();
	
	public String list(HttpServletRequest request, HttpServletResponse response){
		return "user.jsp";
	}
	
}
