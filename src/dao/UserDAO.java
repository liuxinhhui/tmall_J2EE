package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;

import bean.User;
import util.DBUtil;

/*
 * �û�DAO��
 */
public class UserDAO {
	/*
	 * ����ʱ��bean����û��idֵ��ֻ�в������ݿ�֮�����ݿ��Զ�����֮���ٻ�ȡidֵ��ֵ��bean��
	 */
	public void add(User bean){
		String sql = "insert into user values(null,?,?)";
		try(Connection conn = DBUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql);) {

			ps.setString(1, bean.getName());
			ps.setString(2, bean.getPassword());
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
	public void update(User bean) {
		String sql = "update user set name = ?,password = ? where id = ?";
		try (Connection conn = DBUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

			ps.setString(1, bean.getName());
			ps.setString(2, bean.getPassword());
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

			String sql = "delete from user where id = " + id;
			s.execute(sql);

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// ����id
	public User get(int id) {
		User bean = null;

		try (Connection conn = DBUtil.getConnection(); Statement s = conn.createStatement()) {

			String sql = "select * from user where id = " + id;
			ResultSet rs = s.executeQuery(sql);

			if (rs.next()) {
				bean = new User();
				bean.setId(rs.getInt(1));
				bean.setName(rs.getString(2));
				bean.setPassword(rs.getString(3));
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return bean;
	}

	// ��Χ����start<=id<=end
	public ArrayList<User> list(int start, int end) {
		ArrayList<User> beans = new ArrayList<>();
		String sql = "select * from user where id>=? and id<=?";
		try (Connection conn = DBUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

			ps.setInt(1, start);
			ps.setInt(2, end);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				User bean = new User();
				bean.setId(rs.getInt("id"));
				bean.setName(rs.getString("name"));
				bean.setPassword(rs.getString("password"));
				beans.add(bean);
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return beans;
	}

	// ��������
	public ArrayList<User> list() {
		return list(0, Short.MAX_VALUE);
	}

	// ����һ���ж���������
	public int getTotal() {
		int total = 0;

		try (Connection conn = DBUtil.getConnection(); Statement s = conn.createStatement()) {

			String sql = "select count(*) from user";
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
		UserDAO dao = new UserDAO();

//		����
		User bean = new User();
		bean.setName("abc");
		bean.setPassword("123");
		dao.add(bean);

//		��ѯ
		ArrayList<User> beans = dao.list();
		Iterator<User> itr = beans.iterator();
		while (itr.hasNext()) {
			User i = itr.next();
			System.out.println(i.getId());
			System.out.println(i.getName());
			System.out.println(i.getPassword());
		}

//		�޸�
//		User bean = dao.get(1);
//		bean.setName("bcd");
//		bean.setPassword("234");
//		dao.update(bean);

//		ɾ��
		dao.delete(bean.getId());

//		����
		System.out.println(dao.getTotal());
	}
}
