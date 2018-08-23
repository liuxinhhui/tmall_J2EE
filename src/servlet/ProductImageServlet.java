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

import bean.Product;
import bean.ProductImage;
import dao.ProductDAO;
import dao.ProductImageDAO;
import util.ImageUtil;
import util.Page;

public class ProductImageServlet extends BaseBackServlet{
	ProductDAO productDAO = new ProductDAO();
	ProductImageDAO productImageDAO = new ProductImageDAO();
	
	public String list(HttpServletRequest request, HttpServletResponse response, Page page){
		int pid = Integer.parseInt(request.getParameter("pid"));
		
		Product p = productDAO.get(pid);
		ArrayList<ProductImage> pisSingle = (ArrayList<ProductImage>) productImageDAO.list(p,ProductImageDAO.type_single);
		ArrayList<ProductImage> pisDetail = (ArrayList<ProductImage>) productImageDAO.list(p,ProductImageDAO.type_detail);
		
		request.setAttribute("p", p);
		request.setAttribute("pisSingle", pisSingle);
		request.setAttribute("pisDetail", pisDetail);
		
		return "admin/listProductImage.jsp";
	}
	
	public String add(HttpServletRequest request, HttpServletResponse response, Page page){
        Map<String,String> params = new HashMap<>();
        InputStream is = parseUpload(request, params);	//图片
        
        String type= params.get("type");	//类型
        int pid = Integer.parseInt(params.get("pid"));	//产品id
        
        Product p =productDAO.get(pid);
        ProductImage pi = new ProductImage();      
        pi.setType(type);
        pi.setProduct(p);
        productImageDAO.add(pi);	//在数据库中添加
        
        //保存图片
        String fileName = pi.getId()+ ".jpg";
        String imageFolder;
        String imageFolder_small=null;
        String imageFolder_middle=null;
        if(ProductImageDAO.type_single.equals(pi.getType())){
            imageFolder= request.getSession().getServletContext().getRealPath("img/productSingle");
            imageFolder_small= request.getSession().getServletContext().getRealPath("img/productSingle_small");
            imageFolder_middle= request.getSession().getServletContext().getRealPath("img/productSingle_middle");
        }
        else{
            imageFolder= request.getSession().getServletContext().getRealPath("img/productDetail");
        }
        File f = new File(imageFolder, fileName);
        f.getParentFile().mkdirs();
        
        try {
            if(null!=is && 0!=is.available()){
                FileOutputStream fos = new FileOutputStream(f);
                byte b[] = new byte[1024 * 1024];
                int length = 0;
                while (-1 != (length = is.read(b))) {
                    fos.write(b, 0, length);
                }
                fos.flush();
                //通过如下代码，把文件保存为jpg格式
                BufferedImage img = ImageUtil.change2jpg(f);
                ImageIO.write(img, "jpg", f);      
                     
                if(ProductImageDAO.type_single.equals(pi.getType())){
                    File f_small = new File(imageFolder_small, fileName);
                    File f_middle = new File(imageFolder_middle, fileName);
 
                    ImageUtil.resizeImage(f, 56, 56, f_small);	//如果是用于展示的图片 另外保存两份
                    ImageUtil.resizeImage(f, 217, 190, f_middle);
                }
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
		return "@admin_productImage_list?pid=" + pid;
	}
	
	public String delete(HttpServletRequest request, HttpServletResponse response, Page page){
		//删除数据库中数据
		int id = Integer.parseInt(request.getParameter("id"));
        ProductImage pi = productImageDAO.get(id);
        productImageDAO.delete(id);
        
        //删除图片
        if(ProductImageDAO.type_single.equals(pi.getType())){
            String imageFolder_single= request.getSession().getServletContext().getRealPath("img/productSingle");
            String imageFolder_small= request.getSession().getServletContext().getRealPath("img/productSingle_small");
            String imageFolder_middle= request.getSession().getServletContext().getRealPath("img/productSingle_middle");
             
            File f_single =new File(imageFolder_single,pi.getId()+".jpg");
            f_single.delete();
            File f_small =new File(imageFolder_small,pi.getId()+".jpg");
            f_small.delete();
            File f_middle =new File(imageFolder_middle,pi.getId()+".jpg");
            f_middle.delete();
             
        }
        else{
            String imageFolder_detail= request.getSession().getServletContext().getRealPath("img/productDetail");
            File f_detail =new File(imageFolder_detail,pi.getId()+".jpg");
            f_detail.delete();         
        }
        
		return "@admin_productImage_list?pid=" + pi.getProduct().getId();
	}
}
