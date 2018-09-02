package servlet;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.math.RandomUtils;
import org.springframework.web.util.HtmlUtils;

import bean.Category;
import bean.Order;
import bean.OrderItem;
import bean.Product;
import bean.ProductImage;
import bean.PropertyValue;
import bean.Review;
import bean.User;
import comparator.ProductAllComparator;
import comparator.ProductDateComparator;
import comparator.ProductPriceComparator;
import comparator.ProductReviewComparator;
import comparator.ProductSaleCountComparator;
import dao.CategoryDAO;
import dao.OrderDAO;
import dao.OrderItemDAO;
import dao.ProductDAO;
import dao.ProductImageDAO;
import dao.PropertyDAO;
import dao.PropertyValueDAO;
import dao.ReviewDAO;
import dao.UserDAO;
import util.Page;

public class ForeServlet extends BaseBackServlet{
	
	protected CategoryDAO categoryDAO = new CategoryDAO();
    protected OrderDAO orderDAO = new OrderDAO();
    protected OrderItemDAO orderItemDAO = new OrderItemDAO();
    protected ProductDAO productDAO = new ProductDAO();
    protected ProductImageDAO productImageDAO = new ProductImageDAO();
    protected PropertyDAO propertyDAO = new PropertyDAO();
    protected PropertyValueDAO propertyValueDAO = new PropertyValueDAO();
    protected ReviewDAO reviewDAO = new ReviewDAO();
    protected UserDAO userDAO = new UserDAO();
    
    public String home(HttpServletRequest request, HttpServletResponse response, Page page) {
        List<Category> cs= categoryDAO.list();
        new ProductDAO().fill(cs);
        new ProductDAO().fillByRow(cs);
        
        request.setAttribute("cs", cs);
        
        return "fore/homePage.jsp";
    }
    
    //无需登陆的功能
    
    //注册
    public String register(HttpServletRequest request, HttpServletResponse response, Page page) {
    	String name = request.getParameter("name");
        String password = request.getParameter("password");
        name = HtmlUtils.htmlEscape(name);
        boolean exist = userDAO.isExist(name);
         
        if(exist){
            request.setAttribute("msg", "用户名已经被使用,不能使用");
            return "fore/registerPage.jsp"; 
        }
         
        User user = new User();
        user.setName(name);
        user.setPassword(password);
        userDAO.add(user);
         
        return "@fore/registerSuccessPage.jsp"; 
    }
    
    //登陆
    public String login(HttpServletRequest request, HttpServletResponse response, Page page) {
    	String name = request.getParameter("name");
    	String password = request.getParameter("password");
        name = HtmlUtils.htmlEscape(name);
        
        User user = userDAO.get(name, password);
        
        if(null == user){
        	request.setAttribute("msg", "账号密码错误");
            return "fore/loginPage.jsp";
        }
        
        request.getSession().setAttribute("user", user);
        return "@forehome";	//这里不能用服务端跳转 服务端跳转的话 只能直接跳转到某个页面，因为不会再进行一边过滤和BaseForeServlet了
    }
    
    //退出
    public String logout(HttpServletRequest request, HttpServletResponse response, Page page) {
    	request.getSession().removeAttribute("user");
        return "@forehome";
    }
    
    //产品页
    public String product(HttpServletRequest request, HttpServletResponse response, Page page) {
    	int pid = Integer.parseInt(request.getParameter("pid"));
    	
    	//产品实体 包括展示图片和基本信息
    	Product p = productDAO.get(pid);
    	productDAO.setFirstProductImage(p);
    	productDAO.setSaleAndReviewNumber(p);
    	List<ProductImage> productSingleImages = productImageDAO.list(p, ProductImageDAO.type_single);
    	List<ProductImage> productDetailImages = productImageDAO.list(p, ProductImageDAO.type_detail);
    	p.setProductSingleImages(productSingleImages);
    	p.setProductDetailImages(productDetailImages);
    	
    	//产品属性实体
    	List<PropertyValue> pvs = propertyValueDAO.list(p.getId());
    	
    	//产品评价实体
    	List<Review> reviews = reviewDAO.list(p.getId());
        
    	request.setAttribute("p", p);
    	request.setAttribute("pvs", pvs);
    	request.setAttribute("reviews", reviews);
    	
    	return "fore/productPage.jsp?pid=" + pid;
    }
    
