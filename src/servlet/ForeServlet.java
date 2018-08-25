package servlet;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.util.HtmlUtils;

import bean.Category;
import bean.User;
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
}
