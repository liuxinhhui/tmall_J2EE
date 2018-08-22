package servlet;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bean.Category;
import bean.Property;
import dao.CategoryDAO;
import dao.PropertyDAO;
import util.Page;

public class PropertyServlet extends BaseBackServlet{
	PropertyDAO propertyDAO = new PropertyDAO();
	CategoryDAO categoryDAO = new CategoryDAO();
	
	public String list(HttpServletRequest request, HttpServletResponse response, Page page){
		int cid = Integer.parseInt(request.getParameter("cid"));
		
		Category c = categoryDAO.get(cid);
		ArrayList<Property> ps =  propertyDAO.list(cid, page.getStart(),page.getCount());
		int total = propertyDAO.getTotal();
		page.setTotal(total);
		page.setParam("?cid="+cid);
		
		request.setAttribute("c", c);
		request.setAttribute("page", page);
		request.setAttribute("ps", ps);
		
		return "admin/listProperty.jsp";
	}
	
	public String add(HttpServletRequest request, HttpServletResponse response, Page page){
		int cid = Integer.parseInt(request.getParameter("cid"));
		String name = request.getParameter("name");
		
		Category c = categoryDAO.get(cid);
		Property p = new Property();
		p.setCategory(c);
		p.setName(name);
		propertyDAO.add(p);
		
		return "@admin_property_list?cid=" + cid;	//进行客户端跳转，cid参数要重新设置
	}
	
	public String delete(HttpServletRequest request, HttpServletResponse response, Page page){
		int id = Integer.parseInt(request.getParameter("id"));
		
		Property p = propertyDAO.get(id);
		propertyDAO.delete(id);
		
		return "@admin_property_list?cid=" + p.getCategory().getId(); 
	}
	
	public String edit(HttpServletRequest request, HttpServletResponse response, Page page){
		int id = Integer.parseInt(request.getParameter("id"));
		
		Property p = propertyDAO.get(id);
		
		request.setAttribute("p", p);
		
		return "admin/editProperty.jsp";
	}
	
	public String update(HttpServletRequest request, HttpServletResponse response, Page page){
		int id = Integer.parseInt(request.getParameter("id"));
		int cid = Integer.parseInt(request.getParameter("cid"));
		String name = request.getParameter("name");
		
		Category c = categoryDAO.get(cid);
		Property p = new Property();
		p.setId(id);
		p.setCategory(c);
		p.setName(name);
		propertyDAO.update(p);
		
		return "@admin_property_list?cid=" + cid;
	}
}
