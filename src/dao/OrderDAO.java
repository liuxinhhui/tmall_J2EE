package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import bean.Order;
import bean.User;
import util.DBUtil;
import util.DateUtil;

/*
 * ����DAO
 */
public class OrderDAO {
	public static final String waitPay = "waitPay";
	public static final String waitDelivery = "waitDelivery";
	public static final String waitConfirm = "waitConfirm";
	public static final String waitReview = "waitReview";
	public static final String finish = "finish";
	public static final String delete = "delete";
	
	/*
	 * ����ʱ��bean����û��idֵ��ֻ�в������ݿ�֮�����ݿ��Զ�����֮���ٻ�ȡidֵ��ֵ��bean��
	 */
	public void add(Order bean){
		String sql = "insert into order_ values(null,?,?,?,?,?,?,?,?,?,?,?,?)";
		try(Connection conn = DBUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql);) {

			ps.setString(1, bean.getOrderCode());
			ps.setString(2, bean.getAddress());
			ps.setString(3, bean.getPost());
			ps.setString(4, bean.getReceiver());
			ps.setString(5, bean.getMobile());
			ps.setString(6, bean.getUserMessage());
			ps.setTimestamp(7, DateUtil.d2t(bean.getCreateDate()));
			ps.setTimestamp(8, DateUtil.d2t(bean.getPayDate()));
			ps.setTimestamp(9, DateUtil.d2t(bean.getDeliveryDate()));
			ps.setTimestamp(10, DateUtil.d2t(bean.getConfirmDate()));
			ps.setString(11, bean.getStatus());
			ps.setInt(12, bean.getUser().getId());
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
	public void update(Order bean) {
		String sql = "update order_ set orderCode = ?, address = ?, post = ?, receiver = ?, mobile = ?,"+
	"userMessage = ?, createDate = ?, payDate = ?, deliveryDate = ?, confirmDate = ?, status = ?, uid = ? where id = ?";
		try (Connection conn = DBUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

			ps.setString(1, bean.getOrderCode());
			ps.setString(2, bean.getAddress());
			ps.setString(3, bean.getPost());
			ps.setString(4, bean.getReceiver());
			ps.setString(5, bean.getMobile());
			ps.setString(6, bean.getUserMessage());
			ps.setTimestamp(7, DateUtil.d2t(bean.getCreateDate()));
			ps.setTimestamp(8, DateUtil.d2t(bean.getPayDate()));
			ps.setTimestamp(9, DateUtil.d2t(bean.getDeliveryDate()));
			ps.setTimestamp(10, DateUtil.d2t(bean.getConfirmDate()));
			ps.setString(11, bean.getStatus());
			ps.setInt(12, bean.getUser().getId());
			ps.setInt(13, bean.getId());
			ps.execute();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// ����idɾ�����ݱ��е�����
	public void delete(int id) {
		try (Connection conn = DBUtil.getConnection(); Statement s = conn.createStatement()) {

			String sql = "delete from order_ where id = " + id;
			s.execute(sql);

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// ����id
	public Order get(int id) {
		Order bean = null;

		try (Connection conn = DBUtil.getConnection(); Statement s = conn.createStatement()) {

			String sql = "select * from order_ where id = " + id;
			ResultSet rs = s.executeQuery(sql);

			if (rs.next()) {
				UserDAO userDAO = new UserDAO();
				User user = userDAO.get(rs.getInt("uid"));
				
				bean = new Order();
				bean.setId(rs.getInt("id"));
				bean.setOrderCode(rs.getString("orderCode"));
				bean.setAddress(rs.getString("address"));
				bean.setPost(rs.getString("post"));
				bean.setReceiver(rs.getString("receiver"));
				bean.setMobile(rs.getString("mobile"));
				bean.setUserMessage(rs.getString("userMessage"));
				bean.setCreateDate(DateUtil.t2d(rs.getTimestamp("createDate")));
				bean.setPayDate(DateUtil.t2d(rs.getTimestamp("payDate")));
				bean.setDeliveryDate(DateUtil.t2d(rs.getTimestamp("deliveryDate")));
				bean.setConfirmDate(DateUtil.t2d(rs.getTimestamp("confirmDate")));
				bean.setStatus(rs.getString("status"));
				bean.setUser(user);
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return bean;
	}

	// ��Χ����start<=id<=end
	public ArrayList<Order> list(int start, int end) {
		ArrayList<Order> beans = new ArrayList<>();
//		String sql = "select * from order_ where id>=? and id<=?";
		String sql = "select * from order_ order by id desc limit ?,?";
		try (Connection conn = DBUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

			ps.setInt(1, start);
			ps.setInt(2, end);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				UserDAO userDAO = new UserDAO();
				User user = userDAO.get(rs.getInt("uid"));
				
				Order bean = new Order();
				bean.setId(rs.getInt("id"));
				bean.setOrderCode(rs.getString("orderCode"));
				bean.setAddress(rs.getString("address"));
				bean.setPost(rs.getString("post"));
				bean.setReceiver(rs.getString("receiver"));
				bean.setMobile(rs.getString("mobile"));
				bean.setUserMessage(rs.getString("userMessage"));
				bean.setCreateDate(DateUtil.t2d(rs.getTimestamp("createDate")));
				bean.setPayDate(DateUtil.t2d(rs.getTimestamp("payDate")));
				bean.setDeliveryDate(DateUtil.t2d(rs.getTimestamp("deliveryDate")));
				bean.setConfirmDate(DateUtil.t2d(rs.getTimestamp("confirmDate")));
				bean.setStatus(rs.getString("status"));
				bean.setUser(user);
				
				beans.add(bean);
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return beans;
	}

	// ��������
	public ArrayList<Order> list() {
		return list(0, Short.MAX_VALUE);
	}

	// ����һ���ж���������
	public int getTotal() {
		int total = 0;

		try (Connection conn = DBUtil.getConnection(); Statement s = conn.createStatement()) {

			String sql = "select count(*) from order_";
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
		OrderDAO dao = new OrderDAO();
		UserDAO udao = new UserDAO();

//		����
		User u = udao.get(1);
		
		Order bean = new Order();
		bean.setOrderCode("23517863217836");
		bean.setAddress("���пƼ���ѧ��������");
		bean.setPost("441003");
		bean.setReceiver("mimof");
		bean.setMobile("18888888888");
		bean.setUserMessage("�뾡���ʹ�");
		bean.setCreateDate(new Date());
		bean.setStatus("waitDelivery");
		bean.setUser(u);
		dao.add(bean);

//		��ѯ
		ArrayList<Order> beans = dao.list();
		Iterator<Order> itr = beans.iterator();
		while (itr.hasNext()) {
			Order i = itr.next();
			System.out.println(i.getOrderCode());
			System.out.println(i.getAddress());
			System.out.println(i.getPost());
			System.out.println(i.getReceiver());
			System.out.println(i.getMobile());
			System.out.println(i.getUserMessage());
			System.out.println(i.getCreateDate());
			System.out.println(i.getPayDate());
			System.out.println(i.getDeliveryDate());
			System.out.println(i.getConfirmDate());
			System.out.println(i.getStatus());
			System.out.println(i.getUser().getId());
		}

//		�޸�
//		Order bean = dao.get(1);
//		bean.setOrderCode("23517863217836");
//		bean.setAddress("���пƼ���ѧ��������");
//		bean.setPost("441003");
//		bean.setReceiver("mimof");
//		bean.setMobile("18888888888");
//		bean.setUserMessage("�뾡���ʹ�");
//		bean.setCreateDate(new Date());
//		bean.setPayDate(new Date());
//		bean.setStatus("������");
//		bean.setUser(u);
//		dao.update(bean);

//		ɾ��
//		dao.delete(bean.getId());

//		����
//		System.out.println(dao.getTotal());
	}
	
	//��CRUD
	
	// ��Χ���� ĳ���û�����ĳ��״̬�����ж��� start<=id<=end
	public ArrayList<Order> list(int uid, String excludedStatus, int start, int count) {
		ArrayList<Order> beans = new ArrayList<>();
		String sql = "select * from order_ where uid = ? and status != ? order by id desc limit ?,?";
		try (Connection conn = DBUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

			ps.setInt(1, uid);
			ps.setString(2, excludedStatus);
			ps.setInt(3, start);
			ps.setInt(4, count);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				UserDAO userDAO = new UserDAO();
				User user = userDAO.get(rs.getInt("uid"));
				
				Order bean = new Order();
				bean.setId(rs.getInt("id"));
				bean.setOrderCode(rs.getString("orderCode"));
				bean.setAddress(rs.getString("address"));
				bean.setPost(rs.getString("post"));
				bean.setReceiver(rs.getString("receiver"));
				bean.setMobile(rs.getString("mobile"));
				bean.setUserMessage(rs.getString("userMessage"));
				bean.setCreateDate(DateUtil.t2d(rs.getTimestamp("createDate")));
				bean.setPayDate(DateUtil.t2d(rs.getTimestamp("payDate")));
				bean.setDeliveryDate(DateUtil.t2d(rs.getTimestamp("deliveryDate")));
				bean.setConfirmDate(DateUtil.t2d(rs.getTimestamp("confirmDate")));
				bean.setStatus(rs.getString("status"));
				bean.setUser(user);
				
				beans.add(bean);
				
				System.out.println(bean.getId());
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return beans;
	}

	// ��������
	public ArrayList<Order> list(int uid, String excludedStatus) {
		return list(uid, excludedStatus, 0, Short.MAX_VALUE);
	}
	
}