    //分类页
    public String category(HttpServletRequest request, HttpServletResponse response, Page page) {
    	int cid = Integer.parseInt(request.getParameter("cid"));
    	
    	Category c = new CategoryDAO().get(cid);
        new ProductDAO().fill(c);
        new ProductDAO().setSaleAndReviewNumber(c.getProducts());      
         
        String sort = request.getParameter("sort");
        if(null!=sort){
            switch(sort){
                case "review":
                    Collections.sort(c.getProducts(),new ProductReviewComparator());
                    break;
                case "date" :
                    Collections.sort(c.getProducts(),new ProductDateComparator());
                    break;
                     
                case "saleCount" :
                    Collections.sort(c.getProducts(),new ProductSaleCountComparator());
                    break;
                     
                case "price":
                    Collections.sort(c.getProducts(),new ProductPriceComparator());
                    break;
                     
                case "all":
                    Collections.sort(c.getProducts(),new ProductAllComparator());
                    break;
            }
        }
        
        request.setAttribute("c", c);
    	
    	return "fore/categoryPage.jsp";
    }
    
    //分类页
    public String search(HttpServletRequest request, HttpServletResponse response, Page page) {
    	String keyword = request.getParameter("keyword");
    	
        List<Product> ps= new ProductDAO().search(keyword,0,20);
        productDAO.setSaleAndReviewNumber(ps);
        
        request.setAttribute("ps",ps);
    	
    	return "fore/searchResultPage.jsp";
    }
    
    //检查是否登陆
    public String checkLogin(HttpServletRequest request, HttpServletResponse response, Page page) {
        User user =(User) request.getSession().getAttribute("user");
        if(null!=user)
            return "%success";
        return "%fail";
    }
    
    //模态登陆
    public String loginAjax(HttpServletRequest request, HttpServletResponse response, Page page) {
    	String name = request.getParameter("name");
        String password = request.getParameter("password");    
        User user = userDAO.get(name,password);
         
        if(null==user){
            return "%fail";
        }
        request.getSession().setAttribute("user", user);
        return "%success"; 
    }
    
    //立即购买 第一步 直接操作数据库中的 订单项 这时的订单项都是订单id=-1 这是订单项还没有生成订单的标志
    public String buyone(HttpServletRequest request, HttpServletResponse response, Page page) {
    	int pid = Integer.parseInt(request.getParameter("pid"));
        int num = Integer.parseInt(request.getParameter("num"));
        User user =(User) request.getSession().getAttribute("user");
        
        Product p = productDAO.get(pid);
        int oiid = 0;
        boolean found = false;
        //考虑到如果没有立即付款 那么依然需要到购物车中找到它  因此需要放在购物车中
        List<OrderItem> ois = orderItemDAO.listByUser(user.getId());
        for (OrderItem oi : ois) {
            if(oi.getProduct().getId()==p.getId()){
                oi.setNumber(oi.getNumber()+num);
                orderItemDAO.update(oi);
                found = true;
                oiid = oi.getId();
                break;
            }
        }      
        if(!found){
            OrderItem oi = new OrderItem();
            oi.setUser(user);
            oi.setNumber(num);
            oi.setProduct(p);
            oi.setOrder(null);
            orderItemDAO.add(oi);
            oiid = oi.getId();
        }
        	
    	return "@forebuy?oiid=" + oiid;
    }
    
