<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	request.setAttribute("path", request.getContextPath());
%>

<!doctype html>
<html>
<head>
<title>tjpcms - 大事记</title>
<%@ include file="inc/meta.jsp"%>

<link rel="stylesheet" href="${path}/css/dashi.css">

<style>
	.outer{display:table;}
	.event_year{display:table-cell;vertical-align:middle;}
	
	@media (max-width: 1000px) {
		.event_year{width: 20%;overflow: hidden;padding: 0;}
		.event_list{width: 80%;overflow: hidden;}
	}
</style>
</head>
<body>
	<%@ include file="inc/head.jsp"%>
	<div class="sycontent">
		<%@ include file="inc/bread.jsp"%>
		<div class="outer">
			<ul class="event_year">
				<c:forEach items="${ys}" var="t" varStatus="vs">
					<li <c:if test="${vs.index ==0}">class="current"</c:if>
						 <c:if test="${vs.index ==fn:length(ys)-1}">style="border-bottom:2px solid #DDD;"</c:if>><label for="${t.key}">${t.key}</label></li>
				</c:forEach>
			</ul>
			<ul class="event_list">
			<c:forEach items="${ys}" var="t">
				<div>
					<h3 id="${t.key}">${t.key}</h3>
					<c:forEach items="${t.value}" var="s">
						<li>
							<span>${s.lcb}</span>
							<p><span><a target="_blank" href="${s.url }">${s.title }</a></span></p>
						</li>
					</c:forEach>
				</div>
			</c:forEach>
			</ul>
		</div>
	</div>
	<div style="clear:both;"></div>
	
	<c:set var="needcy" value="1"/>
	<%@ include file="inc/btm.jsp"%>

	<script>
		
		$(function(){
			$('label').click(function(){
				$('.event_year>li').removeClass('current');
				$(this).parent('li').addClass('current');
				var year = $(this).attr('for');
				$('#'+year).parent().prevAll('div').slideUp(800);
				$('#'+year).parent().slideDown(800).nextAll('div').slideDown(800);
			});
		})
	</script>
</body>
</html>

