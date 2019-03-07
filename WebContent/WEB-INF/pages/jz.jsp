<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	request.setAttribute("path", request.getContextPath());
%>

<!doctype html>
<html>
<head>
<title>tjpcms - 捐赠</title>
<%@ include file="inc/meta.jsp"%>

<style>
	.sycontent{text-align: center;}
	.aliqr{padding:20px 0 30px;display:inline-block;width: 40%;}
	.wxqr{padding:20px 0 30px;display:inline-block;width: 40%;}
	.juanz{text-shadow: silver 1px 1px 5px !important;font-size: 19px;letter-spacing: 3px;;padding:10px;font-family: '幼圆';}
	.sdowali{ animation: change_ali 5s  ease-in  infinite }
	@keyframes change_ali {
		0%{ text-shadow: 2px 2px 4px #0ae}
		50%{ text-shadow:2px 2px 40px #0ae}
		100%{ text-shadow:2px 2px 4px #0ae}
	}
	.sdowwx{ animation: change_wx 5s  ease-in  infinite }
	@keyframes change_wx {
		0%{ text-shadow: 2px 2px 4px #32CD32}
		50%{ text-shadow:2px 2px 40px #32CD32}
		100%{ text-shadow:2px 2px 4px #32CD32}
	}
	.yaohe{width: 90%;margin: 0 auto;background: #fcf8e3;margin-top: 10px;-webkit-border-radius: 6px;
	-moz-border-radius: 6px;color:#297;text-align:justify;padding: 15px;
	border-radius: 6px;font-size: 15px;text-indent: 2em;line-height: 150%;    border: solid 1px #DFDFDF;}
	.yaohe span{float: right;}
	.jzlist{ border-collapse: collapse; border-spacing: 0;margin: 0 auto;width: 101%;}
	.jzlist thead{background-color: #F08080;}
	.jzlist th{    font-size: 15px; padding: 12px;color: #fff;font-weight: bold;}
	.jzlist td{    font-size: 14px; padding: 8px;color:#CD5C5C}
	#feny{padding-bottom: 10px;padding-top: 12px;}
	.fankui{width: 150px;display: inline-block; position: relative;top:-150px;}
	.fankui button{color: #fff;background-color: #428bca;border-color: #357ebd;
	padding: 10px 16px;
    font-size: 18px;
    line-height: 1.33;    display: inline-block;font-weight: 400;    background-image: linear-gradient(to bottom,#428bca 0,#2d6ca2 100%);
    border: 1px solid transparent;cursor: pointer;
    border-radius: 10px;}
    .fankui a{font-size: 12px !important;padding: 0 6px;border-bottom:1px solid #428bca;letter-spacing: 1px;color:#3c763d;}
    .fankui a:hover{color: #0000CD ;}
    .layui-layer-input{padding: 5px !important;}
    .jzlist td{border: 1px solid #DFDFDF}
    .zfy{font-size: 18px;margin-bottom: 8px;color:#F08080;font-weight: bold;}
    #id_jztx textarea{font-family:'Microsoft YaHei','sans-serif'}

    @media (max-width: 1000px) {
		.aliqr{width: 100%;}
		.wxqr{width: 100%;display: block;}
		.fankui{display: block;top:auto;margin: 0 auto;}
		.jzlist {table-layout:fixed;}
		.jzlist td{word-wrap:break-word !important;padding:8px;white-space:nowrap;text-overflow:ellipsis;overflow:hidden; }
	}
</style>
</head>
<body>
	<%@ include file="inc/head.jsp"%>
	<div class="sycontent">
		<div class="bread" style="text-align: left">
			<ul>
				<li><a class="icon-shouye brdsy" href="${path }/"> 首页</a></li>
				<li><a href="#">捐赠</a></li>
			</ul>
		</div>
		<div class="yaohe">
			感谢您关注tjpcms！tjpcms是作者在实际使用各类cms过程中不断提炼升华出来、面向java web开发者的一套快速建站解决方案，希望能为您的相关工作学习带来便利和帮助！如您欣赏tjpcms，欢迎给作者捐赠。我们将定期更新捐赠列表<c:if test="${recTotal>0 }">(当前共${recTotal}笔)</c:if>，感谢您的支持！
			<div style="text-align:right">2016-11-07 &nbsp;by blue</div>
		</div>
		<div>
			<div class="aliqr">
				<img  src="${path}/images/tjpcms/aliqr.png" alt="支付宝捐赠二维码" />
				<div class="juanz sdowali" style="color:#0000CD">支付宝捐赠二维码</div>
			</div>
			<div class="fankui">
				<label style="color:#F08080;font-weight:bold;font-size: 15px;">已捐赠？</label><a href="javascript:">提醒作者</a>
			</div>
			<div class="wxqr">
				<img src="${path}/images/tjpcms/wxqr.jpg" alt=""  alt="微信捐赠二维码" />
				<div class="juanz sdowwx" style="margin-top:10px;color:#228B22">微信捐赠二维码</div>
			</div>
		</div>
		<div class='zfy'>${zfy}</div>
		<div style="width: 96%;margin:0 auto;padding: 0px;overflow: hidden;border-radius: 6px; border: 1px solid #DFDFDF">
			<table class="jzlist">
				<thead>
					<tr>
						<th>捐赠人</th>
						<th>金额</th>
						<th>日期</th>
						<th>来源</th>
						<th>备注</th>
					</tr>
				</thead>
				<tbody>
					<c:if test="${fn:length(recs)<=0}">
						<tr>
							<td colspan="5">暂无捐赠记录！</td>
						</tr>
					</c:if>
					<c:if test="${fn:length(recs)>0}">
						<c:forEach items="${recs}" var="itm" varStatus="sta">
							<tr <c:if test="${sta.index>=perPage}">style="display:none"</c:if>>
								<td title="${itm.jzr}">${itm.jzr}</td>
								<td title="${itm.je}">￥${itm.je}</td>
								<td title="${itm.jzrq}">${itm.jzrq}</td>
								<td title="${itm.ly}">${itm.ly}</td>
								<td title="${itm.bz}">${itm.bz}</td>
							</tr>
						</c:forEach>
					</c:if>
				</tbody>
			</table>
		</div>
		<div id="feny"></div>
	</div>
	<div style="clear:both;"></div>
	
	<c:set var="needcy" value="1"/>
	<%@ include file="inc/btm.jsp"%>

	<script src="${path}/laypage/laypage.js"></script>
	<script>
		laypage({
			cont : 'feny',
			skip : true, //是否开启跳页
			groups : 5,//连续显示分页数
			pages : ${pgTotal},
			jump : function(e, first) {
				if (!first) { //一定要加此判断，否则初始时会无限刷新
					$(".jzlist>tbody>tr").hide()
					var pp = parseInt('${perPage}')
					if (e.curr == 1) {
						$(".jzlist>tbody>tr:lt(" + pp + ")").show()
					} else {
						var st = (e.curr - 1) * pp - 1, ed = st + pp;
						$(".jzlist>tbody>tr:gt(" + st + ")").show()
						$(".jzlist>tbody>tr:gt(" + ed + ")").hide()
					}
				}
			}
		})
		
		$(function(){
			<c:if test="${fn:length(recs)>0}">
				if ($(window).width()>1000){
					$(".jzlist>tbody>tr").each(function(){
						var etd = $(this).children("td:eq(2)");
						etd.text(etd.attr('title'))
					})
				}else{
					$(".jzlist>tbody>tr").each(function(){
						var etd = $(this).children("td:eq(2)");
						var stt = etd.attr('title');
						etd.text(stt.substring(2, stt.length).replace(/-/g,"."))
					})
				}
			</c:if>
		
			$(".fankui a").click(function() {
				layer.prompt({title : '捐赠提醒',ph: '随便写点啥',formType : 2,id:'id_jztx'},
					function(pass) {
						layer.confirm('确定要提醒作者吗？（请勿恶意提交）', {icon: 3, title:'提示', btn:['确定','取消','返回'],
							yes:function(index){
								layer.confirm('请再次确认是否要提交？', {icon: 0, title:'确认', btn:['确定了','算了吧','返回'],
									yes:function(index){
										fn_ntp_aj('${path}/jzfk.dhtml',{nr:pass}, function(){layer.msg('感谢您的反馈！')});
									},
									cancel:function(idx){layer.close(idx)},
									btn3:function(){$(".fankui a").trigger('click');$("#id_jztx").val(pass)}
								});
							},
							cancel:function(idx){layer.close(idx)},
							btn3:function(){$(".fankui a").trigger('click');$("#id_jztx").val(pass)}
						});
					}
				)
			})
			
			
		})
	</script>
</body>
</html>

