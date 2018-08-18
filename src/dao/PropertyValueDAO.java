package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import bean.Category;
import bean.Product;
import bean.Property;
import bean.PropertyValue;
import util.DBUtil;
import util.DateUtil;

/*
 * ����ֵDAO
 */
public class PropertyValueDAO {
	/*
	 * ����ʱ��bean����û��idֵ��ֻ�в������ݿ�֮�����ݿ��Զ�����֮���ٻ�ȡidֵ��ֵ��bean��
	 */
	public void add(PropertyValue bean){
		String sql = "insert into propertyValue values(null,?,?,?)";
		try(Connection conn = DBUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql);) {

			ps.setString(1, bean.getValue());
			ps.setInt(2, bean.getProduct().getId());
			ps.setInt(3, bean.getProperty().getId());
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
	public void update(PropertyValue bean) {
		String sql = "update propertyValue set value = ?, pid = ?, ptid = ? where id = ?";
		try (Connection conn = DBUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

			ps.setString(1, bean.getValue());
			ps.setInt(2, bean.getProduct().getId());
			ps.setInt(3, bean.getProperty().getId());
			ps.setInt(4, bean.getId());
			ps.execute();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// ����idɾ�����ݱ��е�����
	public void delete(int id) {
		try (Connection conn = DBUtil.getConnection(); Statement s = conn.createStatement()) {

			String sql = "delete from propertyValue where id = " + id;
			s.execute(sql);

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// ����id
	public PropertyValue get(int id) {
		PropertyValue bean = null;

		try (Connection conn = DBUtil.getConnection(); Statement s = conn.createStatement()) {

			String sql = "select * from propertyValue where id = " + id;
			ResultSet rs = s.executeQuery(sql);

			if (rs.next()) {
				ProductDAO productDAO = new ProductDAO();
				Product product = productDAO.get(rs.getInt("pid"));
				PropertyDAO propertyDAO = new PropertyDAO();
				Property property = propertyDAO.get(rs.getInt("ptid"));
				
				bean = new PropertyValue();
				bean.setId(rs.getInt("id"));
				bean.setValue(rs.getString("value"));
				bean.setProduct(product);
				bean.setProperty(property);
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return bean;
	}

	// ��Χ����start<=id<=end
	public ArrayList<PropertyValue> list(int start, int end) {
		ArrayList<PropertyValue> beans = new ArrayList<>();
		String sql = "select * from propertyValue where id>=? and id<=?";
		try (Connection conn = DBUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

			ps.setInt(1, start);
			ps.setInt(2, end);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				ProductDAO productDAO = new ProductDAO();
				Product product = productDAO.get(rs.getInt("pid"));
				PropertyDAO propertyDAO = new PropertyDAO();
				Property property = propertyDAO.get(rs.getInt("ptid"));
				
				PropertyValue bean = new PropertyValue();
				bean = new PropertyValue();
				bean.setId(rs.getInt("id"));
				bean.setValue(rs.getString("value"));
				bean.setProduct(product);
				bean.setProperty(property);
				
				beans.add(bean);
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return beans;
	}

	// ��������
	public ArrayList<PropertyValue> list() {
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
		PropertyValueDAO dao = new PropertyValueDAO();
		ProductDAO pdao = new ProductDAO();
		PropertyDAO ptdao = new PropertyDAO();
		

//		����
		Product p = pdao.get(1);
		Property pt = ptdao.get(1);
		
		PropertyValue bean = new PropertyValue();
		bean.setValue("2��");
		bean.setProduct(p);
		bean.setProperty(pt);
		dao.add(bean);

//		��ѯ
		ArrayList<PropertyValue> beans = dao.list();
		Iterator<PropertyValue> itr = beans.iterator();
		while (itr.hasNext()) {
			PropertyValue i = itr.next();
			System.out.println(i.getValue());
			System.out.println(i.getProduct().getId());
			System.out.println(i.getProperty().getId());
		}

//		�޸�
//		PropertyValue bean = dao.get(1);
//		bean.setValue("18����");
//		bean.setProduct(p);
//		bean.setProperty(pt);
//		dao.update(bean);

//		ɾ��
		dao.delete(bean.getId());

//		����
		System.out.println(dao.getTotal());
	}
}
