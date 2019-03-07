<!--
 * 作者:tjp
 * QQ号：57454144
 * QQ群：168895774（有教学视频下载）
 * 官网：http://www.tjpcms.com
 * 码云：https://git.oschina.net/tjpcms/tjpcms
 * 更新日期：2017-01-22
 * tjpcms - 最懂你的cms
 * 2018-05-10：此处修改为按照登录用户的权限来展示后台的菜单，并且只有超级管理员账号才可以看到权限管理的菜单，只有非test的超级管理员账号才可以操作权限菜单
 * -->


<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%
	request.setAttribute("path", request.getContextPath());
    response.setHeader("Pragma", "No-cache"); 
    response.setHeader("Cache-Control", "No-cache"); 
    response.setDateHeader("Expires", 0); 
%> 
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />  
    <meta http-equiv="X-UA-Compatible" content="IE=EDGE,chrome=1"/>
	<meta http-equiv="pragma" content="no-cache"/>
	<meta http-equiv="cache-control" content="no-cache"/>
	<meta http-equiv="expires" content="0"/>
    <title>${WZMC}后台管理</title>
    <link rel="stylesheet" href="${path }/css/zTreeStyle/zTreeStyle.css" type="text/css"/>  
    <link rel="stylesheet" href="${path }/css/index_ad.css?___v=<%=Math.random()%>>" type="text/css"/>
    <link rel="stylesheet" href="${path }/css/smartMenu.css" type="text/css"/>
    <script type="text/javascript" src="${path }/js/jquery.js"></script>
    <script type="text/javascript" src="${path }/js/jquery.SuperSlide.2.1.1.js"></script>
    <script type="text/javascript" src="${path }/js/jquery.ztree.core.min.js"></script>
    <script type="text/javascript" src="${path }/js/jquery-smartMenu.js"></script>
    <script type="text/javascript" src="${path }/js/jquery.tjpdrag.js"></script>
    <script type="text/javascript" src="${path }/js/cmn.js"></script>
<style>

