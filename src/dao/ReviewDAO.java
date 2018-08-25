package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import bean.Product;
import bean.Review;
import bean.User;
import util.DBUtil;
import util.DateUtil;

/*
 * 评价DAO
 */
public class ReviewDAO {
	/*
	 * 插入时，bean里面没有id值。只有插入数据库之后，数据库自动生成之后，再获取id值赋值到bean上
	 */
	public void add(Review bean){
		String sql = "insert into review values(null,?,?,?,?)";
		try(Connection conn = DBUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql);) {

			ps.setString(1, bean.getContent());
			ps.setTimestamp(2,DateUtil.d2t(bean.getCreateDate()));
			ps.setInt(3, bean.getUser().getId());
			ps.setInt(4, bean.getProduct().getId());
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
	public void update(Review bean) {
		String sql = "update review set content = ?, createDate = ?, uid = ?, pid = ? where id = ?";
		try (Connection conn = DBUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

			ps.setString(1, bean.getContent());
			ps.setTimestamp(2,DateUtil.d2t(bean.getCreateDate()));
			ps.setInt(3, bean.getUser().getId());
			ps.setInt(4, bean.getProduct().getId());
			ps.setInt(5, bean.getId());
			ps.execute();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// 根据id删除数据表中的数据
	public void delete(int id) {
		try (Connection conn = DBUtil.getConnection(); Statement s = conn.createStatement()) {

			String sql = "delete from review where id = " + id;
			s.execute(sql);

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// 查找id
	public Review get(int id) {
		Review bean = null;

		try (Connection conn = DBUtil.getConnection(); Statement s = conn.createStatement()) {

			String sql = "select * from review where id = " + id;
			ResultSet rs = s.executeQuery(sql);

			if (rs.next()) {
				UserDAO userDAO = new UserDAO();
				User user = userDAO.get(rs.getInt("uid"));
				ProductDAO categoryDAO = new ProductDAO();
				Product product = categoryDAO.get(rs.getInt("pid"));
				
				bean = new Review();
				bean.setId(rs.getInt("id"));
				bean.setContent(rs.getString("content"));
				bean.setCreateDate(DateUtil.t2d(rs.getTimestamp("createDate")));
				bean.setUser(user);
				bean.setProduct(product);
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return bean;
	}

	// 范围查找start<=id<=end
	public ArrayList<Review> list(int start, int end) {
		ArrayList<Review> beans = new ArrayList<>();
		String sql = "select * from review where id>=? and id<=?";
		try (Connection conn = DBUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

			ps.setInt(1, start);
			ps.setInt(2, end);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				UserDAO userDAO = new UserDAO();
				User user = userDAO.get(rs.getInt("uid"));
				ProductDAO categoryDAO = new ProductDAO();
				Product product = categoryDAO.get(rs.getInt("pid"));
				
				Review bean = new Review();
				bean.setId(rs.getInt("id"));
				bean.setContent(rs.getString("content"));
				bean.setCreateDate(DateUtil.t2d(rs.getTimestamp("createDate")));
				bean.setUser(user);
				bean.setProduct(product);
				
				beans.add(bean);
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return beans;
	}

	// 查找所有
	public ArrayList<Review> list() {
		return list(0, Short.MAX_VALUE);
	}

	// 查找一共有多少条数据
	public int getTotal() {
		int total = 0;

		try (Connection conn = DBUtil.getConnection(); Statement s = conn.createStatement()) {

			String sql = "select count(*) from review";
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
		ReviewDAO dao = new ReviewDAO();
		UserDAO udao = new UserDAO();
		ProductDAO pdao = new ProductDAO();

//		增加
		Product p = pdao.get(1);
		User u = udao.get(1);
		
		Review bean = new Review();
		bean.setContent("宝贝很好 我很喜欢");
		bean.setCreateDate(new Date());
		bean.setUser(u);
		bean.setProduct(p);
		dao.add(bean);

//		查询
		ArrayList<Review> beans = dao.list();
		Iterator<Review> itr = beans.iterator();
		while (itr.hasNext()) {
			Review i = itr.next();
			System.out.println(i.getContent());
			System.out.println(i.getCreateDate());
			System.out.println(i.getUser().getId());
			System.out.println(i.getProduct().getId());
		}

//		修改
//		Review bean = dao.get(1);
//		bean.setContent("宝贝很好 我很喜欢 超级喜欢");
//		bean.setCreateDate(new Date());
//		bean.setUser(u);
//		bean.setProduct(p);
//		dao.update(bean);

//		删除
		dao.delete(bean.getId());

//		总数
		System.out.println(dao.getTotal());
	}
	
	//非CRUD
	
	//获取一个产品的评价数量
	public int getCount(int pid) {
        String sql = "select count(*) from Review where pid = ? ";
  
        try (Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql);) {
  
            ps.setInt(1, pid);
            ResultSet rs = ps.executeQuery();
  
            while (rs.next()) {
               return rs.getInt(1);
            }
        } catch (SQLException e) {
  
            e.printStackTrace();
        }
        return 0;      
    }
	
	//获取一个产品的所有评价
	public ArrayList<Review> list(int pid) {
		ArrayList<Review> beans = new ArrayList<>();
//		String sql = "select * from review where id>=? and id<=?";
		String sql = "select * from review where pid = ? order by desc";
		try (Connection conn = DBUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

			ps.setInt(1, pid);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				UserDAO userDAO = new UserDAO();
				User user = userDAO.get(rs.getInt("uid"));
				ProductDAO categoryDAO = new ProductDAO();
				Product product = categoryDAO.get(rs.getInt("pid"));
				
				Review bean = new Review();
				bean.setId(rs.getInt("id"));
				bean.setContent(rs.getString("content"));
				bean.setCreateDate(DateUtil.t2d(rs.getTimestamp("createDate")));
				bean.setUser(user);
				bean.setProduct(product);
				
				beans.add(bean);
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return beans;
	}
}
