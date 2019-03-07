<!--
 * 作者:tjp
 * QQ号：57454144
 * QQ群：168895774（有教学视频下载）
 * 官网：http://www.tjpcms.com
 * 码云：https://git.oschina.net/tjpcms/tjpcms
 * 更新日期：2017-01-22
 * tjpcms - 最懂你的cms
 * 2018-06-28：把类似栏目管理这样的页面也做成可配置的，但我仍然会保留栏目管理的代码。因为栏目管理其实也有它强的地方，整个页面都是局部刷新出来的，引入前端模板的写法也是非常的方便(IDE对这种写法的支持略差)。
 * -->

<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%request.setAttribute("path", request.getContextPath());%>

<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="X-UA-Compatible" content="IE=EDGE,chrome=1"/>
	<link rel="stylesheet" href="${path }/css/ht_cmn.css?___v=<%=Math.random()%>" type="text/css"/>
    <script src="${path }/js/jquery.js" type="text/javascript"></script>
    <c:if test="${o.r.tree.tb!='' || o.r.inc_ztree}">
    	<c:set var="___list_js_ztree" value="1"/>
		<link rel="stylesheet" href="${path }/css/zTreeStyle/zTreeStyle.css" type="text/css"/>
    	<script type="text/javascript" src="${path }/js/jquery.ztree.core.min.js"></script>
		<script type="text/javascript" src="${path }/js/jquery.ztree.excheck.js"></script>
	</c:if>
    <script src="${path }/js/cmn.js?___v=<%=Math.random()%>" type="text/javascript"></script>
    <script src="${path}/js/template.js"></script>
    <script type="text/javascript" src="${path}/My97DatePicker/WdatePicker.js"></script>
    
	<c:forEach items="${o.r.cxs}" var="t" varStatus="status">
		<c:if test="${empty ___list_js_tjp_ipttree && t.lx=='tree'}">
			<c:set var="___list_js_tjp_ipttree" value="1"/>
			<script type="text/javascript" src="${path}/js/jquery.tjpinputtree.js?___v=<%=Math.random()%>"></script>
			<c:if test="${empty(___list_js_ztree)}">
				<link rel="stylesheet" href="${path }/css/zTreeStyle/zTreeStyle.css" type="text/css"/>
	    		<script type="text/javascript" src="${path }/js/jquery.ztree.core.min.js"></script>
	    	</c:if>
		</c:if>
	</c:forEach>
    
    
	<c:forEach items="${o.r.extjs}" var="t" varStatus="status">
		<script type="text/javascript" src="${path}/js/extjs/${t}"></script>
	</c:forEach>
	<c:forEach items="${o.r.extjsp}" var="t" varStatus="status">
		<jsp:include page="../extjsp/${t}"/>
	</c:forEach> 

	<style>
		body{background: #fff; overflow: hidden;font-size: 0;} /* 0是为了防止html元素间的空白符造成布局的计算不精确 */
		/* a{text-decoration:none;color:#056dae;outline:none;-webkit-transition: all 0.5s; -moz-transition: all 0.5s; -o-transition: all 0.5s;} */
		.content{padding: 10px;display: inline-block;vertical-align: top; overflow: auto;}
		a:hover{color:#00a4ac;text-decoration:none;}
		.tdop a{margin-right: 1px;}
		.cxqu{background: #f5f5f5; float:left;border-radius: 4px;padding: 1px 0;}
		.cxqu li{float: left;margin:1px 5px 1px 0;line-height: 40px;vertical-align: middle;font-weight: bold; padding:0 6px; border: 2px solid transparent;}
		.cxqu li div{display:inline-block;text-align: center; }
		.Wdate{height: 28px !important;}
		.input-text{width: 186px;}
		.cxqu .sel_zdb{width: 188px;}
		#popsh input{margin-right: 10px;}
		.cls_sid_popsh_out{max-width:600px}
		.cls_def_tip{pointer-events: none !important;}
		.clstdpic{overflow: hidden;margin:0 auto;display: table;}
		.clstdpic img{display: table-cell;vertical-align: middle;padding:2px 0}
		
		.loading{position: relative;top: 2px;}
		.loading:after {  overflow: hidden;   display: inline-block; vertical-align: bottom;  animation: shengluehao 2s infinite;   content: "\2026"; position: absolute;top: -10px;}
		@keyframes shengluehao {from {width: 0px;}to {width: 11px;}}
		.lefttree{display: inline-block; vertical-align: top;border-right: 1px dotted #ccc;width:${o.r.tree.width}px}
		.lefttree>.title{    text-align: center; color: #183152; height: 25px; line-height: 25px; background: url(${path}/img/lanmushu.png) repeat-x;overflow: hidden; font-size: 12px; position: relative;}
		.lecont{    overflow: auto !important;margin-top: 5px;}
	</style>
	
</head>
<body onkeydown="fn_etr();" >

	<c:if test="${!empty(o.bread)}">
		<div class="bread">${o.bread}</div>
	</c:if><div class="content">
		<form action="${o.r.listloc}" id="id_form_cxq" method="post">
			<input type="hidden" name="___pg" value=""/>
			<c:if test="${o.r.tree.tb!=''}">
				<input type="hidden" name="pId" value="${o.r.tree.TREE_ROOT_ID}"/>
				<input type="hidden" name="pId___op" value="="/>
			</c:if>
			<c:if test="${fn:length(o.r.cxs)>0}">
				<ul class="cxqu">
					<c:forEach items="${o.r.cxs}" var="t" varStatus="status">
						<c:set var="haveopsel" value="0"/>
						<c:if test="${t.lx!='hidden'}">
							<li <c:if test="${!empty(t.hasDef) && !empty(t.val)}">class="clsZdHasDef"</c:if>>
								<div style="white-space:nowrap;width: ${cxq_wzWidth}px;">${t.wz}：</div>
								<c:if test="${t.lx=='input'}">
									<div style="width:188px !important" class="clsselinpt">
									<c:choose>
										<c:when  test="${fn:length(t.ops)>1 || (fn:length(t.ops)==1 && t.ops[0].val!='like')}">
											<c:set var="haveopsel" value="1"/>
											<div>
												<select name="${t.zdm}___op" class="select haveopsel">
													<c:forEach items="${t.ops}" var="op" varStatus="status">
														<option value="${op.val}" <c:if test="${t.op==op.val or t.op==op.text}">selected</c:if>>${op.text}</option>
													</c:forEach>
												</select>
											</div>
										</c:when>
										<c:otherwise></c:otherwise>
									</c:choose>
									<input type="text" maxlength="30" class="input-text lh25" name="${t.zdm}" value="${t.val}" style="<c:if test='${haveopsel==1}'>width:100px;</c:if>" />
									</div>
								</c:if>
								<c:if test="${t.lx=='sj'}">
									<input type="text" id="id_sj_${t.zdm}_${status.index}"  onclick="WdatePicker()" 
										class="Wdate ${t.cls} input-text lh25" name="${t.zdm}___${t.cls}" value="${t.val}"/>
								</c:if>
								<c:if test="${t.lx=='sel'}">
									<select name="${t.zdm}" class="select sel_zdb" <c:if test="${lx==2}">disabled readonly</c:if>  onchange="shuaxin()">
										<option value="___-1">全部</option>
										<c:forEach items="${t.selops}" var="itmsel">
											<option value="${itmsel.val}" <c:if test="${t.val==fn:trim(itmsel.val)}">selected</c:if> >${itmsel.txt}</option><!-- fn:trim是强转String -->
										</c:forEach>
									</select>
								</c:if>
								<c:if test="${t.lx=='tree'}">
									<input type="hidden" name="${t.zdm}" value="${t.val}"/>
									<input type="text"   placeholder="请点击选择树节点" name="___cxq_fp_tree_${t.zdm}" class="input-text clstreerdo donot_initphd" style="text-indent: 5px;" readonly />
									<script>$(".clstreerdo[name='___cxq_fp_tree_${t.zdm}']").tjpinputtree($.parseJSON('${t.treelist}'), '${t.val}', function(){shuaxin()})</script>
								</c:if>
							</li>
						</c:if>
					</c:forEach>
					<li style="padding-left: 0;"><input type="button" class="btn btn_cmn  btn82 btn_search" value="查询" onclick="shuaxin()"/></li>
				</ul>
			</c:if>
		</form>

		<div style="clear:both"></div>
		<c:if test="${fn:length(o.r.ans)>0}"><!-- 按钮区 -->
			<div class="btns">
				<c:forEach items="${o.r.ans}" var="t" varStatus="status">
					<input type="button" id="${t.id}" class="btn btn82 ${t.clazz}" style="${t.style}" value="${t.cztext}" onclick="${t.fn}"/>
				</c:forEach>
			</div>
		</c:if>
		
		<table class="list_table" <c:if test="${fn:length(o.r.ans)<=0}">style="margin-top:10px"</c:if>>
			<thead>
				<tr>
					<c:forEach items="${o.r.ths}" var="itm" varStatus="status">
						<c:if test="${itm.display!='none'}">
							<th <c:if test="${!itm.notip}"> title = '${ itm.title}'</c:if> 
								  <c:if test="${!empty(itm.style)}">style='${itm.style}'</c:if>
								  <c:if test="${itm.notip}"> onselectstart="return false"</c:if>
								  <c:if test="${itm.zdm=='uid' || itm.zdm=='shzt'}">zdm="${itm.zdm}"</c:if>>${ itm.title}</th>
						</c:if>
					</c:forEach>
				</tr>
			</thead>
			<tbody  id="id_list_tbody0">
				<c:if test="${!o.r.jubu}"><!-- 非局部刷新 -->
					<c:forEach items="${o.r.tbs}" var="itm" varStatus="status">
						<tr <c:if test="${status.index%2==1}">style='background:rgb(255, 252, 234)'</c:if>>
							<c:if test="${o.d.batdel}">
								<td class="cls_bd_chk" style="padding: 10px 0;" onselectstart="return false"><input name="id[]" type="checkbox" value="" id='${itm.id}'/></td>
							</c:if>
							<td title="${status.index+1+(o.r.curPage-1)*o.r.perPage }">${status.index+1+(o.r.curPage-1)*o.r.perPage}</td>
							<c:forEach items="${o.r.zds}" var="zd">
								<c:if test="${zd.display!='none'}">
									<c:choose>
										<c:when test="${!empty(zd.extc) && zd.extdbzd}">
											<c:if test="${!empty(itm[zd.extc]) }">
												<c:if test="${empty(zd.extp) }"><c:set var="extwz" value="${zd.exts}${itm[zd.extc]}${zd.exte}"/></c:if>
												<c:if test="${!empty(zd.extp) }"><c:set var="extwz" value="${zd.exts}${itm[zd.extc]+zd.extp}${zd.exte}"/></c:if>
											</c:if>
											<c:if test="${empty(itm[zd.extc]) }"><c:set var="extwz" value="${zd.exts}${itm[zd.extc]}${zd.exte}"/></c:if>
											<c:if test="${!empty(zd.extsub) }"><c:set var="extwz" value="${zd.exts}${fn:substring(extwz,0,zd.extsub)}${zd.exte}"/></c:if>
										</c:when>
										<c:when test="${!empty(zd.extc)}">
											<c:set var="extwz" value="${zd.exts}${zd.extc}${zd.exte}"/>
											<c:if test="${!empty(zd.extsub) }"><c:set var="extwz" value="${zd.exts}${fn:substring(zd.extc,0,zd.extsub)}${zd.exte}"/></c:if>
										</c:when>
										<c:otherwise><c:set var="extwz" value=""/></c:otherwise>
									</c:choose>
	
									<c:choose>
										<c:when test="${zd.zdType=='normal'}">
											<c:if test="${!empty(itm[zd.zdm])}"><c:set var="celltext" value="${itm[zd.zdm]}"/></c:if>
											<c:if test="${empty(itm[zd.zdm])}"><c:set var="celltext" value="${zd.empcoltip}"/></c:if>
											<c:if test="${!empty(extwz)}">
												<c:if test="${zd.exttail}"><c:set var="celltext" value="${celltext}${extwz}"/></c:if>
												<c:if test="${!zd.exttail}"><c:set var="celltext" value="${extwz}${celltext}"/></c:if>
											</c:if>
											<c:if test="${zd.zdm=='rq' || 'gx'==zd.zdm}"><c:set var="celltext" value="${fn:substring(celltext,0,10)}"/></c:if>
											<c:choose>
												<c:when test="${empty(zd.href)}">
													<c:set  var="___title" value="${celltext}"/>
													<c:set  var="___title_zdm" value="___title_${zd.zdm}"/>
													<c:if test="${!empty(itm[___title_zdm])}">
														<c:set  var="___title" value="${celltext}：【${itm[___title_zdm] }】"/>
													</c:if>
													<td title="${___title}">${celltext}</td>
												</c:when>
												<c:otherwise><!-- 字段是可以点击的 -->
													<c:set var="recHrefPara" value=""/>
													<c:forEach items="${zd.hrefmp}" var="thf">
														<c:choose>
															<c:when test="${!empty(thf.zdm) && empty(thf.tplzdm) && !empty(itm[thf.zdm])}">
																<c:set var="recHrefPara" value="${recHrefPara}&${thf.zdm}=${itm[thf.zdm]}"/>
															</c:when>
															<c:when test="${!empty(thf.zdm) && !empty(thf.tplzdm) && !empty(itm[thf.tplzdm])}">
																<c:set var="recHrefPara" value="${recHrefPara}&${thf.zdm}=${itm[thf.tplzdm]}"/>
															</c:when>
															<c:otherwise>
																<c:set var="recHrefPara" value="${recHrefPara}&${thf.zdm}=${thf.zdz}"/>
															</c:otherwise>
														</c:choose>
													</c:forEach>
													<c:set var="recHrefPara" value="${empty(recHrefPara)?'':'?'.concat(fn:substring(recHrefPara,1,fn:length(recHrefPara)))}"/>
													<td title="<c:out value='${celltext}'/>">
														<a href="${zd.href}${recHrefPara}" <c:if test="${ zd.tgblank}">target="_blank"</c:if> 
															style="  border-bottom: 1px solid blue;padding: 0 5px;">${celltext}</a>
													</td>
												</c:otherwise>
											</c:choose>
										</c:when>
										<c:when test="${zd.zdType=='pic'}">
											<c:if test="${!empty(itm[zd.zdm])}">
												<td style="padding:0;">
													<c:if test="${!empty(extwz)}">
														<c:if test="${!zd.exttail}">
															${extwz}<div class="clstdpic"><img style="max-height:50px;" src="${itm[zd.zdm]}" /></div>
														</c:if>
														<c:if test="${zd.exttail}">
															<div class="clstdpic"><img style="max-height:50px;" src="${itm[zd.zdm]}" /></div>${extwz}
														</c:if>
													</c:if>
													<c:if test="${empty(extwz)}">
														<div class="clstdpic"><img style="max-height:50px;" src="${itm[zd.zdm]}" /></div>
													</c:if>
												</td>
											</c:if>
											<c:if test="${empty(itm[zd.zdm])}">
												<c:set var="celltext" value="${zd.empcoltip}"/>
												<c:if test="${!empty(extwz)}">
													<c:if test="${zd.exttail}"><c:set var="celltext" value="${celltext}${extwz}"/></c:if>
													<c:if test="${!zd.exttail}"><c:set var="celltext" value="${extwz}${celltext}"/></c:if>
												</c:if>
												<td title="${celltext}">${celltext}</td>
											</c:if>
										</c:when>
										<c:when test="${zd.zdType=='text'}">
											<c:choose>
												<c:when test="${!empty(zd.textval)}">
													<c:set var="celltext" value="${zd.textval}"/>
													<c:if test="${!empty(extwz)}">
														<c:if test="${zd.exttail}"><c:set var="celltext" value="${celltext}${extwz}"/></c:if>
														<c:if test="${!zd.exttail}"><c:set var="celltext" value="${extwz}${celltext}"/></c:if>
													</c:if>
													<td title="${celltext}">${celltext}</td>
												</c:when>
												<c:when test="${!empty(zd.textmap) && !empty(zd.textmpzd)}">
													<c:choose>
														<c:when test="${!empty(zd.textmap[fn:trim(itm[zd.textmpzd])])}">
															<c:set var="celltext" value="${zd.textmap[fn:trim(itm[zd.textmpzd])]}"/>
														</c:when>
		<%-- 										<c:when test="${!empty(zd.textmap[itm[zd.textmpzd]+0])}">
															<c:set var="celltext" value="${zd.textmap[itm[zd.textmpzd]+0]}"/>
														</c:when> --%>
														<c:otherwise>
															<c:set var="celltext" value="${zd.empcoltip}"/>
														</c:otherwise>
													</c:choose>
													<c:if test="${!empty(extwz)}">
														<c:if test="${zd.exttail}"><c:set var="celltext" value="${celltext}${extwz}"/></c:if>
														<c:if test="${!zd.exttail}"><c:set var="celltext" value="${extwz}${celltext}"/></c:if>
													</c:if>
													<td title="${celltext}">${celltext}</td>
												</c:when>
												<c:otherwise>
													<td></td>
												</c:otherwise>
											</c:choose>
										</c:when>
										<c:otherwise>
											<td></td>
										</c:otherwise>
									</c:choose>
								</c:if>
							</c:forEach>					
	
							<c:if test="${fn:length(itm.lstczq)>0}">
								<td class="tdop" style="white-space:normal">
									<c:forEach items="${itm.lstczq}" var="t">
										${t.czx}
									</c:forEach>
								</td>
							</c:if>
						</tr>
					</c:forEach>
				</c:if>
				<c:if test="${o.r.jubu}"><!-- 局部刷新 -->
					<tr style="height: 36px;">
						<td colspan="${fn:length(o.r.ths) }"  >
							<a class="lmlxsx" href="javascript:" onclick="shuaxin()" style="display: none;margin-right:5px;position: relative;top: 2px;">
								<span>刷新</span>
							</a>
							<img src="${path }/img/loading.gif"  style="  vertical-align: middle;"/>
							<span style="line-height:32px" class="loading">获取数据中，请稍后</span>
						</td>
					</tr>
				</c:if>
			</tbody>
		</table>
		<div id="pagin">
			<span class="fymsg"></span>
			<div id="fenye"></div>
		</div>
		</div>

	<script src="${path}/layer/layer.js"></script>
	<script src="${path}/laypage/laypage.js"></script>
    <script type="text/javascript">
    	//按回车键就模拟点击操作区的查询按钮
		function fn_etr(){
	         var e = window.event || arguments.callee.caller.arguments[0];
	         if (e && e.keyCode == 13 && $(".cxqu>li:last>input").length>0) {
	             $(".cxqu>li:last>input").trigger('click')
	         }
		}
    
		function layout(){
			if ($(window).height()>0){
				var h = $(window).height()-($(".bread").length>0?45:20);
				$(".content").height(h)//上下padding共20
				if ('${o.r.tree.tb}'!==''){
					$(".content").width($(window).width()-20-$(".lefttree").width()-1)//1是lefttree的右边框
					$(".lefttree").height(h+20)
					var maxh = $(window).height()-86+('${o.r.tree.tip}'!=''?0:25)
   					$(".lecont").css('max-height', maxh)//一个bread25,菜单树25，提示25，提示间距(margin-top)3,顶部间距5(.lecont的mt)，提示底部留3
   					if (!$.support.leadingWhitespace)$(".lecont").height(maxh)//IE6-8，max-height有点问题
				}
				
				if (!_epp('${o.r.js_aft_layout}')) eval('${o.r.js_aft_layout}');
			}
		}
		window.onresize=function(){
			layout();layout()
		}

		;<c:if test="${o.d.batdel}">
			$(function(){
				//表体部分点击checkbox外围的td也视为响应checkbox
				$("input[name='id[]']").parent().click( function(e){
					$(this).children("input").trigger('click')
					e.stopPropagation()
				})
				$("input[name='id[]']").click( function(e){
					e.stopPropagation()
				})
				
				$(".list_table th:eq(0)").click( function(e){
					$(this).children("input").trigger('click')
					e.stopPropagation()
				})
				$(".list_table th:eq(0)").children("input").click( function(e){
					if ($("input[name='id[]']:checked").size()==0 && !$(this).prop("checked")){
						$(this).prop("checked",false)//这个是针对表体checkbox全未选，表头checkbox勾选，此时点击表头checkbox的情况（就是只有表头勾了，下面的都没勾）
					}else{
						$("input[name='id[]']").each(function(){
							$(this).prop("checked",!$(this).prop("checked"))
						})
					}
					e.stopPropagation()
				})
			})

			function ___fn_batdel(){
				var ids="";
				var cnt = 0, egray=null;
				$("input[name='id[]']").each(function(){
					if ($(this).prop("checked")){
						ids += (this.id+",");
						cnt++;
						$(this).parent().parent().children(":last").children("a").each(function(){
							if (egray==null && $(this).text()=="删除" && $(this).hasClass('czxgray')){
								egray = $(this)//第一个有问题的
							}
						})
					}
				})
				if (cnt == 0){
					layer.msg("请先选择要删除的记录！");
				}else if (egray!=null){
					egray.trigger('click')
				}else{
					layer.confirm('确定要删除勾选的'+cnt+'条记录吗？', {icon: 3, title:'提示'}, function(index){
						if (!_epp('${o.d.zdyQrAft}') && !eval('${o.d.zdyQrAft}')) return;
						___del(ids)
					});
				}
			}
		</c:if>

		;<c:if test="${o.d.batdel || o.d.need}">
			function ___del(id){
				$.ajax({
					type:"POST",
					url:"delete.dhtml",
					data:{ids:id,u:'${o.u}'},
					beforeSend:function(){layer.msg('删除中，请稍后......', {time:-1,icon: 16,shade: 0.3, scrollbar:false});},  
					datatype: "text",
					success:function(ret){
						layer.closeAll();
						if ("0" ==ret){
							if ('${o.r.jubu}'=='true'){layer.msg('删除成功！');}
							if ('${o.r.tree.tb}'!=''){refreshTree();}
							shuaxin();
						}else if ("-1"==ret){
							layer.alert('删除失败，请联系管理员！', { icon:2, title:'错误'});
						}else{
							layer.alert(ret, { icon:2, title:'错误'});
						}
					},
					error: function(){
						layer.alert('删除失败，请刷新后重试！', { icon:2, title:'错误'});
					}
				});
			}
		</c:if>

		;<c:if test="${o.d.need}">
			function ___fn_del(tag,ids){
				var eqidx = "${o.d.batdel}"=="true"?1:0;
 				var xh = $(tag).parents("tr").children(":eq("+eqidx+")").text();
				layer.confirm('确定要删除序号为 <strong>'+xh+' </strong>的记录吗？', {icon: 3, title:'提示'}, function(index){
					if (!_epp('${o.d.zdyQrAft}') && !eval('${o.d.zdyQrAft}')) return;
					___del(ids);
				});
			}
		</c:if>

		var g_curPage=1;
		function getcurPage() {return '${o.r.jubu}'!='true'? '${o.r.curPage}':g_curPage;}
		function getxhidx() {return "${o.d.batdel}"=="true"?1:0;}
		function getu() {return "${o.u}";}
		function getuid() {return "${___uid}";}
		function getzscg() {return "${___zscg}";}

		//这个页面里所有需要刷新页面的地方，无论是全局还是局部，都由这个函数来执行
		function shuaxin(curPage){
			$("input[name='___pg']").val(curPage || getcurPage());
			
			//inputtree的处理
			$("#id_form_cxq .clstreerdo").each(function(){//clstreerdo的值要另外取
				if ($(this).prev().length>0){
					var iptreeval = $(this).data("___id")
					if (iptreeval==undefined ||iptreeval=="" || iptreeval==null){
						$(this).prev().attr("disabled", "disabled")
					}else{
						$(this).prev().removeAttr("disabled").val(iptreeval)
					}
				}
			})
			
			
			if ('${o.r.jubu}'!='true') {//整页刷新
				$("#id_form_cxq").submit();
			}else{//局部刷新
				var dataex="&u="+"${o.u}";
				aj("tbs.dhtml",$("#id_form_cxq").serialize()+dataex,{
				beforeSend:function(){$("input:not([type=hidden]),a,button").attr("disabled","disabled");$(".list_table>tbody>tr:eq(0)").show();$(".list_table>tbody>tr").not(":eq(0)").remove();},
				succ:function(rtn){
					//分页详情区
					$(".list_table>tbody>tr:eq(0)").hide();
					$(".fymsg").html('</i>共<i>'+rtn.recTotal+'</i>条记录，每页<i>'+rtn.perPage+'</i>条，共<i>'+rtn.pgTotal+'</i>页')
					//数据区
					var html = template('sid_tabs', rtn);
					$("#id_list_tbody0").append(html)
					//分页响应
					g_curPage = rtn.curPage;
					laypage({ cont: 'fenye', skip: true,groups: 5,pages: rtn.pgTotal, curr: rtn.curPage || 1, jump: function(e, first){  if(!first)shuaxin(e.curr)}})
					//batdel区响应
					$(".cls_bd_chk").on('click',function(e){$(this).children("input").trigger('click');e.stopPropagation()})
					$("input[name='id[]']").on('click',function(e){e.stopPropagation()})
					//执行局部刷新成功后需要额外执行的代码
					if (!_epp('${o.r.js_aft_sx}')) eval('${o.r.js_aft_sx}');
				},fail:function(){
					layer.alert('获取数据失败，请联系管理员！', { icon:2, title:'错误'});
				},error:function(){
					layer.msg("获取数据失败，请点击刷新按钮重试！");
					$(".list_table>tbody>tr>td:eq(0)").children("a").show().parent().children("span").text("获取数据失败，请点击刷新按钮重试").parent().children("img").remove();
				},complete:function(XMLHttpRequest, textStatus){
					$("input,a,button").removeAttr("disabled")
				}})
			}
		}

		$(function(){
			//如果是局部刷新的，需要通过Ajax去后台把数据查出来，再动态添加到列表区tbs
			if ('${o.r.jubu}'=='true') {
				shuaxin();
			}else{//
				if (!_epp('${o.r.js_aft_sx}')) eval('${o.r.js_aft_sx}');//如果是全局刷新，这里也执行一下吧
				$(".fymsg").html('</i>共<i>'+${o.r.recTotal}+'</i>条记录，每页<i>'+${o.r.perPage}+'</i>条，共<i>'+${o.r.pgTotal}+'</i>页')
				laypage({
				    cont: 'fenye',
				    skip: true,
				    groups: 5,
				    pages: ${o.r.pgTotal},
					curr: function(){
				        //var page = location.search.match(/___pg=(\d+)/);
				        //return page ? page[1] : 1;
				          var page = $("input[name='___pg']").val() || '${o.r.curPage}';
				       	return page;
				    }(),
				    jump: function(e, first){
				        if(!first){
				        	shuaxin(e.curr)
				        }
				    }
				})
			}
			
			//按左右键来切换分页
			$(".btns input:eq(0), .cxqu").focus().blur()//不用再点一下就能翻页
			$(document).keydown(function (e) {
				if (e.which==37) $("#fenye .laypage_curr").text()=="1"?layer.msg('已到第一页'):$("#fenye .laypage_prev")[0].click();//按左
				else if (e.which==39) $("#fenye .laypage_curr").next().hasClass('laypage_total')?layer.msg('已到最后一页'):$("#fenye .laypage_next")[0].click();//按右
			})

			;<c:if test="${cxq_haveShijian}">
				$(".kssj").each(function(){
					var  id = $(this).parent().next().children(".jssj").attr("id");
					$(this).attr("onfocus", "WdatePicker({maxDate:'#F{$dp.$D(\\'"+id+"\\')}'})");
				})
				$(".jssj").each(function(){
					var id = $(this).parent().prev().children(".kssj").attr("id");
					$(this).attr("onfocus", "WdatePicker({minDate:'#F{$dp.$D(\\'"+id+"\\')}'})");
				})
			</c:if>

			<c:if test="${!empty(o.bread)}">
				$(".bread>div").fadeOut(500).fadeIn(500).fadeOut(500).fadeIn(500).fadeOut(500).fadeIn(500);//字典项页面里返回字典表有个闪烁的提示
			</c:if>

			//如果查询区有默认值，提醒一下
			$(".clsZdHasDef").each(function(){
				var tsob = $(this)
				tsob.css({"border-color": "#4b61dc", "border-width":"2px", "border-style":"solid","border-radius":"3px"});
				layer.ready(function(){
					setTimeout(function(){
						layer.tips('请注意该查询项有默认值', tsob,{tips:[3, '#3595CC'], tipsMore:true,tjpcms_clsouter:'cls_def_tip', tjpcms_clscont:'touming88',time:2900});
					},100)
				})
				setTimeout(function(){
					tsob.css({"border-color": "transparent"});
				},3000)
			})
			$(".content").scroll(function(){//但是如果页面滚动了，提示框去掉，不然显示上有问题
				$(".cls_def_tip").remove();
			})
		
			//动态调整含有操作符select的input的宽度
			window.setTimeout(function() {
				$(".clsselinpt").each(function(){
					if ($(this).children("div").length>0&&$(this).find(".haveopsel").length>0){
						$(this).children("input").animate({width:182-$(this).children("div").width()},100);//查询区对齐的
					}
				})
			}, 100)//火狐下面得缓冲一下才能刷出select的宽度

			layer.ready(function(){if ('${ses_ht_tip}' !='') layer.msg('${ses_ht_tip}')})//http://fly.layui.com/jie/1724.html，初始化时宽度还未计算出来，layer的提示会有显示问题

			//如果有树型区域，在初始化这里将树动态查出来
			if ('${o.r.tree.tb}'!==''){
				var conthtml = "<div class='lefttree'><div class='title'>"+'${o.r.tree.title}'+"</div><div class='lecont'><ul id='id_left_tree' class='ztree'></ul></div><div title='"+'${o.r.tree.tip}'+"' class='beizhu'>"+'${o.r.tree.tip}'+"</div></div>";
				if ($("body>.bread").length>0) $("body>.bread").after(conthtml)
				else $("body").prepend(conthtml)
				var ebz = $(".lefttree>.beizhu");
				if (ebz[0].scrollWidth>ebz.width()+5){//如果备注过长加一个marquee
					ebz.html("<marquee scrollamount=5' onMouseOut='' onMouseOver='fn_tipgd(this)'>"+ebz.text()+"</marquee>").css("cursor","pointer")
				}
				refreshTree();
			}

			layout();
			layout();
		})
		
		;<c:if test="${o.r.tree.tb!=''}">
			;var fn_tipgd = function(tag){
				if (fn_tipgd.ing==1) {fn_tipgd.ing=0;tag.stop();}
				else{fn_tipgd.ing=1;tag.start();}
			}
			fn_tipgd.ing=1;
			;function zTreeOnClick(event, treeId, treeNode){
				$("#id_left_tree #"+treeNode.tId+"_a").trigger('___tjpcms_evt_clk_lt')//没辙了，$(document).on('click','#id_left_tree a',function(){})的写法IE就是不兼容，懒得去源码调试了，用这个办法
				$("#id_form_cxq input[name='pId']").val(treeNode.id);
				shuaxin();
			}
			;function refreshTree(){
				var ot = $.fn.zTree.getZTreeObj("id_left_tree");
				var selnode=null;
				if (ot !=null){
					var nodes = ot.getSelectedNodes();
					if (nodes!=null && nodes.length >0) selnode = nodes[0]
				}
				
				var setting = {
					data: {simpleData: {enable: true}},
					callback: {onClick: zTreeOnClick},
					view: {fontCss: function(){/*还得有这个*/},nameIsHTML: true}
				};
				aj("tree.dhtml", $("#id_form_cxq").serialize()+"&u="+"${o.u}",{
					beforeSend:function(){},
					succ:function(rtn){
						var treeObj = $.fn.zTree.init($("#id_left_tree"), setting, rtn.tree);
						if (selnode==null) {
							var nds = treeObj.getNodes();
							if (nds!=null || nds.length>0) treeObj.selectNode(nds[0]);
						}else{
							treeObj.selectNode(selnode);
						}
					},fail:function(){
						layer.alert('获取'+"${o.r.tree.title}"+'失败，请联系管理员！', { icon:2, title:'错误'});
					},error:function(){
						layer.msg("获取"+"${o.r.tree.title}"+"失败，请刷新重试！");//这边就没搞刷新按钮了，我嫌麻烦，tabs那里有
					}
				})
			}
			function ___fn_add(){
				layer.open({
					type: 2,
					title: '新增 ',
					shadeClose: true,
					maxmin: true, 
					area: ['66%', '66%'],
					content: 'aev.dhtml?t=0&u='+'${o.u}'+'&pId='+$("#id_form_cxq input[name='pId']").val(),
					end:function(){refreshTree();shuaxin();}
				});
			}
			function ___fn_view(id){
				layer.open({
					type: 2,
					title: '查看',
					shadeClose: true,
					maxmin: true, 
					area: ['66%', '66%'],
					content: 'aev.dhtml?t=2&u='+'${o.u}'+'&id='+id
				});
			}
		</c:if>
		
		//arttemplate的自定义函数，jsp模板中由el，jstl写出来的逻辑换成js来实现
		function getExtwz(itm,zd){
			var extwz="";
		    if (zd.extc){
		    	var cnt = "";
		    	if (zd.extdbzd){
		    		cnt = itm[zd.extc]!=undefined?itm[zd.extc]:"";
		    		if (zd.extp !=undefined){
		    			cnt = parseInt(cnt)+parseInt(zd.extp)
		    		}
		    	}else{
		    		cnt = zd.extc;
		    	}
		    	if (zd.extsub && cnt) cnt = cnt.substring(0, zd.extsub)
		    	extwz = zd.exts+cnt+zd.exte;
		    } 
		    
		    return extwz;
		}
		template.helper("getExtwz", function(itm,zd){
		    return getExtwz(itm,zd);
		});
		function getCelltext(itm,zd){
			var celltext="";
			if (zd.zdm!=null && itm[zd.zdm]!=null && itm[zd.zdm]!=='' && itm[zd.zdm]!=undefined){
				celltext = itm[zd.zdm];
			}else{
				celltext = zd.empcoltip;
			}
			var extwz = getExtwz(itm,zd)
			if (extwz){
				if(zd.exttail) celltext = celltext+extwz;
				else celltext = extwz+celltext;
			}
			if (zd.zdm=='rq' || 'gx'==zd.zdm) {/* alert(celltext); */celltext = celltext.toString().substring(0,10)}
			
			return celltext;
		}
		template.helper("getCelltext", function(itm,zd){
			return getCelltext(itm,zd);
		});
		template.helper("getTitle", function(itm,zd){//某些字段的title不一定就是显示出来的文字
			var celltext = getCelltext(itm,zd);
			if(itm['___title_'+zd.zdm]!=undefined){
				celltext = celltext+"："+itm['___title_'+zd.zdm]
			}
			
			return celltext;
		});
		template.helper("getHref", function(itm,zd){
			var href="";
			var recHrefPara="";
			if (zd.hrefmp && zd.hrefmp.length>0){
				for(var i=0; i<zd.hrefmp.length; i++){
					var thf = zd.hrefmp[i];//t href   ->   thf
					if (thf.zdm && (thf.tplzdm==null || thf.tplzdm=='') && itm[thf.zdm]){
						recHrefPara = recHrefPara+"&"+thf.zdm+"="+itm[thf.zdm];
					}else if (thf.zdm && thf.tplzdm && itm[thf.tplzdm]){
						recHrefPara = recHrefPara+"&"+thf.zdm+"="+itm[thf.tplzdm];
					}else{
						recHrefPara = recHrefPara+"&"+thf.zdm+"="+itm[thf.zdz];
					}
				}
			}
			
			return zd.href+(recHrefPara!=""?"?"+recHrefPara.substring(1,recHrefPara.length):"");
		});
		template.helper("getPic", function(itm,zd){
			var extwz = getExtwz(itm,zd);
			var divpic = '<div class="clstdpic"><img style="max-height:50px;" src="'+itm[zd.zdm]+'" /></div>';
			if (extwz){
				if (!zd.exttail) return extwz+divpic;
				else return divpic+extwz;
			}else{
				return divpic;
			}
		});
		//马德，写到这我才突然发现，我为什么不每种字段类型写一个函数呢，就像这个getZdtext
		template.helper("getZdtext", function(itm,zd){
			var celltext='';
			if (zd.textval){
				celltext = zd.textval;
			}else if (zd.textmap && zd.textmpzd){
				if (zd.textmap[itm[zd.textmpzd]]){
					celltext = zd.textmap[itm[zd.textmpzd]]
				}else{
					celltext = zd.empcoltip;
				}
			}
			var extwz = getExtwz(itm,zd)
			if (extwz){
				if(zd.exttail) celltext = celltext+extwz;
				else celltext = extwz+celltext;
			}
				
			return celltext;
		});
	</script>
	
	
	<script id="sid_popsh" type="text/html">
		<div id="popsh" style='padding:10px 30px 10px 10px; ;'>
			<textarea id="popshyj" style="width:100%;padding:10px;margin-bottom:10px;color:#999" rows="3" maxlength="100" value=""
			onfocus="if(this.value=='审核意见（100字以内）'){this.value='';this.style.color='#000'}" 
			onblur="if(!this.value){this.value='审核意见（100字以内）';this.style.color='#999'}" style="color:#999999">审核意见（100字以内）</textarea>
			<div style="text-align:center">
				{{if shzt=='待审核'}}
					<input type="button" value="审核通过" class="ext_btn ext_btn_submit" onclick="fn_op_wzsh(this,{{id}}, {{xh}},'{{u}}')"/>
					<input type="button" value="审核不通过" class="ext_btn ext_btn_submit" onclick="fn_op_wzsh(this,{{id}}, {{xh}},'{{u}}')"/>
					<input type="button" value="退回修改" class="ext_btn ext_btn_submit" onclick="fn_op_wzsh(this,{{id}}, {{xh}},'{{u}}')"/>
				{{else if shzt=='审核通过'}}
					<input type="button" value="审核不通过" class="ext_btn ext_btn_submit" onclick="fn_op_wzsh(this,{{id}}, {{xh}},'{{u}}')"/>
					<input type="button" value="退回修改" class="ext_btn ext_btn_submit" onclick="fn_op_wzsh(this,{{id}}, {{xh}},'{{u}}')"/>
				{{else if shzt=='审核不通过'}}
					<input type="button" value="审核通过" class="ext_btn ext_btn_submit" onclick="fn_op_wzsh(this,{{id}}, {{xh}},'{{u}}')"/>
					<input type="button" value="退回修改" class="ext_btn ext_btn_submit" onclick="fn_op_wzsh(this,{{id}}, {{xh}},'{{u}}')"/>
				{{else if shzt=='退回修改'}}
					<input type="button" value="审核通过" class="ext_btn ext_btn_submit" onclick="fn_op_wzsh(this,{{id}}, {{xh}},'{{u}}')"/>
					<input type="button" value="审核不通过" class="ext_btn ext_btn_submit" onclick="fn_op_wzsh(this,{{id}}, {{xh}},'{{u}}')"/>
				{{/if}}
			</div>
		</div>
	</script>
	
	<!-- 这块是前台模板，和后台模板要逻辑一模一样，只是语法不一样，也就是说改的话这里和后台模板那里要同步同逻辑改 -->
	<script id="sid_tabs" type="text/html">
		{{each tabs as itm idx}}
			<tr {{if idx%2==1}}style='background:rgb(255, 252, 234)'{{/if}}>
				{{if odbatdel}}
					<td class='cls_bd_chk' style="padding: 10px 0;" onselectstart="return false"><input name="id[]" type="checkbox" value="" id='{{itm.id}}'/></td>
				{{/if}}
				<td title="{{idx+1+(curPage-1)*perPage}}">{{idx+1+(curPage-1)*perPage}}</td>
				{{each zds as zd}}
					{{if zd.display!='none'}}
						{{if zd.zdType=='normal'}}
							{{if zd.href==null || zd.href==''}}
								<td title="{{itm | getTitle:zd}}">{{itm | getCelltext:zd}}</td>
							{{else}}
								<td title="{{itm | getCelltext:zd}}">
									<a href="{{itm | getHref:zd}}" {{if zd.tgblank}}target="_blank"{{/if}} style="border-bottom: 1px solid blue;padding: 0 5px;">{{itm | getCelltext:zd}}</a>
								</td>
							{{/if}}
						{{else if zd.zdType=='pic'}}
							{{if itm[zd.zdm]}}
								<td style="padding:0;">{{#itm | getPic:zd}}</td>
							{{else}}
								<td title="{{itm | getCelltext:zd}}">{{itm | getCelltext:zd}}</td>
							{{/if}}
						{{else if zd.zdType=='text'}}
							<td title="{{itm | getZdtext:zd}}">{{itm | getZdtext:zd}}</td>
						{{else}}
							<td></td>
						{{/if}}
					{{/if}}
				{{/each}}

				{{if itm.lstczq!=null && itm.lstczq.length>0}}
					<td class="tdop" style="white-space:normal">
						{{each itm.lstczq as t}}{{#t.czx}}{{/each}}
					</td>
				{{/if}}
			</tr>
		{{/each}}
	</script>
</body>
</html>