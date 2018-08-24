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
 * ��ƷDAO
 */
public class ProductDAO {
	/*
	 * ����ʱ��bean����û��idֵ��ֻ�в������ݿ�֮�����ݿ��Զ�����֮���ٻ�ȡidֵ��ֵ��bean��
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

	// ͨ��bean�е�id��λ�������ݿ��и�id�������޸ĳ�bean�е�����ֵ
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

	// ����idɾ�����ݱ��е�����
	public void delete(int id) {
		try (Connection conn = DBUtil.getConnection(); Statement s = conn.createStatement()) {

			String sql = "delete from product where id = " + id;
			s.execute(sql);

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// ����id
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

	// ��Χ����start<=id<=end
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

	// ��������
	public ArrayList<Product> list() {
		return list(0, Short.MAX_VALUE);
	}

	// ����һ���ж���������
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

//		����
		Category c = cdao.get(2);
		Product bean = new Product();
		bean.setName("������ֳ�");
		bean.setSubTitle("���ٳ�� ������ ��ȫ����");
		bean.setOriginalPrice(3888.8f);
		bean.setPromotePrice(199.9f);
		bean.setStock(200);
		bean.setCreateDate(DateUtil.d2t(new Date()));
		bean.setCategory(c);
		dao.add(bean);

//		��ѯ
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

//		�޸�
//		Product bean = dao.get(1);
//		bean.setName("������ֳ�2");
//		bean.setSubTitle("���ٳ�� ������ ��ȫ����2");
//		bean.setOriginalPrice(38888.8f);
//		bean.setPromotePrice(199.9f);
//		bean.setStock(210);
//		bean.setCreateDate(DateUtil.d2t(new Date()));
//		bean.setCategory(c);
//		dao.update(bean);

//		ɾ��
		dao.delete(bean.getId());

//		����
		System.out.println(dao.getTotal());
	}
	
	//���²��ǳ���� CRUD�� Ӧ��д��service��
	
	//����ĳ�������µĲ�Ʒ
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
	
	// ĳһ�����µĲ�Ʒ����
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
	
	// ���ĳһ����������в�Ʒ
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
	
	//��һά��Ʒ��ɶ�ά�� ������ʾ
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
	
	//���ò�Ʒ����ͼƬ
	public void setFirstProductImage(Product p) {
        List<ProductImage> pis= new ProductImageDAO().list(p, ProductImageDAO.type_single);
        if(!pis.isEmpty())
            p.setFirstProductImage(pis.get(0));    
    }
}
