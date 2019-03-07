<%@ page language="java" pageEncoding="UTF-8"%>

<c:if test="${!empty(bread)}">
	<div class="bread">
		<ul>
			<li><a class="icon-shouye brdsy" href="${path }/"> 首页</a></li>
			<c:forTokens items="${bread}" delims=", |" var="name">
			   <li><a href="#">${name}</a></li>
			</c:forTokens>
		</ul>
	</div>
</c:if>


