package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;

import bean.Product;
import bean.Property;
import bean.PropertyValue;
import util.DBUtil;

/*
 * 属性值DAO
 */
public class PropertyValueDAO {
	/*
	 * 插入时，bean里面没有id值。只有插入数据库之后，数据库自动生成之后，再获取id值赋值到bean上
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

	// 通过bean中的id定位，将数据库中该id的数据修改成bean中的属性值
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

	// 根据id删除数据表中的数据
	public void delete(int id) {
		try (Connection conn = DBUtil.getConnection(); Statement s = conn.createStatement()) {

			String sql = "delete from propertyValue where id = " + id;
			s.execute(sql);

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// 查找id
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

	// 范围查找start<=id<=end
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

	// 查找所有
	public ArrayList<PropertyValue> list() {
		return list(0, Short.MAX_VALUE);
	}

	// 查找一共有多少条数据
	public int getTotal() {
		int total = 0;

		try (Connection conn = DBUtil.getConnection(); Statement s = conn.createStatement()) {

			String sql = "select count(*) from propertyValue";
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
//		ProductDAO pdao = new ProductDAO();
//		PropertyDAO ptdao = new PropertyDAO();
		

//		增加
//		Product p = pdao.get(1);
//		Property pt = ptdao.get(1);
//		
//		PropertyValue bean = new PropertyValue();
//		bean.setValue("2米");
//		bean.setProduct(p);
//		bean.setProperty(pt);
//		dao.add(bean);

//		查询
//		ArrayList<PropertyValue> beans = dao.list();
//		Iterator<PropertyValue> itr = beans.iterator();
//		while (itr.hasNext()) {
//			PropertyValue i = itr.next();
//			System.out.println(i.getValue());
//			System.out.println(i.getProduct().getId());
//			System.out.println(i.getProperty().getId());
//		}

//		修改
//		PropertyValue bean = dao.get(1);
//		bean.setValue("18厘米");
//		bean.setProduct(p);
//		bean.setProperty(pt);
//		dao.update(bean);

//		删除
//		dao.delete(bean.getId());

//		总数
//		System.out.println(dao.getTotal());
		
		ArrayList<PropertyValue> beans = dao.list(5);
		Iterator<PropertyValue> itr = beans.iterator();
		while (itr.hasNext()) {
			PropertyValue i = itr.next();
			System.out.println(i.getValue());
			System.out.println(i.getProduct().getId());
			System.out.println(i.getProperty().getId());
		}
	}
	
	//非CRUD方法
	
	//根据产品id和属性id查找唯一属性值
	public PropertyValue get(int ptid, int pid) {
        PropertyValue bean = null;
        try (Connection c = DBUtil.getConnection(); Statement s = c.createStatement();) {
            String sql = "select * from PropertyValue where ptid = " + ptid + " and pid = " + pid;
            ResultSet rs = s.executeQuery(sql);
            if (rs.next()) {
                bean= new PropertyValue();
                int id = rs.getInt("id");
 
                String value = rs.getString("value");
                 
                Product product = new ProductDAO().get(pid);
                Property property = new PropertyDAO().get(ptid);
                bean.setProduct(product);
                bean.setProperty(property);
                bean.setValue(value);
                bean.setId(id);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bean;
    }
	
	//初始化一个产品的属性值 保证其所在分类的每一个属性都有一个对应的属性值
	public void init(Product p) {
        ArrayList<Property> pts= new PropertyDAO().list(p.getCategory().getId());
         
        for (Property pt: pts) {
            PropertyValue pv = get(pt.getId(),p.getId());
            if(null==pv){
                pv = new PropertyValue();
                pv.setProduct(p);
                pv.setProperty(pt);
                this.add(pv);
            }
        }
    }
	
	//查找一个产品的 所有属性值
	public ArrayList<PropertyValue> list(int pid) {
		ArrayList<PropertyValue> beans = new ArrayList<PropertyValue>();
         
        String sql = "select * from PropertyValue where pid = ? order by ptid desc";
        try (Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql);) {
            ps.setInt(1, pid);
  
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                PropertyValue bean = new PropertyValue();
                int id = rs.getInt(1);
 
                int ptid = rs.getInt("ptid");
                String value = rs.getString("value");
                 
                Product product = new ProductDAO().get(pid);
                Property property = new PropertyDAO().get(ptid);
                bean.setProduct(product);
                bean.setProperty(property);
                bean.setValue(value);
                bean.setId(id);          
                beans.add(bean);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return beans;
    }
}
