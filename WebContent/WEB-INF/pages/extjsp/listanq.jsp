<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>


<c:if test="${srcfrom=='pzlc'}"><script type="text/javascript" src="${path}/js/jquery.tjpinputtree.js?___v=<%=Math.random()%>"></script></c:if>

<script>
$(function(){
	var cnt = parseInt('${fn:length(o.r.tbs)}')
	if (isNaN(cnt) || cnt<=0){
		$(".cls_yidu").css({
			filter:"alpha(opacity='20')",
			opacity:'0.2',
			"-moz-opacity":'0.2',
			"-khtml-opacity":'0.2',
		})
	}else{
		$(".cls_yidu").css({
			filter:"alpha(opacity='')",
			opacity:'',
			"-moz-opacity":'',
			"-khtml-opacity":'',
		})
	}

	//初始化时把角色下拉框和px的功能动态添加进来
	if ('${srcfrom}'=='pzlc'){//只对指定的页面做指定的初始化，不然只要引入了listanq.jsp就是要执行初始化不太好
		var esel = $('<input placeholder="点击选择角色" name="jsmc" class="input-text" style="margin-right:10px;text-indent: 5px;" readonly="readonly">');
		esel.tjpinputtree( $.parseJSON('${jstree}'));//好像比eval方便点，具体没看源码
		$(".btns").prepend(esel);
	}
})

//本页已读
function fn_op_yidu(quanbu){
	var cnt = parseInt('${fn:length(o.r.tbs)}')
	if (isNaN(cnt) || cnt<=0){
		layer.msg('本页已无未读消息！')
	}else{
		fn_ntp_aj("aj_wdxx_yd.dhtml", {u:'${o.u}', quanbu:quanbu}, 
			function(){
				parent.refreshWdxx('${fn:length(o.r.tbs)}');
				location.href='wdxx.dhtml';
			})
	}
}

//这个是配置流程页面的新增按钮，因为字段少，就不走aev了，直接添加
function fn_pzlc_xinzeng(tag,lcid){
	if (_epp($(tag).prev().val())){layer.msg('请先选择角色！');$(tag).prev().trigger('click');return;}
	fn_ntp_aj("aj_pzlc_addjs.dhtml",{id:$(tag).prev().data("___id"),lcid:lcid}, function(){shuaxin();})
}

//配置流程里，局部刷新成功后把排序改成input
function fn_pzlc_jsaftsx(){
		$(".list_table>tbody>tr").each(function(){
			var etd = $(this).children("td:eq(-2)");
			var px = etd.text();
			etd.empty().append("<input maxlength='3' type='text' value='"+px+"' class='clsiptpx input-text' style='text-indent:0;text-align:center;height:24px'/>").css("padding","0 8px")
		})
		$(".clsiptpx").focus(function(){
			$(this).data("pxval", $(this).val())
		})
		$(".clsiptpx").blur(function(){
			if($(this).data("pxval")==$(this).val()) return;
			if (! /^-?[1-9]\d*$/.test($(this).val())){
				layer.msg('【排序】请输入整数！')
				$(this).val($(this).data("pxval"));
				$(this).select().focus()
				return;
			}
			fn_ntp_aj("aj_pzlc_xgpx.dhtml",{id:$(this).parent().parent().children("td:eq(0)").children('input').attr("id"),px:$(this).val(),lcid:'${lcid}'},function(){shuaxin();})
		})
}


</script>		
