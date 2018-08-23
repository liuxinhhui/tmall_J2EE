package servlet;

import java.util.ArrayList;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bean.Order;
import dao.OrderDAO;
import dao.OrderItemDAO;
import util.Page;

public class OrderServlet extends BaseBackServlet {
	OrderDAO orderDAO = new OrderDAO();
	OrderItemDAO orderItemDAO = new OrderItemDAO();

	public String list(HttpServletRequest request, HttpServletResponse response, Page page) {
		ArrayList<Order> os = orderDAO.list(page.getStart(),page.getCount());
		
		orderItemDAO.fill(os);
		int total = orderDAO.getTotal();
        page.setTotal(total);
        
        request.setAttribute("page", page);
        request.setAttribute("os", os);
		
		return "admin/listOrder.jsp";
	}

	public String delivery(HttpServletRequest request, HttpServletResponse response, Page page) {
		int id = Integer.parseInt(request.getParameter("id"));
		
		Order o = orderDAO.get(id);
	    o.setDeliveryDate(new Date());
	    o.setStatus(OrderDAO.waitConfirm);
	    orderDAO.update(o);
		 
		return "@admin_order_list";
	}
}
