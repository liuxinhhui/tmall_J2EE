package bean;

/*
 * ��ƷͼƬʵ���ࣺ����չʾ��ͼƬ������ҳ���ͼƬ
 */
public class ProductImage {
	private int id;
	private String type;	/*������չʾͼ��������ͼ ������ļ�ȥ�����ң�*/
	
	private Product product;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}
	
}
