package servlet;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bean.Category;
import dao.CategoryDAO;
import util.ImageUtil;
import util.Page;

public class CategoryServlet extends BaseBackServlet {
	
	CategoryDAO categoryDAO = new CategoryDAO();
	
	public String list(HttpServletRequest request, HttpServletResponse response, Page page){
		ArrayList<Category> thecs = categoryDAO.list(page.getStart(),page.getCount());
		int total = categoryDAO.getTotal();
		
		page.setTotal(total);

		request.setAttribute("thecs", thecs);
		request.setAttribute("page", page);
		
		return "admin/listCategory.jsp";
	}
	
	public String add(HttpServletRequest request, HttpServletResponse response, Page page){
		Map<String,String> params = new HashMap<>();
		InputStream is = super.parseUpload(request, params);
		
		String name= params.get("name");
	    Category c = new Category();
	    c.setName(name);
	    categoryDAO.add(c);
	    
	    File imageFolder= new File(request.getSession().getServletContext().getRealPath("img/category"));
	    File file = new File(imageFolder,c.getId()+".jpg");
	    
	    try {
			if(null != is && 0 != is.available()){
				FileOutputStream fos = new FileOutputStream(file);
				byte b[] = new byte[1024 * 1024];
				int length = 0;
				while(-1 != (length=is.read(b))){
					fos.write(b,0,length);
				}
				fos.flush();
				//通过如下代码，把文件保存为jpg格式
                BufferedImage img = ImageUtil.change2jpg(file);
                ImageIO.write(img, "jpg", file); 
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return "@admin_category_list";
	}
	
	public String delete(HttpServletRequest request, HttpServletResponse response, Page page){
		int id = Integer.parseInt(request.getParameter("id"));
		categoryDAO.delete(id);
		
		File imageFolder= new File(request.getSession().getServletContext().getRealPath("img/category"));
		File file = new File(imageFolder,id+".jpg");
		file.delete();
		
		return "@admin_category_list";
	}
	
	public String edit(HttpServletRequest request, HttpServletResponse response, Page page) {
	    int id = Integer.parseInt(request.getParameter("id"));
	    
	    Category c = categoryDAO.get(id);
	    
	    request.setAttribute("c", c);
	    
	    return "admin/editCategory.jsp";       
	}
	
	public String update (HttpServletRequest request, HttpServletResponse response, Page page) {
		Map<String,String> params = new HashMap<>();
	    InputStream is = super.parseUpload(request, params);
	    int id = Integer.parseInt(params.get("id"));
	    String name= params.get("name");
		
		Category c = new Category();
		c.setId(id);
		c.setName(name);
		categoryDAO.update(c);
		
		File imageFolder= new File(request.getSession().getServletContext().getRealPath("img/category"));
		File file = new File(imageFolder,c.getId()+".jpg");
		
		try {
			if(null != is && 0 != is.available()){
				FileOutputStream fos = new FileOutputStream(file);
				byte b[] = new byte[1024 * 1024];
				int length = 0;
				while(-1 != (length=is.read(b))){
					fos.write(b,0,length);
				}
				fos.flush();
				//通过如下代码，把文件保存为jpg格式
		        BufferedImage img = ImageUtil.change2jpg(file);
		        ImageIO.write(img, "jpg", file); 
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return "@admin_category_list";
	}
}
