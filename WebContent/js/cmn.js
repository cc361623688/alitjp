function isEmptyStr(str) {
	return !str || str.replace(/(^\s+)|(\s+$)/g, "").length == 0;
}

function _epp(str) {
	return isEmptyStr(str)
}

$(document).keydown(function (e) {//阻止backspace返回上一页面
	var doPrevent = false;
	var e = e || window.event;
	var varkey = (e.keyCode) || (e.which) || (e.charCode)
    if (varkey == 8) {
    	doPrevent = true;
    	var d = e.srcElement ||e.target
    	var tag = d.tagName.toUpperCase()
     	var tp = d.type || $(d).attr("type")
     	if (tp) tp = tp.toUpperCase()
        if ((tag == 'INPUT' || tag == 'TEXTAREA'|| tag == 'PASSWORD') 
        	&& !($(d).attr("readonly") || $(d).attr("disabled"))
        	&& !(!tp || tp=='BUTTON' || tp=='RADIO' || tp=='CHECKBOX' || tp=='SUBMIT')) {
        	doPrevent = false
      	}
    }

     if (doPrevent) {
    	e.preventDefault();  
    	e.stopPropagation(); 
    }
});

function $emptyplaceholder(element) {
    var $content = $(element).val();
    return ($content.length === 0) || $content == $(element).attr("placeholder");
};

function initPhd(){
    if (!('placeholder' in document.createElement('input'))) {
        $("textarea[placeholder], input[placeholder]").each(function (index, element) {
            if (!$(element).hasClass('donot_initphd') && $(element).attr("placeholder") || $emptyplaceholder(element)) {
                $(element).val($(element).attr("placeholder"));
                $(element).data("pintuerholder", $(element).css("color"));
                $(element).css("color", "rgb(169,169,169)");
                $(element).focus(function () { $hideplaceholder($(this)); });
                $(element).blur(function () { $showplaceholder($(this)); });
            }
        })
    };
}


$(function(){
	initPhd()
})

function $hideplaceholder(element) {
    if ($(element).data("pintuerholder")) {
        $(element).val("");
        $(element).css("color", $(element).data("pintuerholder"));
        $(element).removeData("pintuerholder");
    }
}
function $showplaceholder(element) {
    if (($(element).val().length === 0 || $(element).val() == $(element).attr("placeholder")) && $(element).attr("type") != "password") {
        $(element).val($(element).attr("placeholder"));
        $(element).data("pintuerholder", $(element).css("color"));
        $(element).css("color", "rgb(169,169,169)");
    }
};

function fn_tp_aj(tp,url,data,m0a1n2){
	layer.confirm(tp, {icon: 3, title:'提示'}, function(index){
		fn_ntp_aj(url,data,m0a1n2);
	});
}

function fn_ntp_aj(url,data,m0a1n2,fail){
	$.ajax({
		type:"POST",
		url:url,
		data:data,
		beforeSend:function(){layer.msg('操作中，请稍后......', {time:-1,icon: 16,shade: 0.3, scrollbar:false});},  
		datatype: "text",
		success:function(ret){
			layer.closeAll();
			if ("0" ==ret){
				if(!m0a1n2 || m0a1n2==0){
					layer.msg("操作成功！")
				}else if (m0a1n2==1){
					layer.alert("操作成功！",function(index){location.reload();});
				}else if(typeof(m0a1n2)=='function'){
					m0a1n2()
				}else{
					
				}
			}else if (typeof(fail)=='function'){
				fail(ret)
			}else if ("-1"==ret){
				layer.alert('操作失败，请联系管理员！', { icon:2, title:'错误'});
			}else{
				layer.alert(ret, { icon:2, title:'错误'});
			}
		},
		error: function(){
			layer.alert('操作失败，请稍后重试！', { icon:2, title:'错误'});
		}
	});
}

