<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<link rel="stylesheet" href="${path}/css/index.css">
<script type="text/javascript" src="${path}/js/jquery.js"></script>
<script type="text/javascript" src="${path }/js/jquery.SuperSlide.2.1.1.js"></script>
<script type="text/javascript" src="${path}/js/cmn.js"></script>
<script type="text/javascript" src="${path}/js/template.js"></script>

<style>
*{font-family: "Microsoft YaHei","sans-serif"};
body{
	background: url("data:image/jpg;base64,/9j/4QAYRXhpZgAASUkqAAgAAAAAAAAAAAAAAP/sABFEdWNreQABAAQAAABkAAD/4QPiaHR0cDovL25zLmFkb2JlLmNvbS94YXAvMS4wLwA8P3hwYWNrZXQgYmVnaW49Iu+7vyIgaWQ9Ilc1TTBNcENlaGlIenJlU3pOVGN6a2M5ZCI/PiA8eDp4bXBtZXRhIHhtbG5zOng9ImFkb2JlOm5zOm1ldGEvIiB4OnhtcHRrPSJBZG9iZSBYTVAgQ29yZSA1LjMtYzAxMSA2Ni4xNDU2NjEsIDIwMTIvMDIvMDYtMTQ6NTY6MjcgICAgICAgICI+IDxyZGY6UkRGIHhtbG5zOnJkZj0iaHR0cDovL3d3dy53My5vcmcvMTk5OS8wMi8yMi1yZGYtc3ludGF4LW5zIyI+IDxyZGY6RGVzY3JpcHRpb24gcmRmOmFib3V0PSIiIHhtbG5zOnhtcFJpZ2h0cz0iaHR0cDovL25zLmFkb2JlLmNvbS94YXAvMS4wL3JpZ2h0cy8iIHhtbG5zOnhtcE1NPSJodHRwOi8vbnMuYWRvYmUuY29tL3hhcC8xLjAvbW0vIiB4bWxuczpzdFJlZj0iaHR0cDovL25zLmFkb2JlLmNvbS94YXAvMS4wL3NUeXBlL1Jlc291cmNlUmVmIyIgeG1sbnM6eG1wPSJodHRwOi8vbnMuYWRvYmUuY29tL3hhcC8xLjAvIiB4bXBSaWdodHM6TWFya2VkPSJGYWxzZSIgeG1wTU06T3JpZ2luYWxEb2N1bWVudElEPSJhZG9iZTpkb2NpZDpwaG90b3Nob3A6N2FiY2EzMTQtYWQ5OS0xMWUzLWIxNDUtODE4MzNlYWQ1ZTlkIiB4bXBNTTpEb2N1bWVudElEPSJ4bXAuZGlkOkVEQzZDMzlGMTM0OTExRTRCMTIwRDEzMjFDMEYxNUNBIiB4bXBNTTpJbnN0YW5jZUlEPSJ4bXAuaWlkOkVEQzZDMzlFMTM0OTExRTRCMTIwRDEzMjFDMEYxNUNBIiB4bXA6Q3JlYXRvclRvb2w9IkFkb2JlIFBob3Rvc2hvcCBDUzYgKFdpbmRvd3MpIj4gPHhtcE1NOkRlcml2ZWRGcm9tIHN0UmVmOmluc3RhbmNlSUQ9InhtcC5paWQ6QzJDOUFBRTIzODEzRTQxMUI0MThBMTQwRkMwMEM3QTAiIHN0UmVmOmRvY3VtZW50SUQ9ImFkb2JlOmRvY2lkOnBob3Rvc2hvcDo3YWJjYTMxNC1hZDk5LTExZTMtYjE0NS04MTgzM2VhZDVlOWQiLz4gPC9yZGY6RGVzY3JpcHRpb24+IDwvcmRmOlJERj4gPC94OnhtcG1ldGE+IDw/eHBhY2tldCBlbmQ9InIiPz7/7gAOQWRvYmUAZMAAAAAB/9sAhAABAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAgICAgICAgICAgIDAwMDAwMDAwMDAQEBAQEBAQIBAQICAgECAgMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwP/wAARCAAGAAYDAREAAhEBAxEB/8QATgABAAAAAAAAAAAAAAAAAAAACgEBAAAAAAAAAAAAAAAAAAAAABAAAwEBAAAAAAAAAAAAAAAAAQIDBQQRAQAAAAAAAAAAAAAAAAAAAAD/2gAMAwEAAhEDEQA/AHoSr0HnsTHUBGX0uA/NpChoM3FZURW2noeo0dgqhlqarQCiutKVD//Z");
}
a:hover{color: #09c}
.sycontent{
	width:998px;margin: 0 auto;padding:  0;border: 1px solid rgb(189,215,242);-webkit-border-radius: 0 0 4px 4px;
	-moz-border-radius: 0 0 4px 4px;
	border-radius: 0 0 4px 4px;
}
.head{
	width: 1000px;margin: 0 auto;position: relative;
}
.topbar {
	margin: 0 auto;text-indent: 1px;
    width: 100%;color: #828282;font-size: 12px;line-height: 27px;
}
.banner{
	height: 80px;line-height: 80px;margin-left: 6px;position: relative;
}
.icon-peach:before{font-size: 60px;}
.ring-hover {-webkit-animation: 1s ease;-moz-animation: 1s ease;-ms-animation: 1s ease;animation: 1s ease;}
.ring-hover:hover {-webkit-animation-name: a-ring;-moz-animation-name: a-ring;-ms-animation-name: a-ring;animation-name: a-ring;}
@-webkit-keyframes a-ring {0% {-webkit-transform: scale(1);}10%,20% {-webkit-transform: scale(0.9) rotate(-3deg);}30%,50%,70%,90% 
{-webkit-transform: scale(1.1) rotate(3deg);}40%,60%,80% {-webkit-transform: scale(1.1) rotate(-3deg);}100% {-webkit-transform: scale(1) rotate(0);}}
@-moz-keyframes a-ring {0% {-moz-transform: scale(1);}10%,20% {-moz-transform: scale(0.9) rotate(-3deg);}30%,50%,70%,90% {-moz-transform: scale(1.1) 
rotate(3deg);}40%,60%,80% {-moz-transform: scale(1.1) rotate(-3deg);}100% {-moz-transform: scale(1) rotate(0);}}
@-ms-keyframes a-ring {0% {-ms-transform: scale(1);}10%,20% {-ms-transform: scale(0.9) rotate(-3deg);}30%,50%,70%,90% {
-ms-transform: scale(1.1) rotate(3deg);}40%,60%,80% {-ms-transform: scale(1.1) rotate(-3deg);}100% {-ms-transform: scale(1) rotate(0);}}
@keyframes a-ring {0% {transform: scale(1);}10%,20% {transform: scale(0.9) rotate(-3deg);}30%,50%,70%,90% {transform: scale(1.1) rotate(3deg);}
40%,60%,80% {transform: scale(1.1) rotate(-3deg);}100% {transform: scale(1) rotate(0);}}
.slogan{font-size: 34px;vertical-align: top;position: absolute;left:237px;color:rgb(21,123,195);
text-shadow :  rgba(200,200,200,1) 3px 3px 20px;font-family: '幼圆','Microsoft YaHei';
}
.nav{height: 45px;line-height: 45px;background: rgb(21,123,195);border-radius:2px;
   -webkit-box-shadow: 0 4px 8px rgba(0, 0, 0, .175);width: 100%;
    box-shadow: 0 4px 8px rgba(0, 0, 0, .175);z-index: 100;position: relative;
}
.nav>ul >li{position: relative;float: left;width: 111px;text-align: center;background: url("data:image/jpg;base64,/9j/4QAYRXhpZgAASUkqAAgAAAAAAAAAAAAAAP/sABFEdWNreQABAAQAAABkAAD/4QPiaHR0cDovL25zLmFkb2JlLmNvbS94YXAvMS4wLwA8P3hwYWNrZXQgYmVnaW49Iu+7vyIgaWQ9Ilc1TTBNcENlaGlIenJlU3pOVGN6a2M5ZCI/PiA8eDp4bXBtZXRhIHhtbG5zOng9ImFkb2JlOm5zOm1ldGEvIiB4OnhtcHRrPSJBZG9iZSBYTVAgQ29yZSA1LjMtYzAxMSA2Ni4xNDU2NjEsIDIwMTIvMDIvMDYtMTQ6NTY6MjcgICAgICAgICI+IDxyZGY6UkRGIHhtbG5zOnJkZj0iaHR0cDovL3d3dy53My5vcmcvMTk5OS8wMi8yMi1yZGYtc3ludGF4LW5zIyI+IDxyZGY6RGVzY3JpcHRpb24gcmRmOmFib3V0PSIiIHhtbG5zOnhtcFJpZ2h0cz0iaHR0cDovL25zLmFkb2JlLmNvbS94YXAvMS4wL3JpZ2h0cy8iIHhtbG5zOnhtcE1NPSJodHRwOi8vbnMuYWRvYmUuY29tL3hhcC8xLjAvbW0vIiB4bWxuczpzdFJlZj0iaHR0cDovL25zLmFkb2JlLmNvbS94YXAvMS4wL3NUeXBlL1Jlc291cmNlUmVmIyIgeG1sbnM6eG1wPSJodHRwOi8vbnMuYWRvYmUuY29tL3hhcC8xLjAvIiB4bXBSaWdodHM6TWFya2VkPSJGYWxzZSIgeG1wTU06T3JpZ2luYWxEb2N1bWVudElEPSJhZG9iZTpkb2NpZDpwaG90b3Nob3A6N2FiY2EzMTQtYWQ5OS0xMWUzLWIxNDUtODE4MzNlYWQ1ZTlkIiB4bXBNTTpEb2N1bWVudElEPSJ4bXAuZGlkOkYzNEZFNzZEMTM0QTExRTRBMjY4Q0JDODVCN0Y4QTI2IiB4bXBNTTpJbnN0YW5jZUlEPSJ4bXAuaWlkOkYzNEZFNzZDMTM0QTExRTRBMjY4Q0JDODVCN0Y4QTI2IiB4bXA6Q3JlYXRvclRvb2w9IkFkb2JlIFBob3Rvc2hvcCBDUzYgKFdpbmRvd3MpIj4gPHhtcE1NOkRlcml2ZWRGcm9tIHN0UmVmOmluc3RhbmNlSUQ9InhtcC5paWQ6QzJDOUFBRTIzODEzRTQxMUI0MThBMTQwRkMwMEM3QTAiIHN0UmVmOmRvY3VtZW50SUQ9ImFkb2JlOmRvY2lkOnBob3Rvc2hvcDo3YWJjYTMxNC1hZDk5LTExZTMtYjE0NS04MTgzM2VhZDVlOWQiLz4gPC9yZGY6RGVzY3JpcHRpb24+IDwvcmRmOlJERj4gPC94OnhtcG1ldGE+IDw/eHBhY2tldCBlbmQ9InIiPz7/7gAOQWRvYmUAZMAAAAAB/9sAhAABAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAgICAgICAgICAgIDAwMDAwMDAwMDAQEBAQEBAQIBAQICAgECAgMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwP/wAARCAAsAAMDAREAAhEBAxEB/8QAeAAAAgMAAAAAAAAAAAAAAAAAAAQDBQgBAAEFAAAAAAAAAAAAAAAAAAQBAgMFCRAAAAIHBAgHAAAAAAAAAAAAERMAITFBEhQVkQIislFhcTKCkiQ1VWUWJkY3hxEAAgICAgMAAAAAAAAAAAAAABEBEqGkIaITo7P/2gAMAwEAAhEDEQA/AMWl3giBUBnDGWPMpNqnBhqi2Lu0uZ8nMF0Xq+laBaprbEFvHuRJSerJA9omPoYi/wC7pbIrYlfad1BdY12Hxol9CZ+sT+XEkvhnbsNv8EJn9EWrtxAL8fmrXpZ0jLBHPUQxQvCHXum2BGjxOcH/2Q==") no-repeat right top;}
.nav>ul >li>a{text-decoration: none;color:white;width: 100%;display: block;font-size: 17px;font-weight: bold;}
.nav>ul >li>a:hover,.nav>ul >li>a.cur{color: #fff !important;background:url(${path}/images/nav_19.jpg) no-repeat;}
.downward{
    display: inline-block;
    width: 0;position: absolute;top:22px;
    height: 0;
    vertical-align: middle;
    border-top: 4px solid;
    border-right: 4px solid transparent;
    border-left: 4px solid transparent;margin-left: 2px;
}
.rightward {
    display: inline-block;
    width: 0;
    height: 0;
    margin-left: 2px;
    vertical-align: middle;
    border-left: 4px solid;
    border-top: 4px solid transparent;
    border-bottom: 4px solid transparent;
}
.sub1,.sub2{
	position: absolute;z-index:2;
	border-radius:0 0 4px 4px;
   -webkit-box-shadow: 0 5px 12px rgba(0, 0, 0, .3);    border: 1px solid #a9a9a9;
    box-shadow: 0 5px 12px rgba(0, 0, 0, .3);
    display:none;background:rgb(255,250,250) /* rgb(52,140,204); */
}
.sub1 li{   position: relative;}
.sub1 li,.sub2 li{     border-bottom: 1px solid #e4e4e4;width: 109px;}
.sub2{position: absolute;left:100%;top:-1px}
.sub1 a,.sub2 a{
	color:#247eab;display: block;height: 37px;line-height: 37px;text-decoration: none;
}
.sub1 a:hover{
	background:#ffaa31;color:white
}
.ring-hover{position: absolute;}
.nvfix{position: fixed;width: 1000px;}
.top0{top:0 !important}
.icsycls{color:#A52A2A;letter-spacing:0px;margin-right: 5px;float: left;}
#id_span_zcdl{float: left;margin-left: 3px;}
.icsycls:before, .icon-cguanliyuan:before,.icon-tuichu:before{color: rgb(21,123,195);    margin-right: 2px;font-size: 18px;}
.icon-cguanliyuan:before{font-size: 15px;margin-left: 12px;}
.bread{
    padding: 8px 15px;
    margin-bottom: 20px !important;
    list-style: none;
    background-color: #f5f5f5;
    border-radius: 4px;border-bottom: 1px solid #eee;
}
.bread a{color: #428bca;text-decoration: none;}
.bread li{display:inline-block;}
.bread>ul>li+li:before {    padding: 0 5px; color: #ccc;content: "/\00a0";}
.brdsy:before {position: relative;top: -1px;}
.pagin{position: relative;width: 90%;margin: 0 auto 30px;}
.fymsg{display: inline-block;padding: 10px;font-size: 12px;}
#fenye{position: absolute;right:0;display: inline-block;padding-top: 4px;}
.laypage_btn{font-family: 'Microsoft YaHei';cursor: pointer;}
.fymsg i {
    color: #056dae;
    font-style: normal;
    font-weight: bold;
    padding: 0 2px;
}
.nav1k{display: none;}
#usrshenfen{font-size: 13px;}

.icon-zhuce1:before { content: "\e660"; color: rgb(21,123,195);    margin-right: 2px;margin-left: 12px;font-size: 19px;}
.icon-mimadenglu:before { content: "\e6ab"; color: rgb(21,123,195);   font-size: 20px;}
.clsyhtx{width:26px;position: relative;top:0px;cursor: pointer;vertical-align: super;    border-radius: 50%;max-height:27px}
.icon-cguanliyuan:before{margin-right: 0 !important; margin-left: 8px !important;}
.shownc {vertical-align:super;cursor: pointer;max-width: 115px;overflow: hidden;white-space:nowrap;text-overflow:ellipsis;display: inline-block;}
.xwblkmr{margin-right: 0 !important;}

@media (max-width: 1000px) {
	.sycontent,.head{width:99%;margin: 0 auto;}
	.banner{height: 45px;line-height: 40px;}
	.slogan{font-size: 22px;left:80px;}
	.ring-hover{width: 48px !important;margin-left: 16px;top:-2px !important;left:0 !important;margin-top: 0 !important}
	.nav{height: 40px;line-height: 41px;/* overflow: hidden; */position: relative;}
	.nav>ul >li>a{font-size: 14px;font-weight: normal;}
	.nav>ul >li>a:hover,.nav>ul >li>a.cur{background:url(${path}/images/nav_19_80.jpg) no-repeat;}
	.nav>ul {position: relative;}
	.nav>ul >li{display: none;width: 80px;background:url(${path}/images/nav_20_39.jpg) no-repeat right top;}
	.downward{top: 18px;}
	.yqlj{ border-top: none}
	.ljulout{ border-top: 1px solid rgb(189,215,242);width: 99%;padding: 0;}
	.ljnr{padding: 7px 4px 7px;}
	a{color:rgb(85, 26, 139);}
	.nav1k{position: absolute;display: inline-block;
		width: 32px;height: 40px;z-index: 9999;opacity:0.25}
	.leftarr{left:0;background: url(${path}/images/nav_1000.png) no-repeat 1px -6px;}
	.rightarr{right:0;background: url(${path}/images/nav_1000.png) no-repeat -1px -55px;}
	.sub1 a,.sub2 a{font-size: 12px !important;height: 29px;line-height: 28px;}
	.sub1 li, .sub2 li {width: 79px;}
	.xwblkmr{margin-right: 18px !important;}
	.form-wrapper{display: none;}
}

</style>
<div class="head">
	<div style="background: rgb(247,247,247);border-bottom:1px solid rgb(238,238,238);height: 29px;padding: 1px;">
		<div class="topbar">
			<span class="icon-shouye icsycls"> 
				tjpcms欢迎你<%-- <label id="usrshenfen"><c:out value="${!empty(ses_usrpt)?'':'，游客'}" /></label> --%>
			</span>
			<span id="id_span_zcdl"></span>
			<span style="float: right;margin-top: 3px;margin-right: 2px;">
				<a target="_blank" href="http://shang.qq.com/wpa/qunwpa?idkey=9025ac47d03fec43dd8b0fdf5fa7bfdd257ac645fe3ee3f32f01741f5260c2cb"><img border="0" src="http://pub.idqqimg.com/wpa/images/group.png" alt="tjpcms 一 最懂你的cms" title="tjpcms 一 最懂你的cms"></a>
			</span>
			<span style="float: right;margin-top: 2px;margin-right: 1px;">
				<a target="_blank" href="http://wpa.qq.com/msgrd?v=3&uin=57454144&site=qq&menu=yes"><img border="0" src="http://wpa.qq.com/pa?p=2:57454144:52" alt="联系作者" title="联系作者"/></a>
			</span>
		</div>
	</div>
	<div style="clear:both"></div>
	<div class="banner" >
			<a href="${path}/">
				<img class="ring-hover" style="margin-top: 8px;margin-right: 10px;;width:66px;top:-100px" src="${path}/images/tjpcms/logo4.png" alt="" />
			</a>
		<span class="slogan">
			tjpcms 一 最懂你的cms
		</span>
		<form class="form-wrapper" action="${path}/sousuo.dhtml">
			<input type="text" id="search" name="gjc" placeholder="搜索站内信息..." maxlength="22">
			<input type="button" id="submit1" value="搜索" onclick="fn_sousuo(this)">
		</form>
	</div>
	<div style="clear:both"></div>
	<div class="nav" onselectstart="return false">
		<a href="javascript:" class="nav1k leftarr"></a>
		<a href="javascript:" class="nav1k rightarr"></a>
		<ul >
			<li>
				<a href="${path}/" title="网站首页" <c:if test="${path==rqu}">class="cur"</c:if>>首页</a>
			</li>
			<c:forEach items="${lms}" var="itm" varStatus="status">
				<li <c:if test="${status.index==fn:length(lms)-1}">style="background:none"</c:if>>
					<a href="${!empty(itm.url)?(itm.url):'javascript:'}" <c:if test="${itm.issel==1}">class="cur"</c:if>>
						${itm.name}
						<c:if test="${fn:length(itm.zlm)>0}"><span class="downward"></span></c:if>
					</a>
					<c:if test="${fn:length(itm.zlm)>0}">
						<ul class="sub1"  >
							<c:forEach items="${itm.zlm}" var="itm1" varStatus="status1">
								<li <c:if test="${status1.index==fn:length(itm.zlm)-1}">style="border:0"</c:if>>
									<a href="${!empty(itm1.url)?(itm1.url):'javascript:'}">
										${itm1.name}
										<c:if test="${fn:length(itm1.zlm)>0}"><span class="rightward"></span></c:if>
									</a>
									<c:if test="${fn:length(itm1.zlm)>0}">
										<ul class="sub2"  >
											<c:forEach items="${itm1.zlm}" var="itm2" varStatus="status2">
												<li >
													<a href="${itm2.url }">${itm2.name}</a>
												</li>
											</c:forEach>
										</ul>
									</c:if>
								</li>
							</c:forEach>
						</ul>
					</c:if>
				</li>
			</c:forEach>
		</ul>
	</div>
</div>


<script id="sid_dl_no" type="text/html">
	<a href="javascript:" style="vertical-align:super" id="id_top_zc" style="margin-right:1px" onclick="fn_zhuce()">注册</a>
	<a href="javascript:" style="vertical-align:super" onclick="fn_topdl()" title="登录tjpcms">登录</a>
</script>
<script id="sid_dl_yes" type="text/html">
	{{if tx!=null && tx!=undefined && tx!=''}}
		<img src="{{tx}}" alt="头像"  title="点击打开{{nc}}的个人中心" class="clsyhtx" onclick="$(this).next().children('a').trigger('click')"/>
		<span class="shownc"><a href="javascript:" onclick="fn_grzx()">{{nc}}</a></span>
	{{else}}
		<span class='shownc'  title="点击打开{{nc}}的个人中心">
			<a href="javascript:" onclick="fn_grzx()" class="icon-cguanliyuan ">&nbsp;{{nc}}&nbsp;</a>
		</span>
	{{/if}}
	<a href="javascript:" style="float:right;margin-left:3px" onclick="fn_ssotc()" title="退出网站登录">退出</a>
</script>


<script>
	//个人中心
	function fn_grzx(){
		location.href='${path}/g2grzx.dhtml'
	}
	
	//用户在个人网站退出时，畅言端也同步退出
	function fn_ssotc(){
		fn_ntp_aj('${path}/ssousr_tc.dhtml',null,function(){
			var img = new Image();
			img.src='http://changyan.sohu.com/api/2/logout?client_id=cysHEHjOS';
			setTimeout(function(){location.reload()},50);//如果是静态化页面的话要延迟一下，因为加载太快，会导致上面的语句还没执行好
		})
	}

	function fixNav(){
		if( $(document).scrollTop() >0 ){
			if ($('.nav').is(":animated")){//动画中
				
			}else{
				$('.nav').addClass('nvfix').animate({top:0},function(){
					//console.log("动画结束$(document).scrollTop()"+$(document).scrollTop())
					if ($(document).scrollTop()>0) $('.nav').addClass('top0')
					else $('.nav').removeClass('nvfix top0')
					$('.nav').removeAttr('style')
				})
			}
		}else{
			$('.nav').removeClass('nvfix').removeClass('top0').removeAttr('style')
		}
	}
	
	
	var mu_tmrid = null;
	function hideMenu(tt){
		if (tt){
			tt.siblings().find("ul").stop(false, true).hide();
			tt.find("ul").slideUp()
		}
	}
	
	function fn_sousuo(tag){
		var val = $(tag).prev().val();
		if (_epp(val)){
			layer.msg('请输入要搜索的内容！')
		}else{
			$(tag).parent().submit();
		}
	}

	$(function(){
		$("#search").keyup(function(){
			var len = $(this).val().length
			if (len>=22){
				layer.msg('搜索关键词最多22个字！')
			}
		})

		//获取登录状态
		$.ajax({
			type:"POST",
			url:"${path}/aj_getdls.dhtml",
			datatype: "JSON",
			success:function(json){
				var obj = eval("("+json+")");
				if (obj.nc){
					var html = template('sid_dl_yes', obj);
					$("#id_span_zcdl").empty().html(html);
					if (location.pathname=="/alitjp/" || location.pathname=="/alitjp/index.dhtml" ||  location.pathname=="/alitjp/static/index.html"){
						layer.tips('点击可进入【个人中心】', $(".shownc"), {tips : [ 1, '#3595CC' ], tjpcms_clscont:'touming50'});
					}
				}else{
					var html = template('sid_dl_no', {});
					$("#id_span_zcdl").empty().html(html)
					var zctipid=null;
					$("#id_top_zc").on("mouseover mouseout", function(event) {
						if (event.type == "mouseover") {
							zctipid = layer.tips('注册后可投稿、享受专属服务', $("#id_top_zc"), {tips : [ 1, '#3595CC' ]});
						} else if (event.type == "mouseout") {
							layer.close(zctipid)
						}
					})
				}

				if (obj.hds){
					$.each(obj.hds, function(idx,obj){
						if(obj.lx=='首页下载') $(".xzcnt").text(obj.cnt);
						else if (obj.lx=='首页点赞') $(".zancnt").text(obj.cnt).attr("title","累计赞"+obj.cnt+"次")
					})
				}

/* 				if (obj.tip){
					layer.ready(function(){//http://fly.layui.com/jie/1724.html
						layer.msg(obj.tip)
					})
				} */
				
			},
			error: function(){
				layer.msg('获取登录状态失败，请刷新后重试！', { icon:2, title:'错误'});
			}
		});
		
	
		$("body").click(function(ev) {
			if ($(ev.target).parents(".nav").length <= 0) {
				$(".nav>ul li").children("ul").hide()
			}
		})

		if ($(window).width() > 1000) {
			//1. 动画未结束，直接消失
			//2. 动画已结束，默认速度上拉
			//3. 有其他下拉同时存在，200速度上拉
			//4. 左右反复移动，离开动画就消失，不动时正常拉出
			//5. 鼠标移出一秒内再移入不消失
			//等等
			$(".nav>ul li").hover(function() {
				clearInterval(mu_tmrid);
				mu_tmrid = null;
				var sbuls = $(this).siblings().children("ul");
				sbuls.stop(true, true);
				if ($(this).children("ul").hasClass("sub1")) {
					sbuls.slideUp(200)
					$(this).children("ul").slideDown()
				} else if (!$(this).children("ul").is(":visible")) {
					sbuls.hide()
					$(this).children("ul").show().css({
						width : 0
					}).animate({
						width : "100%"
					})
				}
			}, function() {
				var tt = $(this)
				if (tt.children("ul").is(":animated")) {
					tt.children("ul").hide()
				} else if (tt.children("ul").is(":visible")) {//已经显示出来了
					if (mu_tmrid == null) {
						if ($(this).children("ul").hasClass("sub2"))
							tt = $(this).parent().parent()
						mu_tmrid = setTimeout(function() {
							hideMenu(tt)
						}, 1000)
					}
				} else {

				}
			});

			$(".ring-hover").animate({top : 15}).animate({top : -7}).animate({top : 0})
			$(".slogan").animate({left : 60}).animate({left : 92}).animate({left : 80})
			$(window).on('scroll', function() {
				fixNav();
			});
			fixNav();
		} else {
			var c = parseInt($(window).width() / 80);
			$(".nav>ul>li:lt(" + (c) + ")").show()

			$("a").removeAttr("target");//手机上显示时，就不打开新窗口了

			$(".nav>ul li").click(function() {
				$(".sub1").hide()
				$(this).children("ul").show()
			})

			$(".leftarr").click(function() {
				if ($(".nav>ul>li:last").is(":visible")) {
					layer.msg("已到最后一项！")
					return;
				}
				$(".nav>ul>li:visible:first").animate({
					width : 0
				}, function() {
					$(".nav>ul>li:visible:last").next().show();
					$(this).hide();
				})
			})
			$(".rightarr").click(function() {
				if ($(".nav>ul>li:visible:first").index() == "0") {
					layer.msg("已到第一项！")
					return;
				}
				$(".nav>ul>li:visible:last").hide()
				$(".nav>ul>li:visible:first").prev().css({
					width : 0
				}).show().animate({
					width : 80
				})
			})
		}

	})
</script>