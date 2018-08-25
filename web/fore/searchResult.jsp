<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" isELIgnored="false"%>
     
<style>
	body{
	    font-size: 12px;
	    font-family: Arial;
	}
	
/* 	分类产品div */
	div.categoryPageDiv div.categoryProducts{
		margin:0px 20px 40px;
	}
/* 	单个产品单元 悬浮加厚边框怎么实现 */
	div.categoryPageDiv div.productUnit{
		width: 225px;
	    height: 330px;
	    border: 3px solid transparent;
	    background-color: white;
	    margin: 12px 5px;
	    float: left;
/* 		overflow:hidden; */
	}
	div.categoryPageDiv div.productUnit:hover{
		border-color: #c40000;
	}
	div.categoryPageDiv div.productUnit div.productUnitFrame{
		border:1px solid #eee;
		height:100%;
	}
	div.categoryPageDiv div.productUnit div.productUnitFrame:hover{
		border-color: #c40000;
	}
/* 	产品单元内部 */
	div.categoryPageDiv div.productUnit div.productUnitFrame img.productImage{
		width: 100%;
		height: 190px;
	}
	div.categoryPageDiv div.productUnit div.productUnitFrame span.productPrice{
		display: block;
	    padding-left: 4px;
		font-size: 20px;
	    color: #CC0000;
	}
	div.categoryPageDiv div.productUnit div.productUnitFrame a.productLink{
		display: block;
	    height:34px;
		margin: 10px 0px;
	    color: #333333;
	    text-decoration: none;
	}
	div.categoryPageDiv div.productUnit div.productUnitFrame a.productLink:hover{
		text-decoration: underline;
    	color: #C40000;
	}
	div.categoryPageDiv div.productUnit div.productUnitFrame a.tmallLink{
		display: block;
		margin: 10px 0px;
	    color: #999999;
	}
	div.categoryPageDiv div.productUnit div.productUnitFrame a.tmallLink:hover{
		text-decoration: underline;
		color: #C40000;
	}
/* 	产品单元 下方的销量 评价 旺旺 */
	div.categoryPageDiv div.productUnit div.productUnitFrame div.productInfo{
		border-top-width: 1px;
	    border-top-style: solid;
	    border-top-color: #EEEEEE;
	    color:#999999;
	    height:30px;
	    text-align: center;
	    line-height: 30px;
	}
	div.categoryPageDiv div.productUnit div.productUnitFrame div.productInfo span.monthDeal,
	div.categoryPageDiv div.productUnit div.productUnitFrame div.productInfo span.productReview{
		display: inline-block;
		width: 90px;
		height: 28px;
	}
	div.categoryPageDiv div.productUnit div.productUnitFrame div.productInfo span.productReview{
		border-left-width: 1px;
	    border-left-style: solid;
	    border-left-color: #EEEEEE;
	    border-right-width: 1px;
	    border-right-style: solid;
	    border-right-color: #EEEEEE;
	}
	div.categoryPageDiv div.productUnit div.productUnitFrame div.productInfo span.monthDeal span.productDealNumber{
		font-weight: bold;
    	color: #B57C5B;
	}
	div.categoryPageDiv div.productUnit div.productUnitFrame div.productInfo span.productReview span.productReviewNumber{
		font-weight: bold;
    	color: #3388BB;
	}
	div.categoryPageDiv div.productUnit div.productUnitFrame div.productInfo span.wangwang{
		display:inline-block;
		width: 20px;
		margin: 0 auto;
	}
	
	div.categoryPageDiv  div.noMatch {
		font-size: 20px;
		width: 200px;
		margin: 100px auto;
		color: #888;
	}
</style>     

<div id="searchResult">
     
    <div class="categoryPageDiv">
		<div class="categoryProducts">
     
		    <c:forEach items="${ps}" var="p">
				<div price="${p.promotePrice}" class="productUnit">
					<div class="productUnitFrame">
						<a href="foreproduct?pid=${p.id}">
							<img width="100px" src="img/productSingle_middle/${p.firstProductImage.id}.jpg" class="productImage">
						</a>
						<span class="productPrice">¥<fmt:formatNumber type="number" value="${p.promotePrice}" minFractionDigits="2"/></span>
						<a href="foreproduct?pid=${p.id}" class="productLink"${fn:substring(p.name, 0, 50)}</a>
						<a href="foreproduct?pid=${p.id}" class="tmallLink">天猫专卖</a>
						<div class="productInfo">
							<span class="monthDeal">月成交<span class="productDealNumber">${p.saleCount}笔</span></span>
							<span class="productReview">月成交<span class="productReviewNumber">${p.reviewCount}</span></span>
							<span class="wangwang">
								<a href="#nowhere" class="wangwanglink">
									<img alt="" src="img/site/wangwang.png">
								</a>
							</span>
						</div>
					</div><!-- productUnitFrame -->
				</div><!-- productUnit -->
			</c:forEach>
			
		    <c:if test="${empty ps}">
		        <div class="noMatch">没有满足条件的产品<div>
		    </c:if>
		     
		    <div style="clear:both"></div>
		
		</div>
    </div>
 
</div>