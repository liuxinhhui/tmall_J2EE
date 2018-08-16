package bean;

/*
 * 分类属性实体类
 */
public class Property {
	private int id;
	private String name;
	
	private Category category;	/*表中用cid表示，实体类直接用实体表示*/

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

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}
}
