<%@ page language="java"  import="java.util.*"  pageEncoding="UTF-8"%>
<style>
	.cmnbtm{height: 36px;background: #3071a9;width: 1000px;margin: 0 auto;line-height: 36px;text-align: center;color:#E7E7E7;}
	.cmnbtm a{font-size: 12px;color:#E7E7E7 !important;text-decoration: none !important;

		-webkit-transition: all 0.5s; -moz-transition: all 0.5s; -o-transition: all 0.5s;}
	.cmnbtm a:hover{color:white !important}
	#SOHUCS, #cyReward{max-width: 1000px;}
	#cyReward{text-align: center;margin-bottom: 22px;}

	@media (max-width: 1000px) {
		.cmnbtm{width:100%;}
	}
</style>

<c:if test="${needcy==1}">
	<!--PC和WAP自适应版-->
	<div id="SOHUCS" sid="${cyid}" ></div> 
	
	<script type="text/javascript"> 
	(function(){ 
	var appid = 'cysHEHjOS'; 
	var conf = 'prod_d88441f85c07ec1e5cecd0cf45872664'; 
	var width = window.innerWidth || document.documentElement.clientWidth; 
	if (width < 960) { 
	window.document.write('<script id="changyan_mobile_js" charset="utf-8" type="text/javascript" src="http://changyan.sohu.com/upload/mobile/wap-js/changyan_mobile.js?client_id=' + appid + '&conf=' + conf + '"><\/script>'); } else { var loadJs=function(d,a){var c=document.getElementsByTagName("head")[0]||document.head||document.documentElement;var b=document.createElement("script");b.setAttribute("type","text/javascript");b.setAttribute("charset","UTF-8");b.setAttribute("src",d);if(typeof a==="function"){if(window.attachEvent){b.onreadystatechange=function(){var e=b.readyState;if(e==="loaded"||e==="complete"){b.onreadystatechange=null;a()}}}else{b.onload=a}}c.appendChild(b)};loadJs("http://changyan.sohu.com/upload/changyan.js",function(){window.changyan.api.config({appid:appid,conf:conf})}); } })(); </script>
</c:if>

<div class="cmnbtm" style="font-family:'Microsoft YaHei','sans-serif'"><!-- 没这个font-family，手机端©显示不出来，显示.，神奇不神奇 -->
	&copy;<%= 2016==Calendar.getInstance().get(Calendar.YEAR)?"2016":"2016-"+Calendar.getInstance().get(Calendar.YEAR)%>
		<a  target="_blank"  style="font-size:14px" href="http://${YUMING}">${YUMING}</a> &nbsp;
	<a class="" target="_blank" style="text-decoration:underline !important;" href="http://www.miitbeian.gov.cn/"> ${bah}</a>
<!-- <script>
var _hmt = _hmt || [];
(function() {
  var hm = document.createElement("script");
  hm.src = "https://hm.baidu.com/hm.js?0893dd8323fb604eea546d73cf49bf6b";
  var s = document.getElementsByTagName("script")[0]; 
  s.parentNode.insertBefore(hm, s);
})();
</script>

<script>
	(function() {
		var bp = document.createElement('script');
		var curProtocol = window.location.protocol.split(':')[0];
		if (curProtocol === 'https') {
			bp.src = 'https://zz.bdstatic.com/linksubmit/push.js';
		} else {
			bp.src = 'http://push.zhanzhang.baidu.com/push.js';
		}
		var s = document.getElementsByTagName("script")[0];
		s.parentNode.insertBefore(bp, s);
	})();
</script> -->
</div>


<style>
	.ssoinpt{
		width: 100%;
		height: 36px;
	    border: 0;
	    color: #000;
	    font-size: 14px;
	    line-height: 30px;
	    margin: 8px 0;
		border: 1px solid #bac7d2;
	    background: #f7fcfe;
	    border-radius: 2px;
	    box-shadow: 2px 2px 2px #e7f1f7 inset;
	    text-indent: 35px;
	}
	.csyzm{width: 65%;display: inline-block;}
	.passcode{width: 30%;display: inline-block;height: 36px;line-height: 30px;margin: 8px 0;float: right;border: 1px solid #bac7d2;-webkit-border-radius: 2px;
	-moz-border-radius: 2px;cursor: pointer;
	border-radius: 2px;}
	#popzhuce .ssoinpt,#popzhuce .passcode{height: 29px;  margin: 6px 0;}
	.udltjpcms{width: 100%;background: rgb(56,163,253);padding: 10px;font-size: 16px;color: #fff;-webkit-border-radius: 3px;
	-moz-border-radius: 3px;margin-top: 8px;
	border-radius: 3px;cursor: pointer;}
	#popzhuce .udltjpcms{padding: 7px;}
	.udltjpcms:hover{background-color: #09f;}
	.split-hr{
	    width: 90%;
	    margin: 28px auto 0;
	    text-align: center;
	    border-top: 1px solid #e5e5e5;
	    position: relative;
	}
	.split-txt{
	    color: #bababa;
	    font-size: 14px;
	    padding: 0 6px;
	    background: #FFF;
	    margin-top: -10px;
	    margin-left: -42px;
	    top: 60%;
	    left: 50%;
	    position: absolute;
	}
	.login-group{
	    margin: 16px auto 0;
	    text-align: center;
	}
	.login-item{
		display: inline-block;
	    cursor: pointer;
	    margin: 0 12px;
	    width: 40px;
	}
	.login-item .login-logo {
	    width: 40px;
	    height: 40px;
	    display: block;
	    border-radius: 40px;
	    background-image: url(${path}/images/sso/sso_wbqq.png);
	    background-repeat: no-repeat;
	}
	.login-logo-qq{background-position: 0 -80px;}
	.login-logo-weibo{background-position: -40px -40px;}
	.login-logo-wechat{background-image: url(${path}/images/sso/sso_bd.png) !important;}
	.cls_pop_ssotitle{border-radius: 10px 10px 0 0;font-weight: bold;font-size: 15px;}
	.cls_pop_ssoouter *{font-family: "Microsoft YaHei";}
	.cls_pop_ssoouter{border-radius: 10px;max-width:95% !important;min-width:320px;}
	
	.clszciptdiv,.clsdliptdiv{position: relative;}
	.icon-sina:before { content: "\e66b"; font-size: 19px;}
	.icon-mima1:before { content: "\e7c6"; font-size: 18px;}
	#popzhuce .icon-cguanliyuan:before { font-size: 17px;margin: 0 !important;}
	.icon-check4:before { content: "\e7bd";font-size: 17px; }
	.clszciptdiv [class*='icon-']:before { position: absolute;left: 8px;top: 12px;color:gray}
	.clsdliptdiv [class*='icon-']:before { position: absolute;left: 8px;top: 17px;color:gray}

	.cls_pop_ssoouter .layui-layer-close {-webkit-animation: 3s infinite linear;-moz-animation: 3s infinite linear;-ms-animation: 3s infinite linear;animation: 3s infinite linear;
		background-position: 2px -39px;margin-left: 0;padding: 0px;}
	.cls_pop_ssoouter .layui-layer-close:hover {-webkit-animation-name: a-rotate;-moz-animation-name: a-rotate;-ms-animation-name: a-rotate;animation-name: a-rotate;}
	@-webkit-keyframes a-rotate {0% {-webkit-transform: rotate(0deg);}100% {-webkit-transform: rotate(359deg);}}
	@-moz-keyframes a-rotate {0% {-moz-transform: rotate(0deg);}100% {-moz-transform: rotate(359deg);}}
	@-ms-keyframes a-rotate {0% {-ms-transform: rotate(0deg);}100% {-ms-transform: rotate(359deg);}}
	@keyframes a-rotate {0% {transform: rotate(0deg);}100% {transform: rotate(359deg);}}
	
	.clszctip{position: absolute;left: -28px;top: 2px;}
	.clszctip:before{color:#2c7 !important;font-weight: bold;}
	.hide{display: none;}
	.icon-cha1:before { content: "\e673"; font-size: 14px !important;color:red !important;}
	.icon-check4{top: 0 !important;}
	.icon-jianpan:before {    font-size: 19px; }
	.cls_pop_zctip .layui-layer-close{right: -11px !important;top: -9px !important;}
	.ssoyzmbdr{border-color:red !important}
}
</style>


<!-- 登录框 -->
<div id="popsso" style='display:none;padding:22px;'>
	<div class="clsdliptdiv">
		<input class="ssoinpt" type="text" value="" placeholder="邮箱" name="yx">
		<span class="icon-sina"></span>
	</div>
	<div class="clsdliptdiv">
		<input class="ssoinpt" type="password" value="" placeholder="登录密码" name="mm">
		<span class="icon-mima1"></span>
	</div>
	<div class="clsdliptdiv">
		<input name="yzm" maxlength="4" class="ssoinpt csyzm" type="text" placeholder="验证码">
		<img alt="验证码" src="${path}/idCode" width="100" height="32" class="passcode" onclick="this.src+=''" title="看不清？点击换一个" />
		<span class="icon-jianpan"></span>
	</div>
	<div style="clear:both"></div>
	<a href="javascript:" style="float:right;font-size:12px;" onclick="layer.msg('先联系群主')">忘记账号密码？</a>
	<a href="javascript:" style="font-size:12px;" onclick="fn_zhuce(0)">没有账号？点击注册</a>
	<input type="button" onclick="fn_ljdl()" value="立即登录" class="udltjpcms"/>
	<div class="split-hr">
		<span class="split-txt">第三方登录</span>
	</div>
	<ul class="login-group">
		<li class="login-item"><span class="login-logo login-logo-qq" title="QQ登录"></span></li>
		<li class="login-item"><span class="login-logo login-logo-weibo" title="微博登录"></span></li>
		<li class="login-item"><span class="login-logo login-logo-wechat" title="百度登录"></span></li>
	</ul>
</div>

<!-- 注册框 -->
<div id="popzhuce" style='display:none;padding:18px 25px 19px 28px;'>
	<form action="">
		<div class="clszciptdiv">
			<span class="clszctip  hide icon-check4"></span>
			<input class="ssoinpt" type="text" value="" name="yx" placeholder="邮箱" maxlength="32">
			<span class="icon-sina"></span>
		</div>
		<div class="clszciptdiv">
			<span class="clszctip  hide icon-check4"></span>
			<input class="ssoinpt" type="password" value="" name="mm" placeholder="登录密码 (6-12位)" maxlength="12">
			<span class="icon-mima1"></span>
		</div>
		<div class="clszciptdiv">
			<span class="clszctip hide icon-check4"></span>
			<input class="ssoinpt" type="password" value="" name="qrmm" placeholder="确认密码" maxlength="12">
			<span class="icon-mima1"></span>
		</div>
		<div class="clszciptdiv">
			<span class="clszctip hide icon-check4"></span>
			<input class="ssoinpt" type="text" value="" name="nc" placeholder="昵称 (2-15位)" maxlength="15">
			<span class="icon-cguanliyuan"></span>
		</div>
		<div class="clszciptdiv">
			<span class="clszctip hide icon-check4"></span>
			<input name="yzm" maxlength="4" class="ssoinpt csyzm" type="text" placeholder="验证码">
			<img alt="验证码" src="${path}/idCode" width="100" height="32" class="passcode" onclick="this.src+=''" title="看不清？点击换一个" />
			<span class="icon-jianpan"></span>
		</div>
	</form>
	<div style="clear:both"></div>
	<a href="javascript:" style="float:right;font-size:12px;" onclick="layer.msg('先联系群主')">忘记账号密码？</a>
	<a href="javascript:" style="font-size:12px;" onclick="fn_topdl(0)">已有账号？点击登录</a>
	<input type="button" onclick="fn_ljzc()" value="立即注册" class="udltjpcms"/>
	<div class="split-hr">
		<span class="split-txt">第三方登录</span>
	</div>
	<ul class="login-group">
		<li class="login-item"><span class="login-logo login-logo-qq" title="QQ登录"></span></li>
		<li class="login-item"><span class="login-logo login-logo-weibo" title="微博登录"></span></li>
		<li class="login-item"><span class="login-logo login-logo-wechat" title="百度登录"></span></li>
	</ul>
</div>

<script src="${path}/js/rsa.js"></script>
<script src="${path}/layer/layer.js"></script>
<script>
	//顶部按钮：登录
	var vTitle ='<img  id="poptitle" src="${path}/images/tjpcms/logo4.png" style="vertical-align:middle;width:31px;" /> 登录tjpcms';
	var vCont = $('#popsso');
	function fn_topdl(clr){
		layer.closeAll();
		layer.open({
		    type: 1,
		    title:vTitle,
		    area:'auto',
		    content:vCont,
		    scrollbar:false,
		    id:'id_pop_sso',
		    tjpcms_clstitle:'cls_pop_ssotitle',
		    tjpcms_clsouter:'cls_pop_ssoouter',
		    success:function(layero, index){
		    	//为了初始化弹出框
		    		if (!(clr==0)){//默认是要初始化清空的，弹出之后点击切换可以不清空
			    	initPhd();
			    	if ('placeholder' in document.createElement('input')) {
			    		$("#popsso input:not(.udltjpcms)").val('');
			    	}
		    	}
		    	$("#popsso input[name='yzm']").removeClass('ssoyzmbdr')

		    	var eyzm = $("#popsso .passcode");
		    	eyzm.attr("src", eyzm.attr("src"))
		    	$("#popsso input:eq(0)").focus();
		    }, cancel:function(index){
				return qurGuanbi("popsso")
		    }
		});
	}

	//询问是否关闭注册（登录）框
	function qurGuanbi(popid){
    	var xunwen = false;
    	$("#"+popid+" input:not(.udltjpcms)").each(function(){
    		if (!_epp($(this).val())){
				xunwen = true;
    			return false;//退出循环
    		}
    	})
    	if (xunwen){
   			layer.confirm('您已填写了内容，确定要关闭吗？', {icon: 3, title:'提示'}, function(index){
   				layer.closeAll();
   				return true;
   			})
   			return false;
    	}else{
	    	layer.closeAll();
	    	return true;
    	}
	}
	
	//注册
	function fn_zhuce(clr){
		layer.closeAll();
		layer.open({
		    type: 1,
		    title:'<img  id="poptitle" src="${path}/images/tjpcms/logo4.png" style="vertical-align:middle;width:31px;" /> 注册tjpcms',
		    area:'auto',
		    content:$("#popzhuce"),
		    id:'id_pop_zhuce',
		    scrollbar:false,
		    tjpcms_clstitle:'cls_pop_ssotitle',
		    tjpcms_clsouter:'cls_pop_ssoouter',
		    success:function(layero, index){
		    	//为了初始化弹出框，关闭后再打开要全部清空到初始状态
		    		if (!(clr==0)){
			    	initPhd();
			    	if ('placeholder' in document.createElement('input')) {
			    		$("#popzhuce input:not(.udltjpcms)").val('');
			    	}
		    	}

		    	var eyzm = $("#popzhuce .passcode");
		    	eyzm.attr("src", eyzm.attr("src"))
		    	$("#popzhuce .udltjpcms").focus();
		    	$("#popzhuce .clszctip").addClass('hide')
		    }, cancel:function(index){
				return qurGuanbi("popzhuce")
		    }
		});
	}

	var bnktip = '输入的值不能含有空格';
	$(function(){
		$(".login-item").click(function(){
			var idx = $(this).index();
			if (idx==0){//qq登录
				location.href='${path}/dsf_qqlog_log.dhtml';
			}else if (idx==1){//微博登录
				location.href='${path}/dsf_sinalog_log.dhtml';
			}else{//百度登录
				location.href='${path}/dsf_bdlog_log.dhtml';
			}
		}).hover(function(){
			$(this).toggleClass("touming88")
		},function(){
			$(this).toggleClass("touming88")
		})

		//验证码的校验
		$('.csyzm').bind('input propertychange blur', function(ev) {
			var je= $(this)
			if (/\s+/.test(je.val())){
				tipError(je, "-1", bnktip)
			}else if (ev.type=='blur' || (je.val().length >= 4 && '验证码'!=je.val())) {
				$.ajax({
					type:"POST",
					url:'${path}/zcdl_chk_yzm.dhtml',//去后台校验验证码
					data:{yzm:je.val()},
					datatype: "text",
					success:function(ret){
						tipError(je, ret, "验证码错误（点击图案可更换）")
					}
				});
			}
		})

		$(".clszctip").mouseover(function(){
			tipError($(this).next())
		})

 		//处理弹出是否回到首页的框后按回车的问题
		document.onkeydown = function(e){ 
		    var ev = document.all ? window.event : e;
		    if(ev.keyCode==13) {
		    	if ($("#popzhuce").is(":visible"))  $("#popzhuce").find(".udltjpcms").trigger('click')
		    	if ($("#popsso").is(":visible"))  $("#popsso").find(".udltjpcms").trigger('click')
		     }
		}

		//前台校验昵称
		$("#popzhuce input[name='nc']").bind(' blur', function(event) {
			if (/\s+/.test($(this).val())){
				tipError($(this), "-1", bnktip)
			}else{
				tipError($(this), (_epp($(this).val()) || $(this).val().length<2)?"-1":"0", "昵称长度范围需为2-15位")
			}
		})

		//密码的校验
		$("#popzhuce input[name='mm']").bind(' blur', function(event) {
			var mm = $(this).val();
			if (/\s+/.test(mm)){
				tipError($(this), "-1", bnktip)
			}else if (_epp(mm) || mm.length<6){
				tipError($(this), "-1", "密码长度范围需为6-12位")
			}else{
				var qrmm = $("#popzhuce input[name='qrmm']").val();
				if (_epp(qrmm)){
					tipError($(this), "0")
				}else{
					if (mm!=qrmm){
						tipError($(this), "-1", "两次输入的密码不一致")
					}else{
						tipError($(this), "0")
						tipError($("#popzhuce input[name='qrmm']"), "0")
					}
				}
			}
		})
		$("#popzhuce input[name='qrmm']").bind(' blur', function(event) {
			var qrmm = $(this).val();
			var mm = $("#popzhuce input[name='mm']").val();
			if (/\s+/.test(qrmm)){
				tipError($(this), "-1", bnktip)
			}else if (_epp(qrmm)){
				if (_epp(mm)){
					tipError($(this), "-1", "请输入确认密码")
				}else{
					tipError($(this), "-1", "请输入确认密码")
				}
			}else{
				if (_epp(mm)){
					tipError($("#popzhuce input[name='mm']"), "-1", "请输入6-12位的密码")
				}else{
					if (mm!=qrmm){
						tipError($(this), "-1", "两次输入的密码不一致")
					}else{
						tipError($(this), "0")
						tipError($("#popzhuce input[name='mm']"), "0")
					}
				}
			}
		})

		//邮箱的校验
		$("#popzhuce input[name='yx']").bind(' blur', function(event) {
			var etag = $(this);
			var pattern =/^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+(.[a-zA-Z0-9_-])+/;
			if (/\s+/.test(etag.val())){
				tipError(etag, "-1", bnktip)
			}else if(!pattern.exec(etag.val())){
				tipError(etag, "-1", "请输入有效的邮箱")
			}else{
				$.ajax({
					type:"POST",
					url:'${path}/zcdl_chk_yx.dhtml',
					data:{yx:etag.val()},
					datatype: "text",
					success:function(ret){
						tipError(etag, ret, "邮箱已存在，请重新输入")
					}
				});
			}
		})

 		layer.ready(function(){//http://fly.layui.com/jie/1724.html
			if ('${ses_qt_tip}'!=''){
				layer.msg('${ses_qt_tip}')
			}
		}) 
	})

	var g_zcTid = {};
	function tipError(eipt, ret, msg){
		if (!ret){
			ret =_epp(eipt.prev().attr("errmsg"))?"0":"-1";
		}
	
		var nm = eipt.attr("name")
		if (ret=="0"){//正确
			eipt.removeClass('ssoyzmbdr')
			if (eipt.prev().length>0) {eipt.prev().removeClass('icon-cha1 hide').addClass('icon-check4').removeAttr("errmsg");}
			layer.close(g_zcTid[nm])
		}else{//错误
			if (!msg){
				msg = eipt.prev().attr("errmsg");
			}else if (eipt.prev().length>0){
				eipt.prev().removeClass('icon-check4 hide').addClass('icon-cha1').attr("errmsg",msg);
			}
			g_zcTid[nm] = layer.tips(msg, eipt,{
				tjpcms_clsouter:'cls_pop_zctip',time:5000,closeBtn:1,id:"lid_zc_"+nm,tips:[1, '#3595CC'], tjpcms_clscont:'touming88', "z-index":9999,tipsMore:true});//存在时不再弹出
			//如果是登录框的验证码，把input的边框弄成红色的
			if (eipt.attr("name")=='yzm' && eipt.closest('#popsso').length>0){
				eipt.addClass('ssoyzmbdr')
			}
		}
	}

	//立即登录
	function fn_ljdl(){
		//查空格
 		var hasKg = false, kjfst;
    	$("#popsso input:not(.udltjpcms)").each(function(){
    		if (/\s+/.test($(this).val())){
				hasKg = true;
    			layer.msg($(this).attr('placeholder')+'中'+bnktip)
    			if (!kjfst) kjfst = $(this)
    		}
    	})
    	if (hasKg){ kjfst.focus();return};

		//检查验证码
 		var eyz = $("#popsso input[name='yzm']")
		if(_epp(eyz.val()) || eyz.val().length!=4 || eyz.hasClass('ssoyzmbdr')){
			tipError(eyz, "-1", "验证码错误（点击图案可更换）")
			return;
		}  

		var rsakey = RSAUtils.getKeyPair('${rsa_exponent}', '', '${rsa_modulus}');
		fn_ntp_json('${path}/aj_sso_dl.dhtml',{
			yx:RSAUtils.encryptedString(rsakey, $("#popsso input[name='yx']").val()),
			mm:RSAUtils.encryptedString(rsakey, $("#popsso input[name='mm']").val()),
			yzm:eyz.val()
		},
		function(){
			location.reload();
		},function(data){
			$.each(data.err, function(idx,obj){
				tipError($("#popsso input[name='"+obj.zd+"']"), "-1", obj.cw)
				$("#popsso .passcode").trigger('click')
			})
		},{clsA:false})
	}

	//点击立即注册
	function fn_ljzc(){
  		$("#popzhuce input:not(.udltjpcms)").trigger('blur')
		if ($("#popzhuce .icon-cha1").length>0) return;

		//这边有点坑，验证码是异步校验的，这儿还得单独再确认一下
		var elyzm= $("#popzhuce input[name='yzm']")
		if (_epp(elyzm.val()) || elyzm.val().length!=4 || elyzm.prev().hasClass('icon-cha1')){
			$("#popzhuce input[name='yzm'").trigger('blur')
			return;
		} 
 
		var rsakey = RSAUtils.getKeyPair('${rsa_exponent}', '', '${rsa_modulus}');
		fn_ntp_json('${path}/aj_zhuce.dhtml',{
			yx:RSAUtils.encryptedString(rsakey, $("#popzhuce input[name='yx']").val()),
			mm:RSAUtils.encryptedString(rsakey, $("#popzhuce input[name='mm']").val()),
			qrmm:RSAUtils.encryptedString(rsakey, $("#popzhuce input[name='qrmm']").val()),
			nc:$("#popzhuce input[name='nc']").val(),//昵称就不加密了吧，单次最多只能加密14个汉字
			yzm:$("#popzhuce input[name='yzm']").val()
		},
		function(){
			location.reload();
		},function(data){
			$.each(data.err, function(idx,obj){
				var ele = $("#popzhuce input[name='"+obj.zd+"']")
				tipError(ele, "-1", obj.cw)
				if (idx==0){ele.focus();}
				$("#popzhuce .passcode").trigger('click')
			})
			if (data.em) layer.msg(data.em)
		},{clsA:false})
	}
</script>










