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
 * 属性DAO类
 */
public class PropertyDAO {
	/*
	 * 插入时，bean里面没有id值。只有插入数据库之后，数据库自动生成之后，再获取id值赋值到bean上
	 */
	public void add(Property bean){
		String sql = "insert into property values(null,?,?)";
		try(Connection conn = DBUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql);) {

			ps.setInt(1, bean.getCategory().getId());	//这里需要注意 第一个是cid 第二个是name 表是这样建的
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

	// 通过bean中的id定位，将数据库中该id的数据修改成bean中的属性值
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

	// 根据id删除数据表中的数据
	public void delete(int id) {
		try (Connection conn = DBUtil.getConnection(); Statement s = conn.createStatement()) {

			String sql = "delete from property where id = " + id;
			s.execute(sql);

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// 查找id
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

	// 范围查找start<=id<=end
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

	// 查找所有
	public ArrayList<Property> list() {
		return list(0, Short.MAX_VALUE);
	}

	// 查找一共有多少条数据
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

//		增加
//		Category c = cdao.get(2);
//		Property bean = new Property();
//		bean.setName("面料");
//		bean.setCategory(c);
//		dao.add(bean);

//		查询
		ArrayList<Property> beans = dao.list(10);
		Iterator<Property> itr = beans.iterator();
		while (itr.hasNext()) {
			Property i = itr.next();
			System.out.println(i.getId());
			System.out.println(i.getName());
			System.out.println(i.getCategory().getId());
		}

//		修改
//		Property bean = dao.get(1);
//		bean.setName("长度");
//		bean.setCategory(c);
//		dao.update(bean);

//		删除
//		dao.delete(bean.getId());

//		总数
//		System.out.println(dao.getTotal());
	}
	
	//以下不是常规的 CRUD了 应该写在service中
	
	//查找某个分类下的属性
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
	//某个分类下所有的属性
	public ArrayList<Property> list(int cid){
		return list(cid,0,Short.MAX_VALUE);
	}
}
