<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c"  uri="http://java.sun.com/jsp/jstl/core" %>
<%
	request.setAttribute("path", request.getContextPath());
%>

<!doctype html>
<html>
<head>
<title>管理后台登录 - ${WZMC}</title>
<%@ include file="inc/meta.jsp"%>
<style>
	*{font-size: 9pt;font-family: "Microsoft YaHei","sans-serif";}
	img{width:112px; height:44px; line-height:46px;cursor: pointer;border:1px solid #ccc !important; }
	
	*{border:0;margin:0;padding:0;}
	body{font-family:'微软雅黑'; margin:0 auto;min-width:980px;}
	ul{display:block;margin:0;padding:0;list-style:none;}
	li{display:block;margin:0;padding:0;list-style: none;}
	img{border:0;}
	dl,dt,dd,span{margin:0;padding:0;display:block;}
	a,a:focus{text-decoration:none;color:#000;outline:none;blr:expression(this.onFocus=this.blur());}
	a:hover{color:#00a4ac;text-decoration:none;}
	table{border-collapse:collapse;border-spacing: 0;}
	cite{font-style:normal;}
	h2{font-weight:normal;}

	/*cloud*/
	#mainBody {width:100%;height:100%;position:absolute;z-index:-1;}
	.cloud {position:absolute;top:0px;left:0px;width:100%;height:100%;background:url(${path}/images/cloud.png) no-repeat;z-index:1;opacity:0.5;}
	#cloud2 {z-index:2;}

	/*login*/
	.logintop{height:47px; position:absolute; top:0; background:url(${path}/images/loginbg1.png) repeat-x;z-index:100; width:100%;}
	.logintop span{color:#fff; line-height:47px; background:url(${path}/images/loginsj.png) no-repeat 21px 18px; text-indent:44px; color:#afc5d2; float:left;}
	.logintop ul{float:right; padding-right:30px;}
	.logintop ul li{float:left; margin-left:20px; line-height:47px;}
	.logintop ul li a{color:#afc5d2;}
	.logintop ul li a:hover{color:#fff;}
	.loginbody{background:url(${path}/images/loginbg3.png) no-repeat center center; width:100%; height:585px; overflow:hidden; position:absolute;top:47px;}
	.systemlogo{background:url(${path}/images/loginlogo.png) no-repeat center;width:100%; height:71px;}
	.loginbox{width:692px; height:336px; background:url(${path}/images/logininfo.png) no-repeat; }
	.loginbox ul{margin-top:88px; margin-left:285px;}
	.loginbox ul li{margin-bottom:25px;}
	.loginbox ul li label{color:#687f92; padding-left:25px;}
	.loginbox ul li label a{color:#687f92;}
	.loginbox ul li label a:hover{color:#3d96c9;}
	.loginbox ul li label input{margin-right:5px;}
	.loginbox,.loginbox1,.loginbox2,.loginbox3,.loginbox4{width:692px; height:373px;}
	.loginuser{width:299px; height:48px; background:url(${path}/images/loginuser.png) no-repeat; border:none; line-height:48px; padding-left:44px; font-size:14px; color:#90a2bc;}
	.loginpwd{width:299px; height:48px; background:url(${path}/images/loginpassword.png) no-repeat; border:none;line-height:48px; padding-left:44px; font-size:14px; color:#90a2bc;}
	.loginbtn{width:111px;height:35px; background:url(${path}/images/buttonbg.png) repeat-x; font-size:14px; font-weight:bold; color:#fff;cursor:pointer; line-height:35px;}
	.loginbm{height:50px; line-height:50px; text-align:center; background:url(${path}/images/loginbg2.png) repeat-x;position:absolute; bottom:0; width:100%; color:#0b3a58;}
	.loginbm a{font-weight:bold;color:#0b3a58;}
	.loginbm a:hover{color:#fff;}
	
	.loginbox1{background:url(${path}/images/logininfo1.png) no-repeat;}
	.loginbox1 ul li{margin-bottom:16px;}
	.loginbox2{background:url(${path}/images/logininfo2.png) no-repeat;}
	.loginbox2 ul li{margin-bottom:16px;}
	.loginbox3{background:url(${path}/images/logininfo3.png) no-repeat;}
	.loginbox3 ul li{margin-bottom:16px;}
	.yzm{width:346px; height:46px; background:url(${path}/images/yzmbg.png) no-repeat; padding-bottom:5px;}
	.yzm span{display:block; float:left;width:227px; height:46px;}
	.yzm span input{background:none; border:none; width:183px; height:46px; line-height:46px;padding-left:44px;font-size:14px; color:#cee4f1;}
	.yzm cite{float:right; width:114px; height:46px; line-height:46px; font-size:18px; color:#fff; text-align:center;}
	
	.loginbody1{background:url(${path}/images/loginbg4.png) no-repeat center center; width:100%; height:585px; overflow:hidden; position:absolute; top:47px;}
	.systemlogo{width:100%; height:71px; text-align:center;}
	.loginbox0{width:810px;margin-top:0px;height:252px;}
	
</style>

<script src="${path}/js/jquery.js"></script>
<script src="${path}/js/cloud.js"></script>
<script src="${path}/layer/layer.js"></script>

<script>
	$(function(){
	    $('.loginbox').css({'position':'absolute','left':($(window).width()-692)/2, 'top':($(window).height()-373)/2+30, 'z-index':'100'});
	    $('.systemlogo').css({'position':'absolute', 'top':($(window).height()-373)/2-60});
		
		$(window).resize(function(){  
	    $('.loginbox').css({'position':'absolute','left':($(window).width()-692)/2,  'top':($(window).height()-373)/2+30, 'z-index':'100'});
	     $('.systemlogo').css({'position':'absolute', 'top':($(window).height()-373)/2-60});
	    })  
	});  
</script> 


	</head>

<body onkeydown="fn_etr();" style="background-color:#1c77ac; background-image:url(${path}/images/light.png); background-repeat:no-repeat; background-position:center top; overflow:hidden;">



    <div class="loginbox loginbox1">
    
    <form action="" method="post" id="dlform">
	    <ul>
	    <li><input name="yhm" type="text" class="loginuser" placeholder="用户名" maxlength="12" value="test"/></li>
	    <li><input name="mm" type="text" class="loginpwd" placeholder="密码" maxlength="16"  value="test"/></li>
	    <li class="yzm">
	    	<span><input value="j1yy" name="yzm" value="" maxlength="4" readonly type="text" placeholder="验证码" onclick="layer.alert('这是【测试超级管理员】的登录页面，不需要输入验证码，点击【免输入登录】按钮即可。如果你想点击更换验证码请到【正式后台用户】的登录页')"/></span>
	    	<!-- <img id="code" alt="验证码" src="${path}/idCode" width="100" height="32" class="passcode"  onclick="this.src+='?'" title="看不清？点击换一个"/> -->
	    	<img id="code"  alt="验证码" src="${path}/images/ht/yzm.jpg" width="100" height="32" class="passcode" title="这个验证码只是给你看的，如果你想点击更换请到【正式后台用户】的登录页" onclick="layer.alert('这是【测试超级管理员】的登录页面，不需要输入验证码，点击【免输入登录】按钮即可。如果你想点击更换验证码请到【正式后台用户】的登录页')"/> 
	    </li>
	    <li style=''><input name="" type="button" class="loginbtn" value="免输入登录" onclick="fn_dl(this)"  /></li>
	    </ul>
    </form>
    
    </div>



    <div id="mainBody">
      <div id="cloud1" class="cloud"></div>
      <div id="cloud2" class="cloud"></div>
    </div>  


<div class="logintop">    
    <span>欢迎登录${WZMC}后台</span>    
    <ul>
    <li><a href="${path}/">回首页</a></li>
    </ul>    
    </div>
    
    <div class="loginbody">
       
    </div>
    
    <div class="systemlogo">
    </div>
    
    <div class="loginbm">${GGY}</div>
	<script>
		var last=5, layidx=null;
		function zidong(){
			var layele = $('#layui-layer' + layidx);
			if ( layele.length<=0){
				return;
			}
		
			layele.find(".layui-layer-content").text(--last+'秒后将为您自动登录');
			if (last<=0){
				$(".loginbtn").trigger('click')
			}
		}
	
		$(function(){
			layer.ready(function(){
				$(".loginbtn").hover(function(){
					layer.tips('可直接点击登录，无需输入', $(".loginbtn"),{tips:[2, '#ffaa31'], id:"id_laytip_htlgn", tipsMore:true});
				})
			})
		
			$("input[name='yhm'], input[name='mm']").blur(function(){
				$("input[name='yhm'], input[name='mm']").val("test")
			})
			
			if (document.referrer.indexOf("yanshi")!=-1 || document.referrer.indexOf("${dlhtlj}")!=-1){
				layer.ready(function(){
					layidx = layer.msg(last+'秒后将为您自动登录',{ time:6000, closeBtn:1})
					setInterval(zidong,1000)
				})
			}
			
			layer.tips('可直接点击登录，无需输入', $(".loginbtn"),{tips:[2, '#ffaa31'], id:"id_laytip_htlgn", tipsMore:true});
		})

		function fn_etr(){
	         var e = window.event || arguments.callee.caller.arguments[0];
	         if (e && e.keyCode == 13 ) {
	             $(".loginbtn").trigger('click')
	         }
		}

		function fn_dl(tag){
			$("input[name='yhm'], input[name='mm']").val("test")
		
			//前台检验
			$.ajax({
				type:"POST",
				url:'${path}/ljdl_ad_test.dhtml',//这个是不做任何校验的，直接登录test账号
				data:$("#dlform").serialize(),
				datatype: "text",
				beforeSend:function(){layer.msg('登录中，请稍后......', {time:-1,icon: 16,shade: 0.3, scrollbar:false});},  
				success:function(data){
					layer.closeAll();
					if (data == "0"){
						location.href = '${path}/g2htgl.dhtml';
					}else{
						layer.alert(data, { icon:2, title:'错误'});
						$(tag).removeAttr("disabled");
						$("#code").trigger('click')
					}
				},
				error: function(){
					layer.alert('登录失败，请重试！', { icon:2, title:'错误'});
					$(tag).removeAttr("disabled");
					$("#code").trigger('click')
				}         
			});
		}
	</script>
    
</body>
</html>
