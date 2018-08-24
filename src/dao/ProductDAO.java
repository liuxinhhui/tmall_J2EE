package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import bean.Category;
import bean.Product;
import bean.ProductImage;
import util.DBUtil;
import util.DateUtil;

/*
 * 产品DAO
 */
public class ProductDAO {
	/*
	 * 插入时，bean里面没有id值。只有插入数据库之后，数据库自动生成之后，再获取id值赋值到bean上
	 */
	public void add(Product bean){
		String sql = "insert into product values(null,?,?,?,?,?,?,?)";
		try(Connection conn = DBUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql);) {

			ps.setString(1, bean.getName());
			ps.setString(2, bean.getSubTitle());
			ps.setFloat(3, bean.getOriginalPrice());
			ps.setFloat(4, bean.getPromotePrice());
			ps.setInt(5, bean.getStock());
			ps.setTimestamp(6, DateUtil.d2t(bean.getCreateDate()));
			ps.setInt(7, bean.getCategory().getId());
			ps.execute();

			ResultSet rs = ps.getGeneratedKeys();
			if (rs.next()) {
				int id = rs.getInt(1);
				bean.setId(id);
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// 通过bean中的id定位，将数据库中该id的数据修改成bean中的属性值
	public void update(Product bean) {
		String sql = "update product set name = ?, subTitle = ?, originalPrice = ?, promotePrice = ?,"
				+ "stock = ?, createDate = ?, cid = ? where id = ?";
		try (Connection conn = DBUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

			ps.setString(1, bean.getName());
			ps.setString(2, bean.getSubTitle());
			ps.setFloat(3, bean.getOriginalPrice());
			ps.setFloat(4, bean.getPromotePrice());
			ps.setInt(5, bean.getStock());
			ps.setTimestamp(6, DateUtil.d2t(bean.getCreateDate()));
			ps.setInt(7, bean.getCategory().getId());
			ps.setInt(8, bean.getId());
			ps.execute();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// 根据id删除数据表中的数据
	public void delete(int id) {
		try (Connection conn = DBUtil.getConnection(); Statement s = conn.createStatement()) {

			String sql = "delete from product where id = " + id;
			s.execute(sql);

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// 查找id
	public Product get(int id) {
		Product bean = null;

		try (Connection conn = DBUtil.getConnection(); Statement s = conn.createStatement()) {

			String sql = "select * from product where id = " + id;
			ResultSet rs = s.executeQuery(sql);

			if (rs.next()) {
				CategoryDAO categoryDAO = new CategoryDAO();
				Category category = categoryDAO.get(rs.getInt("cid"));
				
				bean = new Product();
				bean.setId(rs.getInt("id"));
				bean.setName(rs.getString("name"));
				bean.setSubTitle(rs.getString("subTitle"));
				bean.setOriginalPrice(rs.getFloat("originalPrice"));
				bean.setPromotePrice(rs.getFloat("promotePrice"));
				bean.setStock(rs.getInt("stock"));
				bean.setCreateDate(DateUtil.t2d(rs.getTimestamp("createDate")));
				bean.setCategory(category);
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return bean;
	}

	// 范围查找start<=id<=end
	public ArrayList<Product> list(int start, int end) {
		ArrayList<Product> beans = new ArrayList<>();
//		String sql = "select * from product where id>=? and id<=?";
		String sql = "select * from product order by id desc limit ?,?";
		try (Connection conn = DBUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

			ps.setInt(1, start);
			ps.setInt(2, end);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				CategoryDAO categoryDAO = new CategoryDAO();
				Category category = categoryDAO.get(rs.getInt("cid"));
				
				Product bean = new Product();
				bean.setId(rs.getInt("id"));
				bean.setName(rs.getString("name"));
				bean.setSubTitle(rs.getString("subTitle"));
				bean.setOriginalPrice(rs.getFloat("originalPrice"));
				bean.setPromotePrice(rs.getFloat("promotePrice"));
				bean.setStock(rs.getInt("stock"));
				bean.setCreateDate(DateUtil.t2d(rs.getTimestamp("createDate")));
				bean.setCategory(category);
				
				beans.add(bean);
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return beans;
	}

	// 查找所有
	public ArrayList<Product> list() {
		return list(0, Short.MAX_VALUE);
	}

	// 查找一共有多少条数据
	public int getTotal() {
		int total = 0;

		try (Connection conn = DBUtil.getConnection(); Statement s = conn.createStatement()) {

			String sql = "select count(*) from product";
			ResultSet rs = s.executeQuery(sql);

			if (rs.next()) {
				total = rs.getInt(1);
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return total;
	}

	public static void main(String[] args) {
		ProductDAO dao = new ProductDAO();
		CategoryDAO cdao = new CategoryDAO();

//		增加
		Category c = cdao.get(2);
		Product bean = new Product();
		bean.setName("艾玛二轮车");
		bean.setSubTitle("急速充电 超快起步 安全放心");
		bean.setOriginalPrice(3888.8f);
		bean.setPromotePrice(199.9f);
		bean.setStock(200);
		bean.setCreateDate(DateUtil.d2t(new Date()));
		bean.setCategory(c);
		dao.add(bean);

//		查询
		ArrayList<Product> beans = dao.list();
		Iterator<Product> itr = beans.iterator();
		while (itr.hasNext()) {
			Product i = itr.next();
			System.out.println(i.getId());
			System.out.println(i.getName());
			System.out.println(i.getSubTitle());
			System.out.println(i.getOriginalPrice());
			System.out.println(i.getPromotePrice());
			System.out.println(i.getStock());
			System.out.println(i.getCreateDate());
			System.out.println(i.getCategory().getId());
		}

//		修改
//		Product bean = dao.get(1);
//		bean.setName("艾玛二轮车2");
//		bean.setSubTitle("急速充电 超快起步 安全放心2");
//		bean.setOriginalPrice(38888.8f);
//		bean.setPromotePrice(199.9f);
//		bean.setStock(210);
//		bean.setCreateDate(DateUtil.d2t(new Date()));
//		bean.setCategory(c);
//		dao.update(bean);

//		删除
		dao.delete(bean.getId());

//		总数
		System.out.println(dao.getTotal());
	}
	
	//以下不是常规的 CRUD了 应该写在service中
	
	//查找某个分类下的产品
	public ArrayList<Product> list(int cid, int start, int end) {
		ArrayList<Product> beans = new ArrayList<>();
//		String sql = "select * from product where id>=? and id<=?";
		String sql = "select * from product where cid = ? order by id desc limit ?,?";
		try (Connection conn = DBUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

			ps.setInt(1, cid);
			ps.setInt(2, start);
			ps.setInt(3, end);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				CategoryDAO categoryDAO = new CategoryDAO();
				Category category = categoryDAO.get(rs.getInt("cid"));
				
				Product bean = new Product();
				bean.setId(rs.getInt("id"));
				bean.setName(rs.getString("name"));
				bean.setSubTitle(rs.getString("subTitle"));
				bean.setOriginalPrice(rs.getFloat("originalPrice"));
				bean.setPromotePrice(rs.getFloat("promotePrice"));
				bean.setStock(rs.getInt("stock"));
				bean.setCreateDate(DateUtil.t2d(rs.getTimestamp("createDate")));
				bean.setCategory(category);
				
				beans.add(bean);
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return beans;
	}
	
	public ArrayList<Product> list(int cid) {
		return list(cid, 0, Short.MAX_VALUE);
	}
	
	// 某一分类下的产品数量
	public int getTotal(int cid) {
		int total = 0;

		try (Connection conn = DBUtil.getConnection(); Statement s = conn.createStatement()) {

			String sql = "select count(*) from product where cid=" + cid;
			ResultSet rs = s.executeQuery(sql);

			if (rs.next()) {
				total = rs.getInt(1);
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return total;
	}
	
	// 填充某一个分类的所有产品
	public void fill(Category c){
		List<Product> ps = this.list(c.getId());
		for (Product p : ps) {
			this.setFirstProductImage(p);
		}
		c.setProducts(ps);
	}
	
	public void fill(List<Category> cs){
		for (Category c : cs) {
			fill(c);
		}
	}
	
	//把一维产品变成二维的 方便显示
	public void fillByRow(List<Category> cs){
		int productNumberEachRow = 8;
        for (Category c : cs) {
        	List<Product> products =  c.getProducts();
            List<List<Product>> productsByRow =  new ArrayList<>();
            for (int i = 0; i < products.size(); i+=productNumberEachRow) {
                int size = i+productNumberEachRow;
                size= size>products.size()?products.size():size;
                List<Product> productsOfEachRow =products.subList(i, size);
                productsByRow.add(productsOfEachRow);
            }
            c.setProductsByRow(productsByRow);
        }
	}
	
	//设置产品的主图片
	public void setFirstProductImage(Product p) {
        List<ProductImage> pis= new ProductImageDAO().list(p, ProductImageDAO.type_single);
        if(!pis.isEmpty())
            p.setFirstProductImage(pis.get(0));    
    }
}
