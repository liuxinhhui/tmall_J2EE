package bean;

/*
 * 产品图片实体类：包括展示的图片和详情页里的图片
 */
public class ProductImage {
	private int id;
	private String type;	/*区分是展示图还是详情图 具体的文件去哪里找？*/
	
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
