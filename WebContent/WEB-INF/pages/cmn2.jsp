<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<% request.setAttribute("path", request.getContextPath());%>

<!doctype html>
<html>
<head>

<%@ include file="inc/meta.jsp"%>
<title>tjpcms - ${title}</title>

<style>
	.sycontent{padding-top:0 !important}
	.list{width: 100%;max-width: 100%;border-collapse: collapse;border-spacing: 0;}
	.list th {
	    vertical-align: bottom;text-align: center;
	    padding: 10px;font-size: 15px;
	    background-color: #5F9EDF; color: #fff;
	}
	.list td {
	    padding: 8px;text-align: center;
	    vertical-align: top;border-right: 1px solid #ccc;font-size: 13px;
	}
	.list a{color: #428bca !important;text-decoration: none;}
	.list a:hover{color:#2a6496 !important}
	.outtbl{-webkit-border-radius: 6px;overflow: hidden;
	-moz-border-radius: 6px;width: 90%;margin: 0 auto;
	border-radius: 6px;;border: 1px solid #ccc;margin-bottom: 10px;margin-top: 20px;}
	.shua{margin-bottom: 15px;    
		background-image: -moz-linear-gradient(#fff 0%, #f8f8f8 100%);
		background-image: -o-linear-gradient(#fff 0%, #f8f8f8 100%);
		background-image: -webkit-linear-gradient(#fff 0%, #f8f8f8 100%);
		background-image: linear-gradient(#fff 0%, #f8f8f8 100%);
	    background-repeat: repeat-x;    background-color: #f8f8f8;
    	color: #5e5e5e;    border: 1px solid #e7e7e7;
	    border-radius: 4px;width: 90%;margin: 0 auto;
	    -webkit-box-shadow: inset 0 1px 0 rgba(255,255,255,.15),0 1px 5px rgba(0,0,0,.075);
	    box-shadow: inset 0 1px 0 rgba(255,255,255,.15),0 1px 5px rgba(0,0,0,.075);margin-top: 40px !important;
	}
	.huac{padding: 15px;text-align: justify;font-size: 14px;text-indent: 2em;line-height: 1.5;}
</style>

</head>
<body>
	<%@ include file="inc/head.jsp"%>
	
	<div class="sycontent">
		<form id="id_form">
			<input type="hidden" name="pg" value="${pg }"/>
			<input type="hidden" name="gjc" value="${gjc }"/>
		</form>
	
		<%@ include file="inc/bread.jsp"%>
		<c:if test="${!empty(kaicb) }">
			<div class="shua">
				<div class="huac">
					${kaicb}
				</div>
			</div>
		</c:if>
		<div class="outtbl">
			<table class="list" >
				<thead>
					<tr>
						<th>标题</th>
						<th style="">点击次数</th>
						<th style="width:20%;">更新日期</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${lst}" var="t" varStatus="sta">
						<tr style="background-color: #fefefe;">
							<td><a target="_blank" onclick="fn_cs(this)" href="${t.url}">${t.title}</a></td>
							<td>${t.cs}</td>
							<td>${empty(t.gx1)?fn:substring(t.gx,0,10):t.gx1}</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>
			<div class="pagin">
				<span class="fymsg">共<i>${recTotal}</i>条记录，每页<i>${perPage}</i>条，共<i>${pgTotal }</i>页</span>
				<div id="fenye"></div>
			</div>
	</div>
	
	<%@ include file="inc/btm.jsp"%>
	
	<script src="${path}/layer/layer.js"></script>
	<script src="${path}/laypage/laypage.js"></script>
	<script>
		laypage({
			cont : 'fenye',
			skip : true,
			groups : 5,
			pages :${pgTotal},
			curr: function(){ //通过url获取当前页，也可以同上（pages）方式获取
				return $("input[name='pg']").val();
			}(),
			jump : function(e, first) {
			    if(!first){
					$("input[name='pg']").val(e.curr);
			        $("#id_form").attr('action', location.href).submit();
			    }
			}
		})

		function fn_cs(tag){
			var ecs = $(tag).parent().next();
			ecs.text(parseInt(ecs.text())+1)
		}

		$(function() {
			$(".list>tbody>tr:odd").css("background", "#eaf3f5")
		})
	</script>
</body>
</html>

