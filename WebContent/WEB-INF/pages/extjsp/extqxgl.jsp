
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<!-- 所以就是配置权限的功能和页面就通过extqxgl.jsp嵌入到list.jsp里了，说真的，对这个系统不熟悉不太好搞，并不难，但是不熟悉就搞不定 -->
<script>

$(function(){
	$(".content").before("<div class='cls_set_quanxian'></div>")

	sxPzqx();
/* 	$(document).on('click','#id_left_tree a',function(){
		var ot = $.fn.zTree.getZTreeObj("id_left_tree");
		if (ot!=null && $(this).attr("id")!=null){
			var node = ot.getNodeByTId($(this).attr("id").substring(0,$(this).attr("id").length-2))
			if (node!=null){
				sxPzqx(node.id);
			}
		}
	}) */
	$(document).on('___tjpcms_evt_clk_lt','#id_left_tree a',function(node){
		var ot = $.fn.zTree.getZTreeObj("id_left_tree");
		if (ot!=null && $(this).attr("id")!=null){
			var node = ot.getNodeByTId($(this).attr("id").substring(0,$(this).attr("id").length-2))
			if (node!=null){
				sxPzqx(node.id);
			}
		}
	})
	
	//setTimeout(function(){alert($("#id_left_tree #id_left_tree_12_a")[0].outerHTML)}, 100)
})

//刷新配置权限
function sxPzqx(jsid){
	aj("aj_qxgl_getqx.dhtml", {jsid:jsid, chadt:1},{
		beforeSend:function(){},
		succ:function(rtn){
			var html = template('sid_qxgl_setting', rtn);
			$(".cls_set_quanxian").html(html)
			//菜单树
			var setting = {
				data: {simpleData: {enable: true}},//这个是默认不需要转json格式，直接从数据库读list就行，还能转字段名
				check:{enable:true,chkboxType: { "Y": "ps", "N": "ps" }}
			};
       		var treeObj = $.fn.zTree.init($("#tree1"), setting, rtn.cds.all);
       		treeObj.expandAll(true);
      		//treeObj.selectNode(jsid?treeObj.getNodeByParam("id", jsid):treeObj.getNodes()[0]);
			
			$(".cls_set_quanxian .fangfa li").click(function(e){
				$(this).children("input").trigger('click')
				if ($(this).children("input").prop("checked")){
					$(this).addClass('lisel')
				}else{
					$(this).removeClass('lisel')
				}
				count();
				e.stopPropagation()
			})
			$(".cls_set_quanxian .fangfa li input").click(function(e){
				if ($(this).prop("checked")){
					$(this).parent().addClass('lisel')
				}else{
					$(this).parent().removeClass('lisel')
				}
				count();
				e.stopPropagation()
			})
			count();
		},fail:function(){
			layer.alert('权限树获取失败，请联系管理员！', { icon:2, title:'错误'});
		},error:function(){
			layer.msg("权限树获取失败，请刷新重试！");//这边就没搞刷新按钮了，我嫌麻烦，tabs那里有
		}
	})
}



//权限管理里，自行修正布局,lo:layout
function fn_qxgl_aftlo(){
	$(".content").width($(".content").width()-720-1)//1像素是右边框
	$(".cls_set_quanxian").height($(".lefttree").height())
}

function count(){
	var c= 0,total=0;
	$(".cls_set_quanxian .fangfa li").each(function(e){
		total++;
		if ($(this).children("input").prop("checked")){
			c++;
		}
	})
	$(".cls_set_quanxian #id_cnt").text(c+"/"+total)
	
	return c;
}

//全选
function fn_qx(){
	$(".cls_set_quanxian .fangfa li").each(function(e){
		$(this).children("input").prop("checked", true)
		$(this).addClass('lisel')
	})
	count();
}

//反选
function fn_fx(){
	$(".cls_set_quanxian .fangfa li").each(function(e){
		$(this).trigger('click')
	})
	count();
}

