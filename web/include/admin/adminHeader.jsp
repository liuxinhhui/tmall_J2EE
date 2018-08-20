<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" isELIgnored="false"%> <!-- 启用EL表达式 -->    
<!-- 引入JSTL标签 -->
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
    
<!DOCTYPE html>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<!-- 这里不能加title 否则后面的title就不起作用了 -->
<!-- <title>后台公共页眉 负责引入文件</title> -->

<!-- 引入jquery -->
<script src="js/jquery/2.0.0/jquery.min.js"></script>

<!-- 引入bootstrap -->
<link href="css/bootstrap/3.3.6/bootstrap.min.css" rel="stylesheet">
<script src="js/bootstrap/3.3.6/bootstrap.min.js"></script>

<!-- 引入自定义后台样式 -->
<link href="css/back/style.css" rel="stylesheet">

<!-- 引入自定义的一些判断输入框的js函数 -->
<script src="js/back/judge.js"></script>
</head>
