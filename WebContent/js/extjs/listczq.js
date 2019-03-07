

function fn_cmn_del(tag,id,tp){
	$(tag).hasClass('czxgray')?layer.msg(tp):___fn_del(tag,id);
}

function fn_cmn_edit(tag,id,tp){
	$(tag).hasClass('czxgray')?layer.msg(tp):(location.href='aev.dhtml?t=1&u='+getu()+'&id='+id);
}


function fn_usr_tipedit(id){
	layer.confirm('请注意编辑保存后将由管理员重新审核', {icon: 3, title:'提示'}, function(index){
		location.href="aev.dhtml?t=1&u="+getu()+"&id="+id
	})
}




//角色管理，前台删除
function fn_jsgl_del(tag,id){
	var par = $(tag).parent().parent()
	var xhidx = getxhidx();
	var shu1 = parseInt(par.children("td:eq("+(xhidx+4)+")").text());
	var shu2 = parseInt(par.children("td:eq("+(xhidx+7)+")").text());
	var shu3 = parseInt(par.children("td:eq("+(xhidx+8)+")").text());
	var mc = par.children("td:eq("+(xhidx+3)+")").text();
	if (mc=="超级管理员" || mc=="前台注册用户"){
		layer.msg(mc+'为内置角色，不能新增、编辑或删除！')
	}else if (shu1+shu2+shu3>0){
		layer.msg('请先删掉该角色对应的子菜单、用户和流程！')
	}else{
		___fn_del(tag,id)
	}
}


//配置角色，目前系统的精细程度只能支撑到角色，其实还有其他一些角度来定义用户，比如最常见的部门或者叫机构
//但是角色树貌似可以替代部门、机构或者工作组这个概念
function fn_pzjuese(id,mc,lclms){
	if(lclms>0){layer.alert('已有'+lclms+'个栏目应用了该流程，取消应用后方可配置角色！');return;}
	
	layer.open({
		type: 2,
		title: '配置流程'+' - '+mc,
		shadeClose: true,
		maxmin: true, 
		area: ['66%', '66%'],
		content: 'pzlc.dhtml?id='+id,
		end:function(){shuaxin(getcurPage());}
	});
}


//流程管理里灰化删除按钮
function fn_lc_aftsx(){
	
	$(".list_table>tbody>tr").each(function(){//每条记录
		var etd = $(this).children("td").last()//操作项td
		var edel = etd.children(":contains('删除')")
		var epz = etd.children(":contains('配置流程')")
		var xhidx = getxhidx();
		var mc = etd.siblings(":eq("+(xhidx+2)+")").text()
		var mc1 = epz.siblings(":eq("+(xhidx+2)+")").text()
		if (mc=="无审核" || mc=="自定义"|| mc=="超管审核" || mc1=="无审核" || mc1=="自定义"|| mc1=="超管审核" ){
			edel.addClass("czxgray").attr("title", "点击查看按钮灰化原因").attr("href","javascript:").attr("onclick","layer.msg('【"+mc+"】为系统内置流程，不能删除！')")
			epz.addClass("czxgray").attr("title", "点击查看按钮灰化原因").attr("href","javascript:").attr("onclick","layer.msg('【"+mc+"】为系统内置流程，不能配置！')")
		}

		var shu1 = parseInt(etd.siblings(":eq("+(xhidx+6)+")").text())
		if (shu1>0 && !edel.hasClass('czxgray')){
			edel.addClass("czxgray").attr("title", "点击查看按钮灰化原因").attr("href","javascript:").attr("onclick","layer.msg('栏目流程数为0才可删除！')")
		}
	})
}


