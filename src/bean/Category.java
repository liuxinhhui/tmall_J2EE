package bean;

import java.util.List;

/*
 * ����ʵ����
 */
public class Category {
	private int id;
	private String name;
	
	//������Ϊ����ǰ̨��ҳ��ʾ������Ʒ
	List<Product> products;	//�����µ���Ʒ
	List<List<Product>> productsByRow; //�����ź� ������ʾ
	
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
