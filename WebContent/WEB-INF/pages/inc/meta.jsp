<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<c:if test="${!empty(jj.gjc)}">
	<c:set var="wzkeys" value=""/>
	<c:forTokens items="${jj.gjc}" delims=" ,，" var="name">
	   <c:set var="wzkeys" value="${wzkeys},${name}"/>
	</c:forTokens>
	<c:if test="${!empty(wzkeys)}">
		<c:set var="wzkeys" value="${fn:substring(wzkeys,1,fn:length(wzkeys))},"/>
	</c:if>
</c:if> 

<meta name="keywords" content="${wzkeys}cms,java cms,开源cms,国产cms,SpringMVC CMS,tjpcms,最懂你的cms"/>
<meta name="description" content="${META_DES}"/>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no"/>
<meta http-equiv="pragma" content="no-cache"/>
<meta http-equiv="cache-control" content="no-cache"/>
<meta http-equiv="expires" content="0"/>
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"/>
<meta name="renderer" content="webkit"/>
<meta http-equiv="Cache-Control" content="no-siteapp" />
<meta http-equiv="windows-Target" contect="_top"/>

<link href="${path}/img/favo.ico" mce_href="${path}/img/favo.ico" rel="bookmark" type="image/x-icon" /> 
<link href="${path}/img/favo.ico" mce_href="${path}/img/favo.ico" rel="icon" type="image/x-icon" /> 
<link href="${path}/img/favo.ico" mce_href="${path}/img/favo.ico" rel="shortcut icon" type="image/x-icon" /> 

<c:if test="${empty(title) && !empty(bread) && empty(jj.title)}">
	<c:forTokens items="${bread}" delims=" ," var="name">
	   <c:set var="title" value="${title} - ${name}"/>
	</c:forTokens>
	<c:if test="${!empty(title)}">
		<c:set var="title" value="${fn:substring(title,3,fn:length(title))}"/>
	</c:if>
</c:if>

<c:if test="${empty(title) && !empty(bread) && !empty(jj.title)}">
	<c:forTokens items="${bread}" delims=" ," var="name">
	   <c:set var="title0" value="${title1}"/>
	   <c:set var="title1" value="${name}"/>
	</c:forTokens>
	<c:if test="${!empty(title0)}">
		<c:set var="title" value="${title0} - ${jj.title}"/>
	</c:if>
	<c:if test="${empty(title0)}">
		<c:set var="title" value="${jj.title}"/>
	</c:if>
</c:if>
