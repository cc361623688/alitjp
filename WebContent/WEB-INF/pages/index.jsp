<!--
 * 作者:tjp
 * QQ号：57454144
 * QQ群：168895774（有教学视频下载）
 * 官网：http://www.tjpcms.com
 * 码云：https://git.oschina.net/tjpcms/tjpcms
 * 更新日期：2017-01-22
 * tjpcms - 最懂你的cms
 * -->

<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<% request.setAttribute("path", request.getContextPath());%>

<!doctype html>
<html>
<head>
<title>tjpcms - 首页</title>
<%@ include file="inc/meta.jsp"%>
<link rel="stylesheet" href="${path }/css/barrager.css" type="text/css"/>

<style>
	a{-webkit-transition: all 0.5s; -moz-transition: all 0.5s; -o-transition: all 0.5s;}
	@keyframes pulse{
		0%{-webkit-transform:scale3d(1,1,1);-ms-transform:scale3d(1,1,1);transform:scale3d(1,1,1)} 
		75%{-webkit-transform:scale3d(1,1,1);-ms-transform:scale3d(1,1,1);transform:scale3d(1,1,1)} 
		92%{-webkit-transform:scale3d(1.07,1.07,1.07);-ms-transform:scale3d(1.07,1.07,1.07);transform:scale3d(1.07,1.07,1.07)}
		100%{-webkit-transform:scale3d(1,1,1);-ms-transform:scale3d(1,1,1);transform:scale3d(1,1,1)}
	}
	.pulse{-webkit-animation-name:pulse;animation-name:pulse}
	.animated{-webkit-animation-duration:6s;animation-duration:6s;
		-webkit-animation-fill-mode:both;animation-fill-mode:both;z-index:100;
		animation-iteration-count:infinite;-webkit-animation-iteration-count:infinite;}
	.kaic{
		border: solid 1px #0a8;border-left-width: 6px;width: 94%;margin: 0 auto;
	    padding: 15px;margin-top: 20px;
	    border-radius: 4px;text-align: justify !important;    box-sizing: border-box;
	}
	.kaic strong{
		display: inline;
	    font-size: 20px;
	    color:#0a8
	}
	.kaic p{margin-top: 10px;
		line-height: 24px;text-indent: 2em;color: #b94a48;font-size: 15px;text-shadow: silver 0px 1px 1px !important;text-align: justify !important;
	}
	.xiazai{width: 100%;margin: 0 auto;padding: 5px 0 26px;text-align: center;position: relative;
	    background: -webkit-linear-gradient(left, #f2fefa, rgba(21, 123, 195, 0.3),#f2fefa);}
	.xiazai a{text-decoration: underline;font-size: 12px;}
	.anniu1:hover{animation-play-state:paused;-webkit-animation-play-state:paused;}
	.anniu1{display:inline-block;width: 168px;-webkit-border-radius: 6px;padding: 11px 0px;
	-moz-border-radius: 6px;background:  #1c84c6;color: #fff;box-shadow: inset 0 0 0 #1872ab ,0 5px 0 0 #1872ab,0 10px 5px #999;
	border-radius: 6px;cursor: pointer;border:1px solid  #1c84c6;vertical-align: bottom;}
	.anniu1:hover{filter:alpha(opacity=80);opacity:0.8;-moz-opacity:0.8;-khtml-opacity:0.8;}
	.icon-xiazai:before{font-size: 26px !important;}
	.icon-xiazai label{font-size: 18px;font-weight: bold;cursor: pointer;}
	.tip{text-align: center;color:#333;margin-top: 0;}
	.tip p{padding: 2px 0;}
	.tip p a{text-decoration: underline;}
	.bdsharebuttonbox {padding-top: 10px !important;position: relative;}
	.bdsharebuttonbox a{
		float:none !important; padding: 8px 15px !important;margin: 0 !important;
		font-size: 12px !important;height: 16px !important;line-height: 15px !important;}
		
	.bds_count{display:inline-block;cursor: default !important;margin: 0 !important;line-height: 15px;width: 42px;
		position: absolute;top: 20px;margin-left: 0px !important;}
	.btn{
		-webkit-border-radius: 6px;-moz-border-radius: 6px;border-radius: 6px;
		border: 1px solid #ccc;padding: 11px 0px;
		background: #3bb4f2 !important;color: #fff !important;
		cursor: pointer;border:1px solid  #3bd;display:inline-block}
	}
	.dsharebuttonbox a:hover{filter:alpha(opacity=80) !important;opacity:0.8 !important;-moz-opacity:0.8 !important;-khtml-opacity:0.8 !important;}
	.bdshare-button-style0-16 a:hover{filter:alpha(opacity=80) !important;opacity:0.8 !important;-moz-opacity:0.8 !important;-khtml-opacity:0.8 !important;}
	.zancnt{background: url("${path}/images/tjpcms/sc2.png") no-repeat -54px -46px; display:inline-block;width: 42px;height: 16px;line-height: 15px;
		position: relative;top: 2px;}
	/* .bdshare-button-style0-16 .icon-iconfontcolor68{margin-left: 20px !important;} */
	.icon-iconfontcolor68:before{font-size: 17px;}
	.zancnt:hover{background: url("${path}/images/tjpcms/sc2.png") no-repeat -12px -46px; cursor: default !important;}
	.xzcnt{display: inline-block;width: 56px;}
	.sspznr{text-decoration: underline !important;padding: 3px 16px 0 3px;background:url(${path}/images/tjpcms/sywh1.png) no-repeat right -1px;background-size:14px 14px;margin-right: 3px;}
	.layui-layer-tips{    box-shadow: 0 3px 9px rgba(0, 0, 0, 0.1) !important;}
	.yqlj{border-top:1px dotted  rgb(189,215,242);display: table;}
	.yqlj span{    display: table-cell;vertical-align: middle;}
	.yqlj .ljtt{vertical-align: middle;background: #3595CC;color: #fff;width: 60px;font-size: 13px;text-align: center;padding: 8px;display: table-cell;-webkit-border-radius: 2px;
	-moz-border-radius: 2px;border-radius: 2px;}
	.ljnr{display: inline-block;;vertical-align: bottom;}
	.ljnr li{float: left;padding: 0 10px;}
	.ljulout{padding: 4px 0 4px 10px;;width: 911px;display: inline-block;    line-height: 1.5;}
	.ljnr li a{text-decoration: none;}
	.anleft{display: inline-block;text-align:left;margin-right: 10px;position:relative;top:10px}
	.anright{display: inline-block;text-align:left;margin-left: 10px;position: relative; top: 10px;}
	.xiazai li{height: 22px}
	.clsclspc:before{font-size: 24px !important;}
	.syjztu1{width: 120px;height: 120px;;}
	.syjztu2{width: 100px;height: 100px;;}
	.xinwen {margin: 0 auto !important;width: 100%;text-align: center;padding: 20px 0 }
	.xinwen .xwblk{display: inline-block;background: url(${path}/images/qt/xwbg1.png) no-repeat;width:305px;margin-right: 19px;    vertical-align: top;}
	.xwtitle{height: 32px;text-align: left;line-height: 32px;padding-left: 15px;;font-size: 15px; text-shadow:1px 1px 1px silver;color:red}
	.xinwen ul li{text-align: left !important;padding: 6px;height: 18px;position: relative;}
	.xinwen ul li span{display: inline-block;}
	.xinwen ul li span a{text-decoration: none;}
	.xinwen .xwneirong{width: 200px;overflow: hidden;text-overflow:ellipsis;white-space: nowrap}
	.xinwen .xwriqi{position: absolute;right: 6px;}
	.clszhixie{padding: 2px 0;height:113px;overflow: hidden;border-bottom: 1px solid rgb(219,219,219);}
	.danmuipt{width: 238px;    border: 1px solid #ccc;outline: none;
    height: 28px;position: absolute;right: 69px;    top: 34px;font-size: 12px;
    padding: 0 5px;
    border-radius: 4px 0  0 4px;   color: #333;-webkit-transition: all .5s; -moz-transition: all .5s; 
    -ms-transition: all .5s; -o-transition: all .5s; transition: all .5s;}
    .danmubtn{position: absolute;right: 15px;    top: 34px;
		width: 55px;
		height: 30px;
		line-height: 27px;
		margin-left: 10px;
		border-radius: 0 4px 4px 0;
		font-size: 13px;
		color: #fff;
		background: #cf5a5d;
		background: -webkit-linear-gradient(left, #f6ad36, #cf5a5d);
		background: -o-linear-gradient(right, #f6ad36, #cf5a5d);
		background: -moz-linear-gradient(right, #f6ad36, #cf5a5d);
		background: linear-gradient(to right, #f6ad36, #cf5a5d);
		border: 0;
		outline: none;  cursor: pointer; -webkit-transition: all .5s;  -moz-transition: all .5s;-ms-transition: all .5s; 
		-o-transition: all .5s;  transition: all .5s;
	}
	.danmubtn:hover{
		box-shadow: 1px 1px 1px rgba(122,122,122,.8) ,1px 1px 1px rgba(255,255,255,.6) inset;
	}
	
	@media (max-width: 1000px) {
		.xinwen .xwblk {margin-right: 0 !important;}
		.danmu{margin-top: 16px;}
		.danmubtn, .danmuipt{position: static;}
		.xiazai{padding-bottom: 18px;}
	}
</style>
</head>
<body>
	<%@ include file="inc/head.jsp"%>
	<div class="sycontent">
		<div class="kaic ">
			<strong>欢迎下载使用tjpcms</strong>
			<p></p>
		</div>
		<div class="xiazai  ">
			<ul class="anleft">
				<li style=""><a target="_blank" class="lsbb lefttip" href="${path}/zatan_detail.dhtml?id=303">下载太慢？</a></li>
				<li style=""><a target="_blank" class="lsbb lefttip" href="javascript:" onclick="layer.msg('请先登录')">想投稿？</a></li>
				<li style=""><a target="_blank" class="lsbb lefttip" href="javascript:" onclick="layer.msg('先手动提交给我吧，后期开放码云')">想提交代码？</a></li>
			</ul>
			<div class="anniu1 icon-xiazai animated pulse" style="display: inline-block;">
				&nbsp;&nbsp;&nbsp;<label>立即下载</label>
			</div>
			<ul class="anright">
				<li><a target="_blank" class="zmyong" href="${path}/guanyu.dhtml">有什么用？</a></li>
				<li><a target="_blank" class="zmyong" href="${path}/huanjing.dhtml">遇到问题？</a></li>
				<li><a target="_blank" class="lsbb" href="${path}/bbgx.dhtml">历史版本</a></li>
			</ul>
			<div class="danmu">
				<input type="text" class="danmuipt" maxlength="18" placeholder="皮一下？"/>
				<button class="danmubtn">发射</button>
			</div>
		</div>
		<div class="tip">
			<p>总下载量：<a href="${path }/tongji.dhtml" style="text-decoration:underline !important" class="xzcnt"> </a></p>
			<p>当前版本： <a href="${path}/bbgx_detail.dhtml?id=${bb.id.id}">${bb.val}</a>（${bb.gx }）</p>
			<div class="bdsharebuttonbox" onselectstart="return false">
				<div class="zancnt" title="累计赞次" ></div>
				<a class='icon-zan btn' style="padding-left:19px !important;padding-right: 17px !important;">
					<label style="font-size:13px !important;cursor:pointer">赞</label>
				</a>
				<div style="width:5px;height: 100%;display:inline-block"></div>
				<a class='btn icon-qian clsjuanz' href="${path }/jz.dhtml" style="padding-left:19px !important;padding-right: 17px !important;text-decoration: none;" title="点击查看详情">
					<label style="font-size:13px !important;cursor:pointer" >捐赠</label>
				</a>
				<div style="width:5px;height: 100%;display:inline-block"></div>
				<a class="icon-iconfontcolor68 btn" data-cmd="more" style="padding-left:11px !important">&nbsp;分享</a>
				<div class="bds_count" data-cmd="count" ></div>
				<div style="width: 42px;height: 16px;display:inline-block;"></div>
			</div>
		</div>
		<div class="xinwen" >
			<div class='xwblk'>
				<div class="xwtitle">站内头条</div><ul>
					<c:forEach begin="0" end="3"  step="1" varStatus="s">
						<li title="${zntt[s.index].title}">
							<span class='xwneirong'><a href="${zntt[s.index].url}" target="_blank">${zntt[s.index].title}</a></span>
							<span class='xwriqi'>${fn:substring(zntt[s.index].gx,0,10)}</span>
						</li>
					</c:forEach>
				</ul>
			</div><div class='xwblk'>
				<div class="xwtitle">热门文章</div><ul>
					<c:forEach begin="0" end="3"  step="1" varStatus="s">
						<li title="${rmwz[s.index].title}">
							<span class='xwneirong' style="width:230px"><a href="${rmwz[s.index].url}" target="_blank">${rmwz[s.index].title}</a></span>
							<span class='xwriqi'>${rmwz[s.index].cs}次</span>
						</li>
					</c:forEach>
				</ul>
			</div><div class='xwblk xwblkmr'>
				<div class="xwtitle">致谢名单（${fn:length(zxmd)}人）</div><div class="clszhixie"><ul>
					<c:forEach items="${zxmd}" var="t" varStatus="s">
						<li title="${s.index+1 }、${t.jzr}（捐赠${t.je}元）">
							<span class='xwneirong' style="width:205px"><a href="${path}/jz.dhtml" target="_blank">${s.index+1 }、${t.jzr}（捐赠${t.je}元）</a></span>
							<span class='xwriqi'>${t.jzrq}</span>
						</li>
					</c:forEach>
				</ul></div>
			</div>
		</div>
		<div class="yqlj">
			<span class='ljtt'>友情链接</span><div class="ljulout"><ul class="ljnr">
				<c:forEach items="${ljs}" var="t" varStatus="sta">
					<li ><a href="${t.wz}" onclick="fn_yqcs(${t.id})" target="_blank">${t.mc}</a></li>
				</c:forEach>
			</ul></div>
		</div>
	</div>
	<%@ include file="inc/btm.jsp"%>


	<!-- 飘窗代码 -->
	<div id="float_icon" class="touming88" title="${pc.title }" style="overflow: hidden;border-radius: 6px;position: absolute; z-index: 99999; top:164px; left: 484px; visibility: visible;">
		<span class="icon-shanchu3 clsclspc" style="position: absolute;right:7px;top:3px;color:rgb(119,119,119);cursor: pointer;"></span>
		<a href="${path}/spjc.dhtml" target="_blank">
		<img src="${pc.tu }"></a>
	</div> 
	<script type="text/javascript">
		var dirX = 1, dirY = 1, pctmrid;
		var posX = 0, posY = 0;
		document.getElementById("float_icon").style.top = 0;
		document.getElementById("float_icon").style.left = 0;
		float_icon.style.visibility = "visible";
		pctmrid = window.setInterval("moveIcon()", 50);
		function moveIcon() {
			if (!$("#float_icon").is(":visible")) {clearInterval(pctmrid);pctmrid=null;return;}
			posX += (1 * dirX);
			posY += (1 * dirY);
			$("#float_icon").css("top", posY);
			$("#float_icon").css("left", posX);
			if (posX < 1 || posX + document.getElementById("float_icon").offsetWidth > $(window).width()) {
				dirX = -dirX;
			}
			if (posY < 1|| posY+ document.getElementById("float_icon").offsetHeight+ 5 > $(window).height()) {
				dirY = -dirY;
			}
		}
		
		$(function(){
			//点击发射弹幕
			$(".danmubtn").click(function(){
				var wenzi = $(".danmuipt").val()
				if (_epp(wenzi)) {layer.msg('别皮！',function(){});return;}
				$(".danmuipt").val('')
				$.ajax({
					type:"POST",
					url:"${path}/danmu.dhtml",
					data:{wenzi:wenzi},
				});
				var img=null;
				if (wenzi[0]=='[' && wenzi.indexOf("]")!=-1){
					img = wenzi.substring(1,wenzi.indexOf("]"))
					wenzi = wenzi.substring(wenzi.indexOf("]")+1,wenzi.length)
				}
				var barrager={'info':wenzi,close:false,img:img};
				$('body').barrager(barrager);
			})
		
			$("#float_icon").mouseover(function (){  
            	clearInterval(pctmrid);pctmrid=null;
	        }).mouseout(function (){
	            pctmrid = window.setInterval("moveIcon()", 50);
	        });
	        
	        $(".clsclspc").mouseover(function (){  
            	$(this).css("color", "rgb(47,141,237)")
            	closePc();
	        }).mouseout(function (){  
	            //$(this).css("color", "rgb(119,119,119)")
	        }).click(function(){
	        	closePc();
	        });
	        
	        //从库里查出弹幕
			$.getJSON('${path}/aj_get_alldanmu.dhtml', function(data) {
				var items = data;
				var run_once = true;//是否首次执行
				var index = 0;//弹幕索引
				barrager();//先执行一次
				function barrager() {
					if (run_once) {//如果是首次执行,则设置一个定时器,并且把首次执行置为false
						looper = setInterval(barrager, 3000);
						run_once = false;
					}
					$('body').barrager(items[index]);//发布一个弹幕
					index++;//索引自增
					if (index ==  data.length) {//所有弹幕发布完毕，清除计时器。
						clearInterval(looper);
						return false;
					}
				}
			});
		})

		function closePc() {
			clearInterval(pctmrid);
			pctmrid = null;
			$("#float_icon").slideUp('normal', function() {
				clearInterval(pctmrid);
				pctmrid = null;
				$(this).remove();
			})
		}

		function fn_yqcs(id) {
			$.ajax({
				type : "POST",
				url : "${path}/yqcs.dhtml",
				data : {
					id : id
				},
				datatype : "text"
			});
		}
	</script>


	<script src="${path}/layer/layer.js"></script>
	<script src="${path}/js/jquery.barrager.js"></script><!-- 弹幕 -->
	<script>
		//IE丢失refer的问题，这是什么鬼
		function gotoUrl(url) {
			if (document.all) {//ie
				var gotoLink = document.createElement('a');
				gotoLink.setAttribute('target','_blank')
				gotoLink.href = url;
				document.body.appendChild(gotoLink);
				gotoLink.click();
			} else{
				window.open(url,"_blank")
			}
		}
		
		function plus1(e){
		    var $i=$("<b>").text("+1");
		    var x=e.pageX,y=e.pageY;
		    $i.css({top:y-20,left:x,position:"absolute",color:"#f00", "font-size":"36px","z-index":"999999"}).attr("onselestart","return false");
		    $("body").append($i);
		    $i.animate({top:y-80,opacity:0,"font-size":"0.99em"},2000,function(){
		        $i.remove();
		    });
		    e.stopPropagation();
		}
		
		$(function() {
			setTimeout(function(){jQuery(".xinwen").slide({mainCell:".clszhixie ul",autoPlay:true,effect:"topMarquee",vis:4,interTime:50,trigger:"click"})},2888)
		
			$(".anniu1").click(function(e) {
				clearInterval(pctmrid);pctmrid=null;
				layer.confirm('源码压缩包大小为20M，确定要下载吗？<br/><small>注：服务器带宽低，下载速度较慢，建议加群下载</small><br/><small>另：QQ群文件中有教学视频下载</small>', {icon: 3, title:'提示'}, function(index){
					closePc();
					layer.closeAll();
					$(".xzcnt").text(parseInt($(".xzcnt").text()) + 1)
					gotoUrl('${path}/syxiazai.dhtml?v=${bb.val}')
					plus1(e);
				},function(){
					closePc();
				});
			})
			$(".icon-zan").click(function() {
				layer.closeAll();
				$.ajax({
					type:"POST",
					url:'${path}/syzan.dhtml',
					datatype: "text",
					success:function(ret){
						if ("0" ==ret) {$(".zancnt").text(parseInt($(".zancnt").text()) + 1);layer.msg("谢谢鼓励~")}
						else layer.msg("赞过啦~")
					}
				});
			})

			//为实时配置内容几个字加上说明
			var kaic = '${META_DES}'
			var sscnt = "独有的实时配置增删改查";
			var ssidx = kaic.indexOf(sscnt)
			if (ssidx!=-1){
				kaic = kaic.substring(0,ssidx)+"<a target='_blank'  class='sspznr' href='${path }/jiandan_detail.dhtml?id=159'>"+sscnt+"</a>"
					+ kaic.substring(ssidx+sscnt.length,kaic.length)
				$(".kaic p").html(kaic)
			}

			$('.sspznr').on('mouseover', function(){
				var that = this;
			  	layer.tips('这是什么意思？点击看看吧', that,{tips:[1, '#3595CC']});
			});
			
			$(".icon-iconfontcolor68").click(function(){layer.closeAll();})
		})

		window._bd_share_config = {common:{},share: {}};
		with (document)0[(getElementsByTagName('head')[0] || body).appendChild(createElement('script')).src = 'http://bdimg.share.baidu.com/static/api/js/share.js?v=89860593.js?cdnversion='+ ~(-new Date() / 36e5)];
	</script>
</body>
</html>

