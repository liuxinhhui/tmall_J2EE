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
 * ����DAO
 */
public class ReviewDAO {
	/*
	 * ����ʱ��bean����û��idֵ��ֻ�в������ݿ�֮�����ݿ��Զ�����֮���ٻ�ȡidֵ��ֵ��bean��
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

	// ͨ��bean�е�id��λ�������ݿ��и�id�������޸ĳ�bean�е�����ֵ
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

	// ����idɾ�����ݱ��е�����
	public void delete(int id) {
		try (Connection conn = DBUtil.getConnection(); Statement s = conn.createStatement()) {

			String sql = "delete from review where id = " + id;
			s.execute(sql);

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// ����id
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

	// ��Χ����start<=id<=end
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

	// ��������
	public ArrayList<Review> list() {
		return list(0, Short.MAX_VALUE);
	}

	// ����һ���ж���������
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

//		����
		Product p = pdao.get(1);
		User u = udao.get(1);
		
		Review bean = new Review();
		bean.setContent("�����ܺ� �Һ�ϲ��");
		bean.setCreateDate(new Date());
		bean.setUser(u);
		bean.setProduct(p);
		dao.add(bean);

//		��ѯ
		ArrayList<Review> beans = dao.list();
		Iterator<Review> itr = beans.iterator();
		while (itr.hasNext()) {
			Review i = itr.next();
			System.out.println(i.getContent());
			System.out.println(i.getCreateDate());
			System.out.println(i.getUser().getId());
			System.out.println(i.getProduct().getId());
		}

//		�޸�
//		Review bean = dao.get(1);
//		bean.setContent("�����ܺ� �Һ�ϲ�� ����ϲ��");
//		bean.setCreateDate(new Date());
//		bean.setUser(u);
//		bean.setProduct(p);
//		dao.update(bean);

//		ɾ��
		dao.delete(bean.getId());

//		����
		System.out.println(dao.getTotal());
	}
	
	//��CRUD
	
	//��ȡһ����Ʒ����������
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
	
	//��ȡһ����Ʒ����������
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
