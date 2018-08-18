package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;

import bean.Product;
import bean.ProductImage;
import util.DBUtil;

/*
 * ��ƷͼƬDAO
 */
public class ProductImageDAO {
	/*
	 * ����ʱ��bean����û��idֵ��ֻ�в������ݿ�֮�����ݿ��Զ�����֮���ٻ�ȡidֵ��ֵ��bean��
	 */
	public void add(ProductImage bean){
		String sql = "insert into ProductImage values(null,?,?)";
		try(Connection conn = DBUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql);) {

			ps.setString(1, bean.getType());
			ps.setInt(2, bean.getProduct().getId());;
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
	public void update(ProductImage bean) {
		String sql = "update productImage set type = ?, pid = ? where id = ?";
		try (Connection conn = DBUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

			ps.setString(1, bean.getType());
			ps.setInt(2, bean.getProduct().getId());
			ps.setInt(3, bean.getId());
			ps.execute();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// ����idɾ�����ݱ��е�����
	public void delete(int id) {
		try (Connection conn = DBUtil.getConnection(); Statement s = conn.createStatement()) {

			String sql = "delete from productImage where id = " + id;
			s.execute(sql);

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// ����id
	public ProductImage get(int id) {
		ProductImage bean = null;

		try (Connection conn = DBUtil.getConnection(); Statement s = conn.createStatement()) {

			String sql = "select * from productImage where id = " + id;
			ResultSet rs = s.executeQuery(sql);

			if (rs.next()) {
				ProductDAO categoryDAO = new ProductDAO();
				Product product = categoryDAO.get(rs.getInt("pid"));
				
				bean = new ProductImage();
				bean.setId(rs.getInt("id"));
				bean.setType(rs.getString("type"));
				bean.setProduct(product);
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return bean;
	}

	// ��Χ����start<=id<=end
	public ArrayList<ProductImage> list(int start, int end) {
		ArrayList<ProductImage> beans = new ArrayList<>();
		String sql = "select * from productImage where id>=? and id<=?";
		try (Connection conn = DBUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

			ps.setInt(1, start);
			ps.setInt(2, end);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				ProductDAO categoryDAO = new ProductDAO();
				Product product = categoryDAO.get(rs.getInt("pid"));
				
				ProductImage bean = new ProductImage();
				bean.setId(rs.getInt("id"));
				bean.setType(rs.getString("type"));
				bean.setProduct(product);
				
				beans.add(bean);
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return beans;
	}

	// ��������
	public ArrayList<ProductImage> list() {
		return list(0, Short.MAX_VALUE);
	}

	// ����һ���ж���������
	public int getTotal() {
		int total = 0;

		try (Connection conn = DBUtil.getConnection(); Statement s = conn.createStatement()) {

			String sql = "select count(*) from productImage";
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
		ProductImageDAO dao = new ProductImageDAO();
		ProductDAO pdao = new ProductDAO();

//		����
		Product p = pdao.get(1);
		ProductImage bean = new ProductImage();
		bean.setType("չʾͼƬ");
		bean.setProduct(p);
		dao.add(bean);

//		��ѯ
		ArrayList<ProductImage> beans = dao.list();
		Iterator<ProductImage> itr = beans.iterator();
		while (itr.hasNext()) {
			ProductImage i = itr.next();
			System.out.println(i.getType());
			System.out.println(i.getProduct().getId());
		}

//		�޸�
//		ProductImage bean = dao.get(1);
//		bean.setType("����ͼƬ");
//		bean.setProduct(p);
//		dao.update(bean);

//		ɾ��
		dao.delete(bean.getId());

//		����
		System.out.println(dao.getTotal());
	}
}
