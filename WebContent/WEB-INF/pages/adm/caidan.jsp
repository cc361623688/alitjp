<!--
 * 作者:tjp
 * QQ号：57454144（有任何问题可以直接联系我）
 * QQ群：168895774（有教学视频下载）
 * 官网：http://www.tjpcms.com
 * 更新日期：2018-04-17
 * tjpcms - 最懂你的cms
 * 该文件的作用是什么呢，就是说你可以通过这个页面来设置后台的目录结构和角色权限及用户角色
 * 1、这个系统需要有哪些角色，默认有两个：超级管理员和普通投稿用户，你可以根据系统的实际业务情况添加其他角色，除了超级管理员外其他角色都可以增删改查，超级管理员是拥有系统全部最高权限的，当然比超级管理员更高的就是开发人员了
 * 2、这个系统需要有哪些菜单，这个也是我自认比较好用的功能，也是这次更新版本的重要功能，即你可以设置这个系统的后台是由哪些菜单支撑起来的，分为三级结构，
 *		第一级是系统后台左上角广告语右边的横向导航菜单，这个是最高一层的菜单
 *		第二级是广告语正下方的纵向折叠菜单，就是右侧有一个减号-（加号+）的那个横条
 *     第三级是第二级包含的树形菜单，理论上菜单层次是可以无限的，可以是动态的，也即不是手动添加的，而是从数据库读取的
 *		三级菜单再往下，我就认为是三级菜单的一部分了，因此是否动态只设计到3级，并不是级数再往下太难搞了，这只纯粹是设计，我就是设计到3级菜单支持动态，3级菜单的子菜单不可设计为动态的，因为我觉得到3级菜单树已经足够满足绝大多数情况了
 *		所以，这三级菜单弄出来，任何后台的菜单结构都可以支持了。你说你不要一级的也可以(自行隐藏？)，那就只有2级和3级的，如果你2级的也不要，算了，我觉得这个有点麻烦，有2级挺好看的。当然其实都能改。另外，其他系统里会有站点这个概念，但是
 *		我真的没搞清楚站点这个概念的实际业务需求是啥，就是说站点有啥用呢，反正我是从来没用到啊，也有系统提到说站点用的很少我就不包含这个功能了，以免给大家造成负担和系统臃肿，好吧，我也就这么理解吧。如果你对站点的业务需求比较
 *		清楚，欢迎联系我，和我分享分享，如果确实不错，我会考虑以后有时间合入这个功能。
 * 3、有了角色和菜单了，那当然最后还得有角色权限设置，也就是某个角色可以看到哪些菜单，tjpcms里权限只到菜单这一级，至于更深入的crud权限，我认为这个要结合具体的业务来自行实现，并不是我偷懒，我根据我所了解的实际业务情况来看
 *		绝大多数情况下菜单权限已经完全够用了，如果深入到crud或者其他业务逻辑判断，真的不适宜由系统来实现，越俎代庖了，系统还是得精简一点，只做最核心的功能。	
 * 4、我今天想起来其实还有一个啊，角色菜单和角色权限都有了，还得有用户角色，就是说某个用户属于哪个角色，理论上应该所有的用户都有角色，这样这一套基本的后台权限就这样设计好了，不复杂，但是我觉得足以应对90%以上的情况了
 * 2018-09-07：我今天突然感觉，当初动态只设计到3级上，是不是有点缺陷？难道4级5级菜单就不能是动态了吗，好像还是有可能的呀，我去。其实4级5级都还有办法，可以再挪到1级2级菜单上，应该也能接受，但是再往下是真不行了，这个设计目前感觉
 * 有点缺陷，我没必要只设计到3级上啊，应该是至少3级。不过只到3级也有个好处，就是菜单不会搞得过于复杂，还能看得出来替换的结果。
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
    <script src="${path}/js/template.js"></script>
    <script src="${path}/js/cmn.js"></script>
	<style>
		.l-layout-header-inner{text-align: center;}
		.left{
			width: 280px;text-align: center;border-right:1px dotted #ccc;position: absolute;top:25px;
		}
		.layui-layer-content{height: auto !important;}
		.right{position: absolute;right:1px;top:25px;padding: 0px;overflow: auto !important;}
		.rinner{padding:10px;overflow: hidden;}
		.bread{height: 24px;background:rgb(238,245,253);font-size: 12px;line-height: 23px;
			text-indent: 2em;border-bottom:1px solid rgb(215,228,234);text-shadow: silver 1px 1px 2px !important;}
		.right .rinner{
			padding:10px;
		}
		.left .title{
			 text-align: center;
			 color: #183152;
			 height: 25px;
			 line-height: 25px;
			 background: url('${path}/img/lanmushu.png') repeat-x;
			 overflow: hidden;
			 font-size: 12px;position: relative;
		}
		.list_table td input[type='text']{width: 100% !important;}
		.list_table td:first {text-align: center;}
		.ztree{width:100%;padding: 0;}
		.lecont{overflow: auto !important;width: 268px;margin-top: 5px;}
		.tishi{font-size: 12px;margin-top: 5px;background: #FFFFE0;color: #f60;padding: 5px;border:1px solid #ccc;-webkit-border-radius: 2px;
		-moz-border-radius: 2px;
		border-radius: 2px;}
		.title span{position: absolute;right:0px;margin-right: 10px;cursor:pointer;padding:0 5px;}
		.title span:hover{background: #ccc;}
	</style>
</head>
<body>
	<div class="bread">当前位置：菜单管理</div>
	<div class="content">
		<div class="left">
			<div class="title" onselectstart="return false">菜单树
				<span class="" title="我觉得在这做个按钮，可以把一级菜单折叠打开会很方便，另外更漂亮的高亮显示出动态的三级菜单也不错，还有树区域的宽度可记忆调整，写cookie">没做</span>
			</div>
			<div class="lecont">
					<ul id="tree1" class="ztree"></ul>
			</div>
		</div>
		<div class="right">
			<div class="rinner">
				<div id="rgcon"></div>
				<div id="pagin">
					<span class="fymsg"></span>
					<div id="fenye"></div>
				</div>
			</div>
		</div>
	</div>


<script id="sid_addedit" type="text/html">
	<form id="id_fm_caidan">
		{{if id!=null}}<input type="hidden" name="id" value="{{id}}"/>{{/if}}
		<input type="hidden" name="pId" value="{{pId}}"/>
		<table class="list_table" border="0" cellpadding="0" cellspacing="0">
			<thead>
				<tr>
					<th class="first">{{title}}</th><th style="border-left-width:0"></th>
				</tr>
			</thead>
			<tbody>
				<tr>
				 	<td style="width:20%"><b>父菜单：</b></td>
				 	<td>{{root}}</td>
				 </tr>
				<tr>
				 	<td style="width:20%"><b>菜单名称：</b></td>
				 	<td><input type="text" name="name" class="input-text lh25" size="10" value='{{name}}' maxlength="20"/></td>
				 </tr>
				<tr>
				 	<td><b>链接：</b></td>
				 	<td>
						<input type="text" name="lj" class="input-text lh25" value="{{exobj.lj}}" maxlength="100"/>
						<div class="tishi">该字段存菜单的链接（不需要加dhtml后缀）或者三级动态菜单的表和字段（按照表名/视图名 id pId name lj的顺序）</div>
					</td>
				</tr>
				<tr>
				 	<td><b>是否动态：</b></td>
					<td>
						<select name="dt" class="select" onchange="fn_dtcg(this)">
							<option  {{if exobj.dt=='0'}}selected{{/if}} value="0">否</option>
							<option  {{if exobj.dt=='1'}}selected{{/if}} value="1">是</option>
						</select>
						<div class="tishi">该项是针对3级菜单的，即3级菜单可以是在菜单管理页面手动输入的，也可以是从表或视图里读取的动态树（如栏目列表）</div>
					</td>
				</tr>
				<tr>
				 	<td><b>排序：</b></td>
				 	<td><input type="text" name="px" class="input-text lh25" value="{{px}}" size="10" maxlength="4"/></td>
				 </tr>
				<tr>
				 	<td colspan="2">
						<input type="button" value="保存" class="ext_btn ext_btn_submit" onclick="fn_bc()">&nbsp;
						<input type="button" value="返回" onclick="fn_fh()" class="ext_btn">
					</td>
				 </tr>
			</tbody>
		</table>
	</form>
</script>

<!-- 这个是最右侧的菜单列表页 -->
<script id="sid_list" type="text/html">
	<div class="btns">
		<i></i><input type="button" name="button" class="btn btn82 btn_cmn btn_add" value="新增" onclick="fn_add()"/>
	</div>
	<table width="100%" border="0" cellpadding="0" cellspacing="0" class="list_table" id="CRZ0">
		<thead>
			<tr>
				<th style="width:6em">序号</th>
				<th style="width:6em">id</th>
				<th style="width:6em">pId</th>
				<th title = '菜单名称'>菜单名称</th>
				<th title = '链接/表和字段'>链接/表和字段</th>
				<th style="width:6em"  title = '是否动态'>是否动态</th>
				<th   style="" title = '直接子菜单数'>直接子菜单数</th>
				<th   style="" title = '菜单角色数'>菜单角色数</th>
				<th   style="" title = '菜单用户数'>菜单用户数</th>
				<th style="width:4em" title = '排序'>排序</th>
				<th title='操作' style="width: 8em;">操作</th>
			</tr>
		</thead>
		<tbody>
			{{each dyc as v idx}}
				<tr {{if idx%2!=0}}style='background:rgb(255, 252, 234)'{{/if}}>
					<td>{{(pg-1)*perPage+idx+1}}</td>
					<td>{{v.id}}</td>
					<td>{{v.pId}}</td>
					<td title="{{v.name}}">{{v.name}}</td>
					<td title="{{v.lj}}">{{v.lj}}</td>
					<td title="{{v.dt}}">{{v.dt==1?'是':'否'}}</td>
					<td title="{{v.cnt}}">{{v.cnt}}</td>
					<td title="{{v.cdjss}}">{{v.cdjss}}</td>
					<td title="{{v.cdyhs}}">{{v.cdyhs}}</td>
					<td title="{{v.px}}">{{v.px}}</td>
					<td>
						<a href="javascript:" onclick="fn_edit(this, '{{v.id}}', '{{v.name}}', '{{v.px}}')">编辑</a> |
						<a href="javascript:" onclick="fn_del(this,'{{v.name}}')"{{if v.cnt>0 || v.cdjss>0 || v.cdyhs>0}}class='czxgray' title="点击查看按钮灰化原因"{{/if}}>删除</a>
					</td>
				</tr>
			{{/each}}
		</tbody>
	</table>
	<div class="beizhu" style="padding:5px;letter-spacing:1px;font-size:13px">
		注：需要删光子节点后才能删除对应的父节点。如需修改菜单树中选中节点的名称，请先选中其父节点。
	</div>
</script>


	<script src="${path}/layer/layer.js"></script>
	<script src="${path}/laypage/laypage.js"></script>
    <script type="text/javascript">
    		function cdlistpg(pg, pId,pname){
			pId = pId || getSelNode().id
			pname = pname ||  $.fn.zTree.getZTreeObj("tree1").getNodeByParam("id", pId).name
			  $.getJSON('getcdlistpg.dhtml', {
			      pg: pg || 1,pId:pId
			  }, function(data){
					var html = template('sid_list', data);
					document.getElementById('rgcon').innerHTML =html;
					$(".fymsg").html('<i>'+pname+'</i>共<i>'+data.recTotal+'</i>个直接子菜单，每页<i>'+data.perPage+'</i>个，共<i>'+data.pgTotal+'</i>页')
				        laypage({
			            cont: 'fenye',
			            pages: data.pgTotal,
			            curr: pg||1,
			            skip: true,
			            jump: function(obj, first){
			                if(!first){
			                    cdlistpg(obj.curr, pId, pname);
			                }
			            }
			        }); 
			 });
    	}

		function zTreeOnClick(event, treeId, treeNode){
			cdlistpg(1, treeNode.id, treeNode.name);
		}

   		function layout(){
   			$(".content,.left,.right").height($(window).height()-25)
   			$(".right").width($(window).width()-283)
   			var maxh = $(window).height()-86+25//这里没提示，就不需要减掉25了
   			$(".lecont").css('max-height',maxh)//一个bread25,菜单树25，提示25，提示间距3,顶部间距5，底部间距3
   			if (!$.support.leadingWhitespace)$(".lecont").height(maxh)//IE6-8，max-height有点问题
   		}
   		
		//在菜单树中新增一个菜单
		function fn_add(){
			var treeObj = $.fn.zTree.getZTreeObj("tree1")
			if (treeObj !=null){
				var nodes = treeObj.getSelectedNodes();
				if (nodes.length <=0){
					layer.msg("请先选择左侧菜单树中的节点！")//也就是你想在哪个菜单下建立子菜单
				}else{
					var px=''
					$.ajax({//同步的
				    	type:"POST",async: false,
				        url:"getcdmaxpx.dhtml",
				        datatype: "text",
				        beforeSend:function(){layer.msg('请稍后......', {time:-1,icon: 16,shade: 0.3, scrollbar:false});},  
				        success:function(text){
				          	layer.closeAll();
				        	px = text
				        },
				        error: function(){
							layer.alert('网络阻塞，请重试！', { icon:2, title:'错误'});
						}
					})
					$(".fymsg,#fenye").empty()
        
					var data={root:nodes[0].name,pId:nodes[0].id, px:px,title:'新增菜单',exobj:{}}
					var html = template('sid_addedit', data);
					document.getElementById('rgcon').innerHTML =html;
					fn_dtcg($("select[name='dt']"))
				}
			}
		}

		//保存菜单，这边的保存就没有栏目那边那么复杂，当然其实栏目那边的设计还需要更加复杂，不然新用户搞不懂怎么填
		function fn_bc(){
			var name = $("#id_fm_caidan input[name='name']").val()
			var px = $("#id_fm_caidan input[name='px']").val()
 			if (isEmptyStr(name)){
				$("#id_fm_caidan input[name='name']").focus();
				layer.msg("请填写菜单名称！");
				return;
			}
			if (isEmptyStr(px)){
				$("#id_fm_caidan input[name='px']").focus();
				layer.msg("请填写排序！");
				return;
			}

			$.ajax({
		          type:"POST",
		          url:"cdaddedit.dhtml",
		          data:$("#id_fm_caidan").serialize(),
		          datatype: "text",
		          beforeSend:function(){layer.msg('正在保存......', {time:-1,icon: 16,shade: 0.3, scrollbar:false});},  
		          success:function(ret){
						if (ret=="0"){
							var n = getSelNode();
							refreshTree(n.id, '保存成功！');
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

		function getFont(treeId, node) {
			return node.dt ? {color:"#4169e1", "font-weight":"bold"} : {};//如果是动态3级子菜单，高亮显示该菜单
		}

		//刷新菜单树
   		function refreshTree(selid,msg){
			$.ajax({
		          type:"POST",
		          url:"getcdtree.dhtml",
		          data:{selid:selid},
		          datatype: "json",
		          beforeSend:function(){layer.msg('请稍后......', {time:-1,icon: 16,shade: 0.3, scrollbar:false});},  
		          success:function(json){
		          		layer.closeAll();
						var setting = {
							view: {fontCss: getFont,nameIsHTML: true},
							data: {simpleData: {enable: true}},
							callback: {onClick: zTreeOnClick}
						};
						var data = eval("("+json+")")
		          		var treeObj = $.fn.zTree.init($("#tree1"), setting, data.all);
		          		treeObj.expandAll(true);
		          		var selNode=null;
		          		if (selid) {
		          			selNode = treeObj.getNodeByParam("id", selid)
		          		}else{
		          			selNode = treeObj.getNodes()[0]
		          			selid = selNode.id
		          		}
	          			treeObj.selectNode(selNode);
						//右侧列表项
						var html = template('sid_list', data);
						document.getElementById('rgcon').innerHTML =html;
						$(".fymsg").html('<i>'+selNode.name+'</i>共<i>'+data.recTotal+'</i>个直接子菜单，每页<i>'+data.perPage+'</i>个，共<i>'+data.pgTotal+'</i>页')
				        laypage({//显示分页
				            cont: 'fenye',
				            pages: data.pgTotal,
				            curr: 1,
				            skip: true,
				            jump: function(obj, first){
				                if(!first){
				                    cdlistpg(obj.curr, selid, selNode.name);
				                }
				            }
				        });
				        layout();
				        if (msg)layer.msg(msg);
		          },
		          error: function(){
					layer.alert('菜单获取失败，请刷新重试！', { icon:2, title:'错误'});
		          }         
			});
   		}

		function getSelNode(){
			var treeObj = $.fn.zTree.getZTreeObj("tree1");
			if (treeObj !=null){
				var nodes = treeObj.getSelectedNodes();
				if (nodes.length >0){
					return nodes[0]
				}
			}
			
			return null;
		}

		//新增和编辑页面点返回
		function fn_fh(){
			 cdlistpg(1);
		}

		//删除菜单
		function fn_del(tag, cdnm){
 			if ($(tag).hasClass('czxgray')){//该菜单有角色或者用户也不能删除，这里就不做后台校验了，我嫌麻烦，角色管理是前后台都校验的
				layer.msg('请先删除【'+cdnm+'】的所有下属子菜单、菜单角色及菜单用户！')
				return;
			}

			var xh= $(tag).parent().parent().children("td:eq(0)").text()
			var nm= $(tag).parent().parent().children("td:eq(3)").text()
			layer.confirm('确定要删除序号为 '+xh+' 的菜单【'+nm+'】吗？', {
					icon : 3,
					title : '提示'
				}, function(index) {
					$.ajax({
				          type:"POST",
				          url:"delCaidan.dhtml",
				          data:{id:$(tag).parent().parent().children("td:eq(1)").text()},
				          datatype: "text",
				          beforeSend:function(){layer.msg('正在删除......', {time:-1,icon: 16,shade: 0.3, scrollbar:false});},  
				          success:function(text){
				          		if ("0"==text){
									refreshTree(null,'删除成功！');
				          		}else if ("-1"==text){
				          			layer.alert('删除失败，请联系管理员！', { icon:2, title:'错误'});
				          		}else{
				          			layer.alert(text, { icon:2, title:'错误'});
				          		}
				          },
				          error: function(){
							layer.alert('删除失败，请刷新重试！', { icon:2, title:'错误'});
				          }
					});
			});
		}

		//编辑菜单
		function fn_edit(tag, id, name, px){
			$(".fymsg,#fenye").empty()
			var selnode = getSelNode()
			if (selnode != null) {
				var exobj=null;
				$.ajax({//同步的
			          type:"POST",async: false,
			          url:"getCdById.dhtml",
			          datatype: "json",
			          data:{id:id},
			          beforeSend:function(){layer.msg('请稍后......', {time:-1,icon: 16,shade: 0.3, scrollbar:false});},  
			          success:function(json){
			          		layer.closeAll();
			          		exobj = eval("("+json+")")
			          },
			          error: function(){
			            	layer.alert('网络阻塞，请重试！', { icon:2, title:'错误'});
					  }
				})
				var data={root:selnode.name,pId:selnode.id, name:name,px:px, id:id,title:'编辑菜单',exobj:exobj}
				var html = template('sid_addedit', data);
				document.getElementById('rgcon').innerHTML =html;
				fn_dtcg($("select[name='dt']"))
			}
		}

		window.onresize=function(){
			layout();layout()
		}

		//动态下拉框改变
		function fn_dtcg(tag){
			var val = $(tag).val();
			if (val=='0'){//非动态
				$("input[name='lj']").parent().prev().children("b").text("链接：")
			}else{//是动态的
				$("input[name='lj']").parent().prev().children("b").text("表和字段：")
			}
			$("input[name='lj']").focus()
		}

		$(function(){
			refreshTree();
			layout();layout();
		});
	</script>
</body>
</html>
       