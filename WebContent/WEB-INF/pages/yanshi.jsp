<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<% request.setAttribute("path", request.getContextPath());%>

<!doctype html>
<html>
<head>
<title>tjpcms - 演示</title>
<%@ include file="inc/meta.jsp"%>

<style>
	.sycontent{padding-top:0 !important}
	.panel{text-align: justify;text-indent: 2em;font-size: 15px;border: 1px solid #ddd;-webkit-border-radius: 4px;
	-moz-border-radius: 4px;width: 90%;margin: 0 auto;border-radius: 4px;margin-bottom: 18px;}
	.pnlbd{ padding: 15px;}
	.pnlft{
	    background-color: #f5f5f5;
	    padding: 10px 15px;
	    border-radius: 0 0 4px 4px;
	    border-top: solid 1px #ddd;line-height:175%;}
	 .pnlft p{border-bottom:1px solid #ccc;height:50px;line-height: 50px;    overflow: hidden;white-space: nowrap;text-overflow: ellipsis;    text-indent: 3px;}
	.wzsk{background-color: #0ae;color: #fff;padding:5px 12px;text-indent:0 !important;    border-radius: 6px;margin-right: 5px;}
	
	@media (max-width: 1000px) {
		.htdz{word-break: break-all;word-warp:break-word;}
	}
</style>

</head>
<body>
	<%@ include file="inc/head.jsp"%>
	
	<div class="sycontent" >
		<div class="bread">
			<ul>
				<li><a class="icon-shouye brdsy" href="${path}/"> 首页</a></li>
				<li><a href="#">演示</a></li>
			</ul>
		</div>
		<div class="panel">
			<div class="pnlbd">
				说的再好也不如做得好啊，下面就放出一个基于tjpcms开发的网站的前后台实例，也是首页下载包里的源码编译后的发布版。为了展现作者最大的诚意，此处直接展示官网的前后台：
			</div>
			<div class='pnlft'>
				<p><span class="wzsk">网站前台地址</span><a target="_blank" href="${path}/">http://www.tjpcms.com</a></p>
				<p class="htdz"><span class="wzsk">测试超级管理员</span><a target="_blank" href="${path}/${dlhtlj_test}.dhtml">http://www.tjpcms.com/${dlhtlj_test}.dhtml</a><label style="font-size:13px;margin-left:15px">用户名/密码：无须输入</label></p>
				<p class="htdz" style="border-width:0"><span class="wzsk">测试注册用户</span><a target="_blank" href="${path}/g2grzx.dhtml?f=test">http://www.tjpcms.com/g2grzx.dhtml</a><label style="font-size:13px;margin-left:15px">用户名/密码：无须输入</label></p>
				<p class="htdz"><span class="wzsk">正式后台用户</span><a target="_blank" href="${path}/${dlhtlj}.dhtml">http://www.tjpcms.com/${dlhtlj}.dhtml</a><label style="font-size:13px;margin-left:15px">用户名/密码：须由管理员分配</label></p>
<p class="htdz"><span class="wzsk">正式超级管理员</span><a target="_blank" href="${path}/dlht_tjp_zy.dhtml">dlht_tjp_zy.dhtml</a><label style="font-size:13px;margin-left:15px">用户名/密码：1122/3344</label></p>
			</div>
		</div>
	</div>

	<c:set var="needcy" value="1"/>
	<%@ include file="inc/btm.jsp"%>
	
	<script>
	</script>
</body>
</html>

