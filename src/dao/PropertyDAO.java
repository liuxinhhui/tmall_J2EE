package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;

import bean.Category;
import bean.Property;
import util.DBUtil;

/*
 * ����DAO��
 */
public class PropertyDAO {
	/*
	 * ����ʱ��bean����û��idֵ��ֻ�в������ݿ�֮�����ݿ��Զ�����֮���ٻ�ȡidֵ��ֵ��bean��
	 */
	public void add(Property bean){
		String sql = "insert into property values(null,?,?)";
		try(Connection conn = DBUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql);) {

			ps.setInt(1, bean.getCategory().getId());	//������Ҫע�� ��һ����cid �ڶ�����name ������������
			ps.setString(2, bean.getName());
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
	public void update(Property bean) {
		String sql = "update property set cid = ?,name = ? where id = ?";
		try (Connection conn = DBUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

			ps.setInt(1, bean.getCategory().getId());
			ps.setString(2, bean.getName());
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

			String sql = "delete from property where id = " + id;
			s.execute(sql);

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// ����id
	public Property get(int id) {
		Property bean = null;

		try (Connection conn = DBUtil.getConnection(); Statement s = conn.createStatement()) {

			String sql = "select * from property where id = " + id;
			ResultSet rs = s.executeQuery(sql);

			if (rs.next()) {
				CategoryDAO categoryDAO = new CategoryDAO();
				Category category = categoryDAO.get(rs.getInt("cid"));
				
				bean = new Property();
				bean.setId(rs.getInt("id"));
				bean.setCategory(category);
				bean.setName(rs.getString("name"));
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return bean;
	}

	// ��Χ����start<=id<=end
	public ArrayList<Property> list(int start, int end) {
		ArrayList<Property> beans = new ArrayList<>();
//		String sql = "select * from property where id>=? and id<=?";
		String sql = "select * from property order by id desc limit ?,?";
		try (Connection conn = DBUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

			ps.setInt(1, start);
			ps.setInt(2, end);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				CategoryDAO categoryDAO = new CategoryDAO();
				Category category = categoryDAO.get(rs.getInt("cid"));
				
				Property bean = new Property();
				bean.setId(rs.getInt("id"));
				bean.setName(rs.getString("name"));
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
	public ArrayList<Property> list() {
		return list(0, Short.MAX_VALUE);
	}

	// ����һ���ж���������
	public int getTotal() {
		int total = 0;

		try (Connection conn = DBUtil.getConnection(); Statement s = conn.createStatement()) {

			String sql = "select count(*) from property";
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
		PropertyDAO dao = new PropertyDAO();
//		CategoryDAO cdao = new CategoryDAO();

//		����
//		Category c = cdao.get(2);
//		Property bean = new Property();
//		bean.setName("����");
//		bean.setCategory(c);
//		dao.add(bean);

//		��ѯ
		ArrayList<Property> beans = dao.list(10);
		Iterator<Property> itr = beans.iterator();
		while (itr.hasNext()) {
			Property i = itr.next();
			System.out.println(i.getId());
			System.out.println(i.getName());
			System.out.println(i.getCategory().getId());
		}

//		�޸�
//		Property bean = dao.get(1);
//		bean.setName("����");
//		bean.setCategory(c);
//		dao.update(bean);

//		ɾ��
//		dao.delete(bean.getId());

//		����
//		System.out.println(dao.getTotal());
	}
	
	//���²��ǳ���� CRUD�� Ӧ��д��service��
	
	//����ĳ�������µ�����
	public ArrayList<Property> list(int cid, int start, int end){
		ArrayList<Property> beans = new ArrayList<>();
		String sql = "select * from property where cid = ? order by id desc limit ?,?";
		try (Connection conn = DBUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

			ps.setInt(1, cid);
			ps.setInt(2, start);
			ps.setInt(3, end);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				CategoryDAO categoryDAO = new CategoryDAO();
				Category category = categoryDAO.get(rs.getInt("cid"));
				
				Property bean = new Property();
				bean.setId(rs.getInt("id"));
				bean.setName(rs.getString("name"));
				bean.setCategory(category);
				beans.add(bean);
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return beans;
	}
	//ĳ�����������е�����
	public ArrayList<Property> list(int cid){
		return list(cid,0,Short.MAX_VALUE);
	}
}
