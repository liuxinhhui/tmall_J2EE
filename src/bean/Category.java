package bean;

import java.util.List;

/*
 * 分类实体类
 */
public class Category {
	private int id;
	private String name;
	
	//以下是为了在前台首页显示分类商品
	List<Product> products;	//分类下的商品
	List<List<Product>> productsByRow; //按行排号 方便显示
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<Product> getProducts() {
		return products;
	}
	public void setProducts(List<Product> products) {
		this.products = products;
	}
	public List<List<Product>> getProductsByRow() {
		return productsByRow;
	}
	public void setProductsByRow(List<List<Product>> productsByRow) {
		this.productsByRow = productsByRow;
	}
	
	
}
