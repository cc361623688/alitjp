<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<% request.setAttribute("path", request.getContextPath());%>

<!doctype html>
<html>
<head>
<title>tjpcms - 关于tjpcms</title>
<%@ include file="inc/meta.jsp"%>

<style>
	.kaic{
		border: solid 1px #5a98de;border-left-width: 6px;width: 80%;margin: 0 auto;
	    padding: 15px;
	    text-align: justify !important;
	}
	.bdright{border-left-width: 1px;border-right-width: 6px;}
	.txright{text-align: right;}
	.kaic strong{
	    font-size: 18px;
	    margin-bottom: 10px;color:#5a98de;border-bottom:1px solid #5a98de;
	}
	.kaic p{
		line-height: 24px;/* text-indent: 2em; */color: #6A5ACD;font-size: 15px;text-shadow:rgba(0,0,0,0.2) 0 0 1px !important;text-align: justify !important;
	}
	.kaic p a{
		font-size: 13px;text-decoration: underline;padding: 0 3px;
	}
	.sycontent>div{margin-bottom: -1px;}
	.kaic>div{padding-bottom: 10px;}
	.toprs{-webkit-border-radius: 5px 5px 0 0 ;-moz-border-radius: 5px 5px 0 0 ;border-radius: 5px 5px 0 0 ;}
	.btmrs{-webkit-border-radius: 0 0 5px 5px ;-moz-border-radius:0 0  5px 5px ;border-radius:0 0  5px 5px;}
</style>

</head>
<body>
	<%@ include file="inc/head.jsp"%>
	
	<div class="sycontent">
		<div class="bread">
			<ul>
				<li><a class="icon-shouye brdsy" href="${path }/"> 首页</a></li>
				<li><a href="#">关于tjpcms</a></li>
			</ul>
		</div>
		<div class="kaic toprs ">
			<div ><strong>tjpcms有什么用？</strong></div>
			<p style="text-indent: 2em;">这是所有读者最核心的疑问！网上的开源框架多如牛毛，但往往讲了一大堆，读者也没搞清楚框架究竟能帮使用者做什么。tjpcms最核心的作用就是：帮助java web开发者以动态配置的方式完成增删改查的页面及功能。具体是个什么意思呢，可以举个栗子看一下<a target="_blank" href="${path }/jiandan_detail.dhtml?id=159">tjpcms动态如何配置增删改查</a></p>
		</div>
		<div class="kaic bdright">
			<div class="txright"><strong >下载下来的tjpcms怎么用？</strong></div>
			<p style="text-indent: 2em;">下载下来的压缩包是工程源码及数据库文件，将源码导入到MyEclipse中，数据库导入到Mysql中，再配置一下工程中数据链接，就可以访问啦！具体可见 <a target="_blank" href="${path }/spjc.dhtml">视频教程</a> 和 <a target="_blank" href="${path }/huanjing_detail.dhtml?id=146">环境配置</a></p>
		</div>
		<div class="kaic ">
			<div class=""><strong style="text-align: right;">tjpcms用到或参考了哪些组件和框架？</strong></div>
			<p style="text-indent: 2em;">底层是Java，数据库是Mysql，开发框架是spring MVC，数据库层是MyBatis，后端用到的都是很大众化的。前端主要用到或参考了ueditor、layui、artTemplate、pintuer、ligerUI、iconfont、Ztree等，前端综合要求更高。</p>
		</div>
		<div class="kaic bdright">
			<div class="txright"><strong>tjpcms能帮我做什么？</strong></div>
			<p style="text-indent: 2em;">通俗来说，其实就是帮助你以最便捷的方式将各种类型的数据规范入库，尽可能少写代码，越少越好，做到极致，功能及页面都靠配置来完成，且能够即时修改即时显示，再配合一些工具及技巧（也因此我称之为解决方案而非框架）就极大地提升了效率。cms只管后台的数据入库，显示由前台负责，因此可互不干扰同步开发，具体参见教程。</p>
		</div>
		<div class="kaic" >
			<div class=""><strong>tjpcms相比其他cms有什么优势？</strong></div>
			<p>1、实时配置增删改查。以后台友情链接功能为例，增删改查、校验、图片上传等功能及相关页面，仅需12行代码完成<a target="_blank" href="${path}/jiandan_detail.dhtml?id=152">最普通的单表（友情链接页面）</a></p>
			<p>2、官方JSTL模板。前台模板采用官方的JSTL，不需要额外学习其他模板如FreeMarker、Smarty或其他cms自行开发的小众模板，用JSTL即可实现整站及局部静态化，显著降低了学习成本。</p>
			<p>3、极简设计，提倡减法。坚决避免同类产品的复杂繁冗，仅保留构成cms所需要的最基本功能和表单参数，功能和页面力求简洁易懂，以简为美，由此极大地方便了二次开发。</p>
			<p>4、扎实的教程。作者从切身体会出发，深知配套教程的重要性，因此精心编写了一组使用文档，亲测每一个步骤，力求为用户的使用提供最大程度的方便。
		</div>
		<div class="kaic bdright" >
			<div class="txright"><strong style="text-align: right;">${banquan.title }</strong></div>
			<p style="text-indent: 2em;">${banquan.nr }</p>
		</div>
		<div class="kaic  btmrs" style="margin-bottom: 20px;">
			<div class=""><strong>tjpcms的理念有哪些？</strong></div>
			<p>1、面向的受众是开发者而非普通用户。专业的事情交给专业的人去做，要使用tjpcms必须会java web开发，普通用户是无法使用tjpcms的。</p>
			<p>2、纯信息发布类的cms网站没有生命力。网站要能够发挥实际作用、解决痛点问题就必须要整合业务进去，所以基于cms的二次开发是常态。</p>
			<p>3、功能少了不方便，多了又复杂，但少总比多好。需求是千变万化，甚至意想不到的，很多功能可能用户根本就用不上，还不如没有。</p>
		</div>
	</div>
	
	<c:set var="needcy" value="1"/>
	<%@ include file="inc/btm.jsp"%>
	
	<script>

	</script>
</body>
</html>

