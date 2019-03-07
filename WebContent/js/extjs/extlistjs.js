
//这里是为了在用户管理里，删除用户的确认之后，再加一层确认，因为用户真的是不能随便删除的，我也没有弄flag
function fn_erci(ids){
	layer.confirm('删除后将无法恢复，确认吗？', {icon: 0, title:'警示'}, function(index){
		___del(ids)
	});
	return false;
}


function fn_yhgl_czmm(id, tag){
	var eqidx = getxhidx();
	var xh = $(tag).parents("tr").children(":eq("+eqidx+")").text();
	fn_tp_aj('确定要将序号为 '+xh+' 的用户密码重置为123456吗？', 'aj_yhgl_czmm.dhtml', {id:id});
}

$(function(){
	var list=document.getElementsByTagName('script');
	for(var i=0;i<list.length;i++){
		if (list[i].src.indexOf("extlistjs.js?")==-1) continue;

		if (list[i].src.indexOf("s=liucheng")!=-1){
			
		}else if (list[i].src.indexOf("其他的")!=-1){
			
		}else if (false){//这里可以继续写其他s=xxxxx
			
		}
		break;
	}
	
})


//角色管理的aftsx
function fn_juese_aftsx(){
	$(".list_table>tbody>tr").each(function(){//每条记录
		var etd = $(this).children("td").last()//操作项td
		var ebj = etd.children(":contains('编辑')")
		var edel = etd.children(":contains('删除')")
		var xhidx = getxhidx();
		var shu1 = parseInt(etd.siblings(":eq("+(xhidx+4)+")").text())
		var shu2 = parseInt(etd.siblings(":eq("+(xhidx+7)+")").text())
		var shu3 = parseInt(etd.siblings(":eq("+(xhidx+8)+")").text())//角色流程数
		if (shu1+shu2+shu3>0){
			edel.addClass("czxgray").attr("title", "点击查看按钮灰化原因")
		}
		var mc = etd.siblings(":eq("+(xhidx+3)+")").text()
		if (mc=="超级管理员" || mc=="前台注册用户" ){
			ebj.addClass("czxgray").attr("title", "点击查看按钮灰化原因").attr("href","javascript:").attr("onclick","layer.msg('"+mc+"为系统内置角色，不能编辑！')")
		}
	})
}













