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
	<link rel="stylesheet" href="${path }/css/ht_cmn.css" type="text/css"/>
    <script src="${path }/js/jquery.js" type="text/javascript"></script>
	<style>
		.content{padding: 10px;overflow: auto;}
	</style>
</head>
<body>
	
	<%@ include file="bread.jsp"%>
	
	<div class="content">
		<div class="btns">
			<i></i><input type="button" name="button" class="btn btn_cmn btn82 btn_add " value="新增" onclick="location.href='aeyqlj.dhtml'"/>
		</div>
		<table width="100%" border="0" cellpadding="0" cellspacing="0" class="list_table">
			<thead>
				<tr>
					<th style="width:3em">序号</th>
					<th title = '网站名称'>网站名称</th>
					<th title = '网址'>网址</th>
					<th title = 'Logo'>Logo</th>
					<th title = '排序'>排序</th>
					<th title = '点击次数'>点击次数</th>
					<th title='操作' style="width: 8em;">操作</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${list}" var="itm" varStatus="status">
					<tr <c:if test="${status.index%2==1}">style='background:rgb(255, 252, 234)'</c:if>>
						<td title="${status.index+1+(pg-1)*perPage }">${status.index+1+(pg-1)*perPage }</td>
						<td title="${itm.mc}">${itm.mc}</td>
						<td title="${itm.wz}">${itm.wz}</td>
						<td>
							<c:if test="${!empty(itm.logo)}"><img src="${itm.logo}" height=30/></c:if>
							<c:if test="${empty(itm.logo)}">未上传图片</c:if>
						</td>
						<td title="${itm.px}">${itm.px}</td>
						<td title="${itm.cs}">${itm.cs}</td>
						<td>
							<a href="javascript:" onclick="location.href='aeyqlj.dhtml?id=${itm.id}'">编辑</a>
							 | <a id="${itm.id}" href="javascript:" onclick="fn_del(this)">删除</a>
						</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
		<div id="pagin">
			<span class="fymsg"></span>
			<div id="fenye"></div>
		</div>
		</div>

	<script src="${path}/layer/layer.js"></script>
	<script src="${path}/laypage/laypage.js"></script>
    <script type="text/javascript">
    
		function layout(){
   			$(".content").height($(window).height()-30)
   		}
		window.onresize=function(){
			layout();layout()
		}
    
		$(function(){
			if ('${msg}' !=''){
				layer.msg('${msg}')
			}
			$(".fymsg").html('</i>共<i>'+${recTotal}+'</i>条记录，每页<i>'+${perPage}+'</i>个，共<i>'+${pgTotal}+'</i>页')
			laypage({
			    cont: 'fenye',
			    skip: true, //是否开启跳页
			    groups: 5,//连续显示分页数
			    pages: ${pgTotal},
				curr: function(){
			        	var page = location.search.match(/pg=(\d+)/);
			        return page ? page[1] : 1;
			    }(), 
			    jump: function(e, first){
			        if(!first){
			            location.href = '?pg='+e.curr;
			        }
			    }
			})
			layout();
		})
		
		function fn_del(tag){
			xh= $(tag).parent().parent().children("td:eq(0)").text()
			mc= $(tag).parent().parent().children("td:eq(1)").text()
			layer.confirm('确定要删除序号为 '+xh+' 名称为<br/>'+mc+' 的友情链接吗？', {
					icon : 3,
					title : '提示'
				}, function(index) {
					$.ajax({
				          type:"POST",
				          url:"delyqlj.dhtml",
				          data:{id:$(tag).attr("id")},
				          datatype: "text",
				          beforeSend:function(){layer.msg('正在删除......', {time:-1,icon: 16,shade: 0.3, scrollbar:false});},  
				          success:function(text){
				          		if ("0"==text){
									location.href="?msg=1"
				          		}else{
				          			layer.alert('删除失败，请联系管理员！', { icon:2, title:'错误'});
				          		}
				          },
				          error: function(){
							layer.alert('删除失败，请刷新重试！', { icon:2, title:'错误'});
				          }         
					});
			});
		}
	</script>
</body>
</html>
       