    //根据订单项列表 显示结算页面
    public String buy(HttpServletRequest request, HttpServletResponse response, Page page) {
    	String[] oiids=request.getParameterValues("oiid");
    	User user =(User) request.getSession().getAttribute("user");
    	
    	List<OrderItem> ois = new ArrayList<>();
        float total = 0;
     
        for (String strid : oiids) {
            int oiid = Integer.parseInt(strid);
            OrderItem oi= orderItemDAO.get(oiid);
            if(user.getId() != oi.getUser().getId()){
            	response.setContentType("text/html; charset=UTF-8");
            	return "%您试图访问他人的订单 请停止这种行为";
            }
            productDAO.setFirstProductImage(oi.getProduct());
            total +=oi.getProduct().getPromotePrice()*oi.getNumber();
            ois.add(oi);
        }
         
        request.getSession().setAttribute("ois", ois);
        request.setAttribute("total", total);
        
        return "fore/settleAccountPage.jsp";
    }
    
    
    //加入购物车
    public String addCart(HttpServletRequest request, HttpServletResponse response, Page page) {
        int pid = Integer.parseInt(request.getParameter("pid"));
        int num = Integer.parseInt(request.getParameter("num"));
        User user =(User) request.getSession().getAttribute("user");

        Product p = productDAO.get(pid);
        boolean found = false;
        List<OrderItem> ois = orderItemDAO.listByUser(user.getId());
        for (OrderItem oi : ois) {
            if(oi.getProduct().getId()==p.getId()){
                oi.setNumber(oi.getNumber()+num);
                orderItemDAO.update(oi);
                found = true;
                break;
            }
        }      
        if(!found){
            OrderItem oi = new OrderItem();
            oi.setUser(user);
            oi.setNumber(num);
            oi.setProduct(p);
            orderItemDAO.add(oi);
        }
        return "%success";
    }
    
    //我的订单
    public String cart(HttpServletRequest request, HttpServletResponse response, Page page) {
        User user =(User) request.getSession().getAttribute("user");
        
        List<OrderItem> ois = orderItemDAO.listByUser(user.getId());
        for (OrderItem oi : ois) {
			productDAO.setFirstProductImage(oi.getProduct());
		}
        
        request.setAttribute("ois", ois);
        
        return "fore/shoppingcartPage.jsp";
    }
    
    //在购物修改订单项
    public String changeOrderItem(HttpServletRequest request, HttpServletResponse response, Page page) {
    	User user =(User) request.getSession().getAttribute("user");
    	if(null==user)
			return "%fail";
    	
    	int pid = Integer.parseInt(request.getParameter("pid"));
    	int number = Integer.parseInt(request.getParameter("number"));
    	
    	List<OrderItem> ois = orderItemDAO.listByUser(user.getId());
    	for (OrderItem oi : ois) {
			if(oi.getProduct().getId()==pid){
				oi.setNumber(number);
				orderItemDAO.update(oi);
				break;
			}
			
		}
    	
    	return "%success";
    }
    
    //在购物车删除订单项
    public String deleteOrderItem(HttpServletRequest request, HttpServletResponse response, Page page){
		User user =(User) request.getSession().getAttribute("user");
		if(null==user)
			return "%fail";
		
		int oiid = Integer.parseInt(request.getParameter("oiid"));
		orderItemDAO.delete(oiid);
		
		return "%success";
	}
    
    //生成订单
    public String createOrder(HttpServletRequest request, HttpServletResponse response, Page page){
        User user =(User) request.getSession().getAttribute("user");
        List<OrderItem> ois= (List<OrderItem>) request.getSession().getAttribute("ois");
        if(ois.isEmpty())
            return "@/fore/loginPage.jsp";
     
        String address = request.getParameter("address");
        String post = request.getParameter("post");
        String receiver = request.getParameter("receiver");
        String mobile = request.getParameter("mobile");
        String userMessage = request.getParameter("userMessage");
        
        Order order = new Order();
        String orderCode = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date()) +RandomUtils.nextInt(10000);
     
        order.setOrderCode(orderCode);
        order.setAddress(address);
        order.setPost(post);
        order.setReceiver(receiver);
        order.setMobile(mobile);
        order.setUserMessage(userMessage);
        order.setCreateDate(new Date());
        order.setUser(user);
        order.setStatus(OrderDAO.waitPay);
     
        orderDAO.add(order);
        
        float total =0;
        for (OrderItem oi: ois) {
            oi.setOrder(order);
            orderItemDAO.update(oi);
            total+=oi.getProduct().getPromotePrice()*oi.getNumber();
        }
         
