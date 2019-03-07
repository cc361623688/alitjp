<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<% request.setAttribute("path", request.getContextPath());%>

<!doctype html>
<html>
<head>
<title>tjpcms - 开发笔记</title>
<%@ include file="inc/meta.jsp"%>

<style>
	.sycontent{padding-bottom:18px !important;}
	.tphead{color:rgb(0, 170, 136);margin-bottom: 10px;margin-top: 3px;}
	.tphead span{font-size: 12px;;}
	.clscont,.imgwarp { display: table;}
	.contitle{font-size: 14px;line-height: 20px;vertical-align: middle; display: table-cell;color:rgb(85, 85, 85)}
	.onebj{    box-shadow: 0 0 10px 7px rgba(218, 218, 218, 0.9);width: 90%;margin:0 auto;border-radius:6px;padding: 10px;border:1px solid #ccc;margin-bottom: 20px;}
	.clstx{border-radius: 50%;width:60px;padding: 0 8px;vertical-align: middle; display: table-cell;}
	
	@font-face {font-family: "iconfont";
	  src: url('iconfont.eot?t=1479790100779'); /* IE9*/
	  src: url('iconfont.eot?t=1479790100779#iefix') format('embedded-opentype'), /* IE6-IE8 */
	  url('iconfont.woff?t=1479790100779') format('woff'), /* chrome, firefox */
	  url('iconfont.ttf?t=1479790100779') format('truetype'), /* chrome, firefox, opera, Safari, Android, iOS 4.2+*/
	  url('iconfont.svg?t=1479790100779#iconfont') format('svg'); /* iOS 4.1- */
	}
	
	[class*='icon-']:before {
	    font-family:"iconfont" !important;
	    font-size:16px;
	    font-style:normal;
		font-weight: normal;
		-webkit-font-smoothing: antialiased;
		-webkit-text-stroke-width: 0.2px;
		-moz-osx-font-smoothing: grayscale;
	}
	.icon-7-copy:before { content: "\e659"; }
	.icon-marker:before { content: "\e6e3"; }
	.pagin{margin-bottom:5px !important;}
</style>

</head>
<body>
	<%@ include file="inc/head.jsp"%>
	
	<div class="sycontent">
		<div class="bread">
			<ul>
				<li><a class="icon-shouye brdsy" href="${path }/"> 首页</a></li>
				<li><a href="#">开发笔记</a></li>
			</ul>
		</div>
		<div style="padding-top:15px">
			<c:forEach items="${lst}" var="t" varStatus="sta">
				<div class="onebj">
					<div class="tphead">
						<span class="icon-7-copy" style="margin-right: 10px;"> ${t.gx} </span>
						<span class="icon-marker"> ${t.dd}</span>
					</div>
					<div>
						<div class="imgwarp" style="float:left"><img src="${t.tu}" alt="portrait" class="clstx"/></div>
						<!-- <div style="clear:both"></div> -->
						<div class="clscont" style="height: 60px">
							<div class="contitle">${t.nr}</div>
						</div>
					</div>
				</div>
			</c:forEach>
			<div class="pagin">
				<span class="fymsg">共<i>${recTotal}</i>条记录，每页<i>${perPage}</i>条，共<i>${pgTotal }</i>页</span>
				<div id="fenye"></div>
			</div>
		</div>
	</div>
	
	<c:set var="needcy" value="1"/>
	<%@ include file="inc/btm.jsp"%>
	
	<script src="${path}/layer/layer.js"></script>
	<script src="${path}/laypage/laypage.js"></script>
	<script>
		$(function() {
			laypage({
				cont : 'fenye',
				skip : true,
				groups : 5,
				pages :${pgTotal},
				curr: function(){ //通过url获取当前页，也可以同上（pages）方式获取
				  var page = location.search.match(/pg=(\d+)/);
				  return page ? page[1] : 1;
				}(), 
				jump : function(e, first) {
				    if(!first){
				      location.href = '?pg='+e.curr;
				    }
				}
			})
		})
	</script>
</body>
</html>

