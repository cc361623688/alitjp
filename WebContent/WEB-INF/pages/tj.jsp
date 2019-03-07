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
	<style>
		div p{font-size: 3.2em;margin:0;padding:10px 5px;border-bottom:1px solid #ccc}
		.line{border-bottom:2px solid rgb(51,51,51);font-weight: bold;}
		.shuzi{font-size: 0.8em;}
	</style>
</head>
<body>
	<div>
		<p>当天下载量(ip数)：<span class="line">${xzl}(${xzips })</span></p>
		<p>当天日志量(ip数)：<span class="line">${rzl}(${rzips })</span></p>
		<p>
			近一周下载量(总<label class="line">${zong }</label>)：<br/>
			<span class="shuzi">
				<c:set var="xzhj" value="0"/>
				<c:forEach items="${yzrqs}" var="t1" varStatus="s">
					<c:set var="xzl" value="0"/>
					<c:forEach items="${yzxzls}" var="t2" >
						<c:if test="${t2.rq==t1}">
							<c:set var="xzl" value="${t2.cnt }"/>
						</c:if>
					</c:forEach>
					<c:set var="xzhj" value="${xzl+xzhj }"/>
					${xzl }<c:if test="${s.index<=5 }">,</c:if>
				</c:forEach>
				(共<label id="xzhj" class="line"></label>)
			</span>
		</p>
		<p>
			近一周日志量(合计<label id="rzhj" class="line"></label>)：<br/>
			<span class="shuzi">
				<c:set var="rzhj" value="0"/>
				<c:forEach items="${yzrqs}" var="t1"  varStatus="s">
					<c:set var="xzl" value="0"/>
					<c:forEach items="${yzrzs}" var="t2" >
						<c:if test="${t2.rq==t1}">
							<c:set var="xzl" value="${t2.cnt }"/>
						</c:if>
					</c:forEach>
					<c:set var="rzhj" value="${xzl+rzhj }"/>
					${xzl }<c:if test="${s.index<=5 }">,</c:if>
				</c:forEach>
			</span>
		</p>
		<p>
			上一周注册用户数：<label class="line">${zcs}</label>(总<label class="line">${zczong}</label>)
		</p>
		<c:if test="${fn:length(rdmax)>0}">
			<p >
				当天阅读量前三：<br/>
				<span class="shuzi">
					<label>${rdmax[0].bz}(${rdmax[0].cnt})</label><br/>
					<label>${rdmax[1].bz}(${rdmax[1].cnt})</label><br/>
					<label>${rdmax[2].bz}(${rdmax[2].cnt})</label><br/>
				</span>
			</p>
		</c:if>
		<p style="border-width:0 !important">
			近一周弹幕数(总<label class="line">${dmzong }</label>)：<br/>
			<span class="shuzi">
				<c:set var="dmhj" value="0"/>
				<c:forEach items="${yzrqs}" var="t1" varStatus="s">
					<c:set var="dml" value="0"/>
					<c:forEach items="${yzdms}" var="t2" >
						<c:if test="${t2.rq==t1}">
							<c:set var="dml" value="${t2.cnt }"/>
						</c:if>
					</c:forEach>
					<c:set var="dmhj" value="${dml+dmhj }"/>
					${dml }<c:if test="${s.index<=5 }">,</c:if>
				</c:forEach>
				(共<label id="dmhj" class="line"></label>)
			</span>
		</p>
	</div>
    <script type="text/javascript">
		document.getElementById("xzhj").innerHTML="${xzhj}"
		document.getElementById("rzhj").innerHTML="${rzhj}"
		document.getElementById("dmhj").innerHTML="${dmhj}"
	</script>
</body>
</html>
       