//通用文章里页面刷新成功后执行的操作区字段灰化的代码
//这里只是前台校验，这块的逻辑需要同样在后台校验，3个地方，1、编辑，2、审核，3、删除
//审核可以审核自己的，这没关系，如果要避开自己可以选自定义流程，用代码自行构建逻辑
function fn_cmnsh_aftsx(){
	//先算出shzt和uid的前台idx
	var shztidx=-1,uididx=-1;
	$(".list_table>thead>tr:eq(0)>th").each(function(idx){
		var zdm = $(this).attr("zdm")
		if(shztidx==-1 ||  uididx==-1){
			if (zdm=="shzt"){
				shztidx = idx
			}else if (zdm == "uid"){
				uididx = idx;
			}
		}else{
			return true;
		}
	})
	
	if (shztidx==-1 || uididx==-1){//说明hds中没有配审核状态，也就说明当前栏目没有审核流程或者审核流程为【无审核】
		return;
	}

	//如果是正式超管，审核不通过的才能删除，不是他人未提交的才能编辑和审核
	//如果是非正式超管或者普通用户，只有审核不通过的本人记录才能删除，只有【未提交】或【退回修改】的本人记录才可以编辑，只能审核流转到自己的记录
	var uid = getuid();
	$(".list_table>tbody>tr").each(function(){//每条记录
		var etd = $(this).children("td").last()//操作项td
		var eleshzt = etd.siblings(":eq("+shztidx+")").text()
		var eleuid = etd.siblings(":eq("+uididx+")").text()

		etd.children("a").each(function(){//用contains不精确，只是包含，我要的是相同（etd.children("a:contains('编辑')")）
			var txt = $(this).text().replace(/\s+/g, "");
			if (txt=='编辑'){
				if (getzscg()=='true') {
					if (eleuid.indexOf(uid)!=0 && eleshzt.indexOf('未提交')!=-1){
						$(this).addClass("czxgray").attr("title", "点击查看按钮灰化原因").attr("href","javascript:").attr("onclick","layer.msg('还未提交的他人记录不可编辑！')")//这个是只针对他人的，自己的不受限制
					}
				}else{
					if (eleuid.indexOf(uid)!=0){//说明是我的下属角色或者是待我审核的记录，那都是不能编辑的，就是都不需要被看到按钮
						$(this).remove()
					}else if (eleshzt.indexOf('未提交')!=0 && eleshzt.indexOf('退回修改')!=0){
						$(this).addClass("czxgray").attr("title", "点击查看按钮灰化原因").attr("href","javascript:").attr("onclick","layer.msg('只有【未提交】或【退回修改】的记录才可以编辑！')")
					}
				}
			}else if (txt=='删除'){
				if (getzscg()=='true') {
					if (eleshzt.indexOf('审核不通过')!=0){
						$(this).addClass("czxgray").attr("title", "点击查看按钮灰化原因").attr("href","javascript:").attr("onclick","layer.msg('只有【审核不通过】的记录才可以删除！')")//不管是自己还是他人的记录
					}
				}else{
					if (eleuid.indexOf(uid)!=0){//说明是我的下属角色或者是待我审核的记录，那都是不能删的，就是都不需要被看到按钮
						$(this).remove()
					}else if (eleshzt.indexOf('审核不通过')!=0){//是我的，但不是【审核不通过】的状态，灰化
						$(this).addClass("czxgray").attr("title", "点击查看按钮灰化原因").attr("href","javascript:").attr("onclick","layer.msg('只有【审核不通过】的记录才可以删除！')")
					}
					
				}
			}
		})
	})
}

//通用的审核弹出层
function fn_cmn_sh(tag,id,shzt){
	var xh = getxhidx();
	var data={shzt:shzt,id:id,xh:$(tag).parents("tr").children(":eq("+xh+")").text(),u:getu()}
	layer.open({
	    type: 1,
	    title:'审核（序号'+xh+'）',
	    area:'95%',
	    shadeClose :true,
	    content: template('sid_popsh', data),
	    tjpcms_clsouter:'cls_sid_popsh_out'
	});
}

//审核按钮的功能
function fn_op_wzsh(tag,id,xh,u){
	var op = $(tag).val();
	var tacont = $(tag).parent().prev()
	var tacval = tacont.val()
	if ("审核意见（100字以内）"==tacval) tacval="";
	if ((op=='退回修改' || op=='审核不通过') && _epp(tacval)){
		layer.msg('请填写【'+op+'】的原因！');
		tacont.focus();
		return;
	}
	if (!_epp(tacval) && tacval.length>100){
		layer.msg('审核意见最多100字！');
		tacont.focus();
		return;
	}
	layer.confirm('确定要将序号为 <strong>'+xh+' </strong> 的记录 <strong>'+op+' </strong> 吗？', {icon: 3, title:'提示'}, function(index){
		$.ajax({
			type:"POST",
			url:'aj_cmn_sh.dhtml',
			data:{id:id,u:u,shzt:op,shyj:tacval},
			beforeSend:function(){layer.msg('操作中，请稍后......', {time:-1,icon: 16,shade: 0.3, scrollbar:false});},  
			datatype: "text",
			success:function(ret){
				layer.closeAll();
				if ("0" ==ret){
					shuaxin(getcurPage())
				}else if ("-1"==ret){
					layer.alert('操作失败，请联系管理员！', { icon:2, title:'错误'});
				}else{
					layer.alert(ret, { icon:2, title:'错误'});
				}
			},
			error: function(){
				layer.alert('操作失败，请刷新后重试！', { icon:2, title:'错误'});
			}
		});			
	});
}

