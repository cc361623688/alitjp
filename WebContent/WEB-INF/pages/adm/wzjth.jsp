<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%
	request.setAttribute("path", request.getContextPath());
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="X-UA-Compatible" content="IE=EDGE,chrome=1"/>
	<link rel="stylesheet" href="${path}/css/ht_cmn.css" type="text/css"/>
    <script src="${path }/js/jquery.js" type="text/javascript"></script>
    <script src="${path }/js/cmn.js" type="text/javascript"></script>
	<style>
		.content{padding: 10px;overflow: auto;}
		.list_table td{
			word-break:break-all;
		    white-space:normal;
		}
		.list_table>tbody>tr>td:first-child{font-weight: bold;}
		.list_table td>input{margin-right:10px}
	</style>
</head>
<body>
<%@ include file="bread.jsp"%>
	<div class="content">
		<form id="id_ae_form">
			<table class="list_table" border="0" cellpadding="0" cellspacing="0">
				<thead>
					<tr>
						<th class="first">
							网站静态化
						</th>
						<th style="border-left-width:0"></th>
						<th style="border-left-width:0"></th>
					</tr>
				</thead>
				<tbody>
					<tr>
						<td>一键操作</td>
						<td colspan="1">
							<input type="button" value="一键静态化" class="ext_btn ext_btn_submit" style="" onclick="fn_yjjth(1)"/>
							<input type="button" value="一键动态化" class="ext_btn ext_btn_submit" onclick="fn_yjjth(0)"/>
						</td>
						<td>对整个网站静态化或动态化（包括首页、所有已设置动态化的栏目及其内容），耗时更长，但操作便捷</td>
					</tr>
					<tr>
						<c:set var="sytp" value="动态化"/>
						<c:if test="${syjth=='动态化'}"><c:set var="sytp" value="静态化"/></c:if>
						<td>首页当前状态：${syjth}</td>
						<td colspan="1">
							<input type="button" value="生成静态化首页" class="ext_btn ext_btn_submit" onclick="fn_syjth(1)"/>
							<input type="button" value="生成动态化首页" class="ext_btn ext_btn_submit" onclick="fn_syjth(0)"/>
						</td>
						<td>单独对首页进行动态化或静态化的操作</td>
					</tr>
					<tr>
						<c:set var="lmtp" value="动态化"/>
						<c:if test="${lmjth=='动态化'}"><c:set var="lmtp" value="静态化"/></c:if>
						<td>栏目当前状态：${lmjth}</td>
						<td colspan="1">
							<input type="button" value="栏目静态化" class="ext_btn ext_btn_submit" onclick="fn_lmjth(1)"/>
							<input type="button" value="栏目动态化" class="ext_btn ext_btn_submit" onclick="fn_lmjth(0)"/>
						</td>
						<td>对栏目进行静态化或动态化的操作，单独设置了栏目动态化的栏目不受影响</td>
					</tr>
					<tr>
						<c:set var="nrtp" value="动态化"/>
						<c:if test="${lmnrjth=='动态化'}"><c:set var="nrtp" value="静态化"/></c:if>
						<td>栏目内容当前状态：${lmnrjth}</td>
						<td colspan="1">
							<input type="button" value="栏目内容静态化" class="ext_btn ext_btn_submit" onclick="fn_lmnrjth(1)"/>
							<input type="button" value="栏目内容动态化" class="ext_btn ext_btn_submit" onclick="fn_lmnrjth(0)"/>
						</td>
						<td>对栏目下的内容进行静态化或动态化的操作，单独设置了内容动态化的栏目内容不受影响</td>
					</tr>
				</tbody>
			</table>
		</form>
	</div>
	

	<script src="${path}/layer/layer.js"></script>
    <script type="text/javascript">

		$(function(){
			
		})

		function fn_yjjth(jth){
			fn_tp_aj('确定要一键'+(jth==1?'静态化':'动态化')+'吗？', 'aj_yjjth.dhtml', {jth:jth}, 1);
		}

		//首页静态化
		function fn_syjth(jth){
			fn_tp_aj('确定要将首页'+(jth==1?'静态化':'动态化')+'吗？', 'aj_syjth.dhtml', {jth:jth}, 1);
		}

		//栏目静态化
		function fn_lmjth(jth){
			fn_tp_aj('确定要将栏目'+(jth==1?'静态化':'动态化')+'吗？', 'aj_lmjth.dhtml', {jth:jth}, 1);
		}

		//栏目内容静态化
		function fn_lmnrjth(jth){
			fn_tp_aj('确定要将栏目内容'+(jth==1?'静态化':'动态化')+'吗？', 'aj_lmnrjth.dhtml',{jth:jth}, 1);
		}
	</script>
</body>
</html>
       