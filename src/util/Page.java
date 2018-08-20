package util;

/*
 * Page是后台分页的工具类
 */
public class Page {
	int start;
	int count;
	int total;
	String param;
	
	public Page(int start, int count) {
		super();
		this.start = start;
		this.count = count;
	}

	// 总页数
	public int getTotalPage(){
		if(total % count == 0)
			return total / count;
		else
			return total / count + 1;
	}
	
	// 最后一页的start
	public int getLast(){
		if(total % count == 0)
			return total - count;
		else
			return total - (total%count);
	}
	
	// 是否有前一页
	public boolean isHasPrevious(){
		return start!=0;
	}
	
	// 是否有后一页
	public boolean isHasNext(){
		return start!=getLast();
	}
	
	public int getStart() {
		return start;
	}
	public void setStart(int start) {
		this.start = start;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
	}
	public String getParam() {
		return param;
	}
	public void setParam(String param) {
		this.param = param;
	}
	
}
