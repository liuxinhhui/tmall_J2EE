package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;

import bean.Category;
import util.DBUtil;

/*
 * ����DAO��
 */
public class CategoryDAO {
	
	/*
	 * ����ʱ��bean����û��idֵ��ֻ�в������ݿ�֮�����ݿ��Զ�����֮���ٻ�ȡidֵ��ֵ��bean��
	 */
	public void add(Category bean){
		String sql = "insert into category values(null,?)";
		try(Connection conn = DBUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql);) {

			ps.setString(1, bean.getName());
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
	public void update(Category bean) {
		String sql = "update category set name = ? where id = ?";
		try (Connection conn = DBUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

			ps.setString(1, bean.getName());
			ps.setInt(2, bean.getId());
			ps.execute();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// ����idɾ�����ݱ��е�����
	public void delete(int id) {
		try (Connection conn = DBUtil.getConnection(); Statement s = conn.createStatement()) {

			String sql = "delete from category where id = " + id;
			s.execute(sql);

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// ����id
	public Category get(int id) {
		Category bean = null;

		try (Connection conn = DBUtil.getConnection(); Statement s = conn.createStatement()) {

			String sql = "select * from category where id = " + id;
			ResultSet rs = s.executeQuery(sql);

			if (rs.next()) {
				bean = new Category();
				bean.setId(rs.getInt(1));
				bean.setName(rs.getString(2));
				// bean.setName(rs.getString("name"));
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return bean;
	}

	// ��Χ����start<=id<=end
	public ArrayList<Category> list(int start, int end) {
		ArrayList<Category> beans = new ArrayList<>();
//		String sql = "select * from category where id>=? and id<=?";
		String sql = "select * from category order by id desc limit ?,?";
		try (Connection conn = DBUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

			ps.setInt(1, start);
			ps.setInt(2, end);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				Category bean = new Category();
				bean.setId(rs.getInt("id"));
				bean.setName(rs.getString("name"));
				beans.add(bean);
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return beans;
	}

	// ��������
	public ArrayList<Category> list() {
		return list(0, Short.MAX_VALUE);
	}

	// ����һ���ж���������
	public int getTotal() {
		int total = 0;

		try (Connection conn = DBUtil.getConnection(); Statement s = conn.createStatement()) {

			String sql = "select count(*) from category";
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
		CategoryDAO dao = new CategoryDAO();

//		����
		Category bean = new Category();
		bean.setName("ƽ�⳵");
		dao.add(bean);

//		��ѯ
//		Category bean = dao.get(1);
//		System.out.println(bean.getId());
//		System.out.println(bean.getName());
		ArrayList<Category> beans = dao.list();
		Iterator<Category> itr = beans.iterator();
		while (itr.hasNext()) {
			Category i = itr.next();
			System.out.println(i.getId());
			System.out.println(i.getName());
		}

//		�޸�
//		bean.setName("ɽ�س�");
//		dao.update(bean);

//		ɾ��
//		dao.delete(bean.getId());

//		����
//		System.out.println(dao.getTotal());
	}
}
