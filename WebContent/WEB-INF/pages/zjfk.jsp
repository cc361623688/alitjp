<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<% request.setAttribute("path", request.getContextPath());%>

<!doctype html>
<html>
<head>
<title>tjpcms - 最近访客</title>
<%@ include file="inc/meta.jsp"%>

<style>
	.sycontent{padding-bottom:18px !important;}
	.pagin{margin-bottom:5px !important;}
	.yaohe{background: #fcf8e3;margin-top: 10px;-webkit-border-radius: 6px;
		-moz-border-radius: 6px;color:#297;text-align:justify;padding: 15px;
		border-radius: 6px;font-size: 15px;line-height: 2;    border: solid 1px #DFDFDF;}
	.line{border-bottom:1px solid rgb(51,51,51);font-weight: bold;padding:0 3px}
	.fangke{width: 90%;margin: 0 auto;}
	.title{font-size:15px;font-weight: bold;}
	.txs{border-radius:5px;background: rgb(244,243,241);padding:10px;margin-top: 2px;}
	.txs img{-webkit-border-radius: 50%;-moz-border-radius: 50%;border-radius: 50%;width: 59px;height: 59px;padding: 5px;cursor: pointer;}
	.fymsg b {
	    color: #056dae;
	    font-style: normal;
	    font-weight: bold;
	    padding: 0 2px;
	}
	.moren{font-weight: normal;vertical-align: middle;}
</style>

</head>
<body>
	<%@ include file="inc/head.jsp"%>
	
	<div class="sycontent">
		<div class="bread">
			<ul>
				<li><a class="icon-shouye brdsy" href="${path }/"> 首页</a></li>
				<li><a href="#">最近访客</a></li>
			</ul>
		</div>
		
		<c:set var="dlcs" value="${ grdlcs+dsfdlcs}"/>
		<div class="" style="width:90%;margin:0 auto;">
		<div class="yaohe">
			自<span class="line">2017-03-07</span>上线注册功能至今：<br/>
			1、共有<span class="line">${zczong }</span>名注册用户，<i>
			</i>其中个人用户<span class="line">${grzong }</span>名，第三方用户<span class="line">${dsfzong }</span>名。<br/>
			2、用户登录次数共：<span class="line">${dlcs }</span>次，其中个人用户<span class="line">${grdlcs }</span>次，<i>
			</i>第三方用户<span class="line">${dsfdlcs }</span>次。
		</div>
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
			<div class="fangke">
				<span><label class="title">以下为最近访客记录</label><i>
				</i>（<img src="http://www.tjpcms.com/alitjp/img/logo.png" class="moren" />为默认头像）：</span>
				<div class="txs">
					<c:forEach items="${fks}" var="t" varStatus="sta">
						<img src="${empty(t.tx)?'http://www.tjpcms.com/alitjp/img/logo.png':t.tx}" alt="头像" title="${t.nc}&#10;${fn:substring(t.rq,0,10)}"/>
					</c:forEach>
				</div>
			</div>
			<div class="pagin">
				<span class="fymsg">共<b>${recTotal}</b>条记录，每页<b>${perPage}</b>条，共<b>${pgTotal }</b>页</span>
				<div id="fenye"></div>
			</div>
		</div>
	</div>
	
	<c:set var="needcy" value="1"/>
	<%@ include file="inc/btm.jsp"%>
	
	<script src="${path}/layer/layer.js"></script>
	<script src="${path}/laypage/laypage.js"></script>
	<script>
		var ns = document.getElementsByTagName("i")
		for(var i = 0; i < ns.length; i++) { 
			ns[i].parentNode.removeChild(ns[i]);//方便换行
		}

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

