<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%
	request.setAttribute("path", request.getContextPath());
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
  		<script src="${path}/js/jquery.js"></script>
  </head>
  
  <body onkeydown="fn_etr()">
    <form action="" method="post" id="dlform">
	    <input name="yhm" type="text" maxlength="100" style=""/>
		<input name="mm" type="password"  maxlength="100" style="" />
		<input name="denglu" type="button" value="登录"  onclick="fn_dl()"   style=""/>
    </form>

    <script>
		function fn_etr(){
	         var e = window.event || arguments.callee.caller.arguments[0];
	         if (e && (e.keyCode == 13 || e.keyCode == 32)) {
	             fn_dl();
	         }
		}

    	function fn_dl(yhm,mm){
    		if (!yhm) yhm = $("input[name='yhm']").val()
    		if (!mm) mm = $("input[name='mm']").val()
			$.ajax({
				type:"POST",
				url:'${path}/ljdl_ad_tjp.dhtml',
				data:{yhm:yhm, mm:mm},
				datatype: "text",
				success:function(data){
					if (data == "0"){
						if (localStorage){
							localStorage.setItem('dlhtljtjp_yong', yhm);
							localStorage.setItem('dlhtljtjp_pass', mm);
						}
						location.href = '${path}/g2htgl.dhtml';
					}else{
						alert(data);
					}
				},
				error: function(){
					alert('网络故障');
				}         
			});
    	}
    	
    	$(function(){
			if (localStorage){
				var yong =localStorage.getItem('dlhtljtjp_yong');
	 			var pass= localStorage.getItem('dlhtljtjp_pass');
	 			if (yong && pass){
	 				fn_dl(yong,pass);
	 			}
			}
    	})
    </script>
  </body>
</html>
