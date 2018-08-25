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
import bean.OrderItem;
import bean.Product;
import bean.User;
import util.DBUtil;
import util.DateUtil;

/*
 * 订单项DAO
 */
public class OrderItemDAO {
	/*
	 * 插入时，bean里面没有id值。只有插入数据库之后，数据库自动生成之后，再获取id值赋值到bean上
	 */
	public void add(OrderItem bean){
		String sql = "insert into orderItem values(null,?,?,?,?)";
		try(Connection conn = DBUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql);) {

			ps.setInt(1, bean.getNumber());
			ps.setInt(2, bean.getProduct().getId());
			ps.setInt(3, bean.getOrder().getId());
			ps.setInt(4, bean.getUser().getId());
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
	public void update(OrderItem bean) {
		String sql = "update orderItem set number = ?, pid = ?, oid = ?, uid = ? where id = ?";
		try (Connection conn = DBUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

			ps.setInt(1, bean.getNumber());
			ps.setInt(2, bean.getProduct().getId());
			ps.setInt(3, bean.getOrder().getId());
			ps.setInt(4, bean.getUser().getId());
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

			String sql = "delete from orderItem where id = " + id;
			s.execute(sql);

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// 查找id
	public OrderItem get(int id) {
		OrderItem bean = null;

		try (Connection conn = DBUtil.getConnection(); Statement s = conn.createStatement()) {

			String sql = "select * from orderItem where id = " + id;
			ResultSet rs = s.executeQuery(sql);

			if (rs.next()) {
				UserDAO userDAO = new UserDAO();
				User user = userDAO.get(rs.getInt("uid"));
				ProductDAO productDAO = new ProductDAO();
				Product product = productDAO.get(rs.getInt("pid"));
				OrderDAO orderDAO = new OrderDAO();
				Order order = orderDAO.get(rs.getInt("oid"));
				
				bean = new OrderItem();
				bean.setId(rs.getInt("id"));
				bean.setNumber(rs.getInt("number"));
				bean.setProduct(product);
				bean.setOrder(order);
				bean.setUser(user);
				bean.setUser(user);
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return bean;
	}

	// 范围查找start<=id<=end
	public ArrayList<OrderItem> list(int start, int end) {
		ArrayList<OrderItem> beans = new ArrayList<>();
		String sql = "select * from orderItem order by id desc limit ?,?";
		try (Connection conn = DBUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

			ps.setInt(1, start);
			ps.setInt(2, end);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				UserDAO userDAO = new UserDAO();
				User user = userDAO.get(rs.getInt("uid"));
				ProductDAO productDAO = new ProductDAO();
				Product product = productDAO.get(rs.getInt("pid"));
				OrderDAO orderDAO = new OrderDAO();
				Order order = orderDAO.get(rs.getInt("oid"));
				
				OrderItem bean = new OrderItem();
				bean.setId(rs.getInt("id"));
				bean.setNumber(rs.getInt("number"));
				bean.setProduct(product);
				bean.setOrder(order);
				bean.setUser(user);
				bean.setUser(user);
				
				beans.add(bean);
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return beans;
	}

	// 查找所有
	public ArrayList<OrderItem> list() {
		return list(0, Short.MAX_VALUE);
	}

	// 查找一共有多少条数据
	public int getTotal() {
		int total = 0;

		try (Connection conn = DBUtil.getConnection(); Statement s = conn.createStatement()) {

			String sql = "select count(*) from orderItem";
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
		OrderItemDAO dao = new OrderItemDAO();
		ProductDAO pdao = new ProductDAO();
		OrderDAO odao = new OrderDAO();
		UserDAO udao = new UserDAO();

//		增加
		User u = udao.get(1);
		Order o = odao.get(2);
		Product p = pdao.get(10);
		
		OrderItem bean = new OrderItem();
		bean.setNumber(3);
		bean.setUser(u);
		bean.setOrder(o);
		bean.setProduct(p);
		dao.add(bean);

//		查询
		ArrayList<OrderItem> beans = dao.list();
		Iterator<OrderItem> itr = beans.iterator();
		while (itr.hasNext()) {
			OrderItem i = itr.next();
			System.out.println(i.getNumber());
			System.out.println(i.getProduct().getId());
			System.out.println(i.getOrder().getId());
			System.out.println(i.getUser().getId());
		}

//		修改
//		OrderItem bean = dao.get(1);
//		bean.setNumber(10);
//		bean.setUser(u);
//		bean.setOrder(o);
//		bean.setProduct(p);
//		dao.update(bean);

//		删除
//		dao.delete(bean.getId());

//		总数
//		System.out.println(dao.getTotal());
	}
	
	//非CRUD
	
	//查询一个订单的订单项
	public ArrayList<OrderItem> listByOrder(int oid, int start, int end) {
		ArrayList<OrderItem> beans = new ArrayList<>();
		String sql = "select * from orderItem where oid = ? order by id desc limit ?,?";
		try (Connection conn = DBUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

			ps.setInt(1, oid);
			ps.setInt(2, start);
			ps.setInt(3, end);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				UserDAO userDAO = new UserDAO();
				User user = userDAO.get(rs.getInt("uid"));
				ProductDAO productDAO = new ProductDAO();
				Product product = productDAO.get(rs.getInt("pid"));
				OrderDAO orderDAO = new OrderDAO();
				Order order = orderDAO.get(rs.getInt("oid"));
				
				OrderItem bean = new OrderItem();
				bean.setId(rs.getInt("id"));
				bean.setNumber(rs.getInt("number"));
				bean.setProduct(product);
				bean.setOrder(order);
				bean.setUser(user);
				bean.setUser(user);
				
				beans.add(bean);
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return beans;
	}
	
	public ArrayList<OrderItem> listByOrder(int oid) {
		return listByOrder(oid, 0, Short.MAX_VALUE);
	}
	
	//填充订单数据
	public void fill(Order o) {
        ArrayList<OrderItem> ois=listByOrder(o.getId());
        float total = 0;
        int totalNumber = 0;
        for (OrderItem oi : ois) {
             total+=oi.getNumber()*oi.getProduct().getPromotePrice();
             totalNumber+=oi.getNumber();
        }
        o.setTotal(total);	//设置总金额
        o.setOrderItems(ois);	//设置所有订单项
        o.setTotalNumber(totalNumber); //设置总数量
    }
	
	public void fill(ArrayList<Order> os) {
        for (Order o : os) {
        	fill(o);
        }
    }
	
	//获取某一个产品的销量
	public int getSaleCount(int pid) {
        int total = 0;
           try (Connection c = DBUtil.getConnection(); Statement s = c.createStatement();) {
     
               String sql = "select sum(number) from OrderItem where pid = " + pid;
     
               ResultSet rs = s.executeQuery(sql);
               while (rs.next()) {
                   total = rs.getInt(1);
               }
           } catch (SQLException e) {
     
               e.printStackTrace();
           }
           return total;
   }
	
}
