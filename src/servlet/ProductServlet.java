package servlet;

import java.util.ArrayList;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bean.Category;
import bean.Product;
import dao.CategoryDAO;
import dao.ProductDAO;
import util.Page;

public class ProductServlet extends BaseBackServlet{
	CategoryDAO categoryDAO = new CategoryDAO();
	ProductDAO productDAO = new ProductDAO();
	
	public String list(HttpServletRequest request, HttpServletResponse response, Page page){
		int cid = Integer.parseInt(request.getParameter("cid"));
		
		ArrayList<Product> ps  = productDAO.list(cid, page.getStart(), page.getCount());
		int total = productDAO.getTotal();
		Category c = categoryDAO.get(cid);
		
		page.setParam("&cid="+cid);	//������Ϊ�����ҳ��ʱ�򴫲�
		page.setTotal(total);
		request.setAttribute("ps", ps);
		request.setAttribute("page", page);
		request.setAttribute("c", c);
	
		return "admin/listProduct.jsp";
	}
	
	public String add(HttpServletRequest request, HttpServletResponse response, Page page){
		int cid = Integer.parseInt(request.getParameter("cid"));
		String name = request.getParameter("name");
		String subTitle = request.getParameter("subTitle");
		float originalPrice = Float.parseFloat(request.getParameter("originalPrice"));
		float promotePrice = Float.parseFloat(request.getParameter("promotePrice"));
		int stock = Integer.parseInt(request.getParameter("stock"));
		
		Category c = categoryDAO.get(cid);
		Product p = new Product();
		p.setName(name);
		p.setSubTitle(subTitle);
		p.setOriginalPrice(originalPrice);
		p.setPromotePrice(promotePrice);
		p.setStock(stock);
		p.setCreateDate(new Date());
		p.setCategory(c);
		productDAO.add(p);

		return "@admin_product_list?cid=" + cid;
	}
	
	public String delete(HttpServletRequest request, HttpServletResponse response, Page page){
		int id = Integer.parseInt(request.getParameter("id"));
		
		Product p = productDAO.get(id);
		
		productDAO.delete(id);	//���ݿ�����Ȼû��  ����p����û�б����� ���к��������
		
		return "@admin_product_list?cid=" + p.getCategory().getId();
	}
	
	public String edit(HttpServletRequest request, HttpServletResponse response, Page page){
		int id = Integer.parseInt(request.getParameter("id"));
		
		Product p = productDAO.get(id);
		
		request.setAttribute("p", p);
		
		return "admin/editProduct.jsp";
	}
	
	public String update(HttpServletRequest request, HttpServletResponse response, Page page){
		int id = Integer.parseInt(request.getParameter("id"));
		String name = request.getParameter("name");
		String subTitle = request.getParameter("subTitle");
		float originalPrice = Float.parseFloat(request.getParameter("originalPrice"));
		float promotePrice = Float.parseFloat(request.getParameter("promotePrice"));
		int stock = Integer.parseInt(request.getParameter("stock"));
		int cid = Integer.parseInt(request.getParameter("cid"));
		
		Category c = categoryDAO.get(cid);
		Product p = new Product();
		p.setId(id);
		p.setName(name);
		p.setSubTitle(subTitle);
		p.setOriginalPrice(originalPrice);
		p.setPromotePrice(promotePrice);
		p.setStock(stock);
		p.setCreateDate(new Date());
		p.setCategory(c);
		productDAO.update(p);
		
		return "@admin_product_list?cid=" +cid;
	}
}