</style>
</head>
<body onselectstart="return false;" >
    <div class="top">
    	<div class='tleft ring-hover'>
    		<div class="slogan" >
	    		<ul>
					<li ><a target="_blank" href="http://${YUMING}">${GGY}</a></li>
					<li ><a target="_blank" href="http://${YUMING}">${GGY2}</a></li>
					<li ><a target="_blank" href="javascript:">作者QQ：57454144</a></li>
					<li ><a target="_blank" href="javascript:">官方Q群：168895774</a></li>
		   			<li style="font-size:13px"><a target="_blank" href="http://${YUMING}">官网: ${YUMING }</a></li>
	    		</ul>
    		</div>
    	</div><div class="yjcdqu"><!-- 一级菜单区，不能换行写，不然有空格-->
    		<ul></ul>
    	</div>
    	<div class='tright'>
    		<a class="icon-qian1 cls_jzzz" title="点击查看详情" href="${path}/jz.dhtml" target="_blank">捐赠作者</a>
    		<c:if test="${!empty(ses_cjgly)}">
    			<a class="icon-shezhi1 cls_qxcd" href="${path}/${nedcjgly }/qxcd.dhtml" target="_blank">权限及流程配置</a>
    		</c:if>
    		<a class="icon-shouye" href="${path}/" target="_blank" title="前台首页">前台首页</a>
    		<span class="icon-cguanliyuan nicheng" <c:if test="${wdxxs>0}">style="margin-right:0"</c:if> title="${!empty ses_cjgly?ses_cjgly:fn:substring(ses_usrpt.id,3,fn:length(ses_usrpt.id))}">
    			${!empty ses_cjgly?ses_cjgly:fn:substring(ses_usrpt.id,3,fn:length(ses_usrpt.id))}
    		</span>
   			<c:if test="${!empty(ses_cjgly) && wdxxs>0 }"><span class="nav-counter" title="${wdxxs}条未读消息">${wdxxs}</span></c:if>
    		<a href="javascript:" onclick="fn_adtc()" class="icon-tuichu tcmgnleft" title="退出">退出</a> 
    	</div>
    </div>

	<div class="center">
		<div class="left" onselectstart="return false" >
			<div title="向左收缩" class="lmnushou"></div>
			<div class="lhdr">
				功能菜单
			</div><div class="lmnu"><ul></ul></div>
		</div>
		<div class="right" >
				<div class="tab" onselectstart="return false">
					<ul style="position: relative;">
						<li class="tab_sel" id="tab_li_0">
							<a>我的主页</a>
							<div class="l-tab-links-item-right"></div>
							<div class="l-tab-links-item-left"></div>
							<div class="l-tab-links-item-close rotate-hover hide"></div>
						</li>
					</ul>
				</div>
				<div class="content">
					<div class='bigload'></div>
				</div>
		</div>
	</div>
    
    <div class='btm'>
    	©<%= 2016==Calendar.getInstance().get(Calendar.YEAR)?"2016":"2016-"+Calendar.getInstance().get(Calendar.YEAR)%>
    	<a target="_blank" href='http://${YUMING }' class="wzmcbtm">${YUMING }</a> All Rights Reserved.
    </div>
    
    <script src="${path}/layer/layer.js"></script>
    <script type="text/javascript">
    	//TAB项上的右键菜单
		var imageMenuData = [
	   	 	[
		   	 	{
		        	text: "关闭当前",
		        	func:function(){if ($(this).attr("id")!='tab_li_0') $(this).children(".l-tab-links-item-close").trigger('click')},
		        	gray:function(){return $(this).attr("id")=='tab_li_0';}
			    },{
		        	text: "关闭左侧",
		        	func: function(){closeTablis($(this), $(this).prevUntil("#tab_li_0", "li"), true)},
		        	gray:function(){return $(this).index()<2}
			    }, {
			        text: "关闭右侧",
			        func: function(){	closeTablis($(this), $(this).nextUntil(".content", "li"), $(this).attr("id")=='tab_li_0')},
			        gray:function(){return $(this).index()>= $(this).siblings().size();}
			    }, {
			        text: "关闭其他",
			        func: function(){closeTablis($(this), $(this).siblings(), true)},
			        gray:function(){return ($(this).attr("id")=='tab_li_0' && $(this).siblings().size()==0) || ($(this).attr("id")!='tab_li_0' && $(this).siblings().size()==1)}
			    }
		    ],
		    [
			    {
			        text: "刷新",
			        func: function() {
			        	$(this).trigger('click')
			        	if ($(this).attr("id")!='tab_li_0'){
				        	//$("#"+$(this).data("ltselid")).trigger('click') 不是所有的都有ltselid
				        		var eifm = $("."+$(this).attr("id"));
				        	eifm.attr("src", eifm.attr("src"));
			        	}else{//是我的主页标签
			        		$(".tab_li_0").attr("src", encodeURI(encodeURI('welcome.dhtml')))
			        		$("#tab_li_0").trigger('click') 
			        	}
			        }
			    }
		    ]
		];

		//右键关闭TAB项
		function closeTablis(tag, jihe, showcur){
        	//其他兄弟节点都删掉
            jihe.each(function(){
				var id = $(this).attr("id")
				if (id=='tab_li_0') return true;
				$(this).animate({width:0},500,function(){$(this).remove()});
				$("."+id).remove()
            })
            //显示当前的
			if (showcur){
				tag.addClass('tab_sel')
				$("."+tag.attr("id")).show();
				leftlmtreesel(tag.attr("id"), tag.data("ltselid"))
			}
		}

		//重新调整布局
		//2018-07-02：修改了宽度计算，使得左侧菜单区可以收缩展开了，后面还会再增加动态拖拽左侧菜单区
    		function layout(){
    		var w = $(window).width()
			var ch = $(window).height()-59-4//top31，btm28，center的上边距4
			$(".center").height(ch);
			$(".left,.right").height(ch-2);//上下边框2像素
			
			//现在左侧菜单区可以折叠展开了，所以.left的宽度要动态读取
			var wleft = $(".center>.left").width()//不包含边框的宽度
			
			$(".right").width(w*0.994-wleft-6-2);//left宽度默认是是212(包含左右各1像素边框),左和右的间隔是4像素，.right边框2像素
			$(".content").height(ch-2-26);//减去上下边框和tab的高度
			
			//左侧二级菜单区域的高度，以便二级菜单太多时有滚动条
			$(".lmnu").height(ch-2-$(".lhdr").outerHeight())
			
			//左侧二级菜单处标题栏的宽度，以便二级菜单名字超长时有省略号
			$(".lmnu .biaoti").width($(".lmnu>ul>li:eq(0)").width() - 20 -5-5)//20是+-号图片的宽度，5是右边距，5是间隔

/* 			这里是以前保证栏目树这里的滚动条正确显示的，现在不需要这样的设计了
			var maxh = ch-2-26-26-$(".lmnu>ul>li:not(:last)").height()-6;//26是功能菜单的高度,26是自己那个区的标题高度，6是margin
			$("#lanmutree").parent().css('max-height',maxh);
			if (!$.support.leadingWhitespace)$("#lanmutree").parent().height(maxh);//IE6-8，max-height有点问题 */
    	}

    	//点击树的节点
    		function zTreeOnClick(event, treeId, treeNode){
    		fn_upXiala();
    		if (treeNode.lj=='' || treeNode.lj==null || treeNode.lj==undefined){
    			return;
    		}
    		
    		//如果已经有了直接打开
    			var found= false;
    		var w=0;
    		$(".tab>ul>li").each(function(d){
    			$(this).removeClass("tab_sel")
    			w += $(this)[0].offsetWidth+2;
    			if (!found && $(this).attr("id")=="tab_li_"+treeNode.id){//如果已经有了，要重新刷新一下，因为这边点的是菜单
    				found = true;
    				$(this).data("ltselid", $(".curSelectedNode").attr("id")).addClass("tab_sel")
    				$("."+$(this).attr("id")).remove();
    				loadiFrame(treeNode.lj, treeNode.id,treeNode.para);
    			}
    		})
    		if (!found){//还没打开过
				$(".tab>ul>li:eq(0)").after($(".tab>ul>li:first").clone(true))
	    		$(".tab>ul>li:eq(1)").data("ltselid", $(".curSelectedNode").attr("id")).addClass("tab_sel").attr("id", "tab_li_"+treeNode.id)
	    			.children().first().text(treeNode.name).parent().children(".l-tab-links-item-close").removeClass("hide").show()
	    		$(".tab>ul>li:eq(1)").smartMenu(imageMenuData);
	    		w += $(".tab>ul>li:eq(1)")[0].offsetWidth+2;
	    		loadiFrame(treeNode.lj, treeNode.id,treeNode.para);

	    		 //如果所有tab页的总长度超过了tab条的长度，则把最后的删掉
				var wtab = $(".tab").width()
				while (w > wtab){
					e = $(".tab>ul>li:last")
					w -= e[0].offsetWidth-2
					$("."+e.attr("id")).remove();//对应的iframe也删掉
					e.remove()
				}
    		}
    	}

		//根据栏目src直接加载iframe
		//这边有个我自己设定的业务逻辑，就是前台注册用户只能在前台的弹出框里登录，后台的这个登录页注册用户是无法登录的
		//同样的，前台注册用户的那个弹出框里，后台用户也是无法登录的
		//所以，如果tab页打开的是登录页了，说明session失效了，那就要重新登录或者打开首页（就看是前台还是后台用户），但不能显示在TAB项打开的子页面里，而是应该显示在top窗口上
		//并且我不想在每个tab项打开的页面里通过写js或者引入js文件来实现这一效果，而是直接写在这里了
		function loadiFrame(lanmu, id, para){
			if (!lanmu){
				
			}else{
				$(".content>iframe").hide();
				$(".bigload").show();
				var iframe = $("<iframe>");
			    iframe.load(function(){
			        //$(window.frames["nm_frm_"+lanmu+id].document.body).find(".content").height(iframe.height()-25)//25是bread的高度
			        $(window.frames["nm_frm_"+lanmu+id].document).click(function(){$.smartMenu.remove();layer.closeAll();})//隐藏右键菜单及关闭捐赠的弹出框
			        $(window.frames["nm_frm_"+lanmu+id].document).mousemove(function(){$.tjpDrag.cureReturn()})
			        var doURL = $(this)[0].contentWindow.document.URL;
			        var toptiao = ('${!empty(ses_usrpt) && fn:substringBefore(ses_usrpt.id,'_')=='GR'}'=='true')?'${path}\/':'${path}\/${dlhtlj}.dhtml';
 			        $(window.frames["nm_frm_"+lanmu+id].document.body).append("<script>if ('"+doURL+"'.indexOf('${dlhtlj}.dhtml')!=-1"
 			        	+"&& window.top!=null){window.top.location= '"+toptiao+"'}<\/script>");
			        $(".bigload").hide();
			        iframe.show();
			    })
				iframe.addClass("riframe tab_li_"+id)
				iframe.attr("frameborder", 0)
				iframe.attr("src", encodeURI(encodeURI(lanmu+'.dhtml'+(para?("?"+para):''))))//防止中文乱码
				iframe.attr("name", "nm_frm_"+lanmu+id)
				$(".content").append(iframe);
			}
		}

		//写两次的原因，其实我也不明白，反正两次调整的效果就对了，一次不行哦
		window.onresize=function(){
			layout();layout();
		}

		//点击TAB后左侧菜单选中和定位
		function leftlmtreesel(id,ltselid){
			$(".lmnu .ztree .curSelectedNode").removeClass("curSelectedNode");//welcome页面的话不需要有左侧树节点选中
			if (!ltselid) {
				return;
			}

   			if (id!="tab_li_0"){
   				$("#"+ltselid).addClass("curSelectedNode")
   				var gt = $("#"+ltselid).position().top
   				var cdinner = $("#"+ltselid).parents(".cdinner")
   				cdinner.scrollTop(gt-2)
   			}
		}

		//子窗口调用来刷新未读消息数
		function refreshWdxx(c){
			var wdcnt = parseInt($(".nav-counter").text())
			if (!isNaN(wdcnt)){
				wdcnt-=c;
				$(".nav-counter").text(wdcnt)
				if (wdcnt<=0) {
					$(".nav-counter").remove();
				}
			}
		}

		function leftTreeExColl(e, treeId, treeNode){
			layout();layout();
		}

		//这个其实没啥用，可能也就第一句有用，我本来是打算单击“配置项”这个菜单来实现展开的，但是这样弄有问题，有点废弃这个函数
		function changeLeftSel(treeId,treeNode){
			$(".lmnu .ztree .curSelectedNode").removeClass("curSelectedNode")
		
			var treeObj = $.fn.zTree.getZTreeObj(treeId);
			if (treeNode.lj=='' || treeNode.lj==null) {
				//treeObj.expandNode(treeNode);
				return;
			}

			//alert($(".ztree>li a[class*='level']").length)这样写也行
	        treeObj.selectNode(treeNode);
		}

		function fn_upXiala(){
			//如果一级菜单的下拉子菜单打开着的话，关掉
			if ($(".clsyjxiala").is(":visible")){
				clearTimeout(xltmrId);
				xltmrId = null;
				$(".clsyjxiala").slideUp("fast");
			}
		}

		//点击栏目内容的树节点
		//2018-09-01：将fulanmu_zi和fulanmu_nr这两种类型的cid传递改为传pId的值，不在前端查出子节点传递到后台了
		function zTreeOnClklm(event, treeId, treeNode) {
			fn_upXiala();

			if (treeNode.___cid){//是来自栏目内容里的菜单
				if ("链接" == treeNode.lx){
					if (treeNode.qtlj!=null && (treeNode.qtlj.indexOf("http://")!=-1 || treeNode.qtlj.indexOf("https://")!=-1)){
						window.open(treeNode.qtlj,"_blank")
					}else if (treeNode.qtlj!=null && treeNode.qtlj.indexOf(".dhtml")==-1 ){
						window.open("../"+treeNode.qtlj+".dhtml","_blank")
					}else if (treeNode.qtlj!=null && treeNode.qtlj.indexOf(".dhtml")!=-1){
						window.open("../"+treeNode.qtlj,"_blank")
					}else{
						layer.alert('请正确设置【'+treeNode.name+'】栏目的url值', {icon:0,title:"提示"})
					}
					return;
				}else if ("父栏目nr" == treeNode.lx){
					var cld = treeNode.children
					if (cld && cld.length>0){
						treeNode.lj = treeNode.children[0].lj
					}else{
						layer.alert("【"+treeNode.name+"】栏目类型为【父栏目nr】，但尚无子栏目，请先添加！")
						return;
					}
				}
			}

			treeNode.para =  "___mid=" + treeNode.id//menu id，就是菜单id了，如果cdid好像和cid有点混淆啊
			if (treeNode.___cid) treeNode.para +=  "&___cid=" + treeNode.___cid
			zTreeOnClick(event, treeId, treeNode);
		}

		//退出注销
		function fn_adtc(tag){
			$.ajax({
				type:"POST",
				url:"tuichu.dhtml",
				datatype: "text",
				beforeSend:function(){layer.msg('退出中，请稍后......', {time:-1,icon: 16,shade: 0.3, scrollbar:false});},  
				success:function(data){
					layer.closeAll();
					//畅言同步退出
					if ('${!empty(ses_usrpt)}'=='true'){
						var img = new Image();
						img.src='http://changyan.sohu.com/api/2/logout?client_id=cysHEHjOS';
					}
					setTimeout(function(){location.href = "${path}/"+data+".dhtml"},50)//防止畅言退出还没来得及搞定
				},
				error: function(){
					layer.alert('网络阻塞，请重试！', { icon:2, title:'错误'});
				}         
			});
		}

		//初始化的一些工作
		$(function (){
			//捐赠作者
			var tmrJz = null;//捐赠弹出层定时器
			$('.cls_jzzz').hover(
				function(){
					if (!$(".juanzbd").is(":visible")){
						var that = this;
						layer.tips('<img src="${path}/images/tjpcms/juanz.jpg">', that,{tips:[1, '#fff'],time:0,maxWidth:'100%', tjpcms_clsouter:'juanzbd'});
					}
				},
				function(){//离开文字时，开启定时器定时关闭弹出层
					if (tmrJz!=null) clearTimeout(tmrJz);
					tmrJz = setTimeout(function() {layer.closeAll();tmrJz = null;}, 8888)
				}
			);
			$(document).on('mouseleave', '.juanzbd',//鼠标离开弹出层，定时关闭
				function(){
					if (tmrJz!=null) clearTimeout(tmrJz);
					tmrJz = setTimeout(function() {layer.closeAll();tmrJz = null;}, 8888)
				}
			)
			$(document).on('mouseenter ','.juanzbd',//鼠标进入弹出层，定时器则不关闭，离开再关闭
				function(){
					if (tmrJz!=null){
						clearTimeout(tmrJz);
						tmrJz = null;
					}
				}
			)
		    $(document).click(function(){
		        layer.closeAll();
		        if (tmrJz!=null){clearTimeout(tmrJz);tmrJz=null;}
		    });

			//提示权限菜单这个设置项的功能
			var qxcdLid = null;
 			$('.cls_qxcd').hover(
				function(){
					var that = this;
				  	qxcdLid = layer.tips('非常重要的一个新增功能，可以帮助你设置后台的角色、权限、菜单结构及配置审核流程等等', that,{tips:[3, '#3595CC'],time:5000});
				},
				function(){
					//layer.close(qxcdLid)
				}
			);
		
			//滚动广告
			jQuery(".top").slide({mainCell:".tleft ul",autoPlay:true,effect:"leftMarquee",vis:1,interTime:50,trigger:"click"});

			//点击未读消息徽章，这个是模拟点击树节点了
			$(".nav-counter").click(function(){
				zTreeOnClick(null,null,{id:'tnid_wdxx', name:"未读消息", lj:"wdxx"})
			})

			//TAB项的右键菜单及TAB项拖动交换
			$(".tab>ul>li").smartMenu(imageMenuData);
			$(".tab>ul>li").tjpDrag({ fixids:['tab_li_0'] });

			//点击TAB项
    		$(".tab>ul>li").click(function(e){
    			$.smartMenu.remove();
    			var id = $(this).attr("id")
    			leftlmtreesel(id,$(this).data("ltselid"))
    			$(this).siblings().removeClass("tab_sel");
    			$(this).addClass("tab_sel");
    			$(".content>iframe").hide();
    			$("."+id).show();
    			e.stopPropagation()
    		})

    		//关闭TAB
    		$(".l-tab-links-item-close").click(function(e){
    			var par = $(this).parent()
    			var id = par.attr("id")
    			if (id=='tab_li_0') {
    				$(this).hide()
    				return;
    			}
    			
    			if (par.attr('class').indexOf('tab_sel') != -1){//该项是选中的，需要切换到下一个选中页面，左侧菜单的选中也要变
 		    		par.remove()
	    			$("."+id).remove();
    				var fst = ($(".tab>ul>li").size()==1) ? $(".tab>ul>li:first"):$(".tab>ul>li:eq(1)")
    				fst.addClass('tab_sel')
    				$("."+fst.attr("id")).show();
    				leftlmtreesel(fst.attr("id"), fst.data("ltselid"))
    			}else{//不是选中的直接删除掉就可以了
		    		par.remove()
	    			$("."+id).remove();
    			}
				e.stopPropagation()
    		})
    		
    		//左侧菜单区的收缩展开
    		$(".lmnushou").hover(function(){
    			
    		},function(){
    			
    		})
    		$(".lmnushou,lmnuzhan").click(function(){
    			var cls = $(this).attr("class")
				var wleft = $(".left").width();
				var wright = $(".right").width();
				$(".left *").addClass("lmuhideroll")//就是如果是动画效果收缩，会有滚动条重叠显示的小问题，这里就全部禁掉，展开后再全部启用
    				if (cls.indexOf("lmnushou")!=-1){//点击收缩
					var wZd = 24;
					$(".left").animate({width:wZd},function(){
						$(this).css("background-color","#EAF2FE")
						$(".lmnushou").removeClass("lmnushou").addClass("lmnuzhan")
						$(".left *").removeClass("lmuhideroll")
					}).children(":not(.lmnushou)").fadeOut()
					$(".right").animate({width:wright+wleft-wZd})
    			}else{//点击展开
    					var wZd = 210;
    				$(".left").animate({width:wZd},function(){
    					$(".lmnuzhan").removeClass("lmnuzhan").addClass("lmnushou")
    					$(".left *").removeClass("lmuhideroll")
    				}).css("background-color","#fff").children(":not(.lmnushou)").fadeIn()
    				$(".right").animate({width:wright+wleft-wZd})
    			}
    		})

    		//部分元素的布局调整
    		loadiFrame('welcome', 0);//加载右侧的初始iframe，即默认打开的

    		//先是要把当前登录角色拥有的一级菜单查出来，显示在广告语的右边，并且需要考虑一级菜单过多（或者说一级菜单区域过小）而必须有下拉显示的情况
    		fn_ntp_json("aj_get_htcdtree.dhtml", {}, fn_gethtcdtree);
    		
    		layout();layout();
		});

		//把下拉菜单的数据填充起来
		var xltmrId = null;
		function apdYjXiala(yjcd){
			var curGs = $(".yjcdqu>ul>li").length-1;
			var exiala = $(".yjcdqu>ul>li:last")
			var eul = exiala.addClass('xialali').append("<ul class='clsyjxiala'></ul>").children("ul")
			for (var i=curGs;i<yjcd.length;i++){
				var eli = $("<li><a title='"+yjcd[i].name+"'>"+yjcd[i].name+"</a></li>")
				eul.append(eli.data("yjcdid",yjcd[i].id))
			}
			
			//下拉子菜单点击事件
			$(".clsyjxiala>li>a").click(function(){
			    var iscursel = $(this).hasClass('xialasel')
				$(".yjcdqu>ul>li").siblings().removeClass('yjcdlisel').children('a').removeClass('yjcdasel')
				$(".yjcdqu>ul>li:last").addClass('yjcdlisel')
				$(this).parent().siblings().children("a").removeClass('xialasel')
				$(this).addClass('xialasel')
				fillSubTree(g_ejcd[$('.clsyjxiala>li').index($(this).parent())+$(".yjcdqu>ul>li").length-1], iscursel)
				
				return false;
			})
			$(".clsyjxiala").hover(function(){
				if (xltmrId!=null) {clearTimeout(xltmrId);xltmrId=null;}
			},function(){
				if (!$(this).parent().hasClass('yjcdlisel'))$(this).prev().removeClass('yjcdasel')
				xltmrId = setTimeout(function(){xltmrId = null;if ($(".yjcdqu>ul>li:last").children("ul").is(":visible")) $(".yjcdqu>ul>li:last").children("ul").slideUp("slow");},1000)
				return false;
			})

			//下拉按钮点击事件
 			$(".yjcdqu>ul>li:last").on(/*mouseenter*/"click",function(){//此处mouseenter理论上只作用于父元素内部，但是这里父子元素并不是包裹关系，所以产生的现象我无法理解，需要进一步实验，碍于时间就算了，转为替代的解决办法
				$(this).children("ul").slideToggle("fast");
				if (xltmrId!=null) {clearTimeout(xltmrId);xltmrId=null;}
			})
 			$(".yjcdqu>ul>li:last").hover(function(){
					if (!$(this).children("ul").is(":visible"))$(this).children("ul").slideDown("fast");
					if (xltmrId!=null) {clearTimeout(xltmrId);xltmrId=null;}
				},function(){
					xltmrId = setTimeout(function(){xltmrId = null;if ($(".yjcdqu>ul>li:last").children("ul").is(":visible")) $(".yjcdqu>ul>li:last").children("ul").slideUp("slow");},3000)
			})
		}

		//进入页面时提示一下权限菜单功能
		function showQxTip(){
			$('.cls_qxcd').trigger('mouseenter');
		}

		//把当前角色的所有菜单查询和显示出来
		var g_ejcd=null;
		function fn_gethtcdtree(data){
			if (data ==null || data.yjcd==null || data.yjcd.length==0) {showQxTip();return};

			//把一级菜单显示出来，如果过多，以下拉显示
			var w = $(window).width()
			var yjcdw = w-$(".tleft").width()-$(".tright").width()-10-20//10是tright的右边距，20是与tright的边距
			$(".yjcdqu").width(yjcdw)
			var curw = 0;
			$.each(data.yjcd, function(idx,obj){
				$(".yjcdqu>ul").append("<li><a title='"+obj.name+"'>"+obj.name+"</a></li>");
				var ew = $(".yjcdqu>ul>li:last").data("yjcdid",obj.id).outerWidth();//这项元素的宽度，要包含边框内边距之类的，不可用width，会有计算的bug
				curw += ew;
				if (curw>yjcdw){
					$(".yjcdqu>ul>li:last").remove();//如果添加了下一个li后宽度超出最大限度了，则删掉它
					curw -= ew;
					if (yjcdw-curw>26){//剩余还可用的宽度足够放一个下拉按钮，min-width=24，再加左右边框各1px
						$(".yjcdqu>ul").append("<li><span class='downward'></span></li>");
						apdYjXiala(data.yjcd)
					}else if ($(".yjcdqu>ul>li").length>0){//剩余的不够放一个下拉按钮了，但还有剩余的元素可供删除，则再删除该元素，则必有足够位置放下拉，因为min-width=24
						var tpe = $(".yjcdqu>ul>li:last");
						curw -= tpe.width();
						tpe.remove();
						$(".yjcdqu>ul").append("<li><span class='downward'></span></li>");
						apdYjXiala(data.yjcd)
					}else{//一级菜单区域太小，连一个下拉按钮都放不下，可以提示一下用户
						layer.msg('一级菜单区太窄，请调大页面宽度后刷新重试！')
					}
					return false;
				}
			})

			//把当前选中的一级菜单的下属子菜单全部显示出来，初始化时默认是选中第一个1级菜单的
			g_ejcd = data.ejcd;
			//fillSubTree(data.ejcd[0], true)   //默认会点击第一个一级菜单的，所以就不用写在这里了
			
			//写下面这个纯粹是为了秀一下我的tjpdrag了，就是类似这种平铺的导航、TAB，都可以这样一句话来实现拖动交换
			//但js插件的很多基本概念我还没搞清楚，我想后期再写一个分页，以替换掉laypage为目标
			$(".yjcdqu>ul>li").tjpDrag({
				beforeswap:function(old,nuu){
					var tmp = g_ejcd[old];
					g_ejcd[old] = g_ejcd[nuu];
					g_ejcd[nuu]=tmp;
				},afterswap:function(old,nuu){
					fn_yuanjiao()
				}
			});
			
			//悬浮时字体颜色
			$(".yjcdqu>ul>li").hover(
				function(){
					$(this).children("a,span").addClass('yjcdasel')
				},function(){
					if (!$(this).hasClass('yjcdlisel')) $(this).children("a,span").removeClass('yjcdasel')
			})
			$(".yjcdqu>ul>li:not(.xialali)").click(function(){
				var iscursel= $(this).hasClass('yjcdlisel');
				//if ($(".yjcdqu>ul>li").length>1){
					$(this).siblings().removeClass('yjcdlisel').children('a,span').removeClass('yjcdasel')
					$(".clsyjxiala>li>a").removeClass('xialasel')
					$(this).addClass('yjcdlisel').children('a').addClass('yjcdasel')
					if ($(".xialali")!=null) $(".xialali")
				//}
				fillSubTree(data.ejcd[$(this).index()], iscursel)
			}) 
			
			fn_yuanjiao()//设置圆角
			$(".yjcdqu>ul>li:eq(0)").not(".xialali").trigger('click')//默认刷新后打开第一个菜单项
			showQxTip()
		}
		
		//设置一级菜单区的圆角
		function fn_yuanjiao(){
			$(".yjcdqu>ul>li").removeClass('yuanjiao1 yuanjiao2')
			if ($(".yjcdqu>ul>li").length>1){
				$(".yjcdqu>ul>li:first").addClass('yuanjiao1');
				$(".yjcdqu>ul>li:last").addClass('yuanjiao2');
			}else{
				$(".yjcdqu>ul>li:first").addClass('yuanjiao');
			}
		}
		
		//填充指定1级菜单的子菜单树
		//tree.setting.view.expandSpeed。这个是解决什么问题呢，就是我的2级菜单的标题区域是设置了长度了，超出了会显示...，但是在这里，展开树时是有动画效果的，就是导致在计算标题宽度时没能计算到滚动条宽度，导致
		//标题处省略号和+-标识有些重叠，解决办法有三个
		//1. 把标题处的宽度设置再小一点，小于滚动条的宽度，这样即使有滚动条也不会重叠，缺点就是可现实文字的区域略小一点了
		//2. 在这个函数的最后设置一个定时器，50毫秒后再layout
		//3. 按照贴吧里的办法，展开时不做动画了，就不会异步了，完事后再把动画效果保留住
		function fillSubTree(ejcd, iscursel){
			$(".lmnu>ul>li").remove()
			if (iscursel){
				
			}else{
				$(".tab>ul>li").not(":eq(0)").each(function(){//我就采用换了一级菜单后把TAB清空的方式了，好像这样简单一点，如果是不同的一级菜单下的TAB共存，有点点麻烦，我就不搞了，这样其实也挺好，简单嘛，大家都简单
					var id = $(this).attr("id")
					$("."+id).remove()
					$(this).remove()
				})
				$("#tab_li_0").trigger('click')
			}
			if (ejcd==null|| ejcd.length==0)return;

			var setting = {
				data: {simpleData: {enable: true}},//以列表形式给ztree，由ztree根据列表数据中的节点id，pId来组织树结构
				callback: {
					onClick: zTreeOnClklm,
					beforeClick:changeLeftSel,
					onExpand: leftTreeExColl, 
					onCollapse: leftTreeExColl
				}
			};
			
			for(var i=0;i<ejcd.length;i++){
				$(".lmnu>ul").append("<li><span></span><div class='biaoti' title='"+ejcd[i].name+"'>"+ejcd[i].name+"</div>"
					+"<div class='caidan' ><div class='cdinner'><ul class='ztree' id='___ejcdtree"+i+"'></ul></div></div></li>");
				if (ejcd[i].sub!=null && ejcd[i].sub.length>0){//有子菜单树，则添加进去
					var tree =$.fn.zTree.init($(".lmnu>ul>li:eq("+i+")").find(".ztree"), setting, ejcd[i].sub);
					tree.setting.view.expandSpeed = "";
					tree.expandAll(true);
					tree.setting.view.expandSpeed = "fast";//https://tieba.baidu.com/p/2210467721?red_tag=0616035354
				}
			}
			$('.lmnu>ul>li:last').children(".caidan").addClass('cd_btn_bdr');//最后一个菜单底部加个边框

			//二级菜标题处鼠标的移入移出和折叠展开，ecaidan.is(":hidden")?"1px": "0px")这个是为了修复+号时移入移出有+号晃动的感觉，而-号就没有，这是截取图片背景位置不精确的问题
			$(".lmnu>ul>li>span").hover(
				function(index){
					var ecaidan = $(this).parent().children(".caidan");
					if (ecaidan.is(":animated")) return ;//解决动画中突然移出，+-号显示不正确的情况
					$(this).css("background","url('${path}/img/liger/togglebar.gif') no-repeat "+(ecaidan.is(":hidden")?"1px": "0px")+" "+(ecaidan.is(":hidden")?"-20px": "-60px"))
				},function(index){
					var ecaidan = $(this).parent().children(".caidan");
					if (ecaidan.is(":animated")) return ;
					$(this).css("background","url('${path}/img/liger/togglebar.gif') no-repeat 0 "+(ecaidan.is(":hidden")?" 0px": "-40px"))
				}
			)
			$(".lmnu>ul>li").click(function(index){
				$(this).children("span").css("background","url('${path}/img/liger/togglebar.gif') no-repeat 0 "+($(this).children(".caidan").is(":visible")?" 0": "-40px"))
				$(this).children(".caidan").slideToggle("normal", function(){
					//$(this).prev().children("span").css("background","url('${path}/img/liger/togglebar.gif') no-repeat 0 "+($(this).is(":visible")?" -40px": "0"))
					layout();
					layout();
				});
			})
			$(".ztree").click(function(e){e.stopPropagation()})//现在是改成点击li折叠展开了，ztree也属于li，所以要子元素阻止事件冒泡

			layout();
			layout();//setTimeout(function(){layout();layout();},100)
		}
		
		function sxnc(nc,tx){
			layer.msg("新的昵称是："+nc,{offset:'rb'})
		}
	</script> 
</body>
</html>

