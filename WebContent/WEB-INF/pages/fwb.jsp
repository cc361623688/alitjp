<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<% request.setAttribute("path", request.getContextPath());%>

<!doctype html>
<html>
<head>

<%@ include file="inc/meta.jsp"%>
<title>tjpcms - ${title}</title>
<link rel="stylesheet" href="${path }/css/cssfwb.css" type="text/css"/>

<style>
#cyReward .cy-reward-btn {
    width: 134px;
    height: 49px;
    display: inline-block;
    background: url(${path}/images/tjpcms/juanz.png) no-repeat;
    border: 0;
    cursor: pointer;
}

#cyReward .cy-reward-btn:hover{
	background: url(${path}/images/tjpcms/juanz.png) no-repeat -134px 0;
}

#cyReward #cy-reward-pop .cy-close-btn {
            width: 11px;
            height: 10px;
            position: absolute;
            top: 17px;
            right: 14px;
            background: url('${path}/images/tjpcms/jz_close.jpg');
            cursor: pointer;
}

#cyReward #cy-reward-pop .cy-reward-title {
            width: 100%;
            height: 75px;
            text-align: center;
            line-height: 66px;
            color: #a3a3a3;
            font-size: 16px;
            background: url('${path}/images/tjpcms/jz_rewbg.jpg');
            font-family: 'Microsoft YaHei';
            margin-top: 7px;
}

#cyReward #cy-reward-pop .cy-reward-code {
            width: 130px;
            height: 130px;
            background: url('${path}/images/tjpcms/jz_cdbg.jpg');
            margin: 22px auto 0;
            padding: 17.5px;
            position: relative;
            box-sizing: content-box;
}

#cyReward #cy-reward-pop .platform-choice .platform-item .platform-radio-box {
            width: 16px;
            height: 16px;
            background: url('${path}/images/tjpcms/jz_rdo2.jpg');
            display: block;
            float: left;
            margin-top: 5px;
            margin-right: 14px;
}

#cyReward #cy-reward-pop .platform-choice .platform-item .platform-checked {
            background: url('${path}/images/tjpcms/jz_rdo1.jpg');
}

#cyReward #cy-reward-pop .platform-choice .alipay {
            background-image: url('${path}/images/tjpcms/jz_lx1.jpg');
}

#cyReward #cy-reward-pop .platform-choice .weichat {
            background-image: url('${path}/images/tjpcms/jz_lx2.jpg');
}
</style>

</head>

<body>
	<%@ include file="inc/head.jsp"%>
	<c:if test="${empty(needcy) }">
		<c:set var="needcy" value="1"/>
	</c:if>
	
	<div class="sycontent">
		<%@ include file="inc/bread.jsp"%>
		<div style="margin-bottom: 8px;">
			<div class="title">${jj.title}</div>
			<div class="subxx">
				<span style="margin-right: 15px;" class="yuedu icon-dazhongicon02"> 0次</span><c:if test="${needcy==1}"><a href="javascript:" class="cypara icon-partake" onclick="fn_pinglun()">
					<span id = "changyan_parti_unit" class="cy_cmt_participate" >0</span>人次
				</a>
				<a href="javascript:" class="cypara icon-pinglun" onclick="fn_pinglun()">
					<span id = "changyan_count_unit" class="cy_cmt_count">0</span>人
				</a></c:if>
				<span class="riqi icon-liulanjilu" title="更新日期${fn:substring(jj.gx,0,10) }"> ${fn:substring(jj.gx,0,10) }</span></div>
		</div>
		<div class="jjnr">
			${jj.nr}
		</div>
		<c:if test="${needcy==1}">
			<div id="cyReward">
				<div id="cy-reward-click" class="cy-reward-btn cy-reward-btn-red"></div>
				<div id="cy-reward-pop" style="display: none;">
					<div node-type="cy-close-btn" class="cy-close-btn"></div>
					<img class="cy-avatar" src="${path}/images/tjpcms/jz_tx.png">
					<div class="cy-reward-title">感谢你的支持，我会继续努力！</div>
					<div node-type="cy-reward-code" class="cy-reward-code">
						<img style="display:block;"	src="${path}/images/tjpcms/jz_zfb.png"> 
						<img src="${path}/images/tjpcms/jz_wx.jpg">
					</div>
					<div class="cy-reward-txt">扫码打赏，感谢点滴支持</div>
					<div node-type="platform-choice" class="platform-choice clearfix">
						<div class="platform-item">
							<span class="platform-radio-box platform-checked"></span> 
							<span class="platform-logo alipay"></span>
						</div>
						<div class="platform-item">
							<span class="platform-radio-box"></span>
							 <span class="platform-logo weichat"></span>
						</div>
					</div>
				</div>
			</div>
		</c:if>
	</div>
	
	<%@ include file="inc/btm.jsp"%>
	
	<script>
		function fn_pinglun(){
			if ($(".post-wrap-main").length>0 || $(".comment-textarea").length>0){
				var gt = $("#SOHUCS").position().top
				var off = $(".nav").hasClass('nvfix')?0:$('.nav').height();
	   			$('body,html').animate({scrollTop:gt-45-off}, function(){
	   				var e = $(".post-wrap-main").length>0 ? $(".post-wrap-main") :  $(".comment-textarea");
	   				var le = e.offset().left;
	   				var ri = le+e.width();
	   				var tp = e.offset().top;
	   				var bm = tp+e.height();
	   				layer.msg('来说两句吧...',{offset:[((tp+bm)/2-24-$(document).scrollTop())+'px','']})
	   				$(".textarea-fw").focus()
	   			})
			}else{
				layer.msg('评论框加载中，请稍后...')
			}
		}
	
		$(function() {
			//畅言
			$(".cypara").hover(function(){
				var iscanyu = $(this).hasClass('icon-partake');
				$(this).attr('title', (iscanyu?'参与':'评论')+$(this).children('span').text()+'人'+(iscanyu?'次':''))
			})

			$("#cy-reward-click").click(function(){
				if ($(window).width()>1000){
					$("#cy-reward-pop").toggle();
				}else{
					location.href='${path}/jz.dhtml'
				}
			})
			
			$(".platform-radio-box").click(function(){
				$(".platform-radio-box").removeClass('platform-checked');
				$(this).addClass('platform-checked');
				$(".cy-reward-code>img").hide();
				$(".cy-reward-code>img:eq("+$(".platform-radio-box").index(this)+")").show();
			})
			
			$(".cy-close-btn").click(function(){
				$("#cy-reward-pop").hide();
			})

			//为了方便静态化，所以动态获取阅读次数
			$.ajax({
				type:"POST",
				url:'${path}/fwbgetcs.dhtml',
				data:{u:location.href, title:'${jj.title}'},
				datatype: "TEXT",
				success:function(ret){
					if (!isNaN(parseInt(ret))){
						$(".yuedu").text(" "+ret+"次").attr("title","查看"+ret+"次")
					}
				}
			});
		})
	</script>
	<script type="text/javascript" src="http://assets.changyan.sohu.com/upload/plugins/plugins.count.js"></script>
</body>
</html>

