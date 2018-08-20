<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" isELIgnored="false"%>

<script>
	$(function(){
// 		这表示去掉点击效果，即点击之后不会跳转到其他页面
	    $("ul.pagination li.disabled a").click(function(){
	        return false;
	    });
	});
</script>

<div>
	<nav>
		<ul class="pagination">
			<li <c:if test="${!page.hasPrevious}">class="disabled"</c:if>>
				<a href="?page.start=0"><span>«</span></a>
			</li>
			<li <c:if test="${!page.hasPrevious}">class="disabled"</c:if>>
				<a href="?page.start=${page.start - page.count }"><span>‹</span></a>
			</li>
			
<!-- 			这里需要注意的是 page.totalPage 是可行的 甚至更符合EL语法 -->
<!-- 			li的if标签当前页不可选 a的if标签为当前页标黑加粗 -->
			<c:forEach begin="0" end="${page.getTotalPage() - 1 }" varStatus="status">
				<li <c:if test="${status.index*page.count==page.start}">class="disabled"</c:if>>
					<a href="?page.start=${status.index * page.count }" 
					<c:if test="${status.index * page.count == page.start }">class="current"</c:if>
					>${status.count }</a>
				</li>
			</c:forEach>
			
			<li <c:if test="${!page.hasNext }">class="disabled"</c:if>>
				<a href="?page.start=${page.start + page.count }"><span>›</span></a>
			</li>
			<li <c:if test="${!page.hasNext }">class="disabled"</c:if>>
				<a href="?page.start=${page.last}"><span>»</span></a>
			</li>
		</ul>	
	</nav>
</div>