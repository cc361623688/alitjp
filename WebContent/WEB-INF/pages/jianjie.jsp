<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<% request.setAttribute("path", request.getContextPath());%>

<!doctype html>
<html>
<head>
<title>tjpcms - 关于作者 - 简介</title>
<%@ include file="inc/meta.jsp"%>

<style>
	.jjnr{text-align: justify;padding: 10px;}
</style>

</head>
<body>
	<%@ include file="inc/head.jsp"%>
	


	
	<div class="sycontent">
		<div class="bread">
			<ul>
				<li><a class="icon-shouye brdsy" href="${path }/"> 首页</a></li>
				<li><a href="#">关于作者</a></li>
				<li><a href="#">简介</a></li>
			</ul>
		</div>
		<div class="jjnr">
			${jj.nr}
		</div>
	</div>
	
	<!--PC和WAP自适应版-->

	
	<%@ include file="inc/btm.jsp"%>
	
	<script>
		$(function() {
			
		})
	</script>
</body>
</html>

