package util;

/*
 * Page�Ǻ�̨��ҳ�Ĺ�����
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

	// ��ҳ��
	public int getTotalPage(){
		if(total % count == 0)
			return total / count;
		else
			return total / count + 1;
	}
	
	// ���һҳ��start
	public int getLast(){
		if(total % count == 0)
			return total - count;
		else
			return total - (total%count);
	}
	
	// �Ƿ���ǰһҳ
	public boolean isHasPrevious(){
		return start!=0;
	}
	
	// �Ƿ��к�һҳ
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