//将选中的菜单保存到该角色中
function baocun(nodes, jsid){
	var cdlist=[];//好像也不需要传对象，传一个id就够了，那样的话写法可能也不一样，可能不需要stringify，需要triditional:true
	for(var i=0;i<nodes.length;i++){
		var n = nodes[i];
		var ele = {id:n.id};
		cdlist.push(ele);
	}
	
	var fflist="";
	$(".cls_set_quanxian .fangfa li").each(function(){
		if ($(this).children("input").prop("checked")){
			var title= $(this).attr('title').split("-")
			fflist += title[1]+",";
		}
	})
	$.ajax({
          type:"POST",
          url:"bcquanxian.dhtml",
          dataType:'JSON',
          data:{"cdlist":JSON.stringify(cdlist), jsid:jsid, fflist:fflist},
          datatype: "text",
          beforeSend:function(){layer.msg('正在保存......', {time:-1,icon: 16,shade: 0.3, scrollbar:false});},  
          success:function(ret){
				if (ret=="0"){
					layer.msg("保存成功！")
				}else if (ret=="-1"){
					layer.alert('保存失败，请联系管理员！', { icon:2, title:'错误'});
				}else{
					layer.alert(ret, { icon:2, title:'错误'});
				}
          },
          error: function(){
			layer.alert('保存失败，请刷新重试！', { icon:2, title:'错误'});
          }         
	});
}

//保存权限
function fn_bc(jsid){
	//提示方法的数量
	var c = count();
	if (c<=0){
		layer.confirm('您未选择任何方法，保存后该角色将无法访问后台管理的任何链接，确定吗？', { icon:0, title:'提示'}, function(){
			var treeObj = $.fn.zTree.getZTreeObj("tree1");
			var nodes = treeObj.getChangeCheckedNodes();//改变过的节点，相对于初始
			nodes = treeObj.getCheckedNodes();
			if (nodes.length==0){//未选中任何菜单
				layer.confirm('您未选中任何菜单项，保存后该角色将无菜单权限，确定吗？', { icon:3, title:'询问'}, function(){
					baocun(nodes, jsid);
				});
			}else{
				baocun(nodes, jsid);
			}
		});
	}else{
		var treeObj = $.fn.zTree.getZTreeObj("tree1");
		var nodes = treeObj.getChangeCheckedNodes();//改变过的节点，相对于初始
		nodes = treeObj.getCheckedNodes();
		if (nodes.length==0){//未选中任何菜单
			layer.confirm('您未选中任何菜单项，保存后该角色将无菜单权限，确定吗？', { icon:3, title:'询问'}, function(){
				baocun(nodes, jsid);
			});
		}else{
			baocun(nodes, jsid);
		}
	}
}
</script>

<script id="sid_qxgl_setting" type="text/html">
		<div class="qxinner">
			<div class="tishi">
				角色名：{{jsm}}
				<a href="javascript:" class="fanhui" onclick="fn_bc({{jsid}})">保存</a>
			</div>
			<div class="qxcdffs">
				<div class="qxcnt" style='padding: 5px;'>
					<div class='biaoti' style="padding-top:0;padding-bottom:10px">菜单配置</div>
					<ul id="tree1" class="ztree"></ul>
				</div><div class="qxcnt" style="margin-right:0">
					<div class='biaoti' style='padding-top:5px'>
						<span>方法配置(<label id="id_cnt"></label>)</span>
						<span style='margin-left: 5px;'>
							<input type="button" onclick="fn_qx()" value="全选" class='ext_btn clsiptbtn clstagPIC'/>
							<input type="button" onclick="fn_fx()" value="反选" class='ext_btn clsiptbtn clstagPIC'/>
						</span>
					</div>
					<ul class='fangfa' >
						{{each ffs as t idx}}
							<li title="{{t.uri}} - {{t.ffm}}" {{if t.sel!='' && t.sel!=null}}class='lisel'{{/if}}>
								<input type="checkbox" {{if t.sel!='' && t.sel!=null}}checked='checked'{{/if}}/>&nbsp;{{idx+1}}、{{t.uri}} — {{t.ffm}}
							</li>
						{{/each}}
					</ul>
				</div>
			</div>
		</div>
</script>



