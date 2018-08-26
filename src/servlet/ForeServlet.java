package servlet;

import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
    
    //�����½�Ĺ���
    
    //ע��
    public String register(HttpServletRequest request, HttpServletResponse response, Page page) {
    	String name = request.getParameter("name");
        String password = request.getParameter("password");
        name = HtmlUtils.htmlEscape(name);
        boolean exist = userDAO.isExist(name);
         
        if(exist){
            request.setAttribute("msg", "�û����Ѿ���ʹ��,����ʹ��");
            return "fore/registerPage.jsp"; 
        }
         
        User user = new User();
        user.setName(name);
        user.setPassword(password);
        userDAO.add(user);
         
        return "@fore/registerSuccessPage.jsp"; 
    }
    
    //��½
    public String login(HttpServletRequest request, HttpServletResponse response, Page page) {
    	String name = request.getParameter("name");
    	String password = request.getParameter("password");
        name = HtmlUtils.htmlEscape(name);
        
        User user = userDAO.get(name, password);
        
        if(null == user){
        	request.setAttribute("msg", "�˺��������");
            return "fore/loginPage.jsp";
        }
        
        request.getSession().setAttribute("user", user);
        return "@forehome";	//���ﲻ���÷������ת �������ת�Ļ� ֻ��ֱ����ת��ĳ��ҳ�棬��Ϊ�����ٽ���һ�߹��˺�BaseForeServlet��
    }
    
    //�˳�
    public String logout(HttpServletRequest request, HttpServletResponse response, Page page) {
    	request.getSession().removeAttribute("user");
        return "@forehome";
    }
    
    //��Ʒҳ
    public String product(HttpServletRequest request, HttpServletResponse response, Page page) {
    	int pid = Integer.parseInt(request.getParameter("pid"));
    	
    	//��Ʒʵ�� ����չʾͼƬ�ͻ�����Ϣ
    	Product p = productDAO.get(pid);
    	productDAO.setFirstProductImage(p);
    	productDAO.setSaleAndReviewNumber(p);
    	List<ProductImage> productSingleImages = productImageDAO.list(p, ProductImageDAO.type_single);
    	List<ProductImage> productDetailImages = productImageDAO.list(p, ProductImageDAO.type_detail);
    	p.setProductSingleImages(productSingleImages);
    	p.setProductDetailImages(productDetailImages);
    	
    	//��Ʒ����ʵ��
    	List<PropertyValue> pvs = propertyValueDAO.list(p.getId());
    	
    	//��Ʒ����ʵ��
    	List<Review> reviews = reviewDAO.list(p.getId());
        
    	request.setAttribute("p", p);
    	request.setAttribute("pvs", pvs);
    	request.setAttribute("reviews", reviews);
    	
    	return "fore/productPage.jsp?pid=" + pid;
    }
    
    //����ҳ
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
    
    //����ҳ
    public String search(HttpServletRequest request, HttpServletResponse response, Page page) {
    	String keyword = request.getParameter("keyword");
    	
        List<Product> ps= new ProductDAO().search(keyword,0,20);
        productDAO.setSaleAndReviewNumber(ps);
        
        request.setAttribute("ps",ps);
    	
    	return "fore/searchResultPage.jsp";
    }
    
    //����Ƿ��½
    public String checkLogin(HttpServletRequest request, HttpServletResponse response, Page page) {
        User user =(User) request.getSession().getAttribute("user");
        if(null!=user)
            return "%success";
        return "%fail";
    }
    
    //ģ̬��½
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
    
    //�������� ֱ�Ӳ������ݿ��е� ������ ��ʱ�Ķ�����Ƕ���id=-1 ���Ƕ����û�����ɶ����ı�־
    public String buyone(HttpServletRequest request, HttpServletResponse response, Page page) {
    	int pid = Integer.parseInt(request.getParameter("pid"));
        int num = Integer.parseInt(request.getParameter("num"));
        User user =(User) request.getSession().getAttribute("user");
        
        Product p = productDAO.get(pid);
        int oiid = 0;
        boolean found = false;
        //���ǵ����û���������� ��ô��Ȼ��Ҫ�����ﳵ���ҵ���  �����Ҫ���ڹ��ﳵ��
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
            orderItemDAO.add(oi);
            oiid = oi.getId();
        }
        	
    	return "@fore/settleAccountPage.jsp?oiid=" + oiid;
    }
}