function fn_ntp_json(url,paraobj,m0a1n2,fail,cfg){
	var wtnid=null;//这。。后面异步还能读的到吗？
	cfg = cfg||{clsA:true}
	$.ajax({
		type:"POST",
		url:url,
		data:paraobj,
		beforeSend:function(){wtnid=layer.msg('操作中，请稍后......', {time:-1,icon: 16,shade: 0.3, scrollbar:false});},  
		datatype: "JSON",
		success:function(json){
			if (cfg.clsA)layer.closeAll();
			var data = eval("("+json+")")
			if ("0" ==data.ret){
				if(!m0a1n2 || m0a1n2==0){
					layer.msg("操作成功！")
				}else if (m0a1n2==1){
					layer.alert("操作成功！",function(index){location.reload();});
				}else if(typeof(m0a1n2)=='function'){
					m0a1n2(data)
				}else{
					
				}
			}else if (fail!=null && typeof(fail)=='function'){
				layer.close(wtnid)
				fail(data)
			}else if ("-1"==data.ret){
				layer.msg('操作失败，请联系管理员！', { icon:2, title:'错误'});
			}else{
				layer.msg(data.ret, { icon:2, title:'错误'});
			}
		},
		error: function(){
			layer.alert('操作失败，请稍后重试！', { icon:2, title:'错误'});
		}
	});	
}

function fn_aj_async(url){
	var ret = null;
	$.ajax({//同步的
        type:"POST",async: false,
        url:url,
        datatype: "text",
        beforeSend:function(){layer.msg('操作中，请稍后......', {time:-1,icon: 16,shade: 0.3, scrollbar:false});},  
        success:function(text){
        		layer.closeAll();
        		ret = text;
        },
        error: function(){
          	layer.alert('网络阻塞，请重试！', { icon:2, title:'错误'});
		  }
	})

	return ret;
}


//弄个参数最全的，但是不保证后面还有参数，url和data必须有，当然data可以为空，剩余的参数都用对象传递
function aj(url,data,para){
	
	function _aj(url,data,para){
		$.ajax({
			type:para.type?para.type:"POST",//默认post
			url:url,
			data:data,
			timeout:para.timeout?para.timeout:0,
			beforeSend:!para.beforeSend?function(){layer.msg('操作中，请稍后......', {time:-1,icon: 16,shade: 0.3, scrollbar:false});}:para.beforeSend,  
			datatype: para.datatype?para.datatype:"JSON",//默认json
			success:function(json){
				if (!para.beforeSend)layer.closeAll();
				var rtn = eval("("+json+")")
				if ("0" ==rtn.ret){
					if(!para.succ){
						layer.msg("操作成功！")
					}else if(typeof(para.succ)=='function'){
						para.succ(rtn)
					}else{
						layer.alert("操作成功！",function(index){location.reload();});
					}
				}else if (para.fail!=null && typeof(para.fail)=='function'){
					para.fail(rtn)
				}else if ("-1"==rtn.ret){
					layer.alert(!para.neg1?'操作失败，请联系管理员！':para.neg1, { icon:2, title:'错误'});
				}else{
					layer.alert(ret, { icon:2, title:'错误'});
				}
			},
			error: function(){
				if (para.error && typeof(para.error)=='function'){
					para.error()
				}else{
					layer.alert(!para.error?'操作失败，请重试！':para.error, { icon:2, title:'错误'});
				}
			},
			complete:function(XMLHttpRequest, textStatus){
				if (para.complete){
					para.complete(XMLHttpRequest, textStatus)
				}
			}
		});
	}
	
	if (para && para.confirm){
		layer.confirm(para.confirm, {icon: 3, title:'提示'}, function(index){
			_aj(url,data,para)
		});
	}else{
		_aj(url,data,para)
	}
}



//获取某个节点下的所有子节点
function getAllYeziNodes(treeNode, yeziarr) {
	if (treeNode.isParent) {
		var childrenNodes = treeNode.children;//加var代表是局部变量
		if (childrenNodes) {
			for (var x = 0; x < childrenNodes.length; x++) {//加var代表是局部变量
				if (childrenNodes[x].isParent) {
					getAllYeziNodes(childrenNodes[x], yeziarr);
				} else {
					yeziarr.push(childrenNodes[x])
				}
			}
		}
	}
	
	return;
}


