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
 *		第一级是系统后台左上角广告语右边的菜单，这个是最高一层的菜单
 *		第二级是广告语正下方可以折叠打开的菜单，就是右侧有一个减号-（加号+）的那个横条
 *     第三级是第二级包含的树形菜单，理论上菜单层次是可以无限的
 *		所以，这三级菜单弄出来，任何后台的菜单结构都可以支持了。你说你不要一级的也可以，那就只有2级和3级的，如果你2级的也不要，算了，我觉得这个有点麻烦，有2级挺好看的。当然其实都能改。另外，其他系统里会有站点这个概念，但是
 *		我真的没搞清楚站点这个概念的实际业务需求是啥，就是说站点有啥用呢，反正我是从来没用到啊，也有系统提到说站点用的很少我就不包含这个功能了，以免给大家造成负担和系统臃肿，好吧，我也就这么理解吧。如果你对站点的业务需求比较
 *		清楚，欢迎联系我，和我分享分享，如果确实不错，我会考虑以后有时间合入这个功能。
 * 3、有了角色和菜单了，那当然最后还得有角色权限设置，也就是某个角色可以看到哪些菜单，tjpcms里权限只到菜单这一级，至于更深入的crud权限，我认为这个要结合具体的业务来自行实现，并不是我偷懒，我根据我所了解的实际业务情况来看
 *		绝大多数情况下菜单权限已经完全够用了，如果深入到crud或者其他业务逻辑判断，真的不适宜由系统来实现，越俎代庖了，系统还是得精简一点，只做最核心的功能。	
 * 4、我今天想起来其实还有一个啊，角色菜单和角色权限都有了，还得有用户角色，就是说某个用户属于哪个角色，理论上应该所有的用户都有角色，这样这一套基本的后台权限就这样设计好了，不复杂，但是我觉得足以应对90%以上的情况了
 
 * 2018-06-11
 * 其实本来是想做采集的，因为这个功能对搜索引擎排名很有作用，但是我略一琢磨，感觉这个功能还有点小复杂，光是看jeecms采集的表单就能看出来了，参数无限多（后来发现帝国更多），都不知道干嘛的。所以先退而求其次，角色管理不是弄好了
 * 吗，那顺理成章的基于角色管理的审核流程管理功能也就呼之欲出了，但实事求是的讲，审核流程管理这个功能属于闭门造车，空中楼阁，原因很简单，这里牵涉到了审核、流程，这个你想走线上，可不是那么容易的，核心就一个字：权，这个东西就很
 * 难让你走线上，另外就是实际的业务流程一般会比较奇葩，不会那么乖巧说程序中设定的流程可以满足覆盖实际的流程。只能说随大流吧，别人有这功能，我也有，当然我要更好，因为我闭门苦思造了个劳斯莱斯。
 
 * 2018-06-13
 * 又参考了不少cms的设计，我有点奇怪，我记得我以前找cms的时候没这么百花齐放百家争鸣啊，怎么现在遍地都是，超级多，奇怪，难道都是开源搞的一大抄？其实大致看一看设计后，感觉审核流程或者叫工作流这一块还是很有东西在里面的，但这个
 * 东西却并不是设计和技术，而是非常非常重要的一个点，就是用户需求，用户有没有这样的需求，用户的需求如何定义和引导，如何做出的是一个产品可以包容绝大多数用户的需求而又不会使得用户体验变得复杂、繁冗和让人摸不着头脑，cms的本意是
 * 想通过简洁的页面操作代替专业的代码编程，以较小的学习成本和代价完成用户建站或者是搭建系统的意愿。但是cms系统如何应对更智能化的可视化拖拽操作建站，又如何满足更专业的实际业务需求，这是两个相反的方向。为了满足更专业化和更智能
 * 化，cms不得不提供更多的更精细的功能，但是这样又违背了cms系统出现的本意：只是想让不太懂编程的用户可以建立自己的站点，或者是专业人士在cms的基础上做二次开发。所以我看待后台审核流程或者叫工作流这个功能，我看到的不是别人的
 * 设计，无论是样式的还是功能的还是业务的，我看到的是cms面临的一个困境：我cms就是要把复杂的事情简单化来方便你们的，但是，为了方便你们，更多的你们，我越来越复杂了，甚至复杂到越来越不方便了。所以，归根结底，cms最根本的问题是，
 * 用户的群体是什么，用户到底需要什么，如何在复杂化和简洁化之间为这类用户寻求一个平衡点，我想是一个需要更多思考和大智慧的问题。
 * 今天看到的jtopcms有点惊艳到我了，后台设计非常符合我个人的审美，不知道是不是原创。
 * -->
 
 
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
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
    <meta http-equiv="X-UA-Compatible" content="IE=EDGE,chrome=1"/>
	<meta http-equiv="pragma" content="no-cache"/>
	<meta http-equiv="cache-control" content="no-cache"/>
	<meta http-equiv="expires" content="0"/>
    <title>${WZMC} - 权限菜单设置</title>
    <link rel="stylesheet" href="${path }/css/zTreeStyle/zTreeStyle.css" type="text/css"/>  
    <link rel="stylesheet" href="${path }/css/index_ad.css" type="text/css"/>  
    <link rel="stylesheet" href="${path }/css/smartMenu.css" type="text/css"/>  
    <script src="${path }/js/jquery.js" type="text/javascript"></script>    
    <script type="text/javascript" src="${path }/js/jquery.SuperSlide.2.1.1.js"></script>
    <script type="text/javascript" src="${path }/js/jquery.ztree.core.min.js"></script>
    <script type="text/javascript" src="${path }/js/jquery-smartMenu.js"></script>
    <script type="text/javascript" src="${path }/js/jquery.tjpdrag.js"></script>
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
    	</div>
    	<div class='tright'>
    		<a class="icon-qian1 cls_jzzz" href="${path}/jz.dhtml" target="_blank">捐赠作者</a>
    		<a class="icon-password" href="${path}/${htgllj}/index.dhtml" target="_blank" title="后台首页">后台首页</a>
    		<span class="icon-cguanliyuan" style="margin-right:15px" title="${ses_cjgly}">
    			${ses_cjgly}
    		</span>
    		<a class="icon-shouye" href="${path}/" target="_blank" title="前台首页">前台首页</a>
    	</div>
    </div>
	
	<div class="center">
		<div class="left" onselectstart="return false" >
			<div class="lmnu">
				<ul>
					<li>
						<span style="background: url(&quot;/alitjp/img/liger/togglebar.gif&quot;) 0px -40px no-repeat;"></span>
						<div class="biaoti">权限菜单配置<span></span></div>
						<div class="caidan" style="" id="">
							<div class="cdinner">
								<ul id="treeDemo" class="ztree" ></ul>
							</div>
						</div>
					</li>
					<li>
						<span style="background: url(&quot;/alitjp/img/liger/togglebar.gif&quot;) 0px -40px no-repeat;"></span>
						<div class="biaoti">审核流程配置<span></span></div>
						<div class="caidan" style="" id="">
							<div class="cdinner">
								<ul id="treeShenhe" class="ztree" ></ul>
							</div>
						</div>
					</li>
				</ul>
			</div>
		</div>
		<div class="right" >
				<div class="tab" onselectstart="return false">
					<ul style="position: relative;">
						<li class="tab_sel" id="tab_li_0">
							<a>角色管理</a>
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
    	©<%= 2016==Calendar.getInstance().get(Calendar.YEAR)?"2016":"2016-"+Calendar.getInstance().get(Calendar.YEAR)%> <a target="_blank" href='http://${YUMING }' class="wzmcbtm">${YUMING }</a> All Rights Reserved.
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
			        	var eifm = $("."+$(this).attr("id"));
			        	eifm.attr("src", eifm.attr("src"));
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
    		function layout(){
    		var w = $(window).width()
			var ch = $(window).height()-59-4//top31，btm28，center的上边距4
			$(".center").height(ch);
			$(".left,.right").height(ch-2);//上下边框2像素
			$(".right").width(w*0.994-218);//left宽度是212,左和右的间隔是4像素，右边框2像素
			$(".content").height(ch-2-26);//减去上下边框和tab的高度
			
			//左侧二级菜单处标题栏的宽度，以便二级菜单名字超长时有省略号
			$(".lmnu .biaoti").width($(".lmnu>ul>li:eq(0)").width() - 20 -5-5)//20是+-号图片的宽度，5是右边距，5是间隔
    	}

    	//点击树的节点
    		function zTreeOnClick(event, treeId, treeNode){
    		if (treeNode.lj=='' || treeNode.lj==null){
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
				iframe.attr("src", encodeURI(encodeURI(lanmu+'.dhtml'+(para?"?"+para:''))))//中文乱码
				iframe.attr("name", "nm_frm_"+lanmu+id)
				$(".content").append(iframe);
			}
		}

		//写两次的原因，其实我也不明白，反正两次调整的效果就对了，一次不行哦
		window.onresize=function(){
			layout();layout();
		}

		//点击TAB后左侧菜单选中和定位，这里默认的选中菜单是角色管理，如果是像默认页是【我的主页】的那种，写法略有区别
		function leftlmtreesel(id,ltselid){
			if (!ltselid) {
				return;
			}

 			$(".lmnu .ztree .node_name").parent().removeClass("curSelectedNode")
 			$("#"+ltselid).addClass("curSelectedNode")
 			var gt = $("#"+ltselid).position().top
 			var cdinner = $("#"+ltselid).parents(".cdinner")
 			cdinner.scrollTop(gt-2)
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

		$(function (){
			//捐赠作者
			var tmrJz = null;//捐赠弹出层定时器
			$('.cls_jzzz').hover(
				function(){
					if (!$(".juanzbd").is(":visible")){
						var that = this;
						layer.tips('<img src="${path}/images/tjpcms/juanz.jpg" width="100%">', that,{tips:[1, '#fff'],time:0,maxWidth:'100%', tjpcms_clsouter:'juanzbd'});//要写宽度，否则初始化时图片可能还未加载导致宽度异常
					}
				},
				function(){//离开文字时，开启定时器定时关闭弹出层
					if (tmrJz!=null) clearTimeout(tmrJz);
					tmrJz = setTimeout(function() {layer.closeAll();tmrJz = null;}, 500000)
				}
			);
			$(document).on('mouseleave', '.juanzbd',//鼠标离开弹出层，定时关闭
				function(){
					if (tmrJz!=null) clearTimeout(tmrJz);
					tmrJz = setTimeout(function() {layer.closeAll();tmrJz = null;}, 500000)
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
				  	qxcdLid = layer.tips('非常重要的一个功能，可以帮助你设置后台的角色、权限及菜单布局', that,{tips:[3, '#3595CC'],time:5000});
				},
				function(){
					layer.close(qxcdLid)
				}
			);

			//TAB项的右键菜单及TAB项拖动交换
			$(".tab>ul>li").smartMenu(imageMenuData);
			$(".tab>ul>li").tjpDrag({ fixids:['tab_li_0'] });

			//滚动广告
			jQuery(".top").slide({mainCell:".tleft ul",autoPlay:true,effect:"leftMarquee",vis:1,interTime:50,trigger:"click"});

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

    		//部分元素的布局调整
			$('.lmnu>ul').children('li:last-child').children(".caidan").addClass('cd_btn_bdr');//最后一个菜单底部加个边框

			//左侧菜单数据
			var setting = {
				callback: {
					onClick: zTreeOnClick,
					beforeClick:changeLeftSel,
					onExpand: leftTreeExColl, 
					onCollapse: leftTreeExColl,
				}
			};
			var zNodes =[//这个树是可以静态也可以动态设置的，静态的就是下面这种，写死的，动态的如【栏目列表】这个菜单中的栏目树
				{lj:'juese', id:0,name:"角色管理"},
				{lj:'caidan',id:1, name:"菜单管理"},
				{lj:'quanxian',id:2, name:"权限管理"},
				{lj:'yonghu',id:3, name:"用户管理"},
			];
			$.fn.zTree.init($("#treeDemo"), setting, zNodes);
			$("a[title='角色管理']").trigger("click");//初始时选中左侧角色管理树节点
			//$("#treeDemo>li:eq(1)>a:first").trigger("click");

			//审核流程配置
			var treelc =[
				{lj:'liucheng', id:100,name:"流程管理"},//其实重要的是流程，不是审核，其他cms喜欢叫工作流
			];
			$.fn.zTree.init($("#treeShenhe"), setting, treelc);
			//$("a[title='流程管理']").trigger("click");//初始时选中左侧角色管理树节点

    		layout();layout();
		});
	</script> 
</body>
</html>

