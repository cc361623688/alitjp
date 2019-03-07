<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<% request.setAttribute("path", request.getContextPath());%>

<html>
<head>

<%@ include file="inc/meta.jsp"%>
<title>${title}</title>


</head>
<body>
	<div class="wrap">
    <div class="wrap_cont">
    
    <div class="wrap_max">
    
 <%@ include file="inc/head.jsp"%>
    
            <!-- -->
            <div class="banner-in">
                <a href="#"><img src="images/in/banner01.jpg" alt=""></a>
            </div>
            <!-- -->
           <%@ include file="inc/bread.jsp"%>
            <!-- -->
            <div class="news main">
                <div class="news-nav">
                    <div class="news-nav-title">
                        <span>注册</span>
                    </div>                   
                </div>
                <div class="news-right">
                
<!-- 注册框 -->
<div id="popzhuce" style='padding:18px 25px 19px 28px;'>
	<form action="">
		<div class="clszciptdiv">
			<span class="clszctip  hide icon-check4"></span>
			<input class="ssoinpt" type="text" value="" name="yx" placeholder="用户名" maxlength="32">
			<span class="icon-sina"></span>
		</div>
		<div class="clszciptdiv">
			<span class="clszctip  hide icon-check4"></span>
			<input class="ssoinpt" type="password" value="" name="mm" placeholder="登录密码 (6-12位)" maxlength="12">
			<span class="icon-mima1"></span>
		</div>
		<div class="clszciptdiv">
			<span class="clszctip hide icon-check4"></span>
			<input class="ssoinpt" type="password" value="" name="qrmm" placeholder="确认密码" maxlength="10">
			<span class="icon-mima1"></span>
		</div>			
	</form>
	<div style="clear:both"></div>
	<input type="button" onclick="fn_ljzc()" value="立即注册" class="udltjpcms"/>
</div>
                
                
                </div>
            </div>
            <!-- -->
             <!-- -->          
        </div>
    </div>
</div>
</body>

<script src="${path}/js/rsa.js"></script>
<script src="${path}/layer/layer.js"></script>
<script src="${path}/js/cmn.js"></script>
<script type="text/javascript" src="${path}/js/template.js"></script>

<script>
	layer.msg('test')
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
	

		//密码的校验
		var bnktip = '输入的值不能含有空格';
		$("#popzhuce input[name='mm']").bind(' blur', function(event) {
			var mm = $(this).val();
			if (/\s+/.test(mm)){
				layer.msg('密码中'+bnktip)
			}else if (_epp(mm) || mm.length<6){
				layer.msg("密码长度范围需为6-12位")
			}else{
				var qrmm = $("#popzhuce input[name='qrmm']").val();
				if (_epp(qrmm)){
					layer.msg('确认密码不能为空')
				}else{
					if (mm!=qrmm){
						layer.msg( "两次输入的密码不一致")
					}else{
						
					}
				}
			}
		})
		
		
		$("#popzhuce input[name='qrmm']").bind(' blur', function(event) {
			var qrmm = $(this).val();
			var mm = $("#popzhuce input[name='mm']").val();
			if (/\s+/.test(qrmm)){
				layer.msg('确认密码中'+bnktip)
			}else if (_epp(qrmm)){
				layer.msg('请输入确认密码')
			}else{
				if (_epp(mm)){
					layer.msg("请输入6-12位的密码")
				}else{
					if (mm!=qrmm){
						layer.msg("两次输入的密码不一致")
					}else{
						
					}
				}
			}
		})
	
	
	var rsakey = RSAUtils.getKeyPair('${rsa_exponent}', '', '${rsa_modulus}');
	
	//点击立即注册 ----------------------------------------------------------------------------------------------保存注册用户信息
	
	function fn_ljzc(){
 
		var rsakey = RSAUtils.getKeyPair('${rsa_exponent}', '', '${rsa_modulus}');
		fn_ntp_json('${path}/aj_zhuce.dhtml',{
			yx:RSAUtils.encryptedString(rsakey, $("#popzhuce input[name='yx']").val()),
			mm:RSAUtils.encryptedString(rsakey, $("#popzhuce input[name='mm']").val()),
			qrmm:RSAUtils.encryptedString(rsakey, $("#popzhuce input[name='qrmm']").val())
		},
		function(){
			layer.alert('注册成功',{},function(){
			location.reload();
			})
		},function(data){
			$.each(data.err, function(idx,obj){
				var ele = $("#popzhuce input[name='"+obj.zd+"']")
				ele.focus();
				layer.msg(obj.cw)
				/* tipError(ele, "-1", obj.cw)
				if (idx==0){ele.focus();}
				$("#popzhuce .passcode").trigger('click') */
			})
			if (data.em) layer.msg(data.em)
		},{clsA:false})
	}

</script>

	
</html>