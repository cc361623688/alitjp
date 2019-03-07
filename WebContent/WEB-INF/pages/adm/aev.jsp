<!--
 * 作者:tjp
 * QQ号：57454144
 * QQ群：168895774（有教学视频下载）
 * 官网：http://www.tjpcms.com
 * 码云：https://git.oschina.net/tjpcms/tjpcms
 * 更新日期：2017-01-22
 * tjpcms - 最懂你的cms
 * ${lx}：0-新增，1-编辑，2-查看
-->


<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%
	request.setAttribute("path", request.getContextPath());
%> 

<c:if test="${!o.r.need && empty(lx)}"><c:set var="lx" value="2"/></c:if><!-- 只编辑的情况，默认进来是只读的 -->

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="X-UA-Compatible" content="IE=EDGE,chrome=1"/>
	<meta name="renderer" content="webkit" />
	<meta http-equiv="pragma" content="no-cache"/>
	<meta http-equiv="cache-control" content="no-cache"/>
	<meta http-equiv="expires" content="0"/>
	<link rel="stylesheet" href="${path }/css/ht_cmn.css?___v=<%=Math.random()%>" type="text/css"/>
	<link rel="stylesheet" href="${path}/ueditor/themes/default/css/ueditor.min.css"/>
    <script src="${path }/js/jquery.js" type="text/javascript"></script>
    <script src="${path}/My97DatePicker/WdatePicker.js" type="text/javascript"></script>
    <script src="${path }/js/cmn.js" type="text/javascript"></script>
    <script src="${path }/js/jquery.form.min.js" type="text/javascript"></script>
	<script src="${path}/ueditor/ueditor.config.js" type="text/javascript"></script>
	<script src="${path}/ueditor/ueditor.all.min.js" type="text/javascript"></script>
	<!-- <script src="${path}/js/jquery.formLocalStorage.js?" type="text/javascript"></script>表单暂存，致谢作者 since1986 -->
	<c:forEach items="${o.aev.zds}" var="t" varStatus="status">
		<c:if test="${empty ___aev_js_tjp_ipttree && t.type=='___TREE'}">
			<c:set var="___aev_js_tjp_ipttree" value="1"/>
			<link rel="stylesheet" href="${path }/css/zTreeStyle/zTreeStyle.css" type="text/css"/>
	    	<script type="text/javascript" src="${path }/js/jquery.ztree.core.min.js"></script>
			<script type="text/javascript" src="${path}/js/jquery.tjpinputtree.js?___v=<%=Math.random()%>"></script>
		</c:if>
	</c:forEach>
	
	
	<!-- 可以引入多个js -->
	<c:forEach items="${o.aev.extjs}" var="t" varStatus="status">
		<script type="text/javascript" src="${path}/js/extjs/${t}"></script>
	</c:forEach>
	<c:forEach items="${o.aev.extjsp}" var="t" varStatus="status">
		<jsp:include page="../extjsp/${t}"/>
	</c:forEach> 

	<style>
		body{background: #fff;}
		.content{padding: 10px}
		.upload .ext_btn{margin-right: 5px;}
		.tishi{font-size: 12px;margin-top: 5px;background: #fec;color: #f60;padding: 5px;-webkit-border-radius: 5px;
		-moz-border-radius: 5px;
		border-radius: 5px;}
		.list_table td{word-break:break-all;white-space:normal;}
		.list_table td img{max-height: 150px;}
		.updinfo strong{font-size: 12px;}
		._jquery_form_local_storage {color : green;}
	</style>
</head>
<body>

	<div class="content">
		<form id="id_ae_form">
			${o.aev.hdnpara}
			<c:if test="${o.r.tree.tb!=''}"><input type="hidden" name="pId" value="${pId}"/></c:if>
			<table class="list_table" border="0" cellpadding="0" cellspacing="0">
				<thead>
					<tr>
						<th class="first">
							<c:out value="${o.aev.title}" default="${o.bread}"/>
						</th>
						<th style="border-left-width:0;border-right-width:0"></th>
						<th style="border-left-width:0"></th>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${o.aev.zds}" var="t" varStatus="status">
						<tr>
							<c:if test="${t.type!='___TEXT' || !empty(t.val) || (!empty(t.zdm) && lx==2)}">
						 		<td ><b>${t.title}</b>
						 			<c:if test="${t.required}"><img title ="必填" src="${path}/img/star.png" style="width:10px;vertical-align:top"/></c:if>
						 			<c:if test="${t.unique}"><img title ="唯一" src="${path}/img/sole.png" style="width:10px;vertical-align:top"/></c:if>
						 		</td>
						 	</c:if>

							<c:choose>
								<c:when test="${t.type=='___INPUT'}">
									<td colspan="2">
										<c:set var="iptval" value="${lx==0?t.val:obj[t.zdm]}"/>
										<c:if test="${t.zdm=='rq'}"><c:set var="iptval" value="${fn:substring(iptval,0,10)}"/></c:if>
										<input type="text" <c:if test="${lx==2 || !o.r.need}">disabled readonly</c:if> 
										value="${iptval}" data-validate="${t.validate}" name="${t.zdm}" 
										class="input-text lh25 clsinput" maxlength="${t.maxlen}" ${t.event} placeholder="${t.phd}"/>
									</td>
								</c:when>
								<c:when test="${t.type=='___PASSWORD'}">
									<td colspan="2">
										<c:set var="iptval" value="${lx==0?t.val:obj[t.zdm]}"/>
										<input type="password" <c:if test="${lx==2 || !o.r.need}">disabled readonly</c:if> 
										value="" data-validate="${t.validate}" name="${t.zdm}" 
										class="input-text lh25 clsinput" maxlength="${t.maxlen}" ${t.event} placeholder="${t.phd}"/>
									</td>
								</c:when>
								<c:when test="${t.type=='___SELECT'}">
									<td colspan="2" onselectstart="return false">
										<select name="${t.zdm}" class="select" <c:if test="${lx==2 || !o.r.need}">disabled readonly</c:if>  ${t.event}>
											<c:if test="${!t.required}"><option value="">  </option></c:if>
											<c:forEach items="${t.selops}" var="itmsel" varStatus="sts1">
												<option value="${itmsel.val}" <c:if test="${obj[t.zdm]==itmsel.val || (lx==0&&sts1.index==0)}">selected</c:if>>${itmsel.txt}</option>
											</c:forEach>
										</select>
									</td>
								</c:when>
								<c:when test="${t.type=='___TEXT'}">
									<c:if test="${!empty(t.val)}">
										<td colspan="2" style="text-align:center">${t.val}</td>
									</c:if>
									<c:if test="${!empty(t.zdm) && lx==2}"><td colspan="2" style="text-align:left">${obj[t.zdm]}</td></c:if>
								</c:when>
								<c:when test="${t.type=='___PIC'}">
									<c:if test="${lx!=2 || !o.r.need}">
									 	<td class="upload ${t.validate}" zdm="${t.zdm}">
									 		<input type="hidden" name="up_size_check_tag" value="pic"/>
									 		<input type="file" <c:if test="${lx==2 || !o.r.need}">disabled readonly</c:if> class="input-text lh25 clsiptbtn" name="file"/>
									 		<input type="button" value="上传" onclick="fn_sc(this)" class="ext_btn ext_btn_submit clsiptbtn clstag${t.type}"/>
									 		<input type="button" value="清空" onclick="fn_qk(this)" class="ext_btn clsiptbtn clstag${t.type}"/>
									 		<div class="tishi">请选择jpg、jpeg、png或gif格式的图片，大小不超过3M</div>
										</td>
									</c:if>
								 	<td colspan="${(lx!=2 || !o.r.need) ?1:2}" style="overflow:auto;">
								 		<c:choose>
								 			<c:when test="${!empty(obj[t.zdm])}">
								 				<img src='${obj[t.zdm]}'></img>
								 			</c:when>
								 			<c:when test="${!empty(t.val)}">
								 				<img src='${t.val}'></img>
								 			</c:when>
								 			<c:otherwise>
								 				未上传图片
								 			</c:otherwise>
								 		</c:choose>
								 	</td>
								</c:when>
								<c:when test="${t.type=='___VDO'}">
									<c:if test="${lx!=2 || !o.r.need}">
									 	<td class="upload ${t.validate}" zdm="${t.zdm}">
									 		<input type="hidden" name="up_size_check_tag" value="vdo"/>
									 		<input type="file" <c:if test="${lx==2 || !o.r.need}">disabled readonly</c:if> class="input-text lh25 clsiptbtn" name="file"/>
									 		<input type="button" value="上传" onclick="fn_sc(this)" class="ext_btn ext_btn_submit clsiptbtn clstag${t.type}"/>
									 		<input type="button" value="清空" onclick="fn_qk(this)" class="ext_btn clsiptbtn clstag${t.type}"/>
									 		<div class="tishi">请选择mp4或flv格式的视频，大小不超过500M</div>
										</td>
									</c:if>
								 	<td colspan="${(lx!=2 || !o.r.need) ?1:2}" style="overflow:auto;" class="updinfo">
								 		<c:choose>
								 			<c:when test="${!empty(obj[t.zdm]) || !empty(t.val)}">
								 				<strong>已上传视频</strong>
								 			</c:when>
								 			<c:otherwise>
								 				未上传视频
								 			</c:otherwise>
								 		</c:choose>
								 	</td>
								</c:when>
								<c:when test="${t.type=='___RICH'}">
									<td colspan="2"  style="text-align: left;">
										<script type="text/plain" class="uecontainer ${t.validate} max:${t.maxlen}" id="ueditor${status.index}" zdm="${t.zdm}" style="width:100%;" maxlength="${t.maxlen}">${obj[t.zdm]}</script>
									</td>
								</c:when>
								<c:when test="${t.type=='___TEXTAREA'}">
									<c:set var="havetextarea" value="1"/>
									<td colspan="2">
										<textarea <c:if test="${lx==2 || !o.r.need}">disabled readonly</c:if>  class="input-text clstextarea"  style="width:100%;height: 100%;" rows="6"
										data-validate="${t.validate}" name="${t.zdm}" maxlength="${t.maxlen}" ${t.event} placeholder="${t.phd}">${lx==0?t.val:obj[t.zdm]}</textarea>
									</td>
								</c:when>
								<c:when test="${t.type=='___ZDB'}">
									<c:set var="havezdb" value="1"/>
									<td colspan="2" onselectstart="return false">
										<select name="zdb" class="select" <c:if test="${lx==2 || !o.r.need}">disabled readonly</c:if> 
											onchange="fn_zdbchange($('select[name=\'zdb\']'), '${t.maxlen}', '${t.zdm}', '${obj[t.zdm]}')">
											<c:if test="${!t.required}"><option value="">  </option></c:if>
											<c:forEach items="${t.zdbops}" var="itmsel" varStatus="sts1">
												<option value="${itmsel.val}" <c:if test="${obj['zdb']==itmsel.val || (lx==0&&sts1.index==0)}">selected</c:if>>${itmsel.txt}</option>
											</c:forEach>
											<c:if test="${t.zdbHaveZdy}">
												<option value='' <c:if test="${lx!=0 && empty(obj['zdb'])}">selected</c:if>>自定义</option>
											</c:if>
										</select>
										<select name="${t.zdm}" class="select" <c:if test="${lx==2 || !o.r.need}">disabled readonly</c:if>
											onclick="fn_clkzdx(this)" onchange="fn_cgzdx(this)">
										</select>
									</td>
								</c:when>
								<c:when test="${t.type=='___RADIO'}">
									<td colspan="2">
										<c:forTokens items="${t.val}" delims=" ,，" var="lov" varStatus="los">
											<input type="radio" <c:if test="${lx==2 || !o.r.need}">disabled readonly</c:if>  name="${t.zdm}" class="clsinput"  btm="${t.title}"
												<c:if test="${(los.index==0 && t.required) || obj[t.zdm]==lov}">checked="checked"</c:if> rqd="${t.required}" value="${lov}"/>${lov}&nbsp;&nbsp;&nbsp;
										</c:forTokens>
									</td>
								</c:when>
								<c:when test="${t.type=='___CHECKBOX'}">
									<td colspan="2">
										<c:forTokens items="${t.val}" delims=" ,，" var="lov" varStatus="los">
											<c:set var="ischked" value=""/>
											<c:forTokens items="${obj[t.zdm]}" delims="," var="obv">
												<c:if test="${obv==lov}"><c:set var="ischked" value="1"/></c:if>
											</c:forTokens>
											<input type="checkbox" <c:if test="${lx==2 || !o.r.need}">disabled readonly</c:if>  name="${t.zdm}" class="clsinput"  btm="${t.title}"
												<c:if test="${ischked==1}">checked="checked"</c:if>  rqd="${t.required}" value="${lov}"/>${lov}&nbsp;&nbsp;&nbsp;
										</c:forTokens>
									</td>
								</c:when>
								<c:when test="${t.type=='___DATE'}">
									<td colspan="2">
										<input style="width:auto !important" type="text" name="${t.zdm}" onclick="WdatePicker()" 
											class="Wdate input-text lh25 clsinput" value="${obj[t.zdm]}" rqd="${t.required}" btm="${t.title}"
											<c:if test="${lx==2 || !o.r.need}">disabled readonly</c:if> />
										<c:if test="${t.title=='更新日期' && t.zdm=='gx' && !(lx==2 || !o.r.need)}">
											<c:set var="caneditGxzd" value="1"/>
											<input type="checkbox" id="chkctgx"/>如需编辑更新日期请先勾选
										</c:if>
									</td>
								</c:when>
								<c:when test="${t.type=='___TREE'}">
									<td colspan="2">
										<input type="text"  placeholder="${t.phd}" name="${t.zdm}" class="input-text clstreerdo" style="text-indent: 5px;" readonly value="${obj[t.zdm]}" 
											<c:if test="${lx==2 || !o.r.need}">disabled</c:if> data-validate="${t.validate}"/>
									</td>
									<script>$(".clstreerdo[name='${t.zdm}']").tjpinputtree($.parseJSON('${t.treelist}'), '${lx}'=='0'?null:'${obj[t.zdm]}')</script>
								</c:when>
							</c:choose>
						</tr>
					</c:forEach>
					<c:if test="${!empty(o.aev.tip)}">
						<tr>
							<td colspan="3" class="htwztip">${o.aev.tip}</td>
						</tr>
					</c:if>
					<c:if test="${!o.aev.noBtns}">
						<tr>
						 	<td colspan="3">
						 		<c:if test="${o.r.need}">
									<c:if test="${lx!=2}"><input title="${o.aev.canZc?'提交后进入审核流程':''}" type="button" value="${ o.aev.canZc?'提交':'保存'}" class="ext_btn ext_btn_submit" onclick="fn_bc()"/>&nbsp; </c:if>
									<c:if test="${lx!=2 && o.aev.canZc}"><input id="id_btn_zc" title="${o.aev.canZc?'也可Ctrl+s暂存':''}" type="button" value="暂存" class="ext_btn ext_btn_submit" onclick="fn_bc(1)"/>&nbsp; </c:if>
									<c:if test="${o.r.tree.tb==''}"><input title="返回到数据列表页面" type="button" value="返回" onclick="location.href='${o.r.listloc}'" class="ext_btn"/></c:if><!-- 因为弹出层打开的新增不能返回，必须关闭 -->
						 		</c:if>
						 		<c:if test="${!o.r.need && (o.aev.needa || o.aev.neede)}"><!--没有列表页，只有编辑页-->
						 			<input type="button" value="编辑" class="ext_btn ext_btn_submit" onclick="fn_bj(this)" />
						 			<input type="button" value="取消" class="ext_btn clsquxiao" style="display:none" onclick="fn_qx()"/>
						 		</c:if>
							</td>
						 </tr>
					 </c:if>
				</tbody>
			</table>
		</form>
	</div>
	

	<script src="${path}/layer/layer.js"></script>
    <script type="text/javascript">
    	//ctrl+s暂存
    	<c:if test="${lx!=2 && o.aev.canZc}">
	 		$(document).keydown(function(e){
		        if(e.ctrlKey  && e.which == 83) {
		        	fn_bc(2)
			        e.preventDefault();
			        return false;
		        }
		    })
	    </c:if>
	     
    	//供其他函数来调用，以获取lx的值，主要是extjs中是无法直接获取${lx}的
    	;function getAevLx(){
    		return '${lx}';
    	}
    
		$(function(){
			//前端暂存formLocalStorage({storage_name_perfix:'___tjpcms_aev_jsp_ls_'+'${o.tb}'+'${o.cid}'});//暂存
		
			//textarea类型的长度校验
			;<c:if test='${!empty(havetextarea)}'>
				$("textarea").keyup(function(){
					var mc = $(this).parent().prev().text()
	 				var len = $(this).val().length
	 				var maxlen = parseInt($(this).attr("maxlength"))
					if (len>maxlen){
						layer.msg('【'+mc+'】字段超出最大保存长度(最大'+maxlen+'，当前'+len+')，请检查输入内容！')
					}else if (len==maxlen){
						layer.msg('【'+mc+'】字段已达最大保存长度('+maxlen+')！')
					}
				})
			</c:if>

			;<c:if test="${caneditGxzd==1}">
				$("#chkctgx").prev().attr({disabled:"disabled", readonly:"readonly"})
				$("#chkctgx").click(function(){
					if ($(this).prop("checked")){
						layer.confirm('确定要手动设置该条记录的更新日期吗？', {icon: 3, title:'提示'}, 
							function(index){
								layer.closeAll();
								$("#chkctgx").prev().removeAttr("disabled readonly").focus()
							},function (index){
								$(this).prev().attr({disabled:"disabled", readonly:"readonly"})
								$("#chkctgx").prop("checked", false)
							})
					}else{
						$(this).prev().attr({disabled:"disabled", readonly:"readonly"})
					}
				})
			</c:if>

			;layer.ready(function(){//http://fly.layui.com/jie/1724.html
				if ('${ses_ht_tip}' !=''){
					setTimeout(function(){layer.msg('${ses_ht_tip}')},150)
				}
			})

			var fncg = $(".list_table select[name='zdb']").attr('onchange');
			eval(fncg);

			layout();
			layout();
		})

		function layout(){
			$(".content").height($(window).height()-($(".bread").length>0?45:20))
		}
		window.onresize=function(){
			layout();layout()
		}

		;<c:if test="${lx==2}">
			$(".uecontainer").each(function(idx){
				//alert("${richval1}")//initialContent:"${richval1}",
	   			var e = UE.getEditor($(this).attr("id"),{initialFrameHeight:200,wordCount:false,elementPathEnabled:false,enableAutoSave:false,textarea:$(this).attr("zdm")});
		   		e.addListener('ready',function(){  
		   	    	e.setDisabled();
		   	     }); 
				e.addListener("keydown", function(type,e) {//处理查看时点中UE，再按backspace的情况
					if (this.body.contentEditable == 'false'){//disabled的话就不响应了
						e.preventDefault();  
			   	    	e.stopPropagation(); 
					}
				})
	   		})
		</c:if>

		<c:if test="${lx==0 || lx==1 || !o.r.need}">
			;$(".list_table input[type='radio']").click(function(){
				var rqd = $(this).attr("rqd")
				if ("true"!=rqd || 'checkbox' == $(this).attr('type')){
					$(this).siblings().removeAttr("checked xuanz")
					$(this).attr("xuanz")?$(this).removeAttr("checked xuanz"):$(this).attr({"checked":"checked", "xuanz":"1"})
				}
			})
			;$(".list_table input[type='checkbox']").click(function(){
				var rqd = $(this).attr("rqd")
				var cklen = $(this).siblings("input[type='checkbox']:checked").length+($(this).prop("checked")?1:0)
				if ("true"==rqd && cklen<=0){
					layer.msg($(this).attr('btm')+' 至少选中一项！')
				}
			})
		
			//先是准备模拟失去焦点，百度：addEventListener 如何模拟触发
			//这里攻关了好久，我要记一篇csdn了
			//这个函数取自ueditor.all.js，是为了模拟失去焦点，兼容所有情况，也不影响焦点
		    function setValue(form, editor) {
		        var textarea;
		        if (editor.textarea) {
		            if (UE.utils.isString(editor.textarea)) {
		                for (var i = 0, ti, tis = UE.dom.domUtils.getElementsByTagName(form, 'textarea'); ti = tis[i++];) {
		                    if (ti.id == 'ueditor_textarea_' + editor.options.textarea) {
		                        textarea = ti;
		                        break;
		                    }
		                }
		            } else {
		                textarea = editor.textarea;
		            }
		        }
		        if (!textarea) {
		            form.appendChild(textarea = UE.dom.domUtils.createElement(document, 'textarea', {
		                'name': editor.options.textarea,
		                'id': 'ueditor_textarea_' + editor.options.textarea,
		                'style': "display:none"
		            }));
		            //不要产生多个textarea
		            editor.textarea = textarea;
		        }
		        !textarea.getAttribute('name') && textarea.setAttribute('name', editor.options.textarea );
		        textarea.value = editor.hasContents() ? (editor.options.allHtmlEnabled ? editor.getAllHtml() : editor.getContent(null, null, true)) :''
		    }
		
			$(".uecontainer").each(function(){
				var eudid =  $(this).attr("id");
				var maxlen = parseInt($(this).attr("maxlength"))
	   			var e = UE.getEditor($(this).attr("id"),{initialFrameHeight:200,wordCount:false,elementPathEnabled:false,enableAutoSave:false,textarea:$(this).attr("zdm")});
	   			
				e.addListener("keyup", function(type) {
					var mc = $("#"+this.key).parent().prev().text()
	 				var len = this.getContentLength();
					if (len>maxlen){
						layer.msg('【'+mc+'】字段超出最大保存长度(最大'+maxlen+'，当前'+len+')，请检查输入内容！')
					}else if (len==maxlen){
						layer.msg('【'+mc+'】字段已达最大保存长度('+maxlen+')！')
					}
					
					//把前端暂存也弄一下吧
					//算了，先不弄吧
					//localStorage.setItem("", storage_value);
				})
				
				//ueditor的ctrl+s暂存。因为这里是iframe了，事件监听不到这里了
				;<c:if test="${lx!=2 && o.aev.canZc}">
					e.addListener("keydown",function(type,event){
						if(event.ctrlKey && 83===event.keyCode){
							setValue(e.form,e)//这一个函数搞了我一天多
		                	fn_bc(2)
					        if (event.preventDefault) event.preventDefault();
					        return false;
						}
	                })
	        	</c:if>;
	   		})

			;$pintuercheck = function (element, type, value) {
				var $pintu = value.replace(/(^\s*)|(\s*$)/g, "");
		        if (type=="have_kg" || type=="not_have_kg")$pintu = value;
		        switch (type) {
		            case "required": return /[^(^\s*)|(\s*$)]/.test($pintu); break;
		            case "chinese": return /^[\u0391-\uFFE5]+$/.test($pintu); break;
		            case "number": return /^([+-]?)\d*\.?\d+$/.test($pintu); break;
		            case "integer": return /^-?[1-9]\d*$/.test($pintu); break;
		            case "plusinteger": return /^[1-9]\d*$/.test($pintu); break;
		            case "unplusinteger": return /^-[1-9]\d*$/.test($pintu); break;
		            case "znumber": return /^[1-9]\d*|0$/.test($pintu); break;
		            case "fnumber": return /^-[1-9]\d*|0$/.test($pintu); break;
		            case "double": return /^[-\+]?\d+(\.\d+)?$/.test($pintu); break;
		            case "plusdouble": return /^[+]?\d+(\.\d+)?$/.test($pintu); break;
		            case "unplusdouble": return /^-[1-9]\d*\.\d*|-0\.\d*[1-9]\d*$/.test($pintu); break;
		            case "english": return /^[A-Za-z]+$/.test($pintu); break;
		            case "username": return /^[a-z]\w{3,}$/i.test($pintu); break;
		            case "mobile": return /^\s*(15\d{9}|13\d{9}|14\d{9}|17\d{9}|18\d{9})\s*$/.test($pintu); break;
		            case "phone": return /^((\(\d{2,3}\))|(\d{3}\-))?(\(0\d{2,3}\)|0\d{2,3}-)?[1-9]\d{6,7}(\-\d{1,4})?$/.test($pintu); break;
		            case "tel": return /^((\(\d{3}\))|(\d{3}\-))?13[0-9]\d{8}?$|15[89]\d{8}?$|170\d{8}?$|147\d{8}?$/.test($pintu) || /^((\(\d{2,3}\))|(\d{3}\-))?(\(0\d{2,3}\)|0\d{2,3}-)?[1-9]\d{6,7}(\-\d{1,4})?$/.test($pintu); break;
		            case "email": return /^[^@]+@[^@]+\.[^@]+$/.test($pintu); break;
		            case "url": return /^http:\/\/[A-Za-z0-9]+\.[A-Za-z0-9]+[\/=\?%\-&_~`@[\]\':+!]*([^<>\"\"])*$/.test($pintu); break;
		            	case "url_s": return /(http|https):\/\/[\w\-_]+(\.[\w\-_]+)+([\w\-\.,@?^=%&amp;:/~\+#]*[\w\-\@?^=%&amp;/~\+#])?/.test($pintu); break;
		            case "http": return /(http|https):\/\/([\w.]+\/?)\S*/.test($pintu); break;
		            case "not_have_kg": return /^[^\s+]*$/.test($pintu); break;
		            case "have_kg": return /^\S*$/.test($pintu); break;
		            case "ip": return /^[\d\.]{7,15}$/.test($pintu); break;
		            case "qq": return /^[1-9]\d{4,10}$/.test($pintu); break;
		            case "currency": return /^\d+(\.\d+)?$/.test($pintu); break;
		            case "zipcode": return /^[1-9]\d{5}$/.test($pintu); break;
		            case "chinesename": return /^[\u0391-\uFFE5]{2,15}$/.test($pintu); break;
		            case "englishname": return /^[A-Za-z]{1,161}$/.test($pintu); break;
		            case "age": return /^[1-99]?\d*$/.test($pintu); break;
		            case "date": return /^((((1[6-9]|[2-9]\d)\d{2})-(0?[13578]|1[02])-(0?[1-9]|[12]\d|3[01]))|(((1[6-9]|[2-9]\d)\d{2})-(0?[13456789]|1[012])-(0?[1-9]|[12]\d|30))|(((1[6-9]|[2-9]\d)\d{2})-0?2-(0?[1-9]|1\d|2[0-8]))|(((1[6-9]|[2-9]\d)(0[48]|[2468][048]|[13579][26])|((16|[2468][048]|[3579][26])00))-0?2-29-))$/.test($pintu); break;
		            case "datetime": return /^((((1[6-9]|[2-9]\d)\d{2})-(0?[13578]|1[02])-(0?[1-9]|[12]\d|3[01]))|(((1[6-9]|[2-9]\d)\d{2})-(0?[13456789]|1[012])-(0?[1-9]|[12]\d|30))|(((1[6-9]|[2-9]\d)\d{2})-0?2-(0?[1-9]|1\d|2[0-8]))|(((1[6-9]|[2-9]\d)(0[48]|[2468][048]|[13579][26])|((16|[2468][048]|[3579][26])00))-0?2-29-)) (20|21|22|23|[0-1]?\d):[0-5]?\d:[0-5]?\d$/.test($pintu); break;
		            case "idcard": return /^(\d{6})(\d{4})(\d{2})(\d{2})(\d{3})([0-9]|X)$/.test($pintu); break;
		            case "bigenglish": return /^[A-Z]+$/.test($pintu); break;
		            case "smallenglish": return /^[a-z]+$/.test($pintu); break;
		            case "color": return /^#[0-9a-fA-F]{6}$/.test($pintu); break;
		            case "ascii": return /^[\x00-\xFF]+$/.test($pintu); break;
		            case "md5": return /^([a-fA-F0-9]{32})$/.test($pintu); break;
		            case "zip": return /(.*)\.(rar|zip|7zip|tgz)$/.test($pintu); break;
		            case "img": return /(.*)\.(jpg|gif|ico|jpeg|png)$/.test($pintu); break;
		            case "doc": return /(.*)\.(doc|xls|docx|xlsx|pdf)$/.test($pintu); break;
		            case "mp3": return /(.*)\.(mp3)$/.test($pintu); break;
		            case "video": return /(.*)\.(rm|rmvb|wmv|avi|mp4|3gp|mkv)$/.test($pintu); break;
		            case "flash": return /(.*)\.(swf|fla|flv)$/.test($pintu); break;
		            case "radio":
		                var radio = element.closest('form').find('input[name="' + element.attr("name") + '"]:checked').length;
		                return eval(radio == 1);
		                break;
		            default:
		                var $test = type.split('#');
		            	if ($test[0].indexOf("unique")==0){//是unique类型
		            			var extpa = "&lx="+${lx}+"&val="+element.val()+"&zdm="+element.attr("name")
		            		if($test.length > 1){extpa+=("&___extfilter=("+$test[1]+")")}
		            		if ('${lx}'=='1'){extpa+=("&id="+'${obj.id}')}//是编辑，则有id
                            $.ajaxSetup({ async: false });
                            var $getdata=null
                            		$.getJSON('unique.dhtml?u=${o.u}'+extpa, function (data) {
                                $getdata = data.getdata;
                            })
                            return $getdata == "true";
		            	}else if ($test.length > 1) {
		                    switch ($test[0]) {
		                        case "compare":
		                            return eval(Number($pintu) + $test[1]);
		                            break;
		                        case "regexp":
		                            return new RegExp($test[1], "gi").test($pintu);
		                            break;
		                        case "password":
		                        	return new RegExp($test[1], "g").test($pintu);
		                        	break;
		                        case "length":
		                            var $length;
		                            if (element.attr("type") == "checkbox") {
		                                $length = element.closest('form').find('input[name="' + element.attr("name") + '"]:checked').length;
		                            } else {
		                                $length = $pintu.replace(/[\u4e00-\u9fa5]/g, "***").length;
		                            }
		                            return eval($length + $test[1]);
		                            break;
		                        case "ajax":
		                            var $getdata;
		                            var $url = $test[1] + $pintu;
		                            var $getdata=null
		                           		 $.ajaxSetup({ async: false });
		                            $.getJSON($url, function (data) {
		                                $getdata = data.getdata;
		                            });
		                            if ($getdata == "true") { return true; }
		                            break;
		                        case "repeat":
		                            return value == jQuery('input[name="' + $test[1] + '"]').eq(0).val();//这里我不知道为什么把$pintu里的空格替换掉？我就改成value了
		                            break;
		                        default: return true; break;
		                    }
		                    break;
		                } else {
		                    return true;
		                }
		        }
		    }

			function checkdv(ipt){
				var dvstr = ipt.attr("data-validate")
				var isrequired = !isEmptyStr(dvstr) && (dvstr.indexOf("required") >= 0)
				var $checkvalue = ipt.val()
				var ret={passed:true,invalid:''};
				if (!isEmptyStr(dvstr) && !($checkvalue  == "" && !isrequired)){//两种情况不用校验，1data-validate为空，2非必填且input没有填内容
					var $checkdata = dvstr.split(/\s+/)//按空白分割
	                	for (var i = 0; i < $checkdata.length; i++) {
	                    var $checktype = $checkdata[i].split(':::');
	                    if (!$pintuercheck(ipt, $checktype[0], $checkvalue)) {
	                    	ret.invalid = $checktype[1];
	                    	ret.passed = false
	                        	break;
	                   	}
	                }
				}
				
				return ret
			}
			
			function fn_bc(___zc){
				//___zc:暂存，暂存的话不做前后台校验，只做后台的入库，如果入库失败。。。有点坑。遇到再说吧
				if (!___zc){
					//这里留个接口，可以在系统的保存逻辑之前做自定义的校验
					if ('${fn:length(o.aev.js_bef_zdyjy)>0}'=='true') {
						<c:forEach items="${o.aev.js_bef_zdyjy}" var="t" >
								if (!eval(${t})){
									return;//返回false则中断校验，说明校验已不通过
								}
						</c:forEach>
					}
	
					//前台校验部分
					var invalid = ""
					var kj = null
					$(".list_table>tbody>tr:not(:last)").each(function(){
						var td = $(this).children("td:eq(1)")
						var ipt = td.children(".clsinput:first")
						if (ipt.length>0){//是input
							var attype = ipt.attr('type');
							if (attype=='text' || attype=='password' ){
								if (!ipt.hasClass('Wdate')){
									var ckrto = checkdv(ipt)
									if (!ckrto.passed) {
										invalid = ckrto.invalid
										kj = ipt
										return false;//跳出each循环
									}
								}else{//是日期类型
									var rqd = ipt.attr("rqd")
									if ("true"==rqd && isEmptyStr(ipt.val())){
										invalid = ipt.attr("btm")+" 的值不能为空！";
										kj = ipt
										return false;
									}
								}
							}else if (attype=='radio' || attype=='checkbox'){//目前只检查必填
								var rqd = ipt.attr("rqd")
								var cklen = ipt.siblings("input[type='"+attype+"']:checked").length+ipt.prop("checked")?1:0
								if ("true"==rqd && cklen<=0){
									invalid = '请选择 '+ipt.attr("btm")+" 的值！";
									kj = ipt
									return false;
								}
							}else{
								invalid = '未知input类型: '+attype
								kj = ipt
								return false;
							}
						}else if (td.children(".uecontainer").length>0){//是富文本编辑框，目前就只校验一个长度
							var rich = td.children(".uecontainer")
							var cls = rich.attr('class')
							var rid = rich.attr('id')
							var idxr = cls.indexOf('required')
							var ueobj = UE.getEditor(rid)
							var clen = ueobj.getContentLength()
							if (idxr != -1 && clen<=0){//检查必填
			                   	invalid = cls[idxr+8]==':'?cls.substring(idxr+11,cls.indexOf(' ', idxr+11)):'请输入内容的值';
			                   	kj = ueobj
			                    return false;
							}
							var idxmx = cls.indexOf('max')
							var maxlen = parseInt(cls.substring(idxmx+4,cls.indexOf(' ',idxmx+4)))
							if (clen> maxlen){//如果填了就要检查长度
								var mc = rich.parent().prev().text()
			                   	invalid = '【'+mc+'】字段超出最大保存长度(最大'+maxlen+'，当前'+clen+')，请检查输入内容！'
			                   	kj = ueobj
			                    return false;
							}
						}else if (td.attr("class") && td.attr("class").indexOf("upload")!=-1){//是图片或视频
							var cls = td.attr('class')
							if (cls.indexOf('required') != -1 && td.next().text().indexOf('未上传')!=-1){
								var mc = td.prev().text()
			                   	invalid = '【'+mc+'】必须上传'
			                   	kj = td.find("input[type='file']")
			                    return false;
							}
						}else if (td.children(".clstextarea").length>0){//textarea类型，和input不一样的就是，maxlength属性旧的浏览器不支持
							//这边先判断一下长度
							var tao = td.children(".clstextarea")
							var mc = tao.parent().prev().text()
			 				var len = tao.val().length
			 				var maxlen = parseInt(tao.attr("maxlength"))
							if (len>maxlen){
								invalid = '【'+mc+'】字段超出最大保存长度(最大'+maxlen+'，当前'+len+')，请检查输入内容！'
								kj = tao
								return false;
							}
							//再判断一下data-validate
							var ckrto = checkdv(ipt)
							if (!ckrto.passed) {
								invalid = ckrto.invalid
								kj = tao
								return false;
							}
						}else if (td.children(".clstreerdo").length>0){//是单选树
							var eletree = td.children(".clstreerdo")
							var ckrto = checkdv(eletree)
							if (!ckrto.passed) {
								invalid = ckrto.invalid
								kj = eletree
								return false;//跳出each循环
							}
						}
					})
					if(invalid!=""){
						layer.msg(invalid)
						if (kj) kj.focus()
						return
					}
	
					//这里留个接口，可以在系统的保存逻辑之后做自定义的校验

				}else{//是暂存，只校验必填
					var kj,invalid="";
					$(".list_table>tbody>tr:not(:last)").each(function(){
						var first = $(this).children("td:eq(0)");
						if (first && first.children("img[title='必填']").length>0){
							var td = $(this).children("td:eq(1)");
							if (td.children(".clsinput:first").length>0 && td.children(".clsinput:first").val().length<=0){//是input
								invalid = "请输入"+first.children("b:first").text()+"的值！";
								kj = td.children(".clsinput:first");
								return false;
							}else if (td.children(".uecontainer").length>0){
								var ueobj = UE.getEditor( td.children(".uecontainer").attr("id"))
								if (ueobj.getContentLength()<=0){
				                    invalid = "请输入"+first.children("b:first").text()+"的值！";
				                   	kj = ueobj
				                   	return false;
								}
							}else if (td.attr("class") && td.attr("class").indexOf("upload")!=-1){//是图片或视频
								var cls = td.attr('class')
								if (td.next().text().indexOf('未上传')!=-1){
									var mc = td.prev().text()
				                   	invalid = '【'+mc+'】必须上传'
				                   	kj = td.find("input[type='file']")
				                    return false;
								}
							}else if (td.children(".clstextarea").length>0){
								var tao = td.children(".clstextarea")
								var mc = tao.parent().prev().text()
				 				var len = tao.val().length
								if (len<=0){
									invalid = "请输入"+first.children("b:first").text()+"的值！";
									kj = tao
									return false;
								}
							}else if (td.children(".clstreerdo").length>0){
								var eletree = td.children(".clstreerdo")
								var ckrto = checkdv(eletree)
								if (!ckrto.passed) {
									invalid = ckrto.invalid
									kj = eletree
									return false;//跳出each循环
								}
							}
						}
					})
					if(invalid!=""){
						layer.msg(invalid)
						if (kj) kj.focus()
						return;
					}
				}

				//logo = $("#idupimg").length>0?$("#idupimg").attr("src"):null;
				//alert($("#id_ae_form").serialize())
				
				//添加上图片上传的数据
				var dataex = "&u="+'${o.u}'+(!___zc?"":("&___zc="+___zc))
				$(".upload").each(function(){
					var zdm = $(this).attr("zdm")
					var ntd = $(this).next();
					if (ntd.data("vdodz")!=null){
						dataex += "&"+zdm+"="+ntd.data("vdodz")
					}else{
						var eimg = $(this).next().children("img")
						dataex += "&"+zdm+"="+(eimg.length>0?eimg.attr("src"):"")
					}
				})
				var zdyaev = '${o.aev.zdybc}'
				var ajyrl = isEmptyStr(zdyaev)?"addedit.dhtml?u=${o.u}":(zdyaev+".dhtml")

				//添加富文本编辑器的纯文本，目前好像只想到用于搜索，不然会搜索带样式的内容，逻辑上有漏洞
				$("#id_ae_form .uecontainer").each(function(index){
					var ued = UE.getEditor($(this).attr("id"));
					var enm = $("#id_ae_form>textarea:eq("+index+")").attr("name")
					if (enm ){
						if ($("#id_ae_form>input[name='"+enm+"_wb"+"']").length>0) $("#id_ae_form>input[name='"+enm+"_wb"+"']").val(ued.getContentTxt())
						else $("#id_ae_form").prepend("<input type='hidden' name='"+enm+"_wb"+"' value='"+ued.getContentTxt()+"'/>")
					}else{
						layer.alert('uediter保存异常错误，请联系管理员！')
						return;
					}
		   		})

				//clstreerdo的值要另外取
				$("#id_ae_form .clstreerdo").each(function(){
					dataex += "&"+$(this).attr("name")+"="+$(this).data("___id")
				})

				$.ajax({
			          type:"POST",
			          url:ajyrl,
			          data:$("#id_ae_form").find(":not(.clstreerdo)").serialize()+dataex,
			          datatype: "JSON",
			          beforeSend:function(){layer.msg('正在保存......', {time:-1,icon: 16,shade: 0.3, scrollbar:false});},
			          success:function(json){
			        	  	var rtn = eval("("+json+")")
							if (rtn.ret=="0"){
								if (!___zc && !_epp(rtn.js_aft_bc)) eval(rtn.js_aft_bc)//这里的extjs对应aev.java中的js_aft_bc
								if ('${o.r.tree.tb}'==''){
									if(___zc!=2) location.href='${o.r.listloc}'
									else {
										if ('${lx}'=='0' && rtn.zcnewid){//是新增
											if ($("#id_ae_form>input[name=id]").length<=0)$("#id_ae_form").append("<input type='hidden' name='id' value='"+rtn.zcnewid+"'/>")
											else $("#id_ae_form>input[name=id]").val(rtn.zcnewid)
										}
										layer.msg("暂存成功！")
									}
								}else {
									parent.layer.msg(rtn.tip)
									var index = parent.layer.getFrameIndex(window.name);//获取窗口索引
									parent.layer.close(index);
								}
							}else if (rtn.ret=="-1"){
								layer.alert('保存失败，请联系管理员！', { icon:2, title:'错误'});
							}else{
								layer.alert(rtn.ret, { icon:2, title:'错误'});
							}
			          },
			          error: function(){
						layer.alert('保存失败，请重试！', { icon:2, title:'错误'});
			          }         
				});
			}
		
			//上传图片或视频
			function fn_sc(tag){
				var iptf = $(tag).prev()
				if (isEmptyStr(iptf.val())){
					layer.msg('请先选择要上传的文件！')
					return
				}
				var options = {
			            dataType : "JSON",
			            beforeSubmit : function() {
			            	layer.msg('正在上传，请稍后......', {time:-1,icon: 16,shade: 0.3, scrollbar:false});
			            },
			            success : function(json) {
			            	if(json.ret=="0"){
			            		layer.msg('上传成功！');
			            		var apd = ($(tag).hasClass('clstag___VDO'))?('已上传视频：'+json.orimc+'('+json.size+')'):"<img src='"+json.src+"'></img>";
				            	$(tag).parent().parent().next().empty().data("vdodz",json.src).append(apd)
			            	}else{
			            		fn_qk($(tag).next());
			            		layer.msg(json.ret);
			            	}
			            },
			            error : function(XmlHttpRequest, textStatus, errorThrown) {
			            	layer.msg("上传失败("+textStatus+")"+"，请刷新重试！");
			            }
			        }
					var td = $(tag).parent()
					if (td.is('td')){
			        	var ipts = td.children(":lt(4)")
			        	td.prepend("<form action='ajaxupload.dhtml'  method='post' enctype='multipart/form-data'></form>")
			        	var form = td.children("form")
			        	form.append(ipts).ajaxSubmit(options)
					}else{//如果form元素已经存在了，则不用再套一层了，这是网友反映的问题
						td.ajaxSubmit(options)
					}
				}
		
				function fn_qk(tag){
					var obj = $(tag).prev().prev()
					obj.prop('outerHTML', obj.prop('outerHTML'))
					var tp=($(tag).hasClass('clstag___VDO'))?"未上传视频":"未上传图片";
					if ($(tag).parent().is("form")){
						$(tag).parent().parent().next().empty().append(tp)
					}else{
						$(tag).parent().next().empty().append(tp)
					}
				}
   		</c:if>

		;<c:if test="${!o.r.need}">//无list页面，直接配置aev页面
			$(function(){
				$("#id_ae_form .uecontainer").each(function(idx){
					var e = UE.getEditor($(this).attr("id"));
					e.addListener('ready',function(){  
			   	    	e.setDisabled();//ready后才能设置disabled
			   		});
				})
				$("#id_ae_form .clsinput,#id_ae_form .clstextarea, #id_ae_form .clsiptbtn").each(function(idx){
					$(this).attr("disabled","disabled").attr("readonly", "readonly");
				})
			})

			;function fn_bj(tag){
				var wz = $(tag).val();
				if ("编辑"==wz){
					$(tag).val("保存")
					$(tag).next().show()
					$("#id_ae_form .uecontainer").each(function(idx){
						var e = UE.getEditor($(this).attr("id"));
						e.setEnabled();
					})
					$("#id_ae_form .clsinput,#id_ae_form .clstextarea, #id_ae_form .clsiptbtn").each(function(idx){
						$(this).removeAttr("disabled readonly");
					})
					$("#id_ae_form .list_table>tbody>tr:eq(0)").children("td:eq(1)").children(":eq(0)").focus()
				}else{
					fn_bc();
				}
			}

			function fn_qx(){
				location.reload();
			}
		</c:if>

		;<c:if test="${!empty(havezdb)}">
			function fn_zdbchange(tag,maxlen, zdm, subv){
				var val = $(tag).children("option:selected").text();
				if (isEmptyStr(val)){
					$(tag).next().empty().show();
					$(tag).next().next().val('').hide();
					//$(tag).next().find("option:first").attr('selected','selected');
				}else if ("自定义" == val){
					$(tag).next().removeAttr('name').hide();
					var cdsize = $(tag).parent().children().size();
					if (cdsize==2) {
						var diss = "";
						if ('${lx==2}'=='true') diss=" disabled='disabled' ";
						$(tag).parent().append("<input "+diss+"value='"+subv+"' type='text' style='width:50% !important' class='input-text lh25' maxlength='"+maxlen+"' name='"+zdm+"' />");
					}
					else {
						$(tag).parent().children(":last").show().attr("name",zdm);
					}
				}else{
					$(tag).next().show();
					var eipt = $(tag).next().next();
					eipt.hide();
					if (isEmptyStr($(tag).next().attr('name'))) {
						$(tag).next().attr('name',eipt.attr('name'));
						eipt.removeAttr('name');
					}
					$.ajax({
				          type:"POST",
				          url:"zdbchange.dhtml",
				          data:{id:$(tag).val()},
				          datatype: "JSON",
				          //beforeSend:function(){layer.msg('正在获取字典项......', {time:-1,icon: 16,shade: 0.3, scrollbar:false});},  
				          success:function(json){
				        	 	layer.closeAll();
				        	  	var list = eval("("+json+")")
								if (list && list.length>0){
									var selzdx = $(tag).next()
									selzdx.empty();
									var zdbfv = $(tag).find("option:first").val();
									if (isEmptyStr(zdbfv)) selzdx.append("<option value=' '> </option>");
									for (var i=0;i<list.length;i++){
										var tps = "<option value='"+list[i].val+"'";
										if ((subv && list[i].val==subv) || (!subv && i==0)) tps+=" selected "; 
										selzdx.append(tps+">"+list[i].txt+"</option>");
									}
								}else{
									layer.alert('操作失败，请联系管理员！', { icon:2, title:'错误'});
								}
				          },
				          error: function(){
							layer.alert('字典项获取失败，请刷新重试！', { icon:2, title:'错误'});
				          }      
					})
				}
			}

			function fn_clkzdx(tag){
				if ($(tag).html().length==0){
					layer.msg('请先选择字典表！');
					$(tag).prev().focus();
				}
			}

			function fn_cgzdx(tag){
				var val = $(tag).val()
				if (isEmptyStr(val)){
					$(tag).prev().find("option:first").attr('selected','selected');
				}
			}
		</c:if>
	</script>
</body>
</html>
       