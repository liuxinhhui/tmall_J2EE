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
//		if(total % count == 0)
//			return total / count;
//		else
//			return total / count + 1;
		
		int totalPage;
        // 假设总数是50，是能够被5整除的，那么就有10页
        if (0 == total % count)
            totalPage = total /count;
        // 假设总数是51，不能够被5整除的，那么就有11页
        else
            totalPage = total / count + 1;
        
        if(0==totalPage)		//当total = 0时 用1 之前写的没有考虑到0的情况
            totalPage = 1;
        return totalPage;
	}
	
	// 最后一页的start
	public int getLast(){
//		if(total % count == 0)
//			return total - count;
//		else
//			return total - (total%count);
		
		int last;
        // 假设总数是50，是能够被5整除的，那么最后一页的开始就是45
        if (0 == total % count)
            last = total - count;
        // 假设总数是51，不能够被5整除的，那么最后一页的开始就是50
        else
            last = total - total % count;
        
        last = last<0?0:last;
        return last;
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
