
//这里根据实际的业务需求，对新增或编辑页面中的字段做自定义的校验，如果通过返回true，会继续系统的校验，返回false则停止保存。
function fn_test_jy1(pa){
	console.log("这是保存前校验"+pa)
	return true;
}

//这个和上面的区别是，这里是系统校验后调用
function fn_test_jy2(){
	console.log("这是保存后校验");
	return true;
}

$(function(){
	var list=document.getElementsByTagName('script');
	for(var i=0;i<list.length;i++){
		if (list[i].src.indexOf("extaevjs.js?")==-1) continue;//不是extaevjs.js就不管

		if (list[i].src.indexOf("s=yhgl")!=-1){//不是新增
			if (getAevLx()!='0'){
				$("input[name='yhm'], select[name='jsid']").attr("disabled", "disabled")
				$("input[name='mm'],input[name='qrmm']").remove()
			}
		}else if (list[i].src.indexOf("s=___zscgshzt")!=-1){
			___fn_zszg_selcg()
		}
		break;
	}
	
})


//正式超管的编辑时审核功能，进行前台校验
function ___cmn_zscg_extsh(){
	var shzt = $("#id_ae_form select[name='shzt']").val()
	var eshyj = $("#id_ae_form input[name='shyj']")
	if (("审核不通过"==shzt || "退回修改"==shzt) && _epp(eshyj.val())){
		layer.msg('请填写【'+shzt+'】的理由！')
		eshyj.focus();
		return false;
	}
	
	return true;
}

//这个是为了显示审核意见的必填标志，就是那个	★
function ___fn_zszg_selcg(){
	var eshzt = $("#id_ae_form select[name='shzt']")
	eshzt.parent().prev().children("img[title='必填']").remove()
	if ("审核不通过"==eshzt.val() || "退回修改"==eshzt.val()){
		eshzt.parent().prev().append('<img title="必填" src="/alitjp/img/star.png" style="width:10px;vertical-align:top">')
	}
}