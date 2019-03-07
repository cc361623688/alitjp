<!--
 * 作者:tjp
 * QQ号：57454144（有任何问题可以直接联系我）
 * QQ群：168895774（有教学视频下载）
 * 官网：http://www.tjpcms.com
 * 更新日期：2018-04-17
 * tjpcms - 最懂你的cms
 * 权限管理我重写了，主要是角色换成树型了，这个文件就作废了
 * -->

<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%
	request.setAttribute("path", request.getContextPath());
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="X-UA-Compatible" content="IE=EDGE,chrome=1"/>
	<link rel="stylesheet" href="${path }/css/ht_cmn.css" type="text/css"/>
	<link rel="stylesheet" href="${path }/css/zTreeStyle/zTreeStyle.css" type="text/css"/>
    <script src="${path }/js/jquery.js" type="text/javascript"></script>
    <script type="text/javascript" src="${path }/js/jquery.ztree.core.min.js"></script>
    <script type="text/javascript" src="${path }/js/jquery.ztree.excheck.js"></script>
    <script src="${path}/js/template.js"></script>
    <script src="${path}/js/cmn.js"></script>
	<style>
		.rinner{padding:10px;overflow: hidden;}
		.bread{height: 24px;background:rgb(238,245,253);font-size: 12px;line-height: 23px;
			text-indent: 2em;border-bottom:1px solid rgb(215,228,234);text-shadow: silver 1px 1px 2px !important;}
		.ztree{width:100%;padding: 0;}
		.fanhui{padding: 2px 5px;margin-left: 3px;!important}
		.lecont{overflow: auto !important;margin-top: 5px;max-width: 300px;background: #F5F5F5;-webkit-border-radius: 3px;
		-moz-border-radius: 3px;    border: 1px solid #ddd;display: inline-block;float: left;margin-right: 10px;
		border-radius: 3px;}
		.tishi{font-size: 14px;margin-top: 3px;background: #FFFFE0;color: #f60;padding: 5px;border:1px solid #ccc;font-weight: bold;margin-bottom: 5px;}
		.content{ overflow: auto !important;}
		.biaoti{background: #f5f5f5;text-align: center;padding:5px 0 5px;font-weight: bold;font-size: 15px;position: relative;}
		.fangfa li{font-size: 15px;padding: 5px;border-top: 1px dotted #ccc;text-overflow:ellipsis; white-space:nowrap; overflow:hidden;}
		.lisel{background:rgb(255, 252, 234);}
		#tree1{border-top: 1px dotted #ccc;padding-top: 5px;}
		.biaoti span{font-size: 15px !important; }
	</style>
</head>



<body>
	<div class="bread">当前位置：权限管理 - 配置菜单</div>
	<div class="content">
		<div class="rinner">
			<div class="tishi">
				角色名：${jsm }
				<a href="javascript:" class="fanhui" onclick="fn_bc()">保存</a>
				<a href="javascript:" class="fanhui" onclick="location.href='quanxian.dhtml'">返回</a>
			</div>
			<div>
				<div class="lecont" style='padding: 5px;'>
					<div class='biaoti' style="padding-top:0;padding-bottom:10px">菜单配置</div>
					<ul id="tree1" class="ztree"></ul>
				</div>
				<div class="lecont" style='max-width: 500px;'>
					<div class='biaoti' style='padding-top:5px'>
						<span>方法配置(<label id="id_cnt"></label>)</span>
						<span style='margin-left: 5px;'>
							<input type="button" onclick="fn_qx()" value="全选" class='ext_btn clsiptbtn clstagPIC'/>
							<input type="button" onclick="fn_fx()" value="反选" class='ext_btn clsiptbtn clstagPIC'/>
						</span>
					</div>
					<ul class='fangfa' >
						<c:forEach items="${ffs}" var="t" varStatus="status">
							<li title="${t.uri} - ${t.ffm}" <c:if test="${!empty t.sel}">class='lisel'</c:if>>
								<input type="checkbox" <c:if test="${!empty t.sel}">checked='checked'</c:if>/>&nbsp;${status.index+1}、${t.uri} — ${t.ffm}
							</li>
						</c:forEach>
					</ul>
				</div>
				<div style="clear:both"></div>
			</div>
		</div>
	</div>


	<script src="${path}/layer/layer.js"></script>
	<script src="${path}/laypage/laypage.js"></script>
    <script type="text/javascript">
		//重新调整布局
		function layout(){
   			$(".content").height($(window).height()-25)
		}

		window.onresize=function(){
			layout();layout();
		}
		
		function count(){
			var c= 0;
			$(".fangfa li").each(function(e){
				if ($(this).children("input").prop("checked")){
					c++;
				}
			})
			$("#id_cnt").text(c)
			
			return c;
		}
		
		function fn_qx(){
			$(".fangfa li").each(function(e){
				$(this).children("input").prop("checked", true)
				$(this).addClass('lisel')
			})
			count();
		}
		
		function fn_fx(){
			$(".fangfa li").each(function(e){
				$(this).trigger('click')
			})
			count();
		}
		
		//将选中的菜单保存到该角色中
		function baocun(nodes){
			var cdlist=[];//好像也不需要传对象，传一个id就够了，那样的话写法可能也不一样，可能不需要stringify，需要triditional:true
			for(var i=0;i<nodes.length;i++){
				var n = nodes[i];
				var ele = {id:n.id};
				cdlist.push(ele);
			}
			
			var fflist="";
			$(".fangfa li").each(function(){
				if ($(this).children("input").prop("checked")){
					var title= $(this).attr('title').split("-")
					fflist += title[1]+",";
				}
			})
			$.ajax({
		          type:"POST",
		          url:"bcquanxian.dhtml",
		          dataType:'JSON',
		          data:{"cdlist":JSON.stringify(cdlist), jsid:${jsid}, fflist:fflist},
		          datatype: "text",
		          beforeSend:function(){layer.msg('正在保存......', {time:-1,icon: 16,shade: 0.3, scrollbar:false});},  
		          success:function(ret){
						if (ret=="0"){
							layer.alert("保存成功！",{ icon:6, title:'成功'},function(){
								location.href='quanxian.dhtml';
							})
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
		function fn_bc(){
			//提示方法的数量
			var c = count();
			if (c<=0){
				layer.confirm('您未选择任何方法，保存后该角色将无法访问后台管理的任何链接，确定吗？', { icon:0, title:'提示'}, function(){
					var treeObj = $.fn.zTree.getZTreeObj("tree1");
					var nodes = treeObj.getChangeCheckedNodes();//改变过的节点，相对于初始
					nodes = treeObj.getCheckedNodes();
					if (nodes.length==0){//未选中任何菜单
						layer.confirm('您未选中任何菜单项，保存后该角色将无菜单权限，确定吗？', { icon:3, title:'询问'}, function(){
							baocun(nodes);
						});
					}else{
						baocun(nodes);
					}
				});
			}else{
				var treeObj = $.fn.zTree.getZTreeObj("tree1");
				var nodes = treeObj.getChangeCheckedNodes();//改变过的节点，相对于初始
				nodes = treeObj.getCheckedNodes();
				if (nodes.length==0){//未选中任何菜单
					layer.confirm('您未选中任何菜单项，保存后该角色将无菜单权限，确定吗？', { icon:3, title:'询问'}, function(){
						baocun(nodes);
					});
				}else{
					baocun(nodes);
				}
			}
		
/*			var treeObj = $.fn.zTree.getZTreeObj("tree1");
			var nodes = treeObj.getChangeCheckedNodes();//改变过的节点，相对于初始
 			这个逻辑不对，虽然进入到配置菜单这里没有动过check，但是菜单管理里是可能变动过的，所以这里无条件保存了
 			if (nodes.length==0){
				layer.confirm('未改变该角色拥有的菜单项，确定将返回到列表页', { icon:0, title:'提示'}, function(){
					history.back(-1)
				});
			}else{ 
				nodes = treeObj.getCheckedNodes();
				if (nodes.length==0){//未选中任何菜单
					layer.confirm('您未选中任何菜单项，保存后该角色将无菜单权限，确定吗？', { icon:3, title:'询问'}, function(){
						baocun(nodes);
					});
				}else{
					baocun(nodes);
				}
 			} */
		}

		$(function(){
			$(".fangfa li").click(function(e){
				$(this).children("input").trigger('click')
				if ($(this).children("input").prop("checked")){
					$(this).addClass('lisel')
				}else{
					$(this).removeClass('lisel')
				}
				count();
				e.stopPropagation()
			})
			$(".fangfa li input").click(function(e){
				if ($(this).prop("checked")){
					$(this).parent().addClass('lisel')
				}else{
					$(this).parent().removeClass('lisel')
				}
				count();
				e.stopPropagation()
			})
			count();
		
			//把菜单树显示出来，是带复选框的
			$.ajax({
		          type:"POST",
		          url:"getcdtree.dhtml",
		          data:{selid:null, jsid:${jsid}, chadt:1},//即要把菜单树种三级菜单dt为1的一并查出来组成菜单树
		          datatype: "json",
		          beforeSend:function(){layer.msg('请稍后......', {time:-1,icon: 16,shade: 0.3, scrollbar:false});},  
		          success:function(json){
		          		layer.closeAll();
						var setting = {
							data: {simpleData: {enable: true}},//这个是默认不需要转json格式，直接从数据库读list就行，还能转字段名
							check:{enable:true,chkboxType: { "Y": "ps", "N": "ps" }}
						};
						var data = eval("("+json+")")
		          		var treeObj = $.fn.zTree.init($("#tree1"), setting, data.all);
		          		treeObj.expandAll(true);
		          },
		          error: function(){
					layer.alert('栏目获取失败，请刷新重试！', { icon:2, title:'错误'});
		          }         
			});

			layout();layout();
		});
	</script>
</body>
</html>
       