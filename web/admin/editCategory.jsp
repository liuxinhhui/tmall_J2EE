<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@include file="../include/admin/adminHeader.jsp"%>
<%@include file="../include/admin/adminNavigator.jsp"%>

<title>编辑分类</title>

<script>
	$(function(){
		$("#editForm").submit(function(){
			if(!checkEmpty("name","分类名称"))
				return false;
	
			return true;
		});
	});
</script>

<div class="workingArea">

	<ol class="breadcrumb">
	  <li><a href="admin_category_list">所有分类</a></li>
	  <li class="active">编辑分类</li>
	</ol>
	
	<div class="panel panel-warning editDiv">
		<div class="panel-heading">新增分类</div>
		<div class="panel-body">
			<form method="post" id="editForm" action="admin_category_update" enctype="multipart/form-data">
				<table 	class="editTable">
					<tr>
					    <td>分类名称</td>
					    <td><input id="name" name="name" type="text" class="form-control" value="${c.name }"></td>
					</tr>
					<tr>
					    <td>分类图片</td>
					    <td><input id="categoryPic" name="filepath" type="file" accept="image/*"></td>
					</tr>
					<tr> 
						<td colspan="2" align="center">
							<input type="hidden" name="id" value="${c.id}"><!-- 和增加不同的是 需要知道修改哪个分类 所以要传入一个id -->
							<button type="submit" class="btn btn-success">提 交</button>
						</td>
					</tr>
				</table>
			</form>
		</div>
	</div>
	
</div>

<%@include file="../include/admin/adminFooter.jsp"%>