        return "@forepay?oid="+order.getId() +"&total="+total;
    }
    
    public String pay(HttpServletRequest request, HttpServletResponse response, Page page){
        return "fore/payPage.jsp";
    }
    
    //支付成功
    public String paysuccess(HttpServletRequest request, HttpServletResponse response, Page page) {
        int oid = Integer.parseInt(request.getParameter("oid"));
        
        Order order = orderDAO.get(oid);
        order.setStatus(OrderDAO.waitDelivery);
        order.setPayDate(new Date());
        new OrderDAO().update(order);
        
        request.setAttribute("o", order);
        
        return "fore/paySuccessPage.jsp";    
    } 
    
    //我的订单
    public String myorder(HttpServletRequest request, HttpServletResponse response, Page page) {
    	User user =(User) request.getSession().getAttribute("user");
    	
        List<Order> os= orderDAO.list(user.getId(),OrderDAO.delete);
        orderItemDAO.fill(os);
        
        for (Order o : os) {
        	for (OrderItem oi : o.getOrderItems()) {
				productDAO.setFirstProductImage(oi.getProduct());
			}
		}
        
        request.setAttribute("os", os);
        
    	return "fore/myorderPage.jsp";
    }
    
    //订单页的确认收货按钮 跳转去确认信息
    public String confirmPay(HttpServletRequest request, HttpServletResponse response, Page page) {
        int oid = Integer.parseInt(request.getParameter("oid"));
        
        Order o = orderDAO.get(oid);
        orderItemDAO.fill(o);
        
        for (OrderItem oi : o.getOrderItems()) {
			productDAO.setFirstProductImage(oi.getProduct());
		}
        
        request.setAttribute("o", o);
        
        return "fore/confirmPage.jsp";       
    }
    
    //确认收货
    public String orderfinished(HttpServletRequest request, HttpServletResponse response, Page page) {
        int oid = Integer.parseInt(request.getParameter("oid"));
        
        Order o = orderDAO.get(oid);
        o.setStatus(OrderDAO.waitReview);
        o.setConfirmDate(new Date());
        orderDAO.update(o);
        
        return "fore/orderFinishedPage.jsp";
    }
    
    //跳转到评价订单
	public String review(HttpServletRequest request, HttpServletResponse response, Page page) {
		int oid = Integer.parseInt(request.getParameter("oid"));
		
		Order o = orderDAO.get(oid);
	    orderItemDAO.fill(o);
	    Product p = o.getOrderItems().get(0).getProduct();
	    productDAO.setFirstProductImage(p);
	    List<Review> reviews = reviewDAO.list(p.getId());
	    productDAO.setSaleAndReviewNumber(p);
	    
	    request.setAttribute("p", p);
	    request.setAttribute("o", o);
	    request.setAttribute("reviews", reviews);
		
		return "fore/reviewPage.jsp";
	}
	
	//提交评价
	public String doreview(HttpServletRequest request, HttpServletResponse response, Page page) {
		User user =(User) request.getSession().getAttribute("user");
		int oid = Integer.parseInt(request.getParameter("oid"));
		int pid = Integer.parseInt(request.getParameter("pid"));

		Order o = orderDAO.get(oid);
	    o.setStatus(OrderDAO.finish);
	    orderDAO.update(o);
	    
	    Product p = productDAO.get(pid);
	    String content = request.getParameter("content");
	    content = HtmlUtils.htmlEscape(content);
	    Review review = new Review();
	    review.setContent(content);
	    review.setProduct(p);
	    review.setCreateDate(new Date());
	    review.setUser(user);
	    reviewDAO.add(review);
	     
	    return "@forereview?oid="+oid+"&showonly=true";   
	}
	
	//删除订单
	public String deleteOrder(HttpServletRequest request, HttpServletResponse response, Page page){
	    int oid = Integer.parseInt(request.getParameter("oid"));
	    
	    Order o = orderDAO.get(oid);
	    o.setStatus(OrderDAO.delete);
	    orderDAO.update(o);
	    
	    return "%success";     
	}
}
